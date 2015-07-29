package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.lemania.sis.shared.ProfessorProxy;
import java.lang.String;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class UpdateTimeLogEvent extends
		GwtEvent<UpdateTimeLogEvent.UpdateTimeLogHandler> {

	public static Type<UpdateTimeLogHandler> TYPE = new Type<UpdateTimeLogHandler>();
	private ProfessorProxy prof;
	private String courseId;
	private String year;
	private String month;

	public interface UpdateTimeLogHandler extends EventHandler {
		void onUpdateTimeLog(UpdateTimeLogEvent event);
	}

	public interface UpdateTimeLogHasHandlers extends HasHandlers {
		HandlerRegistration addUpdateTimeLogHandler(UpdateTimeLogHandler handler);
	}

	public UpdateTimeLogEvent(ProfessorProxy prof, String courseId, String year,
			String month) {
		this.prof = prof;
		this.courseId = courseId;
		this.year = year;
		this.month = month;
	}

	public ProfessorProxy getProf() {
		return prof;
	}

	public String getCourseId() {
		return courseId;
	}

	public String getYear() {
		return year;
	}

	public String getMonth() {
		return month;
	}

	@Override
	protected void dispatch(UpdateTimeLogHandler handler) {
		handler.onUpdateTimeLog(this);
	}

	@Override
	public Type<UpdateTimeLogHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<UpdateTimeLogHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, ProfessorProxy prof,
			String courseId, String year, String month) {
		source.fireEvent(new UpdateTimeLogEvent(prof, courseId, year, month));
	}
}
