package archenoah.lib.vaadin.valchecker;

import java.util.Map;
import java.util.Set;

import com.vaadin.server.Page;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Notification;

public class val_checker4 {

    public Map<Object, String> vala;
    private Set<Object> keys;
    private Boolean pr = true;

    public val_checker4(Map<Object, String> val) {
        // TODO Automatisch generierter Konstruktorstub
        vala = val;

    }

    public boolean Is_Valid() {
        keys = vala.keySet();
        
        String caption = "?";
        
        for (Object singleKey : keys) {
            
            if(!(singleKey instanceof AbstractField)) {
                continue;
            }
            
            AbstractField field = (AbstractField) singleKey;
            
            if(!field.isValid()){
                pr = false;
                caption = field.getCaption() != null ? field.getCaption() : vala.get(singleKey);
                caption = (caption.equals("")) ? "?" : caption;
                break;
            }
            
        }
        
        if(!pr){
            
            Notification notif = new Notification(
                    "",
                    caption,
                    Notification.Type.WARNING_MESSAGE);
                notif.show(Page.getCurrent());
            
        }
        
        return pr;
    }

}
