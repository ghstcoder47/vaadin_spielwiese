package archenoah.lib.vaadin.MenuBerechtigung;



import com.vaadin.data.util.sqlcontainer.SQLContainer;

import archenoah.config.CMS_Config_Std;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.java_plugin.array_builder.classes.ArrayBuilder_Einzel;


public class Gruppen_ID {


	private String User_ID;
	
	
	
	public Gruppen_ID(String User_ID) {
		// TODO Automatisch generierter Konstruktorstub
		this.User_ID = User_ID;
		
	}
	
	public int[] Grp_ID_Holen()
	{
		
		
		int temp[]= null;
		int i = 0;
		
		ArrayBuilder_Einzel arb;
		
		CMS_Config_Std std = CMS_Config_Std.getInstance();
		
		
		
		DBClass db = new DBClass();
		db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("AL_G_ID");
		db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_auth_liste_gu");
		db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_auth_liste_gu", "AL_U_ID", "=", User_ID, "");
		
		SQLContainer container = db.DB_Data_Get.DB_SEND_AND_GET();
		
		for (Object cityItemId : container.getItemIds()) {
			
			 arb = new ArrayBuilder_Einzel(temp);
			 temp = arb.Array_Holen_int();
			 
			 temp[i] = (int) container.getItem(cityItemId).getItemProperty("AL_G_ID").getValue();
			 
			
			 i++;
       }
		
		db.DB_Data_Get.connclose();
		
		
		return temp;
		

	}
	

}
