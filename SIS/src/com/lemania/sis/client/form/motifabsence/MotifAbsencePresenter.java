package com.lemania.sis.client.form.motifabsence;

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
import com.lemania.sis.shared.motifabsence.MotifAbsenceProxy;
import com.lemania.sis.shared.motifabsence.MotifAbsenceRequestFactory;
import com.lemania.sis.shared.motifabsence.MotifAbsenceRequestFactory.MotifAbsenceRequestContext;
import com.lemania.sis.shared.service.EventSourceRequestTransport;

public class MotifAbsencePresenter extends
		Presenter<MotifAbsencePresenter.MyView, MotifAbsencePresenter.MyProxy>
		implements MotifAbsenceUiHandlers {
	interface MyView extends View, HasUiHandlers<MotifAbsenceUiHandlers> {
		//
		void initializeUI();
		//
		void setMotifListData(List<MotifAbsenceProxy> motifs);
		//
		void addNewMotif( boolean isEditing, MotifAbsenceProxy newMotif );
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> SLOT_MotifAbsence = new Type<RevealContentHandler<?>>();

	@NameToken(NameTokens.motifabsence)
	@ProxyCodeSplit
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends ProxyPlace<MotifAbsencePresenter> {
	}

	@Inject
	public MotifAbsencePresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy, MainPagePresenter.TYPE_SetMainContent);

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
		loadMotifs();
	}

	/*
	 * */
	private void loadMotifs() {
		//
		MotifAbsenceRequestFactory rf = GWT.create(MotifAbsenceRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		MotifAbsenceRequestContext rc = rf.motifAbsenceRequestContext();
		rc.listAll().fire(new Receiver<List<MotifAbsenceProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<MotifAbsenceProxy> response) {
				getView().setMotifListData(response);
			}
		});
	}

	
	/*
	 * */
	@Override
	public void saveMotifs(final boolean isEditing, MotifAbsenceProxy motif, String txtCode, String txtLabel,
			boolean chkReceivable, boolean chkOutside, boolean chkHealth,
			boolean chkDispense, String txtLetter, String txtSMS) {
		//
		MotifAbsenceRequestFactory rf = GWT.create(MotifAbsenceRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		MotifAbsenceRequestContext rc = rf.motifAbsenceRequestContext();
		//
		MotifAbsenceProxy savingMotif;
		if (isEditing)
			savingMotif = rc.edit( motif );
		else
			savingMotif = rc.create( MotifAbsenceProxy.class );
		savingMotif.setMotifCode(txtCode);
		savingMotif.setMotifLabel(txtLabel);
		savingMotif.setReceivable(chkReceivable);
		savingMotif.setOutside(chkOutside);
		savingMotif.setHealth(chkHealth);
		savingMotif.setDispensable(chkDispense);
		savingMotif.setTextLetter(txtLetter);
		savingMotif.setTextSMS(txtSMS);
		//
		rc.saveAndReturn(savingMotif).fire(new Receiver<MotifAbsenceProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(MotifAbsenceProxy response) {
				//
				getView().addNewMotif(isEditing, response);
			}
		});
	}

}
