package archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Organisationsverwaltung;


import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.form.ServiceTaskingDataView;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Organisationsverwaltung.form.OrganisationDataView;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

public class Init_Organisationsverwaltung {


	private TabSheet tab;
	private OrganisationDataView dataview;
	
	public Init_Organisationsverwaltung(MenuItem menü)
	{
		tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");
		
		
		if(Prüfungtab(menü.getText())== true)
		{
			return;
		}
		
		dataview = new OrganisationDataView(menü);
		dataview.setPermissionsFromDatabase();
		dataview.init(tab);
			
	}
	
	public OrganisationDataView getDataView() {
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
