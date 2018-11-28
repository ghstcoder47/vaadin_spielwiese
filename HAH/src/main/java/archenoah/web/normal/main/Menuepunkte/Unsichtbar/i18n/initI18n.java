package archenoah.web.normal.main.Menuepunkte.Unsichtbar.i18n;


import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

public class initI18n {


	private TabSheet tab;
	private I18nDataView dataview;
	
	public initI18n(MenuItem menü)
	{
		tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");
		
		
		if(Prüfungtab(menü.getText())== true)
		{
			return;
		}
		
		dataview = new I18nDataView(menü);
		dataview.init(tab);
		
	}
	
    public I18nDataView getDataView(){
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
