package com.lemania.sis.client.form.userprofile;

import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.lemania.sis.client.UI.FieldValidation;
import com.lemania.sis.client.values.NotificationValues;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class FrmPasswordView extends ViewWithUiHandlers<FrmPasswordUiHandler> 
		implements FrmPasswordPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, FrmPasswordView> {
	}

	@Inject
	public FrmPasswordView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@UiField Button cmdSave;
	@UiField TextBox txtCurrentPassword;
	@UiField TextBox txtNewPassword1;
	@UiField TextBox txtNewPassword2;
	
	@UiField TextButton cmdSaveUserName;
	@UiField TextField txtCurrentUserName;
	@UiField TextField txtNewUserName1;
	@UiField TextField txtNewUserName2;
	
	
	/*
	 * */
	@UiHandler("cmdSave")
	void onCmdSaveClick(ClickEvent event) {
		if (this.getUiHandlers() != null) {
			this.getUiHandlers().changePassword(txtCurrentPassword.getText(), txtNewPassword1.getText(), txtNewPassword2.getText());
		}
	}
	
	
	/*
	 * */
	@UiHandler("cmdSaveUserName")
	void onCmdSaveUserNameClick( SelectEvent event ) {
		//
		if ( txtCurrentUserName.isValid() && txtNewUserName1.isValid() && txtNewUserName2.isValid()
				&& (txtNewUserName1.getText().equals( txtNewUserName2.getText())) ) {
			//
			getUiHandlers().changeUserName( txtCurrentUserName.getText(), txtNewUserName1.getText() );
		} else {
			AlertMessageBox messageBox = new AlertMessageBox("Erreur", "Merci de vérifier les nouvelles adresses email.");
		    messageBox.show();
		}
	}
	

	/*
	 * */
	@Override
	public void initializeUI() {
		//
		txtNewUserName1.addValidator( new RegExValidator( FieldValidation.emailPattern, NotificationValues.invalid_input + "Nouvelle adresse email" ));
		txtNewUserName2.addValidator( new RegExValidator( FieldValidation.emailPattern, NotificationValues.invalid_input + "Confirmation de la nouvelle adresse email" ));
	}
}
