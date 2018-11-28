package archenoah.lib.vaadin.Components.table;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.UI;

public class DataViewSorter extends DefaultItemSorter {

    // {section fields}
    // ****************
    HashMap<Object, Converter<String, Object>> converterMap;
    private Comparator<Object> propertyValueComparator;
    private Locale locale;
    
    // {end fields}

    // {section constructors}
    // **********************
    public DataViewSorter(CustomTable table) {
        this(table, new DefaultPropertyValueComparator());
    }
    
    public DataViewSorter(CustomTable table, Comparator<Object> propertyValueComparator) {
        this.propertyValueComparator = propertyValueComparator;
        init(table);
    }
    
    private void init(CustomTable table) {
        
        converterMap = new HashMap<Object, Converter<String,Object>>();
        locale = UI.getCurrent().getLocale();
        
        if(!(table.getContainerDataSource() instanceof IndexedContainer)) {
            return;
        }
        
        IndexedContainer con = (IndexedContainer) table.getContainerDataSource();
       
        for (Object spid : con.getSortableContainerPropertyIds()) {
            if(table.getConverter(spid) != null) {
                converterMap.put(spid, table.getConverter(spid));
            }
        }
        
        
        
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
    @Override
    protected int compareProperty(Object propertyId, boolean sortDirection,
        Item item1, Item item2) {
            
        // Get the properties to compare
        final Property<?> property1 = item1.getItemProperty(propertyId);
        final Property<?> property2 = item2.getItemProperty(propertyId);
    
        // Get the values to compare
        Object value1 = (property1 == null) ? null : property1.getValue();
        Object value2 = (property2 == null) ? null : property2.getValue();
    
        Converter<String, Object> converter = converterMap.get(propertyId);
        if(converter != null) {
            value1 = (value1 == null) ? null : converter.convertToPresentation(value1, String.class, locale);
            value2 = (value2 == null) ? null : converter.convertToPresentation(value2, String.class, locale);
        }
        
        // Result of the comparison
        int r = 0;
        if (sortDirection) {
            r = propertyValueComparator.compare(value1, value2);
        } else {
            r = propertyValueComparator.compare(value2, value1);
        }
    
        return r;
    }
    // {end privatemethods}

    // {section database}
    // ******************
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
