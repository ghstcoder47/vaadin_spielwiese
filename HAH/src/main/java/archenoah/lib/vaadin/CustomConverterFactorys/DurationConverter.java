package archenoah.lib.vaadin.CustomConverterFactorys;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.Duration;

import com.vaadin.data.util.converter.Converter;

public class DurationConverter implements Converter<String, Duration>{
    
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DurationConverter.class);
    
    private Pattern datePatt;
    private Matcher matcher;
    
    public DurationConverter() {
        
        datePatt = Pattern.compile("((\\d*?):?([0-5]?[0-9]))");

        
    }

    @Override
    public Duration convertToModel(String value, Class<? extends Duration> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        
        if(value == null){
            return null;
        }
        
        int hours = 0;
        int minutes = 0;
        
        //TASK: something is broken here
        
        matcher = datePatt.matcher(value);
        
        if (matcher.matches()) {
            
          hours = "".equals(matcher.group(2)) ? 0 : Integer.parseInt(matcher.group(2));
          minutes  = "".equals(matcher.group(3)) ? 0 : Integer.parseInt(matcher.group(3));
          
        }
        
        return new Duration(hours*3600*1000 + minutes*60*1000);
    }

    @Override
    public String convertToPresentation(Duration value, Class<? extends String> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        
        if(value == null){
            return null;
        }
        
        String result = value.getStandardHours() + ":" + leftPad(value.getStandardMinutes() - value.getStandardHours()*60); 
        
        return result;
    }

    @Override
    public Class<Duration> getModelType() {
        return Duration.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
    
    private String leftPad(Long l) {
        
        return (l <= 9) ? "0" + l : Integer.toString(l.intValue()); 
    }
    
    /*
     * Private
     */
    
//    private String padWithZero(Integer i){
//        
//        return (i <= 9) ? 0 + i.toString() : i.toString();
//        
//    }
}
