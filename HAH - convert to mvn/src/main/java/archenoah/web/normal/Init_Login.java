package archenoah.web.normal;


import archenoah.web.normal.Login.frm_login;

import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class Init_Login {
    
    public Init_Login() {

        VerticalLayout vl = new VerticalLayout();
        vl.setSizeFull();
        vl.addComponent(new frm_login());
        
        UI.getCurrent().setContent(vl);
        
    }
    
    
}