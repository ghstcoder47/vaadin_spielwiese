package archenoah.web.normal.main.Menuepunkte.Individuelle.TaskSystem.MeetingLogging.classes;

import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.vaadin.Components.Combo.combo;

import com.vaadin.data.Container;
import com.vaadin.ui.ComboBox;

public class MeetingGroupContainer {

	Container con; 
	
	public MeetingGroupContainer() {
		dbGetContainer();
	}

	public Container getContainer(){
		return this.con;
	}
	
	public void refreshContainer(){
		dbGetContainer();
	}
	
   public void fillCombo(ComboBox box){
        box.removeAllItems();
        try {
            combo nc = new combo(con, box, "TMEG_ID", "TMEG_NAME");
        } catch (Exception e) {
            box.removeAllItems();
        }
    }
	
	private void dbGetContainer(){
		
        DBClass db = new DBClass();
        
      	db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("*");

        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cust_taskmanager_meeting_groups");
        db.DB_Data_Get.DB_Ordnen.DB_Ordnen("TMEG_ID", "ASC");
        
        db.debugNextQuery(false);
        
        this.con = db.DB_Data_Get.DB_SEND_AND_GET_Container();
		
	}
	
}
