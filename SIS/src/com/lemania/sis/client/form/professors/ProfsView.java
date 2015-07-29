package com.lemania.sis.client.form.professors;

import java.util.List;

import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.lemania.sis.shared.ClasseProxy;
import com.lemania.sis.shared.CoursProxy;
import com.lemania.sis.shared.EcoleProxy;
import com.lemania.sis.shared.ProfessorProxy;
import com.lemania.sis.shared.SubjectProxy;
import com.lemania.sis.shared.assignment.AssignmentProxy;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ChangeEvent;

public class ProfsView extends ViewWithUiHandlers<ProfessorListUiHandler> implements ProfsPresenter.MyView {

	private final Widget widget;
	
	private int selectedProf;
	private int selectedAssignmentIndex;
	
	private ProfessorProxy selectedProfessor;
	
	// Thuan
	ListDataProvider<ProfessorProxy> dataProvider = new ListDataProvider<ProfessorProxy>();
	ListDataProvider<AssignmentProxy> assignmentDataProvider = new ListDataProvider<AssignmentProxy>();

	public interface Binder extends UiBinder<Widget, ProfsView> {
	}

	@Inject
	public ProfsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@UiField(provided=true) DataGrid<ProfessorProxy> tblProfessors = new DataGrid<ProfessorProxy>();
	@UiField(provided=true) DataGrid<AssignmentProxy> tblAssignments = new DataGrid<AssignmentProxy>();
	
	@UiField Label lblProfNameAssign;
	@UiField Button cmdAssigner;
	@UiField ListBox lstEcoles;
	@UiField ListBox lstCours;
	@UiField ListBox lstClasses;
	@UiField ListBox lstSubjects;
	

	@Override
	public void initializeTable() {
		
		// Add a text column to show the name.
		TextColumn<ProfessorProxy> colName = new TextColumn<ProfessorProxy>() {
	      @Override
	      public String getValue(ProfessorProxy object) {
	        return object.getProfName();
	      }
	    };
	    tblProfessors.addColumn(colName, "Nom");
	    
	    CheckboxCell cellActive = new CheckboxCell();
	    Column<ProfessorProxy, Boolean> colActive = new Column<ProfessorProxy, Boolean>(cellActive) {
	    	@Override
	    	public Boolean getValue(ProfessorProxy ecole){
	    		return ecole.getProfActive();
	    	}	    	
	    };
	    tblProfessors.addColumn(colActive, "Active");
	    tblProfessors.setColumnWidth(colActive, 20, Unit.PCT);
	    
	    colActive.setFieldUpdater(new FieldUpdater<ProfessorProxy, Boolean>(){
	    	@Override
	    	public void update(int index, ProfessorProxy prof, Boolean value){
	    		if (getUiHandlers() != null) {	    			
	    			selectedProf = index;
	    			getUiHandlers().updateProfessorStatus(prof, value);
	    		}	    		
	    	}
	    });
	    
	    // Add a selection model to handle user selection.
	    final SingleSelectionModel<ProfessorProxy> selectionModel = new SingleSelectionModel<ProfessorProxy>();
	    tblProfessors.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	        selectedProfessor = selectionModel.getSelectedObject();
	        if (selectedProfessor != null) {
	        	lblProfNameAssign.setText(selectedProfessor.getProfName());
	        	getUiHandlers().professorSelected(selectedProfessor);
	        }
	      }
	    });
	    
	    // Set data provider
	    dataProvider.addDataDisplay(tblProfessors);
	}

	@Override
	public void setData(List<ProfessorProxy> profs) {
		dataProvider.getList().clear();
		dataProvider.setList(profs);
	}

	@Override
	public void refreshTable(ProfessorProxy prof) {
		dataProvider.getList().remove(selectedProf);
		dataProvider.getList().add(selectedProf, prof);
		dataProvider.refresh();
	}
	
	@UiHandler("cmdAssigner")
	void onCmdAssignerClick(ClickEvent event) {
		if (getUiHandlers() != null)
			getUiHandlers().saveAssignment(
					selectedProfessor.getId().toString(), 
					lstClasses.getValue(lstClasses.getSelectedIndex()),
					lstSubjects.getValue(lstSubjects.getSelectedIndex()),
					true);
	}

	
	/*
	 * Set school list data
	 * */
	@Override
	public void setEcoleList(List<EcoleProxy> ecoleList) {
		// First clear existing data
		lstEcoles.clear(); 
		
		//
		lstEcoles.addItem("-", "");
		for ( EcoleProxy ecole : ecoleList )
			lstEcoles.addItem(ecole.getSchoolName(), ecole.getId().toString());
	}
	
	
	/*
	 * On change: load list of programmes
	 * */
	@UiHandler("lstEcoles")
	void onLstEcolesChange(ChangeEvent event) {
		// If user select the first item, which is null, clear the program list
		if (lstEcoles.getValue(lstEcoles.getSelectedIndex()).isEmpty()) {
			lstCours.clear();
			lstClasses.clear();
			lstSubjects.setSelectedIndex(0);
			return;
		}
		
		// Otherwise, load the program list
		if (getUiHandlers() != null)
			getUiHandlers().onEcoleSelected( lstEcoles.getValue( lstEcoles.getSelectedIndex() ));
	}

	
	/*
	 * Set programmes list
	 * */
	@Override
	public void setCoursList(List<CoursProxy> coursList) {
		// First clear existing data
		lstCours.clear();
		
		// 
		lstCours.addItem("-", "");
		for ( CoursProxy cours : coursList )
			lstCours.addItem( cours.getCoursNom(), cours.getId().toString() );
	}
	
	
	/*
	 * Load class list when a program is selected
	 * */
	@UiHandler("lstCours")
	void onLstCoursChange(ChangeEvent event) {
		//
		if (lstCours.getValue(lstCours.getSelectedIndex()).isEmpty()){
			lstClasses.clear();
			lstSubjects.setSelectedIndex(0);
			return;
		}
		//
		if (getUiHandlers() != null)
			getUiHandlers().onProgrammeSelected( lstCours.getValue( lstCours.getSelectedIndex() ));
	}

	
	/*
	 * Set class list data
	 * */
	@Override
	public void setClasseList(List<ClasseProxy> classeList) {
		// First clear existing data
		lstClasses.clear();
		
		// 
		lstClasses.addItem("-", "");
		for ( ClasseProxy cours : classeList )
			lstClasses.addItem( cours.getClassName(), cours.getId().toString() );
	}
	

	/*
	 * 
	 * */
	@Override
	public void resetForm() {
		//
		tblProfessors.getSelectionModel().setSelected(selectedProfessor, false);
		lblProfNameAssign.setText("");
		
		dataProvider.getList().clear();
		dataProvider.refresh();
		
		assignmentDataProvider.getList().clear();
		assignmentDataProvider.refresh();
		
		lstEcoles.clear();
		lstCours.clear();
		lstClasses.clear();
		lstSubjects.clear();
	}
	

	/*
	 * Initialize assignment table
	 * */
	@Override
	public void initializeAssignmentTable() {
		// Add a text column to show the name.
		TextColumn<AssignmentProxy> colProgrammeName = new TextColumn<AssignmentProxy>() {
	      @Override
	      public String getValue(AssignmentProxy object) {
	        return object.getProgrammeName();
	      }
	    };
	    tblAssignments.addColumn(colProgrammeName, "Programme");
	    tblAssignments.setColumnWidth( colProgrammeName, 25, Unit.PCT);
	    
	    // Add a text column to show the name.	
  		TextColumn<AssignmentProxy> colSubjectName = new TextColumn<AssignmentProxy>() {
  	      @Override
  	      public String getValue(AssignmentProxy object) {
  	        return object.getSubjectName();
  	      }
  	    };
  	    tblAssignments.addColumn(colSubjectName, "Matière");
  	    tblAssignments.setColumnWidth( colSubjectName, 35, Unit.PCT);
	    
	    // Add a text column to show the name.	
 		TextColumn<AssignmentProxy> colClasseName = new TextColumn<AssignmentProxy>() {
 	      @Override
 	      public String getValue(AssignmentProxy object) {
 	        return object.getClasseName();
 	      }
 	    };
 	    tblAssignments.addColumn(colClasseName, "Classe");
 	    tblAssignments.setColumnWidth( colClasseName, 20, Unit.PCT);
    
	    // Active
	    CheckboxCell cellActive = new CheckboxCell();
	    Column<AssignmentProxy, Boolean> colActive = new Column<AssignmentProxy, Boolean>(cellActive) {
	    	@Override
	    	public Boolean getValue(AssignmentProxy ecole){
	    		return ecole.getActive();
	    	}
	    };
	    tblAssignments.addColumn(colActive, "Active");
	    tblAssignments.setColumnWidth( colActive, 10, Unit.PCT);
	    
	    colActive.setFieldUpdater(new FieldUpdater<AssignmentProxy, Boolean>(){
	    	@Override
	    	public void update(int index, AssignmentProxy assignment, Boolean value){
	    		if (getUiHandlers() != null) {	    			
	    			selectedAssignmentIndex = index;
	    			getUiHandlers().updateAssignmentStatus(assignment, value);
	    		}	    		
	    	}
	    });
	    
	    // Set data provider
	    assignmentDataProvider.addDataDisplay( tblAssignments );
	}

	
	/*
	 * Set assignment table data
	 * */
	@Override
	public void setAssignmentTableData(List<AssignmentProxy> assignments) {
		//
		assignmentDataProvider.getList().clear();
		assignmentDataProvider.setList(assignments);
		assignmentDataProvider.refresh();
	}

	
	/*
	 * Show the newly added assignment
	 * */
	@Override
	public void showAddedAssignment(AssignmentProxy assignment) {
		//
		assignmentDataProvider.getList().add(assignment);
		assignmentDataProvider.refresh();
	}

	
	/*
	 * Show updated assignment
	 * */
	@Override
	public void showUpdatedAssignment(AssignmentProxy assignment) {
		//
		assignmentDataProvider.getList().remove(selectedAssignmentIndex);
		assignmentDataProvider.getList().add(selectedAssignmentIndex, assignment);
		assignmentDataProvider.refresh();
	}
	
	
	/*
	 * On classe change load active subject list
	 * */
	@UiHandler("lstClasses")
	void onLstClassesChange(ChangeEvent event) {
		if (getUiHandlers() != null)
			getUiHandlers().onClassSelected();
	}

	@Override
	public void setSubjectList(List<SubjectProxy> subjectList) {
		// First clear existing data
		lstSubjects.clear();
		
		// 
		lstSubjects.addItem("-", "");
		for ( SubjectProxy subject : subjectList )
			lstSubjects.addItem( subject.getSubjectName(), subject.getId().toString() );
	}
}