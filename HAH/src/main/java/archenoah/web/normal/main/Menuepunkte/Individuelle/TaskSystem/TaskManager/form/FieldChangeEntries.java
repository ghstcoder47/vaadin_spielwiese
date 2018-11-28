package archenoah.web.normal.main.Menuepunkte.Individuelle.TaskSystem.TaskManager.form;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.vaadin.CustomConverterFactorys.StringSqlTimestampConverter;
import archenoah.lib.vaadin.Language.i18n.I18nConverter;
import archenoah.lib.vaadin.Language.i18n.I18nManager;

import com.vaadin.data.Container;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

public class FieldChangeEntries extends Label {
	
    org.slf4j.Logger log;
    
	private HashMap<String, Object> oldMap;
	private HashMap<String, Object> newMap;
	
	private List<String> hiddenFields = Arrays.asList("TMT_ID", "TMT_CREATED", "TMT_ENTRY_TYPE", "TMT_ID_REFERENCE", "TMT_HASH", "TMT_TITLE", "TMT_ID_USER_CREATOR", "TMT_COMMENT");
	private I18nManager lngColumns;
	private ValueContainerCollection collection;
	private StringSqlTimestampConverter date;
	
	public FieldChangeEntries(I18nManager LngColumns, ValueContainerCollection collection, HashMap<String, Object> oldMap, HashMap<String, Object> newMap){
		
	    log = org.slf4j.LoggerFactory.getLogger(this.getClass());
	    
		this.oldMap = oldMap;
		this.newMap = newMap;
		this.lngColumns = LngColumns;
		this.collection = collection;
		
		date = new StringSqlTimestampConverter();
		date.setDateFormat(DateFormat.SHORT);
		
		this.setContentMode(ContentMode.HTML);
		this.setValue(generateHtml());
	}
	
	private String generateHtml(){
		ArrayList<FieldChange> data = getFieldChanges();
		
		StringBuilder sb = new StringBuilder();
		
		for (FieldChange fieldChange : data) {
			sb.append(fieldChange.getString());
		}
		
		return sb.toString();
		
	}
	
	private ArrayList<FieldChange> getFieldChanges(){
		
		ArrayList<FieldChange> data = new ArrayList<FieldChangeEntries.FieldChange>();
		
		for (Entry<String, Object> oldEntry : oldMap.entrySet()) {
			
			if(hiddenFields.contains(oldEntry.getKey())){
				continue;
			}
			
			if(MyUtils.equalsWithNulls(oldEntry.getValue(), newMap.get(oldEntry.getKey()))){
				continue;
			}
			
			String oldValue = convertObjects(collection.getValueFromContainer(oldEntry.getKey(), oldEntry.getValue()));
			String newValue = convertObjects(collection.getValueFromContainer(oldEntry.getKey(), newMap.get(oldEntry.getKey())));
			
            String caption = "";
            
            try {
                caption = lngColumns.get(oldEntry.getKey());
            } catch (Exception e) {
                log.warn("'{}' should be listed in TaskManagerDataView 'tableColsAll' field!", oldEntry.getKey()); 
            }
            
			data.add(new FieldChange(caption, oldValue.toString(), newValue.toString()));
		}
		
		return data;
		
	}
	
	private String convertObjects(Object object){
		
		if(object == null){
			return lngColumns.get("field_empty");
		}
		
		String string = object.toString();
		
		if(object instanceof java.sql.Date || object instanceof java.util.Date){
			string = date.convertToPresentation(object, String.class, new Locale("DE"));
		}
		
		return string;
	}
	
	static class ValueContainerCollection{
		
		private HashMap<String, Container> containerMap;
		private HashMap<String, String[]> columnMap;
		private HashMap<String, I18nConverter> lngMap;
		
		
		public ValueContainerCollection() {
			containerMap = new HashMap<String, Container>();
			columnMap = new HashMap<String, String[]>();
			lngMap = new HashMap<String, I18nConverter>();
		}
		
		/**
		 * 
		 * @param column: Column name of the data table ("TMT_ID" etc)
		 * @param con: Container with the ComboBox values
		 * @param idCol: Id column name of the provided Container
		 * @param valueCol: Value column name of the provided Container
		 */
		public void addContainer(String column, Container con, String idCol, String valueCol){
			containerMap.put(column, con);
			columnMap.put(column, new String[]{idCol, valueCol});
		}
		
		public void addContainer(String column, Container con, String idCol, String valueCol, I18nConverter lngConverter){
			addContainer(column, con, idCol, valueCol);
			lngMap.put(column, lngConverter);
		}
		
		public Object getValueFromContainer(String column, Object value){
			
			Object res = value;
			
			if(column != null && containerMap.containsKey(column)){
				
				Container con = containerMap.get(column);
				
				if(con.getContainerPropertyIds().contains(columnMap.get(column)[0])){
					
					
					for (Object cid : con.getItemIds()) {
						
						Object containerIdValue = con.getItem(cid).getItemProperty(columnMap.get(column)[0]).getValue();
						
						if(containerIdValue.equals(value)){
							
							res = con.getContainerProperty(cid, columnMap.get(column)[1]).getValue();
							
							if(lngMap.containsKey(column)){
								res = lngMap.get(column).getRawFromIdConverter((String) res);
							}
							
							break;
							
						}
						
					}
					
				}
				
			}
			
			return res;
			
		}
		
	}
	
	private class FieldChange{
		
		private String field;
		private String oldValue;
		private String newValue;
		
		public FieldChange(String field, String oldValue, String newValue) {
			super();
			this.field = field;
			this.oldValue = oldValue;
			this.newValue = newValue;
		}
		
		public String getString(){
			
			StringBuilder sb = new StringBuilder();
			
			sb.append("<li>");

			sb.append("<span class=\"field\">");
			sb.append(field);
			sb.append("</span>");
			
			sb.append(" <span class=\"oldValue\">");
			sb.append(oldValue);
			sb.append("</span>");

			sb.append(" <span class=\"arrow\">");
			sb.append(" => ");
			sb.append("</span>");
			
			sb.append(" <span class=\"newValue\">");
			sb.append(newValue);
			sb.append("</span>");
			
			sb.append("</li>");
			
			return sb.toString();
						
		}
	}
	
}
