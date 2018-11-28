package archenoah.scheduler.jobs;

import java.util.ArrayList;
import java.util.HashMap;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import archenoah.config.CMS_Config_Std;
import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.email.Mailer;
import archenoah.lib.tool.comunication.email.Mailer.MAIL_TYPE;
import archenoah.lib.tool.templating.TemplatingRenderer;

import com.vaadin.data.Container;
import com.vaadin.data.Item;

public class NurseReminderJob implements Job{

    private org.slf4j.Logger log;
    CMS_Config_Std std;
    Mailer mail;
    TemplatingRenderer render;
    
    private HashMap<Integer, HashMap<String, ArrayList<Item>>> nurseData;
    private HashMap<Integer, Item> nurseInfo;
    
    private Integer weeksInAdvance = 3;
    private Integer daysCS = 3;
    
    public NurseReminderJob() {
        std = CMS_Config_Std.getInstance();
        log = org.slf4j.LoggerFactory.getLogger(this.getClass());
        
        render = new TemplatingRenderer();
        render.loadTemplate("tpl_scheduler_nurse_reminder");
        
        mail = new Mailer(CMS_Config_Std.getInstance());
        mail.setSender("kundenservice@hah.de.com");
        mail.setSubject("Hinweis zu Protokollstatus");
        mail.setType(MAIL_TYPE.HTML);
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        
        log.info("Executing NurseReminder");
        
        fillNurseData();
        fillNurseInfo();
        
        for (Integer nurseId : nurseData.keySet()) {
            
            sendMail(nurseId);
            
        }
        
    }
    
    private void fillNurseData(){
        Container con = dbGetWarnings();
        nurseData = new HashMap<Integer, HashMap<String, ArrayList<Item>>>();
        
        for (Object id : con.getItemIds()) {
            
            Item item = con.getItem(id);
            
            Integer nid = (Integer) item.getItemProperty("id_nurse").getValue();
            
            if(nid == null){
                continue;
            }
            
            HashMap<String, ArrayList<Item>> dataList = nurseData.get(nid);
            
            if(dataList == null){
                dataList = new HashMap<String, ArrayList<Item>>();
                dataList.put("unaccepted", new ArrayList<Item>());
                dataList.put("late", new ArrayList<Item>());
                nurseData.put(nid, dataList);
            }
            
            if(MyUtils.getValueFromItem(item, "unaccepted", Integer.class) == 1) {
                dataList.get("unaccepted").add(item);
            }
            
            if(MyUtils.getValueFromItem(item, "late", Integer.class) == 1) {
                dataList.get("late").add(item);
            }
            
            
        }
    }
    
    private void fillNurseInfo(){
        Container nurses = dbGetNurses();
        nurseInfo = new HashMap<Integer,Item>();
        
        for (Object id : nurses.getItemIds()) {
            Item item = nurses.getItem(id);
            
            Integer nid = (Integer) item.getItemProperty("KSK_ID").getValue();
            
            if(nid == null){
                continue;
            }
            
            nurseInfo.put(nid, item);
            
        }
        
    }

    private String getNurseInfo(Integer id, String property){
        String res = "";
        try {
            res += nurseInfo.get(id).getItemProperty(property).getValue();
        } catch (Exception e) {
            
        }
        
        return res;
    }
    
    private ArrayList<HashMap<String, String>> generateListItems(Integer nurseId, String type){
        ArrayList<HashMap<String, String>> res = new ArrayList<HashMap<String, String>>();
        
        DateTimeFormatter df = DateTimeFormat.forStyle("S-");
        
        if(nurseData.get(nurseId).get(type) == null) {
            return res;
        }
        
        ArrayList<Item> list = nurseData.get(nurseId).get(type);
        
        for (Item item : list) {
            
            HashMap<String, String> map = new HashMap<String, String>();
            
            map.put("date", df.print(new LocalDate(MyUtils.getValueFromItem(item, "date", java.sql.Date.class))));
            map.put("project", MyUtils.getValueFromItem(item, "project", String.class));
            map.put("patient", MyUtils.getValueFromItem(item, "patient", String.class));
            Integer pid = MyUtils.getValueFromItem(item, "pid", Integer.class);
            map.put("pid", pid != null ? pid.toString() : "");
            
            res.add(map);
        }
        
        return res;
    }
    
    private void sendMail(Integer nurseId){
        
        String nurseMail =  getNurseInfo(nurseId, "email");
        
        render.with("nurse", getNurseInfo(nurseId, "nurse"));
        render.with("weekCount", weeksInAdvance.toString());
        render.with("unacceptedData", generateListItems(nurseId, "unaccepted"));
        render.with("lateData", generateListItems(nurseId, "late"));
        
        log.info("sending mail for " + nurseMail);
        mail.resetRecipients(); // mail is a field, recipients persist through loops
        mail.addRecipient(nurseMail);
        mail.setContent(render.render());
        
        try {
            mail.send();
        } catch (IllegalStateException e) {
            log.warn("mail could not be sent for {} {}", nurseId, getNurseInfo(nurseId, "nurse"));
        }
        
        
    }

    
    private Container dbGetWarnings(){
        
        DBClass db = new DBClass();
        
        db.CustomQuery.setSqlString("CALL proc_nurse_reminder('" + MyUtils.formatSqlDate(new LocalDate().plusWeeks(weeksInAdvance)) + "', "+daysCS+");");
        db.CustomQuery.query(true);
        db.CustomQuery.setSqlString("SELECT * FROM temp_nurse_reminder ORDER BY project, date, unaccepted DESC");
        return db.CustomQuery.query();
        
    }
    
    private Container dbGetNurses(){
        
        DBClass db = new DBClass();
        
        db.CustomQuery.setSqlString("SELECT"
                         + "\n " + "    KSK_ID"
                         + "\n " + "    , CONCAT(AUL_VORNAME, ' ', AUL_NAME) as nurse"
                         + "\n " + "    , AUL_EMAIL as email"
                         + "\n " + "FROM"
                         + "\n " + "    cust_krankenschwester_stammdaten_ks"
                         + "\n " + "    inner join cms_auth_stammdaten_user on KSK_U_ID = AUL_ID");
        
        return db.CustomQuery.query();
        
    }
    
}
