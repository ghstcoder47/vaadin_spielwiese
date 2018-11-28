package archenoah.web.normal.main.tray.sm.benutzeronline;

import archenoah.UIS.UI_Functions;
import archenoah.config.CMS_Config_Std;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.vaadin.Logger.Logger;


import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;

import com.vaadin.data.Container;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

public class tray_benutzeronline {

	private MenuBar  tray;
	
	private String Anzahl_User;
	final MenuBar.MenuItem Benutzer_Online;
	private CssLayout layout;
	
	private UI_Functions me;
	
	public tray_benutzeronline() {
		// TODO Automatisch generierter Konstruktorstub
		
		tray = (MenuBar) UI.getCurrent().getSession().getSession().getAttribute("tray");
		layout =  (CssLayout) UI.getCurrent().getSession().getSession().getAttribute("lay");
		me = new  UI_Functions();
		Init_Anzahl_User();
		
		Benutzer_Online = tray.addItem(Anzahl_User,new ThemeResource("image-res/icons-white-16/User.png"), null);
		Benutzer_Online.setDescription(Anzahl_User +" User Online");
		
		OnlineStatus();
	//	new test().start();
	
	}
	
	
	
	
	private void Init_Anzahl_User()
	{
		
		CMS_Config_Std conf = CMS_Config_Std.getInstance();
		DBClass db = new DBClass();
		db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_All();
		db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_session_liste");
		db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_session_liste", "CSL_STATUS_ID", "=",  "1", "AND");
		db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_session_liste", "CSL_USER_ID", "!=",  "0", "");
		
		Container con = db.DB_Data_Get.DB_SEND_AND_GET_Container();
	
		if(con == null)
		{
			
			Anzahl_User = "0";
			
		}else
		{
			Anzahl_User = con.size() +"";
		}
		
	}
	
	
	private void OnlineStatus()
	{
		  final Refresher refresher = new Refresher();

		  refresher.setRefreshInterval(20000);

		  refresher.addListener(new Onlinestatuslistener());
		
		  me.addEx(refresher);
		  
   	 // addExtension(refresher);  
 
		
		
	}

	
	public class Onlinestatuslistener implements RefreshListener {

		@Override
		public void refresh(Refresher source) {
			// TODO Automatisch generierter Methodenstub
			  
		
			Init_Anzahl_User();
			Benutzer_Online.setText(Anzahl_User+"");
			Benutzer_Online.setDescription(Anzahl_User +" User Online");
			
			
		
	
			if(pr_sessionclose() == true)
			{
			
				me.remEx(source);
				
				Abmelden();
				User_abmelden();
				UI.getCurrent().getUI().close();	
			}
			
			
			
		}
		
		
	}
		private boolean pr_sessionclose()
		{
			String Session = UI.getCurrent().getSession().getSession().getId();
			
			CMS_Config_Std std = CMS_Config_Std.getInstance();
			
			DBClass db = new DBClass();
			
			db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("CSL_ID");
			db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_session_liste");
			db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_session_liste", "CSL_SESSION_ID", "=", "'"+Session+"'", "AND");
			db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_session_liste", "CSL_STATUS_SETTER", "=", "1", "");
			
			Container con = db.DB_Data_Get.DB_SEND_AND_GET_Container();
			try {
				if(con.size() > 0 )
				{
					return true;
				}else{
					return false;
				}
			} catch (Exception e) {
				// TODO Automatisch generierter Erfassungsblock
				return false;
			}
			
			
		}
		
		private void Abmelden()
		{
			WebBrowser webBrowser = UI.getCurrent().getSession().getBrowser();
			
			
			Logger log = new Logger();
			log.Logging_Info("3", "Der Benutzer: \""+UI.getCurrent().getSession().getSession().getAttribute("Username")+"\" wurde Abgemeldet.", webBrowser.getAddress(),UI.getCurrent().getSession().getSession().getId());
			
			
		}
		private void User_abmelden()
		{
			CMS_Config_Std std = CMS_Config_Std.getInstance();
			
			DBClass db = new DBClass();
			db.DB_Data_Update.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_session_liste");
			db.DB_Data_Update.DB_Filter.DB_WHERE_Allgemein("cms_session_liste", "CSL_SESSION_ID", "=", "'"+UI.getCurrent().getSession().getSession().getId()+"'", "");
			db.DB_Data_Update.DB_Daten.DB_Data("cms_session_liste", "CSL_STATUS_SETTER", "0");
			db.DB_Data_Update.DB_Update();
			
		}




		



	
		
		
}


