package archenoah.web.normal.main.Menuepunkte.Standard;

import archenoah.lib.vaadin.resources.Icons;

import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;

public class Init_Menüpunkt_Standard {

    private MenuBar.MenuItem menu;
    private Standard_Manue_Comands cmd;

    public Init_Menüpunkt_Standard(MenuItem menu) {
        // TODO Automatisch generierter Konstruktorstub

        this.menu = menu;

        cmd = new Standard_Manue_Comands();
        UI.getCurrent().getSession().getSession().setAttribute("standardCommands", cmd);

        // ************************-----> Administration <--------****************************//
        Menü_Administrator();
        // ************************************************************************//
        // ************************-----> Benutzeradministration <--------****************************//
        Menü_Benutzereinstellungen();
        // ************************************************************************//
        // ************************-----> Info <--------****************************//
        Menü_Info();
        // ************************************************************************//
        // ************************-----> Schliessen <--------****************************//
        Menü_Schliessen();
        // *******************************************************************************//

    }

    public void Menü_Administrator() {
        final MenuBar.MenuItem Administration = menu.addItem("STD_Administration", Icons.Black.admin_16, null);
        Administration.addItem("STD_Administration_Benutzerverwaltung", Icons.Black.benutzer_16, cmd.Benutzerverwaltung);
        Administration.addItem("STD_Administration_Gruppenverwaltung", Icons.Black.gruppe_16, cmd.Gruppenverwaltung);
        Administration.addItem("STD_Administration_Berechtigungsverwaltung", Icons.Black.berechtigung_16, cmd.Berrechtigungsverwaltung);
//        Administration.addItem("STD_Administration_IP Adressen_Verwaltung", Icons.Black.ip_16, null);
//        Administration.addItem("STD_Administration_Einstellungen", Icons.Black.einstellungen_16, null);
//        Administration.addItem("STD_Administration_Backup_und_Restore", Icons.Black.backup_16, null);
//        Administration.addItem("STD_Administration_Log_Verwaltung", Icons.Black.log_16, null);
//        Administration.addItem("STD_Administration_Sessionmanager", Icons.Black.session_16, null);
    }

    public void Menü_Benutzereinstellungen() {
        final MenuBar.MenuItem Benutzereinstelungen = menu.addItem("STD_Benutzereinstelungen", Icons.Black.benutzer_16, null);
        Benutzereinstelungen.addItem("STD_Benutzereinstelungen_Passwortändern", Icons.Black.berechtigung_16, cmd.Passwortändern);

    }

    public void Menü_Info() {
        final MenuBar.MenuItem Info = menu.addItem("STD_Info", Icons.Black.info_16, null);

        final MenuBar.MenuItem Meeting = Info.addItem("STD_Info_Meeting", Icons.Black.meeting_16, null);
        Meeting.addItem("STD_Info_Meeting_WebEx", Icons.Other.webex, cmd.Meeting_Webex);
        Meeting.addItem("STD_Info_Meeting_Teamviewer", Icons.Other.teamviewer, cmd.Meeting_Teamviewer);

        final MenuBar.MenuItem Fernwartung = Info.addItem("STD_Info_Fernwartung", Icons.Black.fernwartung_16, null);
        Fernwartung.addItem("STD_Info_Fernwartung_Teamviewer", Icons.Other.teamviewer, cmd.Fernwartung_Teamviewer);

//        final MenuBar.MenuItem Statistik = Info.addItem("STD_Info_Statistik", null, null);
//        Statistik.addItem("STD_Info_Statistik_OS_CHART", null, cmd.Chart_OS);

//        Info.addItem("STD_Info_Ticket_Erstellen", Icons.Black.ticket_16, null);
//        Info.addItem("STD_Info_Release_Info", Icons.Black.about_16, cmd.Releaseinfo);
//        Info.addItem("STD_Info_Kontakt_Daten", Icons.Black.kontakt_16, null);
    }

    public void Menü_Schliessen() {
        menu.addItem("STD_Abmelden", Icons.Black.logout_16, cmd.Abmelden);
    }

}
