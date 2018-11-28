package archenoah.lib.vaadin.Components.Validators;

import org.vaadin.csvalidation.CSValidator;

import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.AbstractTextField;

public class RegexValidator {

    private AbstractTextField field;
    private CSValidator validator;
    
    public RegexValidator(AbstractTextField field, String regex, String error, String format, boolean preventInvalid) {

        this.field = field;
        
        final CSValidator validator = new CSValidator();
        validator.extend(field);
        if(format != null) {
            validator.setRegExp(regex, format);
            field.setInputPrompt(format);
        }else {
            validator.setRegExp(regex);
        }
        field.addValidator(new RegexpValidator(regex, error != null ? error : ""));
        validator.setPreventInvalidTyping(preventInvalid);
        
        this.validator = validator;
        
    }
    
    /**
     * removes this Validator from the assigned field
     */
    public void remove() {
        validator = null;
    }
    
}
