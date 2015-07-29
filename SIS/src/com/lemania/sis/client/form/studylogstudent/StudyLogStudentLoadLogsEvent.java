package com.lemania.sis.client.form.studylogstudent;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class StudyLogStudentLoadLogsEvent extends GwtEvent<StudyLogStudentLoadLogsEvent.StudyLogStudentLoadLogsHandler> {
    private static Type<StudyLogStudentLoadLogsHandler> TYPE = new Type<StudyLogStudentLoadLogsHandler>();
    
    public interface StudyLogStudentLoadLogsHandler extends EventHandler {
        void onStudyLogStudentLoadLogs(StudyLogStudentLoadLogsEvent event);
    }
    
    public interface StudyLogStudentLoadLogsHandlers extends HasHandlers {
        HandlerRegistration addStudyLogStudentLoadLogsHandler(StudyLogStudentLoadLogsHandler handler);
    }
    
    private final String message;
    private final String subjectId;
    private final String classId;
    private final String dateFrom;
    private final String dateTo;
   
    public String getDateFrom() {
		return dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	StudyLogStudentLoadLogsEvent(final String message, String subjectId, String classId, String dateFrom, String dateTo) {
        this.message = message;
        this.subjectId = subjectId;
        this.classId = classId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public static Type<StudyLogStudentLoadLogsHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final StudyLogStudentLoadLogsHandler handler) {
        handler.onStudyLogStudentLoadLogs(this);
    }

    @Override
    public Type<StudyLogStudentLoadLogsHandler> getAssociatedType() {
        return TYPE;
    }
    
    public String getMessage() {
        return this.message;
    }

	public String getSubjectId() {
		return subjectId;
	}

	public String getClassId() {
		return classId;
	}

}