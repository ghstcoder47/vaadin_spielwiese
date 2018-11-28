package archenoah.lib.custom;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import archenoah.config.CMS_Config_Std;
import archenoah.lib.tool.comunication.email.Mailer;
import archenoah.lib.tool.comunication.email.Mailer.MAIL_TYPE;
import archenoah.lib.tool.comunication.email.MailerGroup;
import archenoah.lib.tool.templating.TemplatingRenderer;
import archenoah.web.normal.UserInfo.UserData;

import com.vaadin.server.ErrorEvent;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

public class GlobalErrorHandler implements ErrorHandler {
    
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GlobalErrorHandler.class);
    
    Mailer mail = new Mailer(CMS_Config_Std.getInstance());
    TemplatingRenderer render = new TemplatingRenderer();
    
    public GlobalErrorHandler() {
        
        mail.setSender(CMS_Config_Std.getInstance().mailUser);
        mail.addAll(new MailerGroup("_err"));
        mail.setType(MAIL_TYPE.HTML);
        
        render.loadTemplate("tpl_error");
        
    }

    @Override
    public void error(ErrorEvent event) {
        
        Throwable err = event.getThrowable();
        showNotification(err);
        err.printStackTrace(System.err);
  
        try {
            
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            err.printStackTrace(pw); // to StringWriter
            
            mail.setSubject("HAH Error: " + err.getClass().getCanonicalName() + " " + err.getMessage());
            
            render.with("error", err.getMessage());
            render.with("time", new Date().toString());
            render.with("user", UserData.get().getUserId());
            render.with("stack", sw.toString());
            
            log.info("sending error mail");
            mail.setContent(render.render());
            try {
                mail.send();
            } catch (IllegalStateException e) {
                log.warn("sending error mail failed: {}", e.getMessage());
            }
            
            
        } catch (Exception e) {
            
            System.err.println("Error Handler Failure");
            e.printStackTrace();
            
        }
        
    }

    private void showNotification(Throwable err){
        
        // Notification with default settings for a warning
        Notification notice = new Notification(
                err.getClass().getCanonicalName(),
                err.getMessage(),
            Notification.Type.ERROR_MESSAGE);
        
        notice.setPosition(Position.MIDDLE_CENTER);
    
                        
        // Show it in the page
        notice.show(Page.getCurrent());
        
    }
    
}
