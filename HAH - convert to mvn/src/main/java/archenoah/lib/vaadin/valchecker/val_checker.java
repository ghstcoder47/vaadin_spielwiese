package archenoah.lib.vaadin.valchecker;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.vaadin.data.Validator;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class val_checker {

	public Map<Object, String> vala;
	private Set<Object> keys;
	private Boolean pr = true;
	
	public val_checker(Map<Object, String> val) {
		// TODO Automatisch generierter Konstruktorstub
		vala = val;
		
	}
	
	
	public boolean Is_Valid()
	{
		keys = vala.keySet();
		
		 
		
		 for (Object singleKey : keys) {
		
			 AbstractField AF = (AbstractField) singleKey;
			
			 if (AF.isValid() == false)
			 {
				 pr = false;
			 }
			
		 }
		
		return pr;
	}
	
	
	
	
	


}
