package com.lemania.sis.client.popup.parentprofile;

import java.util.List;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewWithUiHandlers;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.lemania.sis.client.UI.FieldValidation;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.client.values.TitleValues;
import com.lemania.sis.shared.parent.ParentProxy;
import com.lemania.sis.shared.student.StudentProxy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.CheckBox;

public class ParentProfileView extends
		PopupViewWithUiHandlers<ParentProfileUiHandlers> implements
		ParentProfilePresenter.MyView {
	
	public interface Binder extends UiBinder<PopupPanel, ParentProfileView> {
	}

	@Inject
	ParentProfileView(Binder uiBinder, EventBus eventBus) {
		super(eventBus);

		initWidget(uiBinder.createAndBindUi(this));
	}
	
	
	@UiField Button cmdClose;
	@UiField(provided=true) DataGrid<StudentProxy> tblStudents = new DataGrid<StudentProxy>();
	@UiField(provided=true) DataGrid<StudentProxy> tblChildren = new DataGrid<StudentProxy>();
	@UiField ListBox lstTitle;
	@UiField Button cmdSave;
	@UiField SimplePager pagerStudents;
	@UiField TextBox txtLastName;
	@UiField TextBox txtFirstName;
	@UiField TextBox txtEmail;
	@UiField TextBox txtMobile;
	@UiField TextBox txtPhoneHome;
	@UiField TextBox txtPhoneWork;
	@UiField CheckBox chkAcceptEmail;
	@UiField CheckBox chkAcceptSMS;
	@UiField Button cmdAdd;
	@UiField Button cmdRemove;
	
	
	//
	ListDataProvider<StudentProxy> providerStudents = new ListDataProvider<StudentProxy>(); 
	ListDataProvider<StudentProxy> providerChildren = new ListDataProvider<StudentProxy>();
	
	StudentProxy selectedStudent;
	StudentProxy selectedChild;
	
	
	/*
	 * */
	@UiHandler("cmdClose")
	void onCmdCloseClick(ClickEvent event) {
		//
		this.hide();
	}


	/*
	 * */
	@Override
	public void setStudentsData(List<StudentProxy> students) {
		//
		providerStudents.getList().clear();
		providerStudents.getList().addAll(students);
		providerStudents.flush();
	}


	/*
	 * */
	@Override
	public void initializeUI(boolean editExisting) {
		//
		initializeStudentTable();
		initializeChildrenTable();
		initializeList();
		//
		if (editExisting)
			txtEmail.setEnabled(false);
		else
			txtEmail.setEnabled(true);
	}
	
	
	/*
	 * */
	private void initializeList() {
		// 
		lstTitle.clear();
		lstTitle.addItem("Choisir");
		for (int i=0; i<TitleValues.titles.size(); i++)
			lstTitle.addItem( TitleValues.titles.get(i), TitleValues.titles.get(i));
	}


	/*
	 * */
	public void initializeStudentTable() {
		//
		// Add a text column to show the name.
		TextColumn<StudentProxy> colFirstName = new TextColumn<StudentProxy>() {
	      @Override
	      public String getValue(StudentProxy object) {
	        return object.getLastName();
	      }
	    };
	    tblStudents.addColumn(colFirstName, "Nom");
	    
		//
	    TextColumn<StudentProxy> colLastName = new TextColumn<StudentProxy>() {
	      @Override
	      public String getValue(StudentProxy object) {
	        return object.getFirstName();
	      } 
	    };
	    tblStudents.addColumn(colLastName, "Prénom");
	    
		// Add a selection model to handle user selection.
		final SingleSelectionModel<StudentProxy> selectionModel = new SingleSelectionModel<StudentProxy>();
		tblStudents.setSelectionModel(selectionModel);
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						selectedStudent = selectionModel.getSelectedObject();
					}
				});
	    
		//
		
		providerStudents.addDataDisplay( tblStudents );
		pagerStudents.setDisplay( tblStudents );
	}
	
	
	/*
	 * */
	public void initializeChildrenTable() {
		//
		// Add a text column to show the name.
		TextColumn<StudentProxy> colFirstName = new TextColumn<StudentProxy>() {
	      @Override
	      public String getValue(StudentProxy object) {
	        return object.getLastName();
	      }
	    };
	    tblChildren.addColumn(colFirstName, "Nom");
	    
		//
	    TextColumn<StudentProxy> colLastName = new TextColumn<StudentProxy>() {
	      @Override
	      public String getValue(StudentProxy object) {
	        return object.getFirstName();
	      } 
	    };
	    tblChildren.addColumn(colLastName, "Prénom");
	    
	    // Add a selection model to handle user selection.
		final SingleSelectionModel<StudentProxy> selectionModel = new SingleSelectionModel<StudentProxy>();
		tblChildren.setSelectionModel(selectionModel);
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						selectedChild = selectionModel.getSelectedObject();
					}
				});
	    
		//
		providerChildren.addDataDisplay( tblChildren );
	}
	
	
	/*
	 * */
	@UiHandler("cmdSave")
	void onCmdSaveClick(ClickEvent event) {
		//
		getUiHandlers().saveParent(
				lstTitle.getItemText(lstTitle.getSelectedIndex()), 
				txtFirstName.getText(), 
				txtLastName.getText(), 
				txtEmail.getText(), 
				txtMobile.getText(), 
				txtPhoneHome.getText(), 
				txtPhoneWork.getText(), 
				chkAcceptSMS.getValue(), 
				chkAcceptEmail.getValue(),
				providerChildren.getList() );
	}
	
	/*
	 * */
	@UiHandler("cmdAdd")
	void onCmdAddClick(ClickEvent event) {
		//
		if (selectedStudent == null) { Window.alert( NotificationValues.student_notselected ); return;}
		//
		if ( providerChildren.getList().indexOf(selectedStudent) < 0) {
			//
			providerChildren.getList().add(selectedStudent);
			providerChildren.flush();
		}
		//
		providerStudents.getList().remove( providerStudents.getList().indexOf(selectedStudent));
		providerStudents.flush();
	}
	
	/*
	 * */
	@UiHandler("cmdRemove")
	void onCmdRemoveClick(ClickEvent event) {
		//
		if (selectedChild == null) { Window.alert( NotificationValues.child_notselected ); return;}
		//
		if ( providerStudents.getList().indexOf(selectedChild) < 0) {
			//
			providerStudents.getList().add(selectedChild);
			providerStudents.flush();
		}
		//
		providerChildren.getList().remove( providerChildren.getList().indexOf(selectedChild));
		providerChildren.flush();
		
	}


	/*
	 * */
	@Override
	public void showParentDetails(ParentProxy parent) {
		//
		FieldValidation.selectItemByText(lstTitle, parent.getTitle());
		txtFirstName.setText( parent.getFirstName() ); 
		txtLastName.setText( parent.getLastName() );
		txtEmail.setText( parent.geteMail() );
		txtMobile.setText( parent.getPhoneMobile() );
		txtPhoneHome.setText( parent.getPhoneHome() );
		txtPhoneWork.setText( parent.getPhoneWork() );
		chkAcceptSMS.setValue( parent.isAcceptSMS() );
		chkAcceptEmail.setValue( parent.isAcceptEmail() );
	}


	/*
	 * */
	@Override
	public void showChildren(List<StudentProxy> children) {
		//
		providerChildren.getList().clear();
		providerChildren.getList().addAll( children );
		providerChildren.flush();
	}


	/*
	 * */
	@Override
	public void resetUI(boolean editExisting) {
		//
		lstTitle.setSelectedIndex(0);
		txtFirstName.setText( "" ); 
		txtLastName.setText( "" );
		txtEmail.setText( "" );
		txtMobile.setText( "" );
		txtPhoneHome.setText( "" );
		txtPhoneWork.setText( "" );
		chkAcceptSMS.setValue( false );
		chkAcceptEmail.setValue( false );
		//
		providerChildren.getList().clear();
		providerChildren.flush();
		//
		if (editExisting) txtEmail.setEnabled(false);
			else txtEmail.setEnabled(true);
	}
}
