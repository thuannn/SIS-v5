package com.lemania.sis.client.form.supervisedstudysubcription;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.lemania.sis.client.UI.FieldValidation;
import com.lemania.sis.client.UI.GridButtonCell;
import com.lemania.sis.shared.ProfessorProxy;
import com.lemania.sis.shared.coursesubscription.CourseSubscriptionProxy;
import com.lemania.sis.shared.student.StudentProxy;

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
	    		getUiHandlers().addCourseSubscription( 
	    				ps.getId().toString(), 
	    				lstProfs.getSelectedValue(), 
	    				DateTimeFormat.getFormat("yyyyMMdd").format( dateFrom.getValue() ) );
	    	}
	    });
	    tblStudents.setColumnWidth(colAdd, 100, Unit.PX);
	    tblStudents.addColumn(colAdd, "");	   
	    
	    //
	    dataProvider.addDataDisplay(tblStudents);
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
	    
	    // Add a text column to show the name.
	    Column<CourseSubscriptionProxy, String> colProf = new Column<CourseSubscriptionProxy, String>(new TextCell()) {
	      @Override
	      public String getValue(CourseSubscriptionProxy object) {
	        return object.getProfessorName();
	      }
	    };
	    tblAppliedStudents.addColumn(colProf, "Inscrit par professeur");
	    
	    // Add a text column to show the name.
	    Column<CourseSubscriptionProxy, String> colDate = new Column<CourseSubscriptionProxy, String>(new TextCell()) {
	      @Override
	      public String getValue(CourseSubscriptionProxy object) {
	        return FieldValidation.swissDateFormat( object.getDate() );
	      }
	    };
	    tblAppliedStudents.addColumn(colDate, "Date");
	    
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
	    		return "Désinscrire";
	    	}
	    };
	    colDelete.setFieldUpdater(new FieldUpdater<CourseSubscriptionProxy, String>(){
	    	@Override
	    	public void update(int index, CourseSubscriptionProxy ps, String value){
	    		//
	    		getUiHandlers().removeCourseSubscription( ps, DateTimeFormat.getFormat("yyyyMMdd").format( dateFrom.getValue() ));
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