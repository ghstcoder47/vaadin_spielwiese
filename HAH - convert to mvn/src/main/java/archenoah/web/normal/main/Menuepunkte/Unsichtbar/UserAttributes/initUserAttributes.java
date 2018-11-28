package archenoah.web.normal.main.Menuepunkte.Unsichtbar.UserAttributes;


import archenoah.web.normal.main.Menuepunkte.Unsichtbar.UserAttributes.form.UserAttributesDataView;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

public class initUserAttributes {


	private TabSheet tab;
	private UserAttributesDataView dataview;
	
	public initUserAttributes(MenuItem menü)
	{
		tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");
		
		
		if(Prüfungtab(menü.getText())== true)
		{
			return;
		}
		
		dataview = new UserAttributesDataView(menü);
		dataview.init(tab);
			
	}
	
	public UserAttributesDataView getDataView() {
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
