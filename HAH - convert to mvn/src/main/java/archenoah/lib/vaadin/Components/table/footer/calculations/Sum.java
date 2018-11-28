package archenoah.lib.vaadin.Components.table.footer.calculations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import org.tepi.filtertable.FilterTable;

import com.vaadin.data.Item;

import archenoah.lib.vaadin.Components.table.footer.FooterCalculable;

public class Sum implements FooterCalculable {

	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Sum.class);
	
	public final static String TARGET_COLUMN = "targetColumn";
	
	@Override
	public void calculate(FilterTable table, Map<String, String> columnNames, int scaling) {
		// FIXME Auto-generated method stub
		
		BigDecimal summe = BigDecimal.ZERO;
		
		summe = columnSummate(table, columnNames.get(TARGET_COLUMN));
    	
		table.setColumnFooter(columnNames.get(TARGET_COLUMN), "Σ: " + summe.setScale(scaling, RoundingMode.HALF_UP));
			
	}
	
	public static BigDecimal columnSummate(FilterTable table, String columnName) {
		
		Iterator itemIterator = table.getItemIds().iterator();
		
		BigDecimal summe = BigDecimal.ZERO;
	
		while(itemIterator.hasNext()){
    		
    		int iid = (Integer) itemIterator.next();
    		
    		Item item = table.getItem(iid);
    		
    		BigDecimal tmpValue;
    		Object obj = item.getItemProperty(columnName).getValue();
		    
    		if(Objects.isNull(obj)) {
		    	continue;
		    }
		    
    		if (obj instanceof Long) {
    			tmpValue = new BigDecimal((Long)obj);
    		} else if(obj instanceof Double) {
    			tmpValue = new BigDecimal((Double)obj);
    		} else if(obj instanceof Integer){
    			tmpValue = new BigDecimal((Integer)obj);
    		} else {
    			tmpValue = (BigDecimal) item.getItemProperty(columnName).getValue();
    		}
    				    		
    		summe = summe.add(tmpValue);  	

    	}
		
		return summe;
		
	}

}
