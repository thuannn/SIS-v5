package com.lemania.sis.client.form.subjects;

import java.util.List;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.lemania.sis.client.UI.FieldValidation;
import com.lemania.sis.client.event.LoginAuthenticatedEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent.LoginAuthenticatedHandler;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.place.NameTokens;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.lemania.sis.client.AdminGateKeeper;
import com.lemania.sis.client.CurrentUser;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.SubjectProxy;
import com.lemania.sis.shared.service.SubjectRequestFactory;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.SubjectRequestFactory.SubjectRequestContext;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.Store.Change;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.data.shared.loader.RequestFactoryProxy;

public class FrmSubjectListPresenter 
	extends	Presenter<FrmSubjectListPresenter.MyView, FrmSubjectListPresenter.MyProxy> 
	implements FrmSubjectListUiHandler, LoginAuthenticatedHandler {
	
	//
	private CurrentUser currentUser;
	

	public interface MyView extends View, HasUiHandlers<FrmSubjectListUiHandler> {
		void initializeSubjectTable();
		void setSubjectListData(List<SubjectProxy> subjectList);
		void refreshUpdatedSubject(SubjectProxy subject);
		void initializeGrid( PagingLoader<FilterPagingLoadConfig, PagingLoadResult<SubjectProxy>> loader );
		void commitStoreChange( SubjectProxy updatedSubject );
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.subjectlist)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends ProxyPlace<FrmSubjectListPresenter> {
	}

	@Inject
	public FrmSubjectListPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		
		// Thuan
		getView().setUiHandlers(this);
		getView().initializeSubjectTable();
		//
		initializeGrid();
	}
	
	
	/*
	 * 
	 * */
	@Override
	protected void onReset() {
		//
		super.onReset();
		// Thuan
		loadSubjectList();
	}

	/*
	 * GXT Grid 
	 * Create a data loader and send to View
	 * */
	private void initializeGrid() {
		//
		final SubjectRequestFactory requestFactory = GWT.create(SubjectRequestFactory.class);
		requestFactory.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		//
		RequestFactoryProxy<FilterPagingLoadConfig, PagingLoadResult<SubjectProxy>> proxy = new 
				RequestFactoryProxy<FilterPagingLoadConfig, PagingLoadResult<SubjectProxy>>() {
			@Override
			public void load(FilterPagingLoadConfig loadConfig,
					Receiver<? super PagingLoadResult<SubjectProxy>> receiver) {
				SubjectRequestContext request = requestFactory.subjectRequest();
				List<SortInfo> sortInfo = createRequestSortInfo(request,
						loadConfig.getSortInfo());
				List<FilterConfig> filterConfig = createRequestFilterConfig(
						request, loadConfig.getFilters());
				
				request.listAll(loadConfig.getOffset(), loadConfig.getLimit(),
						sortInfo, filterConfig).to(receiver);
				request.fire();
			}
		};

		final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<SubjectProxy>> loader = new 
				PagingLoader<FilterPagingLoadConfig, PagingLoadResult<SubjectProxy>>(proxy);
		
		loader.useLoadConfig(new FilterPagingLoadConfigBean());
		loader.setRemoteSort(true);
		
		//
		getView().initializeGrid(loader);
	}

	/*
	 * 
	 * */
	@Override
	public void updateSubject(SubjectProxy subject, String subjectName, String subjectCoef, Boolean isActive) {
		// Validate data
		if ( subjectName.isEmpty() ){
			Window.alert( NotificationValues.invalid_input + " - Nom de la matière.");
			return;
		}
		if ( FieldValidation.isNumeric( subjectCoef) ) {
			if ( Double.parseDouble(subjectCoef) < 0 ) {
				Window.alert( NotificationValues.invalid_input + " - Coefficient");
				return;
			}
		} else {
			Window.alert( NotificationValues.invalid_input + " - Coefficient");
			return;
		}
		// Update
		SubjectRequestContext rc = getSubjectRequestContext();
		subject = rc.edit(subject);
		subject.setSubjectName(subjectName);
		subject.setDefaultCoef(Double.parseDouble(subjectCoef));
		subject.setIsActive(isActive);
		
		rc.saveAndReturn(subject).fire(new Receiver<SubjectProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(SubjectProxy response) {
				getView().refreshUpdatedSubject(response);
			}
		});
		//
	}
	
	/*
	 * 
	 * */
	public void loadSubjectList(){
		SubjectRequestContext rc = getSubjectRequestContext();
		rc.listAll().fire(new Receiver<List<SubjectProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<SubjectProxy> response) {
				getView().setSubjectListData(response);
			}
		});
	}
	
	
	/* Get the request context for SubjectRequestFactory
	 * Used in every function which call to Request Factory */
	public SubjectRequestContext getSubjectRequestContext() {
		SubjectRequestFactory rf = GWT.create(SubjectRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		return rf.subjectRequest();
	}

	
	@ProxyEvent
	@Override
	public void onLoginAuthenticated(LoginAuthenticatedEvent event) {
		//
		this.currentUser = event.getCurrentUser();
	}

	
	/*
	 * 20170528 - Update subjectName2
	 * */
	@Override
	public void updateSubjectName2(SubjectProxy subject, String subjectName2) {
		//
		// Update
		SubjectRequestContext rc = getSubjectRequestContext();
		subject = rc.edit(subject);
		subject.setSubjectName2( subjectName2 );
		rc.saveAndReturn(subject).fire(new Receiver<SubjectProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(SubjectProxy response) {
				getView().refreshUpdatedSubject(response);
			}
		});
	}

	
	/*
	 * GXT Grid editing
	 * */
	@Override
	public void updateSubjectFromStore( Change<SubjectProxy, ?> change, SubjectProxy sp) {
		//
		SubjectRequestContext rc = getSubjectRequestContext();
		SubjectProxy editedSubject = rc.edit(sp);
		change.modify( editedSubject );
		rc.saveAndReturn( editedSubject ).fire(new Receiver<SubjectProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(SubjectProxy response) {
				getView().commitStoreChange( response );
			}
		});
	}
}
