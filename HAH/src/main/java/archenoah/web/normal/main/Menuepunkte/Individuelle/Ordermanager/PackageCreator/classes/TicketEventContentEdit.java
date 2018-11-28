package archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.PackageCreator.classes;

import archenoah.lib.custom.MyUtils;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.pop.form.PoPInsertEdit;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.replagal.form.replagal_insert_edit;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.vpriv.form.vpriv_insert_edit;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

public abstract class TicketEventContentEdit {

    TicketEvent tev;
    
    public TicketEventContentEdit(TicketEvent tev) {
        this.tev = tev;
    }
    
    public abstract void onUpdate();
    public abstract void onClose();
    
    public void open(){
        initEdit();
    }
    
    private void initEdit(){
        
        Item item = tev.getItem();
        String project = null;
        Integer dataId = null;
        try {
            project = MyUtils.getValueFromItem(item, "code", String.class);
            dataId = MyUtils.getValueFromItem(item, "project_data_id", Integer.class);
        } catch (Exception e) {
            System.out.println("error in TicketEventContentEdit!");
        }
        
        switch (project) {
        case "REP":
            
            openRep(dataId);
            break;
            
        case "VPRIV": 
            
            openVpriv(dataId);
            break;
        
        case "POP": 
            
            openPop(dataId);
            break;
            
        default:
            break;
        }
        
    }

    
    private void openRep(Integer dataId){
        final replagal_insert_edit pie = new replagal_insert_edit(new Button(), dataId.toString());

        pie.Set_Berrechtigung_Database();
        pie.Init_Class_Window();
        pie.getWindow().addCloseListener(new CloseListener() {
            
            @Override
            public void windowClose(CloseEvent e) {
                
                if(pie.wasSaved()){
                    onUpdate();
                }
                
            }
        });
    }
    
    private void openVpriv(Integer dataId){
        final vpriv_insert_edit pie = new vpriv_insert_edit(new Button(), dataId.toString());

        pie.Set_Berrechtigung_Database();
        pie.Init_Class_Window();
        pie.getWindow().addCloseListener(new CloseListener() {
            
            @Override
            public void windowClose(CloseEvent e) {
                
                if(pie.wasSaved()){
                    onUpdate();
                }
                
            }
        });
    }
    
    private void openPop(Integer dataId){
        final PoPInsertEdit pie = new PoPInsertEdit(new Button(), dataId.toString());

        pie.setPermissionsFromDatabase();
        pie.init();
        pie.getWindow().addCloseListener(new CloseListener() {
            
            @Override
            public void windowClose(CloseEvent e) {
                
                if(pie.wasSaved()){
                    onUpdate();
                }
                
            }
        });
    }
}

