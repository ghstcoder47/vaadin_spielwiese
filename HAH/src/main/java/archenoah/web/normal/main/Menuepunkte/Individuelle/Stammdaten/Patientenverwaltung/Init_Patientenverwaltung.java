package archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Patientenverwaltung;


import archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Patientenverwaltung.Form.PatientManagerDataView;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

public class Init_Patientenverwaltung {


	private TabSheet tab;
	public Init_Patientenverwaltung(MenuItem menu)
	
	{
		tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");
		
		
		if(Prüfungtab(menu.getText())== true)
		{
			return;
		}
		
		PatientManagerDataView pm = new PatientManagerDataView(menu);
		pm.setPermissionsFromDatabase();
		pm.init(tab);
		
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
