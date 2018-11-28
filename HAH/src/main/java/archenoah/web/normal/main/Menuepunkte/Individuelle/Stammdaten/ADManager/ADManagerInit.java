package archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.ADManager;



import archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.ADManager.form.ADManagerDataView;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

public class ADManagerInit {

	private TabSheet tab;
	public ADManagerInit(MenuItem menü) {
		
		tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");
		
		
		if(checkTab(menü.getText())== true)
		{
			return;
		}
		
		ADManagerDataView admdv = new ADManagerDataView(menü);
		admdv.setPermissionsFromDatabase();
		admdv.Init_Class_Tab(tab);
		
	}
	
	
	private boolean checkTab(String Name)
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
