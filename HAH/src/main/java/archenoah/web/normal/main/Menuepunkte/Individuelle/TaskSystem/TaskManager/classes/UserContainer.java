package archenoah.web.normal.main.Menuepunkte.Individuelle.TaskSystem.TaskManager.classes;

import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.vaadin.Components.Combo.combo;

import com.vaadin.data.Container;
import com.vaadin.ui.ComboBox;

public class UserContainer {

	Container con; 
	
	public UserContainer() {
		dbGetContainer();
	}

	public Container getContainer(){
		return this.con;
	}
	
	public void refreshContainer(){
		dbGetContainer();
	}
	
	public void fillCombo(ComboBox box){
	    refreshContainer();
	    box.removeAllItems();
	    try {
            combo nc = new combo(getContainer(), box, "AUL_ID", "user");
        } catch (Exception e) {
            box.removeAllItems();
        }
	}
	
	private void dbGetContainer(){
		
        DBClass db = new DBClass();
        
      	db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("*");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("CONCAT(ASA_NAME,' ',AUL_VORNAME,' ',AUL_NAME)", "user");

        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cms_auth_stammdaten_user", "LEFT JOIN", "cms_auth_stammdaten_anrede", "AUL_ANREDE_ID", "ASA_ID");
        db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_auth_stammdaten_user", "AUL_USERNAME", "!=", "''", "");
        
        
        db.DB_Data_Get.DB_Ordnen.DB_Ordnen("AUL_NAME", "ASC");
        
        db.debugNextQuery(false);
        
        this.con = db.DB_Data_Get.DB_SEND_AND_GET_Container();
		
	}
	
	
}
