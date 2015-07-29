package com.lemania.sis.client.view;

import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.lemania.sis.client.presenter.EcoleAddPresenter;
import com.lemania.sis.client.uihandler.EcoleAddUiHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimpleCheckBox;

public class EcoleAddView extends ViewWithUiHandlers<EcoleAddUiHandler> implements EcoleAddPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, EcoleAddView> {
	}

	@Inject
	public EcoleAddView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@UiField TextBox txtNom;
	@UiField TextBox txtAdresse;
	@UiField Button cmdAddEcole;
	@UiField Button cmdCancel;
	@UiField SimpleCheckBox chkActive;
	
	@UiHandler("cmdCancel")
	void onCmdCancelClicked(ClickEvent event){
		if (getUiHandlers() != null)
			getUiHandlers().ecoleAddCancel();
	}
	
	@UiHandler("cmdAddEcole")
	void onCmdAddEcoleClicked(ClickEvent event){
		if (getUiHandlers() != null)
			getUiHandlers().ecoleAdd(txtNom.getText(), txtAdresse.getText(), chkActive.getValue());
	}
}
