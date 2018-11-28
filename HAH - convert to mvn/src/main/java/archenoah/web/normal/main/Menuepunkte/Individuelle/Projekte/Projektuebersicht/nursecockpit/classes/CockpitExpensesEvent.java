package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.nursecockpit.classes;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import archenoah.lib.custom.MyUtils;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.expenses.form.ExpensesInsertEdit;

import com.vaadin.data.Item;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.components.calendar.event.BasicEvent;

public class CockpitExpensesEvent extends BasicEvent implements TravelTimeEvent{
    
    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    
    private Item item;
//    private ProjectData projectData;

    private Date start;
    private Date end;
    
    private HashMap<String, Pair<String, Integer>> styleMap;
    private ThemeResource icon;
    
    private CloseListener closeListener;
    
    public CockpitExpensesEvent(Item item, CloseListener closeListener, HashMap<String, Pair<String, Integer>> styleMap, ThemeResource icon) {
        super(getCombinedCaption(item), getCombinedDescription(item), getStartDate(item), getEndDate(item));
        this.styleMap = styleMap;
        this.icon = icon;
        
        this.closeListener = closeListener;
        
        start = getStartDate(item);
        end = getEndDate(item);
        
        setItem(item);
        setStyleName(getStyleName());
        
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
    
    public Integer getId(){
        return getId(item);
    }
    public Integer getId(Item item){
        return MyUtils.getValueFromItem(item, "id", Integer.class);
    }
    
    public Integer getNurseId(){
        return getNurseId(item);
    }
    public Integer getNurseId(Item item){
        return MyUtils.getValueFromItem(item, "user_id", Integer.class);
    }
    
    public String getCombinedCaption() {
        return getCombinedCaption(item);
    }
    public static String getCombinedCaption(Item item) {
        return getActivity(item);
    }
    
    public String getActivity() {
        return getNurseName(item);
    }
    public static String getActivity(Item item) {
        return MyUtils.getValueFromItem(item, "activity", String.class);
    }
    
    public String getNurseName() {
        return getNurseName(item);
    }
    public static String getNurseName(Item item) {
        return MyUtils.getValueFromItem(item, "user", String.class);
    }
    
    @Override
    public Long getTravelTime() {
        return getTravelTime(item);
    }
    public static Long getTravelTime(Item item) {
        Time travel = MyUtils.getValueFromItem(item, "travel", java.sql.Time.class);
        if(travel != null) {
            return travel.getTime();
        }else {
            return null;
        }
    }
    
    public String getProjectCode() {
        return getProjectCode(item);
    }
    public static String getProjectCode(Item item) {
        return "X";
    }
    
    public String getCombinedDescription() {
        return getCombinedDescription(item);
    }
    public static String getCombinedDescription(Item item) {
        return getNurseName(item) + "<br />" + MyUtils.getValueFromItem(item, "comment", String.class) + "<br />" + MyUtils.getValueFromItem(item, "status", String.class);
    }
    
    @Override
    public Date getStart() {
        return start;
        //return getStartDate(item);
    }
    public static Date getStartDate(Item item) {
        return getDate(item, "start");
    }
    
    @Override
    public void setStart(Date start) {
        this.start = start;
    }
    
    @Override
    public Date getEnd() {
        return end;
//        return getEndDate(item);
    }
    public static Date getEndDate(Item item) {
        return getDate(item, "end");
    }
    
    @Override
    public void setEnd(Date end) {
        this.end = end;
    }
    
   
    @Override
    public boolean isAllDay() {
        return !(new LocalDateTime(getEnd()).isAfter(new LocalDateTime(getStart())));
    }
    
    @Override
    public String getStyleName() {

        if (getStatusData() == null) {
            return "";
        }

        return getStatusData().getLeft();
    }

    public Integer getIndex() {

        if (getStatusData() == null) {
            return 0;
        }

        return getStatusData().getRight();

    }
    
    public Window onClick() {
        MenuItem selected = new MenuBar() . new MenuItem("", icon, null);
        //return projectData.open(selected, getProjectCode(), getId(), false);
        
        ExpensesInsertEdit pie = new ExpensesInsertEdit(selected, getId().toString());
        
        pie.setPermissionsFromDatabase();
        
        pie.init();
        pie.getWindow().addCloseListener(closeListener);
        return pie.getWindow();

    }
    
    // private
    
    private Pair<String, Integer> getStatusData() {
        return styleMap.get(MyUtils.getValueFromItem(item, "status", String.class));
    }
    
    private static Date getDate(Item item, String timeString) {
        LocalDateTime date = new LocalDateTime(MyUtils.getValueFromItem(item, "date", java.sql.Timestamp.class).getTime());
        Time time = MyUtils.getValueFromItem(item, timeString, java.sql.Time.class);
        
        if(time != null) {
            LocalTime ltime = new LocalTime(time.getTime());
            date = date.withTime(ltime.getHourOfDay(), ltime.getMinuteOfHour(), 0, 0);
        }
        
        return date.toDate();
    }
}
