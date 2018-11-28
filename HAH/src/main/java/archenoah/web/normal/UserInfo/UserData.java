package archenoah.web.normal.UserInfo;

import java.util.ArrayList;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;

import com.vaadin.data.Container;

public class UserData {

    private Integer userId;
    private Country country;
    private Nurse nurse;
    private RegionalManager regionalManager;
    private ProjectManager projectManager;
    private Pharmacy pharmacy;
    private QM qm;
    private CS cs;
    private HR hr;
    private Finance finance;
    private ExpenseChecking expenseChecking;
    
    private String userName;
    private ArrayList<Integer> groupIds = new ArrayList<Integer>(); 
        
    // {section fields}
    // ****************
    // {end fields}

    // {section constructors}
    // **********************
    public UserData(Integer userId) {
        this.userId = userId;
        init();
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    public Integer getUserId() {
        return userId;
    }
    
    public String getUserName() {
        return this.userName;
    }
        
    public Country getCountry() {
        return country;
    }
    
    public Nurse getNurse() {
        return nurse;
    }

    public RegionalManager getRegionalManager() {
        return regionalManager;
    }
    
    public ProjectManager getProjectManager() {
        return projectManager;
    }
    
    public Pharmacy getPharmacy() {
        return pharmacy;
    }
    
    public QM getQM() {
        return qm;
    }
    
    public CS getCS() {
        return cs;
    }
    
    public HR getHR() {
        return hr;
    }
    
    public Finance getFinance() {
        return finance;
    }
    
    public ExpenseChecking getExpenseChecking() {
    	return expenseChecking;
    }
    
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    public static UserData get() {
        return MyUtils.getUserData();
    }
    
    public boolean hasGroup(Integer groupId) {
        return groupIds.contains(groupId);
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private void init() {
        country = new Country(userId);
        nurse = new Nurse(userId);
        regionalManager = new RegionalManager(userId);
        projectManager = new ProjectManager(userId);
        pharmacy = new Pharmacy(userId);
        qm = new QM(userId);
        cs = new CS(userId);
        hr = new HR(userId);
        finance = new Finance(userId);
        expenseChecking = new ExpenseChecking(userId);
        
        dbGetUserName();
        dbGetGroups();
    }
    // {end privatemethods}

    // {section database}
    // ******************
    private void dbGetUserName() {
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select CONCAT(AUL_VORNAME,' ', AUL_NAME) as userName from cms_auth_stammdaten_user where AUL_ID = " + this.userId;
        q.setSqlString(sql);
        
        this.userName = MyUtils.getValueFromItem(MyUtils.getFirstItemFromContainer(q.query()),
            "userName", String.class);
    }
    
    private void dbGetGroups() {
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select distinct AL_G_ID from cms_auth_liste_gu where AL_U_ID = " + this.userId;
        q.setSqlString(sql);
        
        Container con = q.query();
        
        if(con != null && con.size() > 0) {
            for (Object iid : con.getItemIds()) {
                groupIds.add(MyUtils.getValueFromItem(con.getItem(iid), "AL_G_ID", Integer.class));
            }
        }
        
    }
    
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
