package archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.PackageCreator.classes;

import java.util.HashMap;
import java.util.Locale;

import archenoah.lib.custom.MyUtils;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.converter.Converter;

public class PackagerConverter implements Converter<String, Integer> {


    // {section fields}
    // ****************
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PackagerConverter.class);
    
    public enum TYPE{
        
        NAME,
        SIZE;
    }
    
    private HashMap<Integer, String> productMap;
    // {end fields}

    // {section constructors}
    // **********************
    public PackagerConverter(Container container, TYPE type) {
        setupMaps(container, type);
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    @Override
    public Integer convertToModel(String value, Class<? extends Integer> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        return null;
    }

    @Override
    public String convertToPresentation(Integer value, Class<? extends String> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        String pres = productMap.get(value);
        return pres != null ? pres : "?";
    }

    @Override
    public Class<Integer> getModelType() {
        return Integer.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private void setupMaps(Container con, TYPE type) {
        
        productMap = new HashMap<Integer, String>();
        
        switch (type) {
        case NAME:
            setupNameMaps(con);
            break;
        case SIZE:
            setupSizeMaps(con);
            break;
        default:
            break;
        }

    }
    
    private void setupNameMaps(Container con) {
        for (Object iid : con.getItemIds()) {
            Item item = con.getItem(iid);
            productMap.put(MyUtils.getValueFromItem(item, "id", Integer.class),
                    MyUtils.getValueFromItem(item, "name", String.class));
        }
    }
    
    private void setupSizeMaps(Container con) {
        for (Object iid : con.getItemIds()) {
            Item item = con.getItem(iid);
            
            Integer size = MyUtils.getValueFromItem(item, "size", Integer.class);
            
            productMap.put(MyUtils.getValueFromItem(item, "id", Integer.class),
                    (size != null ? size.toString() : "?"));
        }
    }
    // {end privatemethods}

    // {section database}
    // ******************
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
