package archenoah.lib.vaadin.Components.MultiRowData;

import java.util.ArrayList;

import com.vaadin.data.Item;
import com.vaadin.data.util.PropertysetItem;

public abstract class MultiRowSaveListener {

    abstract public void onSave(ArrayList<Item> insertedItems, ArrayList<PropertysetItem> removedItems);

}
