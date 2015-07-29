package com.lemania.sis.client.form.evaluations;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.lemania.sis.client.values.AppSettingValues;
import com.lemania.sis.client.values.EvaluationValues;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.ClasseProxy;
import com.lemania.sis.shared.CoursProxy;
import com.lemania.sis.shared.EcoleProxy;
import com.lemania.sis.shared.EvaluationHeaderProxy;
import com.lemania.sis.shared.EvaluationStudentReportProxy;
import com.lemania.sis.shared.EvaluationSubjectProxy;
import com.lemania.sis.shared.bulletin.BulletinProxy;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;

public class FrmEvaluationInputStudentView extends ViewWithUiHandlers<FrmEvaluationInputStudentUiHandler> implements
		FrmEvaluationInputStudentPresenter.MyView {

	private final Widget widget;
	
	//
	private List<BulletinProxy> bulletins = new ArrayList<BulletinProxy>();
	private List<EvaluationHeaderProxy> evaluationHeaders = new ArrayList<EvaluationHeaderProxy>(); 
	private ListDataProvider<EvaluationSubjectProxy> providerEvaluationSubject = new ListDataProvider<EvaluationSubjectProxy>();
	private int selectedEvaluationSubjectIndex = -1;
	

	public interface Binder extends UiBinder<Widget, FrmEvaluationInputStudentView> {
	}

	@Inject
	public FrmEvaluationInputStudentView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	@UiField(provided=true) DataGrid<EvaluationSubjectProxy> tblEvaluations = new DataGrid<EvaluationSubjectProxy>();
	@UiField ListBox lstYears;
	@UiField ListBox lstEcoles;
	@UiField ListBox lstProgrammes;
	@UiField ListBox lstClasses;
	@UiField ListBox lstEvaluationHeaders;
	@UiField TextArea txtCommentaire;
	@UiField Button cmdSave;
	@UiField ListBox lstBulletins;
	@UiField VerticalPanel pnlEvaluationPrint;
	@UiField VerticalPanel pnlEvaluationMain;
	@UiField Label lblObjective;
	@UiField Label lblStudentName;
	@UiField Label lblFromDate;
	@UiField Label lblToDate;
	@UiField Label lblClass;
	@UiField Label lblClassMaster;
	@UiField Button cmdPrint;
	
	/*
	 * */
	@Override
	public void initializeUI() {
		//
		initializeYearList();		
		
		//
		TextColumn<EvaluationSubjectProxy> colSubjectName = new TextColumn<EvaluationSubjectProxy>() {
 	      @Override
 	      public String getValue(EvaluationSubjectProxy object) {
 	        return object.getSubjectName();
 	      }
 	    };
 	    tblEvaluations.addColumn(colSubjectName, "Matière");
 	    
 	   //
		TextColumn<EvaluationSubjectProxy> colProfName = new TextColumn<EvaluationSubjectProxy>() {
 	      @Override
 	      public String getValue(EvaluationSubjectProxy object) {
 	        return object.getProfessorName();
 	      }
 	    };
 	    tblEvaluations.addColumn(colProfName, "Professeur");
 	    
 	    // 	    
		TextColumn<EvaluationSubjectProxy> colObjective1 = new TextColumn<EvaluationSubjectProxy>() {
 	      @Override
 	      public String getValue(EvaluationSubjectProxy object) {
 	        return object.getObjective1();
 	      }
 	    };
 	   tblEvaluations.setColumnWidth(colObjective1, 12, Unit.PCT);
 	    tblEvaluations.addColumn(colObjective1, EvaluationValues.Objective1); 	    
 	     	    
 	    // 	     	    
		TextColumn<EvaluationSubjectProxy> colObjective2 = new TextColumn<EvaluationSubjectProxy>() {
 	      @Override
 	      public String getValue(EvaluationSubjectProxy object) {
 	        return object.getObjective2();
 	      }
 	    };
 	    tblEvaluations.setColumnWidth(colObjective2, 12, Unit.PCT);
 	    tblEvaluations.addColumn(colObjective2, EvaluationValues.Objective2);
 	    
 	    
    	// 	    
 	    TextColumn<EvaluationSubjectProxy> colObjective3 = new TextColumn<EvaluationSubjectProxy>() {
 	      @Override
 	      public String getValue(EvaluationSubjectProxy object) {
 	        return object.getObjective3();
 	      }
 	    };
 	    tblEvaluations.setColumnWidth(colObjective3, 12, Unit.PCT);
 	    tblEvaluations.addColumn(colObjective3, EvaluationValues.Objective3);
 	  
    	// 	    
 	    TextColumn<EvaluationSubjectProxy> colObjective4 = new TextColumn<EvaluationSubjectProxy>() {
 	      @Override
 	      public String getValue(EvaluationSubjectProxy object) {
 	        return object.getObjective4();
 	      }
 	    };
 	    tblEvaluations.setColumnWidth(colObjective4, 12, Unit.PCT);
 	    tblEvaluations.addColumn(colObjective4, EvaluationValues.Objective4); 	    
 	    
 	    // 	   
 	    TextColumn<EvaluationSubjectProxy> colObjective5 = new TextColumn<EvaluationSubjectProxy>() {
 	      @Override
 	      public String getValue(EvaluationSubjectProxy object) {
 	        return object.getObjective5();
 	      }
 	    };
 	    tblEvaluations.setColumnWidth(colObjective5, 12, Unit.PCT);
 	    tblEvaluations.addColumn(colObjective5, EvaluationValues.Objective5); 	   
 	    
// 	    // 20150519 : not in use anymore 	    
// 	    TextColumn<EvaluationSubjectProxy> colObjective6 = new TextColumn<EvaluationSubjectProxy>() {
// 	      @Override
// 	      public String getValue(EvaluationSubjectProxy object) {
// 	        return object.getObjective6();
// 	      }
// 	    };
// 	    tblEvaluations.setColumnWidth(colObjective6, 10, Unit.PCT);
// 	    tblEvaluations.addColumn(colObjective6, EvaluationValues.Objective6); 	
 	    
 	    //
	    Column<EvaluationSubjectProxy, String> colDelete = new Column<EvaluationSubjectProxy, String> (new ButtonCell()){
	    	@Override
	    	public String getValue(EvaluationSubjectProxy es){
	    		return "X";
	    	}
	    };
	    colDelete.setFieldUpdater(new FieldUpdater<EvaluationSubjectProxy, String>(){
	    	@Override
	    	public void update(int index, EvaluationSubjectProxy es, String value){
	    		selectedEvaluationSubjectIndex = index;
	    		getUiHandlers().removeEvaluationSubject( es );
	    	}
	    });	    
	    tblEvaluations.setColumnWidth(colDelete, 5, Unit.PCT);
	    tblEvaluations.addColumn(colDelete, "");
 	    
 	    //
 	    providerEvaluationSubject.addDataDisplay(tblEvaluations); 
	}
	
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
	public void resetForm() {		
		//
		lstBulletins.setSelectedIndex(0);
		lstEvaluationHeaders.clear();
		//
		providerEvaluationSubject.getList().clear();
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
	@UiHandler("lstClasses")
	void onLstClassesChange(ChangeEvent event) {
		//
		getUiHandlers().onClassSelected(lstClasses.getValue(lstClasses.getSelectedIndex()));
	}
	

	/*
	 * */
	@Override
	public void setStudentListData(List<BulletinProxy> bulletins) {
		//
		if (this.bulletins != null)
			this.bulletins.clear();
		this.bulletins.addAll(bulletins);
		//
		lstBulletins.clear();
		lstBulletins.addItem("-","");
		for (BulletinProxy bulletin : bulletins) {
			lstBulletins.addItem( bulletin.getStudentName(), bulletin.getId().toString());
		}		
	}
	
	/*
	 * */
	@UiHandler("lstBulletins")
	void onLstBulletinsChange(ChangeEvent event) {
		//
		clearStudentReportUI();
		//
		lblStudentName.setText( lstBulletins.getItemText(lstBulletins.getSelectedIndex()) );
		//
		getUiHandlers().onBulletinSelected( lstClasses.getValue(lstClasses.getSelectedIndex()) );
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
		//
		evaluationHeaders.clear();
		evaluationHeaders.addAll(headers);
	}
	
	/*
	 * */
	@UiHandler("lstEvaluationHeaders")
	void onLstEvaluationHeadersChange(ChangeEvent event) {		
		//
		if (lstEvaluationHeaders.getValue(lstEvaluationHeaders.getSelectedIndex()).equals(""))
			return;
		//	
		EvaluationHeaderProxy eh = evaluationHeaders.get(lstEvaluationHeaders.getSelectedIndex()-1);
		lblFromDate.setText( eh.getFromDate() );
		lblToDate.setText( eh.getToDate() );
		lblObjective.setText( eh.getObjective() );
		lblClass.setText( lstClasses.getItemText(lstClasses.getSelectedIndex()) );
		lblClassMaster.setText( eh.getClassMasterName() );
		//
		getUiHandlers().onEvaluationHeaderSelected(
				lstClasses.getValue(lstClasses.getSelectedIndex()), 
				lstBulletins.getValue(lstBulletins.getSelectedIndex()), 
				lstEvaluationHeaders.getValue(lstEvaluationHeaders.getSelectedIndex()),
				eh.getClassMasterId() );
	}

	/*
	 * */
	private void clearStudentReportUI() {
		//				
		lblFromDate.setText("");
		lblToDate.setText("");
		lblClass.setText("");
		lblClassMaster.setText("");		
		lblStudentName.setText("");
		lblObjective.setText("");
		providerEvaluationSubject.getList().clear();
		txtCommentaire.setText("");
	}

	/*
	 * */
	@Override
	public void setEvaluationSubjectTableData(
			List<EvaluationSubjectProxy> evaluationSubject) {
		//
		providerEvaluationSubject.getList().clear();
		providerEvaluationSubject.setList(evaluationSubject);
		//
		tblEvaluations.setHeight( (NotificationValues.lineHeight * (evaluationSubject.size()+1) + 100) + "px");
	}
	
	/*
	 * */
	@UiHandler("cmdSave")
	void onCmdSaveClick(ClickEvent event) {
		//
		getUiHandlers().saveEvaluationStudentReport( 
				lstBulletins.getValue(lstBulletins.getSelectedIndex()), 
				lstEvaluationHeaders.getValue(lstEvaluationHeaders.getSelectedIndex()),
				txtCommentaire.getText() );
	}

	/*
	 * */
	@Override
	public void enableCommentEditing(Boolean isClassMaster) {
		//
		cmdSave.setEnabled( isClassMaster );
	}
	

	/*
	 * */
	@Override
	public void setStudentReportData(EvaluationStudentReportProxy report) {
		//
		txtCommentaire.setText( report.getEvaluationNote() );
	}
	
	
	/*
	 * */
	@UiHandler("cmdPrint")
	void onCmdPrintClick(ClickEvent event) {
		// 
		// pnlEvaluationPrint.setHeight(NotificationTypes.bulletinPageHeight.toString() + "px");
		// 
		PopupPanel popup = new PopupPanel(true) {
			@Override
			  protected void onPreviewNativeEvent(final NativePreviewEvent event) {
			    super.onPreviewNativeEvent(event);
			    switch (event.getTypeInt()) {
			        case Event.ONKEYDOWN:
			            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
			                hide();
			            }
			            break;
			    }
			}
		};
		//
		popup.setStyleName("evaluation");
		popup.add(pnlEvaluationPrint);
		pnlEvaluationMain.getParent().getParent().getParent().getParent().setVisible(false);
		//
		cmdSave.setVisible(false);
		tblEvaluations.setColumnWidth(tblEvaluations.getColumn(tblEvaluations.getColumnCount()-1), 0, Unit.PX);
		//
		popup.addCloseHandler(new CloseHandler<PopupPanel>() {
			public void onClose(CloseEvent<PopupPanel> event) {
				//
				pnlEvaluationMain.getParent().getParent().getParent().getParent().setVisible(true);
				pnlEvaluationMain.add(pnlEvaluationPrint);
				pnlEvaluationPrint.setHeight("100%");
				tblEvaluations.setColumnWidth(tblEvaluations.getColumn(tblEvaluations.getColumnCount()-1), 5, Unit.PCT);
				cmdSave.setVisible(true);
			}
		});
		//
		popup.show();
	}

	

	/*
	 * */
	@Override
	public void removeDeletedEvaluationSubject() {
		//
		providerEvaluationSubject.getList().remove(selectedEvaluationSubjectIndex);
	}
}
