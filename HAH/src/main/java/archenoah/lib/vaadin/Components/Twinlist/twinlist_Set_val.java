package archenoah.lib.vaadin.Components.Twinlist;

import java.util.HashSet;
import java.util.Set;

import com.vaadin.data.Container;
import com.vaadin.ui.TwinColSelect;

public class twinlist_Set_val {

	public twinlist_Set_val(Container container,TwinColSelect twin,String IDname) {
		// TODO Automatisch generierter Konstruktorstub
		
		Set<Object> selection = new HashSet<Object>();
		if(container.size()>0)
			
			
		{
			for (Object cityItemId : container.getItemIds()) {
				
				
				selection.add((int) container.getItem(cityItemId).getItemProperty(IDname).getValue());
	        }
			
			
		//	twin.setNewItemsAllowed(true);
			
			twin.setImmediate(true);
			twin.setValue(selection);
			
			
		
		}
		
		
		
		
		
		
	}

}
