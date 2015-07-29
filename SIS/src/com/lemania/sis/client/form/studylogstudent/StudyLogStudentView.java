package com.lemania.sis.client.form.studylogstudent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.lemania.sis.client.UI.FieldValidation;
import com.lemania.sis.client.values.DataValues;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.ClasseProxy;
import com.lemania.sis.shared.bulletin.BulletinProxy;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectProxy;
import com.lemania.sis.shared.studylog.StudyLogProxy;

class StudyLogStudentView extends ViewWithUiHandlers<StudyLogStudentUiHandlers> implements StudyLogStudentPresenter.MyView {
    interface Binder extends UiBinder<Widget, StudyLogStudentView> {
    }

    @UiField VerticalPanel pnlAdmin;
    @UiField ListBox lstBulletins;
    @UiField ListBox lstClasses;
    @UiField ListBox lstSubjects;
    @UiField FlexTable tblLogs;
    @UiField DateBox dateFrom;
    @UiField DateBox dateTo;
    @UiField Button cmdFilter;
    @UiField ScrollPanel pnlLogs;

    @Inject
    StudyLogStudentView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /************* UI Preparation ********************/
    
    /*
     * */
	@Override
	public void resetForm() {
		//
		lstBulletins.clear();
		lstClasses.clear();
		lstSubjects.clear();
		//
		tblLogs.removeAllRows();
		//
		initializeDateFields();
		FieldValidation.setDaysOfTheMonth(dateFrom, dateTo);
		//
		// Set the height of the log table
		pnlLogs.setHeight(Window.getClientHeight() - pnlLogs.getAbsoluteTop()
				- NotificationValues.footerHeight + "px");
	}

	/*
	 * */
	@Override
	public void showAdminPanel(boolean isVisible) {
		//
		pnlAdmin.setVisible( isVisible );
	}
	
	/*
	 * */
	@Override
	public void setClasseList(List<ClasseProxy> classes) {
		//
		lstClasses.clear();
		lstClasses.addItem("-","");
		for (ClasseProxy clazz : classes) {
			lstClasses.addItem( clazz.getClassName(), clazz.getId().toString());
		}
	}
	
	/*
	 * */
	@Override
	public void setStudentListData(List<BulletinProxy> bulletins) {
		//
		lstBulletins.clear();
		lstBulletins.addItem("-","");
		for (BulletinProxy bulletin : bulletins) {
			if ( !bulletin.getIsFinished() )
				lstBulletins.addItem( bulletin.getStudentName() + " - " + bulletin.getClasseName() + " - " + bulletin.getYear(), 
					bulletin.getId().toString());
		}
	}
	
	/*
	 * */
	@Override
	public void setSubjectsData(List<BulletinSubjectProxy> bulletinSubjects) {
		//
		lstSubjects.clear();
		lstSubjects.addItem("Toutes les matières", DataValues.optionAll );
		for ( BulletinSubjectProxy bs : bulletinSubjects ) {
			lstSubjects.addItem( bs.getSubjectName(), bs.getSubjectId() );
		}
	}
	
	/*
	 * */
	@Override
	public void showLogs(List<StudyLogProxy> studyLogs) {
		//
		tblLogs.removeAllRows();
		showAddedLogs( studyLogs );
	}
	
	
	/*
	 * */
	@Override
	public void showAddedLog(StudyLogProxy studyLog) {
		//
		List<StudyLogProxy> logs = new ArrayList<StudyLogProxy>();
		logs.add( studyLog );
		showAddedLogs( logs );
	}
	
	
	/*
	 * Show the logs and populate UI automatically
	 * */
	@Override
	public void showAddedLogs(List<StudyLogProxy> studyLogs) {
		// 
		VerticalPanel vp;
		HorizontalPanel pnlLogTitle, pnlButton, pnlDate;
		String prevDate = "";
		Label logContent, logTitle, logId, logClass;
		//
		for ( final StudyLogProxy studyLog : studyLogs ) {
			//
			if ( !prevDate.equals(studyLog.getLogDate()) ) {
				pnlDate = new HorizontalPanel();
				pnlDate.setStyleName("slDateLine");
				pnlDate.add( new Label( FieldValidation.swissDateFormat( studyLog.getLogDate() ) ));
				tblLogs.setWidget( tblLogs.getRowCount(), 0, pnlDate );
				prevDate = studyLog.getLogDate();
			}
			//
			vp = new VerticalPanel();
			vp.setWidth("100%");
			//
			logId = new Label( studyLog.getId().toString() );
			logId.setVisible(false);
			vp.add( logId );
			//
			pnlLogTitle = new HorizontalPanel();
			pnlLogTitle.setStyleName("slTitleLine");
			pnlLogTitle.setVerticalAlignment( HasVerticalAlignment.ALIGN_MIDDLE );
			//
			logTitle = new Label( studyLog.getLogTitle());
			pnlLogTitle.add( logTitle );
			//
			logClass = new Label(studyLog.getClasseName());
			pnlLogTitle.add( logClass );
			//
			pnlLogTitle.add( new Label( studyLog.getSubjectName() ));
			//
			pnlLogTitle.setCellWidth(logTitle, "50%");
			pnlLogTitle.setCellWidth(logClass, "10%");
			//
			// Edit and Delete buttons
			pnlButton = new HorizontalPanel();
			pnlButton.setWidth("100%");
			pnlButton.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_RIGHT );
			//
			pnlLogTitle.add( pnlButton );
			pnlLogTitle.setCellWidth( pnlButton, "20%");
			//
			vp.add( pnlLogTitle );
			//
			logContent = new Label( studyLog.getLogContent() );
			logContent.setStyleName("slLogLine");
			vp.add( logContent );
			//
			// Show attached file
			if (!studyLog.getFileName().equals("")) {
				Anchor lnkLogFileName = new Anchor();
				lnkLogFileName.setText(studyLog.getFileName());
				lnkLogFileName.setHref( "/gcs//" + studyLog.getFileName() );
				lnkLogFileName.setTarget( "_blank" );
				lnkLogFileName.setStyleName("slLogLine");
				vp.add(lnkLogFileName);
			}
			//
			tblLogs.setWidget( tblLogs.getRowCount(), 0, vp);
		}
	}
	
	
	/*
	 * */
	private void initializeDateFields() {
		//
		dateFrom.setFormat(new DateBox.DefaultFormat(DateTimeFormat
				.getFormat("dd.MM.yyyy")));
		dateFrom.addValueChangeHandler(new ValueChangeHandler<Date>() {
			int pog = 0;

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				//
				if (pog == 0) {
					pog++;
				} else {
					pog = 0;
				}
			}
		});
		dateFrom.setValue(new Date());
		//
		dateTo.setFormat(new DateBox.DefaultFormat(DateTimeFormat
				.getFormat("dd.MM.yyyy")));
		dateTo.addValueChangeHandler(new ValueChangeHandler<Date>() {
			int pog = 0;

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				//
				if (pog == 0) {
					pog++;
				} else {
					pog = 0;
				}
			}
		});
		dateTo.setValue(new Date());
	}
	
	/****************** UI Handlers ********************************/
	
	
	/*
	 * */
	@UiHandler("cmdFilter")
	void onCmdFilterClicked(ClickEvent event) {
		//
		onLstSubjectsChange( null );
	}
	
	
	/*
	 * */
	@UiHandler("lstClasses")
	void onLstClassesChange(ChangeEvent event) {
		if (getUiHandlers() != null)
			getUiHandlers().onClassChange(lstClasses.getValue(lstClasses.getSelectedIndex()));
	}
	
	/*
	 * */
	@UiHandler("lstBulletins")
	void onLstBulletinsChange(ChangeEvent event) {
		//		
		lstSubjects.clear();
		//
		if (getUiHandlers() != null)
			getUiHandlers().onBulletinChange( lstBulletins.getValue( lstBulletins.getSelectedIndex() ),
					DateTimeFormat.getFormat("yyyyMMdd").format( dateFrom.getValue() ),
					DateTimeFormat.getFormat("yyyyMMdd").format( dateTo.getValue() )
					);
	}

	/*
	 * */
	@UiHandler("lstSubjects")
	void onLstSubjectsChange(ChangeEvent event) {
		//
		if (getUiHandlers() != null)
			getUiHandlers().onSubjectChanged( 
					lstBulletins.getValue( lstBulletins.getSelectedIndex() ),
					lstSubjects.getValue( lstSubjects.getSelectedIndex() ), 
					DateTimeFormat.getFormat("yyyyMMdd").format( dateFrom.getValue() ),
					DateTimeFormat.getFormat("yyyyMMdd").format( dateTo.getValue() )
					);
	}
}