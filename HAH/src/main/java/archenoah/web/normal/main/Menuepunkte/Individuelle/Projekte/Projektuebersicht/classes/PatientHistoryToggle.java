package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.classes;

import java.util.Collection;

import org.tepi.filtertable.FilterTable;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.vaadin.Language.i18n.I18nCB;
import archenoah.lib.vaadin.Language.i18n.I18nGlobalNotifiers;
import archenoah.lib.vaadin.Language.i18n.I18nManager;
import archenoah.lib.vaadin.resources.Icons;

import com.vaadin.data.Item;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

public class PatientHistoryToggle {


    // {section fields}
    // ****************
    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    
    private MenuBar menu;
    private FilterTable table;
    private String patientField;
    private String nameField;
    
    private Integer patientId;
    private String patientName;
    private ToggleListener toggleListener;
    private MenuItem button;

    private I18nManager i18n;
    
    private final static class caption extends I18nCB{
        final static I18nCB patientHistory = set();
    }
    
    // {end fields}

    // {section constructors}
    // **********************
    public PatientHistoryToggle(String patientField, String nameField, MenuBar menu, FilterTable table) {
        this.patientField = patientField;
        this.nameField = nameField;
        this.menu = menu;
        this.table = table;
        
        i18n = new I18nManager(this);
        
        init();
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    public Integer getPatientId() {
        return patientId;
    }
    public void setToggleListener(ToggleListener tgl) {
        this.toggleListener = tgl;
    }
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    public void setEnabled(boolean enabled) {
        button.setEnabled(enabled);
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private void init() {
        
        button = menu.addItem("patientHistory", Icons.White.patient_16, new Command() {
            
            @Override
            public void menuSelected(MenuItem selectedItem) {
                
                Item item = getSelectedItem(); 
                
                log.info("item {}", item);
                
                if(item == null && patientId == null){
                    I18nManager.global(I18nGlobalNotifiers.no_entry_selected);
                    return;
                }
                if(patientId == null) {
                    patientId = MyUtils.getValueFromItem(item, patientField, Integer.class);
                    patientName = MyUtils.getValueFromItem(item, nameField, String.class);
                }else {
                    patientId = null;
                    patientName = null;
                }
                
                toggleCaption();
                
                if(toggleListener != null) {
                    toggleListener.onToggle(patientId);
                }
                
            }
        });
        toggleCaption();
    }
    
    private void toggleCaption() {
        button.setText(i18n.get(caption.patientHistory) + (patientId != null ? ": " + patientName : ""));
    }
    
    
    private Item getSelectedItem() {
        Item item = null;
        
        if(table.isMultiSelect()) {
            try {
                item = table.getItem(((Collection)table.getValue()).toArray()[0]);
            } catch (Exception e) {}
        }else {
            item = table.getItem(table.getValue());
        }
        return item;
    }
    // {end privatemethods}

    // {section database}
    // ******************
    // {end database}

    public abstract static class ToggleListener{
        public abstract void onToggle(Integer patientId); 
    }
    
    // {section layout}
    // ****************
    // {end layout}
}
