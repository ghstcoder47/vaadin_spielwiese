package archenoah.lib.vaadin.Components.table.footer.calculations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.tepi.filtertable.FilterTable;

import archenoah.lib.vaadin.Components.table.footer.FooterCalculable;

public class CalculatedQuota implements FooterCalculable {

	public final static String TARGET_COLUMN = "targetColumn";
	public final static String DIVIDEND_COLUMN = "dividendColumn";
	public final static String DIVISOR_COLUMN = "divisorColumn";
	
	@Override
	public void calculate(FilterTable table, Map<String, String> columnNames, int scaling) {
		// FIXME Auto-generated method stub

		String columnName = columnNames.get(TARGET_COLUMN); //targetColumn(columnValues);
		String dividendColumnName = columnNames.get(DIVIDEND_COLUMN); //columnValues.get(CRFC_DIVIDEND);
		String divisorColumnName = columnNames.get(DIVISOR_COLUMN); //columnValues.get(CRFC_DIVISOR);
										
		BigDecimal sumDividend = Sum.columnSummate(table, dividendColumnName);
		BigDecimal sumDivisor = Sum.columnSummate(table, divisorColumnName);
		BigDecimal sumDividendDivisor = sumDividend.add(sumDivisor);
		
		BigDecimal result = sumDividend.divide(sumDividendDivisor , scaling, RoundingMode.HALF_UP);
		
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
