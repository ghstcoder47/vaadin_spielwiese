package archenoah.web.normal.main.Menuepunkte.Standard.Administration.Berrechtigungsverwaltung;

import archenoah.web.normal.main.Menuepunkte.Standard.Administration.Berrechtigungsverwaltung.forms.frm_berrechtigungsverwaltung_haupt_lists;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

public class Init_Berrechtigungsverwaltung {

	private TabSheet tab;

	public Init_Berrechtigungsverwaltung(MenuItem menü) {
		// TODO Automatisch generierter Konstruktorstub

		tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");

		if (Prüfungtab(menü.getText()) == true) {
			return;
		}

		frm_berrechtigungsverwaltung_haupt_lists fbh = new frm_berrechtigungsverwaltung_haupt_lists(menü);
		fbh.Gen_Tab(tab);

	}

	private boolean Prüfungtab(String Name) {
		int Count = tab.getComponentCount();

		for (int i = 0; i < Count; i++) {
			if (tab.getTab(i).getCaption() == Name) {

				tab.setSelectedTab(i);

				return true;
			}

		}

		return false;
	}

}
