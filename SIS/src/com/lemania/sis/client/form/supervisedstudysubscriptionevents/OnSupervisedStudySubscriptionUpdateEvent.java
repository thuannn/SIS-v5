package com.lemania.sis.client.form.supervisedstudysubscriptionevents;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.lemania.sis.shared.coursesubscription.CourseSubscriptionProxy;

public class OnSupervisedStudySubscriptionUpdateEvent extends GwtEvent<OnSupervisedStudySubscriptionUpdateEvent.OnSupervisedStudySubscriptionUpdateHandler> {
    private static Type<OnSupervisedStudySubscriptionUpdateHandler> TYPE = new Type<OnSupervisedStudySubscriptionUpdateHandler>();
    
    public interface OnSupervisedStudySubscriptionUpdateHandler extends EventHandler {
        void onOnSupervisedStudySubscriptionUpdate(OnSupervisedStudySubscriptionUpdateEvent event);
    }
    
    public interface OnSupervisedStudySubscriptionUpdateHandlers extends HasHandlers {
        HandlerRegistration addOnSupervisedStudySubscriptionUpdateHandler(OnSupervisedStudySubscriptionUpdateHandler handler);
    }
    
    private final String message;
    
    //
    private CourseSubscriptionProxy subscription;
   
    public OnSupervisedStudySubscriptionUpdateEvent(final String message) {
        this.message = message;
    }
    
    public OnSupervisedStudySubscriptionUpdateEvent(CourseSubscriptionProxy subs) {
    	this.message = "";
        this.subscription = subs;
    }

    public static Type<OnSupervisedStudySubscriptionUpdateHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final OnSupervisedStudySubscriptionUpdateHandler handler) {
        handler.onOnSupervisedStudySubscriptionUpdate(this);
    }

    @Override
    public Type<OnSupervisedStudySubscriptionUpdateHandler> getAssociatedType() {
        return TYPE;
    }
    
    public String getMessage() {
        return this.message;
    }

	public CourseSubscriptionProxy getSubscription() {
		return subscription;
	}

	public void setSubscription(CourseSubscriptionProxy subscription) {
		this.subscription = subscription;
	}
}