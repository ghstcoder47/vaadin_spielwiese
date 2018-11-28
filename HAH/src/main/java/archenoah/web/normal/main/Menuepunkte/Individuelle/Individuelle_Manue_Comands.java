package archenoah.web.normal.main.Menuepunkte.Individuelle;

import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

import archenoah.lib.vaadin.Language.i18n.I18nManager;
import archenoah.web.normal.main.Menuepunkte.Individuelle.AuditManager.AuditLogging.initAuditLogging;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.InventoryManager.initInventoryManager;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.OpenTickets.initOpenTickets;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.PackageCreator.initPackageCreator;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.StandaloneSettings.initStandaloneSettings;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.TicketPreferences.initTicketPreferences;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Prescriptions.BalanceManager.initBalanceManager;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Prescriptions.PrescriptionManager.initPrescriptionManager;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.ServicetaskingPlanner.initServiceTaskingPlanner;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.combined.initCombined;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.expenses.initExpenses;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.forsteo.Init_forsteoverwaltung;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.nursecockpit.Init_nursecockpit;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.pop.initPoP;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.replagal.Init_replagalverwaltung;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.initServiceTasking;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.vpriv.initVpriv;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektzuweisung.Init_Projektzuweisungswaltung;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektzuweisung.Form.form_zuweisung_analyse;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Stock.StockManager.initStockManager;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.ADManager.ADManagerInit;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.CalendarManager.initCalendarManager;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Krankenschwesternverwaltung.Init_Krankenschwesternverwaltung;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Organisationsverwaltung.Init_Organisationsverwaltung;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Patientenverwaltung.Init_Patientenverwaltung;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.ProjectLeadsManager.initProjectLeadsManager;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.RegionalLeadsManager.initRegionalLeadsManager;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Ärztverwaltung.Init_Ärzteverwaltung;
import archenoah.web.normal.main.Menuepunkte.Individuelle.TaskSystem.MeetingLogging.initMeetingLogging;
import archenoah.web.normal.main.Menuepunkte.Individuelle.TaskSystem.TaskGroups.initTaskGroups;
import archenoah.web.normal.main.Menuepunkte.Individuelle.TaskSystem.TaskManager.initTaskManager;

public class Individuelle_Manue_Comands {

    public Individuelle_Manue_Comands() {
        // TODO Automatisch generierter Konstruktorstub

    }

    /********************* ----> Stammdaten <------- **********************/
    public Command Krankenschwesternverwaltung = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            // TODO Auto-generated method stub
            I18nManager.clearManagers();
            Init_Krankenschwesternverwaltung ik = new Init_Krankenschwesternverwaltung(selectedItem);
        }
    };
    public Command Ärzteverwaltung = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            // TODO Auto-generated method stub
            I18nManager.clearManagers();
            Init_Ärzteverwaltung ik = new Init_Ärzteverwaltung(selectedItem);
        }
    };
    public Command Patientenverwaltung = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            // TODO Auto-generated method stub
            I18nManager.clearManagers();
            Init_Patientenverwaltung ik = new Init_Patientenverwaltung(selectedItem);
        }
    };
    public Command ADManager = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            // TODO Auto-generated method stub
            I18nManager.clearManagers();
            ADManagerInit ik = new ADManagerInit(selectedItem);
        }
    };

    public Command CalendarManager = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            I18nManager.clearManagers();
            initCalendarManager ik = new initCalendarManager(selectedItem);
        }
    };
    
    public Command RegionalLeads = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            I18nManager.clearManagers();
            initRegionalLeadsManager ik = new initRegionalLeadsManager(selectedItem);
        }
    };
    
    public Command ProjectLeads = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            I18nManager.clearManagers();
            initProjectLeadsManager ik = new initProjectLeadsManager(selectedItem);
        }
    };
    
    public Command Organisationsverwaltung = new Command(){

		@Override
		public void menuSelected(MenuItem selectedItem) {
			// TODO Auto-generated method stub
			I18nManager.clearManagers();
			Init_Organisationsverwaltung ik = new Init_Organisationsverwaltung(selectedItem);
		}
    	
    };
    /***********************************************************************/

    /********************* ----> TaskSystem <------- **********************/
    
    
    public Command TaskSystemTaskManager = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            I18nManager.clearManagers();
            new initTaskManager(selectedItem);
        }
    };
    
    public Command TaskSystemMeetinglogging = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            I18nManager.clearManagers();
            new initMeetingLogging(selectedItem);
        }
    };
    
    public Command TaskSystemAuditlogging = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            I18nManager.clearManagers();
            new initAuditLogging(selectedItem);
        }
    };
    
    public Command TaskSystemTaskGroups = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            I18nManager.clearManagers();
            new initTaskGroups(selectedItem);
        }
    };
    /********************* ----> Projekte <------- **********************/
    public Command Projektzuweisungsverwverwaltung = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            // TODO Auto-generated method stub
            I18nManager.clearManagers();
            Init_Projektzuweisungswaltung ik = new Init_Projektzuweisungswaltung(selectedItem);
        }
    };
    
    public Command ProximityTool = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            
            form_zuweisung_analyse prox = new form_zuweisung_analyse(selectedItem);
            prox.Init_Class_Window();
        }
    };
    
    public Command ServicetaskingPlanner = new Command() {
        
        @Override
        public void menuSelected(MenuItem selectedItem) {
            I18nManager.clearManagers();
            new initServiceTaskingPlanner(selectedItem);
        }
    };
    
    public Command PrescriptionManager = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            I18nManager.clearManagers();
        	initPrescriptionManager ik = new initPrescriptionManager(selectedItem);
        }
    };

    public Command StockManager = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            I18nManager.clearManagers();
            initStockManager ik = new initStockManager(selectedItem);
        }
    };
    
    public Command BalanceManager = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            I18nManager.clearManagers();
        	initBalanceManager ik = new initBalanceManager(selectedItem);
        }
    };
    
    public Command NurseCockpit = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            // TODO Auto-generated method stub
            I18nManager.clearManagers();
            Init_nursecockpit ik = new Init_nursecockpit(selectedItem);
        }
    };
    public Command Combined = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            // TODO Auto-generated method stub
            I18nManager.clearManagers();
            initCombined ik = new initCombined(selectedItem);
        }
    };
    public Command Forsteoverwaltung = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            // TODO Auto-generated method stub
            I18nManager.clearManagers();
            Init_forsteoverwaltung ik = new Init_forsteoverwaltung(selectedItem);
        }
    };
    public Command Replagalverwaltung = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            // TODO Auto-generated method stub
            I18nManager.clearManagers();
            Init_replagalverwaltung ik = new Init_replagalverwaltung(selectedItem);
        }
    };
    
    public Command VprivVerwaltung = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            // TODO Auto-generated method stub
            I18nManager.clearManagers();
            initVpriv ik = new initVpriv(selectedItem);
        }
    };
    
    public Command PoPVerwaltung = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            // TODO Auto-generated method stub
            I18nManager.clearManagers();
            initPoP ik = new initPoP(selectedItem);
        }
    };
    
    public Command Servicetasking = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            // TODO Auto-generated method stub
            I18nManager.clearManagers();
            initServiceTasking ik = new initServiceTasking(selectedItem);
        }
    };
    
    public Command Expenses = new Command(){

		@Override
		public void menuSelected(MenuItem selectedItem) {
			// TODO Auto-generated method stub
    		I18nManager.clearManagers();
    		initExpenses ik = new initExpenses(selectedItem);
		}
    };
    
    /********************* ----> Ordermanager <------- **********************/
    
    public Command OrdermanagerTicketPreferences = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            I18nManager.clearManagers();
            initTicketPreferences ik = new initTicketPreferences(selectedItem);
        }
    };
    
    public Command OrdermanagerStandaloneSettings = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            I18nManager.clearManagers();
            initStandaloneSettings ik = new initStandaloneSettings(selectedItem);
        }
    };
    
    public Command OrdermanagerPackageCreator = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            I18nManager.clearManagers();
            initPackageCreator ik = new initPackageCreator(selectedItem);
        }
    };
    
    public Command OrdermanagerOpenTickets = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            I18nManager.clearManagers();
        	initOpenTickets ik = new initOpenTickets(selectedItem);
        }
    };
    
    public Command OrdermanagerInventory = new Command() {
        @Override
        public void menuSelected(MenuItem selectedItem) {
            I18nManager.clearManagers();
            initInventoryManager ik = new initInventoryManager(selectedItem);
        }
    };
    
    /***********************************************************************/
}
