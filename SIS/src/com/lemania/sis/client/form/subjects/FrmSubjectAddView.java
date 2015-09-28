package com.lemania.sis.client.form.subjects;

import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class FrmSubjectAddView extends ViewWithUiHandlers<FrmSubjectAddUiHandler> 
	implements FrmSubjectAddPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, FrmSubjectAddView> {
	}

	@Inject
	public FrmSubjectAddView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	@UiField Label lblStatus;
	@UiField TextBox txtSubjectName;
	@UiField TextBox txtSubjectName2;
	@UiField TextBox txtDefaultCoef;
	@UiField CheckBox chkActive;
	@UiField TextButton cmdSave;
	
	@UiHandler("cmdSave")
	void onCmdSaveClick( SelectEvent event ) {
		if (getUiHandlers() != null){
			getUiHandlers().addNewSubject(
					txtSubjectName.getText(),
					txtSubjectName2.getText(),
					txtDefaultCoef.getText(), 
					chkActive.getValue());
		}
	}

	@Override
	public void resetForm() {
		//
		txtSubjectName.setText("");
		txtSubjectName2.setText("");
		txtDefaultCoef.setText("");
		chkActive.setValue(true);
	}

	@Override
	public void showStatus(String msg) {
		//
		lblStatus.setText(msg);
		
		Timer t = new Timer(){
			@Override
			public void run(){
				lblStatus.setText("");
			}
		};
		t.schedule(3000);
	}
}
