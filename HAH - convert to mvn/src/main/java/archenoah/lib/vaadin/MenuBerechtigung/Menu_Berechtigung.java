package archenoah.lib.vaadin.MenuBerechtigung;

import java.util.List;





import archenoah.config.CMS_Config_Std;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.java_plugin.array_builder.classes.ArrayBuilder_Einzel;



import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;

public class Menu_Berechtigung {

private String[] Grupp_ID;
private String[] Group_Vorhanden = null;
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
private DBClass rs;

SQLContainer container_Komplement = null;
String[][] Con_inner;



public Menu_Berechtigung(MenuBar menü,String User_ID)

{

	XML_AUS();
	
	Rechteholen r = new Rechteholen(User_ID);
    Con_inner = r.Datenholen();
    Gruppenholen(User_ID);
    DBGruppeVorhanden();
    
    
    
    
    
	
	Komplement_Bestimmung();
	
	
	
	if(Con_inner == null)
	{
		//System.out.println("Keine Berechtigung Vorhanden");
	}else
	{
		//System.out.println(Con_inner.length);
		
		for(int i = 0 ;Con_inner.length>i;i++)
		{
//			System.out.println("*************************************************************");
//			System.out.println(Con_inner[i][0]);
//			System.out.println(Con_inner[i][1]);
//			System.out.println(Con_inner[i][2]);
//			System.out.println(Con_inner[i][3]);
//			System.out.println(Con_inner[i][4]);
//			System.out.println(Con_inner[i][5]);
//			System.out.println(Con_inner[i][6]);
//			System.out.println("*************************************************************");
		}
		
	}
	
	Bereinigen(menü);
	
	menü.requestRepaint();
	
	
	rs.DB_Data_Get.connclose();
}


















private void Bereinigen(MenuBar Menü)
{
	int HPM_Size = Menü.getSize()-1;
	int b = 0;
	
	
	List lst = Menü.getItems();
	MenuBar.MenuItem subitem;
	
	for(int i = 0;HPM_Size>=i;i++)
	{
		

		
		subitem = ((MenuItem)lst.get(b));
	

		if(subitem.getSize() > -1)
		{
			
			Bereinigen_Sub_menu(subitem,subitem.getText());
			
			if(subitem.getSize() == -1)
			{
				
				Menü.removeItem(subitem);
				b--;
				
			}
			
		}else
		{
			Menü.removeItem(subitem);
			b--;
		}
		
		b++;
	}
}


private void Bereinigen_Sub_menu(MenuBar.MenuItem subitem,String Obermenü)
{
	int HPM_Size = subitem.getSize()-1;
	int b = 0;
	
	
	List lst = subitem.getChildren();
	MenuBar.MenuItem subitems;
	
	for(int i = 0;HPM_Size>=i;i++)
	{
	
		subitems = ((MenuItem)lst.get(b));
		
		if(subitems.getSize() > -1)
		{
			
			Bereinigen_Sub_menu(subitems,subitems.getText());
			
			if(subitems.getSize() == -1)
			{
				
				((MenuItem)lst.get(b)).getParent().removeChild(subitems);
				b--;
				
			}
			
		}else
		{
			
			
			if(prüfung(subitem.getText(),subitems.getText())== true)
			{
				((MenuItem)lst.get(b)).getParent().removeChild(subitems);
				b--;
			}
			
		
			
		}
		
		b++;
		
		
		
		
	}
	
	
	
}





private void Komplement_Bestimmung()
{
	CMS_Config_Std std = CMS_Config_Std.getInstance();
    rs = new DBClass();
	rs.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln(CMS_MENUES_UNTERMENU);
	rs.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln(CMS_MENUES_Name);
	rs.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_auth_stammdaten_menu");
	if(Group_Vorhanden == null)
	{
		container_Komplement = null;
		return;
	}
	for(int i = 0;i<Group_Vorhanden.length;i++)
	{
		if(i==0)
		{
			rs.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_auth_stammdaten_menu", "ASM_ID", "!=", Group_Vorhanden[i], "");
		}else
		{
			rs.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("AND cms_auth_stammdaten_menu", "ASM_ID", "!=", Group_Vorhanden[i], "");
		}
	}
	
	container_Komplement = rs.DB_Data_Get.DB_SEND_AND_GET();	

}

private void  XML_AUS()
{
	
	    
	TabbelennameAuth =  "cms_auth_liste_gu";
	   Spalte_User_ID =  "AL_U_ID";
	   Spalte_User_G_ID = "AL_G_ID";
	   tbl_cms_menues =  "cms_auth_stammdaten_menu";
	   CMS_MENUES_Name =  "ASM_UNTERMENU";
	   CMS_MENUES_UNTERMENU =  "ASM_NAME";
	   CMS_MENUES_ID =  "ASM_ID";
	   tbl_cms_menu_berechtigung =  "cms_auth_liste_menu" ;
	   CMS_MENUE_BERECHTIGUNG_ID =  "ALM_ID";
	   CMS_MENUE_BERECHTIGUNG_G_ID =  "ALM_G_ID";
	   CMS_MENUE_BERECHTIGUNG_M_ID =  "ALM_U_ID";
	   CMS_MENUE_BERECHTIGUNG_LESEN  = "ALM_LESEN";
	 

}

private boolean prüfung(String Obermenü ,String Menüpunkt)
{
	boolean Komp = false;
	boolean Inner = false;
	boolean Gesamt;
	String OK;
	String MK;
	
	
	if(container_Komplement == null|container_Komplement.size()>0)
	{
		
		
		
		for (Object cityItemId : container_Komplement.getItemIds()) {
			
			OK =  container_Komplement.getItem(cityItemId).getItemProperty(CMS_MENUES_UNTERMENU).getValue().toString();
			MK =  container_Komplement.getItem(cityItemId).getItemProperty(CMS_MENUES_Name).getValue().toString();
			
			
			 if((OK.equals(Obermenü))  && (MK.equals(Menüpunkt)))
			 {
				
				 Komp = true; 
				 break;
			 }else{
				 Komp = false; 
				 
			 }
			 
			 
           
       }
		

		
		
	}else{
		Komp = false;
		}
	
	if(Con_inner != null)
	{
		
		
		
		for(int i = 0;Con_inner.length>i;i++)
		{
			
			
			
			if(Obermenü.equals(Con_inner[i][1]) && Menüpunkt.equals(Con_inner[i][2])   && Con_inner[i][3].equals("0"))
			
			{
				
				Inner = true;
				break;
			}
			else
			{
				Inner = false;
				
			}
			
			
			
		}
		
		
		
		
		
		
		
		
		
	}else
	{
		Inner = false;
	}
	
	if(Komp == true || Inner == true)
	{
		Gesamt = true;
	}
	else
	{
		Gesamt = false;
	}
	

	return Gesamt;
	

}

private void Gruppenholen(String U_ID)
{
	CMS_Config_Std std = CMS_Config_Std.getInstance();
    DBClass rs = new DBClass();
    rs.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_auth_liste_gu");
    rs.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("AL_G_ID");
    rs.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_auth_liste_gu", "AL_U_ID", "=", U_ID, "");
    
    Container con = rs.DB_Data_Get.DB_SEND_AND_GET_Container();
    
    ArrayBuilder_Einzel arr;
		for (Object ItemIda : con.getItemIds()) {
            //UserId = (int) result.getItem(ItemIda).getItemProperty(this.Spalte_ID)
              //     .getValue();
			
			arr = new ArrayBuilder_Einzel(Grupp_ID);
			Grupp_ID = arr.Array_Holen();
			
			Grupp_ID[Grupp_ID.length-1]=con.getItem(ItemIda).getItemProperty("AL_G_ID").getValue().toString();
			
		
		}



}
private void DBGruppeVorhanden()
{
	CMS_Config_Std std = CMS_Config_Std.getInstance();
    DBClass rs = new DBClass();
    rs.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_auth_liste_menu");
    rs.DB_Data_Get.DB_Spalten.Advanced.DB_Dinstinct("ALM_U_ID");
    
    for(int i = 0;i < Grupp_ID.length;i++)
    {
    	if(i==0)
    	{
    	 rs.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_auth_liste_menu", "ALM_G_ID", "=", Grupp_ID[i], "");
    	}else
    	{
    		 rs.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("AND cms_auth_liste_menu", "ALM_G_ID", "=", Grupp_ID[i], "");
    	}
    }
    
    Container con = rs.DB_Data_Get.DB_SEND_AND_GET_Container();
    
    ArrayBuilder_Einzel arr;
    if(con.size()==0)
    {
    	return;
    }
		for (Object ItemIda : con.getItemIds()) {
            //UserId = (int) result.getItem(ItemIda).getItemProperty(this.Spalte_ID)
              //     .getValue();
			
			arr = new ArrayBuilder_Einzel(Group_Vorhanden);
			Group_Vorhanden = arr.Array_Holen();
			
			Group_Vorhanden[Group_Vorhanden.length-1]=con.getItem(ItemIda).getItemProperty("ALM_U_ID").getValue().toString();
			
		
		}

}




}
