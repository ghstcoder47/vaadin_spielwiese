package archenoah.lib.vaadin.Components.Twinlist;

import com.vaadin.data.Container;
import com.vaadin.ui.TwinColSelect;

public class twinlist_fill {

	public twinlist_fill(Container container,TwinColSelect twin,String IDname,String Anzeigename) {
		// TODO Automatisch generierter Konstruktorstub
		
		
		if(container.size()>0)
			
			
		{
			for (Object cityItemId : container.getItemIds()) {
				if(container.getItem(cityItemId).getItemProperty(Anzeigename).getValue() != null)
	            {
				
		            int GrpId = (Integer) container.getItem(cityItemId).getItemProperty(IDname)
		                    .getValue();
		        	
		            String gruppe = container.getItem(cityItemId).getItemProperty(Anzeigename)
		                    .getValue().toString();
		          
		           
		            twin.addItem(GrpId);
		          
		            twin.setItemCaption(GrpId, gruppe);
	            }
	        }
		
		}
		
		
		
		
		
		
	}

}
