package archenoah.lib.vaadin.Components.table;



import com.vaadin.data.Container;
import com.vaadin.ui.ComboBox;

public class Enum_filler {
	
	private Container ds;
	private Class C_enum;
	
	public Enum_filler(Container container,Object Arg_C_enum,String Anzeigename)
	{
		
		ds = container;
		this.C_enum = (Class) Arg_C_enum;
		Enum_Generator eg = new Enum_Generator();
		
		if(container.size()>0)
			
		
		{
			for (Object cityItemId : ds.getItemIds()) {
	            
	            if(ds.getItem(cityItemId).getItemProperty(Anzeigename).getValue() != null)
	            {
	            	if(eg.is_enumvorhanden(C_enum, container.getItem(cityItemId).getItemProperty(Anzeigename).getValue()+"")== false)
	            	{
	            		 eg.addEnum(C_enum, container.getItem(cityItemId).getItemProperty(Anzeigename).getValue()+"", C_enum.getDeclaredFields());
	            	}
	           
	           // combo.addItem( ds.getItem(cityItemId).getItemProperty(Anzeigename).getValue().toString());
	            }
	        }
		
		}
		
		
	}
	
	
	

}
