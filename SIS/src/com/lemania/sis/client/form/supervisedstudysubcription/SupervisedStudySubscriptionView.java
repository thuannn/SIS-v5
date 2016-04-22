package com.lemania.sis.client.form.supervisedstudysubcription;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
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
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.CoursProxy;
import com.lemania.sis.shared.ProfessorProxy;
import com.lemania.sis.shared.SubjectProxy;
import com.lemania.sis.shared.coursesubscription.CourseSubscriptionProxy;
import com.lemania.sis.shared.student.StudentProxy;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.box.MultiLinePromptMessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.info.Info;

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
    @UiField Button cmdFilter;
    //
    ListDataProvider<StudentProxy> dataProvider = new ListDataProvider<StudentProxy>();
    @UiField(provided=true) DataGrid<StudentProxy> tblStudents = new DataGrid<StudentProxy>();
    //
    ListDataProvider<CourseSubscriptionProxy> appliedStudentsDataProvider = new ListDataProvider<CourseSubscriptionProxy>();
    @UiField(provided=true) DataGrid<CourseSubscriptionProxy> tblAppliedStudents = new DataGrid<CourseSubscriptionProxy>();
    //
    @UiField ListBox lstProfs;
    //
    private DialogBox pp;
    @UiField VerticalPanel pnlSubscriptionDetail;
    @UiField ListBox lstSubjects;
    @UiField CheckBox blnR;
    @UiField CheckBox blnES;
    @UiField TextArea txtNote1;
    @UiField Button cmdSave;
    @UiField Button cmdCancel;
    @UiField Label lblStudentName;
    //
    private StudentProxy selectedStudent = null;
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
	}
	
	
	/*
	 * */
	private void initializeDateFields() {
		//
		dateFrom.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy")));
		dateFrom.addValueChangeHandler( new ValueChangeHandler<Date>(){
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
	}
	
	
	/* 
	 * Initialize Student table 
	 * */
	public void initializeStudentTable() {
		// Add a text column to show the name.
	    Column<StudentProxy, String> colFirstName = new Column<StudentProxy, String>(new TextCell()) {
	      @Override
	      public String getValue(StudentProxy object) {
	        return object.getLastName();
	      }
	    };
	    tblStudents.addColumn(colFirstName, "Nom");
		
	    //
	    Column<StudentProxy, String> colLastName = new Column<StudentProxy, String>(new TextCell()) {
	      @Override
	      public String getValue(StudentProxy object) {
	        return object.getFirstName();
	      } 
	    };
	    tblStudents.addColumn(colLastName, "Prénom");
	    
	    // Add a selection model to handle user selection.
	    final SingleSelectionModel<StudentProxy> selectionModel = new SingleSelectionModel<StudentProxy>();
	    tblStudents.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	//
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		//
	    	}
	    });
	    
	    // Inscrire
	    Column<StudentProxy, String> colAdd = new Column<StudentProxy, String> (new GridButtonCell()){
	    	@Override
	    	public String getValue(StudentProxy bp) {
	    		return "Inscrire";
	    	}
	    };
	    colAdd.setFieldUpdater(new FieldUpdater<StudentProxy, String>(){
	    	@Override
	    	public void update(int index, StudentProxy ps, String value) {
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
					Window.alert( NotificationValues.invalid_input + "Merci de choisir l'option R ou T" );
					return;
				}
				//
				if ( blnR.getValue() && lstSubjects.getSelectedValue().equals("") ) {
					Window.alert( NotificationValues.invalid_input + "Matière" );
					return;
				}
				//
				if (txtNote1.getText().equals("")) {
					Window.alert( NotificationValues.invalid_input + "Commentaire");
					return;
				}
				//
				// If creating a new subscription
				if ( newSubscriptionEdit ) {
					getUiHandlers().addCourseSubscription( 
		    				selectedStudent.getId().toString(), 
		    				lstProfs.getSelectedValue(), 
		    				DateTimeFormat.getFormat("yyyyMMdd").format( dateFrom.getValue() ),
		    				blnR.getValue(), 
		    				blnES.getValue(), 
		    				txtNote1.getText(),
		    				lstSubjects.getSelectedValue() );
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
			Window.alert( NotificationValues.invalid_input + "Professeur");
			return;
		}
		//
		// If creating a new subscription, show blank form with student name
		if (newSubscriptionEdit) {
			resetPnlSubscriptionDetail();
			lblStudentName.setText( selectedStudent.getLastName() + " " + selectedStudent.getFirstName() );
		}
		// If not creating a new subscription, show the currently selected subscription
		if (!newSubscriptionEdit && (selectedSubscription != null)) {
			lblStudentName.setText( selectedSubscription.getStudentName() );
			FieldValidation.selectItemByText(lstSubjects, selectedSubscription.getSubjectName());
			blnR.setValue( selectedSubscription.isR() );
			blnES.setValue( selectedSubscription.isES() );
			txtNote1.setText( selectedSubscription.getNote1() );
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
	    
	    // -- Delete
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
	    		if (ps.getProfessorName().equals(lstProfs.getSelectedItemText()))
	    			getUiHandlers().removeCourseSubscription( ps, DateTimeFormat.getFormat("yyyyMMdd").format( dateFrom.getValue() ));
	    		else
	    			(new AlertMessageBox("Alerte", "Modification non autorisée")).show();
	    	}
	    });
	    tblAppliedStudents.setColumnWidth( colDelete, 100, Unit.PX);
	    tblAppliedStudents.addColumn( colDelete, "");	   
	    
	    //
	    appliedStudentsDataProvider.addDataDisplay(tblAppliedStudents);
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
		lstSubjects.setSelectedIndex(0);
		blnR.setValue(false);
		blnES.setValue(false);
		txtNote1.setText("");
		//
		pnlSubscriptionDetail.setVisible(false);
	}
	
	
	/*--------------------------------*/
	
	
	/*
	 * */
	@Override
	public void setTableData(List<StudentProxy> studentList) {
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
	
	
	/**/
	@Override
	public void setSubjectList(List<SubjectProxy> programmes) {
		// First clear existing data
		lstSubjects.clear();
		
		// 
		lstSubjects.addItem("-", "");
		for ( SubjectProxy subject : programmes )
			lstSubjects.addItem( subject.getSubjectName(), subject.getId().toString() );
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
		if ( autoSelect && (profs.size()>0) )
			lstProfs.setSelectedIndex(1);
	}

	
}