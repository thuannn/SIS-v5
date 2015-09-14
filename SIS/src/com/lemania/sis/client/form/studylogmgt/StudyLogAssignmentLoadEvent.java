package com.lemania.sis.client.form.studylogmgt;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class StudyLogAssignmentLoadEvent extends GwtEvent<StudyLogAssignmentLoadEvent.StudyLogAssignmentLoadHandler> {
    private static Type<StudyLogAssignmentLoadHandler> TYPE = new Type<StudyLogAssignmentLoadHandler>();
    
    public interface StudyLogAssignmentLoadHandler extends EventHandler {
        void onStudyLogAssignmentLoad(StudyLogAssignmentLoadEvent event);
    }
    
    public interface StudyLogAssignmentLoadHandlers extends HasHandlers {
        HandlerRegistration addStudyLogAssignmentLoadHandler(StudyLogAssignmentLoadHandler handler);
    }
    
    private final String message;
    private final String profId;
   
    StudyLogAssignmentLoadEvent(final String message, final String profId) {
        this.message = message;
        this.profId = profId;
    }

    public static Type<StudyLogAssignmentLoadHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final StudyLogAssignmentLoadHandler handler) {
        handler.onStudyLogAssignmentLoad(this);
    }

    @Override
    public Type<StudyLogAssignmentLoadHandler> getAssociatedType() {
        return TYPE;
    }
    
    public String getMessage() {
        return this.message;
    }

	public String getProfId() {
		return profId;
	}
}