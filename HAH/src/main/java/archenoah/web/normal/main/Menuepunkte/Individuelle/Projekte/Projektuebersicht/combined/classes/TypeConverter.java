package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.combined.classes;

import java.util.HashMap;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.vaadin.Language.i18n.I18nConverter;

import com.vaadin.data.Item;

public class TypeConverter {


    // {section fields}
    // ****************
    HashMap<String, I18nConverter> converters;
    // {end fields}

    // {section constructors}
    // **********************
    public TypeConverter() {
        converters = new HashMap<String, I18nConverter>();
        converters.put("REP", new I18nConverter("cust_protokoll_replagal_typ", "PRT_STRING"));
        converters.put("VPRIV", new I18nConverter("cust_protokoll_vpriv_typ", "PVT_STRING"));
        converters.put("POP", new I18nConverter("cust_protokoll_pop_typ", "STRING"));
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    public void convertType(Item item) {
        
        String result = null;
         
        I18nConverter converter = converters.get(MyUtils.getValueFromItem(item, "project_code", String.class));
        
        if(converter != null) {
            
            result = converter.getIdCaption(MyUtils.getValueFromItem(item, "inf_type", String.class));
        }
        
        item.getItemProperty("inf_type").setValue(result);
        
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
