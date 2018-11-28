package archenoah.lib.vaadin.Components.Window_Tab_Generierer;


import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Resource;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class Build_Window_Button {

	Window temp;
	
	public Build_Window_Button(String Titel,Layout lay,String Heigth,String Width, Resource resource) {
		// TODO Automatisch generierter Konstruktorstub
		
		temp = new Window(Titel,lay);
		temp.setIcon(resource);
		temp.setModal(true);
		temp.setHeight(Heigth);
		temp.setWidth(Width);
		temp.center();
		temp.setDraggable(false);
		temp.setResizable(false);
		UI.getCurrent().getUI().addWindow(temp);
			
	}
	
	
	public Window ReturnWindow()
	{
		
		
		return temp;
	}

}
