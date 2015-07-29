package com.lemania.sis.client.view;

import java.util.List;

import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.lemania.sis.client.presenter.CoursAddPresenter;
import com.lemania.sis.client.uihandler.CoursAddUiHandler;
import com.lemania.sis.shared.EcoleProxy;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ListBox;

public class CoursAddView extends ViewWithUiHandlers<CoursAddUiHandler> implements CoursAddPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, CoursAddView> {
	}

	@Inject
	public CoursAddView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	@UiField Button cmdAnnuler;
	@UiField Button cmdAjouter;
	@UiField TextBox txtCoursNom;
	@UiField CheckBox chkActif;
	@UiField ListBox lstEcoles;
	
	@UiHandler("cmdAnnuler")
	void onCmdAnnulerClicked(ClickEvent event){
		if (getUiHandlers() != null)
			getUiHandlers().coursAddCancel();
	}
	
	@UiHandler("cmdAjouter")
	void onCmdAjouterClicked(ClickEvent event){
		if (getUiHandlers() != null)
			getUiHandlers().coursAdd(
					txtCoursNom.getText().trim(), 
					lstEcoles.getValue(lstEcoles.getSelectedIndex()), 
					chkActif.getValue());
	}

	@Override
	public void populateEcoleList(List<EcoleProxy> ecoles){
		// Thuan: populate the list of school names
		lstEcoles.clear();
		
		lstEcoles.addItem("-","");
		for (int i=0; i<ecoles.size(); i++)
			lstEcoles.addItem(ecoles.get(i).getSchoolName(), ecoles.get(i).getId().toString());
	}
}
