package archenoah.lib.custom.hotkeys;

import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractField;

public class ResetHotkey extends ShortcutListener{

    public ResetHotkey() {

        super("reset", null, KeyCode.X, ModifierKey.CTRL);
        
    }

    private ResetHotkey(String caption, int keyCode, int... modifierKeys) {
        super(caption, keyCode, modifierKeys);
    }

    private ResetHotkey(String shorthandCaption, int... modifierKeys) {
        super(shorthandCaption, modifierKeys);
    }

    private ResetHotkey(String caption, Resource icon, int keyCode, int... modifierKeys) {
        super(caption, icon, keyCode, modifierKeys);
    }

    private ResetHotkey(String shorthandCaption) {
        super(shorthandCaption);
    }



    @Override
    public void handleAction(Object sender, Object target) {
        
        if(target instanceof AbstractField<?>) {
            AbstractField<?> fl = ((AbstractField<?>) target);
            fl.setValue(null);
            fl.commit();
        }
    }

}
