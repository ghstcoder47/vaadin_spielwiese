package archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Patientenverwaltung.Form.Filter;

import java.io.Serializable;
import java.util.Locale;

import org.tepi.filtertable.FilterDecorator;
import org.tepi.filtertable.numberfilter.NumberFilterPopupConfig;

import com.vaadin.server.Resource;
import com.vaadin.shared.ui.datefield.Resolution;

public class Decorator_Arzt_Zuweisung_Patient implements FilterDecorator, Serializable {

	@Override
	public String getEnumFilterDisplayName(Object propertyId, Object value) {
		// TODO Automatisch generierter Methodenstub

		 if ("Art".equals(propertyId)) {
			 String Art = (String) value;
			 switch (Art) {
			 case "Hausarzt":
	
			return "Hausarz vt";
			 case "B arzt":
			return "Hausarzaa vt";
			
			}   
			
			 }   
		 // returning null will output default value        return null;
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
		return null;
	}

	@Override
	public String getToCaption() {
		// TODO Automatisch generierter Methodenstub
		return null;
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
		return null;
	}

	@Override
	public String getDateFormatPattern(Object propertyId) {
		// TODO Automatisch generierter Methodenstub
		return null;
	}

	@Override
	public Locale getLocale() {
		// TODO Automatisch generierter Methodenstub
		return null;
	}

	@Override
	public String getAllItemsVisibleString() {
		// TODO Automatisch generierter Methodenstub
		return "Alle Anzeigen";
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
