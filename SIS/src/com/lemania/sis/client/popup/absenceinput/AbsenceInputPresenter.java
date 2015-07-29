package com.lemania.sis.client.popup.absenceinput;

import java.util.List;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.PopupView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.lemania.sis.client.event.AbsenceAfterInputEvent;
import com.lemania.sis.shared.absenceitem.AbsenceItemProxy;
import com.lemania.sis.shared.absenceitem.AbsenceItemRequestFactory;
import com.lemania.sis.shared.absenceitem.AbsenceItemRequestFactory.AbsenceItemRequestContext;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectProxy;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectRequestFactory;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectRequestFactory.BulletinSubjectRequestContext;
import com.lemania.sis.shared.period.PeriodProxy;
import com.lemania.sis.shared.period.PeriodRequestFactory;
import com.lemania.sis.shared.period.PeriodRequestFactory.PeriodRequestContext;
import com.lemania.sis.shared.service.EventSourceRequestTransport;

public class AbsenceInputPresenter extends PresenterWidget<AbsenceInputPresenter.MyView>
		implements AbsenceInputUiHandlers {
	
	
	//
	private String studentId;
	private String classId = "";

	public interface MyView extends PopupView, HasUiHandlers<AbsenceInputUiHandlers>  {
		//
		void setStudentListData( List<BulletinSubjectProxy> bulletinSubjects );
		//
		void setPeriodListData(List<PeriodProxy> periods);
		//
		void resetUI(String studentName);
		//
		void setAddedAbsenceItem(AbsenceItemProxy aip);
		//
		void removeDeletedAbsenceItemId();
	}

	@Inject
	public AbsenceInputPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
		//
		getView().setUiHandlers(this);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}

	@Override
	protected void onReset() {
		super.onReset();
	}
	
	
	/*
	 * Load the 
	 * */
	public void onPopupStart(String studentId, String studentName) {
		//
		this.studentId = studentId;
		//
		getView().resetUI( studentName );
		//
		loadStudentCourses();
	}
	
	
	/* 
	 * Load list of subjects and profs
	 * When finish laoding the subjects and profs, load the periods by event
	 * */
	private void loadStudentCourses() {
		//
		BulletinSubjectRequestFactory rf = GWT.create(BulletinSubjectRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		BulletinSubjectRequestContext rc = rf.bulletinSubjectRequest();
		rc.listAllByStudent( this.studentId ).fire(new Receiver<List<BulletinSubjectProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				//
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<BulletinSubjectProxy> response) {				
				//
				classId = response.get(0).getClassId();			// Keep the class ID to load the periods
				//
				getView().setStudentListData(response);
				//
				loadPeriods();
			}
		});
	}

	
	/*
	 * */
	public void loadPeriods() {
		//
		PeriodRequestFactory rf = GWT.create(PeriodRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		PeriodRequestContext rc = rf.periodRequestContext();
		rc.listAllByClass( classId ).fire(new Receiver<List<PeriodProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<PeriodProxy> response) {
				//
				getView().setPeriodListData(response);
			}
		});
	}

	
	/*
	 * */
	@Override
	public void saveAbsenceItem(String strAbsenceDate, String studentId,
			String periodId, String profId, String classId, String subjectId,
			String motifId, String codeAbsence, String profComment,
			int lateMinute, boolean justified, boolean parentNotified) {
		// 
		AbsenceItemRequestFactory rf = GWT.create(AbsenceItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		AbsenceItemRequestContext rc = rf.absenceItemRequestContext();
		rc.saveAbsenceItem(strAbsenceDate, studentId, periodId, profId, classId, subjectId, motifId, codeAbsence, profComment, lateMinute, justified, parentNotified)
			.fire(new Receiver<AbsenceItemProxy>() {
				@Override
				public void onFailure(ServerFailure error){
					//
					Window.alert(error.getMessage());
				}
				@Override
				public void onSuccess( AbsenceItemProxy response ) {
					//
					getView().setAddedAbsenceItem( response );
					//
					getEventBus().fireEvent( new AbsenceAfterInputEvent() );
				}
			});
	}
	

	/*
	 * */
	@Override
	public void removeAbsenceItem(String aiID) {
		//
		AbsenceItemRequestFactory rf = GWT.create(AbsenceItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		AbsenceItemRequestContext rc = rf.absenceItemRequestContext();
		rc.removeAbsenceItem( aiID ).fire(new Receiver<Void>(){
				@Override
				public void onFailure(ServerFailure error){
					//
					Window.alert(error.getMessage());
				}
				@Override
				public void onSuccess( Void response ) {
					//
					getView().removeDeletedAbsenceItemId();
					//
					getEventBus().fireEvent( new AbsenceAfterInputEvent() );
				}
			});
	}

	
	/*
	 * */
	@Override
	public void closePopup() {
		//
		this.getView().hide();
	}
	
}
