package archenoah.lib.vaadin.Components.table;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.tepi.filtertable.FilterTable;

import archenoah.lib.custom.MyUtils;

import com.google.common.collect.Iterables;
import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Container.ItemSetChangeListener;

public class FilteringTableUpdater {
    

    private FilterTable table = null;
    private Container container = null;
    private Boolean firstUpdate = true;
    
    public FilteringTableUpdater(FilterTable table, Container container) {
        this.table = table;
        this.container = container;
        config();
    }
    
    public FilteringTableUpdater(FilterTable table) {
        this.table = table;
        this.container = this.table.getContainerDataSource();
        config();
    }
    
    public void updateTable(){
        if(table != null && container != null){
            update();
        }
    }
    
    public void updateTable(Container container){
        this.container  = container;
        if(table != null && container != null){
            update();
        }
    }
    
    public void showSum() {
        showSum(null);
    }
    
    public void showSum(final Object column) {
        
        table.addItemSetChangeListener(new ItemSetChangeListener() {
            
            @Override
            public void containerItemSetChange(ItemSetChangeEvent event) {
                
                Object target = (column != null) ? column : Iterables.getLast(Arrays.asList(table.getVisibleColumns()));
                table.setColumnFooter(target, "Σ " + table.size());
            }
        });
        
    }
    
    private void config() {
        table.addItemSetChangeListener(new ItemSetChangeListener() {
            @Override
            public void containerItemSetChange(ItemSetChangeEvent event) {
                table.setValue(null);
            }
        });
    }
    
    private void update(){
        Object first = table.getCurrentPageFirstItemId();
        Object value = table.getValue();
        Object sort = table.getSortContainerPropertyId();
        Boolean asc = table.isSortAscending();
        Boolean immediate = table.isImmediate();
        Boolean collapsingAllowed = table.isColumnCollapsingAllowed();
        
        HashMap<Object, Object> filters = new HashMap<Object, Object>();
        
        for(Object pid : table.getContainerPropertyIds()){
            filters.put(pid, table.getFilterFieldValue(pid));
        }
        
        Object[] visible = table.getVisibleColumns();
        HashMap<Object, Boolean> columnsCollapsed = new HashMap<Object, Boolean>();
        
        for (Object col : visible) {
			columnsCollapsed.put(col, table.isColumnCollapsed(col));
		}
        
     // conflicts with setFilterOnDemand(false)?
//        table.setImmediate(false);
//        table.setRefreshingEnabled(false);
        table.setContainerDataSource(container);
        
        /*****/
        
        table.setSortContainerPropertyId(sort);
        table.setSortAscending(asc);
        table.sort();
        
        table.setValue(value);
        
        if(firstUpdate && container.getItemIds().iterator().hasNext() && !MyUtils.equalsWithNulls(first, container.getItemIds().iterator().next())){
            first = container.getItemIds().iterator().next();
            firstUpdate = false;
        }
        table.setCurrentPageFirstItemId(first);

        
        for(Entry<Object, Object> entry : filters.entrySet()){
            table.setFilterFieldValue(entry.getKey(), entry.getValue());
        }
        
        if(collapsingAllowed){
	        for(Entry<Object, Boolean> entry : columnsCollapsed.entrySet()){
	            table.setColumnCollapsed(entry.getKey(), entry.getValue());
	        }
        }
        table.setRefreshingEnabled(true);
        table.setImmediate(immediate);
    }
    
}
