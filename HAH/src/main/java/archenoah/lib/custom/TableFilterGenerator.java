package archenoah.lib.custom;

import org.tepi.filtertable.FilterGenerator;
import org.tepi.filtertable.FilterTable;

import archenoah.lib.vaadin.CustomConverterFactorys.TableFilterConverter;
import archenoah.lib.vaadin.Language.i18n.I18nConverter;

import com.vaadin.data.Container.Filter;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;

public class TableFilterGenerator implements FilterGenerator{


    // {section fields}
    // ****************
    private FilterTable table;
    
    // {end fields}

    

    // {section constructors}
    // **********************
    public TableFilterGenerator(FilterTable table) {
        this.table = table;
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    @Override
    public Filter generateFilter(Object propertyId, Object value) {
        return null;
    }

    @Override
    public Filter generateFilter(Object propertyId, Field<?> originatingField) {
        return null;
    }

    @Override
    public AbstractField<?> getCustomFilterComponent(Object propertyId) {
        
        if(table.getConverter(propertyId) != null) {
            
            // localized dropdown for LngConverter columns
            if(table.getConverter(propertyId) instanceof I18nConverter) {
                I18nConverter lng = (I18nConverter) table.getConverter(propertyId);
                ComboBox combo = new ComboBox();
                combo.setImmediate(true);
                combo.setFilteringMode(FilteringMode.CONTAINS);
                lng.attachDatasource(combo);
                return combo;
            }
            
            // dropdown for custom converters
            if(table.getConverter(propertyId) instanceof TableFilterConverter) {
                TableFilterConverter con = (TableFilterConverter) table.getConverter(propertyId);
                ComboBox combo = new ComboBox();
                combo.setImmediate(true);
                combo.setFilteringMode(FilteringMode.CONTAINS);
                combo.setContainerDataSource(con.getContainer());
                combo.setItemCaptionPropertyId(con.getItemCaptionProperty());
                return combo;
            }

        }
        
        return null;
    }

    @Override
    public void filterRemoved(Object propertyId) {
        
    }

    @Override
    public void filterAdded(Object propertyId, Class<? extends Filter> filterType, Object value) {
        
    }

    @Override
    public Filter filterGeneratorFailed(Exception reason, Object propertyId, Object value) {
        return null;
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    // {end privatemethods}

    // {section database}
    // ******************
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
