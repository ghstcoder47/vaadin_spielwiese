package archenoah.lib.vaadin.Components.Combo;

import com.vaadin.addon.sqlcontainer.SQLContainer;
import com.vaadin.ui.NativeSelect;

public class nativecombo_Einzeln {

	private SQLContainer ds;
	private NativeSelect combo;
	public nativecombo_Einzeln(SQLContainer container,NativeSelect nselect,String Anzeigename)
	{
		ds = container;
		combo = nselect;
		if(container.size()>0)
			
			
		{
			
		
			for (Object cityItemId : ds.getItemIds()) {
	            
				if(ds.getItem(cityItemId).getItemProperty(Anzeigename).getValue() != null)
	            {
	            nselect.addItem(ds.getItem(cityItemId).getItemProperty(Anzeigename).getValue().toString());
	            }
	        }
		
		}
	}
	
	
	
}
