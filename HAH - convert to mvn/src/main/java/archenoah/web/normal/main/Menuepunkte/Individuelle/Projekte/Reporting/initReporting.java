package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Reporting;


import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Reporting.Form.ReportingDataView;

public class initReporting {


	private TabSheet tab;
	public initReporting(MenuItem menü)
	{		
		tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");
		
		
		if(Prüfungtab(menü.getText())== true)
		{
			return;
		}
		
		ReportingDataView dv = new ReportingDataView(menü);
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
