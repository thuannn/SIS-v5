package com.lemania.sis.client.form.parentmgt;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.lemania.sis.client.AdminGateKeeper;
import com.lemania.sis.client.event.ParentAfterAddEvent;
import com.lemania.sis.client.event.ParentAfterAddEvent.ParentAfterAddHandler;
import com.lemania.sis.client.event.ParentAfterUpdateEvent;
import com.lemania.sis.client.event.ParentAfterUpdateEvent.ParentAfterUpdateHandler;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.place.NameTokens;
import com.lemania.sis.client.popup.parentprofile.ParentProfilePresenter;
import com.lemania.sis.shared.parent.ParentProxy;
import com.lemania.sis.shared.parent.ParentRequestFactory;
import com.lemania.sis.shared.parent.ParentRequestFactory.ParentRequestContext;
import com.lemania.sis.shared.service.EventSourceRequestTransport;

public class ParentManagementPresenter
		extends
		Presenter<ParentManagementPresenter.MyView, ParentManagementPresenter.MyProxy>
		implements ParentManagementUiHandlers, ParentAfterAddHandler, ParentAfterUpdateHandler {
	
	interface MyView extends View,
			HasUiHandlers<ParentManagementUiHandlers> {
		//
		void initializeUI();
		void setParentData(List<ParentProxy> parents);
		void addNewParent(ParentProxy newParent);
	}
	
	//
	private ParentProfilePresenter popupParentProfile;
	

	@NameToken(NameTokens.parentmgt)
	@ProxyCodeSplit
	@UseGatekeeper( AdminGateKeeper.class )
	public interface MyProxy extends
			ProxyPlace<ParentManagementPresenter> {
	}

	@Inject
	public ParentManagementPresenter(EventBus eventBus, MyView view,
			MyProxy proxy, ParentProfilePresenter pp ) {
		super(eventBus, view, proxy, MainPagePresenter.TYPE_SetMainContent);

		getView().setUiHandlers(this);

		this.popupParentProfile = pp;
	}

	protected void onBind() {
		super.onBind();
		//
		getView().initializeUI();
	}

	protected void onReset() {
		super.onReset();
		//
		loadParents();
	}

	
	/*
	 * */
	private void loadParents() {
		//
		ParentRequestFactory rf = GWT.create(ParentRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		ParentRequestContext rc = rf.parentRequestContext();
		rc.listAll().fire(new Receiver<List<ParentProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<ParentProxy> response) {
				getView().setParentData(response);
			}
		});
	}

	/*
	 * */
	@Override
	public void showParentProfilePopup() {
		//
		addToPopupSlot(popupParentProfile, true);
		popupParentProfile.loadActiveStudents();
		popupParentProfile.editExisting = false;
		popupParentProfile.existingParent = null;
		popupParentProfile.getView().resetUI(popupParentProfile.editExisting);
	}

	/*
	 * */
	@Override
	public void editParent(ParentProxy parent) {
		//
		addToPopupSlot(popupParentProfile, true);
		popupParentProfile.loadActiveStudents();
		popupParentProfile.editExisting = true;
		popupParentProfile.existingParent = parent;
		popupParentProfile.getView().resetUI(popupParentProfile.editExisting);
		popupParentProfile.showParentDetail(parent);
	}

	
	/*
	 * */
	@ProxyEvent
	@Override
	public void onParentAfterAdd(ParentAfterAddEvent event) {
		//
		loadParents();
	}

	
	/*
	 * */
	@ProxyEvent
	@Override
	public void onParentAfterUpdate(ParentAfterUpdateEvent event) {
		//
		loadParents();
	}

}
