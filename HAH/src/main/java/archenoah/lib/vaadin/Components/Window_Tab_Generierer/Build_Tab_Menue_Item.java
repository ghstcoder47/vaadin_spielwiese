package archenoah.lib.vaadin.Components.Window_Tab_Generierer;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;

public class Build_Tab_Menue_Item {

	public Build_Tab_Menue_Item(TabSheet tab,String Titel,Layout lay , Resource resource) {
		// TODO Automatisch generierter Konstruktorstub
		
	    String Recourcename = "";
	    ThemeResource tr = null;
	    
	    if(resource != null){
	        Recourcename = resource.toString();
	        Recourcename =Recourcename.substring (0, Recourcename.length()-12) +"white-16.png";
	        tr = new ThemeResource(Recourcename);
	    }
		
		
		
			
		final Tab temp = tab.addTab(lay,Titel,tr);
		temp.setClosable(true);
		tab.setSelectedTab(temp);
	}

}
