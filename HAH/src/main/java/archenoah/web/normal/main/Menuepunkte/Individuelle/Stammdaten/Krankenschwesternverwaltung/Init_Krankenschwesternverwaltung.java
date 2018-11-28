package archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Krankenschwesternverwaltung;

import archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Krankenschwesternverwaltung.forms.data_view_nurseverwaltung;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

public class Init_Krankenschwesternverwaltung {


	private TabSheet tab;
	public Init_Krankenschwesternverwaltung(MenuItem menü)
	{
		tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");
		
		
		if(Prüfungtab(menü.getText())== true)
		{
			return;
		}
		
		
		data_view_nurseverwaltung test = new data_view_nurseverwaltung(menü);
		//test.Init_Class_Tab(tab);
		test.Set_Berrechtigung_Database();
		test.Init_Class_Tab(tab);
		
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
