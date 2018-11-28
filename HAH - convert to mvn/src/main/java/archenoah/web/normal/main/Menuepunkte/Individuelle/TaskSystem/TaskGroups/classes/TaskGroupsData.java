package archenoah.web.normal.main.Menuepunkte.Individuelle.TaskSystem.TaskGroups.classes;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

public class TaskGroupsData {

    // {section autofields}
    // ********************
    // {end autofields}

    // {section fields}
    // ****************
    // {end fields}

    // {section constructors}
    // **********************
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    /**
     * 
     * @return index = group id, TMG_ID, TMG_NAME, TMG_ID_USER_LEAD, TMG_MAIL, (TMG_TYPE)
     */
    public static IndexedContainer getGroups() {
        return MyUtils.convertIndex(dbGetGroups(), "TMG_ID");
    }
    
    /**
     * 
     * @param groupId
     * @return TMG_ID, TMG_NAME, TMG_ID_USER_LEAD, TMG_MAIL, (TMG_TYPE)
     */
    public static Item getGroup(Integer groupId) {
        return dbGetGroup(groupId);
    }
    /**
     * 
     * @param groupId
     * @return TMGM_ID_GROUP, TMGM_ID_USER, member
     */
    public static IndexedContainer getGroupMembers(Integer groupId) {
        return dbGetGroupMembers(groupId);
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    
    // {end privatemethods}

    // {section database}
    // ******************
    private static IndexedContainer dbGetGroups() {
        
        DBClass db = new DBClass();
        
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_All();
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cust_taskmanager_groups");
        return (IndexedContainer) db.DB_Data_Get.DB_SEND_AND_GET_Container();
        
    }
    
    private static Item dbGetGroup(Integer groupId) {
        
        DBClass db = new DBClass();
        
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_All();
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cust_taskmanager_groups");
        db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cust_taskmanager_groups", "TMG_ID", "=", groupId.toString(), "");
        return db.DB_Data_Get.DB_SEND_AND_GET_FIRST_ITEM();
        
    }
    
    private static IndexedContainer dbGetGroupMembers(Integer groupId) {
        
        DBClass db = new DBClass();
        
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("*");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("CONCAT(AUL_VORNAME, ' ', AUL_NAME)", "member");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_taskmanager_group_members", "LEFT JOIN", "cms_auth_stammdaten_user", "TMGM_ID_USER", "AUL_ID");
        db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cust_taskmanager_group_members", "TMGM_ID_GROUP", "=", groupId.toString(), "");
        return (IndexedContainer) db.DB_Data_Get.DB_SEND_AND_GET_Container();
        
    }
    
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
