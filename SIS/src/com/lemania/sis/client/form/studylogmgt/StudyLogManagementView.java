package com.lemania.sis.client.form.studylogmgt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.lemania.sis.client.UI.FieldValidation;
import com.lemania.sis.client.values.DataValues;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.ClasseProxy;
import com.lemania.sis.shared.ProfessorProxy;
import com.lemania.sis.shared.SubjectProxy;
import com.lemania.sis.shared.assignment.AssignmentProxy;
import com.lemania.sis.shared.bulletin.BulletinProxy;
import com.lemania.sis.shared.studylog.StudyLogProxy;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;

class StudyLogManagementView extends ViewWithUiHandlers<StudyLogManagementUiHandlers>
		implements StudyLogManagementPresenter.MyView {

	private final Widget widget;

	interface Binder extends UiBinder<Widget, StudyLogManagementView> {
	}

	@Inject
	public StudyLogManagementView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@UiField
	ListBox lstProfs;
//	@UiField
//	ListBox lstAssignments;
//	@UiField
//	ListBox lstClasses;
	@UiField
	FlexTable tblStudents;
	@UiField
	Button cmdFilter;
	@UiField
	Button cmdSave;
	@UiField
	Button cmdAdd;
	@UiField
	DateBox dateFrom;
	@UiField
	DateBox dateTo;
	@UiField
	ScrollPanel pnlStudents;
	@UiField
	ScrollPanel pnlLogs;
	@UiField
	Label lblStudentQty;
	@UiField
	VerticalPanel pnlAdd;
	@UiField
	VerticalPanel pnlFileUpload;
	@UiField
	TextArea txtContent;
	@UiField
	TextBox txtTitle;
	@UiField
	FlexTable tblLogs;
	@UiField
	Label lblSubject;
	@UiField
	Label lblClass;
	@UiField
	Label lblEditLogId;
	@UiField
	Button cmdClose;
	@UiField
	DateBox dateEntry;
	@UiField
	HorizontalPanel pnlLogEntryButtons;
	
	@UiField VerticalPanel pnlLogAddAssignments;
	
	@UiField FlexTable tblAssignments;
	@UiField FlexTable tblAssignmentView;

	@UiField Label cmdSelectAll;
	@UiField Label cmdDeselectAll;

	
	//
	private DialogBox pp;
	private String logFileName = "";
	private List<String> assignmentIDs = new ArrayList<String>();		// | deliminated string that contains list of assignments Ids
	private List<String> assignmentViewIDs = new ArrayList<String>();
	private Boolean isFileUploaded = false;								// set true when a file is uploaded to save the file name
	
	
	/*
	 * 
	 * */
	@UiHandler("lstProfs")
	void onLstProfsChange(ChangeEvent event) {
		//
		clearStudentList();
		//
		clearLogList();
		//
		resetAssignments();
		//
		if (getUiHandlers() != null)
			getUiHandlers().onProfessorSelected(lstProfs.getValue(lstProfs.getSelectedIndex()));
	}

	/*
	 * 
	 * */
	@Override
	public void setProfListData(List<ProfessorProxy> profs) {
		//
		lstProfs.clear();
		lstProfs.addItem("-", "");
		for (ProfessorProxy prof : profs)
			lstProfs.addItem(prof.getProfName(), prof.getId().toString());
		//
		// automatically select the first prof if this list contains only one professor
		if ( lstProfs.getItemCount() == 2 ) {
			lstProfs.setSelectedIndex(1);
			onLstProfsChange( null );
		}
	}

	/*
	 * 
	 * */
//	@Override
//	public void setSubjectsData(List<SubjectProxy> subjects) {
//		//
//		lstAssignments.clear();
//		lstAssignments.addItem("-", "");
//		for (SubjectProxy sub : subjects) {
//			lstAssignments.addItem(sub.getSubjectName(), sub.getId().toString());
//		}
//	}

	/*
	 * 
	 * */
	@Override
	public void resetForm() {
		//
		lstProfs.clear();
//		lstAssignments.clear();
//		lstClasses.clear();
		//
		clearStudentList();
		//
		tblLogs.removeAllRows();
		//
		resetAssignments();
		//
		// Set the height of the Student scroll panel
		pnlStudents.setHeight(
				Window.getClientHeight() - pnlStudents.getAbsoluteTop() - NotificationValues.footerHeight + "px");
		//
		// Set the height of the log table
		pnlLogs.setHeight(Window.getClientHeight() - pnlLogs.getAbsoluteTop() - NotificationValues.footerHeight + "px");
		//
//		FieldValidation.setDateRangeCurrentMonth( dateFrom, dateTo );
		FieldValidation.setDateRangeCurrentWeek( dateFrom, dateTo );
	}
	
	
	/* */
	public void resetAssignments(){
		//
		tblAssignmentView.removeAllRows();
		tblAssignments.removeAllRows();
		//
		assignmentIDs.clear();
		assignmentViewIDs.clear();
	}
	
	

	/*************************
	 * Data Population
	 ****************************************/

//	/*
//	 * */
//	@Override
//	public void setClassListData(List<ClasseProxy> classes) {
//		//
//		lstClasses.clear();
//		lstClasses.addItem("- Choisir", "");
//		lstClasses.addItem("* Toutes les classes", DataValues.optionAll);
//		for (ClasseProxy cl : classes) {
//			lstClasses.addItem(cl.getClassName(), cl.getId().toString());
//		}
//	}

	/*
	 * */
	@Override
	public void setStudentListData(List<BulletinProxy> bulletins) {
		//
		tblStudents.removeAllRows();
		int row = 0;
		int col = 0;
		for (BulletinProxy sp : bulletins) {
			tblStudents.setText(row, col + 1, sp.getClasseName());
			tblStudents.setText(row, col + 2, sp.getStudentName());
			row++;
		}
		//
		lblStudentQty.setText(Integer.toString(bulletins.size()));
	}

	/*
	 * */
	@Override
	public void showAddedLog(StudyLogProxy studyLog) {
		//
		List<StudyLogProxy> logs = new ArrayList<StudyLogProxy>();
		logs.add(studyLog);
		//
		showAddedLogs(logs);
		//
		if (pp != null)
			pp.hide();
	}

	/*
	 * Show the logs and populate UI automatically
	 */
	@Override
	public void showAddedLogs(List<StudyLogProxy> studyLogs) {
		//
		VerticalPanel vp;
		HorizontalPanel pnlLogTitle, pnlButton, pnlDate;
		Button cmdEdit, cmdDelete;
		String prevDate = "";
		Label logContent, logTitle, logId, logClass;
		//
		HorizontalPanel pnlFileLinks;
		//
		for (final StudyLogProxy studyLog : studyLogs) {
			//
			if (!prevDate.equals(studyLog.getLogDate())) {
				pnlDate = new HorizontalPanel();
				pnlDate.setStyleName("slDateLine");
				pnlDate.add(new Label(FieldValidation.swissDateFormat(studyLog.getLogDate())));
				tblLogs.setWidget(tblLogs.getRowCount(), 0, pnlDate);
				prevDate = studyLog.getLogDate();
			}
			//
			vp = new VerticalPanel();
			vp.setWidth("100%");
			//
			logId = new Label(studyLog.getId().toString());
			logId.setVisible(false);
			vp.add(logId);
			//
			pnlLogTitle = new HorizontalPanel();
			pnlLogTitle.setStyleName("slTitleLine");
			pnlLogTitle.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			//
			logTitle = new Label(studyLog.getLogTitle());
			pnlLogTitle.add(logTitle);
			//
			logClass = new Label(studyLog.getClasseName());
			pnlLogTitle.add(logClass);
			//
			pnlLogTitle.add(new Label(studyLog.getSubjectName()));
			//
			pnlLogTitle.setCellWidth(logTitle, "50%");
			pnlLogTitle.setCellWidth(logClass, "10%");
			//
			// Edit and Delete buttons
			pnlButton = new HorizontalPanel();
			pnlButton.setWidth("100%");
			pnlButton.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			//
			cmdEdit = new Button("Editer");
			cmdEdit.setStyleName("darkBlueButton");
			cmdEdit.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					//
					if (pp != null) {
						lblSubject.setText(studyLog.getSubjectName());
						lblClass.setText(studyLog.getClasseName());
						txtTitle.setText(studyLog.getLogTitle());
						txtContent.setText(studyLog.getLogContent());
						lblEditLogId.setText(studyLog.getId().toString());
						isFileUploaded = false;
						pnlLogAddAssignments.setVisible(false);
						pnlAdd.setVisible(true);
						pp.center();
					}
				}

			});
			pnlButton.add(cmdEdit);
			//
			cmdDelete = new Button("Supprimer");
			cmdDelete.setStyleName("redButton");
			cmdDelete.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					//
					if (Window.confirm("Etes-vous sûr de vouloir supprimer ce texte ?")) {
						getUiHandlers().deleteLog(studyLog);
					}
				}

			});
			pnlButton.add(cmdDelete);
			//
			pnlLogTitle.add(pnlButton);
			pnlLogTitle.setCellWidth(pnlButton, "20%");
			//
			vp.add(pnlLogTitle);
			//
			// Show log content
			logContent = new Label(studyLog.getLogContent());
			logContent.setStyleName("slLogLine");
			//
			vp.add(logContent);
			//
			// Show attached file
			pnlFileLinks = new HorizontalPanel();
			if (!studyLog.getFileName().equals("")) {
				//
				showLogFile( pnlFileLinks, studyLog );
			}
			//
			vp.add( pnlFileLinks );
			//
			tblLogs.setWidget(tblLogs.getRowCount(), 0, vp);
		}
		//
		// Closoe the popup just in case
		if (pp != null)
			pp.hide();
	}
	
	
	/*
	 * */
	void showLogFile( HorizontalPanel pnlFileLinks, final StudyLogProxy studyLog ) {
		//
		Anchor lnkLogFileName;
		Label lnkDeleteFile;
		//
		lnkLogFileName = new Anchor();
		lnkLogFileName.setText(studyLog.getFileName());
		lnkLogFileName.setHref("/gcs/eprofil-uploads/" + studyLog.getFileName());
		lnkLogFileName.setTarget("_blank");
		lnkLogFileName.setStyleName("slLogLine");
		//
		pnlFileLinks.add( lnkLogFileName );
		//
		lnkDeleteFile = new Label("Supprimer");
		lnkDeleteFile.setStyleName("redLink");
		lnkDeleteFile.addClickHandler( new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				//
				ConfirmMessageBox messageBox = new ConfirmMessageBox("Supprimer un fichier", "Etes-vous sûr de vouloir supprimer le fichier : " + studyLog.getFileName() + " ?");
				messageBox.addDialogHideHandler(new DialogHideHandler() {
				      @Override
				      public void onDialogHide(DialogHideEvent event) {
				        switch (event.getHideButton()) {
				          case YES:
				        	  //
				        	  getUiHandlers().removeStudyLogFile( studyLog );
				        	  break;
				          case NO:
				        	  //perform NO action
				        	  break;
				          default:
				        	  //error, button added with no specific action ready
				        }
				      }
				    });
				messageBox.setWidth(300);
				messageBox.show();
			}
			
		});
		//
		pnlFileLinks.add( new Label(" -- "));
		pnlFileLinks.add( lnkDeleteFile );
	}
	
	
	

	/*
	 * */
	@Override
	public void showLogs(List<StudyLogProxy> studyLogs) {
		//
		tblLogs.removeAllRows();
		showAddedLogs(studyLogs);
	}

	/*
	 * */
	@Override
	public void removeDeletedLog(String logId) {
		//
		VerticalPanel vp;
		for (int i = 0; i < tblLogs.getRowCount(); i++) {
			if (tblLogs.getWidget(i, 0) instanceof VerticalPanel) {
				vp = (VerticalPanel) tblLogs.getWidget(i, 0);
				if (((Label) vp.getWidget(0)).getText().equals(logId)) {
					tblLogs.remove(vp);
				}
			}
		}
	}

	/*
	 * 20150813 - BUG FIX - Update edited title ((HorizontalPanel)
	 * 		vp.getWidget(1)).getWidget(2)) -> ((HorizontalPanel)
	 * 		vp.getWidget(1)).getWidget(0))
	 * 20150901 - Always refresh the log list to have all the latest data
	 */
	@Override
	public void showUpdatedLog( StudyLogProxy updatedLog ) {
		//
//		VerticalPanel vp;
//		HorizontalPanel pnlFileLinks;
//		for (int i = 0; i < tblLogs.getRowCount(); i++) {
//			if (tblLogs.getWidget(i, 0) instanceof VerticalPanel) {
//				vp = (VerticalPanel) tblLogs.getWidget(i, 0);
//				if (((Label) vp.getWidget(0)).getText().equals( updatedLog.getId().toString() )) {
//					//
//					((Label) ((HorizontalPanel) vp.getWidget(1)).getWidget(0)).setText( updatedLog.getLogTitle() );
//					((Label) vp.getWidget(2)).setText( updatedLog.getLogContent() );
//					//
//					//
//					pnlFileLinks = (HorizontalPanel) vp.getWidget(3);
//					if ( updatedLog.getFileName() != "" ) {
//						//
//						showLogFile( pnlFileLinks, updatedLog );
//					} else {
//						for (int j=pnlFileLinks.getWidgetCount()-1; j>-1; j--)
//							pnlFileLinks.getWidget(j).removeFromParent();
//					}
//				}
//			}
//		}
		//
		loadStudyLogs();
		//
		if (pp != null)
			pp.hide();
	}
	
	
	/*
	 * */
	@Override
	public void setAssignmentsData(List<AssignmentProxy> assignments) {
		//
		int row = 0;
		int colClassId = 0, colSubject = 1, colClass = 2;
		Label lblAssignmentId;
		CheckBox chkAssignment;
		HorizontalPanel hp;
		for ( AssignmentProxy ap : assignments ) {
			//
			hp = new HorizontalPanel();
			//
			lblAssignmentId = new Label( ap.getId().toString() );
			lblAssignmentId.setVisible(false);
			hp.add( lblAssignmentId );
			//
			chkAssignment = new CheckBox();
			chkAssignment.addValueChangeHandler( new ValueChangeHandler<Boolean>(){

				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					//
					if ( ((CheckBox)event.getSource()).getValue() ) {
						assignmentIDs.add( ((Label)((HorizontalPanel)((CheckBox)event.getSource()).getParent()).getWidget(0)).getText() );
					}
					else { 
						assignmentIDs.remove( ((Label)((HorizontalPanel)((CheckBox)event.getSource()).getParent()).getWidget(0)).getText() );
					}
				}
				
			});
			hp.add( chkAssignment );
			tblAssignments.setWidget( row, colClassId, hp );
			//
			tblAssignments.setText( row, colSubject, ap.getSubjectName() );
			tblAssignments.setText( row, colClass, ap.getClasseName() );
			//
			row++;
		}
	}
	
	
	
	/*
	 * */
	@Override
	public void setAssignmentViewData(List<AssignmentProxy> assignments) {
		//
		int row = 0;
		int colClassId = 0, colSubject = 1, colClass = 2;
		Label lblAssignmentId;
		CheckBox chkAssignment;
		HorizontalPanel hp;
		for ( AssignmentProxy ap : assignments ) {
			//
			hp = new HorizontalPanel();
			//
			lblAssignmentId = new Label( ap.getId().toString() );
			lblAssignmentId.setVisible(false);
			hp.add( lblAssignmentId );
			//
			chkAssignment = new CheckBox();
			chkAssignment.addValueChangeHandler( new ValueChangeHandler<Boolean>(){

				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					//
					if ( ((CheckBox)event.getSource()).getValue() ) {
						assignmentViewIDs.add( ((Label)((HorizontalPanel)((CheckBox)event.getSource()).getParent()).getWidget(0)).getText() );
					}
					else { 
						assignmentViewIDs.remove( ((Label)((HorizontalPanel)((CheckBox)event.getSource()).getParent()).getWidget(0)).getText() );
					}
				}
				
			});
			hp.add( chkAssignment );
			tblAssignmentView.setWidget( row, colClassId, hp );
			//
			tblAssignmentView.setText( row, colSubject, ap.getSubjectName() );
			tblAssignmentView.setText( row, colClass, ap.getClasseName() );
			//
			row++;
		}
	}
	

	/*************************
	 * Controls Events
	 ****************************************/

//	/*
//	 * */
//	@UiHandler("lstClasses")
//	public void onLstClassesChange(ChangeEvent event) {
//		//
//		if (lstClasses.getValue(lstClasses.getSelectedIndex()).equals("")) {
//			clearStudentList();
//			clearLogList();
//			return;
//		}
//		//
//		if (getUiHandlers() != null)
//			getUiHandlers().onLstClassChange(lstProfs.getValue(lstProfs.getSelectedIndex()),
//					lstAssignments.getValue(lstAssignments.getSelectedIndex()),
//					lstClasses.getValue(lstClasses.getSelectedIndex()),
//					DateTimeFormat.getFormat("yyyyMMdd").format(dateFrom.getValue()),
//					DateTimeFormat.getFormat("yyyyMMdd").format(dateTo.getValue()));
//	}

	/*
	 * When user select a subject, load the class list and student list
	 */
//	@UiHandler("lstAssignments")
//	void onLstAssignmentsChange(ChangeEvent event) {
//		//
//		clearStudentList();
//		clearLogList();
//		//
//		if (getUiHandlers() != null)
//			getUiHandlers().onLstAssignmentsChange(lstProfs.getValue(lstProfs.getSelectedIndex()),
//					lstAssignments.getValue(lstAssignments.getSelectedIndex()));
//	}

	/*
	 * If for saving an existing log, do not put class Id in the call
	 * To update, no need subjectId and classId
	 */
	@UiHandler("cmdSave")
	void onCmdSaveClicked(ClickEvent event) {
		//
		if (getUiHandlers() != null) {
			if (lblEditLogId.getText().equals("")) {		// If this is a new text
				//
					getUiHandlers().onStudyLogAdd(
							lstProfs.getValue(lstProfs.getSelectedIndex()),
							"",
							"", 
							txtTitle.getText(), 
							txtContent.getText(),
							"", 
							logFileName, 
							DateTimeFormat.getFormat("yyyyMMdd").format(dateEntry.getValue()), 
							assignmentIDs );
				
			} else {
				//
				if (isFileUploaded){						// If editing an existing log, and a file is uploaded, send the file name
					getUiHandlers().onStudyLogAdd(
						lstProfs.getValue(lstProfs.getSelectedIndex()),
						"", 
						"", 
						txtTitle.getText(),
						txtContent.getText(), 
						lblEditLogId.getText(), 
						logFileName,
						DateTimeFormat.getFormat("yyyyMMdd").format(dateEntry.getValue()),
						assignmentIDs );
				} else {						// If a file is NOT loaded, send the file name as empty
					getUiHandlers().onStudyLogAdd(
						lstProfs.getValue(lstProfs.getSelectedIndex()),
						"", 
						"", 
						txtTitle.getText(),
						txtContent.getText(), 
						lblEditLogId.getText(), 
						"",
						DateTimeFormat.getFormat("yyyyMMdd").format(dateEntry.getValue()),
						assignmentIDs );
				}
			}
		}

	}

	
	/*
	 * */
	@UiHandler("cmdAdd")
	void onCmdAddClicked(ClickEvent event) {
		//
		if (lstProfs.getValue(lstProfs.getSelectedIndex()).equals(""))
			return;
		//
		if (pp != null) {
			//
			lblSubject.setText("");
			lblClass.setText("");
			//
			lblEditLogId.setText("");
			//
			resetAssignmentSelection();
			//
			pnlLogAddAssignments.setVisible(true);
			pnlAdd.setVisible(true);
			//
			resetFileUpload();				// clear the file upload field
			//
			pp.center();
		}
	}
	
	
	/*
	 * */
	public void resetFileUpload() {
		//
		logFileName = "";
		isFileUploaded = false;
	}
	
	
	/*
	 * */
	public void resetAssignmentSelection() {
		//
		assignmentIDs.clear();
		for (int row=0; row<tblAssignments.getRowCount(); row++) {
			((CheckBox)((HorizontalPanel)tblAssignments.getWidget( row , 0)).getWidget(1)).setValue(false);
		}
	}

	
	/*
	 * */
	@UiHandler("cmdClose")
	void onCmdCloseClicked(ClickEvent event) {
		//
		if (pp != null) {
			if (txtTitle.getText().equals("") && txtContent.getText().equals("")) {
				pp.hide();
				return;
			}
			//
			pp.hide();
		}
	}

	/*
	 * */
	@UiHandler("cmdFilter")
	void onCmdShow(ClickEvent event) {
		//
//		onLstClassesChange(null);
		//
		loadStudyLogs();
	}
	
	
	/*
	 * Load the study logs with the chosen date and assignments
	 * */
	public void loadStudyLogs() {
		//
		getUiHandlers().loadStudyLogs(
				DateTimeFormat.getFormat("yyyyMMdd").format(dateFrom.getValue()),
				DateTimeFormat.getFormat("yyyyMMdd").format(dateTo.getValue()),
				assignmentViewIDs );
	}
	
	

	/*************************
	 * UI Manipulation
	 ****************************************/

	/*
	 * */
	public void clearStudentList() {
		//
		// Clear student list
		tblStudents.removeAllRows();
		lblStudentQty.setText("");
	}

	/*
	 * */
	public void clearLogList() {
		//
		tblLogs.removeAllRows();
	}

	/*************************
	 * UI Preparation
	 ****************************************/

	/*
	 * */
	@Override
	public void initializeUI() {
		//
		pnlAdd.setVisible(false);
		//
		initializeDateFields();
		//
		initializeRichTextArea();
		//
		initializeAddPopup();
		//
		initializeSelectButtons();
	}

	/*
	 * */
	private void initializeSelectButtons() {
		//
		cmdDeselectAll.addClickHandler( new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// 
				assignmentViewIDs.clear();
				for (int row=0; row<tblAssignmentView.getRowCount(); row++) {
					((CheckBox)((HorizontalPanel)tblAssignmentView.getWidget( row , 0)).getWidget(1)).setValue(false);
				}
			}
			
		});
		//
		cmdSelectAll.addClickHandler( new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// 
				assignmentViewIDs.clear();
				for (int row=0; row<tblAssignmentView.getRowCount(); row++) {
					((CheckBox)((HorizontalPanel)tblAssignmentView.getWidget( row , 0)).getWidget(1)).setValue(true);
					assignmentViewIDs.add( ((Label)((HorizontalPanel)(tblAssignmentView.getWidget( row , 0))).getWidget(0)).getText() );
				}
			}
			
		});
	}

	/*
	 * */
	private void initializeAddPopup() {
		//
		pp = new DialogBox();
		pp.setGlassEnabled(true);
		pp.setText("Cahier de textes");
		pp.add(pnlAdd);
		//
		FlexTable mainPanel = new FlexTable();
		mainPanel.setWidth("100%");
		mainPanel.setStyleName("subSection");
		//
		final FormPanel formPanel = new FormPanel();
		formPanel.setMethod(FormPanel.METHOD_POST);
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);

		final FileUpload upload = new FileUpload();
		upload.setName("file");

		Button btn = new Button("Télécharger");
		btn.setStyleName("darkBlueButton");

		final Image status = new Image();
		status.setWidth("35px");
		status.setHeight("35px");

		formPanel.setWidget(mainPanel);
		formPanel.setWidth("100%");
		mainPanel.setWidget(0, 0, upload); // file
		upload.setWidth("400px");
		mainPanel.setWidget(0, 1, btn); // submit
		mainPanel.setWidget(0, 2, status);

		initWidget(formPanel);
		//
		btn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (txtTitle.getText().equals("") || upload.getFilename().equals("")) {
					Window.alert(NotificationValues.invalid_input + " - Titre & Fichier");
				} else {
					//
					pnlLogEntryButtons.setVisible(false);
					formPanel.setAction("/gcs/eprofil-uploads/" + FieldValidation.getFileNameFormat(
							DateTimeFormat.getFormat("yyyyMMdd").format(dateEntry.getValue()), upload.getFilename()));
					formPanel.submit();
					status.setUrl("images/processing.gif");
				}
			}
		});
		//
		formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				//
				pnlLogEntryButtons.setVisible(true);
				status.setUrl("images/done.png");
				logFileName = upload.getFilename();
				isFileUploaded = true;
			}
		});
		//
		pnlFileUpload.add(formPanel);
		//
		// When popup is closed, clear all the fields
		pp.addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				//
				pp.hide();
				txtTitle.setText("");
				txtContent.setText("");
				lblEditLogId.setText("");
				upload.getElement().setPropertyString("value", "");
				status.setUrl("");
				dateEntry.setValue(new Date());
				pnlAdd.setVisible(false);
			}

		});
	}

	/*
	 * */
	private void initializeRichTextArea() {
		// TODO : For future use, in case of using Rich Text Editor
	}

	/*
	 * */
	private void initializeDateFields() {
		//
		dateFrom.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy")));
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
		dateTo.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy")));
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
		dateEntry.setValue(new Date());
		//
		dateEntry.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy")));
		dateEntry.addValueChangeHandler(new ValueChangeHandler<Date>() {
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
		dateEntry.setValue(new Date());
	}
	
}