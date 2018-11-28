package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.classes;

import java.util.ArrayList;
import java.util.Date;

import org.joda.time.LocalDate;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;

import com.google.common.base.Joiner;
import com.vaadin.data.Container;
import com.vaadin.data.Item;

public class ServicePlanningCreator {


    // {section fields}
    // ****************
    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    
    private Integer vhId;
    private Integer parentId;
    private Integer patientId;
    private Integer nurseId;
    private Date startDate;
    
    // {end fields}

    // {section constructors}
    // **********************
    public ServicePlanningCreator() {
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************

    public Integer getVhId() {
        return vhId;
    }

    public void setVhId(Integer vhId) {
        this.vhId = vhId;
    }
    
    public Integer getParentId() {
        return parentId;
    }
    public void setParentId(Integer projectId) {
        this.parentId = projectId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getNurseId() {
        return nurseId;
    }

    public void setNurseId(Integer nurseId) {
        this.nurseId = nurseId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    // {end gettersandsetters}


    // {section publicmethods}
    // ***********************
    public ArrayList<Integer> generate() {
        if(vhId == null 
            || parentId == null
            || patientId == null
            || startDate == null) {
            throw new IllegalArgumentException("vhId, parentId, patientId and startDate are required!");
        }
        
        ArrayList<PlanningEntry> planned = new ArrayList<PlanningEntry>();
        
        Container con = dbGetDays();
        for (Object iid : con.getItemIds()) {
            Item item = con.getItem(iid);
            PlanningEntry bean = new PlanningEntry();
            bean.setType(MyUtils.getValueFromItem(item, "STP_TYPE", String.class));
            bean.setDays(MyUtils.getValueFromItem(item, "STP_DAYS", Integer.class));
            bean.setComment(MyUtils.getValueFromItem(item, "STP_COMMENT", String.class));
            planned.add(bean);
        }
        
        if(planned.size() == 0) {
            return new ArrayList<Integer>();
        }
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "INSERT INTO cust_protokoll_servicetasking (ST_VH_ID, ST_ID_PATIENT, ST_ID_NURSE, ST_ID_TYPE, ST_DATE, ST_ORIGINAL_DATE, ST_CAPTION) VALUES";

        
        ArrayList<String> values = new ArrayList<String>();
        for (PlanningEntry entry : planned) {
            String date = "'" + MyUtils.formatSqlDate(new LocalDate(startDate).plusDays(entry.getDays() - 1)) + "'";
            
            String insert = "(";
            
            insert += vhId;
            insert += ", " + patientId;
            insert += ", " + (nurseId != null ? nurseId : "NULL");
            insert += ", '" + entry.getType() + "'";
            insert += ", " + date;
            insert += ", " + date;
            insert += ", '" + entry.getComment() + "'";
            insert += ")";
            
            values.add(insert);

        }
        
        sql += Joiner.on(",\r\n").join(values) + ";";
        q.setSqlString(sql);
        
        ArrayList<Integer> ids = new ArrayList<Integer>();
        q.update(false, ids);
        
        return ids;
        
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    // {end privatemethods}

    // {section database}
    // ******************
    private Container dbGetDays() {
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select * from cust_protokoll_servicetasking_planning where STP_ID_PARENT = " + parentId + " order by STP_DAYS asc, STP_TYPE asc";
        q.setSqlString(sql);
        return q.query();
    }
    // {end database}

    // {section classes}
    // ****************
    
    private class PlanningEntry{
        
        private String type;
        private int days;
        private String comment;
        
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
        public int getDays() {
            return days;
        }
        public void setDays(int days) {
            this.days = days;
        }
        public String getComment() {
            return comment;
        }
        public void setComment(String comment) {
            this.comment = comment;
        }
    }
    
    // {end layout}
}
