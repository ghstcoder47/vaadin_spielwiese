package archenoah.scheduler.jobs;

import java.util.HashMap;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import archenoah.config.CMS_Config_Std;
import archenoah.global.warningmodule.WarningList;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.email.Mailer;
import archenoah.lib.tool.comunication.email.Mailer.MAIL_TYPE;
import archenoah.lib.tool.templating.TemplatingRenderer;

import com.vaadin.data.Container;
import com.vaadin.data.Item;

public class WarningListJob implements Job{

    private org.slf4j.Logger log;
    Mailer mail;
    TemplatingRenderer render;
    
    private HashMap<Integer, Item> nurseInfo;
    private WarningList warnings;
    
    
    public WarningListJob() {
        log = org.slf4j.LoggerFactory.getLogger(this.getClass());
        
        render = new TemplatingRenderer();
        render.loadTemplate("tpl_scheduler_warning_list");
        
        mail = new Mailer(CMS_Config_Std.getInstance());
        mail.setSender("kundenservice@hah.de.com");
        mail.setSubject("Hinweise zum Serviceportal");
        mail.setType(MAIL_TYPE.HTML);
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        
        log.info("Executing WarningListJob");
        
        fillNurseInfo();
        warnings = new WarningList(); 
        
        for (Integer userId : nurseInfo.keySet()) {
            
            sendMail(userId);
            
        }
        
    }
    
    
    
    private void fillNurseInfo(){
        Container nurses = dbGetNurses();
        nurseInfo = new HashMap<Integer,Item>();
        
        for (Object id : nurses.getItemIds()) {
            Item item = nurses.getItem(id);
            
            Integer uid = (Integer) item.getItemProperty("AUL_ID").getValue();
            
            if(uid == null){
                continue;
            }
            
            nurseInfo.put(uid, item);
            
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
    
    
    private void sendMail(Integer userId){
        
        warnings.setUserId(userId);
        if(warnings.getCount() == 0) {
            return;
        }
        
        
        String nurseMail =  getNurseInfo(userId, "email");
        
        render.with("nurse", getNurseInfo(userId, "nurse"));
        render.with("warnings", warnings.getFormattedWarnings());
        
        log.info("sending mail for " + nurseMail);
        mail.resetRecipients(); // mail is a field, recipients persist through loops
        mail.addRecipient(nurseMail);
        mail.setContent(render.render());
        
        try {
            mail.send();
        } catch (IllegalStateException e) {
            log.warn("mail could not be sent for {} {}", userId, getNurseInfo(userId, "nurse"));
        }
        
        
    }

    
    private Container dbGetNurses(){
        
        DBClass db = new DBClass();
        
        db.CustomQuery.setSqlString("SELECT"
                         + "\n " + "    AUL_ID"
                         + "\n " + "    , CONCAT(AUL_VORNAME, ' ', AUL_NAME) as nurse"
                         + "\n " + "    , AUL_EMAIL as email"
                         + "\n " + "FROM"
                         + "\n " + "    cust_krankenschwester_stammdaten_ks"
                         + "\n " + "    inner join cms_auth_stammdaten_user on KSK_U_ID = AUL_ID");
        
        return db.CustomQuery.query();
        
    }
    
}
