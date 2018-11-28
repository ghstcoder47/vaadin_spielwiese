package archenoah.web.normal.UserInfo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.google.common.base.Joiner;
import com.vaadin.data.Container;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;

public class Country {

    // {section fields}
    // ****************
    Integer userId;
    private HashSet<String> permissions;
    
    // {end fields}

    // {section constructors}
    // **********************
    public Country(Integer userId) {
        this.userId = userId;
        getPermissions();
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    
    public Boolean hasPermissions() {
        return permissions.size() > 0;
    }
    
    public Boolean hasPermission(String c) {
        return permissions.contains(c);
    }
    
    public String getFilterString() {
        
        if(permissions.size() == 0) {
            return "";
        }
        
        return "'" + Joiner.on("','").join(permissions) + "'";
        
    }
    
    public String getFilterCondition(String fieldName) {
        
        if(permissions.size() == 0) {
            return "";
        }
        
        return fieldName + " IN (" + getFilterString() + ") ";
    }
    
    public HashSet<String> getCountries(){
        return permissions;
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private void getPermissions() {
        permissions = new HashSet<String>();
        
        String groupsString = MyUtils.getFirstValueFromContainer(dbGetGroups(), "groups", String.class);
        
        if(groupsString.equals("")) { // String.split("") returns an array of size 1!
            return;
        }
        
        List<String> groups = Arrays.asList(groupsString.trim().split("\\s*,\\s*", -1));
        
        permissions.addAll(groups);
        
    }
    // {end privatemethods}

    // {section database}
    // ******************
    private Container dbGetGroups() {
        DBClass db = new DBClass();

        String sql = "SELECT AUL_ID, CONCAT_WS(',', KSK_COUNTRYCODE, GROUP_CONCAT(nat.UA_VALUE)) as groups FROM"
             + "\n " + "cms_auth_stammdaten_user"
             + "\n " + ""
             + "\n " + "LEFT JOIN cust_krankenschwester_stammdaten_ks on AUL_ID = KSK_U_ID"
             + "\n " + "LEFT JOIN cms_user_attributes as nat on AUL_ID = nat.UA_ID_USER and nat.UA_KEY = 'ordermanager_country'"
             + "\n " + "WHERE AUL_ID = " + userId;
        
//        db.debugNextQuery(true);
        db.CustomQuery.setSqlString(sql);
        return db.CustomQuery.query();
    }
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
