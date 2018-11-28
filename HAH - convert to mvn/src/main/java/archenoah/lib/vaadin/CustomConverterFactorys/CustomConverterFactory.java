package archenoah.lib.vaadin.CustomConverterFactorys;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.DefaultConverterFactory;
import com.vaadin.ui.Embedded;

public class CustomConverterFactory extends DefaultConverterFactory {

	@Override
	protected <PRESENTATION, MODEL> Converter<PRESENTATION, MODEL> findConverter(Class<PRESENTATION> presentationType, Class<MODEL> modelType) {
		// Handle String <-> Double
		
		if (presentationType == String.class && modelType == LocalDate.class) {
	        return (Converter<PRESENTATION, MODEL>) new StringLocalDateConverter();
		}
		
		if (presentationType == String.class && modelType == Boolean.class) {
            return (Converter<PRESENTATION, MODEL>) new BooleanToTextConverter();
        }
		
		if (presentationType == Embedded.class && modelType == Boolean.class) {
            return (Converter<PRESENTATION, MODEL>) new BooleanToIconConverter();
        }
		
		if (presentationType == String.class && modelType == LocalTime.class) {
            return (Converter<PRESENTATION, MODEL>) new LocalTimeConverter();
        }
		
		// Let default factory handle the rest
		return super.findConverter(presentationType, modelType);
    }

}
