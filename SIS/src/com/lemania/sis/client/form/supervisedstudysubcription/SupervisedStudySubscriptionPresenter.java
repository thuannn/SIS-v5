package com.lemania.sis.client.form.supervisedstudysubcription;

import java.util.ArrayList;
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
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.lemania.sis.client.CurrentUser;
import com.lemania.sis.client.LoggedInGatekeeper;
import com.lemania.sis.client.UI.MyAlert;
import com.lemania.sis.client.event.CheckDeadlineEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent.LoginAuthenticatedHandler;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.form.supervisedstudysubscriptionevents.OnSupervisedStudySubscriptionAddedEvent;
import com.lemania.sis.client.form.supervisedstudysubscriptionevents.OnSupervisedStudySubscriptionAddedEvent.OnSupervisedStudySubscriptionAddedHandler;
import com.lemania.sis.client.place.NameTokens;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.client.values.Repetition;
import com.lemania.sis.shared.ProfessorProxy;
import com.lemania.sis.shared.SubjectProxy;
import com.lemania.sis.shared.assignment.AssignmentProxy;
import com.lemania.sis.shared.assignment.AssignmentRequestFactory;
import com.lemania.sis.shared.assignment.AssignmentRequestFactory.AssignmentRequestContext;
import com.lemania.sis.shared.bulletin.BulletinProxy;
import com.lemania.sis.shared.bulletin.BulletinRequestFactory;
import com.lemania.sis.shared.bulletin.BulletinRequestFactory.BulletinRequestContext;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectProxy;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectRequestFactory;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectRequestFactory.BulletinSubjectRequestContext;
import com.lemania.sis.shared.coursesubscription.CourseSubscriptionProxy;
import com.lemania.sis.shared.coursesubscription.CourseSubscriptionRequestFactory;
import com.lemania.sis.shared.coursesubscription.CourseSubscriptionRequestFactory.CourseSubscriptionRequestContext;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.ProfessorRequestFactory;
import com.lemania.sis.shared.service.SubjectRequestFactory;
import com.lemania.sis.shared.service.ProfessorRequestFactory.ProfessorRequestContext;
import com.lemania.sis.shared.service.SubjectRequestFactory.SubjectRequestContext;
import com.lemania.sis.shared.student.StudentRequestFactory;
import com.lemania.sis.shared.student.StudentRequestFactory.StudentRequestContext;

public class SupervisedStudySubscriptionPresenter
		extends
		Presenter<SupervisedStudySubscriptionPresenter.MyView, SupervisedStudySubscriptionPresenter.MyProxy>
		implements SupervisedStudySubscriptionUiHandlers,
		LoginAuthenticatedHandler, OnSupervisedStudySubscriptionAddedHandler {

	//
	private CurrentUser currentUser;

	//
	interface MyView extends View,
			HasUiHandlers<SupervisedStudySubscriptionUiHandlers> {
		//
		void initializeUI();

		//
		public void setTableData(List<BulletinSubjectProxy> studentList);

		//
		public void setAppliedStudentsTableData(
				List<CourseSubscriptionProxy> list);

		//
		void setProfListData(List<ProfessorProxy> profs, boolean autoSelect);

		//
		void resetForm();
		
		//
		void setAssignmentTableData(List<AssignmentProxy> assignments);
	}

	//
	@ContentSlot
	public static final Type<RevealContentHandler<?>> SLOT_IndividualCourseSubscription = new Type<RevealContentHandler<?>>();

	//
	@NameToken(NameTokens.individualCourseSubscription)
	@ProxyCodeSplit
	@UseGatekeeper(LoggedInGatekeeper.class)
	interface MyProxy extends ProxyPlace<SupervisedStudySubscriptionPresenter> {
	}

	//
	@Inject
	SupervisedStudySubscriptionPresenter(EventBus eventBus, MyView view,
			MyProxy proxy) {
		super(eventBus, view, proxy, MainPagePresenter.TYPE_SetMainContent);

		getView().setUiHandlers(this);
	}
	

	//
	protected void onBind() {
		//
		super.onBind();
		//
		getView().initializeUI();
	}
	

	//
	protected void onReset() {
		//
		super.onReset();
		//
		loadProfessorList();
		//
		getView().resetForm();
	}
	

	/*
	 * Load professor list, if the current user is a professor, show only
	 * him/her. If current user is admin, show all the professors.
	 */
	public void loadProfessorList() {
		//
		ProfessorRequestFactory rf = GWT.create(ProfessorRequestFactory.class);
		rf.initialize(this.getEventBus(),
				new EventSourceRequestTransport(this.getEventBus()));
		ProfessorRequestContext rc = rf.professorRequest();
		if (currentUser.isProf()) {
			rc.getByEmail(currentUser.getUserEmail()).fire(
					new Receiver<List<ProfessorProxy>>() {
						@Override
						public void onFailure(ServerFailure error) {
							Window.alert(error.getMessage());
						}

						@Override
						public void onSuccess(List<ProfessorProxy> response) {
							getView().setProfListData(response, true);
						}
					});
		}
		if (currentUser.isAdmin()) {
			rc.listAll().fire(new Receiver<List<ProfessorProxy>>() {
				@Override
				public void onFailure(ServerFailure error) {
					Window.alert(error.getMessage());
				}

				@Override
				public void onSuccess(List<ProfessorProxy> response) {
					getView().setProfListData(response, false);
				}
			});
		}
	}
	

	/*
	 * Get the request context for StudenProxy. Used in every function which
	 * call to Request Factory
	 */
	public StudentRequestContext getStudentRequestContext() {
		StudentRequestFactory rf = GWT.create(StudentRequestFactory.class);
		rf.initialize(this.getEventBus(),
				new EventSourceRequestTransport(this.getEventBus()));
		return rf.studentRequest();
	}
	

	/*
	 * */
	@Override
	public void loadAppliedStudentsByDate(String date) {
		//
		CourseSubscriptionRequestFactory rf = GWT
				.create(CourseSubscriptionRequestFactory.class);
		rf.initialize(this.getEventBus(),
				new EventSourceRequestTransport(this.getEventBus()));
		CourseSubscriptionRequestContext rc = rf
				.courseSubscriptionRequestContext();
		rc.listAllByDate(date).fire(
				new Receiver<List<CourseSubscriptionProxy>>() {
					@Override
					public void onFailure(ServerFailure error) {
						Window.alert(error.getMessage());
					}

					@Override
					public void onSuccess(List<CourseSubscriptionProxy> response) {
						getView().setAppliedStudentsTableData(response);
					}
				});
	}
	
	
	/*
	 * */
	@Override
	public void onProfessorSelected(String profId) {
		//
		if (profId.isEmpty()){
			getView().resetForm();
			return;
		}
		//
		AssignmentRequestFactory rf = GWT.create(AssignmentRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		AssignmentRequestContext rc = rf.assignmentRequest();
		rc.listAllActive( profId ).fire(new Receiver<List<AssignmentProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<AssignmentProxy> response) {
				getView().setAssignmentTableData(response);
			}
		});
	}
	
	
	/*
	 * */
	@Override
	public void onAssignmentSelected(String assignmentId) {
		//
		BulletinSubjectRequestFactory rf = GWT.create(BulletinSubjectRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		BulletinSubjectRequestContext rc = rf.bulletinSubjectRequest();
		rc.listAllByAssignment( assignmentId ).fire(new Receiver<List<BulletinSubjectProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<BulletinSubjectProxy> response) {	
				//
				getView().setTableData( response );
			}
		});
	}
	

	/*
	 * Get the current user info when user is logged in
	 */
	@ProxyEvent
	@Override
	public void onLoginAuthenticated(LoginAuthenticatedEvent event) {
		//
		this.currentUser = event.getCurrentUser();
	}
	

	/*
	 * Reload subscription list after a new subscription is added
	 */
	@ProxyEvent
	@Override
	public void onOnSupervisedStudySubscriptionAdded(
			OnSupervisedStudySubscriptionAddedEvent event) {
		//
		loadAppliedStudentsByDate(event.getMessage());
	}

	
	/*
	 * */
	@Override
	public void addCourseSubscription(String studentID, String profID,
			final String date, boolean R, boolean ES, String note,
			String courseID, Repetition rep, String dateRepetitionEnd ) {
		//
		if (profID.equals("")) {
			(new MyAlert(NotificationValues.invalid_input + "Professeur"))
					.center();
			return;
		}
		//
		if (!rep.equals(Repetition.NA)) {
			//
			if (dateRepetitionEnd.equals(date)) {
				(new MyAlert(NotificationValues.invalid_input
						+ "Merci de choisir la date de fin de la répétition."))
						.center();
				return;
			}
			//
			CourseSubscriptionRequestFactory rf = GWT
					.create(CourseSubscriptionRequestFactory.class);
			rf.initialize(this.getEventBus(), new EventSourceRequestTransport(
					this.getEventBus()));
			CourseSubscriptionRequestContext rc = rf
					.courseSubscriptionRequestContext();
			rc.saveAndReturnWithRepetition(studentID, profID, date, R, ES, note, courseID, rep, dateRepetitionEnd )
					.fire(new Receiver<Void>() {
						@Override
						public void onFailure(ServerFailure error) {
							Window.alert(error.getMessage());
						}

						@Override
						public void onSuccess(Void response) {
							//
							getEventBus()
								.fireEvent(
										new OnSupervisedStudySubscriptionAddedEvent(
														date));
							
						}
					});
		} else {
			//
			// Save the subscription as unique
			CourseSubscriptionRequestFactory rf = GWT
					.create(CourseSubscriptionRequestFactory.class);
			rf.initialize(this.getEventBus(), new EventSourceRequestTransport(
					this.getEventBus()));
			CourseSubscriptionRequestContext rc = rf
					.courseSubscriptionRequestContext();
			rc.saveAndReturn(studentID, profID, date, R, ES, note, courseID)
					.fire(new Receiver<CourseSubscriptionProxy>() {
						@Override
						public void onFailure(ServerFailure error) {
							Window.alert(error.getMessage());
						}

						@Override
						public void onSuccess(CourseSubscriptionProxy response) {
							//
							if (response != null)
								getEventBus()
										.fireEvent(
												new OnSupervisedStudySubscriptionAddedEvent(
														date));
							else {
								(new MyAlert( NotificationValues.coursesubscription_studentexist ))
										.center();
							}
						}
					});
		}
	}

	
	/*
	 * */
	@Override
	public void removeCourseSubscription(CourseSubscriptionProxy subscription,
			final String date) {
		//
		CourseSubscriptionRequestFactory rf = GWT
				.create(CourseSubscriptionRequestFactory.class);
		rf.initialize(this.getEventBus(),
				new EventSourceRequestTransport(this.getEventBus()));
		CourseSubscriptionRequestContext rc = rf
				.courseSubscriptionRequestContext();
		rc.removeCourseSubscription(subscription).fire(new Receiver<Void>() {
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert(error.getMessage());
			}

			@Override
			public void onSuccess(Void response) {
				//
				getEventBus().fireEvent(
						new OnSupervisedStudySubscriptionAddedEvent(date));
			}
		});
	}

	
	/*
	 * */
	@Override
	public void saveSubscriptionNote1(
			final CourseSubscriptionProxy subscription, String note,
			final String date) {
		//
		CourseSubscriptionRequestFactory rf = GWT
				.create(CourseSubscriptionRequestFactory.class);
		rf.initialize(this.getEventBus(),
				new EventSourceRequestTransport(this.getEventBus()));
		CourseSubscriptionRequestContext rc = rf
				.courseSubscriptionRequestContext();
		CourseSubscriptionProxy cs = rc.edit(subscription);
		cs.setNote1(note);
		rc.saveAndReturn(cs).fire(new Receiver<CourseSubscriptionProxy>() {
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert(error.getMessage());
			}

			@Override
			public void onSuccess(CourseSubscriptionProxy response) {
				//
				getEventBus().fireEvent(
						new OnSupervisedStudySubscriptionAddedEvent(date));
			}
		});
	}

	
	/*
	 * */
	@Override
	public void saveSubscriptionDetails(CourseSubscriptionProxy subscription,
			String note, final String date, String subjectId, boolean isR,
			boolean isES) {
		//
		CourseSubscriptionRequestFactory rf = GWT
				.create(CourseSubscriptionRequestFactory.class);
		rf.initialize(this.getEventBus(),
				new EventSourceRequestTransport(this.getEventBus()));
		CourseSubscriptionRequestContext rc = rf
				.courseSubscriptionRequestContext();
		//
		CourseSubscriptionProxy cs = rc.edit(subscription);
		rc.saveAndReturn(cs, note, subjectId, isR, isES).fire(
				new Receiver<CourseSubscriptionProxy>() {
					@Override
					public void onFailure(ServerFailure error) {
						Window.alert(error.getMessage());
					}

					@Override
					public void onSuccess(CourseSubscriptionProxy response) {
						//
						getEventBus().fireEvent(
								new OnSupervisedStudySubscriptionAddedEvent(
										date));
					}
				});

	}

	
	
	/*
	 * */
	@Override
	public void removeAllRepetitions(CourseSubscriptionProxy ps, final String curDate) {
		//
		CourseSubscriptionRequestFactory rf = GWT
				.create(CourseSubscriptionRequestFactory.class);
		rf.initialize(this.getEventBus(),
				new EventSourceRequestTransport(this.getEventBus()));
		CourseSubscriptionRequestContext rc = rf
				.courseSubscriptionRequestContext();
		//
		rc.removeAllRepetitions( ps ).fire(
				new Receiver<Void>() {
					@Override
					public void onFailure(ServerFailure error) {
						Window.alert(error.getMessage());
					}

					@Override
					public void onSuccess(Void response) {
						//
						getEventBus().fireEvent(
								new OnSupervisedStudySubscriptionAddedEvent(
										curDate));
					}
				});
	}


	/*
	 * */
	@Override
	public void removeFutureRepetitions(CourseSubscriptionProxy ps, final String curDate ) {
		//
		CourseSubscriptionRequestFactory rf = GWT
				.create(CourseSubscriptionRequestFactory.class);
		rf.initialize(this.getEventBus(),
				new EventSourceRequestTransport(this.getEventBus()));
		CourseSubscriptionRequestContext rc = rf
				.courseSubscriptionRequestContext();
		//
		rc.removeFutureRepetitions( ps, curDate ).fire(
				new Receiver<Void>() {
					@Override
					public void onFailure(ServerFailure error) {
						Window.alert(error.getMessage());
					}

					@Override
					public void onSuccess(Void response) {
						//
						getEventBus().fireEvent(
								new OnSupervisedStudySubscriptionAddedEvent(
										curDate ));
					}
				});
	}

}