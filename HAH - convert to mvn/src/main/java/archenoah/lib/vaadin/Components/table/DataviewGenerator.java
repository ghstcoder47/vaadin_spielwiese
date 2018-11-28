package archenoah.lib.vaadin.Components.table;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import org.tepi.filtertable.FilterGenerator;
import org.tepi.filtertable.FilterTable;

import archenoah.lib.vaadin.CustomConverterFactorys.DataviewConverter;

import com.vaadin.data.Container.Filter;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;

@Deprecated //merge this and TableFIlterConverter!
public class DataviewGenerator implements FilterGenerator{
    
    
    private HashMap<String, DataviewConverter> converterMap;

    /**
     * only works with LngConverters for now
     * @author Developer
     *
     */
    public DataviewGenerator(HashMap<String, DataviewConverter> converterMap) {
        this.converterMap = converterMap;
    }
    
    public DataviewGenerator() {
        this.converterMap = new HashMap<String, DataviewConverter>();
    }
    
    public void addConverter(String column, DataviewConverter converter) {
        converterMap.put(column, converter);
    }
    
    /**
     * set the filter generators AND sets column converters if possible
     * @param table
     */
    public void attach(FilterTable table) {
        
        if(table == null) {
            throw new IllegalArgumentException("table must be set");
        }
        
        table.setFilterOnDemand(false);
        table.setFilterGenerator(this);
        
        Collection<?> pids = table.getContainerPropertyIds();
        
        for (Entry<String, DataviewConverter> entry : converterMap.entrySet()) {
            if(pids.contains(entry.getKey())) {
                table.setConverter(entry.getKey(), entry.getValue());
            }
        }
    }
    
    @Override
    public Filter generateFilter(Object propertyId, Object value) {
        // TASK Auto-generated method stub
        return null;
    }

    @Override
    public Filter generateFilter(Object propertyId, Field<?> originatingField) {
        // TASK Auto-generated method stub
        return null;
    }
    
    @Override
    public AbstractField<?> getCustomFilterComponent(Object propertyId) {
        
        if(converterMap.containsKey(propertyId)) {
            
            ComboBox cb = new ComboBox();
            cb.setFilteringMode(FilteringMode.CONTAINS);
            converterMap.get(propertyId).attachDatasource(cb);
            return cb;
            
        }else {
            return null;
        }
    }

    @Override
    public void filterRemoved(Object propertyId) {
        // TASK Auto-generated method stub
        
    }

    @Override
    public void filterAdded(Object propertyId, Class<? extends Filter> filterType, Object value) {
        // TASK Auto-generated method stub
        
    }

    @Override
    public Filter filterGeneratorFailed(Exception reason, Object propertyId, Object value) {
        // TASK Auto-generated method stub
        return null;
    }

}
