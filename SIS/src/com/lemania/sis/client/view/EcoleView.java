package com.lemania.sis.client.view;

import java.util.ArrayList;
import java.util.List;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.lemania.sis.client.presenter.EcolePresenter;
import com.lemania.sis.client.uihandler.EcoleListUiHandler;
import com.lemania.sis.shared.EcoleProxy;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;

public class EcoleView extends ViewWithUiHandlers<EcoleListUiHandler> implements EcolePresenter.MyView {

	private final Widget widget;
	
	private int selectedEcole;

	public interface Binder extends UiBinder<Widget, EcoleView> {
	}

	@Inject
	public EcoleView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@UiField(provided=true) DataGrid<EcoleProxy> tblEcoles = new DataGrid<EcoleProxy>();

	@Override
	public void setData(List<EcoleProxy> ecoleList) {
	    // Put them together
		tblEcoles.setRowData(ecoleList);
		tblEcoles.setRowCount(ecoleList.size());
	}

	@Override
	public void addEcole(EcoleProxy newEcole) {
		// TODO Auto-generated method stub
		List<EcoleProxy> newEcoles = new ArrayList<EcoleProxy>();
		newEcoles.add(newEcole);
		tblEcoles.setRowData(tblEcoles.getRowCount()+1, newEcoles);
	}
	
	@Override
	public void refreshTable(EcoleProxy updatedEcole){
		List<EcoleProxy> ecoles = new ArrayList<EcoleProxy>();
		ecoles.add(updatedEcole);
        tblEcoles.setRowData(selectedEcole, ecoles);
		tblEcoles.redraw();
		
		// Notify user
		Window.alert("Statut de l'école mis à jour.");
	}

	@Override
	public void initializeTable() {
		// Add a text column to show the name.
	    TextColumn<EcoleProxy> colName = new TextColumn<EcoleProxy>() {
	      @Override
	      public String getValue(EcoleProxy object) {
	        return object.getSchoolName();
	      }
	    };
	    tblEcoles.addColumn(colName, "Nom");
	    
	    TextColumn<EcoleProxy> colAddress = new TextColumn<EcoleProxy>() {
	      @Override
	      public String getValue(EcoleProxy object) {
	        return object.getSchoolAddress();
	      } 
	    };
	    tblEcoles.addColumn(colAddress, "Adresse");
	    
	    CheckboxCell cellActive = new CheckboxCell();
	    Column<EcoleProxy, Boolean> colActive = new Column<EcoleProxy, Boolean>(cellActive) {
	    	@Override
	    	public Boolean getValue(EcoleProxy ecole){
	    		return ecole.getSchoolStatus();
	    	}	    	
	    };
	    tblEcoles.addColumn(colActive, "Active");
	    
	    colActive.setFieldUpdater(new FieldUpdater<EcoleProxy, Boolean>(){
	    	@Override
	    	public void update(int index, EcoleProxy ecole, Boolean value){
	    		if (getUiHandlers() != null) {	    			
	    			selectedEcole = index;
	    			getUiHandlers().updateEcoleStatus(ecole, value);
	    		}	    		
	    	}
	    });
	}
}
