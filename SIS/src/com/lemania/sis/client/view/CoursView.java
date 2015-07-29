package com.lemania.sis.client.view;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.lemania.sis.client.presenter.CoursPresenter;
import com.lemania.sis.client.uihandler.CoursListUiHandler;
import com.lemania.sis.shared.CoursProxy;
import com.lemania.sis.shared.EcoleProxy;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.ListBox;

public class CoursView extends ViewWithUiHandlers<CoursListUiHandler> implements CoursPresenter.MyView {

	private final Widget widget;
	
	// Thuan
	private int selectedCours;

	public interface Binder extends UiBinder<Widget, CoursView> {
	}

	@Inject
	public CoursView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	@UiField(provided=true) DataGrid<CoursProxy> tblCours = new DataGrid<CoursProxy>();
	@UiField ListBox lstEcoles;
	
	@Override
	public void addCours(CoursProxy cours) {
		// TODO Auto-generated method stub
		List<CoursProxy> newCours = new ArrayList<CoursProxy>();
		newCours.add(cours);
		tblCours.setRowData(tblCours.getRowCount()+1, newCours);
	}

	@Override
	public void setData(List<CoursProxy> listCours) {
	    // add the data
	    tblCours.setRowData(listCours);
	    tblCours.redraw();
	}
	
	/*
	 * 
	 * */
	@Override
	public void initializeTable(){
		// Add a text column to show the name.
		EditTextCell cellName = new EditTextCell();
	    Column<CoursProxy, String> colName = new Column<CoursProxy, String>(cellName) {
	      @Override
	      public String getValue(CoursProxy object) {
	        return object.getCoursNom();
	      }
	    };
	    tblCours.addColumn(colName, "Nom");
	    colName.setFieldUpdater(new FieldUpdater<CoursProxy, String>(){
	    	@Override
	    	public void update(int index, CoursProxy cours, String value){
	    		if (getUiHandlers() != null) {
	    			selectedCours = index;
	    			getUiHandlers().updateCoursName(cours, value);
	    		}	    		
	    	}
	    });
	    
	    
	    // Active
	    CheckboxCell cellActive = new CheckboxCell();
	    Column<CoursProxy, Boolean> colActive = new Column<CoursProxy, Boolean>(cellActive) {
	    	@Override
	    	public Boolean getValue(CoursProxy cours){
	    		return cours.getCoursActif();
	    	}	    	
	    };
	    tblCours.addColumn(colActive, "Actif");
	    colActive.setFieldUpdater(new FieldUpdater<CoursProxy, Boolean>(){
	    	@Override
	    	public void update(int index, CoursProxy cours, Boolean value){
	    		if (getUiHandlers() != null) {
	    			selectedCours = index;
	    			getUiHandlers().updateCoursStatus(cours, value);
	    		}	    		
	    	}
	    });
	}

	@Override
	public void refreshTable(CoursProxy updatedCours) {
		// TODO Auto-generated method stub
		List<CoursProxy> listCours = new ArrayList<CoursProxy>();
		listCours.add(updatedCours);
		tblCours.setRowData(selectedCours, listCours);
		tblCours.redraw();
	}

	@Override
	public void populateEcoleList(List<EcoleProxy> ecoles) {
		// Thuan: populate the list of school names
		lstEcoles.clear();
		lstEcoles.addItem("-","");
		for (int i=0; i<ecoles.size(); i++)
			lstEcoles.addItem(ecoles.get(i).getSchoolName(), ecoles.get(i).getId().toString());
	}
	
	@UiHandler("lstEcoles")
	void onLstEcolesSelected(ChangeEvent event){
		if (getUiHandlers() != null)
			getUiHandlers().populateCoursList(lstEcoles.getValue(lstEcoles.getSelectedIndex()));
	}
}
