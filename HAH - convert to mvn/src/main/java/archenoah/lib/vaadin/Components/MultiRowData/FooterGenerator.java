package archenoah.lib.vaadin.Components.MultiRowData;

import java.util.Map;

import com.vaadin.data.Item;

public interface FooterGenerator {
    
    public abstract void reset();
    public abstract String getFooterText();
    public abstract void modify(Object columnId, Item item, Map<Object, Object> generated);

}
