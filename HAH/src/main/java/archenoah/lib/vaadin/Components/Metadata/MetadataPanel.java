package archenoah.lib.vaadin.Components.Metadata;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.tuple.Pair;

import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektzuweisung.Form.Metadata.MetadataForsteoMain;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektzuweisung.Form.Metadata.MetadataPoPMain;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektzuweisung.Form.Metadata.MetadataReplagalMain;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektzuweisung.Form.Metadata.MetadataSAMain;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektzuweisung.Form.Metadata.MetadataVprivMain;

import com.vaadin.ui.Panel;

public abstract class MetadataPanel extends Panel {

    private String mainId = null;
    private String patientId = null;
    private String projectId = null;
    private String nurseId = null;
    private Integer duplicateOrigVhId;
    
    public MetadataPanel() {

    }

    /***********************************/

    /**
     * @return null or ID
     */
    public String getMainId() {
        return mainId;
    }

    public void setMainId(String parentId) {
        this.mainId = parentId;
    }

    public Boolean hasMainId(){
        if(this.mainId == null || this.mainId.equals("") == true){
            return false;
        }
        return true;
    }
    
    /**
     * @return null or ID
     */
    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    
    public Boolean hasPatientId(){
        if(this.patientId == null || this.patientId.equals("") == true){
            return false;
        }
        return true;
    }
    
    /**
     * @return null or ID
     */
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    
    public Boolean hasProjectId(){
        if(this.projectId == null || this.projectId.equals("") == true){
            return false;
        }
        return true;
    }
    
    /**
     * @return null or ID
     */
    public String getNurseId() {
        return nurseId;
    }

    public void setNurseId(String nurseId) {
        this.nurseId = nurseId;
    }
    
    public Boolean hasNurseId(){
        if(this.nurseId == null || this.nurseId.equals("") == true){
            return false;
        }
        return true;
    }
    
    public boolean isDuplicationMode() {
        return duplicateOrigVhId != null;
    }
    
    public Integer getDuplicateOrigVhId() {
        return duplicateOrigVhId;
    }

    public void setDuplicateOrigVhId(Integer duplicateOrigVhId) {
        this.duplicateOrigVhId = duplicateOrigVhId;
    }

    public static MetadataPanel getPanelFor(String projectCode) {
        
        MetadataPanel panel = null;
        switch (projectCode) {
        case "FOR":
            panel = new MetadataForsteoMain();
            break;
        case "REP":
            panel = new MetadataReplagalMain();
            break;
        case "VPRIV":
            panel = new MetadataVprivMain();
            break;
        case "POP":
            panel = new MetadataPoPMain();
            break;
        case "SA":
            panel = new MetadataSAMain();
            break;
        default:
            break;
        };
        return panel;
    }
    
    abstract public Panel getPanel();

    abstract public Boolean validate();
    abstract public Boolean save();
    
    abstract public void update();
    
    abstract public HashMap<String, Object> getFieldValues();
    
    abstract public Boolean hasRecurrence();
    abstract public Boolean hasMultiRecurrence();
    abstract public void setRecurrenceSettings(String Form_ID);
    
    /**
     * this should be implemented in a way that is not dependend on setting main/project/patient ids!
     * @param delegationId the metadata id for the project
     * @param dataDate the current dataset date
     * @return Pair<previousDate, nextDate>
     */
    abstract public Pair<Date, Date> getBoundaryDates(Integer delegationId, Date originalDate);
    
    //TASK: include single Recurrence
    /**
     * 
     * @param all - true if it should generate x dates, where x is max reps. false if it should generate only x dates into the future.
     * @return
     */
    abstract public HashMap<Integer, RecurrenceDateInformation> getRecurrenceDates(Boolean all);
    
    abstract public String getRecurrenceString();
    abstract public Date getRecurrenceStart();
    abstract public Integer getRecurrenceRepetitions();
    abstract public Date getRecurrenceLatestDate();
    abstract public Integer getRecurrenceMetaId();
    abstract public void deleteUnaccepted();
    
    public Boolean isNurseRequired() {
        return true;
    }
    public Boolean isPatientRequired() {
        return true;
    }
    
}
