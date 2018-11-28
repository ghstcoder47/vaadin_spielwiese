package archenoah.lib.vaadin.CustomConverterFactorys;

import java.util.Locale;

import org.joda.time.LocalDate;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.UI;

public class WeekdayConverter implements Converter<String, Integer> {
    
    @Override
    public Integer convertToModel(String value, Class<? extends Integer> targetType, Locale locale)
        throws com.vaadin.data.util.converter.Converter.ConversionException {
        return null;
    }

    @Override
    public String convertToPresentation(Integer value, Class<? extends String> targetType, Locale locale)
        throws com.vaadin.data.util.converter.Converter.ConversionException {
        
        if(value == null || value < 1 || value > 7) {
            return null;
        }
        
        LocalDate date = new LocalDate().withDayOfWeek(value);
        return date.dayOfWeek().getAsText(UI.getCurrent().getLocale());

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
