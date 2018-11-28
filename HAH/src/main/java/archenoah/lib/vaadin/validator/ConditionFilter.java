package archenoah.lib.vaadin.validator;

import archenoah.lib.custom.MyUtils;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;

public class ConditionFilter implements Filter{


    // {section fields}
    // ****************
    
    private ValidatorType type;
    private Object value;
    
    // {end fields}

    // {section constructors}
    // **********************
    public ConditionFilter(ValidatorType type, Object value) {
        this.type = type;
        this.value = value;
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    public ValidatorType getType() {
        return type;
    }
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    @Override
    public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
        
        if(getType() == null) {
            return true;
        }
        
        return getType().name().equals(item.getItemProperty(ValidatorManager.rType).getValue())
            && MyUtils.equalsWithNulls(
                ((value != null) ? value.toString() : null),
                item.getItemProperty(ValidatorManager.rVal).getValue());

    }

    @Override
    public boolean appliesToProperty(Object propertyId) {
        return ValidatorManager.rType.equals(propertyId) || ValidatorManager.rVal.equals(propertyId);
    }
    
    @Override
    public boolean equals(Object filter) {
        if(filter instanceof ConditionFilter) {
            return getType() != null && getType().equals(((ConditionFilter) filter).getType());
        }else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        // avoid hash conflicts with enum
        return ("cf" + getType()).hashCode();
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
