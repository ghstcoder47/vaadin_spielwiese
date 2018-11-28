package archenoah.web.normal.main.Menuepunkte.Standard.Administration.Benutzerverwaltung.Validatoren;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import archenoah.config.CMS_Config_Std;
import archenoah.lib.tool.comunication.dbclass.DBClass;

import com.vaadin.data.Container;
import com.vaadin.data.Validator;
import com.vaadin.ui.PasswordField;

public class Custom_Benutzerform_psw_ändern implements Validator {

    private PasswordField Txt_Pass_1;
    private PasswordField Txt_Pass_2;

    private CMS_Config_Std std;

    public Custom_Benutzerform_psw_ändern(PasswordField Arg_Txt_Pass_1, PasswordField Arg_Txt_Pass_2) {
        // TODO Automatisch generierter Konstruktorstub
        Txt_Pass_1 = Arg_Txt_Pass_1;
        Txt_Pass_2 = Arg_Txt_Pass_2;
        std = CMS_Config_Std.getInstance();
    }

    @Override
    public void validate(Object value) throws InvalidValueException {
        // TODO Automatisch generierter Methodenstub
        if (isValid(value) == false) {
            throw new InvalidValueException("Die Passwörter stimmen nicht überein!");
        } else {
            if (std.Kompl_pr == true) {
                if (isValid_Komplex(value) == false) {
                    throw new InvalidValueException("Das Passwort entspricht nicht der Passwortrichtlinie");
                }
            }
        }

    }

    public boolean isValid_Komplex(Object value) {
        // TODO Automatisch generierter Methodenstub

        if (value.toString().length() >= std.min_psw_length && kompl(value) == true) {
            return true;
        } else {
            return false;
        }
    }

    private boolean kompl(Object value) {

        boolean back = true;

        if (std.Liste_pr == true) {
            if (Unerlaubte_Zeichenkette((String) value) == true) {
                back = false;
            }

        }
        if (std.Zahl_pr == true) {
            Pattern p = Pattern.compile("[0-9]");
            Matcher m = p.matcher(value.toString());
            if (m.find()) {

            } else {

                back = false;
            }

        }
        if (std.Char_pr == true) {
            Pattern p = Pattern.compile("[a-z]");
            Matcher m = p.matcher("12a3");
            if (m.find()) {

            } else {

                back = false;
            }

        }

        if (std.Grösse_pr == true) {
            Pattern p = Pattern.compile("[A-Z]");
            Matcher m = p.matcher(value.toString());
            if (m.find()) {

            } else {

                back = false;
            }

        }

        return back;
    }

    private boolean Unerlaubte_Zeichenkette(String value) {

        CMS_Config_Std std = CMS_Config_Std.getInstance();
        DBClass db = new DBClass();
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_All();
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_validator_passwort_bad_list");
        db.DB_Data_Get.DB_Filter.DB_WHERE_LIKE("cms_validator_passwort_bad_list", "PBL_NAME", "'%" + value + "%'", "OR");
        db.DB_Data_Get.DB_Filter.DB_WHERE_LIKE("cms_validator_passwort_bad_list", "PBL_NAME", "'" + value + "%'", "OR");
        db.DB_Data_Get.DB_Filter.DB_WHERE_LIKE("cms_validator_passwort_bad_list", "PBL_NAME", "'%" + value + "'", "OR");
        db.DB_Data_Get.DB_Filter.DB_WHERE_LIKE("cms_validator_passwort_bad_list", "PBL_NAME", "'" + value + "'", "");

        Container con = db.DB_Data_Get.DB_SEND_AND_GET_Container();

        if (con.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isValid(Object value) {
        // TODO Automatisch generierter Methodenstub

        System.out.println("isValid");
        System.out.println(value);
        System.out.println(Txt_Pass_1.getValue().equals(Txt_Pass_2.getValue()));

        if (Txt_Pass_1.getValue().equals(Txt_Pass_2.getValue()) == true) {
            return true;
        } else {
            return false;
        }

    }
}
