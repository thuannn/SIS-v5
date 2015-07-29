package com.lemania.sis.client.form.classroom;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.lemania.sis.client.AdminGateKeeper;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.place.NameTokens;
import com.lemania.sis.shared.classroom.ClassroomProxy;
import com.lemania.sis.shared.classroom.ClassroomRequestFactory;
import com.lemania.sis.shared.classroom.ClassroomRequestFactory.ClassroomRequestContext;
import com.lemania.sis.shared.service.EventSourceRequestTransport;

public class ClassroomPresenter extends
		Presenter<ClassroomPresenter.MyView, ClassroomPresenter.MyProxy>
		implements ClassroomUiHandlers {
	
	interface MyView extends View, HasUiHandlers<ClassroomUiHandlers> {
		//
		void addClassroomToList(ClassroomProxy cp);
		void initializeUI();
		void setClassroomTableData( List<ClassroomProxy> list);
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> SLOT_Classroom = new Type<RevealContentHandler<?>>();

	@NameToken(NameTokens.classroom)
	@UseGatekeeper(AdminGateKeeper.class)
	@ProxyCodeSplit
	public interface MyProxy extends ProxyPlace<ClassroomPresenter> {
	}

	@Inject
	public ClassroomPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy, MainPagePresenter.TYPE_SetMainContent);

		getView().setUiHandlers(this);
	}

	protected void onBind() {
		//
		super.onBind();
		//
		getView().initializeUI();
	}

	protected void onReset() {
		//
		super.onReset();
		//
		loadClassroomList();
	}

	/*
	 * */
	private void loadClassroomList() {
		//
		ClassroomRequestContext rc = getRequestContext();
		rc.listAll().fire(new Receiver<List<ClassroomProxy>>() {
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess( List<ClassroomProxy> response) {
				getView().setClassroomTableData(response);
			}
		});
	}

	/*
	 * */
	@Override
	public void addClassroom(String name, int capacity, String note,
			boolean isActive) {
		//
		ClassroomRequestContext rc = getRequestContext();
		rc.addClassroom(name, capacity, note, isActive).fire(new Receiver<ClassroomProxy>() {
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess( ClassroomProxy response) {
				getView().addClassroomToList(response);
			}
		});
	}
	
	/*
	 * */
	private ClassroomRequestContext getRequestContext(){
		//
		ClassroomRequestFactory rf = GWT.create(ClassroomRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		return rf.classroomRequestContext();
	}

	/*
	 * */
	@Override
	public void updateClassroom(ClassroomProxy cp, String name, int capacity,
			String note, boolean isActive) {
		//
		ClassroomRequestContext rc = getRequestContext();
		ClassroomProxy cu = rc.edit(cp);
		cu.setRoomName(name);
		cu.setRoomCapacity(capacity);
		cu.setRoomNote(note);
		cu.setActive(isActive);
		rc.saveAndReturn( cu ).fire(new Receiver<ClassroomProxy>() {
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess( ClassroomProxy response) {
				//
			}
		});
	}

}
