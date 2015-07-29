package com.lemania.sis.client.popup.parentprofile;

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
import com.lemania.sis.client.UI.FieldValidation;
import com.lemania.sis.client.event.ParentAfterAddEvent;
import com.lemania.sis.client.event.ParentAfterUpdateEvent;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.parent.ParentProxy;
import com.lemania.sis.shared.parent.ParentRequestFactory;
import com.lemania.sis.shared.parent.ParentRequestFactory.ParentRequestContext;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.student.StudentProxy;
import com.lemania.sis.shared.student.StudentRequestFactory;
import com.lemania.sis.shared.student.StudentRequestFactory.StudentRequestContext;

public class ParentProfilePresenter extends
		PresenterWidget<ParentProfilePresenter.MyView> implements
		ParentProfileUiHandlers {
	
	//
	public boolean editExisting = false;
	public ParentProxy existingParent;
	
	
	//
	public interface MyView extends PopupView,
			HasUiHandlers<ParentProfileUiHandlers> {
		//
		void initializeUI(boolean editExisting);
		void resetUI(boolean editExisting);
		void setStudentsData(List<StudentProxy> students);
		void showParentDetails(ParentProxy parent);
		void showChildren(List<StudentProxy> children);
	}

	//
	@Inject
	ParentProfilePresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);

		getView().setUiHandlers(this);
	}

	//
	protected void onBind() {
		super.onBind();
		//
		initializeUI();
	}

	//
	protected void onReset() {
		super.onReset();
	}
	
	//
	@Override
	protected void onHide() {
	  super.onHide();
	  //
	  editExisting = false;
	  existingParent = null;
	}
	
	
	/*
	 * */
	public void showParentDetail(final ParentProxy parent) {
		//
		if (editExisting) {
			//
			ParentRequestFactory rf = GWT.create(ParentRequestFactory.class);
			rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
			ParentRequestContext rc = rf.parentRequestContext();
			rc.loadChildren(parent).fire(new Receiver<List<StudentProxy>>(){
				@Override
				public void onFailure(ServerFailure error){
					Window.alert(error.getMessage());
				}
				@Override
				public void onSuccess(List<StudentProxy> response) {
					//
					getView().showParentDetails(parent);
					getView().showChildren(response);
				}
			});
		}
	}
	
	
	/*
	 * */
	public void loadActiveStudents() {
		//
		StudentRequestContext rc = getStudentRequestContext();
		rc.listAllActive().fire(new Receiver<List<StudentProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<StudentProxy> response) {
				getView().setStudentsData(response);
			}
		});
	}
	
	
	/* Get the request context for StudenProxy.
	 * Used in every function which call to Request Factory */
	public StudentRequestContext getStudentRequestContext() {
		//
		StudentRequestFactory rf = GWT.create(StudentRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		return rf.studentRequest();
	}
	
	
	/*
	 * */
	public void initializeUI() {
		//
		getView().initializeUI( editExisting );
	}
	

	/*
	 * */
	@Override
	public void saveParent(final String title, final String firstName, final String lastName,
			final String eMail, final String phoneMobile, final String phoneHome,
			final String phoneWork, final boolean acceptSMS, final boolean acceptEmail, final List<StudentProxy> children ) {
		//
		if (!FieldValidation.isValidEmailAddress(eMail)) {
			Window.alert( NotificationValues.invalid_input + " - Email");
			return;
		}
		if ( firstName.equals("") || lastName.equals("") ) {
			Window.alert( NotificationValues.invalid_input + " - Nom & Prénom");
			return;
		}
		//
		if ( !editExisting ) {
			ParentRequestFactory rf = GWT.create(ParentRequestFactory.class);
			rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
			ParentRequestContext rc = rf.parentRequestContext();
			//
			rc.checkExistence(eMail).fire(new Receiver<Boolean>(){
				@Override
				public void onFailure(ServerFailure error){
					Window.alert(error.getMessage());
				}
				@Override
				public void onSuccess(Boolean isExisted) {
					//
					if (isExisted) {
						Window.alert( NotificationValues.parent_already_existed );
					} else {
						saveNewParent( title,  firstName,  lastName,
								 eMail,  phoneMobile,  phoneHome,
								 phoneWork,  acceptSMS,  acceptEmail, children );
					}
				}
			});
		} else {
			saveNewParent( title,  firstName,  lastName,
					 eMail,  phoneMobile,  phoneHome,
					 phoneWork,  acceptSMS,  acceptEmail, children );
		}
	}
	
	
	/*
	 * */
	public void saveNewParent(String title, String firstName, String lastName,
			String eMail, String phoneMobile, String phoneHome,
			String phoneWork, boolean acceptSMS, boolean acceptEmail, List<StudentProxy> children ) {
		//
		String childrenIds = "";
		for (StudentProxy sp : children)
			childrenIds = childrenIds + sp.getId().toString() + " ";
		//
		ParentRequestFactory rf = GWT.create(ParentRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		ParentRequestContext rc = rf.parentRequestContext();
		//
		ParentProxy pp;
		if (! editExisting )
			pp = rc.create( ParentProxy.class );
		else
			pp = rc.edit( existingParent );
		pp.setChildIds(childrenIds);
		pp.setTitle(title);
		pp.setFirstName(firstName);
		pp.setLastName(lastName);
		pp.seteMail(eMail);
		pp.setPhoneHome(phoneHome);
		pp.setPhoneMobile(phoneMobile);
		pp.setPhoneWork(phoneWork);
		pp.setAcceptEmail(acceptEmail);
		pp.setAcceptSMS(acceptSMS);
		//
		rc.saveAndReturn(pp).fire(new Receiver<ParentProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(ParentProxy response) {
				//
				if (! editExisting) {
					getEventBus().fireEvent( new ParentAfterAddEvent(response) );		// Add this new parent to the main grid, create new User for this person
					getView().hide();
				} else
					getEventBus().fireEvent( new ParentAfterUpdateEvent(response) );
			}
		});
	}
}
