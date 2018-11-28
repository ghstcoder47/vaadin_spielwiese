package archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.OpenTickets.classes;

import archenoah.lib.tool.comunication.dbclass.DBClass;

public class TicketDelete {

    public TicketDelete() {
        // TASK Auto-generated constructor stub
    }

    public static Boolean delete(Integer ticketId){
        
        Boolean success = false;
        
        DBClass db = new DBClass();
        db.CustomQuery.setSqlString("DELETE FROM cust_ordermanager_tickets WHERE OMT_ID="+ticketId);
        Integer res = db.CustomQuery.update();
        
        if(res > 0){
            success = true;
            db.CustomQuery.setSqlString("DELETE FROM cust_ordermanager_ticket_contents WHERE OMTC_ID_TICKET="+ticketId);
            db.CustomQuery.update();
        }
        
        return success;
    }
}
