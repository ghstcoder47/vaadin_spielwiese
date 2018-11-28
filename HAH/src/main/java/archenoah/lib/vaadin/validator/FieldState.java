package archenoah.lib.vaadin.validator;

import java.util.ArrayList;

import archenoah.lib.vaadin.Components.Validators.RemovableValidator;

import com.vaadin.ui.AbstractField;

public class FieldState {
    
    private AbstractField<?> field;
    private boolean wasRequired;
    private boolean wasValidationVisible;
    
    private ArrayList<RemovableValidator> validators;
    
    public FieldState(AbstractField<?> field) {
        this.field = field;
        wasRequired = field.isRequired();
        wasValidationVisible = field.isValidationVisible();
        
        validators = new ArrayList<RemovableValidator>();
    }

    public void addValidator(RemovableValidator validator) {
        validators.add(validator);
    }
    
    public void restore() {
        field.setRequired(wasRequired);
        field.setValidationVisible(wasValidationVisible);
        
        for (RemovableValidator dv : validators) {
            dv.remove();
        }
        validators = new ArrayList<RemovableValidator>();
    }
    
}
