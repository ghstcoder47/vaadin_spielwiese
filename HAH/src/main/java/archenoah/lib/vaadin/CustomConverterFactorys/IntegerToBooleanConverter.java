package archenoah.lib.vaadin.CustomConverterFactorys;

import java.util.Locale;

import archenoah.lib.vaadin.Language.i18n.I18nCB;
import archenoah.lib.vaadin.Language.i18n.I18nManager;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.AbstractSelect;

public class IntegerToBooleanConverter implements DataviewConverter<Integer>{

    private static String cpt = "caption";
    
    private I18nManager i18n;
    private final static class caption extends I18nCB {
        static final I18nCB bool_true = set();
        static final I18nCB bool_false = set();
    }

    public IntegerToBooleanConverter() {
        i18n = new I18nManager(this);
    }

    @Override
    public Integer convertToModel(String value, Class<? extends Integer> targetType, Locale locale)
        throws com.vaadin.data.util.converter.Converter.ConversionException {
        
        if(value == null) {
            return null;
        }
        
        if(i18n.get(caption.bool_true).equals(value)) {
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public String convertToPresentation(Integer value, Class<? extends String> targetType, Locale locale)
        throws com.vaadin.data.util.converter.Converter.ConversionException {
        if(value == null || value > 1 || value < 0) {
            return null;
        }
        
        return value == 1 ? i18n.get(caption.bool_true) : i18n.get(caption.bool_false);
    }

    @Override
    public Class<Integer> getModelType() {
        return Integer.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void attachDatasource(AbstractSelect select) {
        
        IndexedContainer con = new IndexedContainer();

        con.addContainerProperty(cpt, String.class, "");
        
        con.addItem(0);
        con.addItem(1);
        
        con.getContainerProperty(1, cpt).setValue(i18n.get(caption.bool_true));
        con.getContainerProperty(0, cpt).setValue(i18n.get(caption.bool_false));
        
        select.setContainerDataSource(con);
        select.setItemCaptionPropertyId(cpt);
        
    }

}
