package archenoah.lib.vaadin.CustomConverterFactorys;

import java.util.HashMap;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

public class BooleanToTextConverter implements Converter<String, Boolean> {
    
    private final HashMap<Boolean, String> byModel = new HashMap<Boolean, String>();
    private final HashMap<String, Boolean> byPresentation = new HashMap<String, Boolean>();
    
    public BooleanToTextConverter() {
        fillMaps();
    }

    @Override
    public Boolean convertToModel(String value, Class<? extends Boolean> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        return byPresentation.get(value);
    }

    @Override
    public String convertToPresentation(Boolean value, Class<? extends String> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        return byModel.get(value);
    }

    @Override
    public Class<Boolean> getModelType() {
        return Boolean.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
    
    private void fillMaps(){
        fillEntry(true, "ja");
        fillEntry(false, "nein");
    }
    
    private void fillEntry(Boolean bool, String str){
        byModel.put(bool, str);
        byPresentation.put(str, bool);
    }
    
}
