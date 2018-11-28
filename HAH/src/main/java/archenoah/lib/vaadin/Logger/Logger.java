package archenoah.lib.vaadin.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import archenoah.config.CMS_Config_Std;
import archenoah.lib.tool.comunication.dbclass.DBClass;

public class Logger {

	
	CMS_Config_Std conf;
	
	
	public Logger() {
		// TODO Automatisch generierter Konstruktorstub
		conf = CMS_Config_Std.getInstance();
	}
	
	
	public void Logging_Info(String Logger_Typ,String Nachrricht,String IpAdresse,String Session)
	{
		if(IpAdresse.equals("0:0:0:0:0:0:0:1"))
		{
			IpAdresse = "127.0.0.1";
		}
		
		Date d = new Date();
		DateFormat dfa = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		String Datum_Uhrzeit = dfa.format(d);
		DBClass db = new DBClass();
		db.DB_Data_Insert.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_logger_liste");
		db.DB_Data_Insert.DB_Daten.DB_Data("cms_logger_liste", "LL_DATE", Datum_Uhrzeit);
		db.DB_Data_Insert.DB_Daten.DB_Data("cms_logger_liste", "LL_NACHRICHT", Nachrricht);
		db.DB_Data_Insert.DB_Daten.DB_Data("cms_logger_liste", "LL_LOG_TYP_ID", "1");
		db.DB_Data_Insert.DB_Daten.DB_Data("cms_logger_liste", "LL_LOGGER_TYP_ID", Logger_Typ);
		db.DB_Data_Insert.DB_Daten.DB_Data("cms_logger_liste", "LL_IP_ADRESSE", IpAdresse);
		db.DB_Data_Insert.DB_Daten.DB_Data("cms_logger_liste", "LL_SESSION", Session);
		db.DB_Data_Insert.DB_Insert();
	}
	public void Logging_Warnung(String Logger_Typ,String Nachrricht,String IpAdresse,String Session)
	{
		if(IpAdresse.equals("0:0:0:0:0:0:0:1"))
		{
			IpAdresse = "127.0.0.1";
		}
		
		Date d = new Date();
		DateFormat dfa = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		String Datum_Uhrzeit = dfa.format(d);
		DBClass db = new DBClass();
		db.DB_Data_Insert.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_logger_liste");
		db.DB_Data_Insert.DB_Daten.DB_Data("cms_logger_liste", "LL_DATE", Datum_Uhrzeit);
		db.DB_Data_Insert.DB_Daten.DB_Data("cms_logger_liste", "LL_NACHRICHT", Nachrricht);
		db.DB_Data_Insert.DB_Daten.DB_Data("cms_logger_liste", "LL_LOG_TYP_ID", "2");
		db.DB_Data_Insert.DB_Daten.DB_Data("cms_logger_liste", "LL_LOGGER_TYP_ID", Logger_Typ);
		db.DB_Data_Insert.DB_Daten.DB_Data("cms_logger_liste", "LL_IP_ADRESSE", IpAdresse);
		db.DB_Data_Insert.DB_Daten.DB_Data("cms_logger_liste", "LL_SESSION", Session);
		db.DB_Data_Insert.DB_Insert();
		
		
		
		
		
		
		
		
	}
	
	public void Logging_Fehler(String Logger_Typ,String Nachrricht,String IpAdresse,String Session)
	{
		if(IpAdresse.equals("0:0:0:0:0:0:0:1"))
		{
			IpAdresse = "127.0.0.1";
		}
		
		Date d = new Date();
		DateFormat dfa = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		String Datum_Uhrzeit = dfa.format(d);
		DBClass db = new DBClass();
		db.DB_Data_Insert.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_logger_liste");
		db.DB_Data_Insert.DB_Daten.DB_Data("cms_logger_liste", "LL_DATE", Datum_Uhrzeit);
		db.DB_Data_Insert.DB_Daten.DB_Data("cms_logger_liste", "LL_NACHRICHT", Nachrricht);
		db.DB_Data_Insert.DB_Daten.DB_Data("cms_logger_liste", "LL_LOG_TYP_ID", "3");
		db.DB_Data_Insert.DB_Daten.DB_Data("cms_logger_liste", "LL_LOGGER_TYP_ID", Logger_Typ);
		db.DB_Data_Insert.DB_Daten.DB_Data("cms_logger_liste", "LL_IP_ADRESSE", IpAdresse);
		db.DB_Data_Insert.DB_Daten.DB_Data("cms_logger_liste", "LL_SESSION", Session);
		db.DB_Data_Insert.DB_Insert();

	}

}
