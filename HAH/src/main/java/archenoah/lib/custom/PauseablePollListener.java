package archenoah.lib.custom;

import com.vaadin.event.UIEvents.PollEvent;
import com.vaadin.event.UIEvents.PollListener;
import com.vaadin.server.ClientConnector.DetachEvent;
import com.vaadin.server.ClientConnector.DetachListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class PauseablePollListener implements PollListener{

    // {section fields}
    // ****************
    boolean pause = false;
    boolean suspendInBackground = true;
    
    private TabSheet tab;
    private AbstractComponent component;
    private PollListener listener;
    
    // {end fields}
    

    // {section constructors}
    // **********************
    /**
     * use getListener method instead!
     * @param tab
     * @param component
     * @param listener
     */
    public PauseablePollListener(TabSheet tab, AbstractComponent component, PollListener listener) {
        
        if(listener == null) {
            throw new IllegalArgumentException("listener can not be null!");
        }
        
        this.tab = tab;
        this.component = component;
        this.listener = listener;
    }
    
    /**
     * used for dummy calls
     */
    public PauseablePollListener() {
    }
    
    /**
     * sets up all the required listener and detach behaviours
     * @param tab
     * @param component
     * @param listener
     * @return
     */
    public static PauseablePollListener getListener(TabSheet tab, AbstractComponent component, PollListener listener) {
        
        final PauseablePollListener ppl = new PauseablePollListener(tab, component, listener);
        UI.getCurrent().getUI().addPollListener(ppl);
        
        tab.getSelectedTab().addDetachListener(new DetachListener() {
            @Override
            public void detach(DetachEvent event) {
                UI.getCurrent().getUI().removePollListener(ppl);
            }
        });
        
        return ppl;
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    
    @Override
    public void poll(PollEvent event) {
        if(pause) {
            return;
        }
        
        if(suspendInBackground && tab != null && !tab.isRendered(component)) {
            return;
        }
        
        listener.poll(event);
    }
    
    public void pause() {
        this.pause = true;
    }
    
    public void resume() {
        pause = false;
    }
    
    public void resumeAndPoll() {
        resume();
        poll(null);
    }
    
    public void suspendInBackground(boolean suspend) {
        this.suspendInBackground = suspend;
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    // {end privatemethods}

    // {section database}
    // ******************
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
