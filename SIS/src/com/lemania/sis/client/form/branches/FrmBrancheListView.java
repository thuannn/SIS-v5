package com.lemania.sis.client.form.branches;

import java.util.List;

import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.BrancheProxy;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.user.cellview.client.SimplePager;

public class FrmBrancheListView extends ViewWithUiHandlers<FrmBrancheListUiHandler> implements
		FrmBrancheListPresenter.MyView {

	private final Widget widget;
	
	// Thuan
	private int selectedBranche = -1;
	ListDataProvider<BrancheProxy> dataProvider = new ListDataProvider<BrancheProxy>();

	public interface Binder extends UiBinder<Widget, FrmBrancheListView> {
	}
	

	@Inject
	public FrmBrancheListView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	
	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@UiField(provided=true) DataGrid<BrancheProxy> tblBranche = new DataGrid<BrancheProxy>();
	@UiField SimplePager pagerBranches;
	
	
	@Override
	public void initializeBrancheTable() {
		//
		// Add a text column to show the name.
	    Column<BrancheProxy, String> colBrancheName = new Column<BrancheProxy, String>(new EditTextCell()) {
	      @Override
	      public String getValue(BrancheProxy object) {
	        return object.getBrancheName();
	      }
	    };
	    tblBranche.addColumn(colBrancheName, "Nom");
	    // Field updater
	    colBrancheName.setFieldUpdater(new FieldUpdater<BrancheProxy, String>(){
	    	@Override
	    	public void update(int index, BrancheProxy branche, String value){
	    		if (getUiHandlers() != null) {	    			
	    			selectedBranche = index;
	    			getUiHandlers().updateBranche( branche, value, branche.getDefaultCoef().toString(), branche.getIsActive() );
	    		}	    		
	    	}
	    });
	    
	    //
	    Column<BrancheProxy, String> colCoef = new Column<BrancheProxy, String>(new EditTextCell()) {
	      @Override
	      public String getValue(BrancheProxy object) {
	        return object.getDefaultCoef().toString();
	      } 
	    };
	    tblBranche.addColumn( colCoef, "Coefficient" );
	    // Field updater
	    colCoef.setFieldUpdater(new FieldUpdater<BrancheProxy, String>(){
	    	@Override
	    	public void update(int index, BrancheProxy branche, String value){
	    		if (getUiHandlers() != null) {	    			
	    			selectedBranche = index;
	    			getUiHandlers().updateBranche( branche, branche.getBrancheName(), value, branche.getIsActive() );
	    		}	    		
	    	}
	    });
	    
	    //
	    CheckboxCell cellActive = new CheckboxCell();
	    Column<BrancheProxy, Boolean> colActive = new Column<BrancheProxy, Boolean>(cellActive) {
	    	@Override
	    	public Boolean getValue(BrancheProxy branche){
	    		return branche.getIsActive();
	    	}	    	
	    };
	    tblBranche.addColumn(colActive, "Active");
	    // Field updater
	    colActive.setFieldUpdater(new FieldUpdater<BrancheProxy, Boolean>(){
	    	@Override
	    	public void update(int index, BrancheProxy branche, Boolean value){
	    		if (getUiHandlers() != null) {	    			
	    			selectedBranche = index;
	    			getUiHandlers().updateBranche( branche, branche.getBrancheName(), branche.getDefaultCoef().toString(), value );
	    		}	    		
	    	}
	    });
	    
	    //
	    pagerBranches.setDisplay(tblBranche);
	    
	    //
	    dataProvider.addDataDisplay(tblBranche);
	}

	
	@Override
	public void setBrancheTableData(List<BrancheProxy> branches) {
		//
		dataProvider.setList(branches);
		dataProvider.flush();
	}

	
	@Override
	public void refreshUpdatedBranche(BrancheProxy branche) {
		//
		dataProvider.getList().remove(selectedBranche);
		dataProvider.getList().add(selectedBranche, branche);
		dataProvider.refresh();
	}
}
