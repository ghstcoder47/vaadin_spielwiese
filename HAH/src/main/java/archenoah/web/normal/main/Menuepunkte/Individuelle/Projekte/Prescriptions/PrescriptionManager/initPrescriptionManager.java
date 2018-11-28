package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Prescriptions.PrescriptionManager;


import archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Prescriptions.PrescriptionManager.form.PrescriptionManagerDataView;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

public class initPrescriptionManager {


    private TabSheet tab;
    public initPrescriptionManager(MenuItem menü)
    {
        tab = (TabSheet) UI.getCurrent().getSession().getSession().getAttribute("tab");
        
        
        if(Prüfungtab(menü.getText())== true)
        {
            return;
        }
        
        PrescriptionManagerDataView test = new PrescriptionManagerDataView(menü);
        test.setPermissionsFromDatabase();
        test.init(tab);
            
    }
    
    private boolean Prüfungtab(String Name)
    {
        int Count = tab.getComponentCount();
        
        
        for(int i=0;i<Count;i++)
        {   
            if(tab.getTab(i).getCaption()==Name)
            {
        
                tab.setSelectedTab(i);
                
                return true;
            }
            
        }
        
        return false;   
    }
}
