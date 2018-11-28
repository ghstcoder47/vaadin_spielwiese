package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.modules;

import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.modules.converters.ModuleFieldConverter;

import com.vaadin.ui.AbstractField;

public class ModuleField<T> {

     // {section fields}
    // ****************
    private AbstractField<T> field;
    private String targetColumn;
    private ModuleFieldConverter converter;
    private Object keyValue;
    
    
    // {end fields}


    // {section constructors}
    // **********************
    public ModuleField(AbstractField<T> field, String targetColumn, Object keyValue, ModuleFieldConverter converter) {
        this.field = field;
        this.targetColumn = targetColumn;
        this.converter = converter;
        this.keyValue = keyValue;
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************

    public AbstractField<?> getField() {
        return field;
    }

    public String getTargetColumn() {
        return targetColumn;
    }

    public ModuleFieldConverter getConverter() {
        return converter;
    }
    
    public Object getKeyValue() {
        return keyValue;
    }
    
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    
    public boolean hasConverter() {
        return converter != null;
    }
    
    public void setValue(T newFieldValue) {
        field.setValue(newFieldValue);
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
