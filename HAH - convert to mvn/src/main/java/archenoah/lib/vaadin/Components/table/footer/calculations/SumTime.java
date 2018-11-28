package archenoah.lib.vaadin.Components.table.footer.calculations;

import java.sql.Time;
import java.util.Iterator;
import java.util.Map;

import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.tepi.filtertable.FilterTable;

import com.vaadin.data.Item;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.vaadin.Components.table.footer.FooterCalculable;

public class SumTime implements FooterCalculable {
	
	public final static String TARGET_COLUMN = "targetColumn";

	@Override
	public void calculate(FilterTable table, Map<String, String> columnNames, int scaling) {
		// FIXME Auto-generated method stub

    	PeriodFormatter parser = new PeriodFormatterBuilder().appendHours().appendLiteral(":").appendMinutes().appendLiteral(":").appendSeconds().toFormatter();
    	Period period = Period.ZERO;
    	
    	for(Iterator i = table.getItemIds().iterator(); i.hasNext();){
    		
    		int iid = (Integer) i.next();
    		
    		Item item = table.getItem(iid);
    		
    		period = period.plus(MyUtils.getValueFromItem(item, columnNames.get(TARGET_COLUMN), Time.class) != null ? parser.parsePeriod(((Time)item.getItemProperty(columnNames.get(TARGET_COLUMN)).getValue()).toString()) : parser.parsePeriod("00:00:00"));

    	}
    	
    	PeriodFormatter printer = new PeriodFormatterBuilder().printZeroAlways().minimumPrintedDigits(2).appendHours().appendLiteral(":").appendMinutes().toFormatter();

    	table.setColumnFooter(columnNames.get(TARGET_COLUMN), "Σ " + printer.print(period.normalizedStandard(PeriodType.time()))  + " h");
    	
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
