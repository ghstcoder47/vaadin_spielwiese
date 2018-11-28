package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektzuweisung;


import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektzuweisung.Form.ProjektzuweisungDataView;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

public class Init_Projektzuweisungswaltung {


	private TabSheet tab;
	public Init_Projektzuweisungswaltung(MenuItem menü)
	{
		tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");
		
		
		if(Prüfungtab(menü.getText())== true)
		{
			return;
		}
		
		ProjektzuweisungDataView dv = new ProjektzuweisungDataView(menü);
		dv.setPermissionsFromDatabase();
		dv.init(tab);
		
		//frm_Ärzteverwaltung fbh = new frm_Ärzteverwaltung(menü);
		//fbh.Gen_Tab(tab);
		
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
