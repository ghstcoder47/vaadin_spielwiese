package archenoah.web.normal.main.Menuepunkte.Individuelle;

import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;

import archenoah.lib.vaadin.resources.Icons;

public class Init_Menüpunkt_Individuelle {

    private final MenuBar.MenuItem menu;
    private final Individuelle_Manue_Comands cmd;

    public Init_Menüpunkt_Individuelle(MenuItem menu) {

        this.menu = menu;

        cmd = new Individuelle_Manue_Comands();

        UI.getCurrent().getSession().getSession().setAttribute("individualCommands", cmd);

        Menü_Stammdaten();
        TaskSystem();
        Menü_Projekte();
        menu_Ordermanager();
        
    }

    public void Menü_Stammdaten() {
        final MenuBar.MenuItem Stammdaten = menu.addItem("INDV_Stammdaten", Icons.Black.stamm_16, null);
        Stammdaten.addItem("INDV_Stammdaten_Patientenverwaltung", Icons.Black.patient_16, cmd.Patientenverwaltung);
        Stammdaten.addItem("INDV_Stammdaten_Krankenschwesternverwaltung", Icons.Black.krankenschwester_16, cmd.Krankenschwesternverwaltung);
        Stammdaten.addItem("INDV_Stammdaten_Ärzteverwaltung", Icons.Black.arzt_16, cmd.Ärzteverwaltung);
        Stammdaten.addItem("INDV_Stammdaten_RegionalLeads", Icons.Black.info_16, cmd.RegionalLeads);
        Stammdaten.addItem("INDV_Stammdaten_ProjectLeads", Icons.Black.info_16, cmd.ProjectLeads);
        Stammdaten.addItem("INDV_Stammdaten_ADManager", Icons.Black.stamm_16, cmd.ADManager);
        Stammdaten.addItem("INDV_Stammdaten_CalendarManager", Icons.Black.projekte_16, cmd.CalendarManager);
//        Stammdaten.addItem("INDV_Stammdaten_Krankenkassen", Icons.Black.kasse_16, null);
//        Stammdaten.addItem("INDV_Stammdaten_Pharmafirmen", Icons.Black.pharma_16, null);
        Stammdaten.addItem("INDV_Stammdaten_Organisationen", Icons.Black.pharma_16, cmd.Organisationsverwaltung);
    }
    
    public void TaskSystem() {
        final MenuBar.MenuItem TaskSystem = menu.addItem("INDV_TaskSystem", Icons.Black.info_16, null);
        /**/ TaskSystem.addItem("INDV_TaskSystem_TaskManager", Icons.Black.info_16, cmd.TaskSystemTaskManager);	
        /**/ TaskSystem.addItem("INDV_TaskSystem_MeetingLogging", Icons.Black.info_16, cmd.TaskSystemMeetinglogging);
        /**/ TaskSystem.addItem("INDV_TaskSystem_Audit_Logging", Icons.Black.info_16, cmd.TaskSystemAuditlogging);
        /**/ TaskSystem.addItem("INDV_TaskSystem_TaskGroups", Icons.Black.info_16, cmd.TaskSystemTaskGroups);	
    }
    
    public void Menü_Projekte() {
        final MenuBar.MenuItem Projekte = menu.addItem("INDV_Projekte", Icons.Black.projekte_16, null);

        final MenuBar.MenuItem Projekte_Übersicht = Projekte.addItem("INDV_Projekte_Projektübersicht", Icons.Black.projekte_u_16, null);
        // Projekte_Übersicht.addItem("INDV_Projekte_Projektübersicht_Esbriet",null,
        // null);
        
        Projekte_Übersicht.addItem("INDV_Projekte_Projektübersicht_Cockpit", Icons.Black.projekte_u_16, cmd.NurseCockpit);
        Projekte_Übersicht.addItem("INDV_Projekte_Projektübersicht_Combined", Icons.Black.info_16, cmd.Combined);
        
        Projekte_Übersicht.addItem("INDV_Projekte_Projektübersicht_Forsteo", Icons.Black.pharma_16, cmd.Forsteoverwaltung);
        Projekte_Übersicht.addItem("INDV_Projekte_Projektübersicht_Replagal", Icons.Black.pharma_16, cmd.Replagalverwaltung);
        Projekte_Übersicht.addItem("INDV_Projekte_Projektübersicht_Vpriv", Icons.Black.pharma_16, cmd.VprivVerwaltung);
        Projekte_Übersicht.addItem("INDV_Projekte_Projektübersicht_PoP", Icons.Black.pharma_16, cmd.PoPVerwaltung);
        Projekte_Übersicht.addItem("INDV_Projekte_Projektübersicht_ServiceTasking", Icons.Black.pharma_16, cmd.Servicetasking);
        
        Projekte_Übersicht.addItem("INDV_Projekte_Projektübersicht_Expenses", Icons.Black.kasse_16, cmd.Expenses);
        
        final MenuBar.MenuItem Projekte_verwaltung = Projekte.addItem("INDV_Projekte_Projektverwaltung", Icons.Black.projekte_v_16, null);
        Projekte_verwaltung.addItem("INDV_Projekte_Projektverwaltung_Projekt_Zuweisung", Icons.Black.projekte_z_16, cmd.Projektzuweisungsverwverwaltung);
        Projekte_verwaltung.addItem("INDV_Projekte_Projektverwaltung_ServicetaskingPlanner", Icons.Black.projekte_16, cmd.ServicetaskingPlanner);
        Projekte_verwaltung.addItem("INDV_Projekte_Proximity_Tool", Icons.Black.ip_16, cmd.ProximityTool);
        
        final MenuBar.MenuItem PrescriptionManager = Projekte.addItem("INDV_Projekte_Prescription", Icons.Black.info_16, null);
        PrescriptionManager.addItem("INDV_Projekte_PrescriptionManager", Icons.Black.info_16, cmd.PrescriptionManager);
        PrescriptionManager.addItem("INDV_Projekte_BalanceManager", Icons.Black.info_16, cmd.BalanceManager);
        
        final MenuBar.MenuItem StockManager = Projekte.addItem("INDV_Projekte_Stock", Icons.Black.info_16, null);
        StockManager.addItem("INDV_Projekte_StockManager", Icons.Black.info_16, cmd.StockManager);

    }
    
    public void menu_Ordermanager(){
        final MenuBar.MenuItem Ordermanager = menu.addItem("INDV_Ordermanager", Icons.Black.info_16, null);
        
        Ordermanager.addItem("INDV_Ordermanager_TicketPreferences", Icons.Black.info_16, cmd.OrdermanagerTicketPreferences);
        Ordermanager.addItem("INDV_Ordermanager_StandaloneSettings", Icons.Black.info_16, cmd.OrdermanagerStandaloneSettings);
        Ordermanager.addItem("INDV_Ordermanager_PackageCreator", Icons.Black.info_16, cmd.OrdermanagerPackageCreator);
        Ordermanager.addItem("INDV_Ordermanager_OpenTickets", Icons.Black.info_16, cmd.OrdermanagerOpenTickets);
        Ordermanager.addItem("INDV_Ordermanager_InventoryManager", Icons.Black.info_16, cmd.OrdermanagerInventory);
    }

}
