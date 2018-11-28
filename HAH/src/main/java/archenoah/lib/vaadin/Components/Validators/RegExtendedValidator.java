package archenoah.lib.vaadin.Components.Validators;

import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.AbstractTextField;

public class RegExtendedValidator extends RegexpValidator implements RemovableValidator{

    // {section fields}
    // ****************
    
    private static final long serialVersionUID = -113893928536597601L;
    
    private AbstractTextField field;
    private String wasInputPrompt;
    // {end fields}

    // {section constructors}
    // **********************
    public RegExtendedValidator(AbstractTextField field, String regex, String error, String format) {
        super(regex, error);
        
        this.field = field;
        wasInputPrompt = field.getInputPrompt();
        field.setInputPrompt(format);
        
        field.addValidator(this);
        
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    @Override
    public void remove() {
        field.removeValidator(this);
        field.setInputPrompt(wasInputPrompt);
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    // {end privatemethods}

    // {section database}
    // ******************
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
