package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.classes;

import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.forsteo.Form.form_forsteo_insert_edit;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.pop.form.PoPInsertEdit;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.replagal.form.replagal_insert_edit;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.form.ServiceTaskingInsertEdit;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.vpriv.form.vpriv_insert_edit;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;

public class ProjectData {

    // {section fields}
    // ****************
    public static enum ProjectCode{
        FOR,
        REP,
        VPRIV,
        POP,
        SA
    }
    private CloseListener closeListener;
    
    
    // {end fields}

    // {section constructors}
    // **********************
    public ProjectData(CloseListener closeListener) {
        this.closeListener = closeListener;
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    
    public Window open(MenuItem selected, String projectCodeString, Integer dataId, Boolean readOnly) {
        return openData(selected, ProjectCode.valueOf(projectCodeString), dataId, readOnly);
    }
    
    public Window open(MenuItem selected, ProjectCode projectCode, Integer dataId, Boolean readOnly) {
        return openData(selected, projectCode, dataId, readOnly);
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    private Window openData(MenuItem selected, ProjectCode projectCode, Integer dataId, Boolean readonly) {
        
        Window win = null;
        
        switch (projectCode) {

        case FOR:

            form_forsteo_insert_edit forsteo = new form_forsteo_insert_edit(selected, dataId.toString());
            forsteo.Init_Class_Window();
            if (readonly) {
                forsteo.Set_Berrechtigung_Custom(1, 0, 0, 0);
            }else {
                forsteo.Set_Berrechtigung_Database();
            }
            forsteo.getWindow().addCloseListener(closeListener);
            win = forsteo.getWindow();

            break;

        case REP:

            replagal_insert_edit replagal = new replagal_insert_edit(selected, dataId.toString());
            replagal.Init_Class_Window();
            if (readonly) {
                replagal.Set_Berrechtigung_Custom(1, 0, 0, 0);
            }else {
                replagal.Set_Berrechtigung_Database();
            }
            replagal.getWindow().addCloseListener(closeListener);
            win = replagal.getWindow();

            break;

        case VPRIV:

            vpriv_insert_edit vpriv = new vpriv_insert_edit(selected, dataId.toString());
            vpriv.Init_Class_Window();
            if (readonly) {
                vpriv.Set_Berrechtigung_Custom(1, 0, 0, 0);
            }else {
                vpriv.Set_Berrechtigung_Database();
            }
            vpriv.getWindow().addCloseListener(closeListener);
            win = vpriv.getWindow();

            break;

        case POP:

            PoPInsertEdit pop = new PoPInsertEdit(selected, dataId.toString());
            if (readonly) {
                pop.setPermissions(0, 1, 0, 0);
            } else {
                pop.setPermissionsFromDatabase();
            }
            pop.init();
            pop.getWindow().addCloseListener(closeListener);
            win = pop.getWindow();

            break;
            
        case SA:

            ServiceTaskingInsertEdit sa = new ServiceTaskingInsertEdit(selected, dataId.toString());
            if (readonly) {
                sa.setPermissions(0, 1, 0, 0);
            } else {
                sa.setPermissionsFromDatabase();
            }
            sa.init();
            sa.getWindow().addCloseListener(closeListener);
            win = sa.getWindow();

            break;
            
        default:
            break;
        }

        return win;
    }
    // {end privatemethods}

    // {section database}
    // ******************
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
