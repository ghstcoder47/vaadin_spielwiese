package archenoah.lib.custom.hotkeys;

import com.vaadin.event.Action.Listener;
import com.vaadin.event.ShortcutAction;

public abstract class GlobalHotkey extends ShortcutAction implements Listener{

    public GlobalHotkey(int kc, int... m) {
        super(null, kc, m);
    }

    
}
