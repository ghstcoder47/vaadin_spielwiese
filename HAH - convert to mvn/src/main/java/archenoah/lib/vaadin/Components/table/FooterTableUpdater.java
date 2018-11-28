package archenoah.lib.vaadin.Components.table;

import java.util.List;
import java.util.Map;

import org.tepi.filtertable.FilterTable;

import archenoah.lib.vaadin.Components.table.footer.FooterCalculable;

public class FooterTableUpdater {
	// {section fields}
	// ****************
	// {end fields}

	// {section constructors}
	// **********************
	public static class FooterJob {
		Map<String, String> columns;
		FooterCalculable calculationJob;
		int scaling;
		
		
		public FooterJob(Map<String, String> column, FooterCalculable calculationJob, int scaling) {
			this.columns = column;
			this.calculationJob = calculationJob;
			this.scaling = scaling;
		}


		public Map<String, String> getColumn() {
			return columns;
		}

		public FooterCalculable getCalculationJob() {
			return calculationJob;
		}


		public int getScaling() {
			return scaling;
		}
		
		
	}

	
	// {end constructors}

	// {section gettersandsetters}
	// ***************************
	// {end gettersandsetters}

	// {section publicmethods}
	// ***********************
	
	public static void performFooterCalculation(FilterTable table, List<FooterJob> jobList){
				
		for (FooterJob footerJob : jobList) {
			
			footerJob.getCalculationJob().calculate(table, footerJob.getColumn(), footerJob.getScaling());	
		}
		
	}
	
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
