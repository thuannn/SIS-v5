package com.lemania.sis.client.form.attendancelist;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lemania.sis.client.CurrentUser;
import com.lemania.sis.client.UI.FieldValidation;
import com.lemania.sis.client.values.AbsenceValues;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.ProfessorProxy;
import com.lemania.sis.shared.absenceitem.AbsenceItemProxy;
import com.lemania.sis.shared.assignment.AssignmentProxy;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectProxy;
import com.lemania.sis.shared.period.PeriodProxy;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.datepicker.client.DateBox;

class AttendanceListView extends ViewWithUiHandlers<AttendanceListUiHandlers>
		implements AttendanceListPresenter.MyView {
	interface Binder extends UiBinder<Widget, AttendanceListView> {
	}

	@Inject
	AttendanceListView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiField FlexTable tblAttendance;
	@UiField ListBox lstProfs;
	@UiField ListBox lstAssignments;
	@UiField RadioButton optAbsent;
	@UiField RadioButton optLate;
	@UiField RadioButton optExclude;
	@UiField RadioButton optHealth;
	@UiField Label lblTitle;
	@UiField DateBox dtAbsenceDate;
	
	//
	int constantStudentNameCol = 0;
	int constantStudentNameRowStart = 1;
	
	int constantPeriodsColStart = 1;
	int constantPeriodRow = 0;
	
	int clickedCellIndex;
	int clickedRowIndex;
	
	Date prevSelectedDate = new Date();
	
	//
	ListDataProvider<AssignmentProxy> providerAssignments = new ListDataProvider<AssignmentProxy>();
	ListDataProvider<BulletinSubjectProxy> providerBulletins = new ListDataProvider<BulletinSubjectProxy>();
	ListDataProvider<PeriodProxy> providerPeriods = new ListDataProvider<PeriodProxy>();
	ListDataProvider<AbsenceItemProxy> providerAbsenceItems = new ListDataProvider<AbsenceItemProxy>();
	
	
	/*
	 * */
	@Override
	public void setProfListData(List<ProfessorProxy> profs) {
		//
		lstProfs.clear();
		lstProfs.addItem("-","");
		for (ProfessorProxy prof : profs)
			lstProfs.addItem(prof.getProfName(), prof.getId().toString());
	}
	
	
	/*
	 * */
	@UiHandler("lstProfs")
	void onLstProfsChange(ChangeEvent event) {
		//
		if (getUiHandlers() != null)
			getUiHandlers().onProfessorSelected(lstProfs.getValue(lstProfs.getSelectedIndex()));
	}
	
	
	/*
	 * */
	@Override
	public void setAssignmentTableData(List<AssignmentProxy> assignments) {
		//
		providerAssignments.getList().clear();
		providerAssignments.getList().addAll(assignments);
		//
		lstAssignments.clear();
		lstAssignments.addItem("-","");
		for ( AssignmentProxy assignment : providerAssignments.getList() ){
			lstAssignments.addItem( 
					assignment.getProgrammeName() + " - "
							+ assignment.getClasseName() + " - "  
							+ assignment.getSubjectName(), 
				assignment.getId().toString());
		}
	}
	
	
	/*
	 * */
	@UiHandler("lstAssignments")
	void onLstAssignmentsChange(ChangeEvent event) {
		//
		if (getUiHandlers() != null)
			getUiHandlers().onAssignmentSelected( providerAssignments.getList().get( lstAssignments.getSelectedIndex() - 1 ),
					DateTimeFormat.getFormat("yyyyMMdd").format(dtAbsenceDate.getValue()) );
	}


	/*
	 * */
	@Override
	public void setStudentListData(List<BulletinSubjectProxy> bulletins) {
		//
		providerBulletins.getList().clear();
		providerBulletins.getList().addAll(bulletins);
		//
		tblAttendance.removeAllRows();
		for (int i=0; i < providerBulletins.getList().size(); i++) {
			tblAttendance.setText( constantStudentNameRowStart + i, this.constantStudentNameCol, providerBulletins.getList().get(i).getStudentName() );
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
			tblAttendance.setText( this.constantPeriodRow, this.constantPeriodsColStart + i, providerPeriods.getList().get(i).getDescription() );
		}
		//
		styleTable();
	}
	
	
	/*
	 * */
	public void styleTable(){
		// Add the Remarque column
		tblAttendance.setText(0, tblAttendance.getCellCount(0), "Remarque");
		//
		// 2014-10-28 : Add admin remarque
		tblAttendance.setText(0, tblAttendance.getCellCount(0), "Remarque Admin");

		// Add empty cells
		for (int i= this.constantStudentNameRowStart; i<tblAttendance.getRowCount(); i++) {
			for (int j= this.constantPeriodsColStart; j<tblAttendance.getCellCount(0); j++) {
				tblAttendance.setText(i, j, "");
			}
		}
		
		// Format normal cells
		for (int i=0; i<tblAttendance.getRowCount(); i++) {
			for (int j=0; j<tblAttendance.getCellCount(0); j++) {
				if (tblAttendance.isCellPresent(i, j))
					tblAttendance.getCellFormatter().setStyleName(i, j, "agendaNormal");
			}
		}
		
		// Period cells
		for (int j=0; j<tblAttendance.getCellCount(0); j++) {
			if (tblAttendance.isCellPresent( 0, j))
				tblAttendance.getCellFormatter().setStyleName( 0, j, "agendaTitle");
		}
		
		// Student name cells
		for (int i=0; i<tblAttendance.getRowCount(); i++)
			if (tblAttendance.isCellPresent(i, 0))
				tblAttendance.getCellFormatter().setStyleName(i, 0, "agendaTitle");
		
		//
		tblAttendance.getElement().getStyle().setVerticalAlign( VerticalAlign.BOTTOM );
	}


	
	/*
	 * */
	@Override
	public void initializeUI() {
		//
		tblAttendance.addClickHandler(new ClickHandler(){
			
			@Override
			public void onClick(ClickEvent event) {
				//
				clickedCellIndex = tblAttendance.getCellForEvent(event).getCellIndex();
				clickedRowIndex = tblAttendance.getCellForEvent(event).getRowIndex();
			}
			
		});
		//
		dtAbsenceDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy")));
		dtAbsenceDate.addValueChangeHandler( new ValueChangeHandler<Date>(){
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				//
				if ( !dtAbsenceDate.equals( prevSelectedDate ) ) { 
					onLstAssignmentsChange( null );
					prevSelectedDate = dtAbsenceDate.getValue();
				}
			}
		});
	}
	
	
	
	/*
	 * */
	@UiHandler("optAbsent")
	void onOptAbsentClick(ClickEvent event) {
		//
		lblTitle.setText("Saisir des absences");
		//
		clearTableWidgets();
		//
		prepareAbsentWidget();
		//
		showCurrentAbsenceItems();
	}
	
	
	/*
	 * */
	void clearTableWidgets() {
		//
		for ( int i= this.constantStudentNameRowStart; i < tblAttendance.getRowCount(); i++ ) {
			for ( int j= this.constantPeriodsColStart; j < tblAttendance.getCellCount(0); j++ ) {
				if ( tblAttendance.isCellPresent(i, j) ) {
					if ( tblAttendance.getWidget(i, j) != null)
						tblAttendance.getWidget(i, j).removeFromParent();
				}
			}
		}
	}
	
	
	/*
	 * */
	void prepareAbsentWidget() {
		//
		CheckBox chkAbsent;
		VerticalPanel pnlAbsenceCell;
		int indexRemarqueCol = tblAttendance.getCellCount(0) - 2;
		//
		for ( int i= this.constantStudentNameRowStart; i < tblAttendance.getRowCount(); i++ ) {
			for ( int j= this.constantPeriodsColStart; j < indexRemarqueCol; j++ ) {   	// don't forget to avoid the Remarque column
				if ( tblAttendance.isCellPresent(i, j) ) {
					//
					pnlAbsenceCell = new VerticalPanel();
					chkAbsent = new CheckBox();
					pnlAbsenceCell.add( chkAbsent );
					//
					chkAbsent.addClickHandler(new ClickHandler(){

						@Override
						public void onClick(ClickEvent event) {
							//
							getWidgetIndex( (VerticalPanel)((CheckBox)event.getSource()).getParent() , tblAttendance );
							//
							// If user check the box
							if ( ((CheckBox)event.getSource()).getValue() ) {
								//
								// If there exists a comment linked to an item, no need to add
								String strRemarque = "";
								if ( ((VerticalPanel) tblAttendance.getWidget( clickedRowIndex, tblAttendance.getCellCount(clickedRowIndex) - 2 )).getWidgetCount() < 2 )
									strRemarque = ((TextBox)((VerticalPanel) tblAttendance.getWidget( clickedRowIndex, tblAttendance.getCellCount(clickedRowIndex) - 2 )).getWidget(0)).getText();
								//
								BulletinSubjectProxy bulletinSubject = providerBulletins.getList().get(clickedRowIndex - constantStudentNameRowStart );
								PeriodProxy pp = providerPeriods.getList().get(clickedCellIndex - constantPeriodsColStart );
								getUiHandlers().saveAbsenceItem(
										DateTimeFormat.getFormat("yyyyMMdd").format(dtAbsenceDate.getValue()),
										bulletinSubject.getStudentId(),
										pp.getId().toString(),
										bulletinSubject.getProfId(),
										bulletinSubject.getClassId(),
										bulletinSubject.getSubjectId(),
										"",
										getSelectedAbsenceTypeCode(),
										strRemarque,
										-1,
										false,
										false );
							} else {
								//
								if ( tblAttendance.getWidget(clickedRowIndex, clickedCellIndex) != null ) {
									getUiHandlers().removeAbsenceItem( ((Label)((VerticalPanel)((CheckBox)event.getSource()).getParent()).getWidget(1)).getText() );
								}
							}
						}
						
					});
					//
					tblAttendance.setWidget(i, j, pnlAbsenceCell);
				}
			}
		}
		
		//
		// Remarque column
		addRemarqueColumn( indexRemarqueCol );
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
	private boolean getWidgetIndex(Widget widget, FlexTable table) {
		for (int row = 0; row < table.getRowCount(); row++) {
			for (int col = 0; col < table.getCellCount(row); col++) {
					Widget w = table.getWidget(row, col);
					if (w == widget) {
						clickedCellIndex = col;
						clickedRowIndex = row;
						return true;
					}
			}
		}
		return false;
	}
	
	
	/*
	 * */
	void prepareLateWidget() {
		//
		VerticalPanel pnlAbsenceCell;
		TextBox txtMinutes;
		int indexRemarqueCol = tblAttendance.getCellCount(0) - 2;
		//
		for ( int i= this.constantStudentNameRowStart; i < tblAttendance.getRowCount(); i++ ) {
			for ( int j= this.constantPeriodsColStart; j < indexRemarqueCol; j++ ) {   	// don't forget the Remarque column
				if ( tblAttendance.isCellPresent(i, j) ) {
					//
					pnlAbsenceCell = new VerticalPanel();
					txtMinutes = new TextBox();
					txtMinutes.setWidth("20px");
					pnlAbsenceCell.add( txtMinutes );
					//
					txtMinutes.addValueChangeHandler(new ValueChangeHandler<String>(){

						@Override
						public void onValueChange(ValueChangeEvent<String> event) {
							//
							getWidgetIndex( (VerticalPanel)((TextBox)event.getSource()).getParent() , tblAttendance );
							//
							String strMinutes = ((TextBox)event.getSource()).getText().trim();
							if ( !strMinutes.equals("") ) {
								if ( ! FieldValidation.isNumeric( strMinutes ) ) {
									Window.alert( NotificationValues.invalid_input + " - Minutes" );
									return;
								}
								//
								// If this is a new absence item (no ID found) ...
								if ( ((VerticalPanel)((TextBox)event.getSource()).getParent()).getWidgetCount() < 2 ) {
									//
									// If there exists a comment linked to an item, no need to add
									String strRemarque = "";
									if ( ((VerticalPanel) tblAttendance.getWidget( clickedRowIndex, tblAttendance.getCellCount(clickedRowIndex)-2 )).getWidgetCount() < 2 )
										strRemarque = ((TextBox)((VerticalPanel) tblAttendance.getWidget( clickedRowIndex, tblAttendance.getCellCount(clickedRowIndex)-2 )).getWidget(0)).getText();
									//
									BulletinSubjectProxy bulletinSubject = providerBulletins.getList().get(clickedRowIndex - constantStudentNameRowStart );
									PeriodProxy pp = providerPeriods.getList().get(clickedCellIndex - constantPeriodsColStart );
									getUiHandlers().saveAbsenceItem(
											DateTimeFormat.getFormat("yyyyMMdd").format(dtAbsenceDate.getValue()),
											bulletinSubject.getStudentId(),
											pp.getId().toString(),
											bulletinSubject.getProfId(),
											bulletinSubject.getClassId(),
											bulletinSubject.getSubjectId(),
											"",
											getSelectedAbsenceTypeCode(),
											strRemarque,
											Integer.parseInt( strMinutes ),
											false,
											false );
								} else {  		// ... or else update the current absence item
									getUiHandlers().updateAbsenceLateItem( 
											((Label)((VerticalPanel)((TextBox)event.getSource()).getParent()).getWidget(1)).getText(),
											strMinutes );
								}
							} else {
								//
								if ( ((VerticalPanel)((TextBox)event.getSource()).getParent()).getWidget(1) != null ) {
									getUiHandlers().removeAbsenceItem( ((Label)((VerticalPanel)((TextBox)event.getSource()).getParent()).getWidget(1)).getText() );
								}
							}
						}
						
					});
					//
					tblAttendance.setWidget(i, j, pnlAbsenceCell);
				}
			}
		}
		
		//
		// Remarque column
		addRemarqueColumn( indexRemarqueCol );
	}
	
	
	
	/*
	 * */
	public void addRemarqueColumn(int remarqueColumnIndex) {
		//
		VerticalPanel vPanel;
		TextBox txtRemarque;
		for (int i= this.constantStudentNameRowStart; i< tblAttendance.getRowCount(); i++) {
			//
			tblAttendance.setText(i, remarqueColumnIndex, "");
			vPanel = new VerticalPanel();
			txtRemarque = new TextBox();
			//
			txtRemarque.addValueChangeHandler(new ValueChangeHandler<String>(){

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					//
					getWidgetIndex( (TextBox)event.getSource(), tblAttendance );
					//
					String strRemarque = ((TextBox)event.getSource()).getText().trim();
					String absenceItemID = "";
					//
					// If there is already an ID with this comment
					if ( ((VerticalPanel) ((TextBox)event.getSource()).getParent()).getWidgetCount() > 1 )
						absenceItemID = ((Label)((VerticalPanel) ((TextBox)event.getSource()).getParent()).getWidget(1)).getText();
					else { // Get the ID of one of the absence item
						for (int col= constantPeriodsColStart; col< tblAttendance.getCellCount(clickedRowIndex) - 1; col++) {
							if ( ((VerticalPanel) tblAttendance.getWidget(clickedRowIndex, col)).getWidgetCount() > 1 ) {
								absenceItemID = ((Label)((VerticalPanel) tblAttendance.getWidget(clickedRowIndex, col)).getWidget(1)).getText();
								break;
							}
						}
					}
					//
					if ( !absenceItemID.equals("") )
						getUiHandlers().updateRemarque( absenceItemID, strRemarque );
						
				}
			});
			//
			vPanel.add(txtRemarque);
			tblAttendance.setWidget(i, remarqueColumnIndex, vPanel );
		}
	}
	

	
	/*
	 * */
	@Override
	public void resetUI( CurrentUser curUser ) {
		//
		dtAbsenceDate.setValue( new Date());
		//
//		if (!curUser.isAdmin())
//			dtAbsenceDate.setEnabled(false);
		//
		lstProfs.clear();
		lstAssignments.clear();
		//
		tblAttendance.removeAllRows();
	}
	
	
	/*
	 * */
	@UiHandler("optLate")
	void onOptLateClick(ClickEvent event) {
		//
		lblTitle.setText("Saisir des retards");
		//
		clearTableWidgets();
		//
		prepareLateWidget();
		//
		showCurrentAbsenceItems();
	}
	
	
	/*
	 * */
	@UiHandler("optExclude")
	void onOptExcludeClick(ClickEvent event) {
		//
		lblTitle.setText("Saisir des exclusions");
		//
		clearTableWidgets();
		//
		prepareAbsentWidget();
		//
		showCurrentAbsenceItems();
	}
	
	
	/*
	 * */
	@UiHandler("optHealth")
	void onOptHealthClick(ClickEvent event) {
		//
		lblTitle.setText("Saisir des infirmeries");
		//
		clearTableWidgets();
		//
		prepareAbsentWidget();
		//
		showCurrentAbsenceItems();
	}


	/*
	 * */
	@Override
	public void setAddedAbsenceItem(AbsenceItemProxy aip) {
		//
		Label id;
		if ( tblAttendance.getWidget(clickedRowIndex, clickedCellIndex) != null ) {
			id = new Label(aip.getId().toString());
			id.setVisible( false );
			((VerticalPanel)tblAttendance.getWidget(clickedRowIndex, clickedCellIndex)).add( id );
		}
		// Add to the current list of absence items
		if (providerAbsenceItems != null)
			providerAbsenceItems.getList().add( aip );
	}


	/*
	 * */
	@Override
	public void removeDeletedAbsenceItemId() {
		//
		String aID = ((Label)((VerticalPanel)tblAttendance.getWidget(clickedRowIndex, clickedCellIndex)).getWidget(1)).getText();
		for (AbsenceItemProxy ai : providerAbsenceItems.getList()) {
			if (ai.getId().toString().equals(aID)) {
				providerAbsenceItems.getList().remove(ai);
				break;
			}
		}
		//
		if ( tblAttendance.getWidget(clickedRowIndex, clickedCellIndex) != null ) {
			((VerticalPanel)tblAttendance.getWidget(clickedRowIndex, clickedCellIndex)).getWidget(1).removeFromParent();
		}
		//
		redrawAbsenceItems();
	}


	/*
	 * */
	@Override
	public void showAbsenceItems(List<AbsenceItemProxy> aip) {
		//
		providerAbsenceItems.getList().clear();
		providerAbsenceItems.getList().addAll(aip);
		//
		redrawAbsenceItems();
	}
	
	
	/*
	 * */
	public void redrawAbsenceItems() {
		//
		// Depends on the currently selected option, prepare the table and show the data
		if (getSelectedAbsenceTypeCode().equals( AbsenceValues.absenceType_Absence_Code))
			onOptAbsentClick( null );
		if (getSelectedAbsenceTypeCode().equals( AbsenceValues.absenceType_Exclude_Code))
			onOptExcludeClick( null );
		if (getSelectedAbsenceTypeCode().equals( AbsenceValues.absenceType_Health_Code))
			onOptHealthClick( null );
		if (getSelectedAbsenceTypeCode().equals( AbsenceValues.absenceType_Late_Code))
			onOptLateClick( null );
	}
	
	
	/*
	 * */
	public void showCurrentAbsenceItems() {
		//
		String codeAbsence = getSelectedAbsenceTypeCode();
		String studentId, periodId;
		VerticalPanel vpanel;
		BulletinSubjectProxy bulletinSubject;
		Label lblId;
		//
		for (int row= this.constantStudentNameRowStart; row< tblAttendance.getRowCount(); row++) {
			for (int col= this.constantPeriodsColStart; col< tblAttendance.getCellCount(0) - 2; col++) {
				//
				bulletinSubject = providerBulletins.getList().get( row - this.constantStudentNameRowStart );
				studentId = bulletinSubject.getStudentId();
				periodId = providerPeriods.getList().get( col - this.constantPeriodsColStart ).getId().toString();
				//
				for ( AbsenceItemProxy ai : providerAbsenceItems.getList() ) {
					if (ai.getCodeAbsenceType().equals(codeAbsence)) {
						if ( ai.getStudentId().equals(studentId) && ai.getPeriodId().equals(periodId) ) {
							//
							vpanel = (VerticalPanel)tblAttendance.getWidget(row, col);
							//
							if (codeAbsence.equals(AbsenceValues.absenceType_Late_Code))
								((TextBox)vpanel.getWidget(0)).setText(Integer.toString(ai.getLateMinutes()));
							else
								((CheckBox)vpanel.getWidget(0)).setValue(true);
							//
							lblId = new Label(ai.getId().toString());
							lblId.setVisible(false);
							vpanel.add( lblId );
							//
							// Show remarque
							if ( !ai.getProfComment().equals("") ) {
								Label r = new Label(ai.getId().toString());
								r.setVisible(false);
								((TextBox)((VerticalPanel) tblAttendance.getWidget(row, tblAttendance.getCellCount(row) - 2 )).getWidget(0)).setText( ai.getProfComment() );
								((VerticalPanel) tblAttendance.getWidget(row, tblAttendance.getCellCount(row)-2)).add( r );
							}
							// Show admin remarque
							tblAttendance.setText(row,  tblAttendance.getCellCount(row) - 1, 
									tblAttendance.getText(row, tblAttendance.getCellCount(row) - 1) + " " + ai.getAdminComment());
						}
					}
				}
			}
		}
	}


	/*
	 * */
	@Override
	public void setUpdatedAbsenceItem(AbsenceItemProxy ai) {
		//
		for (int index=0; index< providerAbsenceItems.getList().size(); index++ ) {
			if ( providerAbsenceItems.getList().get(index).getId().equals(ai.getId()) ) {
				//
				providerAbsenceItems.getList().set(index, ai);
				//
				Label r = new Label(ai.getId().toString());
				r.setVisible(false);
				((VerticalPanel)tblAttendance.getWidget(clickedRowIndex, clickedCellIndex)).add( r );
				//
				break;
			}
		}
		providerAbsenceItems.flush();
	}
}
