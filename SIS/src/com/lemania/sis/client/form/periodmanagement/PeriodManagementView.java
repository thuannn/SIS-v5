package com.lemania.sis.client.form.periodmanagement;

import java.util.List;

import javax.inject.Inject;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.ListDataProvider;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.ClasseProxy;
import com.lemania.sis.shared.period.PeriodProxy;
import com.lemania.sis.shared.perioditem.PeriodItemProxy;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;

public class PeriodManagementView extends
		ViewWithUiHandlers<PeriodManagementUiHandlers> implements
		PeriodManagementPresenter.MyView {
	
	interface Binder extends UiBinder<Widget, PeriodManagementView> {
	}

	@UiField ListBox lstClasses;
	@UiField TextBox txtDescription;
	@UiField TextBox txtNote;
	@UiField CheckBox chkActif;
	@UiField Button cmdAdd;
	@UiField CellTable<PeriodProxy> tblPeriods;
	@UiField IntegerBox txtOrder;
	@UiField Button cmdAddPeriodItem;
	@UiField ListBox lstPeriods;
	
	//
	ListDataProvider<PeriodProxy> periodDataProvider = new ListDataProvider<PeriodProxy>();
	int selectedPeriodIndex = -1;

	@Inject
	PeriodManagementView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	/*
	 * */
	@Override
	public void initializeUI() {
		//
		initializeTable();
	}
	
	/*
	 * */
	private void initializeTable() {
		//
		// Add a text column to show the name.
	    Column<PeriodProxy, String> colOrder = new Column<PeriodProxy, String>(new EditTextCell()) {
	      @Override
	      public String getValue(PeriodProxy object) {
	        return Integer.toString( object.getOrder() );
	      }
	    };
	    tblPeriods.addColumn(colOrder, "Ordre");
	    colOrder.setFieldUpdater(new FieldUpdater<PeriodProxy, String>(){
	    	@Override
	    	public void update(int index, PeriodProxy period, String value){
	    		//
	    		if (period.getOrder() != Integer.parseInt(value) ) {
		    		if (getUiHandlers() != null) {	    			
		    			selectedPeriodIndex = index;
		    			getUiHandlers().updatePeriod(period, period.getDescription(), Integer.parseInt(value), period.getNote(), period.isActive());
		    		}	    		
	    		}
	    	}
	    });
	    
	    
	    //
	    Column<PeriodProxy, String> colPeriodItemText = new Column<PeriodProxy, String>(new EditTextCell()) {
	      @Override
	      public String getValue(PeriodProxy object) {
	        return object.getPeriodText();
	      }
	    };
	    tblPeriods.addColumn(colPeriodItemText, "Period");
	    
	    //
	    Column<PeriodProxy, String> colDescription = new Column<PeriodProxy, String>(new EditTextCell()) {
		      @Override
		      public String getValue(PeriodProxy object) {
		        return object.getDescription();
		      }
		    };
		tblPeriods.addColumn(colDescription, "Format");
		colDescription.setFieldUpdater(new FieldUpdater<PeriodProxy, String>(){
		    @Override
		    public void update(int index, PeriodProxy period, String value){
		    	//
		    	if ( period.getDescription() != value ) {
			    	if (getUiHandlers() != null) {	    			
			    		selectedPeriodIndex = index;
			    		getUiHandlers().updatePeriod(period, value, period.getOrder(), period.getNote(), period.isActive());
			    	}	    
		    	}
		    }
		});
		    
		//
		Column<PeriodProxy, String> colNote = new Column<PeriodProxy, String>(new EditTextCell()) {
		      @Override
		      public String getValue(PeriodProxy object) {
		        return object.getNote();
		      }
		};
		tblPeriods.addColumn(colNote, "Note");
		colNote.setFieldUpdater(new FieldUpdater<PeriodProxy, String>(){
			@Override
			public void update(int index, PeriodProxy period, String value) {
				//
				if (period.getNote() != value) {
				    if (getUiHandlers() != null) {	    			
				    	selectedPeriodIndex = index;
				    	getUiHandlers().updatePeriod(period, period.getDescription(), period.getOrder(), value, period.isActive());
				    }	    		
				}
			}
		});
	    
	    // Active
	    CheckboxCell cellActive = new CheckboxCell();
	    Column<PeriodProxy, Boolean> colActive = new Column<PeriodProxy, Boolean>(cellActive) {
	    	@Override
	    	public Boolean getValue(PeriodProxy period){
	    		return period.isActive();
	    	}	    	
	    };
	    tblPeriods.addColumn(colActive, "Active");
	    colActive.setFieldUpdater(new FieldUpdater<PeriodProxy, Boolean>(){
	    	@Override
	    	public void update(int index, PeriodProxy period, Boolean value){
	    		//
	    		if (getUiHandlers() != null) {
	    			selectedPeriodIndex = index;
	    			getUiHandlers().updatePeriod(period, period.getDescription(), period.getOrder(), period.getNote(), value);
	    		}	    		
	    	}
	    });
	    //
	    
	    periodDataProvider.addDataDisplay(tblPeriods);
	}

	/*
	 * */
	@Override
	public void setClassList(List<ClasseProxy> classes) {
		//
		lstClasses.clear();
		lstClasses.addItem("-","");
		for (ClasseProxy clazz : classes) {
			lstClasses.addItem( clazz.getClassName(), clazz.getId().toString());
		}
	}
	
	/*
	 * */
	@UiHandler("lstClasses")
	void onLstClassesChange(ChangeEvent event) {
		//
		getUiHandlers().onClassSelected( lstClasses.getValue(lstClasses.getSelectedIndex()) );
	}
	
	/*
	 * */
	@UiHandler("cmdAdd")
	void onCmdAddClick(ClickEvent event) {
		//
		if (txtOrder.getValue() == null) { Window.alert(NotificationValues.invalid_input + " Ordre"); return; }
		//
		getUiHandlers().addPeriod(
				lstPeriods.getValue( lstPeriods.getSelectedIndex() ),
				lstClasses.getValue(lstClasses.getSelectedIndex()), 
				txtDescription.getText(), 
				txtOrder.getValue(), 
				txtNote.getText(), 
				chkActif.getValue());
	}

	/*
	 * */
	@Override
	public void addNewPeriod(PeriodProxy period) {
		//
		periodDataProvider.getList().add(period);
		periodDataProvider.flush();
	}

	/*
	 * */
	@Override
	public void updatePeriod(PeriodProxy period) {
		//
		periodDataProvider.getList().set(selectedPeriodIndex, period);
		periodDataProvider.flush();
	}

	/*
	 * */
	@Override
	public void showPeriodData(List<PeriodProxy> periods) {
		//
		periodDataProvider.getList().clear();
		periodDataProvider.setList(periods);
		periodDataProvider.flush();
	}
	
	
	/*
	 * */
	@UiHandler("cmdAddPeriodItem")
	void onCmdAddPeriodItemClick(ClickEvent event) {
		//
		getUiHandlers().showPeriodItemPopup();
	}

	
	/*
	 * */
	@Override
	public void setPeriodListData(List<PeriodItemProxy> periods) {
		//
		lstPeriods.clear();
		lstPeriods.addItem("Choisir", "");
		for (PeriodItemProxy pp : periods) {
			lstPeriods.addItem( pp.getPeriod(), pp.getId().toString() );
		}
	}
	
	
	/*
	 * */
	@UiHandler("lstPeriods")
	void onLstPeriodsChange(ChangeEvent event) {
		//
		if (lstPeriods.getSelectedIndex() > 0)
			txtDescription.setText( lstPeriods.getItemText(lstPeriods.getSelectedIndex()) );
		else
			txtDescription.setText("");
	}
}
