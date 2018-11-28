package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektzuweisung.Form.classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.LocalDate;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.vaadin.Components.Metadata.MetadataPanel;
import archenoah.lib.vaadin.Components.Metadata.RecurrenceDateInformation;
import archenoah.lib.vaadin.recurrence.DateGenerator;

import com.vaadin.data.Container;
import com.vaadin.data.Item;

public class BlankoGenerator {

    // {section fields}
    // ****************
    private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    
    private Item vhItem; 
    private MetadataPanel metadataPanel;
    private BlankoInstructions blankoInstructions;
    private DBClass dbm;
    
    
    // {end fields}

    

    







    // {section constructors}
    // **********************
    public BlankoGenerator() {
        dbm = new DBClass();
    }
    // {end constructors}
    
    // {section gettersandsetters}
    // ***************************
    public void setVkItem(Item vhItem) {
        this.vhItem = vhItem;
    }
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    
    /**
     * inserts upcoming Blankos up to the number specified in the current delegation
     */
    public void fillBlankos() {
        fillInfo();
        iterateBlankos();
    }
    
    /**
     * inserts upcoming Blankos up to max
     * @param max
     */
    public void fillBlankos(Integer max) {
        //TASK TBD
    }
    
    /**
     * Inserts the specified amount of Blankos
     * @param count
     */
    public void insertBlanko(Integer count) {
      //TASK TBD
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    
    // private getters
    
    private Integer getVhId() {
        Integer vhId = null;
        if(vhItem != null
                && vhItem.getItemProperty("VH_ID") != null
                && vhItem.getItemProperty("VH_ID").getValue() != null) {
            
            vhId = (Integer) vhItem.getItemProperty("VH_ID").getValue();
        }
        return vhId;
    }
    
    private Integer getVhProjectId() {
        Integer projectId = null;
        if(vhItem != null
                && vhItem.getItemProperty("VH_PROJEKT_ID") != null
                && vhItem.getItemProperty("VH_PROJEKT_ID").getValue() != null) {
            
            projectId = (Integer) vhItem.getItemProperty("VH_PROJEKT_ID").getValue();
        }
        return projectId;
    }
    
    private Integer getVhPatientId() {
        Integer patientId = null;
        if(vhItem != null
                && vhItem.getItemProperty("VH_PATIENT_ID") != null
                && vhItem.getItemProperty("VH_PATIENT_ID").getValue() != null) {
            
            patientId = (Integer) vhItem.getItemProperty("VH_PATIENT_ID").getValue();
        }
        return patientId;
    }
    
    private String getVhProjectCode() {
        String projectCode = null;
        if(vhItem != null
                && vhItem.getItemProperty("PSLV_CODE") != null
                && vhItem.getItemProperty("PSLV_CODE").getValue() != null) {
            
            projectCode = (String) vhItem.getItemProperty("PSLV_CODE").getValue();
        }
        return projectCode;
    }
    
    //private logic
    
    private void fillInfo() {
        initMetadataPanel();
        blankoInstructions = new BlankoInstructions(getVhProjectId());
    }
    
    private void initMetadataPanel(){
        
        MetadataPanel panel = MetadataPanel.getPanelFor(getVhProjectCode());
        
        if(panel != null) {
            panel.setMainId(getVhId().toString());
            panel.setProjectId(getVhProjectId().toString());
            panel.setPatientId(getVhPatientId().toString());
        }
        metadataPanel = panel;
    }
    

    private void iterateBlankos() {

        CopyOnWriteArrayList<LocalDate> dateList = new CopyOnWriteArrayList<LocalDate>();
        
     // multi Recurrence Blankos
        if(metadataPanel.hasMultiRecurrence()) {
            
            // dyn id, datelist
            HashMap<Integer, RecurrenceDateInformation> rlist = metadataPanel.getRecurrenceDates(false);
            
            if(rlist != null) {
                
                for (Entry<Integer, RecurrenceDateInformation> entry : rlist.entrySet()) {
                    
                    ArrayList<LocalDate> multiList = entry.getValue().getDateList();
                    HashMap<Object, Object> adv = entry.getValue().getAdditionalValues();
                    Integer rid = entry.getKey();
                    
                    if(multiList.size() > 0){
                        for (LocalDate localDate : multiList) {
                            generateBlankos(localDate.toDate(), rid, adv);
                        }
                    }
                }
            
            }
            
        }
        
        
        if (metadataPanel.hasRecurrence()) {

            metadataPanel.setRecurrenceSettings(getVhId().toString());
            DateGenerator dg = new DateGenerator(metadataPanel.getRecurrenceString(), metadataPanel.getRecurrenceStart(),
                    metadataPanel.getRecurrenceRepetitions());
            
            Date latestEntryDate = metadataPanel.getRecurrenceLatestDate();
            
            LocalDate latest = new LocalDate(latestEntryDate);
            LocalDate today = new LocalDate();
            LocalDate lowestDate = latest.isBefore(today) ? latest : today; 
            
            log.debug("reps {}, latest{}, today {}, lowestDate {}", metadataPanel.getRecurrenceRepetitions(), latest, today, lowestDate);
            
            // skip if no entries
            if(latestEntryDate != null) {
                dg.setContinuationDate(lowestDate.toDate());
            }
            
            try {
                
                dateList = new CopyOnWriteArrayList<LocalDate>(dg.generateDateList());
                
                log.debug("datelist before {}", dateList);
                
                // skip if no entries
                if(latestEntryDate != null) {
                
                    dateList.remove(latest);
                    
                    for (Iterator<LocalDate> dit = dateList.iterator(); dit.hasNext();) {
                        LocalDate localDate = dit.next();
                        
                        if(localDate.isBefore(latest)) {
                            dateList.remove(localDate);
                        }
                        
                    }
                }
                
                log.debug("datelist after {}", dateList);
                
            } catch (Exception e) {
                log.error("exception in datelist generator for VH_ID {}", getVhId());
            }

        }

        if (dateList.size() > 0) {
            
            log.info("### generating {} blankos for VH_ID {} ###", dateList.size(), getVhId());
            
            for (LocalDate localDate : dateList) {
                generateBlankos(localDate.toDate(), metadataPanel.getRecurrenceMetaId(), null);
            }
        }
    }
    
    // {end privatemethods}

    // {section database}
    // ******************
   
    @SuppressWarnings("unchecked")
    private void generateBlankos(Date date, Integer delegationId, HashMap<Object, Object> additionalValues) {
        
//        {cust_protokoll_replagal_daten=[(VH_NURSE_ID,PRD_K_ID), (VH_PATIENT_ID,PRD_P_ID), (VH_ARZT_ID,PRD_ARZT_ID)]}
//        (PRD_DELEGATION_ID,PRD_DATE)
//        1892 2016-08-02 18:16:23.0 2016-08-02 18:16:23.0 2 10 1905 null 1 true 2 Replagal 18 REP info@isconet.de
        
        log.debug("generating Blanko for {} - {}", delegationId, date);
        
        DBClass db = new DBClass();
        
        // PB_T, {<PB_V, PB_C>, ...}
        HashMap<String, ArrayList<Pair<String, String>>> fieldMapping = blankoInstructions.getFieldMapping();
        // delIdField, delDateField
        Pair<String, String> ruleMapping = blankoInstructions.getRruleMapping();
        String originalDateField = blankoInstructions.getOriginalDateMapping();
        
        
        for (Entry<String, ArrayList<Pair<String, String>>> fieldEntry : fieldMapping.entrySet()) {
            
            String table = fieldEntry.getKey();
            ArrayList<Pair<String, String>> fields = fieldEntry.getValue();
            
            db.DB_Data_Insert.DB_Tabellen.DB_set_Tabelle_Einzeln(table);

            
            for (Pair<String, String> pair : fields) {

                String _valueCol  = pair.getLeft();
                String _targetCol = pair.getRight();
                
                String value = null;
                
                if(vhItem != null && vhItem.getItemProperty(_valueCol) != null &&
                        vhItem.getItemProperty(_valueCol).getValue() != null) {
                    value = vhItem.getItemProperty(_valueCol).getValue().toString();
                }
                
                db.DB_Data_Insert.DB_Daten.DB_Data(table, _targetCol, value);

                
            }
            
            // add recurrence data
            if(ruleMapping != null) {
                
                String delegationString = delegationId == null ? "" : delegationId.toString();
                
                db.DB_Data_Insert.DB_Daten.DB_Data(table, ruleMapping.getLeft(), delegationString);
                db.DB_Data_Insert.DB_Daten.DB_Data(table, ruleMapping.getRight(), MyUtils.formatSqlDate(date));
                
                if(additionalValues != null) {
                    for (Entry<Object, Object> entry : additionalValues.entrySet()) {
                        
                        String value = entry.getValue() != null ? entry.getValue().toString() : null;
                        db.DB_Data_Insert.DB_Daten.DB_Data(table, entry.getKey().toString(), value);
                        
                    }
                }
                
            }
            
            if(originalDateField != null) {
                db.DB_Data_Insert.DB_Daten.DB_Data(table, originalDateField, MyUtils.formatSqlDate(date));
            }
//            db.debugNextQuery(true);
            db.DB_Data_Insert.DB_Insert();
            db.DB_Data_Insert.DB_Data_Leeren();
            
        }


    }
    
    // {end database}

    // {section classes}
    // ****************
    
    private class BlankoInstructions{
        
        // PB_T, {<PB_V, PB_C>, ...}
        private HashMap<String, ArrayList<Pair<String, String>>> tableMapping = new HashMap<String, ArrayList<Pair<String, String>>>();
        // delIdField, delDateField
        private Pair<String, String> rruleMapping;
        private String originalDateField;
        
        public BlankoInstructions(Integer projectId) {
            fillMappings(projectId);
        }
        
        /**
         * @return PB_T, {<PB_V, PB_C>, ...}
         */
        public HashMap<String, ArrayList<Pair<String, String>>> getFieldMapping(){
            return tableMapping;
        }
        /**
         * @return delegationIdField, delegationDateField
         */
        public Pair<String, String> getRruleMapping(){
            return rruleMapping;
        }
        
        public String getOriginalDateMapping() {
            return originalDateField;
        }
        
        private void fillMappings(Integer projectId) {
            
            String pid = (projectId != null) ? Integer.toString(projectId) : "";

            dbm.DB_Data_Get.DB_Data_Leeren();
            
            dbm.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_All();

            dbm.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cust_projekte_blankos");
            dbm.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cust_projekte_blankos", "PB_P_ID", "=", "'" + pid + "'", "");

            Container con = dbm.DB_Data_Get.DB_SEND_AND_GET();
            
            String delIdField = null;
            String delDateField = null;
            String delOriginalDateField = null;
            
            for (Object iid : con.getItemIds()) {
                
                Item item = con.getItem(iid);
                
                // handle rrule entries
                if(item.getItemProperty("PB_RRULE_TYPE") != null && item.getItemProperty("PB_RRULE_TYPE").getValue() != null) {
                    
                    if(item.getItemProperty("PB_RRULE_TYPE").getValue().equals(1)) {
                        delDateField = (String) item.getItemProperty("PB_C").getValue();
                    }
                    if(item.getItemProperty("PB_RRULE_TYPE").getValue().equals(2)) {
                        delIdField = (String) item.getItemProperty("PB_C").getValue();
                    }
                    if(item.getItemProperty("PB_RRULE_TYPE").getValue().equals(3)) {
                        delOriginalDateField = (String) item.getItemProperty("PB_C").getValue();
                    }
                    
                    continue;
                }
                
                
                // handle field mappings
                String table = (String) item.getItemProperty("PB_T").getValue();
                Pair<String, String> pair = Pair.of(
                        (String) item.getItemProperty("PB_V").getValue(),
                        (String) item.getItemProperty("PB_C").getValue());
                try {
                    if (tableMapping.containsKey(table)) {
                        ArrayList<Pair<String, String>> pairs = tableMapping.get(table);
                        pairs.add(pair);
                    } else {
                        ArrayList<Pair<String, String>> pairs = new ArrayList<Pair<String, String>>();
                        pairs.add(pair);
                        tableMapping.put(table, pairs);
                    }
                } catch (Exception e) {
                    log.warn("error adding fieldMapping entry for PB_ID {}", iid);
                }
                
            }
            
            if(delIdField != null && delDateField != null) {
                rruleMapping = Pair.of(delIdField, delDateField);
            }
            
            if(delOriginalDateField != null) {
                originalDateField = delOriginalDateField;
            }
            
        }
        
    };
    
    // {end classes}
    
    
    
    
    
    
    
    
    
    
    
    
    
    





}
