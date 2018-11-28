package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.modules;

import java.util.HashMap;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;

import com.vaadin.data.Container;
import com.vaadin.data.Item;

public class SubmoduleManager {
    
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SubmoduleManager.class);
    private final static String modulePath = "archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.form.submodules.";
    private final HashMap<Integer, Class<? extends ModuleComponent>> modules = getSubModules(); 
    
    private static SubmoduleManager instance = null;
    protected SubmoduleManager() {
    }
    public static SubmoduleManager getInstance() {
       if(instance == null) {
          instance = new SubmoduleManager();
       }
       return instance;
    }
    
    public ModuleComponent getModule(Integer projectId) {
        
        if(modules.containsKey(projectId)) {
            try {
                ModuleComponent mod = modules.get(projectId).newInstance();
                mod.setProjectId(projectId);
                return mod;
            } catch (InstantiationException | IllegalAccessException e) {
                log.warn("{}", e.getMessage());
                return null;
            }
        }
        
        return null;
        
    }
    
    @SuppressWarnings("unchecked")
    private static HashMap<Integer, Class<? extends ModuleComponent>> getSubModules() {
        
        HashMap<Integer, Class<? extends ModuleComponent>> modules = new HashMap<Integer, Class<? extends ModuleComponent>>();
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select ID_PROJECT, MODULECLASS"
            + "\n " + "from cust_protokoll_servicetasking_submodules;";
        q.setSqlString(sql);
        
        Container con = q.query();
        
        for (Object iid : con.getItemIds()) {
            Item item = con.getItem(iid);
            try {
                 Class<? extends ModuleComponent> sub = (Class<? extends ModuleComponent>) Class.forName(modulePath 
                    + MyUtils.getValueFromItem(item, "MODULECLASS", String.class));
                modules.put(MyUtils.getValueFromItem(item, "ID_PROJECT", Integer.class), sub);
                 
            } catch (ClassNotFoundException | ClassCastException e ) {
                log.warn("{}", e.getMessage());
            }
            
        }
        
        
        
        return modules;
    }
}
