package com.lemania.sis.client.popup.absenceinput;

import java.util.Date;
import java.util.List;

import com.gwtplatform.mvp.client.PopupViewWithUiHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.inject.Inject;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.lemania.sis.client.values.AbsenceValues;
import com.lemania.sis.shared.absenceitem.AbsenceItemProxy;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectProxy;
import com.lemania.sis.shared.period.PeriodProxy;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.client.ui.DialogBox;

public class AbsenceInputView extends PopupViewWithUiHandlers<AbsenceInputUiHandlers> implements
		AbsenceInputPresenter.MyView {

	// private final Widget widget;
	
	//
	ListDataProvider<BulletinSubjectProxy> providerBulletins = new ListDataProvider<BulletinSubjectProxy>();
	ListDataProvider<PeriodProxy> providerPeriods = new ListDataProvider<PeriodProxy>();
	
	//
	int constantStudentNameCol = 0;
	int constantStudentNameRowStart = 1;
	
	int constantPeriodsColStart = 2;
	int constantPeriodRow = 0;
	
	int clickedCellIndex;
	int clickedRowIndex;
	

	public interface Binder extends UiBinder<Widget, AbsenceInputView> {
	}

	@Inject
	public AbsenceInputView(EventBus eventBus, Binder uiBinder) {
		super(eventBus);

		initWidget(uiBinder.createAndBindUi(this));
	}
	
	
	/*
	 * */
	@UiHandler("cmdClose")
	void onCmdCloseClick(ClickEvent event) {
		//
		getUiHandlers().closePopup();
	}
	
	
	@UiField FlexTable tblAbsences;
	@UiField RadioButton optAbsent;
	@UiField RadioButton optExclude;
	@UiField RadioButton optLate;
	@UiField RadioButton optHealth;
	@UiField Label lblStudentName;
	@UiField DateBox dateAbsence;
	@UiField DialogBox dialog;
	
	
	/*
	 * */
	@Override
	public void setStudentListData(List<BulletinSubjectProxy> bulletinSubjects) {
		//
		providerBulletins.getList().clear();
		providerBulletins.getList().addAll(bulletinSubjects);
		//
		BulletinSubjectProxy bsp;
		tblAbsences.setText(0, 0, "");
		for ( int row = 0; row < bulletinSubjects.size(); row++ ) {
			bsp = bulletinSubjects.get( row );
			tblAbsences.setText( row + this.constantStudentNameRowStart, 0, bsp.getSubjectName() );
			tblAbsences.setText( row + this.constantStudentNameRowStart, 1, bsp.getProfName() );
		}
	}
	
	
	/*
	 * */
	@Override
	public void setPeriodListData(List<PeriodProxy> periods) {
		//
		providerPeriods.getList().clear();
		providerPeriods.getList().addAll(periods);
		//
		for (int i=0; i < providerPeriods.getList().size(); i++) {
			tblAbsences.setText( this.constantPeriodRow, this.constantPeriodsColStart + i, providerPeriods.getList().get(i).getDescription() );
		}
		//
		styleTable();
		//
		populateAbsenceInputUI();
	}
	
	
	/*
	 * */
	private void populateAbsenceInputUI() {
		//
		if ( optAbsent.getValue() || optExclude.getValue() || optHealth.getValue() )
			populateAbsenceInput();
		if ( optLate.getValue() )
			populateLateInput();
	}
	
	
	/*
	 * */
	private void populateLateInput() {
		//
		clearAbsenceInputUI();
		//
		VerticalPanel vp;
		TextBox tb;
		//
		// Loop through the cells and add a Vertical Panel with CheckBox child
		for ( int row = this.constantStudentNameRowStart; row < tblAbsences.getRowCount(); row++ ) {
			for ( int col = this.constantPeriodsColStart; col < tblAbsences.getCellCount(0); col++ ) {
				vp = new VerticalPanel();
				tb = new TextBox();
				tb.setStyleName("textboxLateInput");
				vp.add( tb );
				tblAbsences.setWidget(row, col, vp);
				//
				tb.addValueChangeHandler( new ValueChangeHandler<String>() {

					@Override
					public void onValueChange(ValueChangeEvent<String> event) {
						//
						TextBox tbox = (TextBox) event.getSource();
						VerticalPanel vpanel = (VerticalPanel) tbox.getParent();
						Label lID;
						String strMinutes = tbox.getText().trim();
						// get the indexes of the clicked cell
						setCellSelectedIndex( vpanel );		
						// if user select this item
						if ( !strMinutes.equals("") ) {
							// if there is no id label, add this absence item
							if ( vpanel.getWidgetCount() < 2 ) {			
								//
								BulletinSubjectProxy bulletinSubject = providerBulletins.getList().get(clickedRowIndex - constantStudentNameRowStart );
								PeriodProxy pp = providerPeriods.getList().get(clickedCellIndex - constantPeriodsColStart );
								getUiHandlers().saveAbsenceItem(
										DateTimeFormat.getFormat("yyyyMMdd").format( dateAbsence.getValue() ),
										bulletinSubject.getStudentId(),
										pp.getId().toString(),
										bulletinSubject.getProfId(),
										bulletinSubject.getClassId(),
										bulletinSubject.getSubjectId(),
										"",
										getSelectedAbsenceTypeCode(),
										"",
										Integer.parseInt(strMinutes),
										false,
										false );
							}				
						} else {		// otherwise remove this absence item
							if ( vpanel.getWidgetCount() > 1 ) {			
								lID = (Label)vpanel.getWidget(1);
								getUiHandlers().removeAbsenceItem( lID.getText() );
							}
						}
					}
					
				});
			}
		}
	}
	

	/*
	 * */
	protected void setCellSelectedIndex(Widget widget) {
		//
		for ( int row = this.constantStudentNameRowStart; row < tblAbsences.getRowCount(); row++ ) {
			for ( int col = this.constantPeriodsColStart; col < tblAbsences.getCellCount(0); col++ ) {
				if ( (tblAbsences.getWidget(row, col) != null) && tblAbsences.getWidget(row, col).equals(widget) ) {
					clickedCellIndex = col;
					clickedRowIndex = row;
				}
			}
		}
	}

	/*
	 * */
	private void populateAbsenceInput() {
		//
		clearAbsenceInputUI();
		//
		VerticalPanel vp;
		CheckBox cb;
		//
		// Loop through the cells and add a Vertical Panel with Checkbox child
		for ( int row = this.constantStudentNameRowStart; row < tblAbsences.getRowCount(); row++ ) {
			for ( int col = this.constantPeriodsColStart; col < tblAbsences.getCellCount(0); col++ ) {
				vp = new VerticalPanel();
				cb = new CheckBox();
				vp.add( cb );
				tblAbsences.setWidget(row, col, vp);
				//
				cb.addClickHandler( new ClickHandler(){

					@Override
					public void onClick(ClickEvent event) {
						//
						CheckBox cbox = (CheckBox) event.getSource();
						VerticalPanel vpanel = (VerticalPanel) cbox.getParent();
						Label lID;
						// 
						// get the indexes of the clicked cell
						setCellSelectedIndex( vpanel );		
						// 
						// if user select this item
						if ( cbox.getValue() ) {
							// if there is no id label, add this absence item
							if ( vpanel.getWidgetCount() < 2 ) {			
								//
								BulletinSubjectProxy bulletinSubject = providerBulletins.getList().get(clickedRowIndex - constantStudentNameRowStart );
								PeriodProxy pp = providerPeriods.getList().get(clickedCellIndex - constantPeriodsColStart );
								getUiHandlers().saveAbsenceItem(
										DateTimeFormat.getFormat("yyyyMMdd").format( dateAbsence.getValue() ),
										bulletinSubject.getStudentId(),
										pp.getId().toString(),
										bulletinSubject.getProfId(),
										bulletinSubject.getClassId(),
										bulletinSubject.getSubjectId(),
										"",
										getSelectedAbsenceTypeCode(),
										"",
										-1,
										false,
										false );
							}				
						} else {		// otherwise remove this absence item
							if ( vpanel.getWidgetCount() > 1 ) {			
								lID = (Label)vpanel.getWidget(1);
								getUiHandlers().removeAbsenceItem( lID.getText() );
							}
						}
					}
					
				});
			}
		}
	}

	
	
	/*
	 * */
	public String getSelectedAbsenceTypeCode(){
		//
		if (optAbsent.getValue())
			return AbsenceValues.absenceType_Absence_Code;
		if (optLate.getValue())
			return AbsenceValues.absenceType_Late_Code;
		if (optExclude.getValue())
			return AbsenceValues.absenceType_Exclude_Code;
		if (optHealth.getValue())
			return AbsenceValues.absenceType_Health_Code;
		return AbsenceValues.absenceType_Absence_Code;
	}
	
	
	
	/*
	 * */
	private void clearAbsenceInputUI() {
		//
		// Loop through and remove all the widget
		for ( int row = this.constantStudentNameRowStart; row < tblAbsences.getRowCount(); row++ ) {
			for ( int col = this.constantPeriodsColStart; col < tblAbsences.getCellCount(0); col++ ) {
				if ( tblAbsences.getWidget(row, col) != null )
					((VerticalPanel) tblAbsences.getWidget(row, col)).removeFromParent();
			}
		}
	}

	/*
	 * */
	public void styleTable(){
		// Add empty cells
		for (int i= this.constantStudentNameRowStart; i<tblAbsences.getRowCount(); i++) {
			for (int j= this.constantPeriodsColStart; j<tblAbsences.getCellCount(0); j++) {
				tblAbsences.setText(i, j, "");
			}
		}
		
		// Format normal cells
		for (int i=0; i<tblAbsences.getRowCount(); i++) {
			for (int j=0; j<tblAbsences.getCellCount(0); j++) {
				if (tblAbsences.isCellPresent(i, j))
					tblAbsences.getCellFormatter().setStyleName(i, j, "agendaNormal");
			}
		}
		
		// Period cells
		for (int j=0; j<tblAbsences.getCellCount(0); j++) {
			if (tblAbsences.isCellPresent( 0, j))
				tblAbsences.getCellFormatter().setStyleName( 0, j, "agendaTitle");
		}
		
		// Student name cells
		for (int i=0; i<tblAbsences.getRowCount(); i++)
			if (tblAbsences.isCellPresent(i, 0))
				tblAbsences.getCellFormatter().setStyleName(i, 0, "agendaTitle");
		
	}

	
	/*
	 * */
	@Override
	public void resetUI(String studentName) {
		//
		this.setPosition( 200, 50 );
		//
		lblStudentName.setText( studentName );
		//
		dateAbsence.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy")));
		dateAbsence.setValue( new Date() );
	}
	
	
	/*
	 * */
	@UiHandler("optAbsent")
	void onOptAbsentClick(ClickEvent event) {
		//
		populateAbsenceInputUI();
	}
	
	/*
	 * */
	@UiHandler("optExclude")
	void onOptExcludeClick(ClickEvent event) {
		//
		populateAbsenceInputUI();
	}
	
	/*
	 * */
	@UiHandler("optLate")
	void onOptLateClick(ClickEvent event) {
		//
		populateAbsenceInputUI();
	}
	
	/*
	 * */
	@UiHandler("optHealth")
	void onOptHealthClick(ClickEvent event) {
		//
		populateAbsenceInputUI();
	}

	
	/*
	 * */
	@Override
	public void setAddedAbsenceItem(AbsenceItemProxy aip) {
		//
		Label id;
		if ( tblAbsences.getWidget(clickedRowIndex, clickedCellIndex) != null ) {
			id = new Label(aip.getId().toString());
			id.setVisible( false );
			((VerticalPanel) tblAbsences.getWidget(clickedRowIndex, clickedCellIndex)).add( id );
		}
	}
	
	
	/*
	 * */
	@Override
	public void removeDeletedAbsenceItemId() {
		//
		if ( tblAbsences.getWidget(clickedRowIndex, clickedCellIndex) != null ) {
			((VerticalPanel) tblAbsences.getWidget(clickedRowIndex, clickedCellIndex)).getWidget(1).removeFromParent();
		}
	}
}
