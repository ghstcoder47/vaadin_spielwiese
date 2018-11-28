package archenoah.scheduler.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.debug.ExecutionTimeLogger;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektzuweisung.Form.classes.BlankoGenerator;

import com.vaadin.data.Container;

public class BlankoJob implements Job{
    
    private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    
    
    public BlankoJob() {
        // TASK Auto-generated constructor stub
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        
        BlankoGenerator gen = new BlankoGenerator();
        
        Container con = dbGetItems();
        
        if(con == null) {
            return;
        }
        
        log.info("iterating Blankos for {} items", con.size());
        ExecutionTimeLogger ex = new ExecutionTimeLogger("blankos");
        ex.start();
        
        
        for (Object iid : con.getItemIds()) {
            
            try {
                gen.setVkItem(con.getItem(iid));
                gen.fillBlankos();
                
            } catch (Exception e) {
                log.error("error creating blanko for: {}", MyUtils.formatItem(con.getItem(iid)));
                e.printStackTrace();
            }
            
            
        }
        
        log.info("finished iterating");
        ex.stop();
        
    }

    
    private Container dbGetItems() {
        
        DBClass db = new DBClass();
        
        String sql = "select cust_verk_haupt.*, PSLV_CODE"
             + "\n " + "from cust_verk_haupt"
             + "\n " + "left join cust_projekte_blankos on VH_PROJEKT_ID = PB_P_ID"
             + "\n " + "left join cust_projekte_stammdaten_liste on VH_PROJEKT_ID = PSLV_ID"
             + "\n " + "where VH_AKTIV = 1 and PB_RRULE_TYPE IS NOT NULL"
             + "\n " + "group by VH_ID";
        
        db.CustomQuery.setSqlString(sql);
        
        try {
            return db.CustomQuery.query();
        } catch (Exception e) {
            log.error("error getting Blanko Items");
            e.printStackTrace();
            return null;
        }
        
        
        
    }
    

}
