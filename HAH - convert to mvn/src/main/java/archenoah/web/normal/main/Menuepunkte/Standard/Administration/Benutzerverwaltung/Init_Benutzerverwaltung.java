package archenoah.web.normal.main.Menuepunkte.Standard.Administration.Benutzerverwaltung;



import archenoah.web.normal.main.Menuepunkte.Standard.Administration.Benutzerverwaltung.Form.UserManagerDataView;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

public class Init_Benutzerverwaltung {

	private TabSheet tab;
	public Init_Benutzerverwaltung(MenuItem menü) {
		// TODO Automatisch generierter Konstruktorstub
		
		tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");
		
		
		if(Prüfungtab(menü.getText())== true)
		{
			return;
		}
		
		
		
		//frm_benutzerverwaltung fbh = new frm_benutzerverwaltung(menü);
//		fbh.Gen_Tab(tab);
		
		UserManagerDataView umdv = new UserManagerDataView(menü);
		umdv.setPermissionsFromDatabase();
		umdv.Init_Class_Tab(tab);

		
		
		
		
		
	}
	
	
	private boolean Prüfungtab(String Name)
	{
		int Count = tab.getComponentCount();
		
		
		for(int i=0;i<Count;i++)
		{	
			if(tab.getTab(i).getCaption()==Name)
			{
		
				tab.setSelectedTab(i);
				
				return true;
			}
			
		}
		
		return false;	
	}

}
