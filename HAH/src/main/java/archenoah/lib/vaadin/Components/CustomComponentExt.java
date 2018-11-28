package archenoah.lib.vaadin.Components;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.CustomComponent;

public class CustomComponentExt extends CustomComponent {
    public void setValueExt(AbstractField field, Object value) {
	if (field.isReadOnly()) {
	    field.setReadOnly(false);
	    field.setValue(value);
	    field.setReadOnly(true);
	} else {
	    field.setValue(value);
	}
    }
}
