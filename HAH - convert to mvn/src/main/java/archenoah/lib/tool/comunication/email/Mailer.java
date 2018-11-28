package archenoah.lib.tool.comunication.email;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import archenoah.config.CMS_Config_Std;

import com.google.common.base.Joiner;
/**
 * 
 * @author Developer
 * @see
 *<pre>{@code
 * Mailer mailer = new Mailer(CMS_Config_Std.getInstance());
 * mailer.setSender("sender@sender.send");
 * mailer.addRecipient("recipient@recipient.mail");
 * mailer.setSubject("Mail Subject");
 * mailer.setContent("<h1>Mail Content</h1>");
 * mailer.setType(MAIL_TYPE.HTML);
 *
 * mailer.send();
 *}</pre>
 */
public class Mailer {

    // {section autofields}
    // ********************
    // {end autofields}

    // {section fields}
    // ****************
    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass()); //TASK implement logback
    
    public enum MAIL_TYPE {
        PLAIN,
        HTML
    }
    
    public enum MAIL_PRIORITY {
        HIGH(1),
        NORMAL(3),
        LOW(5);
        
        public final String value;
        
        MAIL_PRIORITY(int value){
            this.value = Integer.toString(value);
        };
        
    }
    
    private Session session;

    private Address sender;
    private ArrayList<Address> recipients;
    private ArrayList<Address> cc;
    private ArrayList<Address> bcc;
    private String subject;
    private String content;
    private MAIL_TYPE type = MAIL_TYPE.PLAIN;
    private MAIL_PRIORITY priority = null;
    private CMS_Config_Std config;
    
    // {end fields}

    

    // {section constructors}
    // **********************
    public Mailer(CMS_Config_Std std) {
        
        this.config = std;
        
        resetRecipients();
        resetCC();
        resetBCC();
        
        auth();
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    public Address getSender() {
        return sender;
    }
    
    /**
     * Sets the "from" field
     * @param sender single mail address string
     */
    public void setSender(String sender) {
        try {
            this.sender = new InternetAddress(sender);
        } catch (AddressException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Address> getRecipients() {
        return recipients;
    }
    
    /**
     * Adds one or multiple recipients ("to" field)
     * @param recipient single or comma seperated list of mail addresses
     */
    public void addRecipient(String recipient) {
        try {
            this.recipients.addAll(Arrays.asList(InternetAddress.parse(recipient)));
        } catch (AddressException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Address> getCC() {
        return cc;
    }

    /**
     * Adds one or multiple CC
     * @param cc single or comma seperated list of mail addresses
     */
    public void addCC(String cc) {
        try {
            this.cc.addAll(Arrays.asList(InternetAddress.parse(cc)));
        } catch (AddressException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Address> getBCC() {
        return bcc;
    }

    /**
     * Adds one or multiple BCC
     * @param bcc single or comma seperated list of mail addresses
     */
    public void addBCC(String bcc) {
        try {
            this.bcc.addAll(Arrays.asList(InternetAddress.parse(bcc)));
        } catch (AddressException e) {
            e.printStackTrace();
        }
    }
    
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public MAIL_TYPE getType() {
        return type;
    }

    public void setType(MAIL_TYPE type) {
        this.type = type;
    }
    
    public MAIL_PRIORITY getPriority() {
        return priority;
    }

    public void setPriority(MAIL_PRIORITY priority) {
        this.priority = priority;
    }
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    // {end gettersandsetters}



    // {section publicmethods}
    // ***********************
    /**
     * required:<br/>
     * <ul>
     *   <li>setSender</li>
     *   <li>addRecipient</li>
     *   <li>setSubject</li>
     *   <li>setContent</li>
     *</ul>
     */
    public void send() throws IllegalStateException{
        processAndSend();
    }
    
    public void resetRecipients() {
        recipients = new ArrayList<Address>();
    }
    
    public void resetCC() {
        cc = new ArrayList<Address>();    
    }
    
    public void resetBCC() {
        bcc = new ArrayList<Address>();
    }
    
    public void addAll(MailerGroup group) {
        
        for (Entry<String, String> entry : group.getMails().entrySet()) {
            
            String mail = entry.getKey();
            
            switch (entry.getValue()) {
            case "REC":
                addRecipient(mail);
                break;
            case "CC":
                addCC(mail);
                break;
            case "BCC":
                addBCC(mail);
                break;
            default:
                log.warn("missing or invalid field set for {}", mail);
                break;
            }
            
        }

        
    }
    
    // {end publicmethods}

    // {section privatemethods}
    // ************************



    private void auth() {
        
        session = Session.getInstance(config.mailerConfig, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.mailUser, config.mailPass);
            }
        });
    }
    
    private void processAndSend() throws IllegalStateException {
        
        if(session == null
                || sender == null
                || recipients.size() == 0
                || subject == null
                || content == null
                ) {
            log.error("missing required fields");
            log.warn("session {}", session);
            log.warn("sender {}", sender);
            log.warn("recipients {}", recipients);
            log.warn("subject {}", subject);
            throw new IllegalStateException("missing required fields");
        }
        
        try {

            Message message = new MimeMessage(session);
            
            if(priority != null) {
                message.setHeader("X-Priority", priority.value);
            }
            
            message.setFrom(sender);
            
            if(config.MailerRedirectEnabled) {
                
                
                log.info("Original Recipients: {}", Joiner.on(", ").join(recipients));
                log.info("Original CC: {}", Joiner.on(", ").join(cc));
                log.info("Original BCC: {}", Joiner.on(", ").join(bcc));
                
                HashMap<String, String> redirects = new MailerGroup("_redirect").getMails();
                if(redirects == null || redirects.size() == 0) {
                    log.warn("no '_redirect' group recipients found in 'cms_mailer_groups', aborting");
                    return;
                }else {
                    log.warn("Redirecting Mail to: {}", redirects.keySet());
                }
                
                resetRecipients();
                resetCC();
                resetBCC();
                for (String key : redirects.keySet()) {
                    addRecipient(key);
                }

            }
            
            message.setRecipients(Message.RecipientType.TO, recipients.toArray(new Address[recipients.size()]));
            if(cc.size() > 0) {message.setRecipients(Message.RecipientType.CC, cc.toArray(new Address[cc.size()]));}
            if(bcc.size() > 0) {message.setRecipients(Message.RecipientType.BCC, bcc.toArray(new Address[bcc.size()]));}
            
            message.setSubject(subject);
            
            if(type == MAIL_TYPE.HTML) {
                message.setContent(content, "text/html;charset=utf-8");
            }else {
                message.setText(content);
            }

            Transport.send(message);

        } catch (MessagingException e) {
            log.warn("error sending mail: {}", e.getMessage());
            e.printStackTrace();
        }
    }
    
    // {end privatemethods}

    // {section database}
    // ******************
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
