package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.classes;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;
import archenoah.web.normal.UserInfo.UserData;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

public class ServiceTaskingConfig {


    // {section fields}
    // ****************
    
    private IndexedContainer container;
    
    public enum Permission{
        nurse_required,
        patient_required,
        create_all,
        create_own;
    }
    // {end fields}

    // {section constructors}
    // **********************
    public ServiceTaskingConfig() {
        dbGetConfig();
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    public boolean getPermission(final Permission permission, final Integer projectId) {
        
        if(permission == null) {
            throw new IllegalArgumentException("Permission must be given!");
        }
        if(projectId == null) {
            throw new IllegalArgumentException("ProjectId must be given!");
        }
        
        final String lookup = lookupValue(permission);
        
        container.removeAllContainerFilters();
        container.addContainerFilter(new Filter() {
            
            @Override
            public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {

                boolean key = permission.toString().equals(MyUtils.getValueFromItem(item, "KEY", String.class));
                boolean project = projectId.equals(MyUtils.getValueFromItem(item, "ID_PROJECT", Integer.class));
                boolean value = lookup == null || MyUtils.equalsWithNulls(lookup, MyUtils.getValueFromItem(item, "VALUE", String.class));
                return key && project && value;
            }
            
            @Override
            public boolean appliesToProperty(Object propertyId) {
                return true;
            }
        });
        
        return container.size() > 0;
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private String lookupValue(Permission perm) {
        
        switch (perm) {
        case create_all:
            return getUserIdString();

        case create_own:
            return getUserIdString();

        default:
            return null;
        }
        
    }
    private String getUserIdString() {
        
        if(UserData.get().getUserId() != null) {
            return UserData.get().getUserId().toString(); 
        }else {
            return null;
        }
    }
    // {end privatemethods}

    // {section database}
    // ******************
    private void dbGetConfig() {
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select `KEY`, ID_PROJECT, `VALUE` from cust_protokoll_servicetasking_config;";
        q.setSqlString(sql);
        container = (IndexedContainer) q.query();
    }
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
