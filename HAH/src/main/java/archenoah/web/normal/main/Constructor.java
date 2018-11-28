package archenoah.web.normal.main;

import archenoah.global.UserAttributes;
import archenoah.lib.custom.hotkeys.GlobalHotkey;
import archenoah.lib.vaadin.Components.Quickselect.Quickselect;
import archenoah.lib.vaadin.Components.Window_Tab_Generierer.Init_Resize_wächter;
import archenoah.lib.vaadin.Language.i18n.I18nCB;
import archenoah.lib.vaadin.Language.i18n.I18nManager;
import archenoah.lib.vaadin.MenuBerechtigung.MenuPermissions;
import archenoah.web.normal.UserInfo.UserData;
import archenoah.web.normal.main.Menuepunkte.Init_Menüpunkte;
import archenoah.web.normal.main.Menuepunkte.Unsichtbar.Init_Menüpunkt_Entwicklung;
import archenoah.web.normal.main.tray.Init_Tray;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.WebBrowser;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HasComponents.ComponentDetachEvent;
import com.vaadin.ui.HasComponents.ComponentDetachListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class Constructor {

	private TabSheet tab;
	private MenuBar  men;
	private MenuBar  tray;
	private HorizontalLayout bar;
	private  MenuBar.MenuItem HP;
    private I18nManager i18n;
	
	
	private final static class caption extends I18nCB {
	    static final I18nCB logged_in_as = set();
	    static final I18nCB logged_in_last = set();
	    
        static final I18nCB application_name = set();
    }
	
    public Constructor() {
		// TODO Automatisch generierter Konstruktorstub
		VerticalLayout wrapper = new VerticalLayout();
		wrapper.setSizeFull();
		
		i18n = new I18nManager(this);
		
		
		CssLayout layout = new CssLayout();
		layout.setSizeFull();
		layout.setStyleName("company-logo");
		
		Page.getCurrent().setTitle(i18n.get(caption.application_name));
		
		Menu();
		Tray();
		Tab();
		Statusbar();
		
	
		layout.addComponent(men);
		layout.addComponent(tray);
		layout.addComponent(tab);
		wrapper.addComponent(layout);
		wrapper.addComponent(bar);
			
		wrapper.setExpandRatio(layout, 1f);
		
		/** Allgemein zur Verfügung **/
		UI.getCurrent().getSession().getSession().setAttribute("menue", men);
		UI.getCurrent().getSession().getSession().setAttribute("tray", tray);
		UI.getCurrent().getSession().getSession().setAttribute("tab", tab);
		UI.getCurrent().getSession().getSession().setAttribute("lay", layout);
		
		/*****************************/
		
		Init_Menüpunkte inm = new Init_Menüpunkte();
		
		UI.getCurrent().setContent(wrapper);
		
//		Menu_Berechtigung2 mb = new Menu_Berechtigung2();
		new MenuPermissions();
		
		Init_Tray it = new Init_Tray();
		
		Notification notif = new Notification(
		    i18n.get(caption.application_name),
		    "<div><p>Healthcare at Home</p><div class='isco'><span>powered by</span></div></div>",
		    Type.HUMANIZED_MESSAGE, true);
		notif.setDelayMsec(1000);
		notif.setPosition(Position.MIDDLE_CENTER);
		notif.setStyleName("splash");

		notif.show(Page.getCurrent());
		
		
		UI.getCurrent().addAction(new GlobalHotkey(KeyCode.SPACEBAR, ModifierKey.CTRL) {
            
            @Override
            public void handleAction(Object sender, Object target) {
                
                Quickselect.show();
                
            }
        });
		
		Init_Resize_wächter ir = new Init_Resize_wächter();
		Ent();
		
	
	}
	
	//****** Initialisieren der Componenten *******/
		private void Menu()
		{
			men = new MenuBar();
			men.setImmediate(false);
			//men.setWidth("30px");
			men.setStyleName("superbar-button");
			//men.setWidth("-1px");
			//men.setHeight("-1px");
		
			 HP = men.addItem("Start",null, null);
			
			 UI.getCurrent().getSession().getSession().setAttribute("menue_item", HP);

			
		}
		private void Tray()
		{
			tray = new MenuBar();
			tray.setImmediate(false);
			tray.setStyleName("superbar-tray");

			
			
		}		
		private void Tab()
		{
			tab = new TabSheet();
			tab.setImmediate(false);
			
			tab.setSizeFull();
			
			tab.setStyleName("superbar-taskbar isconet-logo");
			
			tab.addSelectedTabChangeListener(new SelectedTabChangeListener() {
                
                @Override
                public void selectedTabChange(SelectedTabChangeEvent event) {
                    
                    Page.getCurrent().setTitle(i18n.get(caption.application_name) +" - " + tab.getTab(tab.getSelectedTab()).getCaption());
                }
            });
			
			tab.addComponentDetachListener(new ComponentDetachListener() {
                
                @Override
                public void componentDetachedFromContainer(ComponentDetachEvent event) {

                    // for some reason detach listener fires before the component ist removed
                    if(tab.getComponentCount() <= 1) {
                        Page.getCurrent().setTitle(i18n.get(caption.application_name));
                    }
                    
                    men.focus();
                    
                }
            });
		}
		
		private void Ent()
		{
		    
		    UserAttributes ua = new UserAttributes(UserData.get().getUserId());
		    if(ua.getUserAttributeMap() != null && ua.getUserAttributeMap().containsKey("developer_options")) {
		        new Init_Menüpunkt_Entwicklung();
		    }
		    
		}
		private void Statusbar()
		{
			bar = new HorizontalLayout();
			bar.setStyleName("statusbar");
			bar.setHeight("24px");
		
			bar.addComponent(new Label(i18n.get(caption.logged_in_as) + ": " + UI.getCurrent().getSession().getSession().getAttribute("Username")) );
			bar.addComponent(new Label(i18n.get(caption.logged_in_last) + ": " + UI.getCurrent().getSession().getSession().getAttribute("LastLogin")) );
			
//			Label BrowserInformationen = new Label("Browser Informationen");
//			BrowserInformationen.setDescription("<h3>Verbindung ist: </h3>"+Verschlüsslung()+"<h3>Ihre IP Addresse: </h3>"+IP()+"<h3>Ihr Browser: </h3>" +Browser()+"<h3>Ihr Betriebssystem: </h3>" +OS()+"<h3>Ihre Bildschirmauflösung: </h3>" +Auflösung());
//			bar.addComponent(BrowserInformationen);
			
			Label app = new Label("Health Care at Home Data entry Application");
			Label by = new Label("powered by <a href='http://isconet.de' target='_blank'>isconet.de</a>", ContentMode.HTML);
			
			app.setStyleName("statusbar-left");
			by.setStyleName("statusbar-left");
			
			bar.addComponent(app);
			bar.addComponent(by);
			
			
			
			
		}
		
		
		private String Verschlüsslung()
		{
			if(UI.getCurrent().getSession().getBrowser().isSecureConnection() == true)
			{
				MenuItem Benutzer_Online = tray.addItem("",new ThemeResource("image-res/icons-white-16/Lock.png"), null);
				Benutzer_Online.setDescription("Verbindung ist Verschlüsselt");
				
				return "Verschlüsselt";
			
				
			}else
			{
				MenuItem Benutzer_Online = tray.addItem("",new ThemeResource("image-res/icons-white-16/Lock-Open.png"), null);
				Benutzer_Online.setDescription("Verbindung ist Unverschlüsselt");
				return " Nicht Verschlüsselt";
				
				
			}
		}
		
		private String IP()
		{
			String IP_Address = UI.getCurrent().getSession().getBrowser().getAddress();
			if(IP_Address.equals("0:0:0:0:0:0:0:1")==true)
			{
				IP_Address = "127.0.0.1";
			}
			
			return IP_Address;
		}
		
		private String Browser()
		{
			String Browser = null;
			WebBrowser wb = UI.getCurrent().getSession().getBrowser();
			
			if(wb.isFirefox() == true)
			{
				Browser = "Mozilla Firefox " +wb.getBrowserMajorVersion();
			}
			if(wb.isChrome() == true)
			{
				Browser = "Google Chrome "+wb.getBrowserMajorVersion();
			}
			if(wb.isIE() == true)
			{
				Browser = "Microsoft Internet Explorer "+wb.getBrowserMajorVersion();
			}
			if(wb.isSafari() == true)
			{
				Browser = "Apple Safari "+wb.getBrowserMajorVersion();
			}
			if(wb.isOpera() == true)
			{
				Browser = "Opera "+wb.getBrowserMajorVersion();
			}
			return Browser;
		}
		private String OS()
		{
			String OS = null;
			WebBrowser wb = UI.getCurrent().getSession().getBrowser();
			
			if(wb.isWindows() == true)
			{
				OS = "Microsoft Windows"  ;
			}
			
			if(wb.isMacOSX() == true)
			{
				OS = "Apple Macintosh";
			}
				
			if(wb.isLinux() == true)
			{
				OS = "Linux";
			}
			
			
			
			return OS;
		}
		
		private String Auflösung()
		{
			String Auflösung = null;
			WebBrowser wb = UI.getCurrent().getSession().getBrowser();
			
			Auflösung = "Höhe: " + wb.getScreenHeight() +"<br>" + "Breite: " + wb.getScreenWidth();
			
			
			
			return Auflösung;
		}
		
		

}
