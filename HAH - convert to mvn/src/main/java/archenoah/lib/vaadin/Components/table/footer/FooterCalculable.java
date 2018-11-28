package archenoah.lib.vaadin.Components.table.footer;

import java.util.Map;

import org.tepi.filtertable.FilterTable;

public interface FooterCalculable {

	public void calculate(FilterTable table, Map<String, String> columnNames, int scaling);
	
}
