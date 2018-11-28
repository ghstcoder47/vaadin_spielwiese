package archenoah.lib.vaadin.Language.i18n;

import com.vaadin.shared.Position;
import com.vaadin.ui.Notification.Type;
/**
 * NotificationBean helper class
 * <pre>
 *   private final static class notifier extends I18nNB{
 *       static I18nNB name_of_notifier = set(ERROR, MC, 0);
 *   }
 * </pre> 
 */
public class I18nNB {

    public Type type;
    public Position position;
    public int delay;
    
    protected static Type ERROR = Type.ERROR_MESSAGE; 
    protected static Type WARNING = Type.WARNING_MESSAGE; 
    protected static Type HUMANIZED = Type.HUMANIZED_MESSAGE; 
    protected static Type TRAY = Type.TRAY_NOTIFICATION; 
    
    protected static Position TL = Position.TOP_LEFT;
    protected static Position TC = Position.TOP_CENTER;
    protected static Position TR = Position.TOP_RIGHT;
    
    protected static Position ML = Position.MIDDLE_LEFT;
    protected static Position MC = Position.MIDDLE_CENTER;
    protected static Position MR = Position.MIDDLE_RIGHT;
    
    protected static Position BL = Position.BOTTOM_LEFT;
    protected static Position BC = Position.BOTTOM_CENTER;
    protected static Position BR = Position.BOTTOM_RIGHT;
    
    /**
     * 
     * @param type (shortened static field available)
     * @param position (shortened static field available)
     * @param delay in seconds
     * @return
     */
    protected static I18nNB set(Type type, Position position, int delay){
        
        I18nNB bean = new I18nNB();
        bean.type = type;
        bean.position = position;
        bean.delay = (delay == -1 ? -1 : delay * 1000);
        return bean;
    }

}
