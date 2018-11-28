package archenoah.web.normal;

import archenoah.web.normal.Login.frm_login;

import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class Init_Login {

    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Init_Login.class);
    
    
    public Init_Login() {

        VerticalLayout vl = new VerticalLayout();
        vl.setSizeFull();
         vl.addComponent(new frm_login());

        UI.getCurrent().setContent(vl);

    }

}