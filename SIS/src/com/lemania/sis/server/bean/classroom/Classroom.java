package com.lemania.sis.server.bean.classroom;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.lemania.sis.server.DatastoreObject;

@Entity
@Index
public class Classroom extends DatastoreObject implements Comparable<Classroom> {
	//
	private String roomName = "";
	private int roomCapacity = 0;
	private String roomNote = "";
	private boolean isActive = true;
	
	public Classroom() {
		
	}
	
	public Classroom(String roomName, int roomCapacity, String roomNote,
			boolean isActive) {
		super();
		this.roomName = roomName;
		this.roomCapacity = roomCapacity;
		this.roomNote = roomNote;
		this.isActive = isActive;
	}



	public String getRoomName() {
		return roomName;
	}



	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}



	public int getRoomCapacity() {
		return roomCapacity;
	}



	public void setRoomCapacity(int roomCapacity) {
		this.roomCapacity = roomCapacity;
	}



	public String getRoomNote() {
		return roomNote;
	}



	public void setRoomNote(String roomNote) {
		this.roomNote = roomNote;
	}



	public boolean isActive() {
		return isActive;
	}



	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}



	/*
	 * */
	@Override
	public int compareTo(Classroom o) {
		//
		return this.roomName.compareTo( o.roomName );
	}

}
