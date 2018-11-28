package archenoah.lib.vaadin.Components.Window_Tab_Generierer;

import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class Init_Resize_wächter {

	
	
	
	
	public Init_Resize_wächter()
	{
		UI.getCurrent().getUI().getPage().addBrowserWindowResizeListener(new BrowserWindowResizeListener() {
			
			@Override
			public void browserWindowResized(BrowserWindowResizeEvent event) {
				// TODO Automatisch generierter Methodenstub
				
				
				for (Window ItemIda : UI.getCurrent().getWindows()) {
				    
					
					ItemIda.center();
					UI.getCurrent().getUI().requestRepaintAll();
					
				}
				
				
			}
		});
		
	
		
	}
	
	
	
}
