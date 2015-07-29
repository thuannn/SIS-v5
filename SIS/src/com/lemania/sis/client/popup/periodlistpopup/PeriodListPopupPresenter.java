package com.lemania.sis.client.popup.periodlistpopup;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.lemania.sis.client.event.PeriodItemPopupCloseEvent;
import com.lemania.sis.shared.perioditem.PeriodItemProxy;
import com.lemania.sis.shared.perioditem.PeriodItemRequestFactory;
import com.lemania.sis.shared.perioditem.PeriodItemRequestFactory.PeriodItemRequestContext;
import com.lemania.sis.shared.service.EventSourceRequestTransport;

public class PeriodListPopupPresenter extends
		PresenterWidget<PeriodListPopupPresenter.MyView> implements
		PeriodListPopupUiHandlers {
	
	public interface MyView extends PopupView,
			HasUiHandlers<PeriodListPopupUiHandlers> {
		//
		void addNewPeriodItem(PeriodItemProxy pi);
		void setPeriodData(List<PeriodItemProxy> periods);
		//
		void initializeUI();
		void resetUI();
	}

	@Inject
	PeriodListPopupPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);

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
		getView().resetUI();
		//
		loadPeriods();
	}


	/*
	 * */
	public void loadPeriods() {
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
				getView().setPeriodData(response);
			}
		});
	}

	/*
	 * */
	@Override
	public void addPeriod(int fromHour, int fromMinute, int toHour, int toMinute, String note, boolean isActive) {
		//
		PeriodItemRequestFactory rf = GWT.create(PeriodItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		PeriodItemRequestContext rc = rf.periodItemRequestContext();
		rc.addPeriodItem( fromHour, fromMinute, toHour, toMinute, note, isActive).fire(new Receiver<PeriodItemProxy>() {
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(PeriodItemProxy response) {
				getView().addNewPeriodItem(response);
			}
		});
	}

	
	/*
	 * */
	@Override
	public void hidePopup() {
		//
		getView().hide();
		getEventBus().fireEvent( new PeriodItemPopupCloseEvent() );
	}

}
