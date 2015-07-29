package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class ProfileBrancheAfterAddEvent extends
		GwtEvent<ProfileBrancheAfterAddEvent.ProfileBrancheAfterAddHandler> {

	public static Type<ProfileBrancheAfterAddHandler> TYPE = new Type<ProfileBrancheAfterAddHandler>();
	
	// Thuan
	private String profileSubjectId = "";
	private Integer subjectLastPosition = -1;

	public interface ProfileBrancheAfterAddHandler extends EventHandler {
		void onProfileBrancheAfterAdd(ProfileBrancheAfterAddEvent event);
	}

	public interface ProfileBrancheAfterAddHasHandlers extends HasHandlers {
		HandlerRegistration addProfileBrancheAfterAddHandler(
				ProfileBrancheAfterAddHandler handler);
	}

	public ProfileBrancheAfterAddEvent() {
	}
	
	public ProfileBrancheAfterAddEvent( String profileSubjectId, Integer lastPosition ) {
		this.profileSubjectId = profileSubjectId;
		this.subjectLastPosition = lastPosition;
	}

	@Override
	protected void dispatch(ProfileBrancheAfterAddHandler handler) {
		handler.onProfileBrancheAfterAdd(this);
	}

	@Override
	public Type<ProfileBrancheAfterAddHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ProfileBrancheAfterAddHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ProfileBrancheAfterAddEvent());
	}

	public String getProfileSubjectId() {
		return profileSubjectId;
	}

	public void setProfileSubjectId(String profileId) {
		this.profileSubjectId = profileId;
	}
	
	public Integer getSubjectLastPosition(){
		return this.subjectLastPosition;
	}
}
