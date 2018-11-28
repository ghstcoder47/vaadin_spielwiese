package archenoah.lib.vaadin.CustomConverterFactorys;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

@SuppressWarnings("serial")
public class FloatConverter implements Converter<String, Object>{


    // {section fields}
    // ****************
    private int precision = 2;
    // {end fields}

    // {section constructors}
    // **********************
    public FloatConverter() {
        // TASK Auto-generated constructor stub
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    public void setPrecision(int precision) {
        this.precision = precision;
    }
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    @Override
    public Object convertToModel(String value, Class<? extends Object> targetType, Locale locale)
        throws com.vaadin.data.util.converter.Converter.ConversionException {
        return Float.parseFloat(value);
    }

    @Override
    public String convertToPresentation(Object value, Class<? extends String> targetType, Locale locale)
        throws com.vaadin.data.util.converter.Converter.ConversionException {
        return String.format("%."+precision+"f", value);
    }

    @Override
    public Class<Object> getModelType() {
        return Object.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
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
