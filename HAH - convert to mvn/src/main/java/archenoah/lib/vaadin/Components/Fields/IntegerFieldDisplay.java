package archenoah.lib.vaadin.Components.Fields;

import com.vaadin.data.Property;
import com.vaadin.ui.TextField;

public class IntegerFieldDisplay extends TextField {
	
	public IntegerFieldDisplay() {
		// TASK Auto-generated constructor stub
	}

	public IntegerFieldDisplay(String caption) {
		super(caption);
		// TASK Auto-generated constructor stub
	}

	public IntegerFieldDisplay(Property dataSource) {
		super(dataSource);
		// TASK Auto-generated constructor stub
	}

	public IntegerFieldDisplay(String caption, Property dataSource) {
		super(caption, dataSource);
		// TASK Auto-generated constructor stub
	}

	public IntegerFieldDisplay(String caption, String value) {
		super(caption, value);
		// TASK Auto-generated constructor stub
	}

//	public <T> void setValue(T value){
//		
//		switch (value.getClass().getName()) {
//			
//		case "java.lang.Integer":
//			this.setValue((Integer) value);
//			break;
//		
//		case "java.lang.String":
//			super.setValue((String) value);
//			break;
//			
//		default:
//			break;
//		}
//		
//	}
	
	public void setValue(Integer value){

		String presentation = null;
        if(value != null){
            presentation =value.toString();
        }
        
        this.setValue(presentation);
	}

	
}
