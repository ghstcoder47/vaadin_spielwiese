package archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Krankenschwesternverwaltung.components;

import java.math.BigDecimal;
import java.text.DateFormat;

import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataComponent;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataField;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataFieldConverter;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataFieldRows;
import archenoah.lib.vaadin.Components.Steppers.BigDecimalStepper;
import archenoah.lib.vaadin.CustomConverterFactorys.StringSqlTimestampConverter;

import com.vaadin.ui.PopupDateField;

public class UserEmploymentComponent extends MultiRowDataComponent{
    
   

   
    // {section fields}
    // ****************
    
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserEmploymentComponent.class);
    
    private PopupDateField from;
    private PopupDateField until;
    private BigDecimalStepper fraction;
    
    
    // {end fields}

    // {section constructors}
    // **********************

    public UserEmploymentComponent() {
        
        super();
        
        StringSqlTimestampConverter date = new StringSqlTimestampConverter();
        date.setDateFormat(DateFormat.SHORT);
        
        MultiRowDataFieldConverter dateFieldConverter = new MultiRowDataFieldConverter() {
            @Override
            public Object convertForTable(MultiRowDataField field) {
                return new java.sql.Date(((java.util.Date) field.getField().getValue()).getTime());
            }
        };
        
        
        from = new PopupDateField();
        from.setCaption("from");
        from.setRequired(true);
        MultiRowDataField fromField = new MultiRowDataField(from, "UED_FROM", java.sql.Date.class);
        fromField.setConverter(date);
        fromField.setFieldConverter(dateFieldConverter);
        
        until = new PopupDateField();
        until.setCaption("until");
        MultiRowDataField untilField = new MultiRowDataField(until, "UED_UNTIL", java.sql.Date.class);
        untilField.setConverter(date);
        untilField.setFieldConverter(dateFieldConverter);
      
        
        fraction = new BigDecimalStepper();
        fraction.setCaption("fraction");
        fraction.setRequired(true);
        fraction.setMinValue(BigDecimal.ZERO);
        fraction.setMaxValue(BigDecimal.ONE);
        MultiRowDataField fractionField = new MultiRowDataField(fraction, "UED_FRACTION", BigDecimal.class);
        
        MultiRowDataFieldRows rows = new MultiRowDataFieldRows();
        rows.addFieldRow(fromField, untilField, fractionField);
        
        setup("cust_user_employment_data", "UED_ID", "UED_ID_USER", rows);
        
        setParentId(0);
        
        
        
        // TASK Auto-generated constructor stub
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
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
    
   
}
