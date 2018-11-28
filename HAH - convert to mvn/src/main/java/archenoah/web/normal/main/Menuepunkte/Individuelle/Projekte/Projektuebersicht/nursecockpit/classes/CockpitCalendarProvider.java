package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.nursecockpit.classes;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.vaadin.data.Container.Indexed;
import com.vaadin.ui.components.calendar.ContainerEventProvider;
import com.vaadin.ui.components.calendar.event.CalendarEvent;

@SuppressWarnings("serial")
public class CockpitCalendarProvider extends ContainerEventProvider {
    
    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    
    public CockpitCalendarProvider(Indexed container) {
        super(container);
    }

    @Override
    public List<CalendarEvent> getEvents(Date startDate, Date endDate) {
        
        // no need to hide events in month view for now
        
//        Days days = Days.daysBetween(new LocalDateTime(startDate), new LocalDateTime(endDate));
//        
        List<CalendarEvent> list = new CopyOnWriteArrayList<CalendarEvent>(super.getEvents(startDate, endDate));
//        
//        if(days.getDays() > 7) {
//            
//            for (CalendarEvent calendarEvent : list) {
//                if(!(calendarEvent instanceof CockpitCalendarEvent)) {
//                    list.remove(calendarEvent);
//                }
//            }
//            
//        }
        
        return Collections.unmodifiableList(list);
    }
    
    
   
}
