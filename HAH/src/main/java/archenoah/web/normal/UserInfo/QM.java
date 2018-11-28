package archenoah.web.normal.UserInfo;

import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;

import com.vaadin.data.Container;

public class QM {


    // {section fields}
    // ****************
    private Integer userId;
    private Boolean isQM = false;
    // {end fields}

    // {section constructors}
    // **********************
    public QM(Integer userId) {
        this.userId = userId;
        getPermissions();
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    public Boolean isQM() {
        return isQM;
    }
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private void getPermissions() {
        isQM = dbGetQM();
    }
    // {end privatemethods}

    // {section database}
    // ******************
    private Boolean dbGetQM() {
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select 1 as isQM"
             + "\n " + "from cms_auth_liste_gu"
             + "\n " + "where AL_G_ID = 39 and AL_U_ID = " + userId;
        q.setSqlString(sql);
        Container con = q.query();
        
        return con != null && con.size() > 0;
    }
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
