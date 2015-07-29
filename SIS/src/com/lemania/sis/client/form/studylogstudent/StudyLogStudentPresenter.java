package com.lemania.sis.client.form.studylogstudent;

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
import com.lemania.sis.client.event.LoginAuthenticatedEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent.LoginAuthenticatedHandler;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.form.studylogstudent.StudyLogStudentLoadLogsEvent.StudyLogStudentLoadLogsHandler;
import com.lemania.sis.client.place.NameTokens;
import com.lemania.sis.client.values.DataValues;
import com.lemania.sis.shared.ClasseProxy;
import com.lemania.sis.shared.bulletin.BulletinProxy;
import com.lemania.sis.shared.bulletin.BulletinRequestFactory;
import com.lemania.sis.shared.bulletin.BulletinRequestFactory.BulletinRequestContext;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectProxy;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectRequestFactory;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectRequestFactory.BulletinSubjectRequestContext;
import com.lemania.sis.shared.service.ClasseRequestFactory;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.ClasseRequestFactory.ClasseRequestContext;
import com.lemania.sis.shared.studylog.StudyLogProxy;
import com.lemania.sis.shared.studylog.StudyLogRequestFactory;
import com.lemania.sis.shared.studylog.StudyLogRequestFactory.StudyLogRequestContext;

public class StudyLogStudentPresenter
		extends
		Presenter<StudyLogStudentPresenter.MyView, StudyLogStudentPresenter.MyProxy>
		implements StudyLogStudentUiHandlers, LoginAuthenticatedHandler,
		StudyLogStudentLoadLogsHandler {

	/*
	 * Thuan
	 */
	private CurrentUser currentUser;
	List<BulletinProxy> bulletins = new ArrayList<BulletinProxy>();

	/*
	 * */
	interface MyView extends View, HasUiHandlers<StudyLogStudentUiHandlers> {
		//
		void resetForm();

		//
		void showAdminPanel(boolean isVisible);

		//
		void setClasseList(List<ClasseProxy> classes);

		//
		void setStudentListData(List<BulletinProxy> bulletins);

		//
		void setSubjectsData(List<BulletinSubjectProxy> bulletinSubjects);

		//
		void showLogs(List<StudyLogProxy> studyLogs);

		//
		void showAddedLog(StudyLogProxy studyLog);

		void showAddedLogs(List<StudyLogProxy> studyLogs);
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> SLOT_StudyLogStudent = new Type<RevealContentHandler<?>>();

	@UseGatekeeper(LoggedInGatekeeper.class)
	@NameToken(NameTokens.studylogstudent)
	@ProxyCodeSplit
	interface MyProxy extends ProxyPlace<StudyLogStudentPresenter> {
	}

	@Inject
	StudyLogStudentPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy, MainPagePresenter.TYPE_SetMainContent);

		getView().setUiHandlers(this);
	}

	protected void onBind() {
		super.onBind();
	}

	protected void onReset() {
		super.onReset();
		//
		getView().resetForm();
		//
		if (currentUser.isAdmin())
			loadClassList();
		if (currentUser.isProf())
			loadClassListByProf();
		if (currentUser.isStudent())
			loadStudentList();
		if (currentUser.isParent())
			loadStudentListByParent();
	}

	/*
	 * */
	private void loadClassListByProf() {
		//
		loadClassList();
	}

	/*
	 * */
	private void loadClassList() {
		//
		getView().showAdminPanel(true);
		//
		ClasseRequestFactory rf = GWT.create(ClasseRequestFactory.class);
		rf.initialize(this.getEventBus(),
				new EventSourceRequestTransport(this.getEventBus()));
		ClasseRequestContext rc = rf.classeRequest();
		rc.listAllActive().fire(new Receiver<List<ClasseProxy>>() {
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert(error.getMessage());
			}

			@Override
			public void onSuccess(List<ClasseProxy> response) {
				getView().setClasseList(response);
			}
		});
	}

	/*
	 * */
	private void loadStudentList() {
		//
		getView().showAdminPanel(false);
		//
		BulletinRequestFactory rf = GWT.create(BulletinRequestFactory.class);
		rf.initialize(this.getEventBus(),
				new EventSourceRequestTransport(this.getEventBus()));
		BulletinRequestContext rc = rf.bulletinRequest();
		//
		if (currentUser.isAdmin() || currentUser.isProf()) {
			rc.listAllByEmail(currentUser.getUserEmail()).fire(
					new Receiver<List<BulletinProxy>>() {
						@Override
						public void onFailure(ServerFailure error) {
							Window.alert(error.getMessage());
						}

						@Override
						public void onSuccess(List<BulletinProxy> response) {
							//
							getView().setStudentListData(response);
							//
							bulletins.clear();
							bulletins.addAll(response);
						}
					});
		} else {
			rc.listAllByEmailForPublic(currentUser.getUserEmail()).fire(
					new Receiver<List<BulletinProxy>>() {
						@Override
						public void onFailure(ServerFailure error) {
							Window.alert(error.getMessage());
						}

						@Override
						public void onSuccess(List<BulletinProxy> response) {
							//
							getView().setStudentListData(response);
							//
							bulletins.clear();
							bulletins.addAll(response);
						}
					});
		}
	}

	/*
	 * */
	private void loadStudentListByParent() {
		//
		// Hide the class list
		getView().showAdminPanel(false);
		//
		BulletinRequestFactory rf = GWT.create(BulletinRequestFactory.class);
		rf.initialize(this.getEventBus(),
				new EventSourceRequestTransport(this.getEventBus()));
		BulletinRequestContext rc = rf.bulletinRequest();
		rc.listAllByParentUserId(currentUser.getUserId().toString()).fire(
				new Receiver<List<BulletinProxy>>() {
					@Override
					public void onFailure(ServerFailure error) {
						Window.alert(error.getMessage());
					}

					@Override
					public void onSuccess(List<BulletinProxy> response) {
						getView().setStudentListData(response);
						//
						bulletins.clear();
						bulletins.addAll(response);
					}
				});
	}

	/*
	 * */
	public String getClassId(String bulletinId) {
		//
		for (BulletinProxy bp : bulletins) {
			//
			if (bp.getId().toString().equals(bulletinId))
				return bp.getClassId().toString();
		}
		return "";
	}

	/*********************** Event Handlers ******************************/

	/*
	 * */
	@ProxyEvent
	@Override
	public void onLoginAuthenticated(LoginAuthenticatedEvent event) {
		//
		this.currentUser = event.getCurrentUser();
	}

	/*
	 * */
	@Override
	@ProxyEvent
	public void onStudyLogStudentLoadLogs(StudyLogStudentLoadLogsEvent event) {
		//
		StudyLogRequestFactory rf = GWT.create(StudyLogRequestFactory.class);
		rf.initialize(this.getEventBus(),
				new EventSourceRequestTransport(this.getEventBus()));
		StudyLogRequestContext rc = rf.studyLogRequestContext();
		if (!event.getSubjectId().equals(DataValues.optionAll)) {
			//
			rc.listAllBySubjectClass(event.getSubjectId(), event.getClassId(),
					event.getDateFrom(), event.getDateTo()).fire(
					new Receiver<List<StudyLogProxy>>() {
						@Override
						public void onFailure(ServerFailure error) {
							Window.alert(error.getMessage());
						}

						@Override
						public void onSuccess(List<StudyLogProxy> response) {
							getView().showLogs(response);
						}
					});
		} else {
			//
			rc.listAllByClass( event.getClassId(), event.getDateFrom(), event.getDateTo() ).fire(
					new Receiver<List<StudyLogProxy>>() {
						@Override
						public void onFailure(ServerFailure error) {
							Window.alert(error.getMessage());
						}

						@Override
						public void onSuccess(List<StudyLogProxy> response) {
							getView().showLogs(response);
						}
					});
		}
	}

	/*********************** UI Handlers ******************************************/

	/*
	 * */
	@Override
	public void onClassChange(String classId) {
		//
		BulletinRequestFactory rf = GWT.create(BulletinRequestFactory.class);
		rf.initialize(this.getEventBus(),
				new EventSourceRequestTransport(this.getEventBus()));
		BulletinRequestContext rc = rf.bulletinRequest();
		rc.listAllActiveByClass(classId).fire(
				new Receiver<List<BulletinProxy>>() {
					@Override
					public void onFailure(ServerFailure error) {
						Window.alert(error.getMessage());
					}

					@Override
					public void onSuccess(List<BulletinProxy> response) {
						//
						getView().setStudentListData(response);
						//
						bulletins.clear();
						bulletins.addAll(response);
					}
				});
	}

	/*
	 * */
	@Override
	public void onBulletinChange(final String bulletinId,
			final String dateFrom, final String dateTo) {
		//
		BulletinSubjectRequestFactory rf = GWT
				.create(BulletinSubjectRequestFactory.class);
		rf.initialize(this.getEventBus(),
				new EventSourceRequestTransport(this.getEventBus()));
		BulletinSubjectRequestContext rc = rf.bulletinSubjectRequest();
		if (currentUser.isAdmin() || currentUser.isProf()) {
			rc.listAll(bulletinId).fire(
					new Receiver<List<BulletinSubjectProxy>>() {
						@Override
						public void onFailure(ServerFailure error) {
							Window.alert(error.getMessage());
						}

						@Override
						public void onSuccess(
								List<BulletinSubjectProxy> response) {
							//
							getView().setSubjectsData(response);
							//
							getEventBus().fireEvent(
									new StudyLogStudentLoadLogsEvent("",
											DataValues.optionAll,
											getClassId(bulletinId), dateFrom,
											dateTo));
						}
					});
		} else {
			rc.listAllForPublic(bulletinId).fire(
					new Receiver<List<BulletinSubjectProxy>>() {
						@Override
						public void onFailure(ServerFailure error) {
							Window.alert(error.getMessage());
						}

						@Override
						public void onSuccess(
								List<BulletinSubjectProxy> response) {
							//
							getView().setSubjectsData(response);
							//
							getEventBus().fireEvent(
									new StudyLogStudentLoadLogsEvent("",
											DataValues.optionAll,
											getClassId(bulletinId), dateFrom,
											dateTo));
						}
					});
		}
	}
	

	/*
	 * */
	@Override
	public void onSubjectChanged(String bulletinId, String subjectId,
			String dateFrom, String dateTo) {
		//
		getEventBus().fireEvent(
				new StudyLogStudentLoadLogsEvent("", subjectId,
						getClassId(bulletinId), dateFrom, dateTo));
	}

}