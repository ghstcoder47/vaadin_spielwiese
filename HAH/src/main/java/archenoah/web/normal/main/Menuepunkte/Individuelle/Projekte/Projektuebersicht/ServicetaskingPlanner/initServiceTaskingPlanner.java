package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.ServicetaskingPlanner;


import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.ServicetaskingPlanner.form.ServiceTaskingPlannerDataView;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

public class initServiceTaskingPlanner {


	private TabSheet tab;
	private ServiceTaskingPlannerDataView dataview;
	
	public initServiceTaskingPlanner(MenuItem menü)
	{
		tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");
		
		
		if(Prüfungtab(menü.getText())== true)
		{
			return;
		}
		
		dataview = new ServiceTaskingPlannerDataView(menü);
		dataview.setPermissionsFromDatabase();
		dataview.init(tab);
			
	}
	
	public ServiceTaskingPlannerDataView getDataView() {
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
