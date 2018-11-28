package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.replagal;


//import archenoah.lib.vaadin.Components.Window_Tab_Generierer.Build_Tab_Menue_Item;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.replagal.form.ReplagalDataView;

import com.vaadin.ui.MenuBar.MenuItem;
//import com.vaadin.ui.Window;
//import com.vaadin.server.ThemeResource;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
//import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektzuweisung.Form.data_view_projektzuweisungsverwaltung;
//import archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Patientenverwaltung.Form.data_view_patientenverwaltung;
//import archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Ärztverwaltung.Form.data_view_aerzteverwaltung;

public class Init_replagalverwaltung {


	private TabSheet tab;
	private ReplagalDataView dataview;
	
	public Init_replagalverwaltung(MenuItem menü)
	{
		tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");
		
		
		if(Prüfungtab(menü.getText())== true)
		{
			return;
		}
		
		dataview = new ReplagalDataView(menü);

		dataview.setPermissionsFromDatabase();
		dataview.init(tab);
		
	}
	
    public ReplagalDataView getDataView(){
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
