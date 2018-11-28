package archenoah.web.normal.UserInfo;

import com.vaadin.data.Container;

import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;

public class ExpenseChecking {


    // {section fields}
    // ****************
    private Integer userId;
    private Boolean isExpenseChecking = false;
    // {end fields}

    // {section constructors}
    // **********************
    public ExpenseChecking(Integer userId) {
        this.userId = userId;
        getPermissions();
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    public Boolean isExpenseChecking() {
        return isExpenseChecking;
    }
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private void getPermissions() {
        isExpenseChecking = dbGetExpenseChecking();
    }
    // {end privatemethods}

    // {section database}
    // ******************
    private Boolean dbGetExpenseChecking() {
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select 1 as isQM"
             + "\n " + "from cms_auth_liste_gu"
             + "\n " + "where AL_G_ID = 44 and AL_U_ID = " + userId;
        q.setSqlString(sql);
        Container con = q.query();
        
        return con != null && con.size() > 0;
    }
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
