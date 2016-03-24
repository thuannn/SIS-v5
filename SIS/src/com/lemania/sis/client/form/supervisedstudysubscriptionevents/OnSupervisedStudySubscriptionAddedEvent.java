package com.lemania.sis.client.form.supervisedstudysubscriptionevents;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class OnSupervisedStudySubscriptionAddedEvent extends GwtEvent<OnSupervisedStudySubscriptionAddedEvent.OnSupervisedStudySubscriptionAddedHandler> {
    private static Type<OnSupervisedStudySubscriptionAddedHandler> TYPE = new Type<OnSupervisedStudySubscriptionAddedHandler>();
    
    public interface OnSupervisedStudySubscriptionAddedHandler extends EventHandler {
        void onOnSupervisedStudySubscriptionAdded(OnSupervisedStudySubscriptionAddedEvent event);
    }
    
    public interface OnSupervisedStudySubscriptionAddedHandlers extends HasHandlers {
        HandlerRegistration addOnSupervisedStudySubscriptionAddedHandler(OnSupervisedStudySubscriptionAddedHandler handler);
    }
    
    private final String message;
   
    public OnSupervisedStudySubscriptionAddedEvent(final String message) {
        this.message = message;
    }

    public static Type<OnSupervisedStudySubscriptionAddedHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final OnSupervisedStudySubscriptionAddedHandler handler) {
        handler.onOnSupervisedStudySubscriptionAdded(this);
    }

    @Override
    public Type<OnSupervisedStudySubscriptionAddedHandler> getAssociatedType() {
        return TYPE;
    }
    
    public String getMessage() {
        return this.message;
    }
}