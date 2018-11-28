package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.vpriv;


import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.vpriv.form.VprivDataView;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

public class initVpriv {


	private TabSheet tab;
	VprivDataView dataview;
	
	public initVpriv(MenuItem menü)
	{
		tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");
		
		
		if(Prüfungtab(menü.getText())== true)
		{
			return;
		}
		
		dataview = new VprivDataView(menü);
		dataview.setPermissionsFromDatabase();
		dataview.init(tab);
			
	}
	
    public VprivDataView  getDataView(){
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
