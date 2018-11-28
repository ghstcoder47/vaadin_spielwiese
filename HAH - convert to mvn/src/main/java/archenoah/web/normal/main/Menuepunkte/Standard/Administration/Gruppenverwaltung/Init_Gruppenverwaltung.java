package archenoah.web.normal.main.Menuepunkte.Standard.Administration.Gruppenverwaltung;



import archenoah.web.normal.main.Menuepunkte.Standard.Administration.Gruppenverwaltung.Form.frm_gruppenverwaltung;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

public class Init_Gruppenverwaltung {

	private TabSheet tab;
	public Init_Gruppenverwaltung(MenuItem menü) {
		// TODO Automatisch generierter Konstruktorstub
		
		tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");
		
		
		if(Prüfungtab(menü.getText())== true)
		{
			return;
		}
		
		
		
		frm_gruppenverwaltung fbh = new frm_gruppenverwaltung(menü);
		fbh.Gen_Tab(tab);

		
		
		
		
		
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
