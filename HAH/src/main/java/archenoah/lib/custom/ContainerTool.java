package archenoah.lib.custom;

import java.util.Collection;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

public class ContainerTool implements Container{
	
	private static final long serialVersionUID = -4828487137910228077L;
	private Container con;
	private Object[] objects;
	
	public ContainerTool(Container con) {
		this.con = con;
	}
	
	public ContainerTool(Container con, Object... objects){
		this.objects = objects;
	}
	
	/*
	 * Custom
	 */
	
	/**
	 * returns the value of getContainerProperty directly
	 * @param itemId
	 * @param propertyId
	 * @return
	 */
	public Object getContainerValue(Object itemId, Object propertyId){
		return con.getContainerProperty(itemId, propertyId).getValue();
	}
	
	/**
	 * Runs the provided IterationInstruction against all items in the container
	 * 
	 * <pre>
	 *{@code
	 *ContainerTool ct = new ContainerTool(tagContainer, object, ...);
	 *ct.iterateItems(ct.new IterationInstruction() {
	 *  {@literal @Override}
	 *  public void runInstruction(ContainerTool ctt, Object itemId, Item item, Object[] objects) {
	 *    // do things
	 *  }
	 *});
	 * </pre>
	 * 
	 * @param instruction
	 */
	public void iterateItems(IterationInstruction instruction){
		if(con.size() > 0){
			for (Object itemId : con.getItemIds()) {
				instruction.runInstruction(this, itemId, con.getItem(itemId), objects);
			}
		}
	}
	
	
	public abstract class IterationInstruction{
		public abstract void runInstruction(ContainerTool ct, Object itemId, Item item, Object[] objects);
	}
	
	/*
	 * Overrides
	 */
	
	@Override
	public Item getItem(Object itemId) {
		return con.getItem(itemId);
	}

	@Override
	public Collection<?> getContainerPropertyIds() {
		return con.getContainerPropertyIds();
	}

	@Override
	public Collection<?> getItemIds() {
		return con.getItemIds();
	}

	@Override
	public Property getContainerProperty(Object itemId, Object propertyId) {
		return con.getContainerProperty(itemId, propertyId);
	}

	@Override
	public Class<?> getType(Object propertyId) {
		return con.getType(propertyId);
	}

	@Override
	public int size() {
		return con.size();
	}

	@Override
	public boolean containsId(Object itemId) {
		return con.containsId(itemId);
	}

	@Override
	public Item addItem(Object itemId) throws UnsupportedOperationException {
		try {
			return con.addItem(itemId);
		} catch (UnsupportedOperationException e) {
			throw e;
		}
	}

	@Override
	public Object addItem() throws UnsupportedOperationException {
		try {
			return con.addItem();
		} catch (UnsupportedOperationException e) {
			throw e;
		}
	}

	@Override
	public boolean removeItem(Object itemId) throws UnsupportedOperationException{
		try {
			return con.removeItem(itemId);
		} catch (UnsupportedOperationException e) {
			throw e;
		}
	}

	@Override
	public boolean addContainerProperty(Object propertyId, Class<?> type,
			Object defaultValue) throws UnsupportedOperationException {
		try {
			return con.addContainerProperty(propertyId, type, defaultValue);
		} catch (UnsupportedOperationException e) {
			throw e;
		}
	}

	@Override
	public boolean removeContainerProperty(Object propertyId)
			throws UnsupportedOperationException {
		try {
			return con.removeContainerProperty(propertyId);
		} catch (UnsupportedOperationException e) {
			throw e;
		}
	}

	@Override
	public boolean removeAllItems() throws UnsupportedOperationException {
		try {
			return con.removeAllItems();
		} catch (UnsupportedOperationException e) {
			throw e;
		}
	}
	
}