package archenoah.web.normal.main.Menuepunkte.Standard;




import archenoah.lib.vaadin.Language.i18n.I18nManager;
import archenoah.lib.vaadin.Logger.Logger;
import archenoah.web.normal.main.Menuepunkte.Standard.Administration.Benutzerverwaltung.Init_Benutzerverwaltung;
import archenoah.web.normal.main.Menuepunkte.Standard.Administration.Berrechtigungsverwaltung.Init_Berrechtigungsverwaltung;
import archenoah.web.normal.main.Menuepunkte.Standard.Administration.Gruppenverwaltung.Init_Gruppenverwaltung;
import archenoah.web.normal.main.Menuepunkte.Standard.Benutzereinstellungen.Passwortänderung.frm_passwortedit;
import archenoah.web.normal.main.Menuepunkte.Standard.Info.Releaseinfo.Form.frm_release_info;
import archenoah.web.normal.main.Menuepunkte.Standard.Info.Statistiken.auswertung_os;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;




public class Standard_Manue_Comands {


	
	public Standard_Manue_Comands() {
		// TODO Automatisch generierter Konstruktorstub
		

		
		
		
	}

	
	
	/*********************----> Administration <-------**********************/
	public Command Berrechtigungsverwaltung = new Command()
	{
		@Override
		public void menuSelected(MenuItem selectedItem) {
			// TODO Auto-generated method stub
		    I18nManager.clearManagers();
			Init_Berrechtigungsverwaltung ber = new Init_Berrechtigungsverwaltung(selectedItem);	
		}
	};
	public Command Gruppenverwaltung = new Command()
	{
		@Override
		public void menuSelected(MenuItem selectedItem) {
			// TODO Auto-generated method stub
		    I18nManager.clearManagers();
			Init_Gruppenverwaltung ber = new Init_Gruppenverwaltung(selectedItem);	
		}
	};
	
	public Command Benutzerverwaltung = new Command()
	{
		@Override
		public void menuSelected(MenuItem selectedItem) {
			// TODO Auto-generated method stub
		    I18nManager.clearManagers();
			Init_Benutzerverwaltung ber = new Init_Benutzerverwaltung(selectedItem);	
		}
	};
	
	
	
	
	/***********************************************************************/
	/*********************----> Benutzereinstellungen <-------**********************/
	public Command Passwortändern = new Command()
	{
		@Override
		public void menuSelected(MenuItem selectedItem) {
			// TODO Auto-generated method stub
		//	Init_Benutzerverwaltung ber = new Init_Benutzerverwaltung(selectedItem);	
		    I18nManager.clearManagers();
			frm_passwortedit fp = new frm_passwortedit(selectedItem);
		}
	};
	
	
	/***********************************************************************/
	
	/*********************----> Info <-------**********************/
	public Command Meeting_Webex = new Command()
	{
		@Override
		public void menuSelected(MenuItem selectedItem) {
			// TODO Auto-generated method stub
		
			UI.getCurrent().getUI().getPage().open(new ExternalResource("https://signin.webex.com/collabs/auth"), // URL
	                 "_blank",false);
			
			
		}
	};
	public Command Meeting_Teamviewer = new Command()
	{
		@Override
		public void menuSelected(MenuItem selectedItem) {
			// TODO Auto-generated method stub
		
			UI.getCurrent().getUI().getPage().open(new ExternalResource("http://www.teamviewer.com/download/TeamViewerQJ_de-ckc.exe"), // URL
	                 "_blank",false);
			
			
		}
	};
	
	public Command Fernwartung_Teamviewer = new Command()
	{
		@Override
		public void menuSelected(MenuItem selectedItem) {
			// TODO Auto-generated method stub
		
			UI.getCurrent().getUI().getPage().open(new ExternalResource("http://www.teamviewer.com/download/TeamViewerQS_de-ckc.exe"), // URL
	                 "_blank",false);
			
			
		}
	};
	
	
	public Command Releaseinfo = new Command()
	{
		@Override
		public void menuSelected(MenuItem selectedItem) {
			// TODO Auto-generated method stub
		
		    I18nManager.clearManagers();
		frm_release_info ri = new frm_release_info(selectedItem);
			
			
			
			
			
		}
	};
	
	
	public Command Chart_OS = new Command()
	{
		@Override
		public void menuSelected(MenuItem selectedItem) {
			// TODO Auto-generated method stub
		
		    I18nManager.clearManagers();
		auswertung_os asw = new auswertung_os();
			
			
			
			
		}
	};
	/***********************************************************************/
	
	/*
	 * 
	 * 				Abmelden
	 * 
	 * */
	public Command Abmelden = new Command()
	{
		@Override
		public void menuSelected(MenuItem selectedItem) {
			// TODO Auto-generated method stub
			
			/********Abmelden************/
			
			Abmelden();
		
			UI.getCurrent().getSession().close();
			UI.getCurrent().getPage().setLocation(UI.getCurrent().getPage().getLocation());
		}
		
	};
	
	private void Abmelden()
	{
		WebBrowser webBrowser = UI.getCurrent().getSession().getBrowser();
		
		
		Logger log = new Logger();
		log.Logging_Info("3", "Der Benutzer: \""+UI.getCurrent().getSession().getSession().getAttribute("Username")+"\" wurde Abgemeldet.", webBrowser.getAddress(),UI.getCurrent().getSession().getSession().getId());
		
		
	}
}
