package archenoah.web.normal.main.Menuepunkte;

import archenoah.web.normal.main.Menuepunkte.Individuelle.Init_Menüpunkt_Individuelle;
import archenoah.web.normal.main.Menuepunkte.Standard.Init_Menüpunkt_Standard;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;

public class Init_Menüpunkte {

	public Init_Menüpunkte() {
		// TODO Automatisch generierter Konstruktorstub
		
	    MenuItem menu = (MenuItem) UI.getCurrent().getSession().getSession().getAttribute("menue_item");
	    
		Init_Menüpunkt_Individuelle imi = new Init_Menüpunkt_Individuelle(menu);
		Init_Menüpunkt_Standard mstd = new Init_Menüpunkt_Standard(menu);
		
		
		
	}

}
