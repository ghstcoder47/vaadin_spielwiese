package archenoah.lib.vaadin.Components.Calendar;

import com.vaadin.ui.Calendar;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClick;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClickHandler;

public abstract class EventClickCommand implements EventClickHandler{


    // {section fields}
    // ****************
    private Window window;
    private Calendar parent;
    // {end fields}

    // {section constructors}
    // **********************
    public EventClickCommand() {}
    // {end constructors}

    // {section publicmethods}
    // ***********************
    @Override
    public void eventClick(EventClick event) {
        
        eventClickExtended(event);
        
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
    
    public void setParent(Calendar parent) {
        this.parent = parent;
    }
    
    public abstract void eventClickExtended(EventClick event);    
    // {end publicmethods}

}
