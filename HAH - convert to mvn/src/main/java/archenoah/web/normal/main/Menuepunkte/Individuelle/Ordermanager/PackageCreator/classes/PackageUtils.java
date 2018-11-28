package archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.PackageCreator.classes;

import java.util.Date;

import org.joda.time.LocalDate;

import archenoah.lib.custom.MyUtils;

import com.ibm.icu.text.SimpleDateFormat;
import com.vaadin.ui.PopupDateField;

public class PackageUtils {
    
    public enum RANGE {
        WEEK
    }
    
    public PackageUtils() {
        // TASK Auto-generated constructor stub
    }
    
    public static final void limitDatePicker(PopupDateField field, Date date, RANGE range){
        limitDatePicker(field, new LocalDate(date), range);
    }
    
    public static final void limitDatePicker(PopupDateField field, LocalDate date, RANGE range){

        field.setRangeStart(MyUtils.getWeekMonday(date, 0).toDate());
        field.setRangeEnd(MyUtils.getWeekMonday(date, 0).plusWeeks(1).minusDays(1).toDate());
        
    }
    
    
    private String formatDate(Date date){
        SimpleDateFormat fmt = new SimpleDateFormat("YYYY-MM-dd");
        return fmt.format(date);
    }
    
    
    public static Date getTicketStartDate(TicketEvent ticketEvent){
        return ticketEvent.getTicketStart().toDate();
    }
    public static String getTicketStartDateString(TicketEvent ticketEvent){
        return ticketEvent.getTicketStart().toString("YYYY-MM-dd");
    }
    
    public static Date getTicketEndDate(TicketEvent ticketEvent){
        return ticketEvent.getTicketEnd().toDate();
    }
    public static String getTicketEndDateString(TicketEvent ticketEvent){
        return ticketEvent.getTicketEnd().toString("YYYY-MM-dd");
    }
    
    public static String getCondition(TicketEvent ticketEvent){
        String condition = "accepted = 1 AND " 
                + "nurse_id = " + ticketEvent.getNurseId() + " AND "
                + "`date` BETWEEN '" + getTicketStartDateString(ticketEvent)
                + "' AND '" + getTicketEndDateString(ticketEvent) + "'";
        return condition;
    } 
}
