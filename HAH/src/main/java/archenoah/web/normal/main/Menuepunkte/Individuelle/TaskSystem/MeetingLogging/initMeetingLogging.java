package archenoah.web.normal.main.Menuepunkte.Individuelle.TaskSystem.MeetingLogging;


import archenoah.web.normal.main.Menuepunkte.Individuelle.TaskSystem.MeetingLogging.form.MeetingLoggingDataView;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

public class initMeetingLogging {


	private TabSheet tab;
	public initMeetingLogging(MenuItem menü)
	{
		tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");
		
		
		if(Prüfungtab(menü.getText())== true)
		{
			return;
		}
		
		MeetingLoggingDataView test = new MeetingLoggingDataView(menü);
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
