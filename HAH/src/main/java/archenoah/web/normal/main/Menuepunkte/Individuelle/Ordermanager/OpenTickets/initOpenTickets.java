package archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.OpenTickets;


import archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.OpenTickets.form.OpenTicketsDataView;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

public class initOpenTickets {


	private TabSheet tab;
	public initOpenTickets(MenuItem menü)
	{
		tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");
		
		
		if(Prüfungtab(menü.getText())== true)
		{
			return;
		}
		
		OpenTicketsDataView dv = new OpenTicketsDataView(menü);
		dv.setPermissionsFromDatabase();
		dv.init(tab);
			
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
