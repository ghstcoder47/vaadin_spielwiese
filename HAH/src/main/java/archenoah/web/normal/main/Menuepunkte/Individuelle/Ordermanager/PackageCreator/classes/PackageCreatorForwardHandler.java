package archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.PackageCreator.classes;

import com.vaadin.ui.components.calendar.CalendarComponentEvents.ForwardEvent;
import com.vaadin.ui.components.calendar.handler.BasicForwardHandler;

public abstract class PackageCreatorForwardHandler extends BasicForwardHandler {

    public PackageCreatorForwardHandler() {
        // TASK Auto-generated constructor stub
    }
    
    @Override
    public void forward(ForwardEvent event) {
        
        super.forward(event);
        onForward();
        
    }
    
    abstract public void onForward();
    
}
