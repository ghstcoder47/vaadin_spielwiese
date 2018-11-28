package archenoah.global.warningmodule;

import java.io.Serializable;

public class Warning implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -5080013306835229635L;
    // {section fields}
    // ****************
    private Integer userId;
    private String country;
    private Integer patientId;
    private java.sql.Date date;
    private WarningType warningType;
    private WarningSeverity warningSeverity;
    
    private String contents;
    // {end fields}

    // {section constructors}
    // **********************
    public Warning() {
        // TASK Auto-generated constructor stub
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public WarningType getType() {
        return warningType;
    }

    public void setType(WarningType warningType) {
        this.warningType = warningType;
    }

    public WarningSeverity getSeverity() {
        return warningSeverity;
    }

    public void setSeverity(WarningSeverity warningSeverity) {
        this.warningSeverity = warningSeverity;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public java.sql.Date getDate() {
        return date;
    }

    public void setDate(java.sql.Date date) {
        this.date = date;
    }
    
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    // {end privatemethods}

    // {section database}
    // ******************
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
