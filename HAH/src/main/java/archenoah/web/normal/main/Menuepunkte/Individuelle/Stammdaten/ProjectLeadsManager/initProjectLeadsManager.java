package archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.ProjectLeadsManager;


import archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.ProjectLeadsManager.form.ProjectLeadsDataView;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

public class initProjectLeadsManager {


	private TabSheet tab;
	private ProjectLeadsDataView dataview;
	
	public initProjectLeadsManager(MenuItem menü)
	{
		tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");
		
		
		if(Prüfungtab(menü.getText())== true)
		{
			return;
		}
		
		dataview = new ProjectLeadsDataView(menü);
		dataview.setPermissionsFromDatabase();
		dataview.init(tab);
			
	}
	
	public ProjectLeadsDataView getDataView() {
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
