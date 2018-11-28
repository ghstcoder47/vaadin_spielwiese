package archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Ärztverwaltung;


import archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Ärztverwaltung.Form.data_view_aerzteverwaltung;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

public class Init_Ärzteverwaltung {


	private TabSheet tab;
	public Init_Ärzteverwaltung(MenuItem menü)
	{
		tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");
		
		
		if(Prüfungtab(menü.getText())== true)
		{
			return;
		}
		
		data_view_aerzteverwaltung test = new data_view_aerzteverwaltung(menü);
		//test.Init_Class_Tab(tab);
		test.Set_Berrechtigung_Database();
		test.Init_Class_Tab(tab);
		
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
