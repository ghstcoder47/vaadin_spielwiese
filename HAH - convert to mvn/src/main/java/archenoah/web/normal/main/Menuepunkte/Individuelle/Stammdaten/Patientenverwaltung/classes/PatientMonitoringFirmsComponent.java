package archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Patientenverwaltung.classes;

import java.text.DateFormat;

import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataComponent;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataField;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataFieldConverter;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataFieldRows;
import archenoah.lib.vaadin.CustomConverterFactorys.StringSqlTimestampConverter;

import com.vaadin.data.Container;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class PatientMonitoringFirmsComponent extends MultiRowDataComponent {

    // {section fields}
    // ****************
    
    private ComboBox firm;
    private PopupDateField dateStart;
    private TextField code;
    
    // {end fields}

    // {section constructors}
    // **********************
    public PatientMonitoringFirmsComponent() {
        super();
        
        StringSqlTimestampConverter date = new StringSqlTimestampConverter();
        date.setDateFormat(DateFormat.SHORT);
        
        MultiRowDataFieldConverter dateFieldConverter = new MultiRowDataFieldConverter() {
            @Override
            public Object convertForTable(MultiRowDataField field) {
                return new java.sql.Date(((java.util.Date) field.getField().getValue()).getTime());
            }
        };
        
        firm = new ComboBox();
        firm.setCaption("Firma");
        firm.setRequired(true);
        firm.setWidth("160px");
        MultiRowDataField firmField = new MultiRowDataField(firm, "PMF_KEY_FIRM", String.class);
        firmField.setDataSource(dbGetFirms(), "MF_KEY", "MF_NAME");
        firmField.setFieldRatio(2);
        firmField.setRatio(2);
        
        dateStart = new PopupDateField();
        dateStart.setCaption("Startdatum");
        dateStart.setRequired(true);
        dateStart.setWidth("80px");
        MultiRowDataField dateField = new MultiRowDataField(dateStart, "PMF_DATE_FROM", java.sql.Date.class);
        dateField.setFieldRatio(1);
        dateField.setRatio(1);
        dateField.setConverter(date);
        dateField.setFieldConverter(dateFieldConverter);
        
        code = new TextField();
        code.setCaption("code");
        code.setRequired(true);
        code.setNullRepresentation("");
        MultiRowDataField codeField = new MultiRowDataField(code, "PMF_CODE", String.class);
        codeField.setFieldRatio(2);
        codeField.setRatio(2);
        

        // init multirow
        MultiRowDataFieldRows rows = new MultiRowDataFieldRows();
        rows.addFieldRow(firmField, dateField, codeField);
        
        setup(
                "cust_patient_monitoring_firms"
                , "PMF_ID"
                , "PMF_ID_PATIENT"
                , rows);
//        setVisibleColumns(new Object[] {"PLE_ID_PRODUCT", "PLE_ID_SIZE", "PLE_SIZE_COUNT", "PLE_QUANTITY"});
//        mainLayout.addComponent(multiRow);
//        setSizeFull();
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
    // {end privatemethods}

    // {section database}
    // ******************
    
    private Container dbGetFirms() {
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select * from cust_monitoring_firms";
        q.setSqlString(sql);
        return q.query();
    }
    
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
