package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.forsteo;

import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.forsteo.Form.ForsteoDataView;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

public class Init_forsteoverwaltung {

    private TabSheet tab;
    
    private ForsteoDataView dataview;
    
    public Init_forsteoverwaltung(MenuItem menü) {

        tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");

        if (Prüfungtab(menü.getText()) == true) {
            return;
        }

        dataview = new ForsteoDataView(menü);
        dataview.setPermissionsFromDatabase();
        dataview.initForsteoDataView(tab);


    }

    public ForsteoDataView getDataView(){
        return dataview;
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
