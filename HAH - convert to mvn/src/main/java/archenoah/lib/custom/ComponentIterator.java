package archenoah.lib.custom;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;

public class ComponentIterator {
	
	private HashMap<Object, String> objectMap;
	private Layout rootLayout;
	private Boolean includeUncaptioned = false;
	
	public ComponentIterator(Layout layout) {
		objectMap = new HashMap<Object, String>();
		this.rootLayout = layout;
	}

	public ComponentIterator(Layout layout, Boolean includeUncaptioned) {
		this(layout);
		this.includeUncaptioned = includeUncaptioned;
	}
	
	public void includeUncaptioned(Boolean includeUncaptioned){
		this.includeUncaptioned = includeUncaptioned;
	}
	
	public void setRootLayout(Layout layout){
		this.rootLayout = layout;
	}
	
	public HashMap<Object, String> createMap(){
		iterateR(rootLayout.iterator(), objectMap);
		return objectMap;
	}

	public void createMap(Map<Object, String> map){
		iterateR(rootLayout.iterator(), objectMap);
		map.putAll(objectMap);
	}
	
	private void iterateR(Iterator<Component> it, Map<Object, String> map){
	    	if(it.hasNext()){
	    		Component comp = it.next();
	    		
	    		if(comp instanceof Layout){
	    			Iterator<Component> et = ((Layout) comp).iterator();
	    			iterateR(et, map);
	    		}else if(comp instanceof Panel){
	    			Iterator<Component> et = ((Panel) comp).iterator();
	    			iterateR(et, map);
	    		}else if(comp instanceof TabSheet){
                    Iterator<Component> et = ((TabSheet) comp).iterator();
                    iterateR(et, map);
	    		}else{
	    			String caption = comp.getCaption();
	    			
	    			if(includeUncaptioned && (caption == null)){
	    				caption = "";
	    			}
	    			
	    			if(caption != null){
   			    
	    				caption = caption.toLowerCase().replaceAll("[^a-z ]", "").replaceAll(" ", "_");
	    				map.put(comp, caption);
	    			}
			}
			iterateR(it, map);
		}
	}
	
}
