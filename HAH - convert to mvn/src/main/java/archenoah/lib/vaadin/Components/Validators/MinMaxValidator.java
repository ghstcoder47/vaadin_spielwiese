package archenoah.lib.vaadin.Components.Validators;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.vaadin.Language.i18n.I18nManager;

import com.vaadin.data.Validator;
import com.vaadin.ui.AbstractField;


public class MinMaxValidator implements Validator, RemovableValidator{

    // {section fields}
    // ****************
    
    private static final long serialVersionUID = -1163740372919165829L;
    
    private AbstractField<?> field;

    private BigDecimal min;
    private BigDecimal max;
    
    private static final I18nManager i18n;
    private static final String error = "min_max_error";
    
    static {
        i18n = new I18nManager(MinMaxValidator.class.getCanonicalName(), Arrays.asList(error));
    }
    
    // {end fields}

    // {section constructors}
    // **********************
    public MinMaxValidator(AbstractField<?> field, BigDecimal min, BigDecimal max) {
        
        this.field = field;
        this.min = min;
        this.max = max;
        field.addValidator(this);
        
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    @Override
    public void validate(Object value) throws InvalidValueException {
        
        try {
            BigDecimal val = MyUtils.toBigDecimal(value, 4);
            
            if(
                val == null
                || (min != null && val.compareTo(min) < 0)
                || (max != null && val.compareTo(max) > 0)
            ){
                throw new InvalidValueException(i18n.get(error) + getMinMaxInfo());
            }
            
        } catch (Exception e) {
            throw new InvalidValueException(i18n.get(error) + getMinMaxInfo());
        }
        
        
    }
    
    @Override
    public void remove() {
        field.removeValidator(this);
    }

    
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    
    private String getMinMaxInfo() {
        StringBuilder sb = new StringBuilder(" |");
        if(min != null ) {
            sb.append(" > ").append(min.setScale(4, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
        }
        if(max != null) {
            sb.append(", < ").append(max.setScale(4, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
        }
        return sb.toString();
    }
    
    // {end privatemethods}

    // {section database}
    // ******************
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
