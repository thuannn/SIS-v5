package com.lemania.sis.client.form.evaluations;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.lemania.sis.client.values.EvaluationValues;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.EvaluationHeaderProxy;
import com.lemania.sis.shared.EvaluationSubjectProxy;
import com.lemania.sis.shared.ProfessorProxy;
import com.lemania.sis.shared.assignment.AssignmentProxy;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.event.dom.client.ChangeEvent;

public class FrmEvaluationInputView extends ViewWithUiHandlers<FrmEvaluationInputUiHandler> implements
		FrmEvaluationInputPresenter.MyView {

	private final Widget widget;
	
	//
	private ListDataProvider<EvaluationSubjectProxy> providerEvaluationSubject = new ListDataProvider<EvaluationSubjectProxy>();
	private EvaluationSubjectProxy selectedEvaluationSubject = null;
	int selectedEvaluationSubjectIndex = -1;
	private List<String> categoriesName = new ArrayList<String>();

	public interface Binder extends UiBinder<Widget, FrmEvaluationInputView> {
	}

	@Inject
	public FrmEvaluationInputView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@UiField(provided=true) DataGrid<EvaluationSubjectProxy> tblEvaluations = new DataGrid<EvaluationSubjectProxy>();
	@UiField ListBox lstProfs;
	@UiField ListBox lstEvaluationHeaders;
	@UiField ListBox lstAssignments;
	
	/*
	 * */
	@Override
	public void resetForm() {
		//		
		lstAssignments.clear();
		lstEvaluationHeaders.clear();
		
		selectedEvaluationSubject = null;
		providerEvaluationSubject.getList().clear();		
	}

	/*
	 * */
	private void setCategories() {
		//
		categoriesName.clear();
		categoriesName.add( "" );
		categoriesName.add( EvaluationValues.TB );
		categoriesName.add( EvaluationValues.B );
		categoriesName.add( EvaluationValues.AB );
		categoriesName.add( EvaluationValues.S );
		categoriesName.add( EvaluationValues.PA );
		categoriesName.add( EvaluationValues.NA );
	}

	/*
	 * */
	@Override
	public void setProfListData(List<ProfessorProxy> profs) {		
		//
		lstProfs.clear();
		lstProfs.addItem("-","");
		for (ProfessorProxy prof : profs)
			lstProfs.addItem(prof.getProfName(), prof.getId().toString());
	}
	
	/*
	 * */
	@UiHandler("lstProfs")
	void onLstProfsChange(ChangeEvent event) {
		//
		resetForm();
		//
		if (getUiHandlers() != null)
			getUiHandlers().onProfessorSelected(lstProfs.getValue(lstProfs.getSelectedIndex()));
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
					assignment.getProgrammeName() + " - "
							+ assignment.getClasseName() + " - "  
							+ assignment.getSubjectName(), 
				assignment.getId().toString());
		}
	}
	
	/*
	 * */
	@UiHandler("lstEvaluationHeaders")
	void onLstEvaluationHeadersChange(ChangeEvent event) {
		//
		if (getUiHandlers() != null)
			getUiHandlers().onLstEvaluationHeaderSelected(
					lstProfs.getValue(lstProfs.getSelectedIndex()),
					lstAssignments.getValue(lstAssignments.getSelectedIndex()),
					lstEvaluationHeaders.getValue(lstEvaluationHeaders.getSelectedIndex()) );
	}
	
	/*
	 * */
	@UiHandler("lstAssignments")
	void onLstAssignmentsChange(ChangeEvent event) {
		//
		if (getUiHandlers() != null)
			getUiHandlers().onAssignmentSelected( lstAssignments.getValue(lstAssignments.getSelectedIndex()) );
	}

	/*
	 * */
	@Override
	public void setEvaluationHeaderListData(List<EvaluationHeaderProxy> headers) {
		//
		lstEvaluationHeaders.clear();
		lstEvaluationHeaders.addItem("-","");
		for (EvaluationHeaderProxy evh : headers)
			lstEvaluationHeaders.addItem( evh.getFromDate() + " - " + evh.getToDate(), evh.getId().toString() );
		// Clear the table
		selectedEvaluationSubject = null;
		providerEvaluationSubject.getList().clear();	
	}

	/*
	 * */
	@Override
	public void setEvaluationSubjectTableData(
			List<EvaluationSubjectProxy> evaluationSubjectList) {
		//
		providerEvaluationSubject.getList().clear();
		providerEvaluationSubject.getList().addAll(evaluationSubjectList);
		providerEvaluationSubject.refresh();
		providerEvaluationSubject.flush();
		
		//
		tblEvaluations.setHeight( Integer.toString( NotificationValues.lineHeightShortList * (evaluationSubjectList.size()+1) + 100 ) + "px" );
	}

	/*
	 * */
	@Override
	public void initializeUI() {
		//
		setCategories();		
		
		//
		TextColumn<EvaluationSubjectProxy> colStudentName = new TextColumn<EvaluationSubjectProxy>() {
 	      @Override
 	      public String getValue(EvaluationSubjectProxy object) {
 	        return object.getStudentName();
 	      }
 	    };
 	    tblEvaluations.addColumn(colStudentName, "Eleve");
 	    
 	    //
 	    List<String> categoriesName1 = new ArrayList<String>(categoriesName);
 	    SelectionCell categorieCell1 = new SelectionCell( categoriesName1 );
		Column<EvaluationSubjectProxy, String> colObjective1 = new Column<EvaluationSubjectProxy, String>(categorieCell1) {
 	      @Override
 	      public String getValue(EvaluationSubjectProxy object) {
 	        return object.getObjective1();
 	      }
 	    };
 	    tblEvaluations.addColumn(colObjective1, EvaluationValues.Objective1);
 	    colObjective1.setFieldUpdater(new FieldUpdater<EvaluationSubjectProxy, String>() {
 	      @Override
 	      public void update(int index, EvaluationSubjectProxy object, String value) {
 	    	  //
 	    	  selectedEvaluationSubjectIndex = index;
 	    	  selectedEvaluationSubject = providerEvaluationSubject.getList().get(selectedEvaluationSubjectIndex);
 	    	  //
 	    	  getUiHandlers().updateEvaluation(selectedEvaluationSubject, value, 1);
 	      }
 	    });
 	     	    
 	    // 	    
 	    List<String> categoriesName2 = new ArrayList<String>(categoriesName);
 	    SelectionCell categorieCell2 = new SelectionCell( categoriesName2 );
		Column<EvaluationSubjectProxy, String> colObjective2 = new Column<EvaluationSubjectProxy, String>(categorieCell2) {
 	      @Override
 	      public String getValue(EvaluationSubjectProxy object) {
 	        return object.getObjective2();
 	      }
 	    };
 	    tblEvaluations.addColumn(colObjective2, EvaluationValues.Objective2);
 	    colObjective2.setFieldUpdater(new FieldUpdater<EvaluationSubjectProxy, String>() {
 	      @Override
 	      public void update(int index, EvaluationSubjectProxy object, String value) {
 	    	  //
 	    	  selectedEvaluationSubjectIndex = index;
 	    	  selectedEvaluationSubject = providerEvaluationSubject.getList().get(selectedEvaluationSubjectIndex);
 	    	  //
 	    	  getUiHandlers().updateEvaluation(selectedEvaluationSubject, value, 2);
 	      }
 	    });
 	    
    	//
 	    List<String> categoriesName3 = new ArrayList<String>(categoriesName);
 	    SelectionCell categorieCell3 = new SelectionCell( categoriesName3 );
		Column<EvaluationSubjectProxy, String> colObjective3 = new Column<EvaluationSubjectProxy, String>(categorieCell3) {
 	      @Override
 	      public String getValue(EvaluationSubjectProxy object) {
 	        return object.getObjective3();
 	      }
 	    };
 	    tblEvaluations.addColumn(colObjective3, EvaluationValues.Objective3);
 	    colObjective3.setFieldUpdater(new FieldUpdater<EvaluationSubjectProxy, String>() {
 	      @Override
 	      public void update(int index, EvaluationSubjectProxy object, String value) {
 	    	  //
 	    	  selectedEvaluationSubjectIndex = index;
 	    	  selectedEvaluationSubject = providerEvaluationSubject.getList().get(selectedEvaluationSubjectIndex);
 	    	  //
 	    	  getUiHandlers().updateEvaluation(selectedEvaluationSubject, value, 3);
 	      }
 	    }); 	       
 	    
    	//
 	    List<String> categoriesName4 = new ArrayList<String>(categoriesName);
 	    SelectionCell categorieCell4 = new SelectionCell( categoriesName4 );
		Column<EvaluationSubjectProxy, String> colObjective4 = new Column<EvaluationSubjectProxy, String>(categorieCell4) {
 	      @Override
 	      public String getValue(EvaluationSubjectProxy object) {
 	        return object.getObjective4();
 	      }
 	    };
 	    tblEvaluations.addColumn(colObjective4, EvaluationValues.Objective4);
 	    colObjective4.setFieldUpdater(new FieldUpdater<EvaluationSubjectProxy, String>() {
 	      @Override
 	      public void update(int index, EvaluationSubjectProxy object, String value) {
 	    	  //
 	    	  selectedEvaluationSubjectIndex = index;
 	    	  selectedEvaluationSubject = providerEvaluationSubject.getList().get(selectedEvaluationSubjectIndex);
 	    	  //
 	    	  getUiHandlers().updateEvaluation(selectedEvaluationSubject, value, 4);
 	      }
 	    });
 	    
 	    //
 	    List<String> categoriesName5 = new ArrayList<String>(categoriesName);
 	    SelectionCell categorieCell5 = new SelectionCell( categoriesName5 );
		Column<EvaluationSubjectProxy, String> colObjective5 = new Column<EvaluationSubjectProxy, String>(categorieCell5) {
 	      @Override
 	      public String getValue(EvaluationSubjectProxy object) {
 	        return object.getObjective5();
 	      }
 	    };
 	    tblEvaluations.addColumn(colObjective5, EvaluationValues.Objective5);
 	    colObjective5.setFieldUpdater(new FieldUpdater<EvaluationSubjectProxy, String>() {
 	      @Override
 	      public void update(int index, EvaluationSubjectProxy object, String value) {
 	    	  //
 	    	  selectedEvaluationSubjectIndex = index;
 	    	  selectedEvaluationSubject = providerEvaluationSubject.getList().get(selectedEvaluationSubjectIndex);
 	    	  //
 	    	  getUiHandlers().updateEvaluation(selectedEvaluationSubject, value, 5);
 	      }
 	    });
 	    
// 	    // 20150519: not in use anymore
// 	    List<String> categoriesName6 = new ArrayList<String>(categoriesName);
// 	    SelectionCell categorieCell6 = new SelectionCell( categoriesName6 );
//		Column<EvaluationSubjectProxy, String> colObjective6 = new Column<EvaluationSubjectProxy, String>(categorieCell6) {
// 	      @Override
// 	      public String getValue(EvaluationSubjectProxy object) {
// 	        return object.getObjective6();
// 	      }
// 	    };
// 	    tblEvaluations.addColumn(colObjective6, EvaluationValues.Objective6);
// 	    colObjective6.setFieldUpdater(new FieldUpdater<EvaluationSubjectProxy, String>() {
// 	      @Override
// 	      public void update(int index, EvaluationSubjectProxy object, String value) {
// 	    	  //
// 	    	  selectedEvaluationSubjectIndex = index;
// 	    	  selectedEvaluationSubject = providerEvaluationSubject.getList().get(selectedEvaluationSubjectIndex);
// 	    	  //
// 	    	  getUiHandlers().updateEvaluation(selectedEvaluationSubject, value, 6);
// 	      }
// 	    });
 	    
 	    //
 	    providerEvaluationSubject.addDataDisplay(tblEvaluations); 
	}
}
