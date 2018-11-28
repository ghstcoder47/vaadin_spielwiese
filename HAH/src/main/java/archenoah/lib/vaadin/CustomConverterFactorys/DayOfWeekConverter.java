package archenoah.lib.vaadin.CustomConverterFactorys;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

public class DayOfWeekConverter implements Converter<String, Integer>{
	
	DateFormatSymbols symbols;
	String[] dayNames;
	
	public DayOfWeekConverter(){
		symbols = new DateFormatSymbols();
		
		ArrayList<String> names = new ArrayList<String>(Arrays.asList(symbols.getWeekdays()));
		
		String sunday = names.get(1);
		names.remove(sunday);
		names.add(sunday);
		
		dayNames = names.toArray(new String[0]);
	};
	
	@Override
	public Integer convertToModel(String value,
			Class<? extends Integer> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		
		return Arrays.asList(dayNames).indexOf(value);
		
	}

	@Override
	public String convertToPresentation(Integer value,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		
	    if(value == null){
	        return dayNames[0];
	    }
	    
		return dayNames[value];
	}

	@Override
	public Class<Integer> getModelType() {
		return Integer.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
