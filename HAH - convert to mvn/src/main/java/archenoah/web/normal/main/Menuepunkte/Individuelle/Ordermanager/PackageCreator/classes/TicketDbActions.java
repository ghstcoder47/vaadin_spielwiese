package archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.PackageCreator.classes;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.vaadin.Components.ClickMenuBar.ClickCommand;
import archenoah.web.normal.UserInfo.UserData;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.OpenTickets.classes.TITYPE;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.OpenTickets.form.OpenTicketsInsertEdit;

import com.google.common.base.Joiner;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window.CloseListener;

public class TicketDbActions {


    // {section fields}
    // ****************
    private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    private Date dateStart;
    private Date dateEnd;
    private Date dateShipping;
    private Integer userId;
    private Integer patientId;
    private String countryKey;
    private Integer originalTicketId;
    private Map<Integer, Collection<Integer>> virtualIds;
    private ArrayList<Integer> contentIds;
    private Container standaloneContainer;
    
    // {end fields}

    // {section constructors}
    // **********************
    public TicketDbActions() {
        
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    
    public void setTicketData(Date dateStart, Date dateEnd, Date dateShipping, Integer userId, Integer patientId, String countryKey) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.dateShipping = dateShipping;
        this.userId = userId;
        this.patientId = patientId;
        this.countryKey = countryKey;
    }
    
    public void setTicketData(Item item) {
        
        setTicketData(MyUtils.getValueFromItem(item, "OMT_DATE_FROM", Date.class),
                MyUtils.getValueFromItem(item, "OMT_DATE_UNTIL", Date.class),
                MyUtils.getValueFromItem(item, "OMT_DELIVERY_DATE", Date.class), 
                MyUtils.getValueFromItem(item, "OMT_ID_USER", Integer.class),
                MyUtils.getValueFromItem(item, "OMT_ID_PATIENT", Integer.class),
                MyUtils.getValueFromItem(item, "OMT_KEY_COUNTRY", String.class));
    }
    
    public Map<Integer, Collection<Integer>> getVirtualIds() {
        return virtualIds;
    }
    /**
     * 
     * @param contentsIds <projectId, dataId>
     */
    public void setVirtualIds(Map<Integer, Collection<Integer>> contentsIds) {
        this.virtualIds = contentsIds;
    }
    
    public void setVirtualIds(Container con, String projectPropertyId, String dataPropertyId) {
        
        Map<Integer, Collection<Integer>> map = new HashMap<Integer, Collection<Integer>>();
        
        for (Object iid : con.getItemIds()) {
            
            Integer projectId = getIntFromContainer(con.getContainerProperty(iid, projectPropertyId));
            Integer dataId = getIntFromContainer(con.getContainerProperty(iid, dataPropertyId));
            
            if(projectId == null || dataId == null) {
                continue;
            }
            
            // create project level if not exists
            if(map.get(projectId) == null) {
                map.put(projectId, new ArrayList<Integer>());
            }
                
            // add to projectIds
            map.get(projectId).add(dataId);
            
        }
        
        setVirtualIds(map);
        
    }
    
    public void setVirtualIds(ArrayList<Item> contents, String projectPropertyId, String dataPropertyId) {
        
        Map<Integer, Collection<Integer>> map = new HashMap<Integer, Collection<Integer>>();
        
        for (Item item : contents) {
            
            Integer projectId = getIntFromContainer(item.getItemProperty(projectPropertyId));
            Integer dataId = getIntFromContainer(item.getItemProperty(dataPropertyId));
            
            if(projectId == null || dataId == null) {
                continue;
            }
            
            // create project level if not exists
            if(map.get(projectId) == null) {
                map.put(projectId, new ArrayList<Integer>());
            }
                
            // add to projectIds
            map.get(projectId).add(dataId);
            
        }
        
//        log.info("map {}", map);
        
        setVirtualIds(map);
        
    }
    
    public void setStandaloneContents(Container standaloneContainer) {
        this.standaloneContainer = standaloneContainer;
    }
    
    public Container getStandaloneContainer() {
        return standaloneContainer;
    }
    
    public Integer getOriginalTicketId() {
        return originalTicketId;
    }

    public void setOriginalTicketId(Integer originalTicketId) {
        this.originalTicketId = originalTicketId;
    }

    public ArrayList<Integer> getContentIds() {
        return contentIds;
    }
    
    public void setContentIds(Collection<Integer> ids) {
        contentIds = new ArrayList<Integer>(ids);
    }
    
    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Date getDateShipping() {
        return dateShipping;
    }

    public void setDateShipping(Date dateShipping) {
        this.dateShipping = dateShipping;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
        this.patientId = null;
    }
    
    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
        this.userId = null;
    }
    
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    
    public HashMap<Integer, String> getAvailableTickets() {
        return dbGetAvailableTickets();
    }
    
    public Boolean contentsMove(Integer newTicketId) {
        
        if(contentIds == null || contentIds.size() == 0) {
            return false;
        }
        
        if(newTicketId == null || newTicketId == 0) {
            return false;
        }
        
        return (dbMoveContents(newTicketId) != 0);
    }
    
    public Boolean contentsDelete() {
        
        if(contentIds == null || contentIds.size() == 0) {
            return false;
        }
        
        return (dbDeleteContents() != 0);
    }
    
    
    public Integer ticketCreate() {
        return dbCreateTicket();
    }
    
    public static class CreateParams{
        public Date date;
        public Date shippingDate;
        public Integer patientId;
        public Integer userId;
        public String country;
        public MenuItem selectedItem;
        public Button button;
        public CloseListener closeListener;
        public MenuBar parent;
    }
    
    public Integer createAndOpen(final CreateParams params) {
        return createAndOpenWin(params).getTicketId();
    }
    
    public OpenTicketsInsertEdit createAndOpenWin(final CreateParams params) {
        
        HashSet<String> countries = UserData.get().getCountry().getCountries();
        
        String cnt = null;
        if(params.country == null && countries != null && countries.size() > 0) {
            cnt = countries.iterator().next();
        }else {
            cnt = params.country;
        }
        
        if(params.date == null) {
            params.date = new Date();
        }
        setTicketData(params.date, params.date, params.shippingDate, params.userId, params.patientId, cnt);
        
        final OpenTicketsInsertEdit pie;
        ClickCommand click = null;
        Integer ticketId = null;
        
        if(params.selectedItem != null) {
            ticketId = dbCreateTicket();
            pie = new OpenTicketsInsertEdit(params.selectedItem, ticketId.toString());
            if(params.parent != null) {
                click = new ClickCommand() {
                    @Override
                    public void menuClicked(MenuItem selectedItem) {
                        this.setWindow(pie.getWindow());
                        this.setParent(params.parent);
                    }
                };
            }
        }else if(params.button != null) {
            ticketId = dbCreateTicket();
            pie = new OpenTicketsInsertEdit(params.button, ticketId.toString());
        }else {
            return null;
        }
         
        pie.setPermissionsFromDatabase();
        pie.init();
        if(params.closeListener != null) {
            pie.getWindow().addCloseListener(params.closeListener);
        }
        
        if(click != null) {
            click.menuClicked(params.selectedItem);
        }
        
        pie.getWindow().setClosable(false);
        
        return pie;
    }
    
    public Integer ticketCreateBlank() {
        Integer nid = getUserId();
        setUserId(null);
        Integer tid = dbCreateTicket();
        setUserId(nid);
        
        return tid;
    }
    
    public Boolean contentsCreate(Integer ticketId) {
        
        return (dbCreateContents(ticketId) != null);
        
    }
    
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    
    private Integer getIntFromContainer(Property property) {
        
        Integer res = null;
        
        if(property != null){
            
            if(property.getType() == BigDecimal.class && property.getValue() != null) {
                res = ((BigDecimal) property.getValue()).intValueExact();
            }
            
            if(property.getType() == Long.class && property.getValue() != null) {
                res = ((Long) property.getValue()).intValue();
            }
            
            if(property.getType() == Integer.class) {
                res = (Integer) property.getValue();
            }
            
        }
        
        return res;
    }
    
    private String formatDate(Date date){
        SimpleDateFormat fmt = new SimpleDateFormat("YYYY-MM-dd");
        return (date != null) ? fmt.format(date) : null;
    }
    // WHERE country = 'DE' and ( accepted = 1 AND nurse_id = 183 AND `date` BETWEEN '2016-09-19' AND '2016-10-02')
    public String getCondition(){
        
        String condition = "WHERE accepted = 1 AND ";

        if(virtualIds != null && virtualIds.size() > 0) {
            ArrayList<String> cnd = new ArrayList<String>();
            
            for (Entry<Integer, Collection<Integer>> entry : virtualIds.entrySet()) {
                cnd.add("(project = " + entry.getKey() + " AND project_data_id IN(" + Joiner.on(", ").join(entry.getValue()) + "))");
            }
            
            condition += "(" + Joiner.on(" OR ").join(cnd) + ")"; 
        }else {
            condition += "nurse_user_id = " + userId
                    + " AND `date` BETWEEN '" + formatDate(dateStart) + "' AND '" + formatDate(dateEnd) + "'";
        }
        
        
        return condition;
    } 
    
    /**
     * 
     * @return null if none, ArrayList of ticketIds otherwise
     */
    @SuppressWarnings("unchecked")
    private HashMap<Integer, String> dbGetAvailableTickets() {
        
        HashMap<Integer, String> map = null;
        
        DBClass db = new DBClass();
        
        Integer statusLimit = MyUtils.getUserData().getNurse().isNurse() ? 5 : 15;
        
        String sql = "SELECT"
                + " \n" + " OMT_ID"
                + " \n" + ", OMT_DELIVERY_DATE"
                + " \n" + ", CONCAT(IF(AUL_NAME IS NOT NULL and AUL_VORNAME IS NOT NULL," 
                + " \n CONCAT(AUL_VORNAME, ' ', AUL_NAME, ' '), ''), '(#', OMT_ID, ')') as caption"
                
                + " \n" + "from cust_ordermanager_tickets"
                + " \n" + "left join cms_auth_stammdaten_user on OMT_ID_USER = AUL_ID"
                + "\n " + "where OMT_ID_STATUS <= " + statusLimit
                        
                        + ( originalTicketId != null ? " AND OMT_ID != " + originalTicketId : "")
                        + " AND OMT_ID_USER = " + userId + ""
                        + " AND OMT_DATE_FROM = '"+formatDate(dateStart)+"'"
                        + " AND OMT_DATE_UNTIL = '"+formatDate(dateEnd)+"'"
                        + " AND OMT_KEY_COUNTRY = '"+countryKey+"'";
        
        db.CustomQuery.setSqlString(sql);
        
//        db.CustomQuery.setSqlString("select OMT_ID,  from"
//                         + "\n " + "cust_ordermanager_tickets"
//                         + "\n " + "where OMT_ID_STATUS <= 15"
//                                 + " AND OMT_ID_USER = " + userId + ""
//                                 + " AND OMT_DATE_FROM = '"+formatDate(dateStart)+"'"
//                                 + " AND OMT_DATE_UNTIL = '"+formatDate(dateEnd)+"'");
        
//        db.debugNextQuery(true);
        Container con = db.CustomQuery.query();
        
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        
        if(con.size() > 0) {
            //((Collection<? extends Integer>) MyUtils.convertIndex(con, "OMT_ID").getItemIds())
            map = new HashMap<Integer, String>();
            
            for (Object iid : con.getItemIds()) {
                
                String dateDelivery = "";
                if(con.getContainerProperty(iid, "OMT_DELIVERY_DATE") != null && con.getContainerProperty(iid, "OMT_DELIVERY_DATE").getValue() != null) {
                   dateDelivery = dateFormat.format(con.getContainerProperty(iid, "OMT_DELIVERY_DATE").getValue()) + " - ";
                }
                
                map.put((Integer) con.getContainerProperty(iid, "OMT_ID").getValue()
                        , dateDelivery
                        + (String) con.getContainerProperty(iid, "caption").getValue());
            }
            
        }
        
        return map;
        
    }
    
    private String buildTicketsSql(){
        
        String tickets = "";
        
        if(userId != null && userId > 0) {
            
            // from nurse prefs
            tickets =  "INSERT INTO cust_ordermanager_tickets"
                    + "\n" + "(OMT_CREATED_ID_USER, OMT_ID_LOCATION, OMT_ID_SHIPPING_COMPANY, OMT_ID_USER, OMT_DELIVERY_DATE, OMT_DATE_FROM, OMT_DATE_UNTIL, OMT_KEY_COUNTRY, OMT_TYPE)"
                    + "\n" + "SELECT "
                    + "\n" + UI.getCurrent().getSession().getSession().getAttribute("Userid") + ", "
                    + "\n" + "OMPR_ID_LOCATION,"
                    + "\n" + "OMPR_ID_SHIPPING_COMPANY,"
                    + "\n" + "OMPR_ID_USER,"
                    + "\n" + ((dateShipping != null) ? "date('"+formatDate(dateShipping)+"')," : "NULL, ") //TASK add more logic (minDate if null)
                    + "\n" + "date('"+formatDate(dateStart)+"'),"
                    + "\n" + "date('"+formatDate(dateEnd)+"'),"
                    + "\n" + (countryKey != null ? "'" + countryKey + "'" : "NULL")+", "
                    + "\n" + "'" + TITYPE.NURSE.name() + "'"
                    + "\n" + "FROM cust_ordermanager_preferences"
                    + "\n " + "WHERE OMPR_ID_USER = " + userId;
            
        }else if(patientId != null){
            
            tickets =  "INSERT INTO cust_ordermanager_tickets"
                + "\n" + "(OMT_CREATED_ID_USER, OMT_ID_PATIENT, OMT_DELIVERY_DATE, OMT_DATE_FROM, OMT_DATE_UNTIL, OMT_KEY_COUNTRY, OMT_TYPE, OMT_ID_STATUS)"
                + "\n" + "VALUES ("
                + "\n" + UI.getCurrent().getSession().getSession().getAttribute("Userid") + ", "
                + "\n" + patientId + ", "
                + "\n" + ((dateShipping != null) ? "date('"+formatDate(dateShipping)+"')," : "NULL, ")
                + "\n" + "date('"+formatDate(dateStart)+"'),"
                + "\n" + "date('"+formatDate(dateEnd)+"'),"
                + "\n" + (countryKey != null ? "'" + countryKey + "'" : "NULL")+", "
                + "\n" + "'" + TITYPE.PATIENT.name() + "'," 
                + "\n" + "5" // set status to order_placed
                + "\n" + ")";
            
        }else {
            
         // generic
            tickets =  "INSERT INTO cust_ordermanager_tickets"
                    + "\n" + "(OMT_CREATED_ID_USER, OMT_DELIVERY_DATE, OMT_DATE_FROM, OMT_DATE_UNTIL, OMT_KEY_COUNTRY, OMT_TYPE, OMT_ID_STATUS)"
                    + "\n" + "VALUES ("
                    + "\n" + UI.getCurrent().getSession().getSession().getAttribute("Userid") + ", "
                    + "\n" + ((dateShipping != null) ? "date('"+formatDate(dateShipping)+"')," : "NULL, ")
                    + "\n" + "date('"+formatDate(dateStart)+"'),"
                    + "\n" + "date('"+formatDate(dateEnd)+"'),"
                    + "\n" + (countryKey != null ? "'" + countryKey + "'" : "NULL")+", "
                    + "\n" + "'" + TITYPE.GENERIC.name() + "',"
                    + "\n" + "10" // set status to order_placed
                    + "\n" + ")";
            
        }

//        log.info("userID: {}, patientId: {}, ticketsSql: {}", userId, patientId, tickets);
        return tickets;
        
    }
    
    //TASK assign contents via IN() condition
    private String buildContentsSql(Integer ticketId){
        
        String contents = "";
        
        if(standaloneContainer != null && standaloneContainer.size() > 0) {
            
            ArrayList<String> values = new ArrayList<String>();
            
            for (Object iid : standaloneContainer.getItemIds()) {
                
                Item item = standaloneContainer.getItem(iid);
                
                if(item == null) {
                    continue;
                }
                
                values.add("("+
                    ticketId
                    + ", " + MyUtils.getValueFromItem(item, "PP_ID_PROJECT", Integer.class)
                    + ", " + MyUtils.getValueFromItem(item, "OMS_ID_PRODUCT", Integer.class)
                    + ", " + MyUtils.getValueFromItem(item, "OMS_ID_PATIENT", Integer.class)
                    + ", " + MyUtils.getValueFromItem(item, "OMS_QUANTITY", Integer.class)
                    + ", " + "1"
                    + ")");
                
            }
            
            contents = "INSERT INTO cust_ordermanager_ticket_contents"
                + "\n" + "(OMTC_ID_TICKET, OMTC_ID_PROJECT, OMTC_ID_PRODUCT, OMTC_ID_PATIENT, OMTC_QTY, OMTC_CUSTOM) VALUES"
                + Joiner.on(", ").join(values);
            
            
            
        }else {
        
            //TASK this joins on first available id for cuplicate user/date tickets, possible subquery solution?
            //fill ticket contents
            contents = "INSERT INTO cust_ordermanager_ticket_contents"
                    + "\n" + "(OMTC_ID_DATA, OMTC_ID_TICKET, OMTC_ID_PRODUCT, OMTC_ID_PROJECT, OMTC_ID_PATIENT, OMTC_QTY)"
                    + "\n" + "SELECT "
                    + "\n" + ""
                    + "\n" + "project_data_id as OMTC_ID_DATA,"
                    + "\n" + ticketId + " as OMTC_ID_TICKET,"
                    + "\n" + "product as OMTC_ID_PRODUCT,"
                    + "\n" + "project as OMTC_ID_PROJECT,"
                    + "\n" + "patient_id as OMTC_ID_PATIENT,"
                    + "\n" + "project_quantity as OMTC_QTY"
                    + "\n" + "FROM ("
                        + "\n" + "SELECT "
                        + "\n" + "*,"
                        + "\n" + "COUNT(status) as status_count,"
                        + "\n" + "COUNT(project_data_id) as max_count"
                        + "\n" + "FROM view_packagemanager_union_complete"
                        + "\n" + "left join cust_ordermanager_preferences on nurse_user_id = OMPR_ID_USER"
                        + "\n" + ""
                        + "\n" + getCondition()
                        + "\n" + "GROUP BY project_data_id, product"
                        + "\n" + "HAVING (status IS NULL OR status_count < max_count)) as sub"
                    + ";";
        
        }
//        log.info("buildContentsSql: {}", contents);
        
        return contents;
        
    }
    
    private int dbMoveContents(Integer newTicketId) {
        
        String sql = "UPDATE cust_ordermanager_ticket_contents"
             + "\n " + "SET OMTC_ID_TICKET = " + newTicketId
             + "\n " + "WHERE OMTC_ID IN(" + Joiner.on(", ").join(contentIds) + ");";
        
        DBClass db = new DBClass();
//        db.debugNextQuery(true);
        db.CustomQuery.setSqlString(sql);
        return db.CustomQuery.update();
    }
    
    private int dbDeleteContents() {
        
        String sql = "DELETE FROM cust_ordermanager_ticket_contents"
             + "\n " + "WHERE OMTC_ID IN(" + Joiner.on(", ").join(contentIds) + ");";
        
        DBClass db = new DBClass();
//        db.debugNextQuery(true);
        db.CustomQuery.setSqlString(sql);
        return db.CustomQuery.update();
    }
    
    private Integer dbCreateTicket() {
        
        Integer tid = null;
        
        ArrayList<Integer> keysTickets = new ArrayList<Integer>();
        
        DBClass db = new DBClass();
        db.CustomQuery.setSqlString(buildTicketsSql());
//        db.debugNextQuery(true);
        db.CustomQuery.update(false, keysTickets);
        
        if(keysTickets.size() > 0) {
            tid = keysTickets.get(0);
        }
        
        return tid;
        
    }
    
    private Integer dbCreateContents(Integer ticketId) {
        
        if(ticketId == null || ticketId == 0) {
            return null;
        }
        
        DBClass db = new DBClass();
        db.CustomQuery.setSqlString(buildContentsSql(ticketId)); 
//        db.debugNextQuery(true);
        return db.CustomQuery.update();
        
    }
    
    // {end privatemethods}

    // {section database}
    // ******************
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
