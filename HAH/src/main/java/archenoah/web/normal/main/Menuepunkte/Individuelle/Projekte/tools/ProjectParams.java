package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.tools;

import java.util.HashMap;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;

import com.vaadin.data.Container;
import com.vaadin.data.Item;

public class ProjectParams {


    // {section fields}
    // ****************
    HashMap<Integer, HashMap<Params, String>> parameters;
    HashMap<Params, HashMap<Integer, String>> projects;
    
    public static enum Params{
        multi_nurse
    }
    // {end fields}

    // {section constructors}
    // **********************
    public ProjectParams() {
        setupParameters();
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    public HashMap<Params, String> getParamsFor(Integer projectId){
        return parameters.get(projectId);
    }
    
    public String getParamFor(Integer projectId, Params param){
        if(getParamsFor(projectId) == null) {
            return null;
        }
        return getParamsFor(projectId).get(param);
    }
    
    public boolean hasParam(Integer projectId, Params param) {
        if(getParamsFor(projectId) == null) {
            return false;
        }
        return getParamsFor(projectId).containsKey(param);
    }
    
    public HashMap<Integer, String> getProjectsFor(Params param){
        return projects.get(param);
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private void setupParameters() {
        parameters = new HashMap<Integer, HashMap<Params,String>>();
        projects = new HashMap<Params, HashMap<Integer,String>>();
        
        Container con = dbGetParams();
        if(con == null || con.size() == 0) {
            return;
        }
        
        for (Object iid : con.getItemIds()) {
            Item item = con.getItem(iid);
            
            Params param = Params.valueOf(MyUtils.getValueFromItem(item, "PP_PARAM", String.class));
            if(param == null) {
                continue;
            }
            
            Integer project = MyUtils.getValueFromItem(item, "PP_ID_PROJEKT", Integer.class);
            String value = MyUtils.getValueFromItem(item, "PP_VALUE", String.class);
            
            if(parameters.get(project) == null) {
                parameters.put(project, new HashMap<ProjectParams.Params, String>());
            }
            parameters.get(project).put(param, value);
            
            if(projects.get(param) == null) {
                projects.put(param, new HashMap<Integer, String>());
            }
            projects.get(param).put(project, value);
            
        }
        
    }
    // {end privatemethods}

    // {section database}
    // ******************
    private Container dbGetParams() {
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select * from cust_projekte_parameter";
        q.setSqlString(sql);
        return q.query();
    }
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
