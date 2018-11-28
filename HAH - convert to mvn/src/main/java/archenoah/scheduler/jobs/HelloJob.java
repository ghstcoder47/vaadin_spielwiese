package archenoah.scheduler.jobs;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class HelloJob implements Job{

    public HelloJob() {
        // TASK Auto-generated constructor stub
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        
        System.out.println("Hello World! - " + new Date());
        
    }

}
