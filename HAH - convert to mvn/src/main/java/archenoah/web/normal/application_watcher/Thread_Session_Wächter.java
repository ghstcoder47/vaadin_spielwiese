package archenoah.web.normal.application_watcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import archenoah.config.CMS_Config_Std;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.vaadin.Logger.Logger;

import com.vaadin.server.WebBrowser;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class Thread_Session_Wächter extends Thread{
	private Window mainwin;

	private String Username;
	private CMS_Config_Std std;
	public long sessionTimeout;
	public long CreateTime;
	public long LastAccessTime;
	public long tempTime;
	private WebBrowser webBrowser;
	
	
	
	public String Meine_Session;
	
	public Thread_Session_Wächter(String Arg_Username) {
		// TODO Automatisch generierter Konstruktorstub
		std = CMS_Config_Std.getInstance();
	
		Username = Arg_Username;
		sessionTimeout = UI.getCurrent().getSession().getSession().getMaxInactiveInterval();
		CreateTime = UI.getCurrent().getSession().getSession().getCreationTime();
		LastAccessTime = UI.getCurrent().getSession().getSession().getLastAccessedTime();
		Meine_Session = UI.getCurrent().getSession().getSession().getId();
		
		webBrowser =UI.getCurrent().getSession().getBrowser();
	}
	
	@Override
    public void run() {
		
		boolean a = false;
				int i = 0;
				int z =0;
		while(a!=true)
		{     
		    
		    if(UI.getCurrent() == null || UI.getCurrent().getSession() == null || UI.getCurrent().getSession().getSession() == null){
		        a = true;
		        continue;
		    }
		    
			LastAccessTime = UI.getCurrent().getSession().getSession().getLastAccessedTime();
			tempTime = LastAccessTime + (sessionTimeout*1000)-(5000);
			Calendar c = new GregorianCalendar();
			if(tempTime<=c.getTimeInMillis())
			{
				
				Logger log = new Logger();
				log.Logging_Info("3", "Die Session \""+Meine_Session+"\" ist Abgelaufen und der Client wurde Automatisch abgemeldet.", webBrowser.getAddress(),Meine_Session);
				
				
				
				Date d = new Date();
				DateFormat dfa = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
				String Datum_Uhrzeit = dfa.format(d);
				CMS_Config_Std conf = CMS_Config_Std.getInstance();
				DBClass db = new DBClass();
				db.DB_Data_Update.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_session_liste");
				db.DB_Data_Update.DB_Daten.DB_Data("cms_session_liste", "CSL_LAST_AKT", Datum_Uhrzeit);
				db.DB_Data_Update.DB_Daten.DB_Data("cms_session_liste", "CSL_USER_ID", "0");
				db.DB_Data_Update.DB_Daten.DB_Data("cms_session_liste", "CSL_STATUS_ID", "2");
				db.DB_Data_Update.DB_Daten.DB_Data("cms_session_liste", "CLS_CLOSE", Datum_Uhrzeit);
				db.DB_Data_Update.DB_Filter.DB_WHERE_Allgemein("cms_session_liste", "CSL_SESSION_ID", "=", "'"+Meine_Session+"'", "AND");
				db.DB_Data_Update.DB_Filter.DB_WHERE_Allgemein("cms_session_liste", "CSL_STATUS_ID", "=", "1", "");
				db.DB_Data_Update.DB_Update();
				
				
				
				System.out.println("Jo ist in Session");
				
			
				
				
			stop();
			a = true;
				
			}
			
						if(i==30)
						{
							i=0;
							Letzteänderung();
							
						}else
						{
							i++;
						}
						
						if(z==600)
						{
							z=0;
							Berreinigung();
							
						}else
						{
							z++;
						}
				
				
			
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Automatisch generierter Erfassungsblock
				e.printStackTrace();
			}
	
		}
		
	}

	private void Letzteänderung()
	{
		Date d = new Date();
		DateFormat dfa = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		String Datum_Uhrzeit = dfa.format(d);
		CMS_Config_Std conf = CMS_Config_Std.getInstance();
		DBClass db = new DBClass();
		db.DB_Data_Update.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_session_liste");
		db.DB_Data_Update.DB_Daten.DB_Data("cms_session_liste", "CSL_LAST_AKT", Datum_Uhrzeit);
		db.DB_Data_Update.DB_Filter.DB_WHERE_Allgemein("cms_session_liste", "CSL_SESSION_ID", "=", "'"+Meine_Session+"'", "AND");
		db.DB_Data_Update.DB_Filter.DB_WHERE_Allgemein("cms_session_liste", "CSL_STATUS_ID", "=", "1", "");
		db.DB_Data_Update.DB_Update();
	}
	private void Berreinigung()
	{
		Date d = new Date();
		Date da = new Date();
		DateFormat dfa = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		String Datum_Uhrzeit = dfa.format(d);
		
		Calendar c = new GregorianCalendar();
		long sec = c.getTimeInMillis()-(sessionTimeout*1000);
		c.setTimeInMillis(sec);
		da = c.getTime();
		String Filteruhrzeit= dfa.format(da);
		
		CMS_Config_Std conf = CMS_Config_Std.getInstance();
		DBClass db = new DBClass();
		db.DB_Data_Update.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_session_liste");
		db.DB_Data_Update.DB_Daten.DB_Data("cms_session_liste", "CSL_LAST_AKT", Datum_Uhrzeit);
		db.DB_Data_Update.DB_Daten.DB_Data("cms_session_liste", "CSL_USER_ID", "0");
		db.DB_Data_Update.DB_Daten.DB_Data("cms_session_liste", "CSL_STATUS_ID", "2");
		db.DB_Data_Update.DB_Daten.DB_Data("cms_session_liste", "CLS_CLOSE", Datum_Uhrzeit);
		db.DB_Data_Update.DB_Filter.DB_WHERE_Allgemein("cms_session_liste", "CSL_LAST_AKT", "<=", "'"+Filteruhrzeit+"'", "AND");
		db.DB_Data_Update.DB_Filter.DB_WHERE_Allgemein("cms_session_liste", "CSL_STATUS_ID", "=", "1", "");
		db.DB_Data_Update.DB_Update();
		
		
		
	}
	
	
	
}
