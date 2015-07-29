package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class MasterAgendaLoadEvent extends
		GwtEvent<MasterAgendaLoadEvent.MasterAgendaLoadHandler> {

	public static Type<MasterAgendaLoadHandler> TYPE = new Type<MasterAgendaLoadHandler>();
	
	//
	private String profileId = "";
	private String classId = "";
	private String bulletinId = "";

	public interface MasterAgendaLoadHandler extends EventHandler {
		void onMasterAgendaLoad(MasterAgendaLoadEvent event);
	}

	public interface MasterAgendaLoadHasHandlers extends HasHandlers {
		HandlerRegistration addMasterAgendaLoadHandler(
				MasterAgendaLoadHandler handler);
	}

	public MasterAgendaLoadEvent() {
	}
	
	public MasterAgendaLoadEvent(String profileId) {
		this.profileId = profileId;
	}
	
	public MasterAgendaLoadEvent(String classId, String profileId, String bulletinId) {
		this.classId = classId;
		this.profileId = profileId;
		this.bulletinId = bulletinId;
	}

	@Override
	protected void dispatch(MasterAgendaLoadHandler handler) {
		handler.onMasterAgendaLoad(this);
	}

	@Override
	public Type<MasterAgendaLoadHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MasterAgendaLoadHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new MasterAgendaLoadEvent());
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public static Type<MasterAgendaLoadHandler> getTYPE() {
		return TYPE;
	}

	public static void setTYPE(Type<MasterAgendaLoadHandler> tYPE) {
		TYPE = tYPE;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getBulletinId() {
		return bulletinId;
	}

	public void setBulletinId(String bulletinId) {
		this.bulletinId = bulletinId;
	}
}
