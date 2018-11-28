/**
 * 
 */
package archenoah.lib.vaadin.Components.Combo;

import java.util.Collection;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;

/**
 * @author Developer
 * Custom wrapper to easily access Container Property Values
 */
public class ContainerComboBox extends ComboBox {
	
	private Object idProperty;
	private Boolean overrideDefaultId = false;
	
	/**
	 * 
	 */
	public ContainerComboBox() {
		// TASK Auto-generated constructor stub
	}
	
	/**
	 * @param caption
	 */
	public ContainerComboBox(String caption) {
		super(caption);
		// TASK Auto-generated constructor stub
	}

	/**
	 * @param caption
	 * @param options
	 */
	public ContainerComboBox(String caption, Collection<?> options) {
		super(caption, options);
		// TASK Auto-generated constructor stub
	}

	/**
	 * @param caption
	 * @param dataSource
	 */
	public ContainerComboBox(String caption, Container dataSource) {
		super(caption, dataSource);
	}
	
	/* ************************************************************************** */
	
	/**
	 * The next setValue() or getValue() will return the value of the specified Property instead.\n
	 * This needs to be called before each setValue() or getValue() to avoid programmatic issues (setting again to the container index for some reason)
	 * @param override
	 */
	public void setOverrideDefaultId(Boolean override){
		this.overrideDefaultId = override;
	}
	
	/**
	 * Sets the Container Property id used to retrieve the Value
	 * @param idProperty
	 */
	public void setIdProperty(Object idProperty){
		this.idProperty = idProperty;
	}
	
	/**
	 * Returns the default property id value of the Container
	 * @return
	 */
	public Object getIdPropertyValue(){
		return getIdPropertyValue(this.idProperty);
	}
	
	
	/**
	 * Returns the vale of the provided property from Container
	 * @param propertyId
	 * @return
	 */
	public Object getIdPropertyValue(Object propertyId){
		Property p = super.getContainerProperty(super.getValue(), propertyId);
		Object value = (p != null) ? p.getValue() : null;
		return value;
	}
	
	public void setValueByPropertyId(Object id){
		Object cid = null;
		
		for (Object lid : super.getContainerDataSource().getItemIds()) {
			if(super.getContainerProperty(lid, this.idProperty).getValue().equals(id)){
				cid = lid;
				break;
			}
		}
		
		super.setValue(cid, true);
	}
	
	/* ************************************************************************************** */
	
//	@Override
//	public Object getValue(){
//		
//		Object value = overrideDefaultId ? getIdPropertyValue() : super.getValue();
//		overrideDefaultId = false;
//		return value;
//	}
//	
//	@Override
//	public void setValue(Object value){
//		
//		if(value == null){
//			super.setValue(null);
//			return;
//		}
//		
//		if(overrideDefaultId){
//			setValueByPropertyId(value);
//			overrideDefaultId = false;
//		}else{
//			super.setValue(value);
//		}
//		
//	}
	
	
}
