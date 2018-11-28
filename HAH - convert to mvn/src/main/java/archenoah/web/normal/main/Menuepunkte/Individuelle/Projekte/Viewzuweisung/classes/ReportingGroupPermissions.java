package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Viewzuweisung.classes;

import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataComponent;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataField;
import archenoah.lib.vaadin.Components.MultiRowData.MultiRowDataFieldRows;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.IsNull;
import com.vaadin.data.util.filter.Not;
import com.vaadin.ui.ComboBox;

public class ReportingGroupPermissions extends MultiRowDataComponent{
	// {section fields}
	// ****************
	
	private ComboBox group;
		
	// {end fields}

	// {section constructors}
	// **********************

	public ReportingGroupPermissions(){
		super();
		
        group = new ComboBox();
        group.setCaption("Group");
        group.setWidth("200px");
        group.setImmediate(true);
        group.setRequired(true);
        MultiRowDataField groupField = new MultiRowDataField(group, "CRA_ID_GROUP");
        groupField.setDataSource(dbGetGroups(), "AGL_ID", "AGL_NAME");
        
        // init multirow
        MultiRowDataFieldRows rows = new MultiRowDataFieldRows();
        rows.addFieldRow(groupField);
        
        setup("cust_report_assignments", "CRA_ID", "CRA_ID_VIEW", rows);
        
        setEventListeners();
        setFilter();
        
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
	
	private void setEventListeners(){	
		
	}
	
	private void setFilter() {
	    addContainerFilter(new Not(new IsNull("CRA_ID_GROUP")));
	}
	
	// {end privatemethods}

	// {section database}
	// ******************
	
	private Container dbGetGroups() {
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "SELECT"
			 + "\n " + "	AGL_ID"
			 + "\n " + "	, AGL_NAME"
			 + "\n " + "FROM"
			 + "\n " + "	cms_auth_stammdaten_group"
			 + "\n " + "WHERE"
			 + "\n " + "	AGL_ID != 1"
			 + "\n " + "ORDER BY"
			 + "\n " + "	AGL_NAME";
        q.setSqlString(sql);
        return q.query();
    }
	
    
	// {end database}

	// {section layout}
	// ****************
	// {end layout}
}
