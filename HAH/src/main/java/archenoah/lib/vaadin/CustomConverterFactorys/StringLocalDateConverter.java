package archenoah.lib.vaadin.CustomConverterFactorys;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.joda.time.LocalDate;

import com.vaadin.data.util.converter.Converter;

@SuppressWarnings("serial")
public class StringLocalDateConverter implements Converter<String, LocalDate> {

    private int format = DateFormat.SHORT;

    public void setFormat(int dateFormat) {
	format = dateFormat;
    };

    @Override
    public LocalDate convertToModel(String value, Class<? extends LocalDate> targetType, Locale locale)
	    throws com.vaadin.data.util.converter.Converter.ConversionException {
	DateFormat df = DateFormat.getDateInstance(format, locale);
	Date date = null;
	try {
	    date = df.parse(value);
	} catch (ParseException e) {
	    e.printStackTrace();
	    return new LocalDate();
	}
	return new LocalDate(date.toInstant());
    }

    @Override
    public String convertToPresentation(LocalDate value, Class<? extends String> targetType, Locale locale)
	    throws com.vaadin.data.util.converter.Converter.ConversionException {

	Date date = value.toDate();
	String presentation = DateFormat.getDateInstance(format, locale).format(date);
	return presentation;
    }

    @Override
    public Class<LocalDate> getModelType() {
	return LocalDate.class;
    }

    @Override
    public Class<String> getPresentationType() {
	return String.class;
    }

}