package archenoah.lib.vaadin.Components.MultiRowData;

import java.util.ArrayList;

import com.vaadin.data.Item;

public interface MultiRowPopupInput {
    
    ArrayList<MultiRowDataField> getFields();
    void show();
    void setSaveAction(SaveAction saveAction);
    void setValue(Item item);
    
    static abstract class SaveAction{
        /**
         * 
         * @param fieldList - should be null ob abort/invalid
         */
        public abstract void onSave(ArrayList<MultiRowDataField> fieldList);
    }
    
}
