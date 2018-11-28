package archenoah.lib.vaadin.CustomConverterFactorys;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.LocalTime;

import com.vaadin.data.util.converter.Converter;

public class LocalTimeConverter implements Converter<String, LocalTime>{
    
    private Pattern datePatt;
    private Matcher matcher;
    
    public LocalTimeConverter() {
        
        datePatt = Pattern.compile("(([01]?[0-9]|2[0-3]):?([0-5][0-9]))");

        
    }

    @Override
    public LocalTime convertToModel(String value, Class<? extends LocalTime> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        
        if(value == null){
            return null;
        }
        
        int hours = 0;
        int minutes = 0;
        
        matcher = datePatt.matcher(value);
        if (matcher.matches()) {
          hours = Integer.parseInt(matcher.group(2));
          minutes  = Integer.parseInt(matcher.group(3));
        }
        
        return new LocalTime(hours,minutes,0,0);
    }

    @Override
    public String convertToPresentation(LocalTime value, Class<? extends String> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        
        if(value == null){
            return null;
        }
        
        String result = padWithZero(value.getHourOfDay()) + ":" + padWithZero(value.getMinuteOfHour());
        
        return result;
    }

    @Override
    public Class<LocalTime> getModelType() {
        return LocalTime.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
    
    /*
     * Private
     */
    
    private String padWithZero(Integer i){
        
        return (i <= 9) ? 0 + i.toString() : i.toString();
        
    }
}
