package archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Ärztverwaltung.classes;

import java.text.DateFormat;

import com.vaadin.data.Container;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;

import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataComponent;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataField;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataFieldConverter;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataFieldRows;
import archenoah.lib.vaadin.CustomConverterFactorys.StringSqlTimestampConverter;

@SuppressWarnings("serial")
public class ArztMonitoringOrganisationComponent extends MultiRowDataComponent{
	// {section fields}
	// ****************
	
    private ComboBox organisation;
    private TextField position;
    private PopupDateField dateStart;
    private PopupDateField dateUntil;
	
	// {end fields}

	// {section constructors}
	// **********************

	public ArztMonitoringOrganisationComponent(){
		super();
				
        StringSqlTimestampConverter date = new StringSqlTimestampConverter();
        date.setDateFormat(DateFormat.SHORT);
        
        MultiRowDataFieldConverter dateFieldConverter = new MultiRowDataFieldConverter() {
            @Override
            public Object convertForTable(MultiRowDataField field) {
                return new java.sql.Date(((java.util.Date) field.getField().getValue()).getTime());
            }

        };
        
        organisation = new ComboBox();
        organisation.setCaption("Organisation");
        organisation.setRequired(true);
        organisation.setWidth("160px");
        MultiRowDataField organisationField = new MultiRowDataField(organisation, "VAO_ORGANISATION_ID", Integer.class);
        organisationField.setDataSource(dbGetOrganisation(), "OSL_ID", "OSL_NAME");
        organisationField.setFieldRatio(2);
        organisationField.setRatio(2);
        
        position = new TextField();
        position.setCaption("Position");
        position.setRequired(true);
        position.setWidth("160px");
        position.setNullRepresentation("");
        MultiRowDataField positionField = new MultiRowDataField(position, "VAO_POSITION", String.class);
        positionField.setRatio(2);
        
        dateStart = new PopupDateField();
        dateStart.setCaption("Startdatum");
        dateStart.setRequired(true);
        dateStart.setWidth("80px");
        MultiRowDataField dateFieldStart = new MultiRowDataField(dateStart, "VAO_DATE_START", java.sql.Date.class);
        dateFieldStart.setFieldRatio(1);
        dateFieldStart.setRatio(1);
        dateFieldStart.setConverter(date);
        dateFieldStart.setFieldConverter(dateFieldConverter);
        
        dateUntil = new PopupDateField();
        dateUntil.setCaption("Enddatum");
        dateUntil.setRequired(false);
        dateUntil.setWidth("80px");
        MultiRowDataField dateFieldUntil = new MultiRowDataField(dateUntil, "VAO_DATE_UNTIL", java.sql.Date.class);
        dateFieldUntil.setFieldRatio(1);
        dateFieldUntil.setRatio(1);
        dateFieldUntil.setConverter(date);
        dateFieldUntil.setFieldConverter(dateFieldConverter);
        

        // init multirow
        MultiRowDataFieldRows rows = new MultiRowDataFieldRows();
        rows.addFieldRow(organisationField, positionField, dateFieldStart, dateFieldUntil);
        
        setup(
                "cust_verk_arzt_organisation"
                , "VAO_ID"
                , "VAO_ARZT_ID"
                , rows);
        
        //setParentId(0);
        
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
	
    private Container dbGetOrganisation() {
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select OSL_ID, OSL_NAME from cust_organisation_stammdaten_liste";
        q.setSqlString(sql);
        return q.query();
    }
	
	// {end database}

	// {section layout}
	// ****************
	// {end layout}
}
