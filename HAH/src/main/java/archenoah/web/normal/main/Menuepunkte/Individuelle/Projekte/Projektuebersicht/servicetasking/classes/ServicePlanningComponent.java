package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.classes;

import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataComponent;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataField;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataFieldRows;
import archenoah.lib.vaadin.Components.Steppers.IntStepper;
import archenoah.lib.vaadin.Language.i18n.I18nConverter;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;

public class ServicePlanningComponent extends MultiRowDataComponent{

    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    
    // {section fields}
    // ****************
    
    private ComboBox type;
    private IntStepper days;
    private TextField comment;

    // {end fields}

    // {section constructors}
    // **********************
    public ServicePlanningComponent() {
        super();

        type = new ComboBox();
        type.setCaption("Typ");
        type.setRequired(true);
        type.setWidth("100%");
        MultiRowDataField typeField = new MultiRowDataField(type, "STP_TYPE", String.class);
        typeField.setDataSource(new I18nConverter("cust_protokoll_servicetasking_master_types", "STXT_ID", "STXT_KEY").getLocalizedContainer(),
            "STXT_KEY", I18nConverter.CAPTION_PROPERTY);
        typeField.setFieldRatio(1);
        
        days = new IntStepper();
        days.setCaption("Tage");
        days.setMinValue(1);
        days.setRequired(true);
        days.setWidth("80px");
        MultiRowDataField daysField = new MultiRowDataField(days, "STP_DAYS", Integer.class);
        daysField.setFieldRatio(0);
        daysField.setDefaultValue(0);
        
        comment = new TextField();
        comment.setCaption("Kommentar");
        comment.setRequired(true);
        comment.setNullRepresentation("");
        comment.setWidth("100%");
        MultiRowDataField commentField = new MultiRowDataField(comment, "STP_COMMENT", String.class);
        
        
        // init multirow
        MultiRowDataFieldRows rows = new MultiRowDataFieldRows();
        rows.addFieldRow(daysField, typeField);
        rows.addFieldRow(commentField);
        
        setup(
                "cust_protokoll_servicetasking_planning"
                , "STP_ID"
                , "STP_ID_PARENT"
                , rows);
//        setVisibleColumns(new Object[] {"PLE_ID_PRODUCT", "PLE_ID_SIZE", "PLE_SIZE_COUNT", "PLE_QUANTITY"});
        setParentId(0);

    }

    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    

    private void setListeners() {
       
    }
    
    // {end privatemethods}

    // {section database}
    // ******************
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
    
}
