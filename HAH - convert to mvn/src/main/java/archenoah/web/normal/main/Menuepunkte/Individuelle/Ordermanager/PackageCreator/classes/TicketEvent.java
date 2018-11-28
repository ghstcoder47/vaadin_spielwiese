package archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.PackageCreator.classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import org.joda.time.LocalDate;

import archenoah.lib.custom.MyUtils;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.OpenTickets.classes.TITYPE;

import com.vaadin.data.Item;
import com.vaadin.ui.components.calendar.event.BasicEvent;

public class TicketEvent extends BasicEvent {
    
    private Item item;
    private LocalDate ticketStart;
    private LocalDate ticketEnd;
    private String countryKey;
    private ArrayList<Item> ticketContents;
    
    public enum TicketType {
        STANDALONE,
        PLANNED,
        CREATED,
        UNACCEPTED_GROUPED,
        UNACCEPTED_SINGLE,
        MANUFACTURER_ORDER
    }
    
    private TicketType type = null;
    private Integer id = null;
    
    private Integer importance = 0;
    private TITYPE packageType = null;
    
    public TicketEvent(String caption, String description, Date date) {
        super(caption, description, date);
    }

    public TicketEvent(String caption, String description, Date startDate, Date endDate) {
        super(caption, description, startDate, endDate);
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
    
    public LocalDate getTicketStart() {
        return ticketStart;
    }

    public void setTicketStart(LocalDate ticketStart) {
        this.ticketStart = ticketStart;
    }

    public LocalDate getTicketEnd() {
        return ticketEnd;
    }

    public void setTicketEnd(LocalDate ticketEnd) {
        this.ticketEnd = ticketEnd;
    }
    
    public String getCountryKey() {
        return countryKey;
    }

    public void setCountryKey(String countryKey) {
        this.countryKey = countryKey;
    }

    public ArrayList<Item> getContents() {
        return ticketContents;
    }

    public void setTicketContents(ArrayList<Item> arrayList) {
        this.ticketContents = arrayList;
    }

    public Integer getNurseId(){
        return (Integer) item.getItemProperty("KSK_ID").getValue();
    }
    
    public Integer getNurseUserId(){
        return (Integer) item.getItemProperty("OMPR_ID_USER").getValue();
    }
    
    public Integer getInterval(){
        return  (Integer) item.getItemProperty("OMPR_TICKET_LENGTH").getValue();
    }
    
    public Integer getPatientId() {
        return MyUtils.getValueFromItem(item, "OMS_ID_PATIENT", Integer.class);
    }
    
    public Integer getId() {
        return id;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getImportance() {
        return importance;
    }
    /**
     * Higher value = higher up in calendar
     * @param importance
     */
    public void setImportance(Integer importance) {
        this.importance = importance;
    }
    
    public Integer getDuplicationHash() {
        return Objects.hash(packageType, ticketStart, ticketEnd, MyUtils.getValueFromItem(item, "name", String.class));
    }

    /**
     * @return the packageType
     */
    public TITYPE getPackageType() {
        return packageType;
    }

    /**
     * @param packageType the packageType to set
     */
    public void setPackageType(TITYPE packageType) {
        this.packageType = packageType;
    }
}
