package com.lemania.sis.client.form.branches;

import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.CheckBox;

public class FrmBrancheAddView extends ViewWithUiHandlers<FrmBrancheAddUiHandler> implements
		FrmBrancheAddPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, FrmBrancheAddView> {
	}

	@Inject
	public FrmBrancheAddView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	@UiField Button cmdSave;
	@UiField TextBox txtBrancheName;
	@UiField TextBox txtBrancheCoef;
	@UiField Label lblStatus;
	@UiField CheckBox chkBrancheActive;
	
	@UiHandler("cmdSave")
	void onCmdSaveClick(ClickEvent event) {
		if (getUiHandlers() != null) {
			getUiHandlers().addNewBranche(txtBrancheName.getText(), txtBrancheCoef.getText(), chkBrancheActive.getValue());
		}
	}

	@Override
	public void showStatus(String msg) {
		// 
		lblStatus.setText( msg );
		
		Timer t = new Timer(){
			@Override
			public void run(){
				lblStatus.setText("");
			}
		};
		t.schedule(3000);
	}

	@Override
	public void resetForm() {
		// 
		txtBrancheName.setText("");
		txtBrancheCoef.setText("");
		chkBrancheActive.setValue(true);
	}
}