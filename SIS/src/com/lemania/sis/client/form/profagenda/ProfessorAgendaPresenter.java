package com.lemania.sis.client.form.profagenda;

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
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.lemania.sis.client.AdminGateKeeper;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.place.NameTokens;
import com.lemania.sis.shared.ProfessorProxy;
import com.lemania.sis.shared.masteragendaitem.MasterAgendaItemProxy;
import com.lemania.sis.shared.masteragendaitem.MasterAgendaItemRequestFactory;
import com.lemania.sis.shared.masteragendaitem.MasterAgendaItemRequestFactory.MasterAgendaItemRequestContext;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.ProfessorRequestFactory;
import com.lemania.sis.shared.service.ProfessorRequestFactory.ProfessorRequestContext;

public class ProfessorAgendaPresenter
		extends
		Presenter<ProfessorAgendaPresenter.MyView, ProfessorAgendaPresenter.MyProxy>
		implements ProfessorAgendaUiHandlers {
	
	interface MyView extends View, HasUiHandlers<ProfessorAgendaUiHandlers> {
		//
		void setProfList(List<ProfessorProxy> profs);
		//
		void showMasterAgendaItemData(List<MasterAgendaItemProxy> mais);
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> SLOT_ProfessorAgenda = new Type<RevealContentHandler<?>>();

	@NameToken(NameTokens.profagenda)
	@ProxyCodeSplit
	@UseGatekeeper( AdminGateKeeper.class )
	public interface MyProxy extends ProxyPlace<ProfessorAgendaPresenter> {
	}

	@Inject
	public ProfessorAgendaPresenter(EventBus eventBus, MyView view,
			MyProxy proxy) {
		super(eventBus, view, proxy, MainPagePresenter.TYPE_SetMainContent);

		getView().setUiHandlers(this);
	}

	
	/*
	 * */
	protected void onBind() {
		//
		super.onBind();
	}

	
	/*
	 * */
	protected void onReset() {
		//
		super.onReset();
		//
		loadActiveProfs();
	}
	

	/*
	 * */
	private void loadActiveProfs() {
		//
		ProfessorRequestFactory rf = GWT.create(ProfessorRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		ProfessorRequestContext rc = rf.professorRequest();
		rc.listAllActive().fire(new Receiver<List<ProfessorProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<ProfessorProxy> response) {
				getView().setProfList(response);
			}
		});
	}


	/*
	 * */
	@Override
	public void onProfessorChange(String profId) {
		//
		MasterAgendaItemRequestFactory rf = GWT.create(MasterAgendaItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		MasterAgendaItemRequestContext rc = rf.masterAgendaItemRequestContext();
		rc.listAllByProf( profId ).fire(new Receiver<List<MasterAgendaItemProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<MasterAgendaItemProxy> response) {
				getView().showMasterAgendaItemData(response);
			}
		});		
	}
	
}
