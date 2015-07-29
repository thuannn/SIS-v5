package com.lemania.sis.client.form.periodmanagement;

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
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.lemania.sis.client.AdminGateKeeper;
import com.lemania.sis.client.event.PeriodItemPopupCloseEvent;
import com.lemania.sis.client.event.PeriodItemPopupCloseEvent.PeriodItemPopupCloseHandler;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.place.NameTokens;
import com.lemania.sis.client.popup.periodlistpopup.PeriodListPopupPresenter;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.ClasseProxy;
import com.lemania.sis.shared.period.PeriodProxy;
import com.lemania.sis.shared.period.PeriodRequestFactory;
import com.lemania.sis.shared.period.PeriodRequestFactory.PeriodRequestContext;
import com.lemania.sis.shared.perioditem.PeriodItemProxy;
import com.lemania.sis.shared.perioditem.PeriodItemRequestFactory;
import com.lemania.sis.shared.perioditem.PeriodItemRequestFactory.PeriodItemRequestContext;
import com.lemania.sis.shared.service.ClasseRequestFactory;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.ClasseRequestFactory.ClasseRequestContext;

public class PeriodManagementPresenter
		extends
		Presenter<PeriodManagementPresenter.MyView, PeriodManagementPresenter.MyProxy>
		implements PeriodManagementUiHandlers, PeriodItemPopupCloseHandler {
	
	interface MyView extends View, HasUiHandlers<PeriodManagementUiHandlers> {
		//
		void initializeUI();
		//
		void showPeriodData(List<PeriodProxy> periods);
		//
		void setClassList(List<ClasseProxy> classes);
		//
		void addNewPeriod(PeriodProxy period);
		//
		void updatePeriod(PeriodProxy period);
		//
		void setPeriodListData( List<PeriodItemProxy> periods);
	}
	
	
	private PeriodListPopupPresenter periodItemPopup;
	

	@ContentSlot
	public static final Type<RevealContentHandler<?>> SLOT_PeriodManagement = new Type<RevealContentHandler<?>>();

	@NameToken(NameTokens.periodmgt)
	@ProxyCodeSplit
	@UseGatekeeper( AdminGateKeeper.class )
	public interface MyProxy extends ProxyPlace<PeriodManagementPresenter> {
	}

	@Inject
	public PeriodManagementPresenter(EventBus eventBus, MyView view, MyProxy proxy,
			PeriodListPopupPresenter pp) {
		super(eventBus, view, proxy, MainPagePresenter.TYPE_SetMainContent);
		
		this.periodItemPopup = pp;

		getView().setUiHandlers(this);
	}

	protected void onBind() {
		super.onBind();
		//
		getView().initializeUI();
	}

	protected void onReset() {
		super.onReset();
		//
		loadClassList();
		//
		onPeriodItemPopupClose(null);
	}
	
	/*
	 * */
	private void loadClassList() {
		//
		ClasseRequestFactory rf = GWT.create(ClasseRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		ClasseRequestContext rc = rf.classeRequest();
		rc.listAllActive().fire(new Receiver<List<ClasseProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<ClasseProxy> response) {
				getView().setClassList(response);
			}
		});
	}

	/*
	 * */
	@Override
	public void onClassSelected(String classId) {
		//
		if (classId.equals("")) { Window.alert( NotificationValues.invalid_input + " Classe"); return; }
		//
		PeriodRequestFactory rf = GWT.create(PeriodRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		PeriodRequestContext rc = rf.periodRequestContext();
		rc.listAllByClass(classId).fire(new Receiver<List<PeriodProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<PeriodProxy> response) {
				getView().showPeriodData(response);
			}
		});
	}

	/*
	 * */
	@Override
	public void addPeriod(String periodItemId, String classId, String description, int order, String note, boolean isActive) {
		//
		if (periodItemId.equals("")) { Window.alert( NotificationValues.invalid_input + " Period"); return; }
		if (classId.equals("")) { Window.alert( NotificationValues.invalid_input + " Classe"); return; }
		if (description.equals("")) { Window.alert( NotificationValues.invalid_input + " Decsription"); return; }
		//
		PeriodRequestFactory rf = GWT.create(PeriodRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		PeriodRequestContext rc = rf.periodRequestContext();
		rc.addPeriod(periodItemId, classId, description, order, note, isActive).fire(new Receiver<PeriodProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(PeriodProxy response) {
				getView().addNewPeriod(response);
			}
		});
	}

	/*
	 * */
	@Override
	public void updatePeriod(PeriodProxy pp, String description, int order,
			String note, boolean isActive) {
		//
		if (description.equals("")) { Window.alert( NotificationValues.invalid_input + " Decsription"); return; }
		//
		PeriodRequestFactory rf = GWT.create(PeriodRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		PeriodRequestContext rc = rf.periodRequestContext();
		//
		PeriodProxy pu = rc.edit(pp);
		pu.setDescription(description);
		pu.setOrder(order);
		pu.setNote(note);
		pu.setActive(isActive);
		//
		rc.saveAndReturn(pu).fire(new Receiver<PeriodProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(PeriodProxy response) {
				getView().updatePeriod(response);
			}
		});
	}

	
	/*
	 * */
	@Override
	public void showPeriodItemPopup() {
		//
		addToPopupSlot(periodItemPopup, true);
		periodItemPopup.loadPeriods();
	}

	
	/*
	 * */
	@ProxyEvent
	@Override
	public void onPeriodItemPopupClose(PeriodItemPopupCloseEvent event) {
		//
		PeriodItemRequestFactory rf = GWT.create(PeriodItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		PeriodItemRequestContext rc = rf.periodItemRequestContext();
		rc.listAll().fire(new Receiver<List<PeriodItemProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<PeriodItemProxy> response) {
				getView().setPeriodListData(response);
			}
		});
	}

}
