package com.lemania.sis.client.view;

import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.lemania.sis.client.presenter.ContactPresenter;
import com.lemania.sis.client.uihandler.ContactUiHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class ContactView extends ViewWithUiHandlers<ContactUiHandler> implements ContactPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, ContactView> {
	}

	@Inject
	public ContactView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@UiField TextBox txtLastName;
	@UiField TextBox txtFirstName;
	@UiField TextArea txtMessage;
	@UiField Button cmdSend;
	@UiField TextBox txtEmail;
	
	@UiHandler("cmdSend")
	void onCmdSendClick(ClickEvent event) {
		if (getUiHandlers() != null)
			getUiHandlers().sendMessage( txtLastName.getText(), txtFirstName.getText(), txtEmail.getText(), txtMessage.getText());
	}

	@Override
	public void resetForm() {
		//
		txtLastName.setText("");
		txtFirstName.setText("");
		txtMessage.setText("");
		txtEmail.setText("");
	}
}
