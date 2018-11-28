package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.expenses.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map.Entry;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;
import archenoah.lib.vaadin.Language.i18n.I18nGlobalNotifiers;
import archenoah.lib.vaadin.Language.i18n.I18nManager;

import com.google.common.base.Joiner;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.shared.Position;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification.Type;

public class ModuleComponent extends CustomComponent{


    // {section fields}
    // ****************
    
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ModuleComponent.class);
    
    private Integer parentId;
    private Integer projectId;
    private Integer patientId;
    private boolean readOnly = true;
    
    private ArrayList<SaveListener> saveListeners = new ArrayList<ModuleComponent.SaveListener>();
    private ArrayList<FieldValueChangeListener> fieldListeners = new ArrayList<ModuleComponent.FieldValueChangeListener>();
    
    public enum FieldValue{
        PARENT,
        PROJECT,
        PATIENT,
        READONLY
    }
    
    private LinkedHashMap<ModuleEntry, IndexedContainer> itemList = new LinkedHashMap<>(); 
    
    private HashMap<ParentValue, AbstractField<?>> parentValueMap = new HashMap<>();
    private HashMap<ParentValue, ArrayList<ValueChangeListener>> parentListenerQueue = new HashMap<>();
    public enum ParentValue{
        NURSE,
        DATE
    }
    
    // {end fields}

    // {section constructors}
    // **********************
    public ModuleComponent() {
        super();
    }

    public ModuleComponent(Component content) {
        super(content);
    }

    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
        onFieldValueChange(FieldValue.PARENT, parentId, Integer.class);
    }
    
    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
        onFieldValueChange(FieldValue.PROJECT, projectId, Integer.class);
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
        onFieldValueChange(FieldValue.PATIENT, patientId, Integer.class);
    }
    
    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        onFieldValueChange(FieldValue.READONLY, readOnly, Boolean.class);
    }
    
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    public void registerEntry(ModuleEntry entry) {
        
        if(!itemList.containsKey(entry)) {
            itemList.put(entry, null);
        }
    }
    
    public boolean load(Integer parentId) {
        setParentId(parentId);
        return load();
    }
    
    public boolean load() {
        
        if(parentId == null) {
            throw new IllegalStateException("parentId is required");
        }
        
        return dbLoad();
    }
    
    public boolean save(Integer parentId) {
        setParentId(parentId);
        return save();
    }
    
    public boolean save() {
        
        if(parentId == null) {
            throw new IllegalStateException("parentId is required");
        }
        
        boolean saved = dbSave();
        
        if(saved) {
            for (SaveListener sl : saveListeners) {
                sl.onSave();
            }
        }
        
        return saved;
    }
    
    public boolean validate() {
        return isValid();
    }
    
    public void registerParentValue(ParentValue parentValue, final AbstractField<?> field) {
        parentValueMap.put(parentValue, field);
        if(parentListenerQueue.get(parentValue) != null) {
            for (ValueChangeListener vcl : parentListenerQueue.get(parentValue)) {
                field.addValueChangeListener(vcl);
                vcl.valueChange(new ValueChangeEvent() {
                    
                    @Override
                    public Property getProperty() {
                        return field.getPropertyDataSource();
                    }
                });
            }
            parentListenerQueue.remove(parentValue);
        }
    }

    public <T> T getParentValue(ParentValue parentValue, Class<T> type) {
        return type.cast(parentValueMap.get(parentValue).getValue()); 
    }
    
    public void addFieldChangeListener(FieldValueChangeListener fieldValueChangeListener) {
        fieldListeners.add(fieldValueChangeListener);
    }
    
    public void addSaveListener(SaveListener saveListener) {
        saveListeners.add(saveListener);
    }
    
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    
    protected void addValueChangeListener(ParentValue parentValue, ValueChangeListener vcl) {
        if(parentValueMap.containsKey(parentValue)) {
            parentValueMap.get(parentValue).addValueChangeListener(vcl);
        }else {
            if(parentListenerQueue.get(parentValue) == null) {
                parentListenerQueue.put(parentValue, new ArrayList<ValueChangeListener>());
            }
            parentListenerQueue.get(parentValue).add(vcl);
        }
    }
    
    private void onFieldValueChange(FieldValue fieldValue, Object value, Class<?> type) {
        for (FieldValueChangeListener fvc : fieldListeners) {
            fvc.onFieldValueChange(fieldValue, value, type);
        }
    }
    
    private boolean isValid() {
        
        ArrayList<String> invalid = new ArrayList<String>();
        
        for (ModuleEntry module : itemList.keySet()) {
            
            for (AbstractField<?> field : module.getByComponentList().keySet()) {
                if(!field.isValid()) {
                    invalid.add(field.getCaption());
                }
            }
        }
        
        if(invalid.size() > 0) {
            
            I18nManager.triggerNotifier("", Joiner.on(", ").join(invalid), Type.WARNING_MESSAGE, Position.MIDDLE_CENTER, 5000);
            I18nManager.global(I18nGlobalNotifiers.fields_empty);
            
            return false;
        }
        
        return true;
    }
    
    private ArrayList<String> wrapTargets(ModuleEntry module){
        
        ArrayList<String> keys = new ArrayList<String>(module.getByTargetList().keySet());
        keys.add(0, module.getParent());
        if(module.isMulti()) {
            keys.add(1, module.getMultiKey());
        }
        for (final ListIterator<String> i = keys.listIterator(); i.hasNext();) {
            i.set("`" + i.next() + "`");
        }
        return keys;
        
    }
    
    @SuppressWarnings("unchecked")
    private ArrayList<String> wrapValues(ModuleEntry module){
        ArrayList<String> values = new ArrayList<String>();
        
        if(module.isMulti()) {

            for (ModuleField field : module) {
                
                ArrayList<String> m = new ArrayList<String>();
                m.add(0, parentId.toString());
                m.add(1, MyUtils.formatSqlValue(field.getKeyValue()));
                
                if(field.hasConverter()) {
                    m.add(MyUtils.formatSqlValue(
                        field.getConverter().convertForDatabase(field.getField().getValue())));
                }else {
                    m.add(MyUtils.formatSqlValue(field.getField().getValue()));
                }
                
                values.add("(" + Joiner.on(", ").join(m) + ")");
            }
            
        }else {
            values.add(0, parentId.toString());
            for (ModuleField field : module) {
                if(field.hasConverter()) {
                    values.add(MyUtils.formatSqlValue(
                        field.getConverter().convertForDatabase(field.getField().getValue())));
                }else {
                    values.add(MyUtils.formatSqlValue(field.getField().getValue()));
                }
                
            }
        }
        
        return values;
    }
    
    private ArrayList<String> wrapUpdate(ModuleEntry module){
        ArrayList<String> keys = new ArrayList<String>(module.getByTargetList().keySet());
        for (final ListIterator<String> i = keys.listIterator(); i.hasNext();) {
            String key = i.next();
            i.set("`" + key + "` = VALUES(`" + key + "`)");
        }
        return keys;
    }
    
    // {end privatemethods}

    // {section database}
    // ******************
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean dbLoad() {
        
        for (ModuleEntry module : itemList.keySet()) {
            
            StringBuilder sql = new StringBuilder();
            
            sql.append("SELECT ");
            if(module.isMulti()) {
                sql.append("`");
                sql.append(module.getMultiKey());
                sql.append("`, ");
            }
            sql.append(Joiner.on(", ").join(wrapTargets(module)));
            sql.append(" FROM `");
            sql.append(module.getTable());
            sql.append("` WHERE `");
            sql.append(module.getParent());
            sql.append("`= ");
            sql.append(parentId);
            sql.append(";");
            
            CustomQuery q = new DBClass().CustomQuery;
            q.setSqlString(sql.toString());
//            q.db.debugNextQuery(true);
            itemList.put(module, (IndexedContainer) q.query());
            
        }
        
        for (Entry<ModuleEntry, IndexedContainer> entry : itemList.entrySet()) {
            
            ModuleEntry module = entry.getKey();
            IndexedContainer con = entry.getValue();

            for (ModuleField field : module) {
                
                con.removeAllContainerFilters();
                if(module.isMulti()) {
                    if(field.getKeyValue() == null) {
                        continue;
                    }
                    con.addContainerFilter(new SimpleStringFilter(module.getMultiKey(),
                        field.getKeyValue().toString(), true, false));
                }
                
                field.setValue(MyUtils.getValueFromItem(MyUtils.getFirstItemFromContainer(con),
                    field.getTargetColumn(), field.getField().getType()));
            }
            
        }
        
        return true;
    }
    
    
    private boolean dbSave() {
        
        if(!readOnly) {
            return false;
        }
        
        if(!isValid()) {
            return false;
        }
        
        for (Entry<ModuleEntry, IndexedContainer> entry : itemList.entrySet()) {
            
            ModuleEntry module = entry.getKey();
            
            StringBuilder sql = new StringBuilder();
            
            sql.append("INSERT INTO `");
            sql.append(module.getTable());
            sql.append("` (");
            sql.append(Joiner.on(", ").join(wrapTargets(module)));
            sql.append(") VALUES\n");
            sql.append(module.isMulti() ? "" : "(");
            sql.append(Joiner.on(", ").join(wrapValues(module)));
            sql.append(module.isMulti() ? "" : ")");
            sql.append("\n ON DUPLICATE KEY UPDATE \n");
            sql.append(Joiner.on(",\n").join(wrapUpdate(module)));
            sql.append(";");
            
            
            CustomQuery q = new DBClass().CustomQuery;
            q.setSqlString(sql.toString());
            q.update();
        }
        
        return true;
    }
    // {end database}

    // {section classes}
    // ****************
    public abstract static class FieldValueChangeListener {
        public abstract void onFieldValueChange(FieldValue fieldValue, Object value, Class<?> type);
    }
    public abstract static class SaveListener {
        public abstract void onSave();
    }
    // {end layout}
}
