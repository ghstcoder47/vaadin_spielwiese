package archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.OpenTickets.classes;

import org.joda.time.LocalDate;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

public class DbGetTicketContents {
    
    private Boolean grouped = false;
    private Integer ticketId = null;
    private Boolean debug = false;
    
    public DbGetTicketContents() {
        // TASK Auto-generated constructor stub
    }
    
    public Boolean getGrouped() {
        return grouped;
    }

    public void setGrouped(Boolean grouped) {
        this.grouped = grouped;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public Boolean getDebug() {
        return debug;
    }

    public void setDebug(Boolean debug) {
        this.debug = debug;
    }

    /**
     * Set ticketId first.
     * @return Container -> project_date, PP_PRODUCT, project_quantity_static, OMTC_QTY, patient_all, OMTC_ID_DATA, OMTC_ID, OMTC_UPDATED
     */
    public Container getContainer(){
        
        if(ticketId == null){
            return new IndexedContainer();
        }
        
        return getTicketContents();
    }
    
    private Container getTicketContents(){
        
        DBClass db = new DBClass();
        
        String sql = "SELECT "
                + "\n" + (grouped ? "" : "project_date, ")
                + "\n" + "PP_PRODUCT"
                + "\n" + ", rx.balance as rx"
                + "\n" + ", sp.balance as sp"
                + "\n" + ", SUM(project_quantity_static) as project_quantity_static"
                + "\n" + ", SUM(OMTC_QTY) as OMTC_QTY"
                + "\n" + ", CONCAT(IF(OMTC_CUSTOM = 1, '* ', ''), PSL_VORNAME, ' ', PSL_NAME) as patient_all"
                + "\n" + ", CONCAT(PSL_VORNAME, ' ', PSL_NAME) as patient_sort"
                + "\n" + ", OMTC_ID_DATA" //used by isDeletable()
                + "\n" + ", OMTC_ID" //used by getItemById()
                + "\n" + ", OMTC_UPDATED" //used by isDatabaseModified()
                + "\n" + ", OMTC_ID_PATIENT" //used by Balance Updates
                + "\n" + ", PP_ID" //used by Balance Updates
                + "\n" + ", OMTC_CUSTOM" //used by isDeletable in ContentsPanel
                
                + "\n" + "FROM cust_ordermanager_ticket_contents"
                + "\n" + "LEFT JOIN cust_patient_stammdaten_liste on OMTC_ID_PATIENT = PSL_ID"
                + "\n" + "LEFT JOIN view_packagemanager_union_complete on "
                       + "OMTC_ID_DATA = project_data_id and OMTC_ID_PROJECT = project and OMTC_ID_PRODUCT = product"
                + "\n" + "LEFT JOIN cust_prescriptions_products on OMTC_ID_PRODUCT = PP_ID"
                + "\n" + "LEFT JOIN view_balance_rx as rx on PSL_ID = rx.PB_ID_PATIENT and rx.PB_ID_PRODUCT = PP_ID"
                + "\n" + "LEFT JOIN view_balance_sp as sp on PSL_ID = sp.SPB_ID_PATIENT and sp.SPB_ID_PRODUCT = PP_ID"
                
                
                + "\n" + "WHERE OMTC_ID_TICKET = " + ticketId.toString()
                + "\n" + "GROUP BY "
                        + (grouped ? "OMTC_ID_PATIENT, OMTC_ID_PRODUCT" : "OMTC_ID")
                + "\n" + "ORDER BY patient_sort, PP_PRODUCT, date";
        
        db.CustomQuery.setSqlString(sql);
        db.debugNextQuery(debug);
        
        return MyUtils.convertIndex(db.CustomQuery.query(), "OMTC_ID");
        
    }
    
    /**
     * 
     * @return null or earliest entry date
     */
    public LocalDate getEarliestEntry(){
        
        LocalDate date = null;
        
        DBClass db = new DBClass();
        
        String sql = "select min(date) as min_date"
                    + "\n " + "from cust_ordermanager_ticket_contents"
                    + "\n " + "inner join view_packagemanager_union_complete on OMTC_ID_DATA = project_data_id and OMTC_ID_PROJECT = project"
                    + "\n" + "WHERE OMTC_ID_TICKET = " + ticketId.toString();
        
        db.CustomQuery.setSqlString(sql);
        Container con = db.CustomQuery.query();
        
        if(con.size() > 0){
            date = new LocalDate(con.getContainerProperty(con.getItemIds().iterator().next(), "min_date").getValue());
        }
     
        return date;
    }
    
    public Item getTicketItem() {
        Item item = null;
        DBClass db = new DBClass();
        
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("*");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cust_ordermanager_tickets");
        db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cust_ordermanager_tickets", "OMT_ID", "=", ticketId.toString(), "");
        
        item = db.DB_Data_Get.DB_SEND_AND_GET_FIRST_ITEM();
        
        return item;
        
    }
    
}
