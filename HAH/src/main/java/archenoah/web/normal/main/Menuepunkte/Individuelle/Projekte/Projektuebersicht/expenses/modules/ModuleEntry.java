package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.expenses.modules;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.modules.converters.ModuleFieldConverter;

import com.vaadin.ui.AbstractField;

public class ModuleEntry extends ArrayList<ModuleField>{
    
    private String dbTable;
    private String dbParent;
    private String dbMultiKey;

    private LinkedHashMap<AbstractField<?>, ModuleField<?>> byComponentList = new LinkedHashMap<>();
    private LinkedHashMap<String, ModuleField<?>> byTargetList = new LinkedHashMap<>();
    
    public ModuleEntry(String table, String parent) {
        this.dbTable = table;
        this.dbParent = parent;
    }

    public String getTable() {
        return dbTable;
    }

    public String getParent() {
        return dbParent;
    }
    
    public boolean isMulti() {
        return dbMultiKey != null;
    }

    public String getMultiKey() {
        return dbMultiKey;
    }

    public LinkedHashMap<AbstractField<?>, ModuleField<?>> getByComponentList() {
        return byComponentList;
    }

    public LinkedHashMap<String, ModuleField<?>> getByTargetList() {
        return byTargetList;
    }

    public ModuleEntry withMultiKey(String column) {
        this.dbMultiKey = column;
        return this;
    }
    
    public <T> ModuleEntry withMultiField(AbstractField<T> field, String targetColumn, Object keyValue) {
        return withMultiField(field, targetColumn, keyValue, null);
    }
    
    public <T> ModuleEntry withMultiField(AbstractField<T> field, String targetColumn, Object keyValue, ModuleFieldConverter converter) {
        insert(new ModuleField<T>(field, targetColumn, keyValue, converter));
        return this;
    }
    
    public <T> ModuleEntry withField(AbstractField<T> field, String targetColumn) {
        return withField(field, targetColumn, null);
    }
    
    public <T> ModuleEntry withField(AbstractField<T> field, String targetColumn, ModuleFieldConverter converter) {
        insert(new ModuleField<T>(field, targetColumn, null, converter));
        return this;
    }
    
    public <T> boolean insert(ModuleField<T> e) {
        byComponentList.put(e.getField(), e);
        byTargetList.put(e.getTargetColumn(), e);
        return super.add(e);
    }
    
}
