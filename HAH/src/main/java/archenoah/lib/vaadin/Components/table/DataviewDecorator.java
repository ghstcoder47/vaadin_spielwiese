package archenoah.lib.vaadin.Components.table;

import java.util.HashMap;
import java.util.Locale;

import org.tepi.filtertable.FilterDecorator;
import org.tepi.filtertable.numberfilter.NumberFilterPopupConfig;

import archenoah.lib.vaadin.Language.i18n.I18nConverter;

import com.vaadin.server.Resource;
import com.vaadin.shared.ui.datefield.Resolution;

@Deprecated
public class DataviewDecorator implements FilterDecorator {
	
	private HashMap<String, I18nConverter> converterMap;
	
	/**
	 * use DataviewGenerator instead
	 */
	public DataviewDecorator(HashMap<String, I18nConverter> converterMap) {
		this.converterMap = converterMap;
	}

	@Override
	public String getEnumFilterDisplayName(Object propertyId, Object value) {
		
		if(converterMap.get(propertyId) == null){
			return null;
		}
		
		return converterMap.get(propertyId).getLngText(value.toString());

	}

	@Override
	public Resource getEnumFilterIcon(Object propertyId, Object value) {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public String getBooleanFilterDisplayName(Object propertyId, boolean value) {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public Resource getBooleanFilterIcon(Object propertyId, boolean value) {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public boolean isTextFilterImmediate(Object propertyId) {
		return true;
	}

	@Override
	public int getTextChangeTimeout(Object propertyId) {
		// TASK Auto-generated method stub
		return 250;
	}

	@Override
	public String getFromCaption() {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public String getToCaption() {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public String getSetCaption() {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public String getClearCaption() {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public Resolution getDateFieldResolution(Object propertyId) {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public String getDateFormatPattern(Object propertyId) {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public Locale getLocale() {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public String getAllItemsVisibleString() {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public NumberFilterPopupConfig getNumberFilterPopupConfig() {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public boolean usePopupForNumericProperty(Object propertyId) {
		// TASK Auto-generated method stub
		return false;
	}

}
