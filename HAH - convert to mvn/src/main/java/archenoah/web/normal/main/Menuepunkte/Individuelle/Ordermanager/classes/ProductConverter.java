package archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.classes;

import java.util.HashMap;
import java.util.Locale;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.converter.Converter;

public class ProductConverter implements Converter<String, Integer> {


    // {section fields}
    // ****************
    HashMap<Integer, String> productMap;
    // {end fields}

    // {section constructors}
    // **********************
    public ProductConverter() {
        setupMaps();
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
        return productMap.get(value);
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
    private void setupMaps() {
        
        productMap = new HashMap<Integer, String>();
        
        Container con = dbGetProducts();
        for (Object iid : con.getItemIds()) {
            Item item = con.getItem(iid);
            productMap.put(MyUtils.getValueFromItem(item, "PP_ID", Integer.class),
                    MyUtils.getValueFromItem(item, "PP_PRODUCT", String.class));
        }
        
    }
    // {end privatemethods}

    // {section database}
    // ******************
    private Container dbGetProducts() {
        DBClass db = new DBClass();

        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("*");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cust_prescriptions_products");
        return db.DB_Data_Get.DB_SEND_AND_GET_Container();
        
    }
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
