package com.lemania.sis.client.form.studentmgt;

import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.lemania.sis.client.uihandler.StudentAddUiHandler;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.event.dom.client.KeyUpEvent;

public class StudentAddView extends ViewWithUiHandlers<StudentAddUiHandler> implements
		StudentAddPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, StudentAddView> {
	}

	@Inject
	public StudentAddView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	@UiField TextBox txtLastName;
	@UiField TextBox txtFirstName;
	@UiField TextBox txtEmail;
	@UiField CheckBox blnActive;
	@UiField TextButton cmdSave;
	@UiField Label lblStatus;
	
	@UiHandler("cmdSave")
	void onCmdSaveClick( SelectEvent event ) {
		if (this.getUiHandlers() != null){
			this.getUiHandlers().createStudent(txtFirstName.getText(), txtLastName.getText(), txtEmail.getText(), blnActive.getValue());
		}
	}
	
	@Override
	public void statusMessage(String msg) {
		lblStatus.setText(msg);
		resetForm();
	}

	@Override
	public void resetForm() {
		txtLastName.setText("");
		txtFirstName.setText("");
		txtEmail.setText("");
		blnActive.setValue(true);
	}
	
	/*
	 * */
	private void updateEmail() {
		//
		if (txtFirstName.getText().equals(""))
			txtEmail.setText( txtLastName.getText().trim().toLowerCase() + "@eprofil.ch");
		else
			if (txtLastName.getText().equals(""))
				txtEmail.setText( txtFirstName.getText().substring(0, 1).toLowerCase() + "@eprofil.ch");
			else
				txtEmail.setText( txtFirstName.getText().substring(0, 1).toLowerCase() + "." + txtLastName.getText().trim().toLowerCase() + "@eprofil.ch");
	}
	
	/*
	 * */
	@UiHandler("txtLastName")
	void onTxtLastNameKeyUp(KeyUpEvent event) {
		updateEmail();
	}
	
	/*
	 * */
	@UiHandler("txtFirstName")
	void onTxtFirstNameKeyUp(KeyUpEvent event) {
		updateEmail();
	}
}
