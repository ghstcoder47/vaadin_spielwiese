package archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.RegionalLeadsManager;


import archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.RegionalLeadsManager.form.RegionalLeadsDataView;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

public class initRegionalLeadsManager {


	private TabSheet tab;
	private RegionalLeadsDataView dataview;
	
	public initRegionalLeadsManager(MenuItem menü)
	{
		tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");
		
		
		if(Prüfungtab(menü.getText())== true)
		{
			return;
		}
		
		dataview = new RegionalLeadsDataView(menü);
		dataview.setPermissionsFromDatabase();
		dataview.init(tab);
			
	}
	
	public RegionalLeadsDataView getDataView() {
	    return dataview;
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
