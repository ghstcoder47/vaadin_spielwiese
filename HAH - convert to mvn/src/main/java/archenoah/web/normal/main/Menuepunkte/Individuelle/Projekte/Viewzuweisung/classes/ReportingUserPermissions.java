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

public class ReportingUserPermissions extends MultiRowDataComponent{
	// {section fields}
	// ****************
	
	private ComboBox user;
		
	// {end fields}

	// {section constructors}
	// **********************

	public ReportingUserPermissions(){
		super();
        
        user = new ComboBox();
        user.setCaption("User");
        user.setWidth("200px");
        user.setImmediate(true);
        user.setRequired(true);
        MultiRowDataField userField = new MultiRowDataField(user, "CRA_ID_USER");
        userField.setDataSource(dbGetUsers(), "AUL_ID", "NAME");
        
        // init multirow
        MultiRowDataFieldRows rows = new MultiRowDataFieldRows();
        rows.addFieldRow(userField);
        
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
        addContainerFilter(new Not(new IsNull("CRA_ID_USER")));
    }
	
	// {end privatemethods}

	// {section database}
	// ******************
	
    private Container dbGetUsers() {
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "SELECT"
			 + "\n " + "	AUL_ID"
			 + "\n " + "	, CONCAT(AUL_VORNAME, ' ', AUL_NAME) AS 'NAME'"
			 + "\n " + "FROM"
			 + "\n " + "	cms_auth_stammdaten_user"
			 + "\n " + "WHERE"
			 + "\n " + "	AUL_USERNAME IS NOT NULL "
			 + "\n " + "	AND AUL_USERNAME != ''"
			 + "\n " + "ORDER BY"
			 + "\n " + "	AUL_NAME";
        q.setSqlString(sql);
        return q.query();
    }
    
	// {end database}

	// {section layout}
	// ****************
	// {end layout}
}
