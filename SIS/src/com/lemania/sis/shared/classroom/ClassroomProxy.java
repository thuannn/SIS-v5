package com.lemania.sis.shared.classroom;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.lemania.sis.server.ObjectifyLocator;
import com.lemania.sis.server.bean.classroom.Classroom;

@ProxyFor( value=Classroom.class, locator=ObjectifyLocator.class )
public interface ClassroomProxy extends EntityProxy {
	//
	Long getId();
	
	public String getRoomName();
	public void setRoomName(String roomName);

	public int getRoomCapacity();
	public void setRoomCapacity(int roomCapacity);

	public String getRoomNote();
	public void setRoomNote(String roomNote);
	
	public boolean isActive();
	public void setActive(boolean isActive);
}
