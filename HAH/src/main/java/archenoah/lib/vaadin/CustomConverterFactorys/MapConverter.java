package archenoah.lib.vaadin.CustomConverterFactorys;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import archenoah.lib.custom.MyUtils;

import com.vaadin.data.Container;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.converter.Converter;

public class MapConverter<T> implements Converter<String, T>, TableFilterConverter{

    private static final long serialVersionUID = -5908060398127222926L;
    
    private Class<T> type;
    private Map<T, String> map;
    
    public MapConverter(Map<T, String>map, Class<T> type){
        this.map = map;
        this.type = type;
    }
    
    /**
     * helper constructor, container valueProperty must be string!
     * @param con
     * @param indexProperty
     * @param valueProperty
     * @param type
     */
    public MapConverter(Container con, Object indexProperty, Object valueProperty, Class<T> type) {
        this.map = new HashMap<T, String>();
        this.type = type;
        
        for (Object iid : con.getItemIds()) {
            map.put(MyUtils.getValueFromItem(con.getItem(iid), indexProperty, type),
                MyUtils.getValueFromItem(con.getItem(iid), valueProperty, String.class));
        }
    }
    
    @Override
    public T convertToModel(String value, Class<? extends T> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
        return null;
    }

    @Override
    public String convertToPresentation(T value, Class<? extends String> targetType, Locale locale)
        throws com.vaadin.data.util.converter.Converter.ConversionException {
        return map.get(value);
    }

    @Override
    public Class<T> getModelType() {
        return type;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IndexedContainer getContainer() {

        IndexedContainer con = new IndexedContainer();
        con.addContainerProperty(caption, String.class, "");
        
        for (Entry<T, String> entry : map.entrySet()) {
            con.addItem(entry.getKey()).getItemProperty(caption).setValue(entry.getValue());
        }
        
        return con;
    }

    @Override
    public Object getItemCaptionProperty() {
        return caption;
    }
    


}
