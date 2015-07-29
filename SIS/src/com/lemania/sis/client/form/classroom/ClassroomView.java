package com.lemania.sis.client.form.classroom;

import java.util.List;

import javax.inject.Inject;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.classroom.ClassroomProxy;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.view.client.ListDataProvider;

public class ClassroomView extends ViewWithUiHandlers<ClassroomUiHandlers>
		implements ClassroomPresenter.MyView {
	interface Binder extends UiBinder<Widget, ClassroomView> {
	}

	@UiField(provided=true) CellTable<ClassroomProxy> cellTable = new CellTable<ClassroomProxy>();
	@UiField Button cmdAdd;
	@UiField TextBox txtClassroomName;
	@UiField IntegerBox txtCapacity;
	@UiField TextBox txtNote;
	@UiField CheckBox chkActif;
	
	@SuppressWarnings("unused")
	private int selectedClassroomIndex = -1;
	
	ListDataProvider<ClassroomProxy> classroomDataProvider = new ListDataProvider<ClassroomProxy>();

	@Inject
	ClassroomView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	/*
	 * */
	@UiHandler("cmdAdd")
	void onCmdAddClick(ClickEvent event) {
		//
		if (txtCapacity.getValue() == null) {
			Window.alert( NotificationValues.invalid_input + " - Capacité");
			return;
		}
		//
		getUiHandlers().addClassroom(txtClassroomName.getText(), txtCapacity.getValue(), txtNote.getText(), chkActif.getValue());
	}

	/*
	 * */
	@Override
	public void addClassroomToList(ClassroomProxy cp) {
		//
		classroomDataProvider.getList().add(cp);
		classroomDataProvider.refresh();
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
	    Column<ClassroomProxy, String> colName = new Column<ClassroomProxy, String>(new EditTextCell()) {
	      @Override
	      public String getValue(ClassroomProxy object) {
	        return object.getRoomName();
	      }
	    };
	    cellTable.addColumn(colName, "Nom");
	    colName.setFieldUpdater(new FieldUpdater<ClassroomProxy, String>(){
	    	@Override
	    	public void update(int index, ClassroomProxy classe, String value){
	    		//
	    		if (classe.getRoomName() != value) {
		    		if (getUiHandlers() != null) {	    			
		    			selectedClassroomIndex = index;
		    			getUiHandlers().updateClassroom( classe, value, classe.getRoomCapacity(), classe.getRoomNote(), classe.isActive() );
		    		}	    		
	    		}
	    	}
	    });
	    
	    Column<ClassroomProxy, String> colCapacity = new Column<ClassroomProxy, String>(new EditTextCell()) {
		      @Override
		      public String getValue(ClassroomProxy object) {
		        return Integer.toString( object.getRoomCapacity() );
		      }
		    };
		cellTable.addColumn(colCapacity, "Capacité");
		colCapacity.setFieldUpdater(new FieldUpdater<ClassroomProxy, String>(){
		    @Override
		    public void update(int index, ClassroomProxy classe, String value){
		    	//
		    	if (classe.getRoomCapacity() != Integer.parseInt(value)) {
			    	if (getUiHandlers() != null) {	    			
			    		selectedClassroomIndex = index;
			    		getUiHandlers().updateClassroom( classe, classe.getRoomName(), Integer.parseInt(value), classe.getRoomNote(), classe.isActive() );
			    	}	    
		    	}
		    }
		});
		    
		Column<ClassroomProxy, String> colNote = new Column<ClassroomProxy, String>(new EditTextCell()) {
		      @Override
		      public String getValue(ClassroomProxy object) {
		        return object.getRoomNote();
		      }
		};
		cellTable.addColumn(colNote, "Commentaire");
		colNote.setFieldUpdater(new FieldUpdater<ClassroomProxy, String>(){
			@Override
			public void update(int index, ClassroomProxy classe, String value) {
				//
				if (classe.getRoomNote() != value) {
				    if (getUiHandlers() != null) {	    			
				    	selectedClassroomIndex = index;
				    	getUiHandlers().updateClassroom( classe, classe.getRoomName(), classe.getRoomCapacity(), value, classe.isActive() );
				    }	    		
				}
			}
		});
	    
	    // Active
	    CheckboxCell cellActive = new CheckboxCell();
	    Column<ClassroomProxy, Boolean> colActive = new Column<ClassroomProxy, Boolean>(cellActive) {
	    	@Override
	    	public Boolean getValue(ClassroomProxy classe){
	    		return classe.isActive();
	    	}	    	
	    };
	    cellTable.addColumn(colActive, "Active");
	    colActive.setFieldUpdater(new FieldUpdater<ClassroomProxy, Boolean>(){
	    	@Override
	    	public void update(int index, ClassroomProxy classe, Boolean value){
	    		//
	    		if (getUiHandlers() != null) {
	    			selectedClassroomIndex = index;
	    			getUiHandlers().updateClassroom( classe, classe.getRoomName(), classe.getRoomCapacity(), classe.getRoomNote(), value );
	    		}	    		
	    	}
	    });
	    //
	    
	    classroomDataProvider.addDataDisplay(cellTable);
	}

	/*
	 * */
	@Override
	public void setClassroomTableData(List<ClassroomProxy> list) {
		//
		classroomDataProvider.getList().clear();
		classroomDataProvider.setList(list);
		classroomDataProvider.flush();
	}
}
