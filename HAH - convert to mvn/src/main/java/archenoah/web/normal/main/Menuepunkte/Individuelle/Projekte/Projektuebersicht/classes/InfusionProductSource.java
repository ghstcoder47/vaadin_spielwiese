package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.classes;

import java.util.Arrays;
import java.util.Collection;

import archenoah.lib.vaadin.Language.i18n.I18nManager;

import com.vaadin.ui.ComboBox;

public class InfusionProductSource extends ComboBox{

    // {section fields}
    // ****************
    
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(InfusionProductSource.class);
    
    private I18nManager i18n;
    public static String internal = "int";
    public static String external = "ext";
    
    private Collection<?> values;
    
    private boolean first = true;
    
    // {end fields}

    // {section constructors}
    // **********************
    public InfusionProductSource() {
        super();
        
        this.setNullSelectionAllowed(false);
        
        i18n = new I18nManager(InfusionProductSource.class.getCanonicalName(),
            Arrays.asList(
                internal,
                external
        ));
        
        this.addItem(internal);
        this.setItemCaption(internal, i18n.get(internal));
        
        this.addItem(external);
        this.setItemCaption(external, i18n.get(external));
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    public void setType(ComboBox parent, Object...values ) {
        setType(parent, Arrays.asList(values));
    }
    
    public void setType(ComboBox parent, Collection<?> values) {
        
        this.values = values;
        
        parent.addValueChangeListener(new ValueChangeListener() {
            
            @Override
            public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
                
                handleValue(event.getProperty().getValue());
                
            }
        });
        handleValue(parent.getValue());
        
    }
    
    public boolean affectsBalance() {
        return InfusionProductSource.internal.equals(getValue());
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private void handleValue(Object value) {
        
        boolean req = values.contains(value);
        this.setRequired(req);
        this.setVisible(req);
        
        //ignore first value change - ValuceChangeListener fires 'retroactively' even when added after fillForm()! 
        if(first) {
            first = false;
        }else {
            this.setValue(req ? null : InfusionProductSource.internal);
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
