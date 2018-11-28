package archenoah.lib.vaadin.Components.Combo;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.ui.NativeSelect;

public class nativecombo {

	private SQLContainer ds;

	public nativecombo(SQLContainer container, NativeSelect nselect, String IDname, String Anzeigename) {
		ds = container;
		// combo = nselect;
		if (container.size() > 0)

		{
			for (Object cityItemId : ds.getItemIds()) {
				if (ds.getItem(cityItemId).getItemProperty(Anzeigename).getValue() != null) {

					Object GrpId = ds.getItem(cityItemId).getItemProperty(IDname).getValue();

					String gruppe = ds.getItem(cityItemId).getItemProperty(Anzeigename).getValue().toString();

					nselect.addItem(GrpId);

					nselect.setItemCaption(GrpId, gruppe);
				}
			}

		}
	}

}
