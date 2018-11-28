package archenoah.lib.vaadin.Components.table.footer.calculations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.tepi.filtertable.FilterTable;

import archenoah.lib.vaadin.Components.table.footer.FooterCalculable;

public class Quota implements FooterCalculable {

	public final static String TARGET_COLUMN = "targetColumn";
	public final static String DIVIDEND_COLUMN = "dividendColumn";
	public final static String DIVISOR_COLUMN = "divisorColumn";
	
	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Quota.class);
	
	@Override
	public void calculate(FilterTable table, Map<String, String> columnNames, int scaling) {
		// FIXME Auto-generated method stub
		
		String columnName = columnNames.get(TARGET_COLUMN); //targetColumn(columnValues);
		String dividendColumnName = columnNames.get(DIVIDEND_COLUMN); //columnValues.get(CRFC_DIVIDEND);
		String divisorColumnName = columnNames.get(DIVISOR_COLUMN); //columnValues.get(CRFC_DIVISOR);
										
		log.info("dividend: {}, divisor: {}", dividendColumnName, divisorColumnName);
		
		BigDecimal sumDividend = Sum.columnSummate(table, dividendColumnName);
		BigDecimal sumDivisor = Sum.columnSummate(table, divisorColumnName);
		
		log.info("dividendValue: {}, divisorValue: {}", sumDividend, sumDivisor);
		
		BigDecimal result = sumDividend.divide(sumDivisor, scaling, RoundingMode.HALF_UP);
		
		table.setColumnFooter(columnName, "Quote: " + result.toString());
		
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
