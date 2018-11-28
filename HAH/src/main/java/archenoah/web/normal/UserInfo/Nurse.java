package archenoah.web.normal.UserInfo;

import java.util.ArrayList;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;

import com.vaadin.data.Container;
import com.vaadin.data.Item;

public class Nurse {

    // {section fields}
    // ****************
    private Integer userId;
    private Integer nurseId;
    private Boolean isNurse = false;
    private ArrayList<Integer> groupIdList;
    private ArrayList<Integer> projectIdList;
    private ArrayList<String> projectCodeList;
    
    // {end fields}

    // {section constructors}
    // **********************
    public Nurse(Integer userId) {
        this.userId = userId;
        getPermissions();
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    public Boolean isNurse() {
        return isNurse;
    }
    
    public Integer getNurseId() {
        return nurseId;
    }
    
    public Boolean forGroupId(Integer groupId) {
        return groupIdList.contains(groupId);
    }
    
    public Boolean forProjectId(Integer projectId) {
        return projectIdList.contains(projectId);
    }
    
    public Boolean forProjectCode(String projectCode) {
        return projectCodeList.contains(projectCode);
    }
    
    public ArrayList<Integer> getGroupIds() {
        return new ArrayList<Integer>(groupIdList);
    }
    
    public ArrayList<Integer> getProjectIds() {
        return new ArrayList<Integer>(projectIdList);
    }
    
    public ArrayList<String> getProjectCodes() {
        return new ArrayList<String>(projectCodeList);
    }
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private void getPermissions() {
        
        nurseId = dbGetNurseId();
        
        groupIdList = new ArrayList<>();
        projectIdList = new ArrayList<>();
        projectCodeList = new ArrayList<>();
        
        Container projects = dbGetProjects();
        
        for (Object iid : projects.getItemIds()) {
            Item item = projects.getItem(iid);
            
            groupIdList.add(MyUtils.getValueFromItem(item, "groupId", Integer.class));
            projectIdList.add(MyUtils.getValueFromItem(item, "projectId", Integer.class));
            projectCodeList.add(MyUtils.getValueFromItem(item, "projectCode", String.class));
            
        }
        
        projectIdList.remove(null);
        projectCodeList.remove(null);
        
        isNurse = groupIdList.contains(12);
        
    }
    // {end privatemethods}

    // {section database}
    // ******************
    private Integer dbGetNurseId() {
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select KSK_ID"
             + "\n " + "from cust_krankenschwester_stammdaten_ks"
             + "\n " + "where KSK_U_Id = " + userId;
        q.setSqlString(sql);
        return MyUtils.getValueFromItem(MyUtils.getFirstItemFromContainer(q.query()), "KSK_ID", Integer.class);
    }
    
    private Container dbGetProjects() {
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select"
             + "\n " + "    AL_G_ID as groupId"
             + "\n " + "    , PSLV_ID as projectId"
             + "\n " + "    , PSLV_CODE as projectCode"
             + "\n " + "from    "
             + "\n " + "    cms_auth_liste_gu"
             + "\n " + "    left join cust_projekte_stammdaten_liste on AL_G_ID = PSLV_GROUP"
             + "\n " + "where ( PSLV_ID IS NOT NULL OR AL_G_ID = 12) and AL_U_ID = " + userId;
        q.setSqlString(sql);
        return q.query();
    }
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
