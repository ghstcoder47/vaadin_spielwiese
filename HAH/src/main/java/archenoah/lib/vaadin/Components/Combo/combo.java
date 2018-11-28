package archenoah.lib.vaadin.Components.Combo;

import com.vaadin.addon.sqlcontainer.SQLContainer;
import com.vaadin.data.Container;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.NativeSelect;


public class combo {

	private Container ds;
	private ComboBox combo;
	
	public combo (Container container,ComboBox combo,String IDname,String Anzeigename)
	{
		ds = container;
		this.combo = combo;
		if(container.size()>0)
			
			
		{
			for (Object cityItemId : ds.getItemIds()) {
				if(ds.getItem(cityItemId).getItemProperty(Anzeigename).getValue() != null)
	            {
		            int GrpId = (Integer) ds.getItem(cityItemId).getItemProperty(IDname)
		                    .getValue();
		            String gruppe = ds.getItem(cityItemId).getItemProperty(Anzeigename)
		                    .getValue().toString();
		            combo.addItem(GrpId);
		            combo.setItemCaption(GrpId, gruppe);
	            }
	        }
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
