package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Reporting.classes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.tepi.filtertable.FilterTable;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.data.Item;
import com.vaadin.ui.CustomTable;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;
import archenoah.lib.vaadin.Components.table.FooterTableUpdater;
import archenoah.lib.vaadin.Components.table.FooterTableUpdater.FooterJob;
import archenoah.lib.vaadin.Components.table.footer.calculations.CalculatedQuota;
import archenoah.lib.vaadin.Components.table.footer.calculations.Count;
import archenoah.lib.vaadin.Components.table.footer.calculations.Quota;
import archenoah.lib.vaadin.Components.table.footer.calculations.Sum;

public class ReportingFooterUpdater {
	
	private static final String CRFC_TYPE = "CRFC_TYPE";
	private static final String	CRFC_COLUMN = "CRFC_COLUMN";
	private static final String CRFC_DIVIDEND = "CRFC_DIVIDEND";
	private static final String CRFC_DIVISOR = "CRFC_DIVISOR";
	private static final String CRFC_SCALING = "CRFC_SCALING";

	protected static List<FooterTableUpdater.FooterJob> jobList = new ArrayList<>();
	
	public enum TableFooterCalculation{		
		
		SUM() {
			@Override
			public void calculate(CustomTable table, Map<String, String> columnValues) {
				String columnName = targetColumn(columnValues);
				
				Map<String, String> columns = new HashMap<>();
				columns.put(Sum.TARGET_COLUMN, columnName);
				jobList.clear();
				jobList.add(new FooterJob(columns, new Sum(), extractScaling(columnValues)));

			}
			
		},
		COUNT() {
			@Override
			public void calculate(CustomTable table, Map<String, String> columnValues) {
				// ziehe zähle die anzahl

				String columnName = targetColumn(columnValues);
				
				Map<String, String> columns = new HashMap<>();
				columns.put(Count.TARGET_COLUMN, columnName);
				jobList.clear();
				jobList.add(new FooterJob(columns, new Count(), extractScaling(columnValues)));
				
			}
		},
		QUOTA() {

			@Override
			public void calculate(CustomTable table, Map<String, String> columnValues) {
				// 
				String columnName = targetColumn(columnValues);
				String dividendColumnName = columnValues.get(CRFC_DIVIDEND);
				String divisorColumnName = columnValues.get(CRFC_DIVISOR);

				Map<String, String> columns = new HashMap<>();
				columns.put(Quota.TARGET_COLUMN, columnName);
				columns.put(Quota.DIVIDEND_COLUMN, dividendColumnName);
				columns.put(Quota.DIVISOR_COLUMN, divisorColumnName);
				jobList.clear();
				jobList.add(new FooterJob(columns, new Quota(), extractScaling(columnValues)));
				
				
			}

		},
		CALCULATED_QUOTA() {
			
			@Override
			public void calculate(CustomTable table, Map<String, String> columnValues) {
				
				String columnName = targetColumn(columnValues);
				String dividendColumnName = columnValues.get(CRFC_DIVIDEND);
				String divisorColumnName = columnValues.get(CRFC_DIVISOR);

				Map<String, String> columns = new HashMap<>();
				columns.put(CalculatedQuota.TARGET_COLUMN, columnName);
				columns.put(CalculatedQuota.DIVIDEND_COLUMN, dividendColumnName);
				columns.put(CalculatedQuota.DIVISOR_COLUMN, divisorColumnName);
				jobList.clear();
//				jobList.add(new FooterJob(columns, new CalculatedQuota(), extractScaling(columnValues)));
				
			}
			
		},
		MONTHLY_AVERAGE_OF_THREE_MONTHS() {
			@Override
			public void calculate(CustomTable table, Map<String, String> columnValues) {
				
				final BigDecimal THREE_MONTHS = new BigDecimal(3);
				
				BigDecimal summe = BigDecimal.ZERO;
				String columnName = targetColumn(columnValues);
				String dividendColumnName = columnValues.get(CRFC_DIVIDEND);
				
				summe = columnSummate(table, dividendColumnName).divide(THREE_MONTHS, extractScaling(columnValues), RoundingMode.HALF_UP);
		    					
				table.setColumnFooter(columnName, "Σ: " + summe);
				
			}
		};

		
		
		
		
		public abstract void calculate(CustomTable table, Map<String, String> columnValues);
			
		
		
		protected  String targetColumn(Map<String, String> columnValues) {
			String columnName = columnValues.get(CRFC_COLUMN);
			return columnName;
		}

		protected Integer extractScaling(Map<String, String> columnValues) {
									
			return Integer.valueOf(columnValues.get(CRFC_SCALING));
		}
		
		protected BigDecimal columnSummate(CustomTable table, String columnName) {
			Iterator itemIterator = table.getItemIds().iterator();
			
			BigDecimal summe = BigDecimal.ZERO;
					
			while(itemIterator.hasNext()){
	    		
	    		int iid = (Integer) itemIterator.next();
	    		
	    		Item item = table.getItem(iid);
	    		    		
	    		BigDecimal tmpValue;
	    		Object obj = item.getItemProperty(columnName).getValue();
    		
	    		if (obj instanceof Long) {
	    			tmpValue = new BigDecimal((Long)obj);
	    		} else if(obj instanceof Double) {
	    			tmpValue = new BigDecimal((Double)obj);
	    		} else {
	    			tmpValue = (BigDecimal) item.getItemProperty(columnName).getValue();
	    		}
	    				    		
	    		summe = summe.add(tmpValue);  	

	    	}
			return summe;
		}
		
	}

	// {section fields}
	// ****************

	protected FilterTable table;
	private String view;
	
	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ReportingFooterUpdater.class);
	
	// {end fields}

	// {section constructors}
	// **********************
	public ReportingFooterUpdater(FilterTable table) {
		this.table = table;
		config();
	}
	// {end constructors}

	// {section gettersandsetters}
	// ***************************

	public void setViewName(String view) {
		this.view = view;
	}

	// {end gettersandsetters}

	// {section publicmethods}
	// ***********************
	public void updateTableFooter() {

		/*
		 * - DB: suche in nach spalten mit footerUpdate - initialisiere
		 * berechnung - führe berechnung aus
		 */

		Container con = dbGetCalculableColumnNames();

		for (Object item : con.getItemIds()) {
			
			performFooterCalculation(con, item);
		}

		FooterTableUpdater.performFooterCalculation(table, jobList);
		
	}

	private void performFooterCalculation(Container con, Object item) {
		
//		List<String> columnName = new ArrayList<>();
		Map<String, String> columnName = new HashMap<>();
		
		String calculationType = readValuesFromDB(con, item, CRFC_TYPE);		
		columnName.put(CRFC_COLUMN, readValuesFromDB(con, item, CRFC_COLUMN));
		columnName.put(CRFC_DIVIDEND, readValuesFromDB(con, item, CRFC_DIVIDEND));
		columnName.put(CRFC_DIVISOR, readValuesFromDB(con, item, CRFC_DIVISOR));
		columnName.put(CRFC_SCALING, readValuesFromDB(con, item, CRFC_SCALING));
						
		TableFooterCalculation toCalculate = TableFooterCalculation.valueOf(calculationType);			
		toCalculate.calculate(table, columnName);

	}
	
	private String readValuesFromDB(Container con, Object item, String columnName) {
		String calculationType = MyUtils.getValueFromItem(con.getItem(item), columnName, String.class);
		return calculationType;
	}
	// {end publicmethods}

	// {section privatemethods}
	// ************************
	private void config() {

		table.addItemSetChangeListener(new ItemSetChangeListener() {

			@Override
			public void containerItemSetChange(ItemSetChangeEvent event) {

				updateTableFooter();

			}
		});
	}

	// {end privatemethods}

	// {section database}
	// ******************

	private Container dbGetCalculableColumnNames() {

		CustomQuery q = new DBClass().CustomQuery;

		String sql = "SELECT" 
				+ "\n " + "	CRFC_COLUMN" 
				+ "\n " + "	, CRFC_TYPE" 
				+ "\n " + "	, CRFC_DIVIDEND" 
				+ "\n " + "	, CRFC_DIVISOR"
				+ "\n " + "	, CRFC_SCALING"
				+ "\n " + "FROM" 
				+ "\n "	+ "	cust_report_footer_calculation" 
				+ "\n " + "LEFT JOIN" 
				+ "\n "	+ "	cust_report_views ON CRFC_ID_VIEWS = CRV_ID" 
				+ "\n "	+ "WHERE" 
				+ "\n " + "	CRV_VIEW = '" + view + "';";

		q.setSqlString(sql);

		// q.db.debugNextQuery(true);

		return q.query();
	}

	// {end database}

	// {section layout}
	// ****************
	// {end layout}
}
