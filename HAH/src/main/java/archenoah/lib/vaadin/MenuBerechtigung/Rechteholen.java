package archenoah.lib.vaadin.MenuBerechtigung;

import archenoah.config.CMS_Config_Std;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.java_plugin.array_builder.classes.ArrayBuilder_Einzel;
import archenoah.lib.tool.java_plugin.array_builder.classes.ArrayBuilder_Mehrfach;



import com.vaadin.data.Container;
import com.vaadin.data.util.sqlcontainer.SQLContainer;

public class Rechteholen {

	Container con ;
	
	private int[] ids=null;
	private int[]Distinct_Data;
	
	ArrayBuilder_Mehrfach arbm;
	
	private String user_id;
	private String Serveradresse;
	private String Datenbankname;
	private String Username;
	private String Passwort;
	private String Port;
	private String TabbelennameAuth;
	private String Spalte_User_ID;
	private String Spalte_User_G_ID;
	private String tbl_cms_menues;
	private String CMS_MENUES_Name;
	private String CMS_MENUES_UNTERMENU;
	private String CMS_MENUES_ID;

	private String tbl_cms_menu_berechtigung ;
	private String CMS_MENUE_BERECHTIGUNG_ID;
	private String CMS_MENUE_BERECHTIGUNG_G_ID;
	private String CMS_MENUE_BERECHTIGUNG_M_ID;
	private String CMS_MENUE_BERECHTIGUNG_LESEN;
	private String CMS_MENUE_BERECHTIGUNG_BEARBEITEN;
	private String CMS_MENUE_BERECHTIGUNG_LOESCHEN;
	private String CMS_MENUE_BERECHTIGUNG_ERSTELLEN;
	
	private String[][] data_Container = null;
	
	
	
	public Rechteholen(String User_id) {
		// TODO Automatisch generierter Konstruktorstub
		
		XML_AUS();
		this.user_id = User_id;
		
		Gruppen_ID grpid = new Gruppen_ID(user_id);
		ids = grpid.Grp_ID_Holen();
		
		if(ids == null)
		{
			return;
		}
		
Container condata = Container_Data();
		
		
		
	
		
		
		
		
		Container_DISTINCT();
		
		if(Distinct_Data == null)
		{
			
			data_Container = null;
			return;
		}
		
		String temp[]= null;
		ArrayBuilder_Einzel arb;
		int i = 0;
		
		
		//    Berechtigung
		
		
		int Lesen = 0 ;
		int Bearbeiten = 0 ;
		int Loeschen = 0 ;
		int Erstellen = 0;
		String CMS_MENUES_NAME_D = null;
		String CMS_MENUES_UNTERMENU = null;
		int CMS_MENUES_ID = 0;
		
		int g= 0;
		
		for(int b = 0; Distinct_Data.length>b;b++)
		{
			
			
		
			
	        for (Object cityItemId : condata.getItemIds()) {
	        
	        	
	        	
	        	
	        	
	        	
	        	if(Distinct_Data[b]==(int) condata.getItem(cityItemId).getItemProperty(CMS_MENUE_BERECHTIGUNG_M_ID).getValue())
	        	{
	        		
	        		CMS_MENUES_NAME_D = (String) condata.getItem(cityItemId).getItemProperty(CMS_MENUES_Name).getValue();
	        		CMS_MENUES_UNTERMENU = (String) condata.getItem(cityItemId).getItemProperty(this.CMS_MENUES_UNTERMENU).getValue();
	        		CMS_MENUES_ID = (int) condata.getItem(cityItemId).getItemProperty(this.CMS_MENUE_BERECHTIGUNG_M_ID).getValue();
	        		//******************************Lesen************************************************************** 
	        		
	        		
	        		if((int) condata.getItem(cityItemId).getItemProperty(CMS_MENUE_BERECHTIGUNG_LESEN).getValue()==1)
	        		{
	        		
	        			Lesen = 1;
	        		}else
	        		{
	        			if(Lesen == 1)
	        			{
	        				
	        				Lesen = 1;
	        			}else
	        			{
	        				if((int) condata.getItem(cityItemId).getItemProperty(CMS_MENUE_BERECHTIGUNG_LESEN).getValue()==0)
	        				{
	        					
	        					Lesen = 0;
	        				}else
	        				{
	        					Lesen = -1;
	        				}
	        				
	        			}
	        		}
	        		//******************************Lesen************************************************************** 
	        		//******************************Bearbeiten************************************************************** 
	        		if((int) condata.getItem(cityItemId).getItemProperty(CMS_MENUE_BERECHTIGUNG_BEARBEITEN).getValue()==1)
	        		{
	        			Bearbeiten = 1;
	        		}else
	        		{
	        			if(Bearbeiten == 1)
	        			{
	        				Bearbeiten = 1;
	        			}else
	        			{
	        				if((int) condata.getItem(cityItemId).getItemProperty(CMS_MENUE_BERECHTIGUNG_BEARBEITEN).getValue()==0)
	        				{
	        					Bearbeiten = 0;
	        				}else
	        				{
	        					Bearbeiten = -1;
	        				}
	        				
	        			}
	        		}
	        		
	        		
	        		//******************************Bearbeiten************************************************************** 
	        		//******************************LOESCHEN************************************************************** 
	        		if((int) condata.getItem(cityItemId).getItemProperty(CMS_MENUE_BERECHTIGUNG_LOESCHEN).getValue()==1)
	        		{
	        			Loeschen = 1;
	        		}else
	        		{
	        			if(Loeschen == 1)
	        			{
	        				Loeschen = 1;
	        			}else
	        			{
	        				if((int) condata.getItem(cityItemId).getItemProperty(CMS_MENUE_BERECHTIGUNG_LOESCHEN).getValue()==0)
	        				{
	        					Loeschen = 0;
	        				}else
	        				{
	        					Loeschen = -1;
	        				}
	        				
	        			}
	        		}
	        		
	        		
	        		//******************************LOESCHEN************************************************************** 
	        		//******************************ERSTELLEN************************************************************** 
	        		if((int) condata.getItem(cityItemId).getItemProperty(CMS_MENUE_BERECHTIGUNG_ERSTELLEN).getValue()==1)
	        		{
	        			Erstellen = 1;
	        		}else
	        		{
	        			if(Erstellen == 1)
	        			{
	        				Erstellen = 1;
	        			}else
	        			{
	        				if((int) condata.getItem(cityItemId).getItemProperty(CMS_MENUE_BERECHTIGUNG_ERSTELLEN).getValue()==0)
	        				{
	        					Erstellen = 0;
	        				}else
	        				{
	        					Erstellen = -1;
	        				}
	        				
	        			}
	        		}
	        		
	        		
	        		//******************************ERSTELLEN************************************************************** 
	        		
	        		
	        	
	        	}
	        	
	        }
			


	        //////////////////////////////////////////////////////////////
	        
	        
	        
	        if(this.data_Container == null)
			 {
	        	arbm = new ArrayBuilder_Mehrfach(data_Container,7);
		        data_Container = arbm.Array_Holen();
				 this.data_Container[0][0] = CMS_MENUES_ID +""; 
				 this.data_Container[0][1] = CMS_MENUES_NAME_D;
				 this.data_Container[0][2] = CMS_MENUES_UNTERMENU;
				 this.data_Container[0][3] = Lesen+"";
				 this.data_Container[0][4] = Bearbeiten+"";
				 this.data_Container[0][5] = Loeschen+"";
				 this.data_Container[0][6] = Erstellen+"";
				 
			 }else
				 
			 {
				 arbm = new ArrayBuilder_Mehrfach(data_Container,7);
			     data_Container = arbm.Array_Holen();
				 this.data_Container[this.data_Container.length-1][0] = CMS_MENUES_ID +""; 
				 this.data_Container[this.data_Container.length-1][1] = CMS_MENUES_NAME_D;
				 this.data_Container[this.data_Container.length-1][2] = CMS_MENUES_UNTERMENU;
				 this.data_Container[this.data_Container.length-1][3] = Lesen+"";
				 this.data_Container[this.data_Container.length-1][4] =  Bearbeiten+"";
				 this.data_Container[this.data_Container.length-1][5] =   Loeschen+"";
				 this.data_Container[this.data_Container.length-1][6] =  Erstellen+"";
			 }
			 
	        
			Lesen = 0;
			Bearbeiten = 0;
			Loeschen = 0;
			Erstellen = 0;
	        
	        
		}
		
		
	
	

	}
	
	
	
	public String[][] Datenholen()
	{
		
		
		return data_Container;
	}
	
	private void Distinct_Array_Füllen(SQLContainer cond)
	{
		
		int temp[]= null;
		ArrayBuilder_Einzel arb;
		int i = 0;
		
	
		
		
		
        for (Object cityItemId : cond.getItemIds()) {
        	
        	 arb = new ArrayBuilder_Einzel(temp);
			 temp = arb.Array_Holen_int();
			 
			 temp[i] = (int) cond.getItem(cityItemId).getItemProperty(CMS_MENUE_BERECHTIGUNG_M_ID).getValue();
			 
			 i++;
			 
        	
            
        }
		
		
        Distinct_Data = temp;
        
		
		
	}
	
	
	
	
	
	
	private Container Container_Data()
	{
		CMS_Config_Std std = CMS_Config_Std.getInstance();
		DBClass rsa = new DBClass();
		rsa.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_All();
		rsa.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung(tbl_cms_menu_berechtigung, "INNER JOIN", tbl_cms_menues, CMS_MENUE_BERECHTIGUNG_M_ID , CMS_MENUES_ID);
		
		
		
		
		
		
		for(int i = 0;ids.length>i;i++)
		{
			if(ids.length-1 != i)
			{
				rsa.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein(tbl_cms_menu_berechtigung, CMS_MENUE_BERECHTIGUNG_G_ID, "=", ids[i]+"", "OR");
				
			}else
			{
				
				rsa.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein(tbl_cms_menu_berechtigung, CMS_MENUE_BERECHTIGUNG_G_ID, "=", ids[i]+"", "");
			}
		}
		
	
		Container con = rsa.DB_Data_Get.DB_SEND_AND_GET_Container();
		
		
	
		
		
		return  con;	
		
		
	}
	
	
	
	private void Container_DISTINCT()
	{
		CMS_Config_Std std = CMS_Config_Std.getInstance();
		DBClass rs = new DBClass();
		rs.DB_Data_Get.DB_Spalten.Advanced.DB_Dinstinct(CMS_MENUE_BERECHTIGUNG_M_ID);
		rs.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung(tbl_cms_menu_berechtigung, "INNER JOIN", tbl_cms_menues, CMS_MENUE_BERECHTIGUNG_M_ID , CMS_MENUES_ID);
		rs.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_auth_stammdaten_menu", "ASM_BER", "!=", "0", "AND");
		
		
		
	
		
		for(int i = 0;ids.length>i;i++)
		{
			if(ids.length-1 != i)
			{
				rs.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein(tbl_cms_menu_berechtigung, CMS_MENUE_BERECHTIGUNG_G_ID, "=", ids[i]+"", "OR");
				
			}else
			{
				
				rs.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein(tbl_cms_menu_berechtigung, CMS_MENUE_BERECHTIGUNG_G_ID, "=", ids[i]+"", "");
			}
		}
		
		
		Container cont = rs.DB_Data_Get.DB_SEND_AND_GET_Container();
		
		
		int temp[]= null;
		ArrayBuilder_Einzel arb;
		int i = 0;
		
		
		
		if(cont == null)
		{
			
			
			Distinct_Data = null;
			
			return; 
		}
		
		
        for (Object cityItemId : cont.getItemIds()) {
        	
        	
        	 arb = new ArrayBuilder_Einzel(temp);
			 temp = arb.Array_Holen_int();
			 
			 temp[i] = (int) cont.getItem(cityItemId).getItemProperty(CMS_MENUE_BERECHTIGUNG_M_ID).getValue();
			
			 i++;
			 
        	
            
        }
		
        
    
		
        Distinct_Data = temp;
        
		
		
		
		
		
		
		
		
	}
	
	
	private void  XML_AUS()
	{
		
		    
		   TabbelennameAuth =  "cms_auth_liste_gu";
		   Spalte_User_ID =  "AL_U_ID";
		   Spalte_User_G_ID = "AL_G_ID";
		   tbl_cms_menues =  "cms_auth_stammdaten_menu";
		   CMS_MENUES_Name =  "ASM_O_MEN";
		   CMS_MENUES_UNTERMENU =  "ASM_U_MEN";
		   CMS_MENUES_ID =  "ASM_ID";
		   tbl_cms_menu_berechtigung =  "cms_auth_liste_menu" ;
		   CMS_MENUE_BERECHTIGUNG_ID =  "ALM_ID";
		   CMS_MENUE_BERECHTIGUNG_G_ID =  "ALM_G_ID";
		   CMS_MENUE_BERECHTIGUNG_M_ID =  "ALM_U_ID";
		   CMS_MENUE_BERECHTIGUNG_LESEN  = "ALM_LESEN";
		   CMS_MENUE_BERECHTIGUNG_BEARBEITEN  =  "ALM_BEARBEITEN";
		   CMS_MENUE_BERECHTIGUNG_LOESCHEN  =  "ALM_LOESCHEN";
		   CMS_MENUE_BERECHTIGUNG_ERSTELLEN  =  "ALM_ERSTELLEN";
		   

	}
		
	

}
