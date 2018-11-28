package archenoah.scheduler;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import archenoah.config.CMS_Config_Std;
import archenoah.scheduler.jobs.BlankoJob;
import archenoah.scheduler.jobs.TaskMailerJob;
import archenoah.scheduler.jobs.TimebutlerSyncJob;
import archenoah.scheduler.jobs.WarningListJob;

@WebListener
public class JobScheduler implements ServletContextListener {

    /**
     * 
     */
    private static final long serialVersionUID = 2010613298272782958L;
    private Logger log;
    private SchedulerFactory schedulerFactory;
    private Scheduler scheduler;
    
    enum GROUPS{
        MAILING,
        PROJECTS,
        DATABASE
    };
    
    public JobScheduler() {
        log = LoggerFactory.getLogger(JobScheduler.class);
        
        log.info(padMessage("Initializing"));
        try {
            schedulerFactory = new StdSchedulerFactory(CMS_Config_Std.configHome + "quartz.properties");
            scheduler = schedulerFactory.getScheduler();
            scheduler.clear();
        } catch (SchedulerException e) {
            log.error(padMessage("Error getting Scheduler"));
            e.printStackTrace();
        }
        
        log.info(padMessage("Initialization Complete"));
        
    }

    
    public void run() throws SchedulerException {
        
       
        if(!CMS_Config_Std.getInstanceXmlOnly().SchedulerEnabled) {
            log.info("Scheduler is disabled by config file, ignoring Jobs");
        }else {
          addJob("WarningList", GROUPS.MAILING, WarningListJob.class, CronScheduleBuilder.cronSchedule("0 0 7 ? * MON,WED,FRI *"));
          addJob("BlankoGenerator", GROUPS.PROJECTS, BlankoJob.class, CronScheduleBuilder.cronSchedule("0 0 1 1/1 * ? *"));
          addJob("TimeButler", GROUPS.DATABASE, TimebutlerSyncJob.class, CronScheduleBuilder.cronSchedule("0 5 0 1/1 * ? *"));
          addJob("TaskMailer", GROUPS.MAILING, TaskMailerJob.class, CronScheduleBuilder.cronSchedule("0 0 8 1/1 * ? *"));
        }
        
        //testing
//        addJob("HelloJob", GROUPS.MAILING, HelloJob.class, CronScheduleBuilder.cronSchedule("0/5 * * * * ?"));
        


        scheduler.start();
        log.info(padMessage("Started Scheduler"));


      }
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info(padMessage("Starting Job Scheduler..."));
        JobScheduler js = new JobScheduler();
        try {
            js.run();
        } catch (SchedulerException e) {
            // TASK Auto-generated catch block
            e.printStackTrace();
        }
      }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info(padMessage("                         "));
        log.info(padMessage("Stopping Job Scheduler..."));
        log.info(padMessage("                         "));
        
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            log.error("Error shutting down scheduler {}", e);
        }
    }
    
    private String padMessage(String message){
        return "------- " + message + " -----------------";
    }
    
    private void addJob(String name, GROUPS group, Class<? extends Job> job, CronScheduleBuilder builder){
        
        String grp = group.name();
        String ident = grp + "." + name;
        
        log.info(padMessage("Scheduling Job: " + ident));

        try {
            scheduler.scheduleJob(
                    newJob(job)
                        .withIdentity(name, grp)
                        .build(),
                    newTrigger()
                        .withIdentity("trigger-"+name, grp)
                        .withSchedule(builder)
                        .build()
                    );
        } catch (SchedulerException e) {
            log.error("Error scheduling Job: " + ident, e);
        } 
    }
    
}
