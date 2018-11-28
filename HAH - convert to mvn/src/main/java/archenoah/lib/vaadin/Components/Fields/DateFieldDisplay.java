package archenoah.lib.vaadin.Components.Fields;

import java.text.DateFormat;
import java.util.Date;

import com.vaadin.data.Property;
import com.vaadin.ui.TextField;

public class DateFieldDisplay extends TextField {
	
	private int dateFormat = DateFormat.SHORT;
	private int timeFormat = 0;
	
	public DateFieldDisplay() {
		// TASK Auto-generated constructor stub
	}

	public DateFieldDisplay(String caption) {
		super(caption);
		// TASK Auto-generated constructor stub
	}

	public DateFieldDisplay(Property dataSource) {
		super(dataSource);
		// TASK Auto-generated constructor stub
	}

	public DateFieldDisplay(String caption, Property dataSource) {
		super(caption, dataSource);
		// TASK Auto-generated constructor stub
	}

	public DateFieldDisplay(String caption, String value) {
		super(caption, value);
		// TASK Auto-generated constructor stub
	}

	/**
	 * 
	 * @param format
	 * 		   Dateformat.SHORT by default
	 */
	public void setDateFormat(int format){
		this.dateFormat = format;
	}
	
	/**
	 * 
	 * @param format
	 * 		  disabled (0) by default
	 */
	public void setTimeFormat(int format){
		this.timeFormat = format;
	}
	
//	public <T> void setValue(T value){
//		
//	    String type = "";
//	    if(value != null) {
//	        type = value.getClass().getName();
//	    }
//		switch (type) {
//		
//		case "java.sql.Timestamp":
//			this.setValue((java.sql.Timestamp) value);
//			break;
//
//		case "java.sql.Date":
//			this.setValue((java.sql.Date) value);
//			break;
//			
//		case "java.util.Date":
//			this.setValue((java.util.Date) value);
//			break;
//		
//		case "java.lang.String":
//			super.setValue((String) value);
//			break;
//			
//		default:
//		    super.setValue(null);
//			break;
//		}
//		
//	}
	
	public void setValue(java.sql.Timestamp timestamp){
	    if(timestamp != null) {
	        this.setValue(new Date(timestamp.getTime()));
	    }else {
	        this.setValue((Date) null);
	    }
	}
	
	public void setValue(java.sql.Date date){
	    if(date != null) {
	        this.setValue(new Date(date.getTime()));
	    }else {
            this.setValue((Date) null);
        }
	}
	
	public void setValue(Date date){
		String presentation = null;
        if(date != null){
            
            if(this.timeFormat != 0){
                presentation = DateFormat.getDateTimeInstance(dateFormat, timeFormat).format(date);
            }else{
                presentation = DateFormat.getDateInstance(dateFormat).format(date);
            }
        }
        this.setValue(presentation);
	}

}
