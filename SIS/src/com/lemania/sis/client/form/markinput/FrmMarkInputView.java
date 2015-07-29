package com.lemania.sis.client.form.markinput;

import java.util.List;

import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.lemania.sis.client.CurrentUser;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.ProfessorProxy;
import com.lemania.sis.shared.SettingOptionProxy;
import com.lemania.sis.shared.assignment.AssignmentProxy;
import com.lemania.sis.shared.bulletinbranche.BulletinBrancheProxy;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectProxy;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FrmMarkInputView extends ViewWithUiHandlers<FrmMarkInputUiHandler> implements
		FrmMarkInputPresenter.MyView {

	private final Widget widget;
	private boolean blockT1 = false;
	private boolean blockT2 = false;
	private boolean blockT3 = false;
	private boolean blockT4 = false;
	
	// Thuan
	private ListDataProvider<BulletinSubjectProxy> bulletinSubjectDataProvider = new ListDataProvider<BulletinSubjectProxy>();
	private ListDataProvider<BulletinSubjectProxy> bulletinSubjectAverageDataProvider = new ListDataProvider<BulletinSubjectProxy>();
	private ListDataProvider<BulletinBrancheProxy> bulletinBrancheDataProvider = new ListDataProvider<BulletinBrancheProxy>();
	//
	private int selectedBulletinSubjectIndex = -1;
	private BulletinSubjectProxy selectedBulletinSubject;
	//
	private int selectedBulletinBrancheIndex = -1;
	private BulletinBrancheProxy selectedBulletinBranche;
	

	public interface Binder extends UiBinder<Widget, FrmMarkInputView> {
	}

	@Inject
	public FrmMarkInputView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	@UiField(provided=true) DataGrid<BulletinSubjectProxy> tblBulletinSubjects = new DataGrid<BulletinSubjectProxy>();
	@UiField(provided=true) DataGrid<BulletinBrancheProxy> tblBranches = new DataGrid<BulletinBrancheProxy>();
	@UiField ListBox lstProfs;
	@UiField ListBox lstAssignments;
	@UiField DoubleBox txt_t_1_1;
	@UiField DoubleBox txt_t_1_2;
	@UiField DoubleBox txt_t_1_3;
	@UiField DoubleBox txt_t_1_4;
	@UiField DoubleBox txt_t_1_5;
	@UiField DoubleBox txt_t_2_1;
	@UiField DoubleBox txt_t_2_2;
	@UiField DoubleBox txt_t_2_3;
	@UiField DoubleBox txt_t_2_4;
	@UiField DoubleBox txt_t_2_5;
	@UiField DoubleBox txt_t_3_1;
	@UiField DoubleBox txt_t_3_2;
	@UiField DoubleBox txt_t_3_3;
	@UiField DoubleBox txt_t_3_4;
	@UiField DoubleBox txt_t_3_5;
	@UiField Button cmdSave;
	@UiField TextArea txtRemarque1;
	@UiField TextArea txtRemarque2;
	@UiField TextArea txtRemarque3;
	@UiField Label lblStudentName;
	@UiField Label txtT1;
	@UiField Label txtT2;
	@UiField Label txtT3;
	@UiField VerticalPanel pnlT1;
	@UiField VerticalPanel pnlT2;
	@UiField VerticalPanel pnlT3;
	@UiField VerticalPanel pnlNoteInput;
	@UiField(provided=true) DataGrid<BulletinSubjectProxy> tblAverageGrade = new DataGrid<BulletinSubjectProxy>();
	@UiField DoubleBox txt_t_4_1;
	@UiField DoubleBox txt_t_4_2;
	@UiField DoubleBox txt_t_4_3;
	@UiField DoubleBox txt_t_4_4;
	@UiField DoubleBox txt_t_4_5;
	@UiField TextArea txtRemarque4;
	@UiField Label txtT4;
	@UiField VerticalPanel pnlT4;
	
	
	@Override
	public void setProfListData(List<ProfessorProxy> profs) {
		//
		lstProfs.clear();
		lstProfs.addItem("-","");
		for (ProfessorProxy prof : profs)
			lstProfs.addItem(prof.getProfName(), prof.getId().toString());
	}
	
	
	/**/
	@UiHandler("lstProfs")
	void onLstProfsChange(ChangeEvent event) {
		//
		resetForm();
		//
		if (getUiHandlers() != null)
			getUiHandlers().onProfessorSelected(lstProfs.getValue(lstProfs.getSelectedIndex()));
	}

	
	/**/
	@Override
	public void resetForm() {
		//
		lstAssignments.clear();
		bulletinSubjectDataProvider.getList().clear();
		bulletinBrancheDataProvider.getList().clear();
		lblStudentName.setText("");
		//
		clearInputFields();
	}

	
	private void clearInputFields() {
		//
		txt_t_1_1.setText("");
		txt_t_1_2.setText("");
		txt_t_1_3.setText("");
		txt_t_1_4.setText("");
		txt_t_1_5.setText("");
		//
		txt_t_2_1.setText("");
		txt_t_2_2.setText("");
		txt_t_2_3.setText("");
		txt_t_2_4.setText("");
		txt_t_2_5.setText("");
		//
		txt_t_3_1.setText("");
		txt_t_3_2.setText("");
		txt_t_3_3.setText("");
		txt_t_3_4.setText("");
		txt_t_3_5.setText("");
		//
		txt_t_4_1.setText("");
		txt_t_4_2.setText("");
		txt_t_4_3.setText("");
		txt_t_4_4.setText("");
		txt_t_4_5.setText("");	
		//
		txtRemarque1.setText("");
		txtRemarque2.setText("");
		txtRemarque3.setText("");
		txtRemarque4.setText("");
		//
		txtT1.setText("");
		txtT2.setText("");
		txtT3.setText("");
		txtT4.setText("");
		//
		lblStudentName.setText("");		
		//
		blockT1( blockT1 );
		blockT2( blockT2 );
		blockT3( blockT3 );
		blockT4( blockT4 );
		//		
		pnlT1.setVisible(false);
		pnlT2.setVisible(false);
		pnlT3.setVisible(false);
		pnlT4.setVisible(false);
		cmdSave.setVisible(false);
	}
	

	/*
	 * 
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

	
	/**/
	@Override
	public void initializeUi() {
		//
		initializeBulletinSubjectTable();
		initializeBulletinBrancheTable();
		initializeAverageGrade();
	}
	
	
	/**/
	private void initializeAverageGrade() {
		//
		// Add a text column to show the name.	
 		TextColumn<BulletinSubjectProxy> colT1 = new TextColumn<BulletinSubjectProxy>() {
 	      @Override
 	      public String getValue(BulletinSubjectProxy object) {
 	        return object.getT1();
 	      }
 	    };
 	    tblAverageGrade.addColumn(colT1, "T1");
 	    
 	    // Add a text column to show the name.	
 		TextColumn<BulletinSubjectProxy> colExamT1 = new TextColumn<BulletinSubjectProxy>() {
 	      @Override
 	      public String getValue(BulletinSubjectProxy object) {
 	        return object.getExamT1();
 	      }
 	    };
 	    tblAverageGrade.addColumn(colExamT1, "E1");
 	    
 	    // Add a text column to show the name.	
 		TextColumn<BulletinSubjectProxy> colT2 = new TextColumn<BulletinSubjectProxy>() {
 	      @Override
 	      public String getValue(BulletinSubjectProxy object) {
 	        return object.getT2();
 	      }
 	    };
 	    tblAverageGrade.addColumn(colT2, "T2");
 	    
 	    // Add a text column to show the name.	
 		TextColumn<BulletinSubjectProxy> colExamT2 = new TextColumn<BulletinSubjectProxy>() {
 	      @Override
 	      public String getValue(BulletinSubjectProxy object) {
 	        return object.getExamT2();
 	      }
 	    };
 	    tblAverageGrade.addColumn(colExamT2, "E2");
 	    
 	 	// Add a text column to show the name.	
 		TextColumn<BulletinSubjectProxy> colT3 = new TextColumn<BulletinSubjectProxy>() {
 	      @Override
 	      public String getValue(BulletinSubjectProxy object) {
 	        return object.getT3();
 	      }
 	    };
 	    tblAverageGrade.addColumn(colT3, "T3");
 	    
 	   // Add a text column to show the name.	
 		TextColumn<BulletinSubjectProxy> colExamT3 = new TextColumn<BulletinSubjectProxy>() {
 	      @Override
 	      public String getValue(BulletinSubjectProxy object) {
 	        return object.getExamT3();
 	      }
 	    };
 	    tblAverageGrade.addColumn(colExamT3, "E3");
 	    
 	    //
 		TextColumn<BulletinSubjectProxy> colT4 = new TextColumn<BulletinSubjectProxy>() {
 	      @Override
 	      public String getValue(BulletinSubjectProxy object) {
 	        return object.getT4();
 	      }
 	    };
 	    tblAverageGrade.addColumn(colT4, "T4");
 	    
 	   // Add a text column to show the name.	
 		TextColumn<BulletinSubjectProxy> colExamT4 = new TextColumn<BulletinSubjectProxy>() {
 	      @Override
 	      public String getValue(BulletinSubjectProxy object) {
 	        return object.getExamT4();
 	      }
 	    };
 	    tblAverageGrade.addColumn(colExamT4, "E4");
 	    
    	// Add a text column to show the name.	
 		TextColumn<BulletinSubjectProxy> colAn = new TextColumn<BulletinSubjectProxy>() {
 	      @Override
 	      public String getValue(BulletinSubjectProxy object) {
 	        return object.getAn();
 	      }
 	    };
 	    tblAverageGrade.addColumn(colAn, "Moyenne");
 	    
 	    //
 	    bulletinSubjectAverageDataProvider.addDataDisplay(tblAverageGrade);
	}
	

	/*
	 * */
	private void initializeBulletinBrancheTable() {
		// Add a text column to show the name.
		TextColumn<BulletinBrancheProxy> colBrancheName = new TextColumn<BulletinBrancheProxy>() {
 	      @Override
 	      public String getValue(BulletinBrancheProxy object) {
 	        return object.getBulletinBrancheName();
 	      }
 	    };
 	    tblBranches.addColumn(colBrancheName, "Branches");
 	    
 	   // Add a text column to show the name.	
 		TextColumn<BulletinBrancheProxy> colCoef = new TextColumn<BulletinBrancheProxy>() {
 	      @Override
 	      public String getValue(BulletinBrancheProxy object) {
 	        return object.getBrancheCoef().toString();
 	      }
 	    };
 	   tblBranches.addColumn(colCoef, "Coef");
 	   tblBranches.setColumnWidth(colCoef, 15, Unit.PCT);
 	    
 	    // Add a text column to show the name.	
 		TextColumn<BulletinBrancheProxy> colT1 = new TextColumn<BulletinBrancheProxy>() {
 	      @Override
 	      public String getValue(BulletinBrancheProxy object) {
 	        return object.getT1();
 	      }
 	    };
 	   tblBranches.addColumn(colT1, "T1");
 	   tblBranches.setColumnWidth(colT1, 15, Unit.PCT);
 	    
 	    // Add a text column to show the name.	
 		TextColumn<BulletinBrancheProxy> colT2 = new TextColumn<BulletinBrancheProxy>() {
 	      @Override
 	      public String getValue(BulletinBrancheProxy object) {
 	        return object.getT2();
 	      }
 	    };
 	   tblBranches.addColumn(colT2, "T2");
 	   tblBranches.setColumnWidth(colT2, 15, Unit.PCT);
 	    
 	 	// Add a text column to show the name.	
 		TextColumn<BulletinBrancheProxy> colT3 = new TextColumn<BulletinBrancheProxy>() {
 	      @Override
 	      public String getValue(BulletinBrancheProxy object) {
 	        return object.getT3();
 	      }
 	    };
 	   tblBranches.addColumn(colT3, "T3");
 	   tblBranches.setColumnWidth(colT3, 15, Unit.PCT);
 	   
 	   // Add a text column to show the name.	
 		TextColumn<BulletinBrancheProxy> colT4 = new TextColumn<BulletinBrancheProxy>() {
 	      @Override
 	      public String getValue(BulletinBrancheProxy object) {
 	        return object.getT4();
 	      }
 	    };
 	   tblBranches.addColumn(colT4, "T4");
 	   tblBranches.setColumnWidth(colT4, 15, Unit.PCT);
 	 	    
 	   	
 	    // Add a selection model to handle user selection.
	    final SingleSelectionModel<BulletinBrancheProxy> selectionModel = new SingleSelectionModel<BulletinBrancheProxy>();
	    tblBranches.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	        selectedBulletinBranche = selectionModel.getSelectedObject();
	        if (selectedBulletinBranche != null) {
	        	//
	        	lblStudentName.setText( selectedBulletinSubject.getStudentName() + " - " + selectedBulletinBranche.getBulletinBrancheName());
	        	//
	        	selectedBulletinBrancheIndex = bulletinBrancheDataProvider.getList().indexOf(selectedBulletinBranche);
	        	getUiHandlers().onBulletinBrancheSelected(selectedBulletinBranche);
	        	//
	        	blockT1(blockT1);
	        	blockT2(blockT2);
	        	blockT3(blockT3);
	        	blockT4(blockT4);
	        }
	      }
	    });
	    
	    // Set data provider
	    bulletinBrancheDataProvider.addDataDisplay( tblBranches );
	}
	

	/**/
	private void initializeBulletinSubjectTable() {
		// Add a text column to show the name.
		TextColumn<BulletinSubjectProxy> colSubjectName = new TextColumn<BulletinSubjectProxy>() {
 	      @Override
 	      public String getValue(BulletinSubjectProxy object) {
 	        return object.getStudentName();
 	      }
 	    };
 	    tblBulletinSubjects.addColumn(colSubjectName, "Elève");
   
 	    
 	    // Add a selection model to handle user selection.
	    final SingleSelectionModel<BulletinSubjectProxy> selectionModel = new SingleSelectionModel<BulletinSubjectProxy>();
	    tblBulletinSubjects.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	        selectedBulletinSubject = selectionModel.getSelectedObject();
	        if (selectedBulletinSubject != null) {
	        	//
	        	bulletinSubjectAverageDataProvider.getList().clear();
	        	bulletinSubjectAverageDataProvider.getList().add(selectedBulletinSubject);
	        	//
	        	selectedBulletinBrancheIndex = -1;
	        	tblBranches.getSelectionModel().setSelected(selectedBulletinBranche, false);
	        	selectedBulletinBranche = null;
	        	clearInputFields();
	        	//
	        	selectedBulletinSubjectIndex = bulletinSubjectDataProvider.getList().indexOf(selectedBulletinSubject);
	        	getUiHandlers().onBulletinSubjectSelected(selectedBulletinSubject);	        	
	        }
	      }
	    });
	    
	    // Set data provider
	    bulletinSubjectDataProvider.addDataDisplay( tblBulletinSubjects );
	}
	

	/**/
	@Override
	public void setBulletinSubjectTableData(List<BulletinSubjectProxy> bulletinSubjects) {
		//
		bulletinSubjectDataProvider.getList().clear();
		bulletinSubjectDataProvider.setList(bulletinSubjects);
	}

	
	/**/
	@Override
	public void setBulletinBrancheTableData(List<BulletinBrancheProxy> bulletinBranches) {
		// 
		bulletinBrancheDataProvider.getList().clear();
		bulletinBrancheDataProvider.setList(bulletinBranches);
	}
	
	
	/**/
	@UiHandler("lstAssignments")
	void onLstAssignmentsChange(ChangeEvent event) {
		//
		bulletinSubjectDataProvider.getList().clear();
		bulletinBrancheDataProvider.getList().clear();
		//
		selectedBulletinBrancheIndex = -1;
    	tblBranches.getSelectionModel().setSelected(selectedBulletinBranche, false);
    	selectedBulletinBranche = null;
    	//
		selectedBulletinSubjectIndex = -1;
    	tblBulletinSubjects.getSelectionModel().setSelected(selectedBulletinSubject, false);
    	selectedBulletinSubject = null;
		//
		clearInputFields();
		//
		if (getUiHandlers() != null)
			getUiHandlers().onAssignmentSelected( lstAssignments.getValue(lstAssignments.getSelectedIndex()));
	}

	@Override
	public void showCurrentNotes() {
		//
		txt_t_1_1.setText( selectedBulletinBranche.getT1_1() );
		txt_t_1_2.setText( selectedBulletinBranche.getT1_2() );
		txt_t_1_3.setText( selectedBulletinBranche.getT1_3() );
		txt_t_1_4.setText( selectedBulletinBranche.getT1_4() );
		txt_t_1_5.setText( selectedBulletinBranche.getT1_5() );
		//
		txt_t_2_1.setText( selectedBulletinBranche.getT2_1() );
		txt_t_2_2.setText( selectedBulletinBranche.getT2_2() );
		txt_t_2_3.setText( selectedBulletinBranche.getT2_3() );
		txt_t_2_4.setText( selectedBulletinBranche.getT2_4() );
		txt_t_2_5.setText( selectedBulletinBranche.getT2_5() );
		//
		txt_t_3_1.setText( selectedBulletinBranche.getT3_1() );
		txt_t_3_2.setText( selectedBulletinBranche.getT3_2() );
		txt_t_3_3.setText( selectedBulletinBranche.getT3_3() );
		txt_t_3_4.setText( selectedBulletinBranche.getT3_4() );
		txt_t_3_5.setText( selectedBulletinBranche.getT3_5() );
		//
		txt_t_4_1.setText( selectedBulletinBranche.getT4_1() );
		txt_t_4_2.setText( selectedBulletinBranche.getT4_2() );
		txt_t_4_3.setText( selectedBulletinBranche.getT4_3() );
		txt_t_4_4.setText( selectedBulletinBranche.getT4_4() );
		txt_t_4_5.setText( selectedBulletinBranche.getT4_5() );
		//
		txtT1.setText( selectedBulletinBranche.getT1() );
		txtT2.setText( selectedBulletinBranche.getT2() );
		txtT3.setText( selectedBulletinBranche.getT3() );
		txtT4.setText( selectedBulletinBranche.getT4() );
		//
		modifyUiByProgramme();
	}
	
	
	/*
	 * */
	@Override
	public void showCurrentRemarques(){
		//
		txtRemarque1.setText( selectedBulletinSubject.getRemarqueT1() );
		txtRemarque2.setText( selectedBulletinSubject.getRemarqueT2() );
		txtRemarque3.setText( selectedBulletinSubject.getRemarqueT3() );
		txtRemarque4.setText( selectedBulletinSubject.getRemarqueT4() );
	}
	
	
	/*
	 * */
	@UiHandler("cmdSave")
	void onCmdSaveClick(ClickEvent event) {
		if (getUiHandlers() != null)
			getUiHandlers().saveNotes(selectedBulletinBranche, selectedBulletinSubject, 
					txt_t_1_1.getText(), txt_t_1_2.getText(), txt_t_1_3.getText(), txt_t_1_4.getText(), txt_t_1_5.getText(), 
					txt_t_2_1.getText(), txt_t_2_2.getText(), txt_t_2_3.getText(), txt_t_2_4.getText(), txt_t_2_5.getText(), 
					txt_t_3_1.getText(), txt_t_3_2.getText(), txt_t_3_3.getText(), txt_t_3_4.getText(), txt_t_3_5.getText(), 
					txt_t_4_1.getText(), txt_t_4_2.getText(), txt_t_4_3.getText(), txt_t_4_4.getText(), txt_t_4_5.getText(), 
					txtRemarque1.getText(), txtRemarque2.getText(), txtRemarque3.getText(), txtRemarque4.getText() );
	}

	/*
	 * */
	@Override
	public void showUpdatedBulletinDetails(
			BulletinBrancheProxy bulletinBranche,
			BulletinSubjectProxy bulletinSubject) {
		//
		bulletinSubjectDataProvider.getList().set(selectedBulletinSubjectIndex, bulletinSubject);
		bulletinSubjectDataProvider.flush();
		//
		selectedBulletinBranche = bulletinBranche;
		bulletinBrancheDataProvider.getList().set(selectedBulletinBrancheIndex, bulletinBranche);
		bulletinBrancheDataProvider.flush();
		//
		bulletinSubjectAverageDataProvider.getList().set(0, bulletinSubject);
		bulletinSubjectAverageDataProvider.flush();
		//
		txtT1.setText( bulletinBranche.getT1() );
		txtT2.setText( bulletinBranche.getT2() );
		txtT3.setText( bulletinBranche.getT3() );
		txtT4.setText( bulletinBranche.getT4() );
	}
	

	/*
	 * 
	 * */
	@Override
	public void modifyUiByProgramme() {
		//
		if (selectedBulletinBrancheIndex > -1){
			pnlT1.setVisible(true);
			pnlT2.setVisible(true);
			cmdSave.setVisible(true);
		}
		//
		if (lstAssignments.getItemText( lstAssignments.getSelectedIndex() ).toLowerCase().contains("prématurité")){
			tblAverageGrade.setColumnWidth(4, 12, Unit.PCT);
			tblAverageGrade.setColumnWidth(5, 12, Unit.PCT);
			tblAverageGrade.setColumnWidth(6, 12, Unit.PCT);
			tblAverageGrade.setColumnWidth(7, 12, Unit.PCT);	
			
			tblBranches.setColumnWidth(4, 15, Unit.PCT);
			tblBranches.setColumnWidth(5, 15, Unit.PCT);
			
			if (selectedBulletinBrancheIndex > -1) {
				if ( !pnlT3.isVisible() ) pnlT3.setVisible(true);
				if ( !pnlT4.isVisible() ) pnlT4.setVisible(true);
			}
			return;
		}
		//
		if (lstAssignments.getItemText( lstAssignments.getSelectedIndex() ).toLowerCase().contains("matu")){
			tblAverageGrade.setColumnWidth(4, "0px");
			tblAverageGrade.setColumnWidth(5, "0px");
			tblAverageGrade.setColumnWidth(6, "0px");
			tblAverageGrade.setColumnWidth(7, "0px");
			
			tblBranches.setColumnWidth(4, "0px");
			tblBranches.setColumnWidth(5, "0px");
			
			pnlT3.setVisible(false);
			pnlT4.setVisible(false);
		} else {			
			tblAverageGrade.setColumnWidth(4, 15, Unit.PCT);
			tblAverageGrade.setColumnWidth(5, 15, Unit.PCT);
			tblAverageGrade.setColumnWidth(6, "0px");
			tblAverageGrade.setColumnWidth(7, "0px");
			
			tblBranches.setColumnWidth(4, 15, Unit.PCT);
			tblBranches.setColumnWidth(5, "0px");
			
			if (selectedBulletinBrancheIndex > -1)
				if ( !pnlT3.isVisible() ) pnlT3.setVisible(true);
			pnlT4.setVisible(false);
		}
	}


	/*
	 * 
	 * */
	@Override
	public void modifyUiByDeadline(List<SettingOptionProxy> settings, CurrentUser currentUser) {
		// 
		int currentDate = Integer.parseInt(
				Integer.toString(currentUser.getCurrentYear())
				+ ((currentUser.getCurrentMonth() < 10) ? ("0" + Integer.toString(currentUser.getCurrentMonth())) : Integer.toString(currentUser.getCurrentMonth()))  
				+ ((currentUser.getCurrentDay() < 10) ? ("0" + Integer.toString(currentUser.getCurrentDay())) : Integer.toString(currentUser.getCurrentDay())) );
		//
		int deadLine = 0;
		for (SettingOptionProxy setting : settings) {
			//
			if (lstAssignments.getItemText( lstAssignments.getSelectedIndex() ).toLowerCase().contains("maturité suisse")) {
					if (!lstAssignments.getItemText( lstAssignments.getSelectedIndex() ).toLowerCase().contains("préalable"))	{
						if (setting.getOptionName().equals(NotificationValues.deadline_matu_t1)) {
							deadLine = Integer.parseInt( setting.getOptionValue() );
							if (deadLine <= currentDate) blockT1 = true;
							else blockT1 = false;
						}
						if (setting.getOptionName().equals(NotificationValues.deadline_matu_t2)) {
							deadLine = Integer.parseInt( setting.getOptionValue() );
							if (deadLine <= currentDate) blockT2 = true;
							else blockT2 = false;
						}
					}
			}
			//
			if (lstAssignments.getItemText( lstAssignments.getSelectedIndex() ).toLowerCase().contains("baccalauréat français")) {
				if (setting.getOptionName().equals(NotificationValues.deadline_bac_t1)) {
					deadLine = Integer.parseInt( setting.getOptionValue() );
					if (deadLine <= currentDate) blockT1 = true;
					else blockT1 = false;
				}
				if (setting.getOptionName().equals(NotificationValues.deadline_bac_t2)) {
					deadLine = Integer.parseInt( setting.getOptionValue() );
					if (deadLine <= currentDate) blockT2 = true;
					else blockT2 = false;
				}
				if (setting.getOptionName().equals(NotificationValues.deadline_bac_t3)) {
					deadLine = Integer.parseInt( setting.getOptionValue() );
					if (deadLine <= currentDate) blockT3 = true;
					else blockT3 = false;
				}
			}
			//
			if (lstAssignments.getItemText( lstAssignments.getSelectedIndex() ).toLowerCase().contains("etudes secondaires")) {
				if (setting.getOptionName().equals(NotificationValues.deadline_es_t1)) {
					deadLine = Integer.parseInt( setting.getOptionValue() );
					if (deadLine <= currentDate) blockT1 = true;
					else blockT1 = false;
				}
				if (setting.getOptionName().equals(NotificationValues.deadline_es_t2)) {
					deadLine = Integer.parseInt( setting.getOptionValue() );
					if (deadLine <= currentDate) blockT2 = true;
					else blockT2 = false;
				}
				if (setting.getOptionName().equals(NotificationValues.deadline_es_t3)) {
					deadLine = Integer.parseInt( setting.getOptionValue() );
					if (deadLine <= currentDate) blockT3 = true;
					else blockT3 = false;
				}
			}
		}
	}

	/*
	 * 
	 * */
	private void blockT1(boolean block) {
		// 
		txt_t_1_1.setEnabled(!block);
		txt_t_1_2.setEnabled(!block);
		txt_t_1_3.setEnabled(!block);
		txt_t_1_4.setEnabled(!block);
		txt_t_1_5.setEnabled(!block);
		txtRemarque1.setEnabled(!block);
	}
	
	/*
	 * 
	 * */
	private void blockT2(boolean block) {
		// 
		txt_t_2_1.setEnabled(!block);
		txt_t_2_2.setEnabled(!block);
		txt_t_2_3.setEnabled(!block);
		txt_t_2_4.setEnabled(!block);
		txt_t_2_5.setEnabled(!block);
		txtRemarque2.setEnabled(!block);
	}
	
	/*
	 * 
	 * */
	private void blockT3(boolean block) {
		// 
		txt_t_3_1.setEnabled(!block);
		txt_t_3_2.setEnabled(!block);
		txt_t_3_3.setEnabled(!block);
		txt_t_3_4.setEnabled(!block);
		txt_t_3_5.setEnabled(!block);
		txtRemarque3.setEnabled(!block);
	}
	
	/*
	 * 
	 * */
	private void blockT4(boolean block) {
		// 
		txt_t_4_1.setEnabled(!block);
		txt_t_4_2.setEnabled(!block);
		txt_t_4_3.setEnabled(!block);
		txt_t_4_4.setEnabled(!block);
		txt_t_4_5.setEnabled(!block);
		txtRemarque4.setEnabled(!block);
	}
}
