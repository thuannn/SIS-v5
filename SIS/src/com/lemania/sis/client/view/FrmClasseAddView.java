package com.lemania.sis.client.view;

import java.util.List;

import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.lemania.sis.client.presenter.FrmClasseAddPresenter;
import com.lemania.sis.client.uihandler.FrmClasseAddUiHandler;
import com.lemania.sis.shared.CoursProxy;
import com.lemania.sis.shared.EcoleProxy;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.ui.CheckBox;

public class FrmClasseAddView extends ViewWithUiHandlers<FrmClasseAddUiHandler> implements
		FrmClasseAddPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, FrmClasseAddView> {
	}

	@Inject
	public FrmClasseAddView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	@UiField ListBox lstEcoles;
	@UiField ListBox lstCoursList;
	@UiField TextBox txtClassName;
	@UiField Label lblStatus;
	@UiField Button cmdSave;
	@UiField CheckBox chkActive;
	
	@UiHandler("cmdSave")
	void onCmdSaveClick(ClickEvent event) {
		if ( getUiHandlers() != null )
			getUiHandlers().addNewClasse(txtClassName.getText(), lstCoursList.getValue( lstCoursList.getSelectedIndex() ), chkActive.getValue());
	}

	@Override
	public void setEcoleList(List<EcoleProxy> ecoleList) {
		// First clear existing data
		lstEcoles.clear(); 
		
		//
		lstEcoles.addItem("-", "");
		for ( EcoleProxy ecole : ecoleList )
			lstEcoles.addItem(ecole.getSchoolName(), ecole.getId().toString());
	}
	
	@UiHandler("lstEcoles")
	void onLstEcolesChange(ChangeEvent event) {
		// If user select the first item, which is null, clear the program list
		if (lstEcoles.getValue(lstEcoles.getSelectedIndex()).isEmpty()) {
			lstCoursList.clear();
			return;
		}
		
		// Otherwise, load the program list
		if (getUiHandlers() != null)
			getUiHandlers().onEcoleSelected( lstEcoles.getValue( lstEcoles.getSelectedIndex() ));
	}

	@Override
	public void setCoursList(List<CoursProxy> coursList) {
		// First clear existing data
		lstCoursList.clear();
		
		// 
		lstCoursList.addItem("-", "");
		for ( CoursProxy cours : coursList )
			lstCoursList.addItem( cours.getCoursNom(), cours.getId().toString() );
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
		lstEcoles.setSelectedIndex(0);
		lstCoursList.clear();
		txtClassName.setText("");
		chkActive.setValue(true);
	}
}
