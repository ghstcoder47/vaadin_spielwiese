package archenoah.UIS;

import archenoah.config.CMS_Config_Std;

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;

public class UIS_Pruefung extends UIProvider {

	@Override
	public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
		// TODO Automatisch generierter Methodenstub
		CMS_Config_Std conf = CMS_Config_Std.getInstance();	
		
		

		if(conf.Test_No_Database == true)
		{
			return UI_Metro_1.class;
		
		}
		
		int SID = conf.Style_ID;
		
		
		switch(SID)
		{
		
		case 1:
			return UI_Metro_1.class;
		case 2:
			return UI_Metro_2.class;
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
		
		default:
			
			return UI_Metro_1.class;
		
		}
	
	
		
		
		
	
		

		
	}

}
