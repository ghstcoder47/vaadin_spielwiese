package archenoah.lib.vaadin.Components.Combo;

import com.vaadin.addon.sqlcontainer.SQLContainer;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.NativeSelect;


public class combo_Einzeln {

	private SQLContainer ds;
	private ComboBox combo;
	
	public combo_Einzeln (SQLContainer container,ComboBox combo,String Anzeigename)
	{
		ds = container;
		this.combo = combo;
		
		if(container.size()>0)
			
		
		{
			for (Object cityItemId : ds.getItemIds()) {
	            
	            if(ds.getItem(cityItemId).getItemProperty(Anzeigename).getValue() != null)
	            {
	            combo.addItem( ds.getItem(cityItemId).getItemProperty(Anzeigename).getValue().toString());
	            }
	        }
		
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
