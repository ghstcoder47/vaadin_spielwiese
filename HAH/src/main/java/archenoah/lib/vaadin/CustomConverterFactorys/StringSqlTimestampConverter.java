package archenoah.lib.vaadin.CustomConverterFactorys;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

@SuppressWarnings("serial")
public class StringSqlTimestampConverter implements Converter<String, Object> {

    private int dateFormat = DateFormat.SHORT;
    private int timeFormat = 0;

    public void setDateFormat(int dateFormat) {
        this.dateFormat = dateFormat;
    };
    
    public void setTimeFormat(int dateFormat) {
        this.timeFormat = dateFormat;
    };
    
    public void setFormat(int dateFormat){
        setDateFormat(dateFormat);
    }
    
    
    
    @Override
    public Timestamp convertToModel(String value, Class<? extends Object> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        //TASK fix for includeTime
        DateFormat df = DateFormat.getDateInstance(dateFormat, locale);
        Date date = null;
        try {
            date = df.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Timestamp(0);
        }
        return new Timestamp(date.getTime());
    }

    @Override
    public String convertToPresentation(Object value, Class<? extends String> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        String presentation = null;
        if(value != null){
            
            if(timeFormat != 0){
                
                if(dateFormat != 0){
                    presentation = DateFormat.getDateTimeInstance(dateFormat, timeFormat, locale).format(value);
                }else{
                    presentation = DateFormat.getTimeInstance(timeFormat, locale).format(value);
                }
                
            }else{
                presentation = DateFormat.getDateInstance(dateFormat, locale).format(value);
            }
            
        }
        return presentation;
    }

    @Override
    public Class<Object> getModelType() {
        return Object.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }

}
