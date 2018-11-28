package archenoah.lib.vaadin.Components.table.footer.calculations;

import java.util.Collection;
import java.util.Map;

import org.tepi.filtertable.FilterTable;

import archenoah.lib.vaadin.Components.table.footer.FooterCalculable;

public class Count implements FooterCalculable {

	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Count.class);
	
	public final static String TARGET_COLUMN = "targetColumn";
	
	@Override
	public void calculate(FilterTable table, Map<String, String> columnNames, int scaling) {
		// FIXME Auto-generated method stub
		
		Integer size = table.getValue() != null ? ((Collection) table.getValue()).size() : 0;
		
		String result = "Σ " + table.size() + ((size > 0) ? (" (" + size + ")") : "");
		
		table.setColumnFooter(columnNames.get(TARGET_COLUMN), "Anz.: " + result);	

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
