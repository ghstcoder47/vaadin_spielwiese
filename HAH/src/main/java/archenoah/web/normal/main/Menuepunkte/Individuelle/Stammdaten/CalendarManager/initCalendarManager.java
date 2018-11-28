package archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.CalendarManager;

import archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.CalendarManager.form.CalendarManagerDataView;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

public class initCalendarManager {


	private TabSheet tab;
	private CalendarManagerDataView dataview;
	
	public initCalendarManager(MenuItem menü)
	{
	    tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");
        
        
        if(Prüfungtab(menü.getText())== true)
        {
            return;
        }
        
        dataview = new CalendarManagerDataView(menü);
        dataview.setPermissionsFromDatabase();
        dataview.init(tab);
		
	}
	
	public CalendarManagerDataView getDataView() {
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
