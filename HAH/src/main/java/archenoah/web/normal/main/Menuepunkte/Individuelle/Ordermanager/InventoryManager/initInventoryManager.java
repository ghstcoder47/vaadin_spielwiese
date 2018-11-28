package archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.InventoryManager;


import archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.InventoryManager.form.InventoryManagerDataView;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

public class initInventoryManager {


	private TabSheet tab;
	public initInventoryManager(MenuItem menü)
	{
		tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");
		
		
		if(Prüfungtab(menü.getText())== true)
		{
			return;
		}
		
		InventoryManagerDataView test = new InventoryManagerDataView(menü);
		test.setPermissionsFromDatabase();
		test.init(tab);
			
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
