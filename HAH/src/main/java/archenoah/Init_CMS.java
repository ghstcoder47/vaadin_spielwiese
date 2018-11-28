/**
 * 
 * 
 * 
 * 									Archenoah Version 0.1
 * 
 * 									Author : 	Asan Kaceri
 * 
 *
 */
package archenoah;

import archenoah.config.CMS_Config_Std;
import archenoah.global.GlobalNotifier;
import archenoah.lib.custom.GlobalErrorHandler;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.web.ieerror.Init_ieerror;
import archenoah.web.normal.Init_Login;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.event.UIEvents.PollEvent;
import com.vaadin.event.UIEvents.PollListener;
import com.vaadin.server.Page;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.UI;

public class Init_CMS {

    private final GlobalNotifier notifier = new GlobalNotifier();
    
    public Init_CMS() {

        notifier.checkNotifications();
        UI.getCurrent().getUI().addPollListener(new PollListener() {

            @Override
            public void poll(PollEvent event) {
                notifier.checkNotifications();
            }
        });
        
        
        /***************************** Allgemeine Aufrufe **********************************/

        WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
        CMS_Config_Std conf = CMS_Config_Std.getInstance();
        UI.getCurrent().setLocale(webBrowser.getLocale());

        if (conf.EmailErrorEnabled) {
            UI.getCurrent().setErrorHandler(new GlobalErrorHandler());
        }

        if (webBrowser.isTooOldToFunctionProperly()) {
            new Init_ieerror();
            return;
        }

        if (conf.Test_No_Database == true) {
            return;
        }

        if (conf.Aktiv_APP.equals("1") != true) {
            return;
        }



        DBClass db = new DBClass();

        if (db.DB_Data_Advanced.DB_Database_Func_CheckDBExistence() == false) {
            return;
        }

        if (Whitelistprüfung(webBrowser.getAddress()) != true) {
            if (Blacklistprüfung(webBrowser.getAddress()) == true) {
                return;
            }
        }

        new Init_Login();
        UI.getCurrent().getUI().setPollInterval(conf.globalPollingInterval);

    }

    public boolean Blacklistprüfung(String IPA) {

        if (IPA.equals("0:0:0:0:0:0:0:1")) {
            IPA = "127.0.0.1";
        }

        DBClass db = new DBClass();
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_All();
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_ip_waechter_liste");
        db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_ip_waechter_liste", "IWL_TYP_ID", "=", "2", "AND");
        db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_ip_waechter_liste", "IWL_IP_ADRESSE", "=", "'" + IPA + "'", "");
        SQLContainer conta = db.DB_Data_Get.DB_SEND_AND_GET();

        if (conta.size() > 0) {
            db.DB_Data_Get.connclose();
            return true;

        } else {
            db.DB_Data_Get.connclose();
            return false;
        }

    }

    public boolean Whitelistprüfung(String IPA) {

        if (IPA.equals("0:0:0:0:0:0:0:1")) {
            IPA = "127.0.0.1";
        }
        DBClass db = new DBClass();
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_All();
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_ip_waechter_liste");
        db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_ip_waechter_liste", "IWL_TYP_ID", "=", "1", "AND");
        db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_ip_waechter_liste", "IWL_IP_ADRESSE", "=", "'" + IPA + "'", "");
        SQLContainer conta = db.DB_Data_Get.DB_SEND_AND_GET();

        if (conta.size() > 0) {
            db.DB_Data_Get.connclose();
            return true;

        } else {
            db.DB_Data_Get.connclose();
            return false;
        }

    }
}
