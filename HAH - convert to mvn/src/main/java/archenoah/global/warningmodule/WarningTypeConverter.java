package archenoah.global.warningmodule;

import java.util.Locale;

import archenoah.lib.vaadin.CustomConverterFactorys.TableFilterConverter;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.converter.Converter;

public class WarningTypeConverter implements Converter<String, WarningType>, TableFilterConverter{
    
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WarningTypeConverter.class);
    
    public WarningTypeConverter() {
        // TASK Auto-generated constructor stub
    }

    @Override
    public WarningType convertToModel(String value, Class<? extends WarningType> targetType, Locale locale)
        throws com.vaadin.data.util.converter.Converter.ConversionException {
        // TASK Auto-generated method stub
        return null;
    }

    @Override
    public String convertToPresentation(WarningType value, Class<? extends String> targetType, Locale locale)
        throws com.vaadin.data.util.converter.Converter.ConversionException {
        return WarningList.i18n.get(value);
    }

    @Override
    public Class<WarningType> getModelType() {
        return WarningType.class;
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

        for (WarningType type : WarningType.protocols.values()) {
            con.addItem(type).getItemProperty(caption).setValue(WarningList.i18n.get(type));
        }  
        
        for (WarningType type : WarningType.shipping.values()) {
            con.addItem(type).getItemProperty(caption).setValue(WarningList.i18n.get(type));
        }
        
        for (WarningType type : WarningType.tasks.values()) {
            con.addItem(type).getItemProperty(caption).setValue(WarningList.i18n.get(type));
        }
        
        return con;
    }

    @Override
    public Object getItemCaptionProperty() {
        return caption;
    }

}
