package archenoah.web.normal.main.tray.sm.traywächter;

import archenoah.UIS.UI_Functions;
import archenoah.web.normal.main.tray.sm.benutzeronline.tray_benutzeronline.Onlinestatuslistener;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;

public class traywächter {

	private MenuBar  men;
	private MenuBar  tray;
	private UI_Functions me;
	public traywächter() {
		// TODO Automatisch generierter Konstruktorstub
		
		men  = (MenuBar) UI.getCurrent().getSession().getSession().getAttribute("menue");
		tray = (MenuBar) UI.getCurrent().getSession().getSession().getAttribute("tray");
		me =  new UI_Functions();
		
		Refreshstarter();
	}
	
	
	private void Refreshstarter()
	{
		 final Refresher refresher = new Refresher();

		  refresher.setRefreshInterval(10000);

		  refresher.addListener(new Wächterlistener());
		
		
	}
	
	public class Wächterlistener implements RefreshListener {

		@Override
		public void refresh(Refresher source) {
			// TODO Automatisch generierter Methodenstub
			  
		
		}
	}
	

}
