package archenoah.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import archenoah.lib.tool.comunication.dbclass.DBClass;

import com.vaadin.data.Container;






public class CMS_Config_Std {
    
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CMS_Config_Std.class);
    
    // singleton setup
    private static CMS_Config_Std instance = null;
    private static CMS_Config_Std instanceXmlOnly = null;
    
    public static final String configHome = System.getProperty("user.home") + File.separator + ".hah" + File.separator;
    
    public String dbDriver;
    public String dbProtocol;
    public String dbParams;
	public String Standard_Server_URL;
	public String Standard_Server_Username;
	public String Standard_Server_Passwort;
	public String Standard_Server_Database;
	public String Standard_Server_Port;
	public String Licence;
	public String Aktiv_APP;
	public String Firma;
	public String Name;
	public String Vorname;
	public String Strasse;
	public String PLZ;
	public String Ort;
	public String Telefon;
	public String Email;
	public String Last_Update;
	public String Last_Work_Day;
	public String Update_Intervalle;
//	public String SMTP_Adresse;
//	public String SMTP_Username;
//	public String SMTP_Passwort;
//	public String SMTP_Port;
	public String mailUser;
	public String mailPass;
	public String Server_Adresse;
	public String Server_Email;
	public int MaxFehlversuchUser;
	public int MaxFehlversuchIP;
	public boolean EmailErrorEnabled;
	public Boolean Test_No_Database= false;
	public String Session_Time_Out="";
	public String Session_Warnung_Time_Out = "";
	public int Max_User_GL_Online_Count = 0;
	public String Suport_Email = "";
	public int Style_ID;
		
	public char[]  Update;
	
	public String Email_CS;
	
	final public Properties mailerConfig = new Properties();
	
	
	public boolean Kompl_pr;
	public int min_psw_length;
	public boolean Liste_pr;
	public boolean Zahl_pr;
	public boolean Char_pr;
	public boolean Grösse_pr;
	
	
	
	public int globalPollingInterval;
    public boolean MailerRedirectEnabled;
    
    public boolean SchedulerEnabled = false;
    
    public String timebutlerApiKey;
    public String timebutlerUrl;
	
    public synchronized static CMS_Config_Std getInstance() {
        if (instance == null) {
            instance = new CMS_Config_Std(false);
        }
        return instance;
    }
    
    public synchronized static CMS_Config_Std getInstanceXmlOnly() {
        if (instanceXmlOnly == null) {
            instanceXmlOnly = new CMS_Config_Std(true);
        }
        return instanceXmlOnly;
    }
    
	protected CMS_Config_Std(Boolean xmlOnly) {
		Properties properties = new Properties();
		String configFilePath = configHome + "hah.properties";
		
		// *******************          Archenoah Config *********************************************
		
		
		BufferedInputStream stream = null;
        try {
            stream = new BufferedInputStream(new FileInputStream(configFilePath));
            properties.load(stream);
        } catch (FileNotFoundException e1) {
            System.err.println("File " + configFilePath + " not found. See config/hah.properties.example");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
            }
        }
		
        if(properties.isEmpty()) {
            return;
        }
		
        dbDriver		         =  properties.getProperty("db.driver");
        dbProtocol      		 =  properties.getProperty("db.protocol");
        dbParams          		 =  properties.getProperty("db.params");
        
		Standard_Server_URL		 =  properties.getProperty("db.url");
		Standard_Server_Username =  properties.getProperty("db.user");
		Standard_Server_Passwort =  properties.getProperty("db.pass");
		Standard_Server_Database =  properties.getProperty("db.database");
		Standard_Server_Port	 =  properties.getProperty("db.port");
		
		try {
		    EmailErrorEnabled = Boolean.parseBoolean(properties.getProperty("mail.errorMailerEnabled"));
        } catch (Exception e) {
            EmailErrorEnabled = false;
        }
		
		try {
		    MailerRedirectEnabled = Boolean.parseBoolean(properties.getProperty("mail.redirectEnabled"));
        } catch (Exception e) {
            MailerRedirectEnabled = false;
        }
		
        try {
            SchedulerEnabled = Boolean.parseBoolean(properties.getProperty("scheduler.enabled"));
        } catch (Exception e) {
            SchedulerEnabled = false;
        }
        
        try {
            timebutlerApiKey = properties.getProperty("timebutler.apiKey");
        } catch (Exception e) {
            timebutlerApiKey = null;
        }
        
        try {
            timebutlerUrl = properties.getProperty("timebutler.url");
        } catch (Exception e) {
            timebutlerUrl = null;
        }
        
		mailUser = properties.getProperty("mail.user");
		mailPass = properties.getProperty("mail.pass");
		
		for (Entry<Object, Object> entry : properties.entrySet()) {
            if(entry.getKey().toString().startsWith("mail.smtp.")) {
                mailerConfig.setProperty(entry.getKey().toString(), entry.getValue().toString());
            }
        }
		
		// *******************          Nächste  *********************************************
		
		if(xmlOnly) {
		    return;
		}
		
		
    	// Holen er Informationen Über SQL  
    	
    	DBClass db = new DBClass();
    	
    	//******************Prüfung Datenbank Vorhanden********//
    	if(db.DB_Data_Advanced.DB_Database_Func_CheckDBExistence()==false)
    	{
    		Test_No_Database = true;
    		return;
    	}
    	if(db.DB_Data_Advanced.DB_Database_Func_Check_Empty()==true)
    	{
    		Test_No_Database = true;
    		return;
    	}
    	
    		
    	db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_einstelungen_archenoah");
    	db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_All();
    	db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_einstelungen_archenoah", "CEA_ID", "=", "1", "");
    	Container con = db.DB_Data_Get.DB_SEND_AND_GET_Container();
    	
    	
    	
    	
    		if(con.size()!=0)
    		{
    			for (Object ItemIda : con.getItemIds()) {
    				
    				/********************************-Lizenz-Informationen-****************************************/
    				Licence					=   (String) con.getItem(ItemIda).getItemProperty("CEA_LIZENZ").getValue();
    				Firma					=   (String) con.getItem(ItemIda).getItemProperty("CEA_LIZENZ_FIRMA").getValue();
    				Name					=   (String) con.getItem(ItemIda).getItemProperty("CEA_LIZENZ_NAME").getValue();
    				Vorname					=   (String) con.getItem(ItemIda).getItemProperty("CEA_LIZENZ_VORNAME").getValue();
    				Strasse					=   (String) con.getItem(ItemIda).getItemProperty("CEA_STRASSE").getValue();
    				PLZ						=   (String) con.getItem(ItemIda).getItemProperty("CEA_PLZ").getValue();
    				Ort						=   (String) con.getItem(ItemIda).getItemProperty("CEA_ORT").getValue();
    				Telefon					=   (String) con.getItem(ItemIda).getItemProperty("CEA_TELEFON").getValue();
    				Email					=   (String) con.getItem(ItemIda).getItemProperty("CEA_EMAIL").getValue();
    				/********************************-Server-Informationen-****************************************/
    				Aktiv_APP				=   (String) con.getItem(ItemIda).getItemProperty("CEA_APP_AKTIV").getValue();
    				Server_Adresse  		=   (String) con.getItem(ItemIda).getItemProperty("CEA_SERVER_ADRESSE").getValue();
    				Server_Email   			=   (String) con.getItem(ItemIda).getItemProperty("CEA_SERVER_EMAIL_ADRESSE").getValue();
    				Session_Time_Out        =	(String) con.getItem(ItemIda).getItemProperty("CEA_SESSION_TIME_OUT").getValue();
    				Session_Warnung_Time_Out =	(String) con.getItem(ItemIda).getItemProperty("CEA_WARNUNG_TIMEOUT").getValue();
    				Max_User_GL_Online_Count =	(int) con.getItem(ItemIda).getItemProperty("CEA_MAX_LEICHTEITIG_USER_ONLINE").getValue();
    				Suport_Email 		=	(String) con.getItem(ItemIda).getItemProperty("CEA_SUPORT_EMAIL_ADRESSE").getValue();
    				MaxFehlversuchUser 	   	=   (int) con.getItem(ItemIda).getItemProperty("CEA_MAX_LOGIN_FAILD_TO_Lock_ACC").getValue();
    				MaxFehlversuchIP   	    =   (int) con.getItem(ItemIda).getItemProperty("CEA_MAX_LOGIN_FAILD_TO_Lock_IP").getValue();
    				
    				
    				/********************************-Lizenz-Check-Informationen-****************************************/
    				Last_Update				=  (String) con.getItem(ItemIda).getItemProperty("CEA_LAST_LIZENZ_UPDATE").getValue();
    				Last_Work_Day			=  (String) con.getItem(ItemIda).getItemProperty("CEA_LAST_WORK_DAY").getValue();
    				Update_Intervalle	    =  (String) con.getItem(ItemIda).getItemProperty("CEA_UPDATE_INTERVALL").getValue();
    				
    				/********************************-SMTP-Informationen-****************************************/
//    				SMTP_Adresse		    =  (String) con.getItem(ItemIda).getItemProperty("CEA_SMTP_SERVER_ADRESSE").getValue();
//    				SMTP_Username		    =  (String) con.getItem(ItemIda).getItemProperty("CEA_SMTP_USER").getValue();
//    				SMTP_Passwort		    =  (String) con.getItem(ItemIda).getItemProperty("CEA_SMTP_PASSWORT").getValue();
//    				SMTP_Port		        =  (String) con.getItem(ItemIda).getItemProperty("CEA_SMTP_PORT").getValue();
    				
    				/********************************-Passwort Richtlinien-****************************************/
    				Kompl_pr         		=   (boolean) con.getItem(ItemIda).getItemProperty("CEA_PSW_KOMP_R").getValue();
    				min_psw_length         	=   (int) con.getItem(ItemIda).getItemProperty("CEA_PSW_MIN_LEN").getValue();
    				Liste_pr         	    =   (boolean) con.getItem(ItemIda).getItemProperty("CEA_PSW_LISTE_PR").getValue();
    				Zahl_pr         	    =   (boolean) con.getItem(ItemIda).getItemProperty("CEA_PSW_INT_PR").getValue();
    				Char_pr         	    =   (boolean) con.getItem(ItemIda).getItemProperty("CEA_PSW_CHAR_PR").getValue();
    				Grösse_pr         	    =   (boolean) con.getItem(ItemIda).getItemProperty("CEA_PSW_GRÖSSE").getValue();
    				Style_ID         		=   (int) con.getItem(ItemIda).getItemProperty("CEA_STYLE_ID").getValue();
    				
    				/**********************************Custom *****************************************************/
    				Email_CS  				=   (String) con.getItem(ItemIda).getItemProperty("CEA_CS_EMAIL").getValue();
    				
    				globalPollingInterval   =   (int) con.getItem(ItemIda).getItemProperty("CEA_POLLING_INTERVAL").getValue(); 
    		        //UserId = (int) result.getItem(ItemIda).getItemProperty(this.Spalte_ID)
    		          //     .getValue();
    			}
    		}
		
	}
	

}
