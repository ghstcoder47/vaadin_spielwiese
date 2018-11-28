package archenoah.lib.vaadin.recurrence;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.LocalDate;

import com.google.ical.compat.jodatime.LocalDateIterator;
import com.google.ical.compat.jodatime.LocalDateIteratorFactory;

public class DateBoundaries {
    
    /**
     * 
     * @param rrule
     * @param startDate
     * @param originalDate
     * @return Pair<previousDate, followingDate> or null if invalid
     */
    public static Pair<Date, Date> getBoundaries(String rrule, Date startDate, Date originalDate){
        
        if(rrule == null || "".equals(rrule) || startDate == null || originalDate == null) {
            return null;
        }
        
        try {
            
            LocalDate previous = null;
            LocalDate original = new LocalDate(originalDate);
            LocalDate following = null;
            
            LocalDateIterator it = LocalDateIteratorFactory.createLocalDateIterator(rrule, new LocalDate(startDate), true);
            while(it.hasNext()) {
                
                LocalDate next = it.next();
                if(next.isBefore(original)){
                    previous = next;
                }
                if(next.isAfter(original)) {
                    following = next;
                    break;
                }
            }
            
            Date p = null;
            Date f = null;
            
            if(previous != null) {
                p = previous.toDate();
            }
            if(following != null) {
               f = following.toDate();
            }
            return Pair.of(p, f);
            
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
