package archenoah.lib.vaadin.Components.Fields;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.vaadin.csvalidation.CSValidator;

import archenoah.lib.vaadin.CustomConverterFactorys.DurationConverter;

import com.vaadin.data.Property;
import com.vaadin.ui.TextField;

/**
 * 
 * @author Martin Knühl
 *
 */
@SuppressWarnings({ "serial", "rawtypes" })
public class CustomDurationPicker extends TextField {

    private PropertyDuration duration;
    
    /*
     * Constructor
     */
    
    public CustomDurationPicker() {
        configureComponents();
    }

    /*
     * Public
     */
    public void setValue(int hours, int minutes){
        duration.setValue(new Duration(hours*3600*1000 + minutes*60*1000));
    }
    
    public void setValue(Duration ld){
        duration.setValue(ld);
    }
    
    public void setValue(Date date){
        
        LocalDateTime ld = new LocalDateTime(date);
        setValue(ld.getHourOfDay(), ld.getMinuteOfHour());
    }
    
    @Override
    public void setValue(String value){
        
        if(value == null){
            duration.setValue(null);
            return;
        }
        
        if("".equals(value)) {
            super.setValue(null);
            return;
        }
        
        String[] values = value.split(":");
        
        if(values.length > 1
            && StringUtils.isNumeric(values[0])
            && StringUtils.isNumeric(values[1])){
            setValue(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
        }else {
            duration.setValue(null);
        }
       
    }
    
    public Duration getDuration(){
        return duration.getValue();
    }
    
    /*
     * Private
     */
    
    private void configureComponents(){
      
        duration = new PropertyDuration();
        
        setProperties();
        setListeners();
        setImmediate(true);
        
    }
    
    private void setProperties(){
        this.setImmediate(true);
        this.setNullRepresentation("");
        this.setInputPrompt("--:--");
        this.setNullSettingAllowed(true);
        this.setWidth("3em");
        this.setInvalidCommitted(true);
        
        final CSValidator validator = new CSValidator();
        validator.extend(this);
        validator.setRegExp("((\\d*?)?:?([0-5]?[0-9])?)?");
        validator.setPreventInvalidTyping(true);
        
        this.setConverter(new DurationConverter());
        this.setPropertyDataSource(duration);
        
    }
    
    private void setListeners(){
        
    }
    
    /*
     * Classes
     */
    
    class PropertyDuration implements Property<Duration>{
        
        private Duration lt;
        
        public PropertyDuration(){
//            lt = new LocalTime(0,0,0,0);
        }
        
        @Override
        public Duration getValue() {
            
//            if(lt.equals(LocalTime.MIDNIGHT)){
//                return null;
//            }
            
            return lt;
        }
        
        @Override
        public void setValue(Duration newValue) throws com.vaadin.data.Property.ReadOnlyException {
            lt = newValue;
            markAsDirty();
        }

        @Override
        public Class<? extends Duration> getType() {
            return Duration.class;
        }

        @Override
        public boolean isReadOnly() {
            // TASK Auto-generated method stub
            return false;
        }

        @Override
        public void setReadOnly(boolean newStatus) {
            // TASK Auto-generated method stub
            
        }
        
    }

}
