package archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.PackageCreator;


import archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.PackageCreator.form.PackageCreatorDataView;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

public class initPackageCreator {


	private TabSheet tab;
	public initPackageCreator(MenuItem menü)
	{
		tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");
		
		
		if(Prüfungtab(menü.getText())== true)
		{
			return;
		}
		
		PackageCreatorDataView dv = new PackageCreatorDataView(menü);
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
