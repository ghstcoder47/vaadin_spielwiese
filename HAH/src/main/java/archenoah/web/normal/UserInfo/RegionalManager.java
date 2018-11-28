package archenoah.web.normal.UserInfo;

import java.util.ArrayList;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;

import com.vaadin.data.Container;
import com.vaadin.data.Item;

public class RegionalManager {

 // {section fields}
    // ****************
    private Integer userId;
    private Boolean isRegionalManager = false;
    private ArrayList<Integer> groupIdList;
    private ArrayList<Integer> projectIdList;
    private ArrayList<String> projectCodeList;
    
    // {end fields}

    // {section constructors}
    // **********************
    public RegionalManager(Integer userId) {
        this.userId = userId;
        getPermissions();
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    public Boolean isRegionalManager() {
        return isRegionalManager;
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
        
        isRegionalManager = projects.size() > 0;
        
    }
    // {end privatemethods}

    // {section database}
    // ******************
    private Container dbGetProjects() {
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select"
         + "\n " + "    PSLV_ID as projectId"
         + "\n " + "    , PSLV_GROUP as groupId"
         + "\n " + "    , PSLV_CODE as projectCode"
         + "\n " + "from cust_stammdaten_regionalleiter"
         + "\n " + "inner join cust_projekte_stammdaten_liste on SRL_ID_PROJEKT = PSLV_ID"
         + "\n " + "where SRL_ID_USER = " + userId;
        q.setSqlString(sql);
        return q.query();
    }
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
