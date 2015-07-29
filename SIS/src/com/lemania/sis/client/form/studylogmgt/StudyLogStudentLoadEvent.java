package com.lemania.sis.client.form.studylogmgt;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class StudyLogStudentLoadEvent extends GwtEvent<StudyLogStudentLoadEvent.StudyLogStudentLoadHandler> {
    private static Type<StudyLogStudentLoadHandler> TYPE = new Type<StudyLogStudentLoadHandler>();
    
    public interface StudyLogStudentLoadHandler extends EventHandler {
        void onStudyLogStudentLoad(StudyLogStudentLoadEvent event);
    }
    
    
    private final String message;
    private String profId;
    private String subjectId;
    private String classId;
   
    StudyLogStudentLoadEvent(final String message) {
        this.message = message;
    }
    
    StudyLogStudentLoadEvent(final String message, String profId, String subjectId, String classId) {
        this.message = message;
        this.profId = profId;
        this.subjectId = subjectId;
        this.classId = classId;
    }

    public static Type<StudyLogStudentLoadHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final StudyLogStudentLoadHandler handler) {
        handler.onStudyLogStudentLoad(this);
    }

    @Override
    public Type<StudyLogStudentLoadHandler> getAssociatedType() {
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

	public String getClassId() {
		return classId;
	}
}