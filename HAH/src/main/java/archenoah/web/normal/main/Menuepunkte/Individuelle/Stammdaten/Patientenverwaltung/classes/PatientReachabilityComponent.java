package archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Patientenverwaltung.classes;

import java.util.Locale;

import archenoah.lib.vaadin.Components.Fields.CustomTimePicker;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataComponent;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataField;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataFieldRows;
import archenoah.lib.vaadin.CustomConverterFactorys.WeekdayConverter;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class PatientReachabilityComponent extends MultiRowDataComponent {

    // {section fields}
    // ****************
    
    private ComboBox weekday;
    private CustomTimePicker from;
    private CustomTimePicker until;
    
    // {end fields}

    // {section constructors}
    // **********************
    public PatientReachabilityComponent() {
        super();
        
        weekday = new ComboBox();
        weekday.setCaption("Wochentag");
        weekday.setRequired(true);
        weekday.setWidth("160px");
        fillWeekdays();
        MultiRowDataField weekdayField = new MultiRowDataField(weekday, "PR_WEEKDAY", Integer.class);
        weekdayField.setConverter(new WeekdayConverter());
        weekdayField.setFieldRatio(2);
        weekdayField.setRatio(2);
        
        from = new CustomTimePicker();
        from.setCaption("Von");
        from.setWidth("80px");
        MultiRowDataField fromField = new MultiRowDataField(from, "PR_FROM", String.class);
        fromField.setConverter(new DummyConverter());
        fromField.setFieldRatio(1);
        fromField.setRatio(1);
        
        until = new CustomTimePicker();
        until.setCaption("Bis");
        until.setWidth("80px");
        MultiRowDataField untilField = new MultiRowDataField(until, "PR_UNTIL", String.class);
        untilField.setConverter(new DummyConverter());
        untilField.setFieldRatio(1);
        untilField.setRatio(1);
        

        // init multirow
        MultiRowDataFieldRows rows = new MultiRowDataFieldRows();
        rows.addFieldRow(weekdayField, fromField, untilField);
        
        setup(
                "cust_patient_reachability"
                , "PR_ID"
                , "PR_ID_PATIENT"
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
    private void fillWeekdays() {
        
        WeekdayConverter wsc = new WeekdayConverter();
        
        for (int i = 1; i <= 7; i++) {
            weekday.addItem(i);
            weekday.setItemCaption(i, wsc.convertToPresentation(i, String.class, UI.getCurrent().getLocale()));
        }

        
    }
    // {end privatemethods}

    // {section database}
    // ******************
    
    
    protected class DummyConverter implements Converter<String, String> {

        @Override
        public String convertToModel(String value, Class<? extends String> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
            // TASK Auto-generated method stub
            return value;
        }

        @Override
        public String convertToPresentation(String value, Class<? extends String> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
            // TASK Auto-generated method stub
            return value;
        }

        @Override
        public Class<String> getModelType() {
            // TASK Auto-generated method stub
            return null;
        }

        @Override
        public Class<String> getPresentationType() {
            // TASK Auto-generated method stub
            return null;
        }
    };
    
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
