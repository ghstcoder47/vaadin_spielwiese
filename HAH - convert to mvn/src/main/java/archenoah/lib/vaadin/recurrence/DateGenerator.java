package archenoah.lib.vaadin.recurrence;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.joda.time.LocalDate;

import com.google.ical.compat.jodatime.LocalDateIteratorFactory;

public class DateGenerator {
    
    private String rruleString = null;
    private LocalDate startDate = null;
    private Integer maxDates = null;
    
    private Date continuationDate = null;
    
    public DateGenerator(String rruleString, Date startDate, Integer maxDates) {
        this.rruleString = rruleString;
        this.startDate = new LocalDate(startDate);
        this.maxDates = maxDates;
    }
    
    public DateGenerator(String rruleString, LocalDate startDate, Integer maxDates) {
        this.rruleString = rruleString;
        this.startDate = startDate;
        this.maxDates = maxDates;
    }
    
    public String getRruleString() {
        return rruleString;
    }

    public void setRruleString(String rruleString) {
        this.rruleString = rruleString;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Integer getMaxDates() {
        return maxDates;
    }

    public void setMaxDates(Integer maxDates) {
        this.maxDates = maxDates;
    }

    public void setContinuationDate(Date cont){
        this.continuationDate = cont;
    }
    
    public ArrayList<LocalDate> generateDateList(){
        ArrayList<LocalDate> dateList = new ArrayList<LocalDate>();
        try {
            Integer current = 0;
            
            if(rruleString != null && startDate != null) {
                
                for (LocalDate date : LocalDateIteratorFactory.createLocalDateIterable(rruleString, startDate, true)) {
                    
                    if(continuationDate != null && date.isBefore(new LocalDate(continuationDate).plusDays(1))){
                        continue;
                    }
                    
                    if(++current <= maxDates){
                        dateList.add(date);
                    }else{
                        break;
                    }
                }
                
            }
            
        } catch (ParseException e) {
            dateList = new ArrayList<LocalDate>();
        }
        
        return dateList;
    }
    
}
