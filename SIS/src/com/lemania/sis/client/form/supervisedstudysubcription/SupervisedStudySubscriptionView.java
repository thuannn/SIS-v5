package com.lemania.sis.client.form.supervisedstudysubcription;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.lemania.sis.client.UI.FieldValidation;
import com.lemania.sis.client.UI.GridButtonCell;
import com.lemania.sis.client.UI.MyAlert;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.client.values.Repetition;
import com.lemania.sis.shared.ProfessorProxy;
import com.lemania.sis.shared.SubjectProxy;
import com.lemania.sis.shared.assignment.AssignmentProxy;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectProxy;
import com.lemania.sis.shared.coursesubscription.CourseSubscriptionProxy;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;

class SupervisedStudySubscriptionView extends ViewWithUiHandlers<SupervisedStudySubscriptionUiHandlers> implements SupervisedStudySubscriptionPresenter.MyView {
    
	//
	interface Binder extends UiBinder<Widget, SupervisedStudySubscriptionView> {
    }

	//
    @Inject
    SupervisedStudySubscriptionView(Binder uiBinder) {
    	//
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    //
    @Override
    public void setInSlot(Object slot, IsWidget content) {
    	//
        if (slot == SupervisedStudySubscriptionPresenter.SLOT_IndividualCourseSubscription) {
        } else {
            super.setInSlot(slot, content);
        }
    }
    
    
    //
    @UiField DateBox dateFrom;
    @UiField DateBox dateRepetitionEnd;
    @UiField Button cmdFilter;
    //
    ListDataProvider<BulletinSubjectProxy> dataProvider = new ListDataProvider<BulletinSubjectProxy>();
    @UiField(provided=true) DataGrid<BulletinSubjectProxy> tblStudents = new DataGrid<BulletinSubjectProxy>();
    //
    ListDataProvider<CourseSubscriptionProxy> appliedStudentsDataProvider = new ListDataProvider<CourseSubscriptionProxy>();
    @UiField(provided=true) DataGrid<CourseSubscriptionProxy> tblAppliedStudents = new DataGrid<CourseSubscriptionProxy>();
    //
    @UiField ListBox lstProfs;
    @UiField ListBox lstAssignments;
    //
    private DialogBox pp;
    //
    @UiField VerticalPanel pnlSubscriptionDetail;
    @UiField ListBox lstSubjects;
    @UiField ListBox lstRepetition;
    @UiField CheckBox blnR;
    @UiField CheckBox blnES;
    @UiField TextArea txtNote1;
    @UiField Button cmdSave;
    @UiField Button cmdCancel;
    @UiField Label lblStudentName;
    //
    private BulletinSubjectProxy selectedStudent = null;
    private CourseSubscriptionProxy selectedSubscription = null;
    private boolean newSubscriptionEdit;
    
    /*
	 * */
	@Override
	public void initializeUI() {
		//
		initializeDateFields();
		//
		initializeStudentTable();
		//
		initializeAppliedStudentTable();
		//
		initializePopup();
		//
		initializeRepetitionList();
	}
	
	
	/*
	 * */
	private void initializeRepetitionList() {
		//
		lstRepetition.clear();
		for (Repetition r : Repetition.values()) {
			lstRepetition.addItem( r.toString(), r.name() );
		}
	}
	

	/*
	 * */
	private void initializeDateFields() {
		//
		dateFrom.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy")));
		dateFrom.setValue(new Date());
		//
		dateRepetitionEnd.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy")));
		dateRepetitionEnd.setValue( new Date());
	}
	
	
	/* 
	 * Initialize Student table 
	 * */
	public void initializeStudentTable() {
		// Add a text column to show the name.
	    Column<BulletinSubjectProxy, String> colFirstName = new Column<BulletinSubjectProxy, String>(new TextCell()) {
	      @Override
	      public String getValue(BulletinSubjectProxy object) {
	        return object.getStudentName();
	      }
	    };
	    tblStudents.addColumn(colFirstName, "Nom");
	    
	    // Add a selection model to handle user selection.
	    final SingleSelectionModel<BulletinSubjectProxy> selectionModel = new SingleSelectionModel<BulletinSubjectProxy>();
	    tblStudents.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	//
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		//
	    	}
	    });
	    
	    // Inscrire
	    Column<BulletinSubjectProxy, String> colAdd = new Column<BulletinSubjectProxy, String> (new GridButtonCell()){
	    	@Override
	    	public String getValue(BulletinSubjectProxy bp) {
	    		return "Inscrire";
	    	}
	    };
	    colAdd.setFieldUpdater(new FieldUpdater<BulletinSubjectProxy, String>(){
	    	@Override
	    	public void update(int index, BulletinSubjectProxy ps, String value) {
	    		//
	    		selectedStudent = ps;
	    		newSubscriptionEdit = true;
	    		//
	    		showSubscriptionDetailBox();
	    	}
	    });
	    tblStudents.setColumnWidth(colAdd, 100, Unit.PX);
	    tblStudents.addColumn(colAdd, "");	   
	    
	    //
	    dataProvider.addDataDisplay(tblStudents);
	}
	
	
	
	/*
	 * */
	private void initializePopup() {
		//
		pp = new DialogBox(true) {
			@Override
			  protected void onPreviewNativeEvent(final NativePreviewEvent event) {
			    super.onPreviewNativeEvent(event);
			    switch (event.getTypeInt()) {
			        case Event.ONKEYDOWN:
			            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
			            	//
			                hide();
			            }
			            break;
			    }
			}
		};
		//
		pp.addCloseHandler(new CloseHandler<PopupPanel>() {
			public void onClose(CloseEvent<PopupPanel> event) {
				//
				resetPnlSubscriptionDetail();
			}
		});
		//
		cmdCancel.addClickHandler( new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				//
				resetPnlSubscriptionDetail();
				pp.hide();
			}
			
		});
		//
		cmdSave.addClickHandler( new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				//
				if ( !blnR.getValue() &&  !blnES.getValue() ) {
					(new MyAlert( NotificationValues.invalid_input + "Merci de choisir l'option R ou T" )).center();
					return;
				}
				//
				if ( blnR.getValue() && lstSubjects.getSelectedValue().equals("") ) {
					(new MyAlert( NotificationValues.invalid_input + "Matière" )).center();
					return;
				}
				//
				if (txtNote1.getText().equals("")) {
					(new MyAlert( NotificationValues.invalid_input + "Commentaire" )).center();
					return;
				}
				//
				// If creating a new subscription
				if ( newSubscriptionEdit ) {
					getUiHandlers().addCourseSubscription( 
		    				selectedStudent.getStudentId().toString(),
		    				lstProfs.getSelectedValue(), 
		    				DateTimeFormat.getFormat("yyyyMMdd").format( dateFrom.getValue() ),
		    				blnR.getValue(), 
		    				blnES.getValue(), 
		    				txtNote1.getText(),
		    				lstSubjects.getSelectedValue(),
		    				Repetition.valueOf( lstRepetition.getSelectedValue() ), 
		    				DateTimeFormat.getFormat("yyyyMMdd").format( dateRepetitionEnd.getValue() ) );
				}
				//
				if ( !newSubscriptionEdit ) {
					getUiHandlers().saveSubscriptionDetails(
							selectedSubscription,
							txtNote1.getText(),
							DateTimeFormat.getFormat("yyyyMMdd").format( dateFrom.getValue() ),
							lstSubjects.getSelectedValue(),
							blnR.getValue(), 
		    				blnES.getValue() );
				}
			}
			
		});
		//
		pp.add( pnlSubscriptionDetail );
		pp.setAutoHideEnabled(false);
		pp.setGlassEnabled( true );
		pp.setText("Subscription");
	}
	
	
	
	/*
	 * 
	 * */
	private void showSubscriptionDetailBox() {
		//
		if (lstProfs.getSelectedValue().equals("")) {
			(new MyAlert( NotificationValues.invalid_input + "Professeur" )).center();
			return;
		}
		//
		// If creating a new subscription, show blank form with student name
		if (newSubscriptionEdit) {
			resetPnlSubscriptionDetail();
			lblStudentName.setText( selectedStudent.getStudentName() );
		}
		// If not creating a new subscription, show the currently selected subscription
		// Disable the repetition option
		if (!newSubscriptionEdit && (selectedSubscription != null)) {
			//s
			lblStudentName.setText( selectedSubscription.getStudentName() );
			FieldValidation.selectItemByText(lstSubjects, selectedSubscription.getSubjectName());
			blnR.setValue( selectedSubscription.isR() );
			blnES.setValue( selectedSubscription.isES() );
			txtNote1.setText( selectedSubscription.getNote1() );
			//
			FieldValidation.selectItemByText( lstRepetition, selectedSubscription.getRep().toString() );
			lstRepetition.setEnabled(false);
			dateRepetitionEnd.setValue( DateTimeFormat.getFormat("yyyyMMdd").parse( selectedSubscription.getEndDate() ) );
			dateRepetitionEnd.setEnabled(false);
		}
		//
		pnlSubscriptionDetail.setVisible(true);
		//
		pp.center();
	}
	
	
	
	/* 
	 * Initialize Student table 
	 * */
	public void initializeAppliedStudentTable() {
		//
	    Column<CourseSubscriptionProxy, String> colStudentName = new Column<CourseSubscriptionProxy, String>(new TextCell()) {
	      @Override
	      public String getValue(CourseSubscriptionProxy object) {
	        return object.getStudentName();
	      } 
	    };
	    tblAppliedStudents.addColumn(colStudentName, "Elève");
	    tblAppliedStudents.setColumnWidth(colStudentName, 15, Unit.PCT);
	    
	    // Add a text column to show the name.
	    Column<CourseSubscriptionProxy, String> colProf = new Column<CourseSubscriptionProxy, String>(new TextCell()) {
	      @Override
	      public String getValue(CourseSubscriptionProxy object) {
	        return object.getProfessorName();
	      }
	    };
	    tblAppliedStudents.addColumn(colProf, "Inscrit par");
	    tblAppliedStudents.setColumnWidth(colProf, 15, Unit.PCT);
	    
//	    // Add a text column to show the name.
//	    Column<CourseSubscriptionProxy, String> colDate = new Column<CourseSubscriptionProxy, String>(new TextCell()) {
//	      @Override
//	      public String getValue(CourseSubscriptionProxy object) {
//	        return FieldValidation.swissDateFormat( object.getDate() );
//	      }
//	    };
//	    tblAppliedStudents.addColumn(colDate, "Date");
//	    tblAppliedStudents.setColumnWidth(colDate, 10, Unit.PCT);
	    
	    // Add a text column to show the name.
	    Column<CourseSubscriptionProxy, String> colSubject = new Column<CourseSubscriptionProxy, String>(new TextCell()) {
	      @Override
	      public String getValue(CourseSubscriptionProxy object) {
	        return object.getSubjectName();
	      }
	    };
	    tblAppliedStudents.addColumn(colSubject, "Matière");
	    tblAppliedStudents.setColumnWidth(colSubject, 10, Unit.PCT);
	    
	    
	    // Add a text column to show the name.
	    Column<CourseSubscriptionProxy, String> colR = new Column<CourseSubscriptionProxy, String>(new TextCell()) {
	      @Override
	      public String getValue(CourseSubscriptionProxy object) {
	        return object.isR() ? "R" : "";
	      }
	    };
	    tblAppliedStudents.addColumn(colR, "R");
	    tblAppliedStudents.setColumnWidth(colR, 50, Unit.PX);
	    
	    // Add a text column to show the name.
	    Column<CourseSubscriptionProxy, String> colES = new Column<CourseSubscriptionProxy, String>(new TextCell()) {
	      @Override
	      public String getValue(CourseSubscriptionProxy object) {
	        return object.isES() ? "T" : "";
	      }
	    };
	    tblAppliedStudents.addColumn(colES, "T");
	    tblAppliedStudents.setColumnWidth(colES, 50, Unit.PX);
	    
	    // Add a text column to show the name.
	    Column<CourseSubscriptionProxy, String> colNote = new Column<CourseSubscriptionProxy, String>(new TextCell()) {
	      @Override
	      public String getValue(CourseSubscriptionProxy object) {
	        return object.getNote1();
	      }
	    };
	    tblAppliedStudents.addColumn(colNote, "A faire");
	    
	    // Note input
 		Column<CourseSubscriptionProxy, String> colNoteInput = new Column<CourseSubscriptionProxy, String>(
 				new GridButtonCell()) {
 			@Override
 			public String getValue(CourseSubscriptionProxy bp) {
 				return "Editer";
 			}
 		};
 		colNoteInput
			.setFieldUpdater(new FieldUpdater<CourseSubscriptionProxy, String>() {
				@Override
				public void update(int index, CourseSubscriptionProxy ps, String value) {
					//
					if ( !ps.getProfessorName().equals(lstProfs.getSelectedItemText()) ) {
						(new AlertMessageBox("Alerte", "Modification non autorisée" )).show();
						return;
					}
					//
		    		selectedSubscription = ps;
		    		newSubscriptionEdit = false;
		    		//
		    		showSubscriptionDetailBox();
				}
			});
 		tblAppliedStudents.setColumnWidth(colNoteInput, 75, Unit.PX);
 		tblAppliedStudents.addColumn(colNoteInput, "");
	    
	    // Add a selection model to handle user selection.
	    final SingleSelectionModel<CourseSubscriptionProxy> selectionModel = new SingleSelectionModel<CourseSubscriptionProxy>();
	    tblAppliedStudents.setSelectionModel( selectionModel );
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	//
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		//
	    	}
	    });
	    
	    //
	    // Delete
	    Column<CourseSubscriptionProxy, String> colDelete = new Column<CourseSubscriptionProxy, String> (new GridButtonCell()){
	    	@Override
	    	public String getValue(CourseSubscriptionProxy bp){
	    			return "Supprimer"; 
	    	}
	    };
	    colDelete.setFieldUpdater(new FieldUpdater<CourseSubscriptionProxy, String>(){
	    	@Override
	    	public void update(int index, CourseSubscriptionProxy ps, String value){
	    		//
	    		if (ps.getProfessorName().equals(lstProfs.getSelectedItemText())) {
	    			//
	    			if (!ps.getRepetitionCode().equals("")) {
	    				//
	    				showDeleteRepetitionBox( ps );
	    			}
	    			else
	    				getUiHandlers().removeCourseSubscription( ps, DateTimeFormat.getFormat("yyyyMMdd").format( dateFrom.getValue() ));
	    		} else
	    			(new AlertMessageBox("Alerte", "Modification non autorisée")).show();
	    	}
	    });
	    tblAppliedStudents.setColumnWidth( colDelete, 100, Unit.PX);
	    tblAppliedStudents.addColumn( colDelete, "");	   
	    
	    //
	    appliedStudentsDataProvider.addDataDisplay(tblAppliedStudents);
	}
	
	
	/*
	 * Show deletion options for repetitive subscriptions
	 * */
	public void showDeleteRepetitionBox( final CourseSubscriptionProxy ps ) {
		//
		// Set title
		final DialogBox boxConfirm = new DialogBox();
		//
		boxConfirm.setText("Alert");
		// Enable animation.
		boxConfirm.setAnimationEnabled(true);
		// Enable glass background.
		boxConfirm.setGlassEnabled(true);
		// Set modal
		boxConfirm.setModal(true);

		// DialogBox is a SimplePanel, so you have to set its widget property to
		// whatever you want its contents to be.
		Button all = new Button("TOUT supprimer");
		all.setStyleName("gridButton");
		all.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				getUiHandlers().removeAllRepetitions( ps, DateTimeFormat.getFormat("yyyyMMdd").format( dateFrom.getValue() ) );
				boxConfirm.hide();
			}
		});
		
		// DialogBox is a SimplePanel, so you have to set its widget property to
		// whatever you want its contents to be.
		Button future = new Button("Supprimer les FUTURES répétitions");
		future.setStyleName("gridButton");
		future.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				getUiHandlers().removeFutureRepetitions( ps, DateTimeFormat.getFormat("yyyyMMdd").format( dateFrom.getValue() ) );
				boxConfirm.hide();
			}
		});
		
		// DialogBox is a SimplePanel, so you have to set its widget property to
		// whatever you want its contents to be.
		Button cancel = new Button("Annuler");
		cancel.setStyleName("gridButton");
		cancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				boxConfirm.hide();
			}
		});
		
		// Label for message
		Label lbl = new Label( "Merci de choisir une option de suppression." );
		lbl.setStyleName("txtAlert");

		// Vertical Panel
		VerticalPanel vp = new VerticalPanel();
		vp.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
		vp.setSpacing(10);
		//
		vp.add(lbl);
		vp.add(all);
		vp.add(future);
		vp.add(cancel);
		//
		boxConfirm.setWidget(vp);
		//
		boxConfirm.center();
	}
	
	
	/*
	 * */
	@Override
	public void resetForm() {
		//
		dataProvider.getList().clear();
		dataProvider.flush();
		//
		appliedStudentsDataProvider.getList().clear();
		appliedStudentsDataProvider.flush();
		//
		resetPnlSubscriptionDetail();
	}
	
	
	/*
	 * */
	private void resetPnlSubscriptionDetail() {
		//
		blnR.setValue(false);
		blnES.setValue(false);
		txtNote1.setText("");
		//
		lstRepetition.setEnabled(true);
		dateRepetitionEnd.setEnabled(true);
		//
		pnlSubscriptionDetail.setVisible(false);
	}
	
	
	/*--------------------------------*/
	
	
	/*
	 * */
	@Override
	public void setTableData(List<BulletinSubjectProxy> studentList) {
		//
		dataProvider.getList().clear();
		dataProvider.setList(studentList);
		dataProvider.flush();
		tblStudents.setPageSize( studentList.size() );
	}
	
	
	/* 
	 * Clicking on this buttton will load all students that have absences during the selected date range
	 * */
	@UiHandler("cmdFilter")
	void onCmdFilterClick(ClickEvent event) {
		//
		loadAppliedStudentsByDate();
	}
	
	/*
	 * */
	@UiHandler("lstProfs")
	void onLstProfsChange(ChangeEvent event) {
		//
		if (getUiHandlers() != null)
			getUiHandlers().onProfessorSelected( lstProfs.getValue(lstProfs.getSelectedIndex()) );
	}
	
	
	/*
	 * */
	@UiHandler("lstAssignments")
	void onLstAssignmentsChange(ChangeEvent event) {
		//
		// Auto select the current subject in the subscription panel
		lstSubjects.setSelectedIndex( lstAssignments.getSelectedIndex() );
		//
		dataProvider.getList().clear();
		//
		if (getUiHandlers() != null)
			getUiHandlers().onAssignmentSelected( lstAssignments.getValue(lstAssignments.getSelectedIndex()));
	}

	
	/*
	 * */
	private void loadAppliedStudentsByDate() {
		//
		String fromDate = DateTimeFormat.getFormat("yyyyMMdd").format( dateFrom.getValue() );
		getUiHandlers().loadAppliedStudentsByDate( fromDate );
	}

	/*
	 * */
	@Override
	public void setAppliedStudentsTableData(List<CourseSubscriptionProxy> list) {
		//
		appliedStudentsDataProvider.getList().clear();
		appliedStudentsDataProvider.setList( list );
		appliedStudentsDataProvider.flush();
		tblAppliedStudents.setPageSize( list.size() );
		//
		pp.hide();
	}
	
	
	/*
	 * */
	@Override
	public void setAssignmentTableData(List<AssignmentProxy> assignments) {
		// 		
		lstAssignments.clear();
		lstAssignments.addItem("-","");
		for (AssignmentProxy assignment : assignments){
			lstAssignments.addItem( 
							assignment.getClasseName() + " - "  
							+ assignment.getSubjectName(), 
				assignment.getId().toString());
		}
		// Set subject list
		// First clear existing data
		lstSubjects.clear();
		
		// 
		lstSubjects.addItem("-", "");
		for ( AssignmentProxy subject : assignments )
			lstSubjects.addItem( subject.getSubjectName(), subject.getSubjectId() );
	}
	
	
	/*
	 * */
	@Override
	public void setProfListData(List<ProfessorProxy> profs, boolean autoSelect ) {
		//
		lstProfs.clear();
		lstProfs.addItem("-","");
		for (ProfessorProxy prof : profs)
			lstProfs.addItem(prof.getProfName(), prof.getId().toString());
		//
		// autoSelect is true if this user is a professor
		if ( autoSelect && (profs.size()>0) ) {
			//
			lstProfs.setSelectedIndex(1);
			//
			onLstProfsChange( null );
		}
	}

	
}