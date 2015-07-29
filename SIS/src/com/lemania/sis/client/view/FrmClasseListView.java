package com.lemania.sis.client.view;

import java.util.List;

import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.lemania.sis.client.presenter.FrmClasseListPresenter;
import com.lemania.sis.client.uihandler.FrmClasseListUiHandler;
import com.lemania.sis.shared.ClasseProxy;
import com.lemania.sis.shared.CoursProxy;
import com.lemania.sis.shared.EcoleProxy;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ChangeEvent;

public class FrmClasseListView extends ViewWithUiHandlers<FrmClasseListUiHandler> implements
		FrmClasseListPresenter.MyView {

	private final Widget widget;
	
	// Thuan
	private int selectedClasse = -1;
	ListDataProvider<ClasseProxy> dataProvider = new ListDataProvider<ClasseProxy>();

	public interface Binder extends UiBinder<Widget, FrmClasseListView> {
	}

	@Inject
	public FrmClasseListView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	@UiField(provided=true) DataGrid<ClasseProxy> tblClasseList = new DataGrid<ClasseProxy>();
	@UiField ListBox lstEcoles;
	@UiField ListBox lstCours;
	
	@Override
	public void initializeTable() {
		//
		// Add a text column to show the name.
	    Column<ClasseProxy, String> colName = new Column<ClasseProxy, String>(new EditTextCell()) {
	      @Override
	      public String getValue(ClasseProxy object) {
	        return object.getClassName();
	      }
	    };
	    tblClasseList.addColumn(colName, "Nom de la classe");
	    colName.setFieldUpdater(new FieldUpdater<ClasseProxy, String>(){
	    	@Override
	    	public void update(int index, ClasseProxy classe, String value){
	    		if (getUiHandlers() != null) {	    			
	    			selectedClasse = index;
	    			getUiHandlers().updateClasse(classe, value, classe.getIsActive());
	    		}	    		
	    	}
	    });
	    
	    // Active
	    CheckboxCell cellActive = new CheckboxCell();
	    Column<ClasseProxy, Boolean> colActive = new Column<ClasseProxy, Boolean>(cellActive) {
	    	@Override
	    	public Boolean getValue(ClasseProxy classe){
	    		return classe.getIsActive();
	    	}	    	
	    };
	    tblClasseList.addColumn(colActive, "Active");
	    colActive.setFieldUpdater(new FieldUpdater<ClasseProxy, Boolean>(){
	    	@Override
	    	public void update(int index, ClasseProxy classe, Boolean value){
	    		if (getUiHandlers() != null) {
	    			selectedClasse = index;
	    			getUiHandlers().updateClasse(classe, classe.getClassName(), value);
	    		}	    		
	    	}
	    });
	    //
	    
	    dataProvider.addDataDisplay(tblClasseList);
	}

	@Override
	public void setClasseListData(List<ClasseProxy> classeList) {
		//
		dataProvider.setList(classeList);
	}

	@Override
	public void refreshUpdatedClasse(ClasseProxy classe) {
		//
		dataProvider.getList().remove(selectedClasse);
		dataProvider.getList().add(selectedClasse, classe);
		dataProvider.refresh();
	}

	@Override
	public void setEcoleList(List<EcoleProxy> ecoleList) {
		// First clear the existing data
		lstEcoles.clear();
		
		// Set new list
		lstEcoles.addItem("-", "");
		for ( EcoleProxy ecole : ecoleList )
			lstEcoles.addItem(ecole.getSchoolName(), ecole.getId().toString());
	}
	
	@UiHandler("lstEcoles")
	void onLstEcolesChange(ChangeEvent event) {
		// If user select the first item, which is null, clear the program list
		if (lstEcoles.getValue(lstEcoles.getSelectedIndex()).isEmpty()) {
			lstCours.clear();
			return;
		}
		
		//
		if (getUiHandlers() != null)
			getUiHandlers().onEcoleSelected( lstEcoles.getValue( lstEcoles.getSelectedIndex() ));
	}

	@Override
	public void setCoursList(List<CoursProxy> subjectList) {
		// First clear existing data
		lstCours.clear();
		
		// Set new list
		lstCours.addItem("-", "");
		for ( CoursProxy cours : subjectList )
			lstCours.addItem( cours.getCoursNom(), cours.getId().toString() );
	}
	
	@UiHandler("lstCours")
	void onLstCoursChange(ChangeEvent event) {
		//
		if (getUiHandlers() != null)
			getUiHandlers().onSubjectSelected( lstCours.getValue( lstCours.getSelectedIndex() ));
	}

	@Override
	public void resetForm() {
		//
		lstEcoles.setSelectedIndex(0);
		lstCours.clear();
		dataProvider.getList().clear();
	}
}
