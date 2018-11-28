package archenoah.lib.vaadin.Components.table.RowGenerators;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import archenoah.lib.custom.MyUtils;

import com.vaadin.data.Item;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.CustomTable.ColumnGenerator;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class HtmlLabelColumnGenerator implements ColumnGenerator{


    private Map<Object, Object> mapping;
    
    /**
     * 
     * @param mapping <columnId, dataColumnId>
     */
    public HtmlLabelColumnGenerator(Map<Object, Object> mapping) {
        setupMap(mapping);
    }
    
    public HtmlLabelColumnGenerator(Object columnId, Object dataColumnId) {
        LinkedHashMap<Object, Object> mapping = new LinkedHashMap<Object, Object>();
        mapping.put(columnId, dataColumnId);
        setupMap(mapping);
    }
    
    private void setupMap(Map<Object, Object> mapping) {
        this.mapping = Collections.unmodifiableMap(new LinkedHashMap<Object, Object>(mapping));
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Object generateCell(CustomTable source, Object itemId, Object columnId) {
        
        if(mapping.get(columnId) == null) {
            return null;
        }
        
        Item item = source.getContainerDataSource().getItem(itemId);
        Label label = new Label(MyUtils.getValueFromItem(item, mapping.get(columnId), String.class));
        label.setContentMode(ContentMode.HTML);
        return label;
    }

}
