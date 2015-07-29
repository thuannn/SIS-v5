package com.lemania.sis.client.form.motifabsence;

import java.util.List;

import javax.inject.Inject;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.lemania.sis.shared.motifabsence.MotifAbsenceProxy;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;

public class MotifAbsenceView extends
		ViewWithUiHandlers<MotifAbsenceUiHandlers> implements
		MotifAbsencePresenter.MyView {
	interface Binder extends UiBinder<Widget, MotifAbsenceView> {
	}


	@Inject
	MotifAbsenceView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}
	@UiField(provided=true) DataGrid<MotifAbsenceProxy> tblMotifs = new DataGrid<MotifAbsenceProxy>();
	@UiField SimplePager pagerMotifs;
	@UiField TextBox txtCode;
	@UiField TextBox txtLabel;
	@UiField CheckBox chkReceivable;
	@UiField CheckBox chkOutside;
	@UiField CheckBox chkHealth;
	@UiField CheckBox chkDispense;
	@UiField TextArea txtLetter;
	@UiField TextArea txtSMS;
	@UiField Button cmdSave;
	@UiField Button cmdAdd;
	
	
	//
	ListDataProvider<MotifAbsenceProxy> providerMotifs = new ListDataProvider<MotifAbsenceProxy>();
	MotifAbsenceProxy selectedMotif;
	int selectedMotifIndex = -1;
	
	
	/*
	 * */
	@Override
	public void initializeUI() {
		//
		initializeTable();
	}
	
	
	/*
	 * */
	public void initializeTable() {
		//
		// Code
	    TextColumn<MotifAbsenceProxy> colCode = new TextColumn<MotifAbsenceProxy>() {
	      @Override
	      public String getValue(MotifAbsenceProxy object) {
	        return object.getMotifCode();
	      } 
	    };
	    tblMotifs.setColumnWidth(colCode, 10, Unit.PCT);
	    tblMotifs.addColumn(colCode, "Code");
	    
		// Label
	    TextColumn<MotifAbsenceProxy> colLabel = new TextColumn<MotifAbsenceProxy>() {
	      @Override
	      public String getValue(MotifAbsenceProxy object) {
	        return object.getMotifLabel();
	      } 
	    };
	    tblMotifs.addColumn(colLabel, "Libellé");
	    
		// Letter
		TextColumn<MotifAbsenceProxy> colLetter = new TextColumn<MotifAbsenceProxy>() {
			@Override
			public String getValue(MotifAbsenceProxy object) {
					return (object.getTextLetter().length() < 20) ? object.getTextLetter() : object.getTextLetter().substring(0, 20) + "..."; 
			}
	    };
	    tblMotifs.setColumnWidth(colLetter, 20, Unit.PCT);
	    tblMotifs.addColumn(colLetter, "Texte lettre");
	    
		// SMS
		TextColumn<MotifAbsenceProxy> colSMS = new TextColumn<MotifAbsenceProxy>() {
			@Override
			public String getValue(MotifAbsenceProxy object) {
				return (object.getTextSMS().length() < 20) ? object.getTextSMS() : object.getTextSMS().substring(0, 20) + "..."; 
			}
		};
		tblMotifs.setColumnWidth(colSMS, 20, Unit.PCT);
		tblMotifs.addColumn(colSMS, "Texte SMS");
		
		// Receivable
		TextColumn<MotifAbsenceProxy> colReceivable = new TextColumn<MotifAbsenceProxy>() {
			@Override
			public String getValue(MotifAbsenceProxy object) {
				return object.isReceivable()? "Oui" : "Non";
			}
		};
		tblMotifs.setColumnWidth(colReceivable, 5, Unit.PCT);
		tblMotifs.addColumn(colReceivable, "Re");
		
		// Outside
		TextColumn<MotifAbsenceProxy> colOutside = new TextColumn<MotifAbsenceProxy>() {
			@Override
			public String getValue(MotifAbsenceProxy object) {
				return object.isOutside() ? "Oui" : "Non";
			}
		};
		tblMotifs.setColumnWidth(colOutside, 5, Unit.PCT);
		tblMotifs.addColumn(colOutside, "HE");
		
		// Health
		TextColumn<MotifAbsenceProxy> colHealth = new TextColumn<MotifAbsenceProxy>() {
			@Override
			public String getValue(MotifAbsenceProxy object) {
				return object.isHealth() ? "Oui" : "Non";
			}
		};
		tblMotifs.setColumnWidth(colHealth, 5, Unit.PCT);
		tblMotifs.addColumn(colHealth, "Sa");
		
		// Dispensable
		TextColumn<MotifAbsenceProxy> colDispensable = new TextColumn<MotifAbsenceProxy>() {
			@Override
			public String getValue(MotifAbsenceProxy object) {
				return object.isDispensable() ? "Oui" : "Non";
			}
		};
		tblMotifs.setColumnWidth(colDispensable, 5, Unit.PCT);
		tblMotifs.addColumn(colDispensable, "Di");

		
		// Add a selection model to handle user selection.
		final SingleSelectionModel<MotifAbsenceProxy> selectionModel = new SingleSelectionModel<MotifAbsenceProxy>();
		tblMotifs.setSelectionModel(selectionModel);
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						selectedMotif = selectionModel.getSelectedObject();
						selectedMotifIndex = providerMotifs.getList().indexOf( selectedMotif );
						showMotifDetails( selectedMotif );
					}
				});
	    
		//
		
		providerMotifs.addDataDisplay( tblMotifs );
		pagerMotifs.setDisplay( tblMotifs );
	}
	
	
	/*
	 * */
	private void showMotifDetails(
			MotifAbsenceProxy sm) {
		//
		txtCode.setText( sm.getMotifCode() );
		txtLabel.setText( sm.getMotifLabel() );
		chkReceivable.setValue( sm.isReceivable() );
		chkOutside.setValue( sm.isOutside() );
		chkHealth.setValue( sm.isHealth() );
		chkDispense.setValue( sm.isDispensable() );
		txtLetter.setText( sm.getTextLetter() );
		txtSMS.setText( sm.getTextSMS() );
	}


	/*
	 * */
	@Override
	public void setMotifListData(List<MotifAbsenceProxy> motifs) {
		//
		providerMotifs.getList().clear();
		providerMotifs.getList().addAll(motifs);
		providerMotifs.flush();
	}
	
	
	/*
	 * */
	@UiHandler("cmdSave")
	void onCmdSaveClick(ClickEvent event) {
		//
		getUiHandlers().saveMotifs(
				true,
				selectedMotif,
				txtCode.getText(), 
				txtLabel.getText(), 
				chkReceivable.getValue(), 
				chkOutside.getValue(), 
				chkHealth.getValue(), 
				chkDispense.getValue(), 
				txtLetter.getText(), 
				txtSMS.getText() );
	}
	
	
	/*
	 * */
	@UiHandler("cmdAdd")
	void onCmdAddClick(ClickEvent event) {
		//
		getUiHandlers().saveMotifs(
				false,
				null,
				txtCode.getText(), 
				txtLabel.getText(), 
				chkReceivable.getValue(), 
				chkOutside.getValue(), 
				chkHealth.getValue(), 
				chkDispense.getValue(), 
				txtLetter.getText(), 
				txtSMS.getText() );
	}


	/*
	 * */
	@Override
	public void addNewMotif(boolean isEditing, MotifAbsenceProxy newMotif) {
		//
		if (isEditing)
			providerMotifs.getList().set(selectedMotifIndex, newMotif);
		else
			providerMotifs.getList().add(newMotif);
		providerMotifs.flush();
	}
}
