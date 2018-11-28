package archenoah.lib.vaadin.MenuBerechtigung;

import archenoah.config.CMS_Config_Std;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;
import archenoah.lib.tool.java_plugin.array_builder.classes.ArrayBuilder_Einzel;
import archenoah.web.normal.UserInfo.UserData;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Individuelle_Manue_Comands;

import com.vaadin.data.Container;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;

public class Menu_Berechtigung2 {

    private String[] Grupp_ID;
    private String[] Group_Vorhanden = null;
    private Container container_Komplement;

    private String user_id;
    private MenuBar men;

    private Container con_men_punkte;
    private Container con_men_lng;

//    private String[][] ui_men_punkte;
    String[][] Con_inner;

    private String LNG_ID;

    private Individuelle_Manue_Comands cmd = (Individuelle_Manue_Comands) UI.getCurrent().getSession().getSession().getAttribute("individualCommands");

    /*************** --->Name_Vars<----- ******************/
    private String CMS_tabelle_menü_punkte;
    private String CMS_tabelle_menü_punkte_ID;
//    private String CMS_tabelle_menü_punkte_OM;
    private String CMS_tabelle_menü_punkte_UM;
    private String CMS_tabelle_menü_punkte_BER;
    private String CMS_tabelle_menü_punkte_set_pic;
    private String CMS_tabelle_menü_punkte_pic;
    private String CMS_tabelle_menü_punkte_enable;
//    private String CMS_tabelle_menü_punkte_bez_obm;
    private String CMS_auth_liste_menu;
    private String CMS_auth_liste_menu_UID;

    private String CMS_language_menu_liste;
    private String CMS_language_menu_liste_UID;
    private String CMS_language_menu_liste_LNG_ID;

    /****************************************************/

    public Menu_Berechtigung2()

    {
        user_id = UI.getCurrent().getSession().getSession().getAttribute("Userid").toString();
        men = (MenuBar) UI.getCurrent().getSession().getSession().getAttribute("menue");
        LNG_ID = (String) UI.getCurrent().getUI().getSession().getAttribute("Lng_Id");

        XML_AUS();

        // *******-> DB Pflege <---***/
        Get_Menue_punkte_DB();
//        Get_Menue_punkte_UI(men.getItems().get(0));
//        Fill_Men();
//        Get_Menue_punkte_DB();
//        Del_Men();
//        Get_Menue_punkte_DB();
        // ***************************/

        // *******-> Berrechtigung <---***/
        Rechteholen r = new Rechteholen(user_id);
        Con_inner = r.Datenholen();
        Gruppenholen(user_id);
        DBGruppeVorhanden();
        Komplement_Bestimmung();
        Bereinigen(men.getItems().get(0));

        // *******-> Sprache und Bilder <---***/
//        Get_Menue_punkte_UI(men.getItems().get(0));
        Get_Menue_lng();
        LNG_Men_PIC(men.getItems().get(0));
        // ************************************/

        // ******-> Öffne default tabs <---***/

        openDefaultSet();

        // ************************************/
    }

    /********************* Berrechtigung **********************/
    private void Gruppenholen(String U_ID) {
        CMS_Config_Std std = CMS_Config_Std.getInstance();
        DBClass rs = new DBClass();
        rs.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_auth_liste_gu");
        rs.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("AL_G_ID");
        rs.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_auth_liste_gu", "AL_U_ID", "=", U_ID, "");

        Container con = rs.DB_Data_Get.DB_SEND_AND_GET_Container();

        ArrayBuilder_Einzel arr;
        for (Object ItemIda : con.getItemIds()) {
            // UserId = (int) result.getItem(ItemIda).getItemProperty(this.Spalte_ID)
            // .getValue();

            arr = new ArrayBuilder_Einzel(Grupp_ID);
            Grupp_ID = arr.Array_Holen();

            Grupp_ID[Grupp_ID.length - 1] = con.getItem(ItemIda).getItemProperty("AL_G_ID").getValue().toString();
        }

    }

    private void DBGruppeVorhanden() {
        CMS_Config_Std std = CMS_Config_Std.getInstance();
        DBClass rs = new DBClass();
        rs.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_auth_liste_menu");
        rs.DB_Data_Get.DB_Spalten.Advanced.DB_Dinstinct("ALM_U_ID");

        for (int i = 0; i < Grupp_ID.length; i++) {
            if (i == 0) {
                rs.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_auth_liste_menu", "ALM_G_ID", "=", Grupp_ID[i], "");
            } else {
                rs.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("OR cms_auth_liste_menu", "ALM_G_ID", "=", Grupp_ID[i], "");
            }
        }

        Container con = rs.DB_Data_Get.DB_SEND_AND_GET_Container();

        ArrayBuilder_Einzel arr;
        if (con.size() == 0) {
            return;
        }
        for (Object ItemIda : con.getItemIds()) {
            // UserId = (int) result.getItem(ItemIda).getItemProperty(this.Spalte_ID)
            // .getValue();

            arr = new ArrayBuilder_Einzel(Group_Vorhanden);
            Group_Vorhanden = arr.Array_Holen();

            Group_Vorhanden[Group_Vorhanden.length - 1] = con.getItem(ItemIda).getItemProperty("ALM_U_ID").getValue().toString();

        }

    }

    private void Komplement_Bestimmung() {
        CMS_Config_Std std = CMS_Config_Std.getInstance();
        DBClass rs = new DBClass();
        rs.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("ASM_O_MEN");
        rs.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("ASM_U_MEN");
        rs.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_auth_stammdaten_menu");
        rs.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_auth_stammdaten_menu", "ASM_BER", "!=", "0", "AND");
        if (Group_Vorhanden == null) {
            container_Komplement = null;
            return;
        }
        for (int i = 0; i < Group_Vorhanden.length; i++) {
            if (i == 0) {
                rs.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_auth_stammdaten_menu", "ASM_ID", "!=", Group_Vorhanden[i], "");
            } else {
                rs.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("AND cms_auth_stammdaten_menu", "ASM_ID", "!=", Group_Vorhanden[i], "");
            }
        }

        container_Komplement = rs.DB_Data_Get.DB_SEND_AND_GET_Container();

    }

    private void Bereinigen(MenuItem Menü) {

        if (Menü.getSize() == -1) {
            // *** Prüfung Ob zum Löschen

            if (Menü_Del(Menü.getText()) == true) {

                MenuBar.MenuItem subitems = Menü;
                Menü.getParent().removeChild(subitems);
            }

            return;
        } else {
            int Count = Menü.getSize();

            for (int i = 0; i < Count; i++) {

                if (Count != Menü.getSize()) {
                    i = i - 1;
                    Count = Menü.getSize();
                }

                Bereinigen(Menü.getChildren().get(i));

            }

            if ((ZW_Menü(Menü.getText()) == true) && (Menü.getSize() == -1)) {

                if (Menü.getText() != "Start") {
                    MenuBar.MenuItem subitems = Menü;
                    Menü.getParent().removeChild(subitems);

                }
            }
        }
    }

    private boolean ZW_Menü(String Mename) {
        boolean gef = false;
        for (Object ItemIda : con_men_punkte.getItemIds()) {
            // UserId = (int) result.getItem(ItemIda).getItemProperty(this.Spalte_ID).getValue;
            //
            if (con_men_punkte.getItem(ItemIda).getItemProperty("ASM_O_MEN").getValue().equals(Mename) == true) {
                gef = true;
            }
        }

        return gef;
    }

    private boolean Menü_Del(String Menüname) {
        boolean Nicht_del = true;

        if (container_Komplement == null) {
            return true;
        }

        for (Object ItemIda : container_Komplement.getItemIds()) {
            // UserId = (int) result.getItem(ItemIda).getItemProperty(this.Spalte_ID).getValue;

            if (container_Komplement.getItem(ItemIda).getItemProperty("ASM_U_MEN").getValue().equals(Menüname) == true) {
                Nicht_del = false;
            }
        }

        if (Con_inner == null) {

        } else {
            // System.out.println(Con_inner.length);

            for (int i = 0; Con_inner.length > i; i++) {
                if ((Con_inner[i][2].equals(Menüname) == true) && (Con_inner[i][3].equals("1") == false)) {

                    Nicht_del = false;
                }
            }
        }

        if (Nicht_del == true) {
            return false;
        } else {
            return true;
        }

    }

    /********************* Menü Generierung ***********************/

    private void Get_Menue_punkte_DB() {
        CMS_Config_Std std = CMS_Config_Std.getInstance();
        DBClass rs = new DBClass();
        rs.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_All();
        rs.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln(CMS_tabelle_menü_punkte);

        con_men_punkte = rs.DB_Data_Get.DB_SEND_AND_GET_Container();
    }

//    private void Get_Menue_punkte_UI(MenuItem meni) {
//        if (meni.getSize() == 0) {
//            return;
//        } else {
//            int Count = meni.getSize();
//
//            for (int i = 0; i < Count; i++) {
//                ArrayBuilder_Mehrfach ar = new ArrayBuilder_Mehrfach(ui_men_punkte, 2);
//                ui_men_punkte = ar.Array_Holen();
//                ui_men_punkte[ui_men_punkte.length - 1][0] = meni.getText();
//                MenuItem tmpm = meni.getChildren().get(i);
//                ui_men_punkte[ui_men_punkte.length - 1][1] = tmpm.getText();
//                Get_Menue_punkte_UI(tmpm);
//            }
//        }
//    }

    private void Get_Menue_lng() {
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select * from cms_auth_stammdaten_menu";
        q.setSqlString(sql);
        
        con_men_lng = q.query();
        
//        CMS_Config_Std std = CMS_Config_Std.getInstance();
//        DBClass rs = new DBClass();
//        rs.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_All();
//        rs.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung(CMS_tabelle_menü_punkte, "INNER JOIN", CMS_language_menu_liste, CMS_tabelle_menü_punkte_ID, CMS_language_menu_liste_UID);
//        rs.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein(CMS_language_menu_liste, CMS_language_menu_liste_LNG_ID, "=", LNG_ID, "");
//        con_men_lng = rs.DB_Data_Get.DB_SEND_AND_GET_Container();

    }

//    private void Fill_Men() {
//        boolean gf = false;
//
//        for (int i = 0; i < ui_men_punkte.length; i++) {
//            if (con_men_punkte.size() == 0) {
//                Insert_Men(ui_men_punkte[i][0], ui_men_punkte[i][1]);
//            } else {
//                gf = false;
//
//                for (Object ItemIda : con_men_punkte.getItemIds()) {
//                    // UserId = (int) result.getItem(ItemIda).getItemProperty(this.Spalte_ID).getValue;
//
//                    if ((con_men_punkte.getItem(ItemIda).getItemProperty(CMS_tabelle_menü_punkte_OM).getValue().equals(ui_men_punkte[i][0]) == true) && (con_men_punkte.getItem(ItemIda).getItemProperty(CMS_tabelle_menü_punkte_UM).getValue().equals(ui_men_punkte[i][1]) == true)) {
//                        gf = true;
//                    }
//                }
//
//                if (gf == false) {
//                    Insert_Men(ui_men_punkte[i][0], ui_men_punkte[i][1]);
//                }
//
//            }
//
//        }
//    }

//    private void Del_Men() {
//        boolean gf = false;
//        for (Object ItemIda : con_men_punkte.getItemIds()) {
//            // UserId = (int) result.getItem(ItemIda).getItemProperty(this.Spalte_ID).getValue;
//            //
//            gf = false;
//            for (int i = 0; i < ui_men_punkte.length; i++) {
//                if (((con_men_punkte.getItem(ItemIda).getItemProperty(CMS_tabelle_menü_punkte_OM).getValue().equals(ui_men_punkte[i][0]) == true) && (con_men_punkte.getItem(ItemIda).getItemProperty(CMS_tabelle_menü_punkte_UM).getValue().equals(ui_men_punkte[i][1]) == true))
//                        || (boolean) con_men_punkte.getItem(ItemIda).getItemProperty(CMS_tabelle_menü_punkte_enable).getValue() == true) {
//                    gf = true;
//                }
//
//            }
//            if (gf == false) {
//                Delete_Men(con_men_punkte.getItem(ItemIda).getItemProperty(CMS_tabelle_menü_punkte_ID).getValue().toString());
//            }
//
//        }
//    }

    private void Insert_Men(String OM, String UM) {

        DBClass db = new DBClass();

        db.DB_Data_Insert.DB_Tabellen.DB_set_Tabelle_Einzeln(CMS_tabelle_menü_punkte);
//        db.DB_Data_Insert.DB_Daten.DB_Data(CMS_tabelle_menü_punkte, CMS_tabelle_menü_punkte_bez_obm, OM);
//        db.DB_Data_Insert.DB_Daten.DB_Data(CMS_tabelle_menü_punkte, CMS_tabelle_menü_punkte_OM, OM);
        db.DB_Data_Insert.DB_Daten.DB_Data(CMS_tabelle_menü_punkte, CMS_tabelle_menü_punkte_UM, UM);
        db.DB_Data_Insert.DB_Daten.DB_Data(CMS_tabelle_menü_punkte, CMS_tabelle_menü_punkte_BER, "0");
        db.DB_Data_Insert.DB_Daten.DB_Data(CMS_tabelle_menü_punkte, CMS_tabelle_menü_punkte_set_pic, "0");
        db.DB_Data_Insert.DB_Daten.DB_Data(CMS_tabelle_menü_punkte, CMS_tabelle_menü_punkte_pic, "");
        db.DB_Data_Insert.DB_Daten.DB_Data(CMS_tabelle_menü_punkte, CMS_tabelle_menü_punkte_enable, "0");
        db.DB_Data_Insert.DB_Insert();
    }

    private void Delete_Men(String ID) {

        CMS_Config_Std std = CMS_Config_Std.getInstance();
        DBClass db = new DBClass();

        db.DB_Data_Delete.DB_Tabellen.DB_set_Tabelle_Einzeln(CMS_tabelle_menü_punkte);
        db.DB_Data_Delete.DB_Filter.DB_WHERE_Allgemein(CMS_tabelle_menü_punkte, CMS_tabelle_menü_punkte_ID, "=", ID, "");
        db.DB_Data_Delete.DB_Delete();

        db = new DBClass();

        db.DB_Data_Delete.DB_Tabellen.DB_set_Tabelle_Einzeln(CMS_auth_liste_menu);
        db.DB_Data_Delete.DB_Filter.DB_WHERE_Allgemein(CMS_auth_liste_menu, CMS_auth_liste_menu_UID, "=", ID, "");
        db.DB_Data_Delete.DB_Delete();

        db = new DBClass();

        db.DB_Data_Delete.DB_Tabellen.DB_set_Tabelle_Einzeln(CMS_language_menu_liste);
        db.DB_Data_Delete.DB_Filter.DB_WHERE_Allgemein(CMS_language_menu_liste, CMS_language_menu_liste_UID, "=", ID, "");
        db.DB_Data_Delete.DB_Delete();
    }

    /**************************************************************/

    /*********************** LNG AND ICON *************************/
    private void LNG_Men_PIC(MenuItem meni) {
        boolean gf = false;

        if (meni.getSize() == 0) {
            return;
        } else {
            int Count = meni.getSize();

            for (int i = 0; i < Count; i++) {
                gf = false;

                MenuItem tmpm = meni.getChildren().get(i);

                for (Object ItemIda : con_men_lng.getItemIds()) {
                    // UserId = (int) result.getItem(ItemIda).getItemProperty(this.Spalte_ID).getValue;

                    //
                    if (con_men_lng.getItem(ItemIda).getItemProperty("ASM_U_MEN").getValue().equals(tmpm.getText()) == true) {
                        tmpm.setText((String) con_men_lng.getItem(ItemIda).getItemProperty("CLML_M_NAME").getValue());

                        if ((boolean) con_men_lng.getItem(ItemIda).getItemProperty("ASM_SET_PIC").getValue() == true && !((String) con_men_lng.getItem(ItemIda).getItemProperty("ASM_PIC").getValue()).equals("")) {
                            tmpm.setIcon(new ThemeResource((String) con_men_lng.getItem(ItemIda).getItemProperty("ASM_PIC").getValue()));
                        }

                        LNG_Men_PIC(tmpm);

                        gf = true;
                    }

                }

                if (gf == false) {

                    Insert_LNG(tmpm.getText());
                    Get_Menue_lng();
                    LNG_Men_PIC(tmpm);
                }
            }
        }
    }

    private void Insert_LNG(String M_name) {
        CMS_Config_Std std = CMS_Config_Std.getInstance();
        DBClass db = new DBClass();

        db.DB_Data_Insert.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_language_menu_liste");
        db.DB_Data_Insert.DB_Daten.DB_Data("cms_language_menu_liste", "CLML_M_ID", Get_M_ID(M_name));
        db.DB_Data_Insert.DB_Daten.DB_Data("cms_language_menu_liste", "CLML_M_NAME", M_name);
        db.DB_Data_Insert.DB_Daten.DB_Data("cms_language_menu_liste", "CLML_N_LNG", LNG_ID);

        db.DB_Data_Insert.DB_Insert();
    }

    private String Get_M_ID(String M_name) {
        CMS_Config_Std std = CMS_Config_Std.getInstance();
        DBClass db = new DBClass();

        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_auth_stammdaten_menu");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("ASM_ID");
        db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_auth_stammdaten_menu", "ASM_U_MEN", "=", "'" + M_name + "'", "");

        // db.DB_Data_Get.DB_DEBUG_SQL_STRING();
        Container con = db.DB_Data_Get.DB_SEND_AND_GET_Container();

        String mid = null;
        for (Object ItemIda : con.getItemIds()) {
            // UserId = (int) result.getItem(ItemIda).getItemProperty(this.Spalte_ID).getValue;
            //
            mid = con.getItem(ItemIda).getItemProperty("ASM_ID").getValue().toString();

        }

        return mid;
    }

    /**************************************************************/
    // TASK: DB-Integration
    private void openDefaultSet() {

        for (int i = 0; i < Grupp_ID.length; i++) {
            String group = Grupp_ID[i];

            switch (group) {
            case "12": // Nurse
                if(UserData.get().getNurse().isNurse() && UserData.get().getNurse().getNurseId() != null) {
                    runMenuItemCommand(men.getItems().get(0), cmd.NurseCockpit);
                }
                break;

            default:
                break;
            }

        }

    }

    private void runMenuItemCommand(MenuItem item, Command command) {

        Command lcom = item.getCommand();

        if (lcom != null) {

            if (command.hashCode() == lcom.hashCode()) {
                lcom.menuSelected(item);
                return;
            }

        }

        if (item.hasChildren()) {
            for (MenuItem child : item.getChildren()) {
                runMenuItemCommand(child, command);
            }
        }

    }

    private void XML_AUS() {
        CMS_tabelle_menü_punkte = "cms_auth_stammdaten_menu";
        CMS_tabelle_menü_punkte_ID = "ASM_ID";
//        CMS_tabelle_menü_punkte_bez_obm = "ASM_BEZ_OBM";
//        CMS_tabelle_menü_punkte_OM = "ASM_O_MEN";
        CMS_tabelle_menü_punkte_UM = "ASM_U_MEN";
        CMS_tabelle_menü_punkte_BER = "ASM_BER";
        CMS_tabelle_menü_punkte_set_pic = "ASM_SET_PIC";
        CMS_tabelle_menü_punkte_pic = "ASM_PIC";
        CMS_tabelle_menü_punkte_enable = "ASM_ENABLE";
        CMS_auth_liste_menu = "cms_auth_liste_menu";
        CMS_auth_liste_menu_UID = "ALM_U_ID";
        CMS_language_menu_liste = "cms_language_menu_liste";
        CMS_language_menu_liste_UID = "CLML_M_ID";
        CMS_language_menu_liste_LNG_ID = "CLML_N_LNG";

    }

}
