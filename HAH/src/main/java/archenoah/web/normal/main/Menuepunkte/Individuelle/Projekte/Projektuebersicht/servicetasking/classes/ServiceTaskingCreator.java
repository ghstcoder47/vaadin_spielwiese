package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.classes;

import java.util.ArrayList;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;
import archenoah.lib.vaadin.Language.i18n.I18nCB;
import archenoah.lib.vaadin.Language.i18n.I18nManager;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektzuweisung.Form.Metadata.ServiceTaskingComponents.RecurrenceManager;

import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBox;
import de.steinwedel.messagebox.MessageBoxListener;

@Deprecated
public class ServiceTaskingCreator{
    
    // {section fields}
    // ****************
    private RecurrenceManager manager;
    private MessageBox messageBox;
    private SaveListener saveListener;
    private I18nManager i18n;
    
    // {end fields}
    
    private final static class caption extends I18nCB {
        static final I18nCB title = set();
    }
    
    // {section constructors}
    // **********************
    public ServiceTaskingCreator() {
        manager = new RecurrenceManager();
        i18n = new I18nManager(this);
        
        configureComponents();
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    public void show(Item item) {
        showPopup(item);
    }
    
    public void addSaveListener(SaveListener saveListener) {
        this.saveListener = saveListener;;
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private void configureComponents() {
        manager.setEnabled(false);
        manager.addValueChangeListener(new ValueChangeListener() {
            
            @Override
            public void valueChange(ValueChangeEvent event) {
                messageBox.getButton(ButtonId.OK).setEnabled(manager.getItem() != null);
            }
        });
        manager.setWidth("280px");
        manager.setHeight("200px");
    }
    
    
    private void showPopup(final Item item) {
        
        if(item == null) {
            return;
        }
        
        manager.setParentId(MyUtils.getValueFromItem(item, "VH_ID", Integer.class));
        manager.setCaption(MyUtils.getValueFromItem(item, "patient", String.class));
        
        MessageBoxListener mbl = new MessageBoxListener() {

            @Override
            public void buttonClicked(ButtonId buttonId) {

                Integer tid = null;
                Boolean close = false;
                
                if (buttonId.equals(ButtonId.OK)) {
                    
                    tid = create(item, manager.getItem());
                    
                    close = (tid != null);
                    
                }else {
                    close = true;
                }
                
                if(close) {
                    messageBox.close();
                    if(saveListener != null && tid != null) {
                        saveListener.onSave(tid);
                    }
                }
                
                
                
            }
        };

        messageBox = MessageBox.showCustomized(Icon.NONE, i18n.get(caption.title), manager, mbl, ButtonId.CANCEL, ButtonId.OK);
        messageBox.getButton(ButtonId.OK).setEnabled(false);
        messageBox.setAutoClose(false);
        
    }
    
    private Integer create(Item rowItem, Item typeItem) {
        
        if(rowItem == null || typeItem == null) {
            return null;
        }
        
        return dbCreate(MyUtils.getValueFromItem(typeItem, "STMD_ID", Integer.class),
            MyUtils.getValueFromItem(rowItem, "VH_ID", Integer.class),
            MyUtils.getValueFromItem(typeItem, "STMD_TYPE", String.class));
    }
    
    // {end privatemethods}

    // {section database}
    // ******************
    private Integer dbCreate(Integer delId, Integer vhId, String type) {
        
        if(delId == null) {
            return null;
        }
        
        type = type != null ? "'" + type + "'" : "NULL";
        
        ArrayList<Integer> generatedKeys = new ArrayList<Integer>();
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "INSERT INTO `cust_protokoll_servicetasking` (ST_ID_DELEGATION, ST_ID_NURSE, ST_TYPE) VALUES"
             + "\n " + "('"+delId+"'"
             + "\n " + ",(select VH_NURSE_ID"
             + "\n " + "    from cust_verk_haupt"
             + "\n " + "    where VH_ID = "+vhId+")"
             + "\n " + ", "+type+");";
        q.setSqlString(sql);
//        q.db.debugNextQuery(true);
        
        q.update(false, generatedKeys);
        
        return generatedKeys.iterator().next();
    }
    // {end database}
    
    public static abstract class SaveListener{
        public abstract void onSave(Integer newId);
    }
}
