package archenoah.lib.vaadin.Components.ClickMenuBar;

import java.io.Serializable;

import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

public abstract class ClickCommand implements Command, Serializable{
    
    private static final long serialVersionUID = -3406317556116436445L;
    
    private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    
    private Window window;
    private MenuBar parent;
    
    public ClickCommand() {}
    
    @Override
    public void menuSelected(MenuItem selectedItem) {
        
        menuClicked(selectedItem);
        
        if(parent == null || window == null) {
            return;
        }
        
        parent.setEnabled(false);
        window.addCloseListener(new CloseListener() {
            
            @Override
            public void windowClose(CloseEvent e) {
                
                parent.setEnabled(true);
                
            }
        });
        
        window = null;
    }
    
    public void setWindow(Window window) {
        this.window = window;
    }
    
    public void setParent(MenuBar parent) {
        this.parent = parent;
    }
    
    
    public abstract void menuClicked(MenuItem selectedItem);

}
