package archenoah.lib.vaadin.Language.i18n;

import com.vaadin.server.Resource;

import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
/**
 * NotificationBean helper class
 * <pre>
 *   private final static class notifier extends I18nNB{
 *       static I18nNB name_of_notifier = set(ERROR, MC, 0);
 *   }
 * </pre> 
 */
public class I18nMB {

    public Icon icon;
    public Resource resource;
    public ButtonId[] buttons;
    public String width;
    public String height;
    
    protected static I18nMB set(Icon icon, ButtonId... buttons){
        return set(icon, null, null, buttons);
    }
    
    protected static I18nMB set(Resource resource, ButtonId... buttons){
        return set(resource, null, null, buttons);
    }
   
    protected static I18nMB set(Icon icon, String height, String width, ButtonId... buttons){
        
        I18nMB bean = new I18nMB();
        bean.icon = icon;
        bean.buttons = buttons;
        bean.height = height;
        bean.width = width;
        return bean;
    }
    
    protected static I18nMB set(Resource resource, String height, String width, ButtonId... buttons){
        
        I18nMB bean = new I18nMB();
        bean.resource = resource;
        bean.buttons = buttons;
        bean.height = height;
        bean.width = width;
        return bean;
    }
    
}
