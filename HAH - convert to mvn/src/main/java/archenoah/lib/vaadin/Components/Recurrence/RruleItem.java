package archenoah.lib.vaadin.Components.Recurrence;

import java.util.Date;
import java.util.HashMap;

import archenoah.lib.custom.MyUtils;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.AbstractField;

@SuppressWarnings("unchecked")
public class RruleItem extends PropertysetItem{


    // {section fields}
    // ****************
    private HashMap<PART, Object> properties;
    private HashMap<PART, AbstractField> fields;
    // {end fields}

    // {section constructors}
    // **********************
    public RruleItem() {
        properties = new HashMap<PART, Object>();
        this.addItemProperty(PART.START, new ObjectProperty<Date>(new Date()));
        this.addItemProperty(PART.RRULE, new ObjectProperty<String>(""));
        this.addItemProperty(PART.CUSTOM, new ObjectProperty<Boolean>(false));
    }
    
    public RruleItem(Item item) {
        
        this();
        initItem(item);
        
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************

    public void setPropertyFor(PART part, Object propertyId) {
        properties.put(part, propertyId);
        try {
            getItemProperty(part).setValue(MyUtils.getValueFromItem(this, propertyId, Object.class));
        } catch (Exception e) {
        }
        
    }
    
    public Date getStart() {
        return MyUtils.getValueFromItem(this, PART.START, Date.class);
    }
    
    public String getRrule() {
        return MyUtils.getValueFromItem(this, PART.RRULE, String.class);
    }
    
    public Boolean isCustom() {
        return MyUtils.getValueFromItem(this, PART.CUSTOM, Boolean.class);
    }
    
    public void setStart(Date start) {
        getItemProperty(PART.START).setValue(start);
    }
    
    public void setRrule(String rrule) {
        getItemProperty(PART.RRULE).setValue(rrule);
    }
    
    public void setCustom(Boolean custom) {
        getItemProperty(PART.CUSTOM).setValue(custom);
    }
    
    public Date getOriginalStart() {
        return MyUtils.getValueFromItem(this, properties.get(PART.START), Date.class);
    }
    
    public String getOriginalRrule() {
        return MyUtils.getValueFromItem(this,  properties.get(PART.RRULE), String.class);
    }
    
    public Boolean isOriginalCustom() {
        return MyUtils.getValueFromItem(this,  properties.get(PART.CUSTOM), Boolean.class);
    }
    
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private void initItem(Item item) {
        
        for (Object pid : item.getItemPropertyIds()) {
            this.addItemProperty(pid, item.getItemProperty(pid));
        }
    }
    // {end privatemethods}

    // {section database}
    // ******************
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
