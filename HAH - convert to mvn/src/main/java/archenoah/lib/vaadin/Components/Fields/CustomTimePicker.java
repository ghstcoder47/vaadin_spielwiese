package archenoah.lib.vaadin.Components.Fields;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalTime;
import org.vaadin.csvalidation.CSValidator;

import archenoah.lib.vaadin.CustomConverterFactorys.LocalTimeConverter;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.ui.TextField;

/**
 * 
 * @author Martin Knühl
 *
 */
@SuppressWarnings({ "serial", "rawtypes" })
public class CustomTimePicker extends TextField {

    private PropertyTime time;
    private CustomTimePicker lower;
    private CustomTimePicker upper;
    
    /*
     * Constructor
     */
    
    public CustomTimePicker() {
        configureComponents();
    }

    /*
     * Public
     */
    public void setValue(int hours, int minutes){
        time.setValue(new LocalTime(hours, minutes, 0, 0));
    }
    
    public void setValue(LocalTime ld){
        time.setValue(ld);
    }
    
    public void setValue(Date date){
        time.setValue(new LocalTime(date));
    }
    
    @Override
    public void setValue(String value){
        
        if(value == null){
            time.setValue(null);
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
            time.setValue(null);
        }
       
    }
    
    public LocalTime getTime(){
        return time.getValue();
    }
    
    public void setLowerBound(final CustomTimePicker lower) {
        this.lower = lower;
    }
    
    public void setUpperBound(CustomTimePicker upper) {
        this.upper = upper;
    }
    
    /*
     * Private
     */
    
    private void configureComponents(){
      
        time = new PropertyTime();
        
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
        validator.setRegExp("(([01]?[0-9]|2[0-3])?:?([0-5]?[0-9])?)?");
        validator.setPreventInvalidTyping(true);
        
        this.setConverter(new LocalTimeConverter());
        this.setPropertyDataSource(time);
        
        final CustomTimePicker self = this;
        
        this.addValidator(new Validator() {
            
            @Override
            public void validate(Object value) throws InvalidValueException {
                
                if(lower != null && getTime() != null && lower.getTime() != null && getTime().isBefore(lower.getTime())) {
                    throw new InvalidValueException(">= " + lower.getValue());
                }
                
                if(upper != null && getTime() != null && upper.getTime() != null && getTime().isAfter(upper.getTime())) {
                    throw new InvalidValueException("<= " + upper.getValue());
                }
                
            }
        });
        
    }
    
    private void setListeners(){
        
        final CustomTimePicker self = this;
        
        this.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
                
                if(lower != null) {
                    lower.isValid();
                    lower.markAsDirty();
                }
                
                if(upper != null) {
                    upper.isValid();
                    upper.markAsDirty();
                }
                
                self.markAsDirty();

            }

        });
        
        
        
    }
    
    /*
     * Classes
     */
    
    class PropertyTime implements Property<LocalTime>{
        
        private LocalTime lt;
        
        public PropertyTime(){
//            lt = new LocalTime(0,0,0,0);
        }
        
        @Override
        public LocalTime getValue() {
            
//            if(lt.equals(LocalTime.MIDNIGHT)){
//                return null;
//            }
            
            return lt;
        }
        
        @Override
        public void setValue(LocalTime newValue) throws com.vaadin.data.Property.ReadOnlyException {
            lt = newValue;
            markAsDirty();
        }

        @Override
        public Class<? extends LocalTime> getType() {
            return LocalTime.class;
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
