package archenoah.lib.vaadin.CustomConverterFactorys;

import com.vaadin.data.util.IndexedContainer;

public interface TableFilterConverter {
    
    /**
     * default caption property, useable directly in implelenting classes
     */
    public static String caption = "caption";
    
    public IndexedContainer getContainer();
    public Object getItemCaptionProperty();
    
}
