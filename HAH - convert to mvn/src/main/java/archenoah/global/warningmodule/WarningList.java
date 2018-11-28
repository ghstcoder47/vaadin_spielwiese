package archenoah.global.warningmodule;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.joda.time.LocalDate;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;
import archenoah.lib.vaadin.CustomConverterFactorys.StringSqlTimestampConverter;
import archenoah.lib.vaadin.Language.i18n.I18nEnum;
import archenoah.lib.vaadin.Language.i18n.I18nManager;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.PackageCreator.classes.TicketEvent;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.PackageCreator.form.PackageCreatorDataView;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.UI;

public class WarningList {


    // {section fields}
    // ****************
    
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WarningList.class);
    
    private Integer weeksInAdvance = 3;
    private Integer daysCS = 3;
    
    private Integer userId;
    private String country;
    private Integer patientId;
    private WarningType type;
    
    private LinkedHashMap<WarningType, ArrayList<Warning>> warnings;
    
    private IndexedContainer protocolContainer;
    private IndexedContainer ticketUnconfirmedContainer;
    private BeanItemContainer<TicketEvent> ticketOrderContainer;
    private IndexedContainer ticketOrderNurseContainer;
    
 //    private static I18nManager i18n = new I18nManager(WarningList.class, null,
//        new ArrayList<I18nEnum>());
    
    private static ArrayList<Class<? extends I18nEnum>> list = new ArrayList<Class<? extends I18nEnum>>();
    static {
        list.add(WarningType.class);
    }
    
    public static I18nManager i18n = new I18nManager(WarningList.class, null, list);
    
    // {end fields}

    // {section constructors}
    // **********************
    public WarningList() {
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        
        if(country != null) {
            this.country = country.toUpperCase();
        }else {
            this.country = null;
        }
        
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public WarningType getType() {
        return type;
    }

    public void setType(WarningType type) {
        this.type = type;
    }
    
    public Integer getCount() {
        
        if(warnings == null) {
            if(getWarnings() == null) {
                return 0;
            }
        }
        
        Integer count = 0;
        
        for (ArrayList<Warning> list : warnings.values()) {
            count += list.size();
        }
        
        return count;
    }
    
    // {end gettersandsetters}
    
    // {section publicmethods}
    // ***********************
    
    public LinkedHashMap<WarningType, ArrayList<Warning>> getWarnings() {
        
        if(protocolContainer == null) {
            protocolContainer = dbGetProtocolWarnings();
        }
        if(ticketUnconfirmedContainer == null) {
            ticketUnconfirmedContainer = dbGetUnconfirmedTickets();
        }
        if(ticketOrderContainer == null){
            ticketOrderContainer = dbGetTicketOrderWarnings();
        }
        if(ticketOrderNurseContainer == null){
            ticketOrderNurseContainer = dbGetTicketOrderNurseWarnings();
        }
        
        warnings = new LinkedHashMap<WarningType, ArrayList<Warning>>();
        addProtocolWarnings();
        addTicketUnconfirmedWarnings();
        addTicketOrderWarnings();
        addTicketOrderNurseWarnings();
        
        return warnings;
    }
    
    public String getFormattedWarnings() {
        
        getWarnings();
        
        StringBuilder bld = new StringBuilder();
        
        for (Entry<WarningType, ArrayList<Warning>> entry : warnings.entrySet()) {
            
            bld.append("<h3>" + i18n.get(entry.getKey()) + "</h3>");
            
            for (Warning warning : entry.getValue()) {
                
                bld.append(warning.getContents());
                
            }
        }
        
        return bld.toString();
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    /**
     * initializes an empty key list, used for manually defining insertion order
     * @param type
     */
    private void addToMap(WarningType type) {
        if(warnings.get(type) == null ) {
            warnings.put(type, new ArrayList<Warning>());
        }
    }
    
    private void addToMap(Warning warning) {
        
        if(warnings.get(warning.getType()) == null ) {
            warnings.put(warning.getType(), new ArrayList<Warning>());
        }
        
        warnings.get(warning.getType()).add(warning);
    }
    
    private void addProtocolWarnings() {
        
        if(protocolContainer == null || ( type != null && !(type instanceof WarningType.protocols))) {
            return;
        }
        
        addToMap(WarningType.protocols.unaccepted);
        addToMap(WarningType.protocols.late);
        
        //filtering
        protocolContainer.removeAllContainerFilters();
        protocolContainer.addContainerFilter(new Filter() {
            
            @Override
            public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
                
                boolean usr = userId == null || MyUtils.getValueFromItem(item, "id_nurse_user", BigInteger.class).intValueExact() == userId;
                boolean pat = patientId == null || MyUtils.getValueFromItem(item, "id_patient", Integer.class) == patientId;
                
                return usr && pat;
            }
            
            @Override
            public boolean appliesToProperty(Object propertyId) {
                return false;
            }
        });
        
        StringSqlTimestampConverter dc = new StringSqlTimestampConverter();
        
        for (Object iid : protocolContainer.getItemIds()) {
            Item item = protocolContainer.getItem(iid);
            
            Warning warning = new Warning();
            warning.setPatientId(MyUtils.getValueFromItem(item, "id_patient", Integer.class));
            warning.setUserId(MyUtils.getValueFromItem(item, "id_nurse_user", BigInteger.class).intValueExact());
            warning.setSeverity(WarningSeverity.WARNING);
            warning.setDate(MyUtils.getValueFromItem(item, "date", java.sql.Date.class));
            
            String project = MyUtils.getValueFromItem(item, "project", String.class);
            String patient = MyUtils.getValueFromItem(item, "patient", String.class);
            String date = dc.convertToPresentation(MyUtils.getValueFromItem(item, "date", java.sql.Date.class), String.class, UI.getCurrent().getLocale());
            
            warning.setContents("<div>" + date + ": <b>" + project + "</b> " + patient +"</div>");

            if((type == null || (type == WarningType.protocols.unaccepted))
                && MyUtils.getValueFromItem(item, "unaccepted", Integer.class) == 1) {
                warning.setType(WarningType.protocols.unaccepted);
            }
            if((type == null || (type == WarningType.protocols.late))
                && MyUtils.getValueFromItem(item, "late", Integer.class) == 1) {
                warning.setType(WarningType.protocols.late);
            }
            
            addToMap(warning);
        }
        
    }
    
    private void addTicketUnconfirmedWarnings() {
        
        if(ticketUnconfirmedContainer == null || (type != null && type != WarningType.shipping.received)) {
            return;
        }
        
        //filtering
        ticketUnconfirmedContainer.removeAllContainerFilters();
        ticketUnconfirmedContainer.addContainerFilter(new Filter() {
            
            @Override
            public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
                
                boolean usr = userId == null || userId.equals(MyUtils.getValueFromItem(item, "OMT_ID_USER", Integer.class));
                boolean pat = patientId == null || patientId.equals(MyUtils.getValueFromItem(item, "OMT_ID_PATIENT", Integer.class));
                boolean cntr = country == null || country.equals(MyUtils.getValueFromItem(item, "OMT_KEY_COUNTRY", String.class));
                
                return usr && pat && cntr;
            }
            
            @Override
            public boolean appliesToProperty(Object propertyId) {
                return false;
            }
        });
        
        StringSqlTimestampConverter dc = new StringSqlTimestampConverter();
        
        for (Object iid : ticketUnconfirmedContainer.getItemIds()) {
            Item item = ticketUnconfirmedContainer.getItem(iid);
            
            Warning warning = new Warning();
            warning.setPatientId(MyUtils.getValueFromItem(item, "OMT_ID_PATIENT", Integer.class));
            warning.setUserId(MyUtils.getValueFromItem(item, "OMT_ID_USER", Integer.class));
            warning.setSeverity(WarningSeverity.WARNING);
            warning.setDate(MyUtils.getValueFromItem(item, "OMT_DELIVERY_DATE", java.sql.Date.class));
            
            Integer tid = MyUtils.getValueFromItem(item, "OMT_ID", Integer.class);
            String date = dc.convertToPresentation(MyUtils.getValueFromItem(item, "OMT_DELIVERY_DATE", java.sql.Date.class), String.class, UI.getCurrent().getLocale());
            
            warning.setContents("<div><b>#" + tid + "</b> - " + date + "</div>");

            warning.setType(WarningType.shipping.received);
            
            addToMap(warning);
        }
        
    }
    
    private void addTicketOrderWarnings() {
        
        if(ticketOrderContainer == null || (type != null && type != WarningType.shipping.ordered)) {
            return;
        }
        
        //filtering
        ticketOrderContainer.removeAllContainerFilters();
        ticketOrderContainer.addContainerFilter(new Filter() {
            
            @Override
            public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
                
                TicketEvent tev = (TicketEvent) itemId;
                
                boolean usr = userId == null || userId.equals(tev.getNurseUserId());
                boolean pat = patientId == null; // TBI, requires changes to TicketEvent beans
                boolean cntr = country == null || country.equals(tev.getCountryKey());
                
                return usr && pat && cntr;
            }
            
            @Override
            public boolean appliesToProperty(Object propertyId) {
                return false;
            }
        });
        
        StringSqlTimestampConverter dc = new StringSqlTimestampConverter();
        
        for (TicketEvent tev : ticketOrderContainer.getItemIds()) {
            
            Warning warning = new Warning();
//            warning.setPatientId(MyUtils.getValueFromItem(item, "OMT_ID_PATIENT", Integer.class));
            warning.setCountry(tev.getCountryKey());
            warning.setUserId(tev.getNurseUserId());
            warning.setSeverity(WarningSeverity.WARNING);
            warning.setDate(new java.sql.Date(tev.getStart().getTime()));
            
            warning.setContents("<div>" + tev.getCaption() + "</div>");

            warning.setType(WarningType.shipping.ordered);
            
            addToMap(warning);
        }
        
    }
    
    private void addTicketOrderNurseWarnings() {
        
        if(ticketOrderNurseContainer == null || (type != null && type != WarningType.shipping.ordered)) {
            return;
        }
        
        //filtering
        ticketOrderNurseContainer.removeAllContainerFilters();
        ticketOrderNurseContainer.addContainerFilter(new Filter() {
            
            @Override
            public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
                
                boolean usr = userId == null || userId.equals(MyUtils.getValueFromItem(item, "OMT_ID_USER", Integer.class));
                boolean pat = patientId == null || patientId.equals(MyUtils.getValueFromItem(item, "OMT_ID_PATIENT", Integer.class));
                boolean cntr = country == null || country.equals(MyUtils.getValueFromItem(item, "OMT_KEY_COUNTRY", String.class));
                
                return usr && pat && cntr;
            }
            
            @Override
            public boolean appliesToProperty(Object propertyId) {
                return false;
            }
        });
        
        StringSqlTimestampConverter dc = new StringSqlTimestampConverter();
        
        for (Object iid : ticketOrderNurseContainer.getItemIds()) {
            
            Item item = ticketOrderNurseContainer.getItem(iid);
            
            String country = MyUtils.getValueFromItem(item, "OMT_KEY_COUNTRY", String.class);
            Integer tid = MyUtils.getValueFromItem(item, "OMT_ID", Integer.class);
            String nurse = MyUtils.getValueFromItem(item, "nurse", String.class);
            
            Warning warning = new Warning();
//            warning.setPatientId(MyUtils.getValueFromItem(item, "OMT_ID_PATIENT", Integer.class));
            warning.setCountry(country);
            warning.setUserId(MyUtils.getValueFromItem(item, "OMT_ID_USER", Integer.class));
            warning.setSeverity(WarningSeverity.WARNING);
            warning.setDate(MyUtils.getValueFromItem(item, "OMT_DELIVERY_DATE", java.sql.Date.class));
            
            
            
            warning.setContents("<div>[" + country + "] " + nurse + " (#" + tid + ")</div>");

            warning.setType(WarningType.shipping.ordered);
            
            addToMap(warning);
        }
        
    }
    
    // {end privatemethods}

    // {section database}
    // ******************
    
    private IndexedContainer dbGetProtocolWarnings(){
        
        DBClass db = new DBClass();
        
        db.CustomQuery.setSqlString("CALL proc_nurse_reminder('" + MyUtils.formatSqlDate(new LocalDate().plusWeeks(weeksInAdvance)) + "', "+daysCS+");");
//        db.debugNextQuery(true);
        db.CustomQuery.query(true);
        db.CustomQuery.setSqlString("select temp_nurse_reminder.*, CAST(KSK_U_ID as UNSIGNED) as id_nurse_user from temp_nurse_reminder"
                         + "\n " + "left join cust_krankenschwester_stammdaten_ks on id_nurse = KSK_ID"
                         + "\n " + "ORDER BY project, date, unaccepted DESC;");
//        db.debugNextQuery(true);
        return (IndexedContainer) db.CustomQuery.query();
        
    }
    
    private IndexedContainer dbGetUnconfirmedTickets() {
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select * from cust_ordermanager_tickets"
            + "\n " + "where OMT_DELIVERY_DATE <= NOW() and OMT_ID_STATUS = 20"
                + " and OMT_DELIVERY_DATE >= '" + MyUtils.formatSqlDate(new LocalDate().minusMonths(2)) + "'";
        q.setSqlString(sql);
        
        return (IndexedContainer) q.query();

        
    }
    
    private BeanItemContainer<TicketEvent> dbGetTicketOrderWarnings() {
        
        BeanItemContainer<TicketEvent> beans = new  BeanItemContainer<TicketEvent>(TicketEvent.class);
        PackageCreatorDataView.addAllPlannedEvents(beans, new LocalDate().minusMonths(2), new LocalDate().plusDays(10));
        
        return beans;
    }
    
    private IndexedContainer dbGetTicketOrderNurseWarnings() {
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select "
            + "\n " + "CONCAT(AUL_VORNAME, ' ', AUL_NAME) as nurse,"
            + "\n " + "OMT_ID,"
            + "\n " + "OMT_KEY_COUNTRY,"
            + "\n " + "OMT_ID_USER,"
            + "\n " + "OMT_DELIVERY_DATE"
            + "\n " + "from cust_ordermanager_tickets"
            + "\n " + "left join cms_auth_stammdaten_user on OMT_ID_USER = AUL_ID"
            + "\n " + "WHERE   OMT_TYPE = 'NURSE' and OMT_ID_STATUS < 10"
            + "\n " + "and OMT_DELIVERY_DATE between '" + MyUtils.formatSqlDate(new LocalDate().minusMonths(2)) + "' and '" 
            + MyUtils.formatSqlDate(new LocalDate().plusDays(10)) + "';";
        q.setSqlString(sql);
        
        return (IndexedContainer) q.query();
        
    }
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
