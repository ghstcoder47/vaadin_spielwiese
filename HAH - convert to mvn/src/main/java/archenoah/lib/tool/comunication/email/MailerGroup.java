package archenoah.lib.tool.comunication.email;

import java.util.HashMap;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;

import com.vaadin.data.Container;

public class MailerGroup {

    // {section fields}
    // ****************
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MailerGroup.class);
    private HashMap<String, String> mails; 
    // {end fields}

    // {section constructors}
    // **********************
    public MailerGroup(String groupKey) {
        setGroupKey(groupKey);
    }
    public HashMap<String, String> getMails() {
        return mails;
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    public void setGroupKey(String groupKey) {
        fillMails(dbGetGroup(groupKey));
    }
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private void fillMails(Container con) {
        mails = new HashMap<String, String>();
        for (Object iid : con.getItemIds()) {
            String mail = MyUtils.getValueFromItem(con.getItem(iid), "MAIL_ENTRY", String.class);
            String field = MyUtils.getValueFromItem(con.getItem(iid), "ENTRY_FIELD", String.class);
            mails.put(mail, field);
        }
    }
    // {end privatemethods}

    // {section database}
    // ******************
    private Container dbGetGroup(String groupKey) {
        
        if(groupKey == null) {
            log.error("group key is missing!");
            return null;
        }
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select MAIL_ENTRY, ENTRY_FIELD from cms_mailer_groups where GROUP_KEY = '" + groupKey + "'";
        q.setSqlString(sql);
        
        return q.query();
    }
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
