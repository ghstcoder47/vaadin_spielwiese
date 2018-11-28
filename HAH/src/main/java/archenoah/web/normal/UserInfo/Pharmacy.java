package archenoah.web.normal.UserInfo;

import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;

import com.vaadin.data.Container;

public class Pharmacy {


    // {section fields}
    // ****************
    private Integer userId;
    private Boolean isPharmacy = false;
    // {end fields}

    // {section constructors}
    // **********************
    public Pharmacy(Integer userId) {
        this.userId = userId;
        getPermissions();
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    public Boolean isPharmacy() {
        return isPharmacy;
    }
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private void getPermissions() {
        isPharmacy = dbGetPharmacy();
    }
    // {end privatemethods}

    // {section database}
    // ******************
    private Boolean dbGetPharmacy() {
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select 1 as isPharmacy"
             + "\n " + "from cms_auth_liste_gu"
             + "\n " + "where AL_G_ID = 21 and AL_U_ID = " + userId;
        q.setSqlString(sql);
        Container con = q.query();
        
        return con != null && con.size() > 0;
    }
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
