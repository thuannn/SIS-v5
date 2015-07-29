package com.lemania.sis.client.form.studylogmgt;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class StudyLogClassLoadEvent extends GwtEvent<StudyLogClassLoadEvent.StudyLogClassLoadHandler> {
    private static Type<StudyLogClassLoadHandler> TYPE = new Type<StudyLogClassLoadHandler>();
    
    public interface StudyLogClassLoadHandler extends EventHandler {
        void onStudyLogClassLoad(StudyLogClassLoadEvent event);
    }
    
    public interface StudyLogClassLoadHandlers extends HasHandlers {
        HandlerRegistration addStudyLogClassLoadHandler(StudyLogClassLoadHandler handler);
    }
    
    private final String message;
    private String profId;
    private String subjectId;
   
    StudyLogClassLoadEvent(final String message) {
        this.message = message;
    }
    
    StudyLogClassLoadEvent(final String message, String profId, String subjectId) {
        this.message = message;
        this.profId = profId;
        this.subjectId = subjectId;
    }

    public static Type<StudyLogClassLoadHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final StudyLogClassLoadHandler handler) {
        handler.onStudyLogClassLoad(this);
    }

    @Override
    public Type<StudyLogClassLoadHandler> getAssociatedType() {
        return TYPE;
    }
    
    public String getMessage() {
        return this.message;
    }

	public String getProfId() {
		return profId;
	}

	public String getSubjectId() {
		return subjectId;
	}
}