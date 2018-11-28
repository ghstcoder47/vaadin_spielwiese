package archenoah.web.normal.application_watcher;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import archenoah.config.CMS_Config_Std;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.email.Mailer;
import archenoah.lib.tool.comunication.email.Mailer.MAIL_TYPE;
import archenoah.lib.vaadin.Logger.Logger;

import com.vaadin.data.Container;
import com.vaadin.ui.UI;


public class Thread_Watcher extends Thread {

	String IPAdd;
	String User;
	
	int Max_Fehl_User;
	int Max_Fehl_IP;
	private Logger log;
	private CMS_Config_Std str = CMS_Config_Std.getInstance();
	private String  Meine_Session;
	public Thread_Watcher(String IPAdresse,String Username) {
		Meine_Session = UI.getCurrent().getSession().getSession().getId();
		// TODO Automatisch generierter Konstruktorstub
		
		User = Username;
		if(IPAdresse.equals("0:0:0:0:0:0:0:1"))
		{
			IPAdresse = "127.0.0.1";

		}
		IPAdd = IPAdresse;
		
		Max_Fehl_User 	=	str.MaxFehlversuchUser;
		Max_Fehl_IP 	=	str.MaxFehlversuchIP;
		
	}
	
	
	@Override
    public void run() {
		
		if(User_exist(User))
		{
			log = new Logger();
			log.Logging_Fehler("3", "Falsche Authentifizierung: Der Benutzer  \""+ User +"\" hat sich versucht mit einem falschen Passwort anzumelden.", IPAdd,Meine_Session);
			
			if(PrüfungSperrenUser(User)>=Max_Fehl_User)
			{
				SperrenUser();
				log = new Logger();
				log.Logging_Fehler("2", "Benutzer Gesperrt: Achtung es wurde wegen wiederholter Fehlanmeldung der Benutzer \""+ User +"\" gesperrt!", IPAdd,Meine_Session);
				Email_User_Gesperrt();
			}

		}else
		{
			log = new Logger();
			log.Logging_Warnung("3", "Falsche Authentifizierung: Achtung es wurde versucht mit einem unbekannten Benutzer sich anzumelden! (\""+ User +"\")", IPAdd,Meine_Session);
			// Prüfung Falscher Benutzername 
			
		}
		
		if(PrüfungWhite()!=true)
		{
			
			if(PrüfungSperrenIP() >=Max_Fehl_IP)
			{
				SperrenIP();
				log = new Logger();
				log.Logging_Fehler("2", "IP Gesperrt: Achtung es wurde wegen wiederholter Fehlanmeldung durch \""+ User +"\" die IP "+ IPAdd +" gesperrt!", IPAdd,Meine_Session);
				Email_IP_Gesperrt();
			}
		}
	}
	
	
	/********************************User***************************************/
	private boolean User_exist(String User)
	{
		DBClass db = new DBClass();
		db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_auth_stammdaten_user");
		db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("AUL_USERNAME");
		db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_auth_stammdaten_user", "AUL_USERNAME", "=", "'"+User+"'", "");
		Container con = db.DB_Data_Get.DB_SEND_AND_GET_Container();
		if(con!=null|con.size()>0)
		{
			return true;
		}else
		{
			return false;
		}
	}
	
	private void SperrenUser()
	{
		CMS_Config_Std std = CMS_Config_Std.getInstance();
		DBClass db = new DBClass();
		db.DB_Data_Update.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_auth_stammdaten_user");
		db.DB_Data_Update.DB_Daten.DB_Data("cms_auth_stammdaten_user", "AUL_AKTIV", "0");
		db.DB_Data_Update.DB_Filter.DB_WHERE_Allgemein("cms_auth_stammdaten_user", "AUL_USERNAME", "=", "'"+User+"'", "");
		db.DB_Data_Update.DB_Update();
		
	}
	
	private int PrüfungSperrenUser(String User)
	{
		Date d = new Date();
	
		Date da =new Date(d.getTime()-(1000*60*30));
		
		DateFormat dfa = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		String Datum_Uhrzeit = dfa.format(da);
		CMS_Config_Std std = CMS_Config_Std.getInstance();
		DBClass db = new  DBClass();
		db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_All();
		db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_logger_liste");
		db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_logger_liste", "LL_DATE", ">=", "'"+Datum_Uhrzeit+"'", "AND");
		db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_logger_liste", "LL_LOG_TYP_ID", "=", "'3'", "AND");
		db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_logger_liste", "LL_LOGGER_TYP_ID", "=", "'3'", "AND");
		db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_logger_liste", "LL_NACHRICHT", "=", "'Falsche Authentifizierung: Der Benutzer  \""+ User +"\" hat sich versucht mit dem Falschen Passwort anzumelden.'", "AND");
		db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_logger_liste", "LL_IP_ADRESSE", "=", "'"+IPAdd+"'", "");
		Container con = db.DB_Data_Get.DB_SEND_AND_GET_Container();
		
		if(con.size()>0)
		{
			return con.size();
		}else{
			return 0;
		}
		
		
		
	}
	private int PrüfungSperrenIP()
	{
		Date d = new Date();
	
		Date da =new Date(d.getTime()-(1000*60*30));
		
		DateFormat dfa = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		String Datum_Uhrzeit = dfa.format(da);
		CMS_Config_Std std = CMS_Config_Std.getInstance();
		DBClass db = new  DBClass();
		db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_All();
		db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_logger_liste");
		db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_logger_liste", "LL_DATE", ">=", "'"+Datum_Uhrzeit+"'", "AND");
		db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_logger_liste", "LL_LOG_TYP_ID", "=", "'3'", "AND");
		db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_logger_liste", "LL_LOGGER_TYP_ID", "=", "'3'", "AND");
		db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_logger_liste", "LL_IP_ADRESSE", "=", "'"+IPAdd+"'", "");
		Container con = db.DB_Data_Get.DB_SEND_AND_GET_Container();
		
		if(con.size()>0)
		{
			return con.size();
		}else{
			return 0;
		}
		
		
		
	}
	
	private void Email_User_Gesperrt()
	{
	    Mailer mail = new Mailer(CMS_Config_Std.getInstance());
	    mail.setSubject("Achtung Benutzer wurde Gesperrt!");
	    mail.setType(MAIL_TYPE.PLAIN);
	    mail.setSender(str.Server_Email);
	    mail.addRecipient(Empfängeradresse());
	    mail.setContent("Sehr geehrter Administrator der Benutzer: \""+User+"\" wurde wegen wiederholter Fehlanmeldung Gesperrt!");
	    
	    mail.send();
	}
	private void Email_IP_Gesperrt()
	{
	    
        Mailer mail = new Mailer(CMS_Config_Std.getInstance());
        mail.setSubject("Achtung IP wurde Gesperrt!");
        mail.setType(MAIL_TYPE.PLAIN);
        mail.setSender(str.Server_Email);
        mail.addRecipient(Empfängeradresse());
        mail.setContent("Sehr geehrter Administrator die IP: \""+IPAdd+"\" wurde wegen wiederholter Fehlanmeldung Gesperrt!");

        mail.send();
	}
	
	private String Empfängeradresse()
	{
		String Empf = "";
		CMS_Config_Std std = CMS_Config_Std.getInstance();
		DBClass db = new  DBClass();
		db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_All();
		db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_auth_stammdaten_user");
		db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_auth_stammdaten_user", "AUL_ID", "=", "'1'", "");
		Container con = db.DB_Data_Get.DB_SEND_AND_GET_Container();
		for (Object ItemIda : con.getItemIds()) {
			
			Empf = (String) con.getItem(ItemIda).getItemProperty("AUL_EMAIL").getValue();
              //     .getValue();
		}
		
		
		return Empf;
	}
	
	
	
	private void SperrenIP()
	{
		Date d = new Date();
		DateFormat dfa = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		String Datum_Uhrzeit = dfa.format(d);
		
		
		CMS_Config_Std std = CMS_Config_Std.getInstance();
		DBClass db = new DBClass();
		db.DB_Data_Insert.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_ip_waechter_liste");
		db.DB_Data_Insert.DB_Daten.DB_Data("cms_ip_waechter_liste", "IWL_IP_ADRESSE", IPAddresseholen(IPAdd));
		db.DB_Data_Insert.DB_Daten.DB_Data("cms_ip_waechter_liste", "IWL_TYP_ID", "2");
		db.DB_Data_Insert.DB_Daten.DB_Data("cms_ip_waechter_liste", "IWL_DATE_ERSTELLT", Datum_Uhrzeit);
		db.DB_Data_Insert.DB_Daten.DB_Data("cms_ip_waechter_liste", "IWL_HOSTNAME", IPAdd);
		db.DB_Data_Insert.DB_Daten.DB_Data("cms_ip_waechter_liste", "IWL_BEMERKUNGEN", "Host wurde Gesperrt wegen Wiederholter Fehlanmeldung");
		db.DB_Data_Insert.DB_Insert();
	}
	
	
	
	private String IPAddresseholen(String Host)
	{

		String IPAddresse = null;
		
        try {
            IPAddresse = InetAddress.getByName(Host).getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
		return IPAddresse;
		
	}
	
	
	
	private boolean PrüfungWhite()
	{
		CMS_Config_Std std = CMS_Config_Std.getInstance();
		DBClass db = new  DBClass();
		db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_ip_waechter_liste");
		db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_All();
		db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_ip_waechter_liste", "IWL_IP_ADRESSE", "=", "'"+IPAdd+"'", "AND");
		db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_ip_waechter_liste", "IWL_TYP_ID", "=", "'1'", "");
		Container con  = db.DB_Data_Get.DB_SEND_AND_GET_Container();
		
		if (con.size() >0)
		{
			return true;
			
			
		}else
		{
			return false;
			
		}
		
		
	}
	
	
	
	
	
	
	
	

}
