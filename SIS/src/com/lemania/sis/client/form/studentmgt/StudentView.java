package com.lemania.sis.client.form.studentmgt;

import java.util.List;

import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.lemania.sis.shared.student.StudentProxy;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class StudentView extends ViewWithUiHandlers<StudentListUiHandler> implements StudentPresenter.MyView {

	private final Widget widget;
	
	/* Thuan */
	private int selectedStudent = -1;
	ListDataProvider<StudentProxy> dataProvider = new ListDataProvider<StudentProxy>();
	//
	private final MultiWordSuggestOracle mySuggestions = new MultiWordSuggestOracle();
	SuggestBox sgbStudents;

	
	@UiField(provided=true) DataGrid<StudentProxy> tblStudents = new DataGrid<StudentProxy>();
	@UiField SimplePager pagerStudent;
	@UiField HorizontalPanel pnlSearch;
	@UiField RadioButton optAll;
	@UiField RadioButton optActive;
	@UiField RadioButton optInactive;
	
	
	public interface Binder extends UiBinder<Widget, StudentView> {
	}

	@Inject
	public StudentView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	
	/* Initialize Student table */
	@Override
	public void initializeTable(){
		//
	    Column<StudentProxy, String> colLastName = new Column<StudentProxy, String>(new EditTextCell()) {
	      @Override
	      public String getValue(StudentProxy object) {
	        return object.getFirstName();
	      } 
	    };
	    tblStudents.addColumn(colLastName, "Prénom");
	    // Field updater
	    colLastName.setFieldUpdater(new FieldUpdater<StudentProxy, String>(){
	    	@Override
	    	public void update(int index, StudentProxy student, String value){
	    		if (getUiHandlers() != null) {	    			
	    			selectedStudent = index;
	    			getUiHandlers().updateStudentFirstName(student, value);
	    		}	    		
	    	}
	    });
		
		// Add a text column to show the name.
	    Column<StudentProxy, String> colFirstName = new Column<StudentProxy, String>(new EditTextCell()) {
	      @Override
	      public String getValue(StudentProxy object) {
	        return object.getLastName();
	      }
	    };
	    tblStudents.addColumn(colFirstName, "Nom");
	    // Field updater
	    colFirstName.setFieldUpdater(new FieldUpdater<StudentProxy, String>(){
	    	@Override
	    	public void update(int index, StudentProxy student, String value){
	    		if (getUiHandlers() != null) {	    			
	    			selectedStudent = index;
	    			getUiHandlers().updateStudentLastName(student, value);
	    		}	    		
	    	}
	    });
	    
	    //
	    Column<StudentProxy, String> colEmail = new Column<StudentProxy, String>(new EditTextCell()) {
	      @Override
	      public String getValue(StudentProxy object) {
	        return object.getEmail();
	      } 
	    };
	    tblStudents.addColumn(colEmail, "Email");
	    // Field updater
	    colEmail.setFieldUpdater(new FieldUpdater<StudentProxy, String>(){
	    	@Override
	    	public void update(int index, StudentProxy student, String value){
	    		if (getUiHandlers() != null) {	    			
	    			selectedStudent = index;
	    			getUiHandlers().updateStudentEmail(student, value);
	    		}	    		
	    	}
	    });
	    
	    //
	    CheckboxCell cellActive = new CheckboxCell();
	    Column<StudentProxy, Boolean> colActive = new Column<StudentProxy, Boolean>(cellActive) {
	    	@Override
	    	public Boolean getValue(StudentProxy student){
	    		return student.getIsActive();
	    	}	    	
	    };
	    tblStudents.addColumn(colActive, "Active");
	    // Field updater
	    colActive.setFieldUpdater(new FieldUpdater<StudentProxy, Boolean>(){
	    	@Override
	    	public void update(int index, StudentProxy student, Boolean value){
	    		if (getUiHandlers() != null) {	    			
	    			selectedStudent = index;
	    			getUiHandlers().updateStudentStatus(student, value);
	    		}	    		
	    	}
	    });
	    
	    // Add a selection model to handle user selection.
	    final SingleSelectionModel<StudentProxy> selectionModel = new SingleSelectionModel<StudentProxy>();
	    tblStudents.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	//
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		//
	    	}
	    });
	    
	    //
	    pagerStudent.setDisplay(tblStudents);
	    
	    //
	    dataProvider.addDataDisplay(tblStudents);
	    
	}

	@Override
	public void refreshTable() {
		// TODO Auto-generated method stub
		
	}

	/*
	 * */
	@Override
	public void setTableData(List<StudentProxy> studentList) {
		//
		dataProvider.getList().clear();
		dataProvider.setList(studentList);
		dataProvider.flush();
		//
		mySuggestions.clear();
		for (StudentProxy sp : studentList) {
			mySuggestions.add( sp.getFirstName() + " " + sp.getLastName() );
		}
		//
		pagerStudent.setPage(0);
	}

	/*
	 * */
	@Override
	public void updateEditedStudent(StudentProxy student) {
		dataProvider.getList().remove(selectedStudent);
		dataProvider.getList().add(selectedStudent, student);
		dataProvider.refresh();
	}
	
	
	/*
	 * */
	private void initializeSuggestBox() {
		//
		sgbStudents = new SuggestBox( mySuggestions );
		sgbStudents.addSelectionHandler( new SelectionHandler<Suggestion>(){

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				//
				showSelectedStudent( event.getSelectedItem().getReplacementString() );
			}
			
		});
		sgbStudents.setWidth( "300px" );
		pnlSearch.add( sgbStudents );
	}
	
	
	/*
	 * */
	public void showSelectedStudent( String studentName ) {
		//
		for (StudentProxy sp : dataProvider.getList() ) {
			if ( studentName.contains( sp.getFirstName() + " " + sp.getLastName() ) ) {
				tblStudents.getSelectionModel().setSelected( sp, true );
				pagerStudent.setPage( Math.round( dataProvider.getList().indexOf(sp) / pagerStudent.getPageSize() ) );
				break;
			}
		}
	}

	
	/*
	 * */
	@Override
	public void initializeUI() {
		// 
		initializeSuggestBox();
	}
	
	
	/*
	 * */
	@UiHandler("optAll")
	void onOptAllClick(ClickEvent event) {
		//
		getUiHandlers().listAllStudent();
	}
	
	
	/*
	 * */
	@UiHandler("optActive")
	void onOptActiveClick(ClickEvent event) {
		//
		getUiHandlers().listAllStudentActive();
	}
	
	
	/*
	 * */
	@UiHandler("optInactive")
	void onOptInactiveClick(ClickEvent event) {
		//
		getUiHandlers().listAllStudentInactive();
	}
}
