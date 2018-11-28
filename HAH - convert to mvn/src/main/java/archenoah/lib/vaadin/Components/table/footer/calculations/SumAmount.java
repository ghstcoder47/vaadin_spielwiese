package archenoah.lib.vaadin.Components.table.footer.calculations;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

import org.tepi.filtertable.FilterTable;

import com.vaadin.data.Item;

import archenoah.lib.vaadin.Components.table.footer.FooterCalculable;

public class SumAmount implements FooterCalculable {

	public final static String TARGET_COLUMN = "targetColumn";
	
	@Override
	public void calculate(FilterTable table, Map<String, String> columnNames, int scaling) {
		// FIXME Auto-generated method stub

    	BigDecimal items = new BigDecimal(0);
    	
    	for(Iterator i = table.getItemIds().iterator(); i.hasNext();){
    		
    		int iid = (Integer) i.next();
    		
    		Item item = table.getItem(iid);
    		
    		Object tmpObj = item.getItemProperty(columnNames.get(TARGET_COLUMN)).getValue();
    		
    		items = items.add(tmpObj != null ? (BigDecimal) tmpObj : new BigDecimal(0));
    	}
    	
    	table.setColumnFooter(columnNames.get(TARGET_COLUMN), "Σ " + items + " €");
		
	}
	// {section fields}
	// ****************
	// {end fields}

	// {section constructors}
	// **********************

	// {end constructors}

	// {section gettersandsetters}
	// ***************************
	// {end gettersandsetters}

	// {section publicmethods}
	// ***********************
	// {end publicmethods}

	// {section privatemethods}
	// ************************
	// {end privatemethods}

	// {section database}
	// ******************
	// {end database}

	// {section layout}
	// ****************
	// {end layout}
}
