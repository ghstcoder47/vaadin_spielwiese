package archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.OpenTickets.classes;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.vaadin.Language.i18n.I18nGlobalNotifiers;
import archenoah.lib.vaadin.Language.i18n.I18nManager;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.OpenTickets.form.OpenTicketsInsertEdit;

import com.vaadin.data.Item;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Window;

public class TicketContentLocator {

    // {section fields}
    // ****************
    String table = "cust_ordermanager_ticket_contents";
    private MenuItem menuItem;
    
    // {end fields}


    // {section constructors}
    // **********************
    public TicketContentLocator(MenuItem menuItem) {
        this.menuItem = menuItem;
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    public Window locateContent(Integer projectId, Integer dataId) {
        
        Integer tid = dbGetTicketId(projectId, dataId);
        
        if(tid == null) {
            I18nManager.global(I18nGlobalNotifiers.no_entry_selected);
            return null;
        }
        
        OpenTicketsInsertEdit pie = new OpenTicketsInsertEdit(menuItem, tid.toString());
        pie.setPermissions(0, 1, 0, 0);
        pie.init();
        
        return pie.getWindow();
        
    }

    // {end publicmethods}

    // {section privatemethods}
    // ************************
    // {end privatemethods}

    // {section database}
    // ******************
    private Integer dbGetTicketId(Integer projectId, Integer dataId) {
        
        Integer tid = null;
        
        projectId = projectId != null ? projectId : 0;
        dataId = dataId != null ? dataId : 0;
        
        DBClass db = new DBClass();

        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("OMTC_ID_TICKET");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln(table);
        
        db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein(table, "OMTC_ID_PROJECT", "=", projectId.toString(), "AND");
        db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein(table, "OMTC_ID_DATA", "=", dataId.toString(), "");
        
//        db.debugNextQuery(true);
        
        Item item = db.DB_Data_Get.DB_SEND_AND_GET_FIRST_ITEM();
        
        return MyUtils.getValueFromItem(item, "OMTC_ID_TICKET", Integer.class);
    }
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
