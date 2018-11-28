package archenoah.lib.vaadin.Components.Fields;

import java.util.Collection;
import java.util.HashMap;

import archenoah.lib.vaadin.Language.i18n.I18nGlobalCaptions;
import archenoah.lib.vaadin.Language.i18n.I18nManager;

import com.vaadin.data.Container;
import com.vaadin.ui.ComboBox;

public class BooleanComboBox extends ComboBox {


    // {section fields}
    // ****************
    private static HashMap<Object, Integer> mapping;
    static {
        mapping = new HashMap<Object, Integer>();
        mapping.put(true, 1);
        mapping.put(false, 0);
        mapping.put(1, 1);
        mapping.put(0, 0);
        mapping.put(-1, -1); // special case for tertiary select options (NullValueAllowed = true) 
    }
    // {end fields}

    // {section constructors}
    // **********************
    
    public BooleanComboBox() {
        super();
        init();
    }

    private BooleanComboBox(String caption, Collection<?> options) {}

    private BooleanComboBox(String caption, Container dataSource) {}

    public BooleanComboBox(String caption) {
        super(caption);
        init();
    }
    
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    @Override
    public void setValue(Object value) {
        
        if(mapping.containsKey(value)) {
            super.setValue(mapping.get(value));
        }else {
            super.setValue(null);
        }
        
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private void init() {
        addItem(-1);
        setItemCaption(-1, I18nManager.global(I18nGlobalCaptions.BOOL_UNKNOWN));
        
        addItem(1);
        setItemCaption(1, I18nManager.global(I18nGlobalCaptions.BOOL_TRUE));
        
        addItem(0);
        setItemCaption(0, I18nManager.global(I18nGlobalCaptions.BOOL_FALSE));
        
        setNullSelectionAllowed(false);
        setNullSelectionItemId(-1);
        
        setWidth("60px");
        
    }
    // {end privatemethods}



    // {section database}
    // ******************
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
