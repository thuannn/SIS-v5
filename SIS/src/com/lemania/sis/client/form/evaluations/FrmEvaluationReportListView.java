package com.lemania.sis.client.form.evaluations;

import java.util.List;

import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.lemania.sis.client.values.AppSettingValues;
import com.lemania.sis.shared.ClasseProxy;
import com.lemania.sis.shared.CoursProxy;
import com.lemania.sis.shared.EcoleProxy;
import com.lemania.sis.shared.EvaluationHeaderProxy;
import com.lemania.sis.shared.ProfessorProxy;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class FrmEvaluationReportListView extends ViewWithUiHandlers<FrmEvaluationReportListUiHandler> implements
		FrmEvaluationReportListPresenter.MyView {

	private final Widget widget;
	
	// Thuan
	private ListDataProvider<EvaluationHeaderProxy> providerEvaluationHeader = new ListDataProvider<EvaluationHeaderProxy>();
	private EvaluationHeaderProxy selectedEvaluationHeader;
	int selectedEvaluationHeaderIndex = -1;

	public interface Binder extends UiBinder<Widget, FrmEvaluationReportListView> {
	}

	@Inject
	public FrmEvaluationReportListView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@UiField ListBox lstYears;
	@UiField ListBox lstProgrammes;
	@UiField ListBox lstClasses;
	@UiField ListBox lstClassMaster;
	@UiField(provided=true) DataGrid<EvaluationHeaderProxy> tblReports = new DataGrid<EvaluationHeaderProxy>();
	@UiField RichTextArea txtObjective;
	@UiField Button cmdCreate;
	@UiField ListBox lstEcoles;
	@UiField Button cmdSave;
	@UiField TextBox dateFrom;
	@UiField TextBox dateTo;
		
	/*
	 * */
	public void initializeYearList(){
		lstYears.clear();
		lstYears.addItem("-","");
		lstYears.addItem( AppSettingValues.schoolYearDescription, AppSettingValues.schoolYearCode );
	}

	/*
	 * */
	@Override
	public void initializeUI() {
		//
		initializeYearList();
		initializeEvaluationHeaderTable();
	}

	/*
	 * */
	private void initializeEvaluationHeaderTable() {
		// From Date
 		TextColumn<EvaluationHeaderProxy> colFrom = new TextColumn<EvaluationHeaderProxy>() {
 	      @Override
 	      public String getValue(EvaluationHeaderProxy object) {
 	        return object.getFromDate();
 	      }
 	    };
 	    tblReports.addColumn(colFrom, "Du");
 	    
    	// To Date
 		TextColumn<EvaluationHeaderProxy> colTo = new TextColumn<EvaluationHeaderProxy>() {
 	      @Override
 	      public String getValue(EvaluationHeaderProxy object) {
 	        return object.getToDate();
 	      }
 	    };
 	    tblReports.addColumn(colTo, "Au");
 	    
 	    // Add a selection model to handle user selection.
	    final SingleSelectionModel<EvaluationHeaderProxy> selectionModel = new SingleSelectionModel<EvaluationHeaderProxy>();
	    tblReports.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	        selectedEvaluationHeader = selectionModel.getSelectedObject();
	        selectedEvaluationHeaderIndex = providerEvaluationHeader.getList().indexOf(selectedEvaluationHeader);
	        if (selectedEvaluationHeader != null) {
	        	populateEvaluationHeaderData();
	        }
	      }
	    });
 	    
 	    //
 	    providerEvaluationHeader.addDataDisplay(tblReports);
	}
	
	/*
	 * */
	public void populateEvaluationHeaderData() {
		//
		dateFrom.setText(selectedEvaluationHeader.getFromDate());
		dateTo.setText(selectedEvaluationHeader.getToDate());
		txtObjective.setText(selectedEvaluationHeader.getObjective());
		selectListValue(lstClassMaster, selectedEvaluationHeader.getClassMasterId());
	}

	/*
	 * */
	private void selectListValue(ListBox listBox, String value) {
		//
		for (int i=0; i<listBox.getItemCount(); i++) {
			if (listBox.getValue(i).equals(value)) {
				listBox.setSelectedIndex(i);
				break;
			}
		}
	}

	/*
	 * */
	@Override
	public void setEcoleList(List<EcoleProxy> ecoles) {		
		// First clear existing data
		lstEcoles.clear(); 
		
		//
		lstEcoles.addItem("-", "");
		for ( EcoleProxy ecole : ecoles )
			lstEcoles.addItem(ecole.getSchoolName(), ecole.getId().toString());
	}
	
	/*
	 * */
	@UiHandler("lstEcoles")
	void onLstEcolesChange(ChangeEvent event) {		
		// If user select the first item, which is null, clear the program list
		if (lstEcoles.getValue(lstEcoles.getSelectedIndex()).isEmpty()) {
			lstProgrammes.clear();
			lstClasses.clear();
			return;
		}
		
		// Otherwise, load the program list
		if (getUiHandlers() != null)
			getUiHandlers().onEcoleSelected( lstEcoles.getValue( lstEcoles.getSelectedIndex() ));
	}
	
	/*
	 * */
	@Override
	public void setCoursList(List<CoursProxy> programmes) {
		// First clear existing data
		lstProgrammes.clear();
		
		// 
		lstProgrammes.addItem("-", "");
		for ( CoursProxy cours : programmes )
			lstProgrammes.addItem( cours.getCoursNom(), cours.getId().toString() );
	}
	
	/*
	 * */
	@UiHandler("lstProgrammes")
	void onLstProgrammesChange(ChangeEvent event) {
		//
		if (lstProgrammes.getValue(lstProgrammes.getSelectedIndex()).isEmpty())			
			return;
		//
		if (getUiHandlers() != null)
			getUiHandlers().onProgrammeSelected( lstProgrammes.getValue( lstProgrammes.getSelectedIndex() ));
	}

	/*
	 * */
	@Override
	public void setClasseList(List<ClasseProxy> classes) {	
		// First clear existing data
		lstClasses.clear();
		
		// 
		lstClasses.addItem("-", "");
		for ( ClasseProxy cours : classes )
			lstClasses.addItem( cours.getClassName(), cours.getId().toString() );
	}
	
	/*
	 * */
	@UiHandler("cmdSave")
	void onCmdSaveClick(ClickEvent event) {
		//
		if (selectedEvaluationHeader == null){
			Window.alert("Merci de choisir un rapport à modifier.");
			return;
		}
		//
		getUiHandlers().updateReport(selectedEvaluationHeader, dateFrom.getText(), dateTo.getText(), 
				lstClassMaster.getValue(lstClassMaster.getSelectedIndex()), txtObjective.getText());
	}

	/*
	 * */
	@Override
	public void resetEditForm() {
		//
		dateFrom.setText("");
		dateTo.setText("");
		txtObjective.setText("");
		
		//
		lstClassMaster.setSelectedIndex(0);
	}
	
	/*
	 * */
	@UiHandler("cmdCreate")
	void onCmdCreateClick(ClickEvent event) {
		//
		getUiHandlers().createNewReport(dateFrom.getText(), dateTo.getText(), txtObjective.getText(), 
				lstYears.getValue(lstYears.getSelectedIndex()), 
				lstClasses.getValue(lstClasses.getSelectedIndex()), 
				lstClassMaster.getValue(lstClassMaster.getSelectedIndex()));
	}

	/*
	 * */
	@Override
	public void setProfListData(List<ProfessorProxy> profs) {
		//
		lstClassMaster.clear();
		lstClassMaster.addItem("-","");
		for (ProfessorProxy prof : profs)
			lstClassMaster.addItem(prof.getProfName(), prof.getId().toString());
	}

	/*
	 * */
	@Override
	public void addNewEvaluationHeaderToList(EvaluationHeaderProxy eh) {
		//
		providerEvaluationHeader.getList().add(eh);
	}
	
	/*
	 * */
	@UiHandler("lstClasses")
	void onLstClassesChange(ChangeEvent event) {
		//
		resetEditForm();
		//
		getUiHandlers().onClassSelected(lstClasses.getValue(lstClasses.getSelectedIndex()));
	}

	/*
	 * */
	@Override
	public void setEvaluationHeaderListData(List<EvaluationHeaderProxy> eHs) {
		//
		providerEvaluationHeader.getList().clear();
		providerEvaluationHeader.setList(eHs);
	}

	/*
	 * */
	@Override
	public void resetForm() {
		//
		lstEcoles.clear();
		lstProgrammes.clear();
		lstClasses.clear();
		providerEvaluationHeader.getList().clear();
	}

	/*
	 * */
	@Override
	public void updateEvaluationHeader(EvaluationHeaderProxy eh) {
		//
		if (selectedEvaluationHeaderIndex != -1)
			providerEvaluationHeader.getList().set(selectedEvaluationHeaderIndex, eh);
	}
}
