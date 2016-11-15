package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.lemania.sis.client.CurrentSchool;
import com.google.gwt.event.shared.HandlerRegistration;

public class DrawSchoolInterfaceEvent extends
		GwtEvent<DrawSchoolInterfaceEvent.DrawSchoolInterfaceHandler> {

	public static Type<DrawSchoolInterfaceHandler> TYPE = new Type<DrawSchoolInterfaceHandler>();
	
	//
	private String schoolCode;
	private CurrentSchool currentSchool;

	public interface DrawSchoolInterfaceHandler extends EventHandler {
		void onDrawSchoolInterface(DrawSchoolInterfaceEvent event);
	}

	public interface DrawSchoolInterfaceHasHandlers extends HasHandlers {
		HandlerRegistration addDrawSchoolInterfaceHandler(
				DrawSchoolInterfaceHandler handler);
	}

	public DrawSchoolInterfaceEvent() {
	}
	
	public DrawSchoolInterfaceEvent(String schoolCode) {
		this.schoolCode = schoolCode;
	}
	
	public DrawSchoolInterfaceEvent(CurrentSchool addCurrentSchool) {
		this.schoolCode = addCurrentSchool.getSchoolCode();
		this.currentSchool = addCurrentSchool;
	}

	@Override
	protected void dispatch(DrawSchoolInterfaceHandler handler) {
		handler.onDrawSchoolInterface(this);
	}

	@Override
	public Type<DrawSchoolInterfaceHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<DrawSchoolInterfaceHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new DrawSchoolInterfaceEvent());
	}

	public String getSchoolCode() {
		return schoolCode;
	}

	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}

	public CurrentSchool getCurrentSchool() {
		return currentSchool;
	}

	public void setCurrentSchool(CurrentSchool currentSchool) {
		this.currentSchool = currentSchool;
	}
}
