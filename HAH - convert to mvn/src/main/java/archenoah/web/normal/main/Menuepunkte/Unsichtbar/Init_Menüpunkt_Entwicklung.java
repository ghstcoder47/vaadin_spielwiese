package archenoah.web.normal.main.Menuepunkte.Unsichtbar;


import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionException;

import archenoah.config.CMS_Config_Std;
import archenoah.lib.tool.comunication.dbclass.ConnectionPoolSingleton;
import archenoah.lib.tool.comunication.dbclass.GeoPoolSingleton;
import archenoah.lib.tool.comunication.dbclass.RefreshableConnectionPool;
import archenoah.lib.tool.comunication.email.Mailer;
import archenoah.lib.tool.comunication.email.Mailer.MAIL_TYPE;
import archenoah.lib.tool.comunication.email.MailerGroup;
import archenoah.lib.tool.templating.TemplatingRenderer;
import archenoah.lib.vaadin.Language.i18n.I18nCB;
import archenoah.lib.vaadin.Language.i18n.I18nMB;
import archenoah.lib.vaadin.Language.i18n.I18nManager;
import archenoah.lib.vaadin.resources.Icons;
import archenoah.scheduler.jobs.BlankoJob;
import archenoah.web.normal.main.Menuepunkte.Unsichtbar.UserAttributes.initUserAttributes;
import archenoah.web.normal.main.Menuepunkte.Unsichtbar.i18n.initI18n;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;

import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBoxListener;

public class Init_Menüpunkt_Entwicklung {
	
	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Init_Menüpunkt_Entwicklung.class);
	
	private MenuBar.MenuItem Menü;

	private  MenuBar.MenuItem Entwicklerbereich;
	private  MenuBar.MenuItem Voralge_System;
	private  MenuBar.MenuItem Sprachtools;
	private  MenuBar.MenuItem FileManager;

    private I18nManager i18n;
	
	private static class msg extends I18nMB{
	    static final I18nMB confirm = set(Icon.WARN, ButtonId.YES, ButtonId.ABORT);
	    static final I18nMB info = set(Icon.INFO, ButtonId.CLOSE);
	}
	
	private final static class caption extends I18nCB {
	    static final I18nCB i18n = set();
	    static final I18nCB run_blanko_scheduler = set();
	    static final I18nCB connectionPool = set();
        static final I18nCB userAttributes = set();
        static final I18nCB testmail = set();
    }
	
	public Init_Menüpunkt_Entwicklung() {
		// TODO Automatisch generierter Konstruktorstub
	    i18n = new I18nManager(this);
		
		Menü = (MenuItem) UI.getCurrent().getSession().getSession().getAttribute("menue_item");
		
		 Gen_men();
		
	}
	
	
	private void Gen_men()
	{
		Entwicklerbereich = Menü.addItemBefore("Entwicklertools",Icons.Black.dev_16, null,Lastitem(Menü));
		
		Entwicklerbereich.addItem(i18n.get(caption.i18n), Icons.Black.info_16, new Command() {

            @Override
            public void menuSelected(MenuItem selectedItem) {
                
                new initI18n(selectedItem);
                
            }
		    
		});
		
        Entwicklerbereich.addItem(i18n.get(caption.connectionPool), Icons.Black.info_16, new Command() {
            
            private Label poolInfo = new Label();
            
            @Override
            public void menuSelected(MenuItem selectedItem) {
                
                RefreshableConnectionPool pool = ConnectionPoolSingleton.INSTANCE.getPool();
                RefreshableConnectionPool geo = GeoPoolSingleton.INSTANCE.getPool();
                
                
                poolInfo.setContentMode(ContentMode.HTML);
                StringBuilder str = new StringBuilder();
                
                str.append("<b>pool</b><br />");
                if(pool.getAvailableConnections() != null) {
                    str.append("availableConnections: ");
                    str.append(pool.getAvailableConnections().size());
                    str.append(", reservedConnections: ");
                    str.append(pool.getReservedConnections().size());
                }
                
                str.append("<br /><b>geo</b><br />");
                if(geo.getAvailableConnections() != null) {
                    str.append("availableConnections: ");
                    str.append(geo.getAvailableConnections().size());
                    str.append(", reservedConnections: ");
                    str.append(geo.getReservedConnections().size());
                }
                
                poolInfo.setValue(str.toString());
                
                i18n.messageBox(msg.info, new MessageBoxListener() {

                    @Override
                    public void buttonClicked(ButtonId arg0) {
                    }
                }, poolInfo);

            }

        });
		
		Entwicklerbereich.addItem(i18n.get(caption.run_blanko_scheduler), Icons.Black.ip_16, new Command() {
            
            @Override
            public void menuSelected(MenuItem selectedItem) {
                
                i18n.messageBox(msg.confirm, new MessageBoxListener() {
                    
                    @Override
                    public void buttonClicked(ButtonId arg0) {
                        
                        if(arg0 != ButtonId.YES) {
                            return;
                        }
                        
                        try {
                            new BlankoJob().execute(null);
                        } catch (JobExecutionException e) {
                            e.printStackTrace();
                        }
                        
                    }
                });
                

                
            }
        });
		

		Entwicklerbereich.addItem(i18n.get(caption.userAttributes), Icons.Black.ip_16, new Command() {
            
            @Override
            public void menuSelected(MenuItem selectedItem) {
                new initUserAttributes(selectedItem);
            }
        });

		Entwicklerbereich.addItem(i18n.get(caption.testmail), Icons.Black.mail_16, new Command() {
            
            @Override
            public void menuSelected(MenuItem selectedItem) {
                
                TemplatingRenderer render = new TemplatingRenderer();
                render.loadTemplate("tpl_error");
                render.with("stack", new Date().toString());
                
                Mailer mail = new Mailer(CMS_Config_Std.getInstance());
                
                MailerGroup sender = new MailerGroup("_test_sender");
                
                mail.setSender(sender.getMails().keySet().iterator().next());
                mail.setSubject("Devtools Test Mail");
                mail.setType(MAIL_TYPE.HTML);
                mail.addAll(new MailerGroup("_test_recipient"));
                mail.setContent(render.render());
                
                mail.send();
                
                log.info("mail sent from {} to {}, {}, {}", mail.getSender(), mail.getRecipients(), mail.getCC(), mail.getBCC());
            }
        });
		
	}
	
	private MenuBar.MenuItem Lastitem(MenuBar.MenuItem item)
	{
		List it  = item.getChildren();
		
		int Count = it.size();
		
		
		
		
		
		
		return (MenuBar.MenuItem)it.get(Count-1);
		
	}
	
	
	public Command close = new Command()
	{
		@Override
		public void menuSelected(MenuItem selectedItem) {
			// TODO Auto-generated method stub
			//INIT_Benutzerverwaltung U_Verw = new INIT_Benutzerverwaltung(Hauptfenster,tab);
			
			Menü.removeChild(Entwicklerbereich);
		}
		
	};
	
	
	

}
