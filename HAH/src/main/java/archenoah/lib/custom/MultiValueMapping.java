package archenoah.lib.custom;

import java.util.ArrayList;
import java.util.Collection;

import com.vaadin.ui.AbstractSelect;

public class MultiValueMapping {
    
    // {section autofields}
    // ********************
    // {end autofields}

    // {section fields}
    // ****************
    private AbstractSelect component;
    
    private ArrayList<Object> databaseValues;
    private ArrayList<Object> addValues;
    private ArrayList<Object> removeValues;
    
    
    // {end fields}

    // {section constructors}
    // **********************
    public MultiValueMapping(AbstractSelect select) {
        super();
        this.component = select;
        
        setDatabaseState();
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    public void setDatabaseState() {
        databaseValues = new ArrayList<Object>();
        
        if(component.getValue() instanceof Collection) {
            databaseValues.addAll((Collection<? extends Object>) component.getValue());
        }
    }
    
    public ArrayList<Object> getAddValues(){
        calculateValues();
        return addValues;
    }
    
    public ArrayList<Object> getRemoveValues(){
        calculateValues();
        return removeValues;
    }
    
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    
    private void calculateValues() {
        
        addValues = new ArrayList<Object>();
        addValues.addAll((Collection<? extends Object>) component.getValue());
        addValues.removeAll(databaseValues);
        
        removeValues = new ArrayList<Object>();
        removeValues.addAll(databaseValues);
        removeValues.removeAll((Collection<? extends Object>) component.getValue());
        
    }
    
    // {end privatemethods}

    // {section database}
    // ******************
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
