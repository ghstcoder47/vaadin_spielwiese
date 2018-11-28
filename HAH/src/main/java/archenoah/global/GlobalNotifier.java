package archenoah.global;

import java.util.ArrayList;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class GlobalNotifier {

    // {section fields}
    // ****************
    ArrayList<String> notificationsList;
    // {end fields}

    // {section constructors}
    // **********************
    public GlobalNotifier() {
        // TASK Auto-generated constructor stub
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    
    public void checkNotifications() {
        refreshNotifications();
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private void showNotifier(Item item) {

        String title = MyUtils.getValueFromItem(item, "TITLE", String.class);
        String text = MyUtils.getValueFromItem(item, "TEXT", String.class);
        String params = MyUtils.getValueFromItem(item, "PARAM", String.class);
        try {
            text = String.format(text, (Object[]) params.split(","));
        } catch (Exception e) {
        }
        
        
        Notification notif = new Notification(title, "<br />" + text, Type.ERROR_MESSAGE);

        notif.setHtmlContentAllowed(true);

        notif.setDelayMsec(-1);
        notif.setPosition(Position.TOP_CENTER);

        notif.show(Page.getCurrent());
    }
    
    private void refreshNotifications() {
        
        Container con = dbGetNotifications();
        
        if(con == null || con.size() == 0) {
            return;
        }
        
        ArrayList<String> tempList = new ArrayList<String>();
        for (Object iid : con.getItemIds()) {
            tempList.add(MyUtils.getValueFromItem(con.getItem(iid), "hash", String.class));
        }
        
        if(notificationsList != null && notificationsList.containsAll(tempList)) {
            // nothing changed
            return;
        }
        
        notificationsList = new ArrayList<String>();
        for (Object iid : con.getItemIds()) {
            
            showNotifier(con.getItem(iid));
            notificationsList.addAll(tempList);
            
        }
    }
    
    // {end privatemethods}

    // {section database}
    // ******************
    private Container dbGetNotifications() {
        
        DBClass db = new DBClass();
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("*");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("MD5(CONCAT(TEXT, TIME))", "hash");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cms_global_notifiers");
        db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_global_notifiers", "ACTIVE", "=", "1", "AND");
        db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cms_global_notifiers", "TIME", "<", "NOW()", "");
        
        return db.DB_Data_Get.DB_SEND_AND_GET();
        
    }
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
