package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.forsteo.Filter;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Locale;

import org.tepi.filtertable.FilterDecorator;
import org.tepi.filtertable.numberfilter.NumberFilterPopupConfig;

import com.vaadin.server.Resource;
import com.vaadin.shared.ui.datefield.Resolution;

public class Decorator_Forsteo implements FilterDecorator, Serializable {

	@Override
	public String getEnumFilterDisplayName(Object propertyId, Object value) {
		// TODO Automatisch generierter Methodenstub
		return null;
	}

	@Override
	public Resource getEnumFilterIcon(Object propertyId, Object value) {
		// TODO Automatisch generierter Methodenstub
		return null;
	}

	@Override
	public String getBooleanFilterDisplayName(Object propertyId, boolean value) {
		// TODO Automatisch generierter Methodenstub
		return null;
	}

	@Override
	public Resource getBooleanFilterIcon(Object propertyId, boolean value) {
		// TODO Automatisch generierter Methodenstub
		return null;
	}

	@Override
	public boolean isTextFilterImmediate(Object propertyId) {
		// TODO Automatisch generierter Methodenstub
		return true;
	}

	@Override
	public int getTextChangeTimeout(Object propertyId) {
		// TODO Automatisch generierter Methodenstub
		  return 500;
	}

	@Override
	public String getFromCaption() {
		// TODO Automatisch generierter Methodenstub
		 return "Start Datum:";

	}

	@Override
	public String getToCaption() {
		// TODO Automatisch generierter Methodenstub
		return "End Datum:";

	}

	@Override
	public String getSetCaption() {
		// TODO Automatisch generierter Methodenstub
		return null;
	}

	@Override
	public String getClearCaption() {
		// TODO Automatisch generierter Methodenstub
		return null;
	}

	@Override
	public Resolution getDateFieldResolution(Object propertyId) {
		// TODO Automatisch generierter Methodenstub
		 return Resolution.DAY;
	}

	@Override
	public String getDateFormatPattern(Object propertyId) {
		// TODO Automatisch generierter Methodenstub
		return null;
	}

    public DateFormat getDateFormat(Object propertyId) {
        return DateFormat.getDateInstance(DateFormat.SHORT, new Locale("de",
                "DE"));
    }
    
	@Override
	public Locale getLocale() {
		// TODO Automatisch generierter Methodenstub
		return null;
	}

	@Override
	public String getAllItemsVisibleString() {
		// TODO Automatisch generierter Methodenstub
		 return "Zeige alle";
	}

	@Override
	public NumberFilterPopupConfig getNumberFilterPopupConfig() {
		// TODO Automatisch generierter Methodenstub
		return null;
	}

	@Override
	public boolean usePopupForNumericProperty(Object propertyId) {
		// TODO Automatisch generierter Methodenstub
		return false;
	}

}
