package archenoah.lib.vaadin.Components.Metadata;

import java.util.ArrayList;
import java.util.HashMap;

import org.joda.time.LocalDate;

public class RecurrenceDateInformation {
    private ArrayList<LocalDate> dateList;
    private HashMap<Object, Object> additionalValues;
    private LocalDate latestDate;
    
    public RecurrenceDateInformation() {
        
    }
    
    public ArrayList<LocalDate> getDateList() {
        return dateList;
    }
    public void setDateList(ArrayList<LocalDate> dateList) {
        this.dateList = dateList;
    }
    public HashMap<Object, Object> getAdditionalValues() {
        return additionalValues;
    }
    public void setAdditionalValues(HashMap<Object, Object> additionalValues) {
        this.additionalValues = additionalValues;
    }
    public LocalDate getLatestDate() {
        return latestDate;
    }
    public void setLatestDate(LocalDate latestDate) {
        this.latestDate = latestDate;
    }
}
