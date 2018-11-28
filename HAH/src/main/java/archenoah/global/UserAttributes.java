package archenoah.global;

import java.util.ArrayList;
import java.util.HashMap;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;

import com.google.common.base.Joiner;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.UI;

public class UserAttributes {

    
    // {section fields}
    // ****************
    private Integer userId = null;
    private ArrayList<String> attributes = new ArrayList<String>();
    // <userId, <attribute, value>>
    private HashMap<Integer, HashMap<String, ArrayList<String>>> attributeMap; 
    // {end fields}

    // {section constructors}
    // **********************
    public UserAttributes() {
        
    }
    public UserAttributes(Integer userId) {
        setUserId(userId);
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public void setCurrentUserId() {
        setUserId((int) UI.getCurrent().getSession().getSession().getAttribute("Userid"));
    }
    
    public ArrayList<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<String> attributes) {
        this.attributes = attributes;
    }
    
    public void addAttribute(String attribute) {
        this.attributes.add(attribute);
    }
    
    public void removeAttribute(String attribute) {
        this.attributes.remove(attribute);
    }
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    
    /**
     * returns all (specified) attributes for all (specified) users
     * @return HashMap&lt;userId, HashMap&lt;attribute, ArrayList&lt;values&gt;&gt;&gt;
     */
    public HashMap<Integer, HashMap<String, ArrayList<String>>> getAttributeMap(){
        generateMap();
        return attributeMap;
    }
    
    /**
     * returns all (specified) attributes for the selected user, or null if no user selected
     * @return HashMap&lt;attribute, ArrayList&lt;values&gt;&gt;
     */
    public HashMap<String, ArrayList<String>> getUserAttributeMap(){
        generateMap();
        if(userId == null || attributeMap == null) {
            return null;
        }
        return attributeMap.get(userId);
    }
    
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    
    private void generateMap() {
        Container con = dbGetAttributes();
        
        if(con == null || con.size() == 0) {
            attributeMap = null;
            return;
        }
        
        attributeMap = new HashMap<Integer, HashMap<String,ArrayList<String>>>();
        
        for (Object iid : con.getItemIds()) {
            Item item = con.getItem(iid);
            
            Integer uid = MyUtils.getValueFromItem(item, "UA_ID_USER", Integer.class);
            String attr = MyUtils.getValueFromItem(item, "UA_KEY", String.class);
            String val = MyUtils.getValueFromItem(item, "UA_VALUE", String.class);
            
            if (attributeMap.get(uid) == null) {
                attributeMap.put(uid, new HashMap<String, ArrayList<String>>());
            }
            
            if(attributeMap.get(uid).get(attr) == null) {
                attributeMap.get(uid).put(attr, new ArrayList<String>());
            }
            
            attributeMap.get(uid).get(attr).add(val);
            
        }
    }
    
    // {end privatemethods}

    // {section database}
    // ******************
    private Container dbGetAttributes() {
        int UID = (int) UI.getCurrent().getSession().getSession().getAttribute("Userid");
        
        DBClass db = new DBClass();
        
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_All();
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_user_attributes");
        
        if(attributes != null && attributes.size() > 0) {
            db.DB_Data_Get.DB_Filter.DB_WHERE_In("cms_user_attributes", "UA_KEY", "'"+Joiner.on("','").join(attributes)+"'", (userId != null ? "AND" : ""));
        }
        
        if(userId != null) {
            db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_user_attributes", "UA_ID_USER", "=", "'" + userId + "'", "");
        }

        
//        db.debugNextQuery(true);
        
        return db.DB_Data_Get.DB_SEND_AND_GET();
    }
    // {end database}



    // {section layout}
    // ****************
    // {end layout}
}
