package archenoah.web.normal.main.Menuepunkte.Individuelle.TaskSystem.TaskManager.classes;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.joda.time.LocalDate;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import archenoah.config.CMS_Config_Std;
import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;
import archenoah.lib.tool.comunication.email.Mailer;
import archenoah.lib.tool.comunication.email.Mailer.MAIL_PRIORITY;
import archenoah.lib.tool.comunication.email.Mailer.MAIL_TYPE;
import archenoah.lib.tool.comunication.email.MailerGroup;
import archenoah.lib.tool.templating.TemplatingRenderer;
import archenoah.lib.vaadin.Language.i18n.I18nConverter;

import com.google.common.base.Joiner;
import com.vaadin.data.Container;
import com.vaadin.data.Item;

public class TaskManagerMailHandler {

    // {section fields}
    // ****************
    
    public enum MTYPE {

        NEW(
            ROLE.RESPONSIBLE,
            ROLE.ASSIGNEE),
        REMINDER( // scheduler
            ROLE.ASSIGNEE
            ),
        WARNING( // scheduler
            ROLE.ASSIGNEE,
            ROLE.RESPONSIBLE
            ),
        CRITICAL( // scheduler
            ROLE.ASSIGNEE,
            ROLE.RESPONSIBLE,
            ROLE.CREATOR
            ),
        CLOSED(
            ROLE.ASSIGNEE,
            ROLE.RESPONSIBLE,
            ROLE.CREATOR
            ),

        CHANGE_STATUS(
            ROLE.ASSIGNEE,
            ROLE.RESPONSIBLE
            ),
        CHANGE_ASSIGNEE(
            ROLE.ASSIGNEE,
            ROLE.RESPONSIBLE
            );

        ROLE[] roles;

        private MTYPE(ROLE... roles) {
            this.roles = roles;
        }
        
        public ROLE[] getRoles() {
            return roles;
        }
    }
    
    public enum ROLE {
        CREATOR,
        ASSIGNEE,
        RESPONSIBLE,
        CONCERNS
    }
    
    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    
    private Integer taskId;
    
    private Item task;
    private HashMap<ROLE, Item> users;
    private HashMap<Item, HashSet<MTYPE>> notificationsMap;

    private TemplatingRenderer render;
    // {end fields}

    private Mailer mail;

    private I18nConverter statusConverter;

    private static Whitelist whitelist = Whitelist.basic();
    
    // {section constructors}
    // **********************
    public TaskManagerMailHandler() {
        
        render = new TemplatingRenderer();
        render.loadTemplate("tpl_scheduler_nurse_reminder");
        
        mail = new Mailer(CMS_Config_Std.getInstance());
        
        String sender = new MailerGroup("_noreply").getMails().keySet().iterator().next();
        
        if(sender == null) {
            log.error("_noreply mailer group not defined!");
        }
        
        mail.setSender(sender);
        mail.setType(MAIL_TYPE.HTML);
        
        statusConverter =  new I18nConverter("cust_taskmanager_master_status", "TMMST_ID", "TMMST_TOKEN");
    }
    
    public TaskManagerMailHandler(Integer taskId) {
        this();
        setTaskId(taskId);
    }
    
    public TaskManagerMailHandler(Item task) {
        this();
        setTask(task);
    }
    
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    /**
     * @return the taskId
     */
    public Integer getTaskId() {
        return taskId;
    }

    /**
     * @param taskId the taskId to set
     */
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
        getTask();
        setTitle();
        reset();
    }
    
    public void setTask(Item task) {
        this.task = task;
        this.taskId = MyUtils.getValueFromItem(task, "TMT_ID", Integer.class);
        setTitle();
        reset();
    }
    
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    
    public void reset() {
        notificationsMap = null;
        users = null;
    }
    
    public void addNotification(MTYPE type) {
        
        
        log.info("adding notification {}", type);
        if(type == null) {
            return;
        }
        
        if(notificationsMap == null) {
            log.info("notificationsMap is empty");
            notificationsMap = new HashMap<>();
        }
        
        if(users == null) {
            log.info("users is empty");
            getUsers();
        }
        
        for (ROLE role : type.getRoles()) {
            
            Item ui = users.get(role);
            
            if(ui == null) {
                continue;
            }
            
            HashSet<MTYPE> map = notificationsMap.get(ui);
            if(map == null) {
                map = new HashSet<MTYPE>();
                notificationsMap.put(users.get(role), map);
            }
            map.add(type);
        }

    }
    
    public void sendMails() {
        
        if(notificationsMap == null || notificationsMap.size() == 0) {
            log.info("nothing to send");
            return;
        }
        
        for ( Entry<Item, HashSet<MTYPE>> entry : notificationsMap.entrySet()) {
            sendMail(entry);
        }
    }
    
   
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    
    private void setTitle() {
        mail.setSubject("Informationen zu Aufgabe #" + taskId);
    }
    
    private void sendMail(Entry<Item, HashSet<MTYPE>> entry) {
        
        
        render.loadTemplate("tpl_task_mailer");
//        render.loadTemplate("test_tpl");
        
        render.with("type", entry.getValue());
        
        render.with("creator", formatUser(ROLE.CREATOR));
        render.with("assignee", formatUser(ROLE.ASSIGNEE));
        render.with("responsible", formatUser(ROLE.RESPONSIBLE));
        
        render.with("id", taskId);
        render.with("title", MyUtils.getValueFromItem(task, "TMT_TITLE", String.class));
        render.with("due", new LocalDate(MyUtils.getValueFromItem(task, "TMT_DUE", java.sql.Date.class).getTime()).toString("dd.MM.YYYY"));
        render.with("status", statusConverter.getIdCaption(MyUtils.getValueFromItem(task, "TMT_ID_STATUS", Integer.class)));
        render.with("description", Jsoup.clean(MyUtils.getValueFromItem(task, "TMT_DESCRIPTION", String.class), whitelist));
        
        
//        log.info("{}", render.render());
        
        log.info("sending mail for " + entry.getKey());
        mail.resetRecipients(); // mail is a field, recipients persist through loops
        mail.addRecipient(MyUtils.getValueFromItem(entry.getKey(), "AUL_EMAIL", String.class));
        mail.setContent(render.render());
        
        if(entry.getValue().contains(MTYPE.CRITICAL)) {
            mail.setPriority(MAIL_PRIORITY.HIGH); 
        }
        
        try {
            mail.send();
        } catch (IllegalStateException e) {
            log.warn("mail could not be sent for {}", entry.getKey());
        }
        
    }
    
    private String formatUser(ROLE role) {
        Item item = users.get(role);
        return MyUtils.getValueFromItem(item, "AUL_VORNAME", String.class)
            + " "
            + MyUtils.getValueFromItem(item, "AUL_NAME", String.class);
    }
    
    // {end privatemethods}

    // {section database}
    // ******************
    private void getTask() {

        DBClass db = new DBClass();
        CustomQuery q = db.CustomQuery;
        q.setSqlString("select * from cust_taskmanager_tasks where TMT_ID = " + taskId);
//        db.debugNextQuery(true);
        task = MyUtils.getFirstItemFromContainer(q.query());
    }
    
    private void getUsers() {
        
        if(users == null) {
            users = new HashMap<>();
        }
        
        HashMap<ROLE, Integer> uids = new HashMap<>();
        uids.put(ROLE.CREATOR, MyUtils.getValueFromItem(task, "TMT_ID_USER_CREATOR", Integer.class));
        uids.put(ROLE.CONCERNS, MyUtils.getValueFromItem(task, "TMT_ID_USER_CONCERNS", Integer.class));
        uids.put(ROLE.RESPONSIBLE, MyUtils.getValueFromItem(task, "TMT_ID_USER_RESPONSIBLE", Integer.class));
        uids.put(ROLE.ASSIGNEE, MyUtils.getValueFromItem(task, "TMT_ID_USER_ASSIGNEE", Integer.class));
        
        Collection<Integer> in = uids.values();
        in.removeAll(Collections.singleton(null)); // Joiner does not like null values
        
        DBClass db = new DBClass();
        CustomQuery q = db.CustomQuery;
        q.setSqlString("select"
             + "\n " + "    AUL_ID"
             + "\n " + "    , AUL_USERNAME"
             + "\n " + "    , AUL_NAME"
             + "\n " + "    , AUL_VORNAME"
             + "\n " + "    , AUL_EMAIL"
             + "\n " + "from cms_auth_stammdaten_user where AUL_ID IN ("+ Joiner.on(", ").join(in) +")");
//        db.debugNextQuery(true);
        Container con = q.query();

        users.put(ROLE.CREATOR, MyUtils.getItemFromContainer(con, "AUL_ID", uids.get(ROLE.CREATOR)));
        users.put(ROLE.CONCERNS, MyUtils.getItemFromContainer(con, "AUL_ID", uids.get(ROLE.CONCERNS)));
        users.put(ROLE.RESPONSIBLE, MyUtils.getItemFromContainer(con, "AUL_ID", uids.get(ROLE.RESPONSIBLE)));
        users.put(ROLE.ASSIGNEE, MyUtils.getItemFromContainer(con, "AUL_ID", uids.get(ROLE.ASSIGNEE)));

    }
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
