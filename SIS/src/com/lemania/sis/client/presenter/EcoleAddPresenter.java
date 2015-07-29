package com.lemania.sis.client.presenter;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.lemania.sis.client.AdminGateKeeper;
import com.lemania.sis.client.CurrentUser;
import com.lemania.sis.client.event.EcoleAddedEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent.LoginAuthenticatedHandler;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.place.NameTokens;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.lemania.sis.client.uihandler.EcoleAddUiHandler;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.EcoleProxy;
import com.lemania.sis.shared.service.EcoleRequestFactory;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.EcoleRequestFactory.EcoleRequestContext;

public class EcoleAddPresenter extends
		Presenter<EcoleAddPresenter.MyView, EcoleAddPresenter.MyProxy> 
		implements EcoleAddUiHandler, LoginAuthenticatedHandler {
	
	//
	private CurrentUser currentUser;
	

	public interface MyView extends View, HasUiHandlers<EcoleAddUiHandler> {
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.addecole)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends ProxyPlace<EcoleAddPresenter> {
	}

	@Inject
	public EcoleAddPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
		
		// Thuan: attach Ui handler
		getView().setUiHandlers(this);
	}
	
	// Thuan	
	private EcoleProxy ep;

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	@Override
	protected void onReset() {
		super.onReset();
	}
	

	@Override
	public void ecoleAdd(String ecoleNom, String ecoleAdresse, Boolean ecoleActive) {
		//
		if (this.currentUser.isReadOnly()){
			Window.alert(NotificationValues.readOnly);
			return;
		}
		
		//
		if (ecoleNom.isEmpty() || ecoleAdresse.isEmpty()){
			Window.alert("Veuillez saissir le nom et l'addresse de l'�cole !");
			return;
		}
		
		//
		EcoleRequestFactory rf = GWT.create(EcoleRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		EcoleRequestContext rc = rf.ecoleRequest();
		ep = rc.create(EcoleProxy.class);
		ep.setSchoolName(ecoleNom);
		ep.setSchoolAddress(ecoleAdresse);
		ep.setSchoolStatus(ecoleActive);
		rc.save(ep).fire(new Receiver<Void>(){
			@Override
			public void onSuccess(Void response){
				returnToEcoleListSuccess();
			}
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
		});
	}

	@Override
	public void ecoleAddCancel() {
		// 
		returnToEcoleListCancel();
	}

	private void returnToEcoleListCancel() {
		// TODO Auto-generated method stub
		History.newItem(NameTokens.ecolepage);
	}
	
	private void returnToEcoleListSuccess() {
		this.getEventBus().fireEvent(new EcoleAddedEvent(ep));
	}

	
	@ProxyEvent
	@Override
	public void onLoginAuthenticated(LoginAuthenticatedEvent event) {
		//
		this.currentUser = event.getCurrentUser();
	}
}
