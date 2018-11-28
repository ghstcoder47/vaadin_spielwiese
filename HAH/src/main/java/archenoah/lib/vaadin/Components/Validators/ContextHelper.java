package archenoah.lib.vaadin.Components.Validators;

import org.vaadin.jonatan.contexthelp.ContextHelp;

import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.UI;

public class ContextHelper {

    public ContextHelper(AbstractTextField field, String help) {
        final ContextHelp contextHelp = new ContextHelp();
        contextHelp.extend(UI.getCurrent());
        contextHelp.setFollowFocus(true);
        contextHelp.setHideOnBlur(true);
        contextHelp.hideHelp();
        contextHelp.addHelpForComponent(field, help);

        field.addBlurListener(new BlurListener() {

            @Override
            public void blur(BlurEvent event) {
                contextHelp.hideHelp();
            }
        });
    }
}
