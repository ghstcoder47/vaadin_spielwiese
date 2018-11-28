package archenoah.lib.custom;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;

import com.vaadin.data.Item;
import com.vaadin.ui.AbstractField;

public class DataSetComparator {


    // {section fields}
    // ****************
    private Item item;
    private HashMap<AbstractField<?>, Object> mapping;
    private HashMap<AbstractField<?>, CustomComparator> customComparators;
    // {end fields}

    // {section constructors}
    // **********************
    
    public DataSetComparator(Item item, HashMap<AbstractField<?>, Object> mapping) {
        this.item = item;
        this.mapping = mapping;
    }
    public DataSetComparator(Item item, HashMap<AbstractField<?>, Object> mapping, HashMap<AbstractField<?>, CustomComparator> customComparators) {
        this(item, mapping);
        this.customComparators = customComparators;
    }
    
    public static DataSetComparator dbMapHelper(Item item, Map<Object, String> dbMap) {
        return new DataSetComparator(item, mapDbMap(dbMap));
    }
    public static DataSetComparator dbMapHelper(Item item, Map<Object, String> dbMap, HashMap<AbstractField<?>, CustomComparator> customComparators) {
        return new DataSetComparator(item, mapDbMap(dbMap), customComparators);
    }
    
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    public boolean hasChanges() {
        return getChanges().size() > 0;
    }
    
    /**
     * Map of changed fields, empty if no changes are detected
     * @return map of the changed field, with Pairs of <oldValue, newValue> as values
     */
    public HashMap<AbstractField<?>, Pair<Object, Object>> getChanges() {
        return generateChangeMap();
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    
    @SuppressWarnings("unchecked")
    private HashMap<AbstractField<?>, Pair<Object, Object>> generateChangeMap() {
        HashMap<AbstractField<?>, Pair<Object, Object>> changes = new HashMap<AbstractField<?>, Pair<Object, Object>>();
        
        for (Entry<AbstractField<?>, Object> entry : mapping.entrySet()) {
            AbstractField<?> field = entry.getKey();
            Object propertyId = entry.getValue();
            
            boolean hasChanges = false;
            
            if(customComparators != null
                && customComparators.containsKey(field)
                && !customComparators.get(field).isEqual(item, field)) {
                
                Pair<Object, Object> values = customComparators.get(field).getValues(item, field);
                if(values != null) {
                    changes.put(field, values);
                }else {
                    hasChanges = true;
                }
            }else {
                hasChanges = !MyUtils.equalsWithNulls(MyUtils.getValueFromItem(item, propertyId, Object.class), field.getValue());
            }
            
            
            if(hasChanges) {
                changes.put(field, (Pair<Object, Object>) Pair.of(MyUtils.getValueFromItem(item, propertyId, Object.class), field.getValue()));
            }
        }
        
        return changes;
    }
    
    @SuppressWarnings("rawtypes")
    private static HashMap<AbstractField<?>, Object> mapDbMap(Map<Object, String> dbMap) {
        HashMap<AbstractField<?>, Object> mapping = new HashMap<AbstractField<?>, Object>();
        
        for (Entry<Object, String> entry : dbMap.entrySet()) {
            
            if(entry.getKey() instanceof AbstractField) {
                mapping.put((AbstractField) entry.getKey(), entry.getValue().split(":")[1]);
            }
            
        }
        
        return mapping;
    }
    // {end privatemethods}

    // {section classes}
    // ******************
    public abstract class CustomComparator{
        public abstract boolean isEqual(Item item, AbstractField<?> field);
        public abstract Pair<Object, Object> getValues(Item item, AbstractField<?> field);
    }
    // {end database}

}
