package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.expenses;


import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.expenses.form.ExpensesDataView;

public class initExpenses {


	private TabSheet tab;
	private ExpensesDataView dataview;
	
	public initExpenses(MenuItem menü)
	{
		tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");
		
		
		if(Prüfungtab(menü.getText())== true)
		{
			return;
		}
		
		dataview = new ExpensesDataView(menü);
		dataview.setPermissionsFromDatabase();
		dataview.init(tab);
			
	}
	
	public ExpensesDataView getDataView() {
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
