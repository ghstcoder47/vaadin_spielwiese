package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.nursecockpit.classes;

import java.util.Date;

import archenoah.web.normal.main.Menuepunkte.Individuelle.Individuelle_Manue_Comands;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.forsteo.Init_forsteoverwaltung;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.pop.initPoP;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.replagal.Init_replagalverwaltung;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.initServiceTasking;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.vpriv.initVpriv;

import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;

public class ProjectTabOpener {

    // {section fields}
    // ****************
    private final Individuelle_Manue_Comands cmd = (Individuelle_Manue_Comands) UI.getCurrent().getSession().getSession().getAttribute("individualCommands");
    private Date month;

    // {end fields}

    // {section constructors}
    // **********************
    public ProjectTabOpener() {
        // TASK Auto-generated constructor stub
    }

    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************

    public void openTab(String projectCode, Date month) {

        this.month = (month != null) ? month : new Date();

        MenuBar men = (MenuBar) UI.getCurrent().getSession().getSession().getAttribute("menue");
        Command com = null;
        String project = projectCode;
        switch (project) {
        case "FOR":
            com = cmd.Forsteoverwaltung;
            break;
        case "REP":
            com = cmd.Replagalverwaltung;
            break;
        case "VPRIV":
            com = cmd.VprivVerwaltung;
            break;
        case "POP":
            com = cmd.PoPVerwaltung;
            break;
        case "SA":
            com = cmd.Servicetasking;
            break;
        default:
            break;
        }
        if (com != null) {
            for (MenuItem item : men.getItems()) {
                getMenuItemCommand(item, com);
            }
        }

    }

    // {end publicmethods}

    // {section privatemethods}
    // ************************

    private void getMenuItemCommand(MenuItem item, Command command) {

        Command lcom = item.getCommand();

        if (lcom != null) {

            if (command.hashCode() == lcom.hashCode()) {

                if (cmd.Forsteoverwaltung.equals(lcom)) {

                    Init_forsteoverwaltung forsteo = new Init_forsteoverwaltung(item);
                    if (forsteo.getDataView() != null) {
                        forsteo.getDataView().setGlobalFilterMonth(month);
                    }

                }

                if (cmd.Replagalverwaltung.equals(lcom)) {

                    Init_replagalverwaltung replagal = new Init_replagalverwaltung(item);
                    if (replagal.getDataView() != null) {
                        replagal.getDataView().setGlobalFilterMonth(month);
                    }

                }

                if (cmd.VprivVerwaltung.equals(lcom)) {

                    initVpriv vpriv = new initVpriv(item);
                    if (vpriv.getDataView() != null) {
                        vpriv.getDataView().setGlobalFilterMonth(month);
                    }

                }

                if (cmd.PoPVerwaltung.equals(lcom)) {

                    initPoP pop = new initPoP(item);
                    if (pop.getDataView() != null) {
                        pop.getDataView().setGlobalFilterMonth(month);
                    }

                }
                
                if (cmd.Servicetasking.equals(lcom)) {

                    initServiceTasking sa = new initServiceTasking(item);
                    if (sa.getDataView() != null) {
                        sa.getDataView().setGlobalFilterMonth(month);
                    }

                }
                
                return;
            }

        }
        
        if (item.hasChildren()) {
            for (MenuItem child : item.getChildren()) {
                getMenuItemCommand(child, command);
            }
        }
        
    }
    // {end privatemethods}

    // {section database}
    // ******************
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
