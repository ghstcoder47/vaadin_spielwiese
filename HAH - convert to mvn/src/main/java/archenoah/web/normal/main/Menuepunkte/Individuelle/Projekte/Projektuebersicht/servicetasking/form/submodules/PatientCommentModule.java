package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.form.submodules;

import archenoah.lib.vaadin.Language.i18n.I18nCB;
import archenoah.lib.vaadin.Language.i18n.I18nManager;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.form.ServiceTaskingInsertEdit;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.modules.ModuleComponent;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.modules.ModuleComponent.FieldValue;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.modules.ModuleComponent.FieldValueChangeListener;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Patientenverwaltung.classes.PatientCommentComponent;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;

public class PatientCommentModule extends PatientCommentComponent {


    // {section fields}
    // ****************
    private ModuleComponent module;
    
    private I18nManager i18n;
    
    private final static class caption extends I18nCB {
        static final I18nCB idFilter = set();
    }
    // {end fields}

    // {section constructors}
    // **********************
    public PatientCommentModule(ModuleComponent module) {
        super();
        this.module = module;
        
        i18n = new I18nManager(this);
        
        setType(ServiceTaskingInsertEdit.class);
        
        module.addFieldChangeListener(new FieldValueChangeListener() {
            
            @Override
            public void onFieldValueChange(FieldValue fieldValue, Object value, Class<?> type) {
                
                switch (fieldValue) {
                case PARENT:
                    if(value != null) {
                        setDataId((Integer) value);
                        addTopComponent(getIdFilter());
                    }
                    break;
                    
                case PATIENT:
                    setParentId((Integer) value);
                    break;
                    
                case READONLY:
                    allowAdd((Boolean) value);
                    break;
                default:
                    break;
                }
                
            }
        });
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private CheckBox getIdFilter() {
        
        CheckBox idFilter = new CheckBox(i18n.get(caption.idFilter));
        idFilter.setImmediate(true);
        idFilter.addValueChangeListener(new ValueChangeListener() {
            
            @Override
            public void valueChange(ValueChangeEvent event) {
                filterDataId( (boolean) event.getProperty().getValue() ? module.getParentId() : null);
            }
        });
        
        return idFilter;
        
    }
    // {end privatemethods}

    // {section database}
    // ******************
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
