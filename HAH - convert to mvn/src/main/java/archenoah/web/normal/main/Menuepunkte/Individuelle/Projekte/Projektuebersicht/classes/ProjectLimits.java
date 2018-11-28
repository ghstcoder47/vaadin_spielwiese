package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.classes;

import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;

import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class ProjectLimits {


    // {section fields}
    // ****************
    
    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    
    private String projectCode;
    private Integer dataId;
    private Integer delegationId;
    
    private Date originalDate;
    
    private Object assignedNurse;
    
    private Integer ticketId;
    private String ticketSql;
    
    private boolean isNurse;
    // {end fields}
    
    // {section constructors}
    // **********************
    public ProjectLimits(String projectCode, Integer dataId, Integer delegationId) {
        this.projectCode = projectCode;
        this.dataId = dataId;
        this.delegationId = delegationId;
        this.isNurse = MyUtils.getUserData().getNurse().isNurse();
    }
    
    public ProjectLimits(String projectCode, String dataId, Integer delegationId) {
        Integer id = null;
        
        try {
            id = Integer.parseInt(dataId);
        } catch (Exception e) {}
        
        this.projectCode = projectCode;
        this.dataId = id;
        this.delegationId = delegationId;
        this.isNurse = MyUtils.getUserData().getNurse().isNurse();
        
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    public void setOriginalDate(Date date) {
        originalDate = date;
    }
    
    public void setNurseSelect(final ComboBox nurseSelect) {
        setupNurseSelect(nurseSelect);
    }
    
    public void setStatusSelect(final AbstractSelect statusSelect) {
        setupStatusSelect(statusSelect);
    }
    
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    public void limitDatePicker(DateField dateField) {
        
        if(originalDate == null) {
            return;
        }

        LocalDate original = new LocalDate(originalDate);
        LocalDate lowerLimit = original.minusDays(7);
        LocalDate upperLimit = original.plusDays(7);
        
        if(dateField != null) {
            
            // avoids issues with re-setting limits (eg from plannedDate Listener)
            dateField.setRangeStart(null);
            dateField.setRangeEnd(null);
            
            dateField.setRangeStart(lowerLimit.toDate());
            dateField.setRangeEnd(upperLimit.toDate());
            
            setOriginalDateTooltip(dateField);
            
        }
        
    }
    
    public void setOriginalDateTooltip(DateField dateField) {
        
        DateTimeFormatter fmt = DateTimeFormat.shortDate();
        LocalDate original = new LocalDate(originalDate);
        
        String desc = "<table>"
          + "\n " + "  <tr>"
          + "\n " + "    <th>Plandatum:</th>"
          + "\n " + "  </tr>"
          + "\n " + "  <tr>"
          + "\n " + "    <td>"+original.toString(fmt)+"</td>"
          + "\n " + "  </tr>"
          + "\n " + "</table>";
         
         dateField.setDescription(desc);
        
    }
    
    public boolean isInTicket() {
        return getTicketId() != null;
    }

    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private void infoPopup() {
        
        Notification notif = new Notification("Paketwarnung", "<br />Dieses Protokoll ist bereits einem Versandpaket zugewiesen. (#" + ticketId + ")"
            + "<br />Bitte wenden Sie sich an den Customer Service.", Type.WARNING_MESSAGE);
        notif.setHtmlContentAllowed(true);

        notif.setDelayMsec(3000);
        notif.setPosition(Position.MIDDLE_CENTER);

        notif.show(Page.getCurrent());

        
    }
    
    
    private void setupNurseSelect(final ComboBox nurseSelect) {
        
        assignedNurse = nurseSelect.getValue();
        
        nurseSelect.addFocusListener(new FocusListener() {
            
            @Override
            public void focus(FocusEvent event) {
                
              if(isNurse && isInTicket()) {
                  infoPopup();
                  nurseSelect.setValue(assignedNurse);
                  nurseSelect.setEnabled(false);
                  nurseSelect.setStyleName("nogrey");
                  clearDatasource(nurseSelect);
              }
                
            }
        });
        
    }
    
    private void setupStatusSelect(final AbstractSelect statusSelect) {
        if(isNurse && isInTicket() && (Integer) statusSelect.getValue() > 1) {
            statusSelect.removeItem(1);
        }
        if(isNurse && (Integer) statusSelect.getValue() <= 3) {
            statusSelect.removeItem(4);
            statusSelect.removeItem(5);
        }
    }
    
    private Integer getTicketId() {
        
        if(dataId == null) {
            return null;
        }
        
        CustomQuery q = new DBClass().CustomQuery;
        if(ticketSql == null) {
            ticketSql = "select OMTC_ID_TICKET from cust_ordermanager_ticket_contents"
                + "\n " + "inner join cust_projekte_stammdaten_liste on OMTC_ID_PROJECT = cust_projekte_stammdaten_liste.PSLV_ID"
                + "\n " + "where OMTC_ID_DATA = " + dataId + " and PSLV_CODE = '" + projectCode + "'";
        }else {
            return ticketId;
        }
        q.setSqlString(ticketSql);
        
        ticketId = MyUtils.getValueFromItem(MyUtils.getFirstItemFromContainer(q.query()), "OMTC_ID_TICKET", Integer.class);
        return ticketId;
    }
    
    private void clearDatasource(final ComboBox nurseSelect) {
        
        for (Object iid : new CopyOnWriteArrayList<Object>(nurseSelect.getItemIds())) {
            if(!MyUtils.equalsWithNulls(iid, assignedNurse)) {
                nurseSelect.removeItem(iid);
            }
        }
    }
    // {end privatemethods}

    // {section database}
    // ******************
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
