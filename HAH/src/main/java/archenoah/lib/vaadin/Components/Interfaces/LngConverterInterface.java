package archenoah.lib.vaadin.Components.Interfaces;

import com.vaadin.data.Container;
import com.vaadin.data.util.converter.Converter;

public interface LngConverterInterface extends Converter<String, Object>{
	
	abstract String getLngText(String value);
	abstract Container getContainer();
	
}
