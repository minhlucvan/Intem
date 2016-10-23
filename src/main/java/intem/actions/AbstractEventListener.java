package intem.actions;

import intem.actions.Event;
import intem.actions.EventListener;

/**
 * Created by minh on 25/09/16.
 */
public abstract class AbstractEventListener implements EventListener {
    protected boolean internal;
    protected int untilEvent;

    public AbstractEventListener(){
        this.internal = true;
        this.untilEvent = Event.END_DEFAULT_EVENT;
    }
}
