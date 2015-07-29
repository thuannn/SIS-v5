package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class CheckClassMasterRoleEvent extends
		GwtEvent<CheckClassMasterRoleEvent.CheckClassMasterRoleHandler> {
	
	//
	private String profId = "";

	public static Type<CheckClassMasterRoleHandler> TYPE = new Type<CheckClassMasterRoleHandler>();

	public interface CheckClassMasterRoleHandler extends EventHandler {
		void onCheckClassMasterRole(CheckClassMasterRoleEvent event);
	}

	public interface CheckClassMasterRoleHasHandlers extends HasHandlers {
		HandlerRegistration addCheckClassMasterRoleHandler(
				CheckClassMasterRoleHandler handler);
	}

	public CheckClassMasterRoleEvent() {
	}
	
	public CheckClassMasterRoleEvent(String profId) {
		this.profId = profId;
	}

	@Override
	protected void dispatch(CheckClassMasterRoleHandler handler) {
		handler.onCheckClassMasterRole(this);
	}

	@Override
	public Type<CheckClassMasterRoleHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CheckClassMasterRoleHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new CheckClassMasterRoleEvent());
	}

	public String getProfId() {
		return profId;
	}

	public void setProfId(String profId) {
		this.profId = profId;
	}
}
