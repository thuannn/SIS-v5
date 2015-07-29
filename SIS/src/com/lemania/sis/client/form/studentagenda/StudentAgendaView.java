package com.lemania.sis.client.form.studentagenda;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lemania.sis.client.UI.AgendaVerticalPanel;
import com.lemania.sis.client.values.ClassPeriodValues;
import com.lemania.sis.shared.ClasseProxy;
import com.lemania.sis.shared.ProfileProxy;
import com.lemania.sis.shared.bulletin.BulletinProxy;
import com.lemania.sis.shared.masteragendaitem.MasterAgendaItemProxy;
import com.lemania.sis.shared.period.PeriodProxy;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ChangeEvent;

public class StudentAgendaView extends
		ViewWithUiHandlers<StudentAgendaUiHandlers> implements
		StudentAgendaPresenter.MyView {
	
	interface Binder extends UiBinder<Widget, StudentAgendaView> {
	}

	@UiField ListBox lstClasses;
	@UiField ListBox lstProfiles;
	@UiField FlexTable tblAgenda;
	@UiField ListBox lstStudents;
	@UiField VerticalPanel pnlPrincipal;
	
	
	//
	int colorIndex = 0;
	//
	List<PeriodProxy> periods = new ArrayList<PeriodProxy>();
	//
	VerticalPanel selectedVP;
	

	@Inject
	StudentAgendaView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	
	/*
	 * */
	@Override
	public void setClassList(List<ClasseProxy> classes) {
		//
		lstClasses.clear();
		lstClasses.addItem("-","");
		for (ClasseProxy clazz : classes) {
			lstClasses.addItem( clazz.getClassName(), clazz.getId().toString());
		}
	}
	
	
	/*
	 * */
	@Override
	public void initializeUI() {
		//
		tblAgenda.removeAllRows();
		lstProfiles.clear();
	}
	
	
	/*
	 * */
	@Override
	public void initializeUI(List<PeriodProxy> periods) {
		//
		this.periods.clear();
		this.periods.addAll(periods);
		drawTable(periods);
	}
	
	
	/*
	 * */
	@Override
	public void drawTable(List<PeriodProxy> periods) {
		//
		tblAgenda.removeAllRows();
		//
		tblAgenda.setText(0, 0, "");
		tblAgenda.setText(0, 1, ClassPeriodValues.getDayName(ClassPeriodValues.d2_code));
		tblAgenda.setText(0, 2, ClassPeriodValues.getDayName(ClassPeriodValues.d3_code));
		tblAgenda.setText(0, 3, ClassPeriodValues.getDayName(ClassPeriodValues.d4_code));
		tblAgenda.setText(0, 4, ClassPeriodValues.getDayName(ClassPeriodValues.d5_code));
		tblAgenda.setText(0, 5, ClassPeriodValues.getDayName(ClassPeriodValues.d6_code));
		//
		for (int i=0; i<periods.size(); i++) {
			tblAgenda.setText( i+ 1, 0, periods.get(i).getDescription() );
		}
		//
		for (int i=1; i<tblAgenda.getRowCount(); i++) {
			for (int j=1; j<tblAgenda.getCellCount(0); j++) {
					//
					final VerticalPanel vp = new VerticalPanel();
					vp.add(new Label("+"));
					vp.getWidget(0).setStyleName("hiddenText");
					tblAgenda.setWidget(i, j, vp);
					vp.addDomHandler(new MouseOverHandler() {

						@Override
						public void onMouseOver(MouseOverEvent arg0) {
							//
							selectedVP = vp;
							vp.getElement().getStyle().setBackgroundColor("#f2f2f2"); 
						}
						
					}, MouseOverEvent.getType());
					//
					vp.addDomHandler(new MouseOutHandler() {

						@Override
						public void onMouseOut(MouseOutEvent arg0) {
							//
							vp.getElement().getStyle().setBackgroundColor("white");
						}
						
					}, MouseOutEvent.getType());
					//
					vp.setStyleName("agendaItemContainer");
					tblAgenda.setWidget(i , j, vp );
			}
		}
		// Set IDs
		int i = tblAgenda.getRowCount();
		tblAgenda.setText(i, 0, "");
		tblAgenda.setText(i, 1, ClassPeriodValues.d2_code);
		tblAgenda.setText(i, 2, ClassPeriodValues.d3_code);
		tblAgenda.setText(i, 3, ClassPeriodValues.d4_code);
		tblAgenda.setText(i, 4, ClassPeriodValues.d5_code);
		tblAgenda.setText(i, 5, ClassPeriodValues.d6_code);
		//
		i = tblAgenda.getCellCount(0);
		for (int j=0; j < periods.size(); j++) {
			tblAgenda.setText( j+1 , i, periods.get(j).getId().toString() );
		}
		//
		styleTable();
	}
	
	
	/*
	 * */
	public void styleTable(){
		// All cells
		for (int i=0; i<tblAgenda.getRowCount(); i++) {
			for (int j=0; j<tblAgenda.getCellCount(0); j++) {
				if (tblAgenda.isCellPresent(i, j))
					tblAgenda.getCellFormatter().setStyleName(i, j, "agendaNormal");
			}
		}
		// Hidden data
		for (int j=0; j<tblAgenda.getCellCount(0); j++) {
			if (tblAgenda.isCellPresent( tblAgenda.getRowCount() - 1, j))
				tblAgenda.getCellFormatter().setStyleName( tblAgenda.getRowCount() - 1, j, "agendaHidden");
		}
		//
		for (int i=0; i<tblAgenda.getRowCount(); i++)
			if (tblAgenda.isCellPresent(i, tblAgenda.getCellCount(0)))
				tblAgenda.getCellFormatter().setStyleName(i, tblAgenda.getCellCount(0), "agendaHidden");
		// Titles
		for (int j=0; j<tblAgenda.getCellCount(0); j++) {
			if (tblAgenda.isCellPresent( 0, j))
				tblAgenda.getCellFormatter().setStyleName( 0, j, "agendaTitle");
		}
		//
		for (int i=0; i<tblAgenda.getRowCount()-1; i++)
			if (tblAgenda.isCellPresent(i, 0))
				tblAgenda.getCellFormatter().setStyleName(i, 0, "agendaTitle");
		//
		tblAgenda.getElement().getStyle().setVerticalAlign( VerticalAlign.BOTTOM );
	}
	
	
	/*
	 * */
	@UiHandler("lstClasses")
	void onLstClassesChange(ChangeEvent event) {
		//
		if (getUiHandlers() != null)
			getUiHandlers().onClassChanged( lstClasses.getValue(lstClasses.getSelectedIndex()) );
	}
	
	
	/*
	 * */
	@UiHandler("lstProfiles")
	void onLstProfilesChange(ChangeEvent event) {
		//
		if (getUiHandlers() != null)
			getUiHandlers().onProfileChanged( 
					lstClasses.getValue( lstClasses.getSelectedIndex() ),
					lstProfiles.getValue( lstProfiles.getSelectedIndex()) );
	}
	
	
	/*
	 * */
	@Override
	public void setProfileListData(List<ProfileProxy> profiles) {
		//
		lstProfiles.clear();
		lstProfiles.addItem("-","");
		//
		for (ProfileProxy profile : profiles){
			lstProfiles.addItem( profile.getProfileName(), profile.getId().toString() );
		}
		lstProfiles.setSelectedIndex(0);
	}
	
	
	/*
	 * */
	@Override
	public void showMasterAgendaItemData(List<MasterAgendaItemProxy> mais) {
		//
		clearTable();
		//
		int rowIndex;
		int cellIndex;
		for ( MasterAgendaItemProxy mai : mais ) {
			rowIndex = getRowIndexById( mai.getPeriodId() );
			cellIndex = getCellIndexById( mai.getJourCode() );
			if ( (rowIndex != -1) && (cellIndex != -1)) {
				showMasterAgendaItem( mai, rowIndex, cellIndex );
			}
		}
	}
	
	
	/*
	 * */
	private int getRowIndexById(String periodId) {
		//
		for ( int i=0; i < tblAgenda.getRowCount(); i++ ) {
			if ( tblAgenda.getText(i, tblAgenda.getCellCount(i)-1).equals(periodId) )
				return i;
		}
		return -1;
	}
	
	
	/*
	 * */
	private int getCellIndexById(String jourCode) {
		//
		for ( int i=0; i < tblAgenda.getCellCount(0); i++ ) {
			if ( tblAgenda.getText( tblAgenda.getRowCount()-1, i).equals(jourCode) )
				return i;
		}
		return -1;
	}
	
	
	/*
	 * No colspan or rowspan, display in each cell to simplify
	 * */
	public void showMasterAgendaItem( final MasterAgendaItemProxy mai, final int rowIndex, final int cellIndex ) {
		//
		int duration = mai.getDuration();
		for (int i=0; i<duration; i++) {
			// 
			final AgendaVerticalPanel avp = new AgendaVerticalPanel();
			avp.setStyleName("agendaSelected");
			avp.getElement().getStyle().setBackgroundColor( ClassPeriodValues.colors.get( colorIndex ));
			//
			avp.addDomHandler( new MouseOverHandler(){
				@Override
				public void onMouseOver(MouseOverEvent arg0) {
					//
					avp.getElement().getStyle().setBorderStyle( Style.BorderStyle.SOLID );
					avp.getElement().getStyle().setBorderWidth( 1, Unit.PX );
					avp.getElement().getStyle().setBorderColor("lightskyblue");
					avp.getElement().getStyle().setCursor( Style.Cursor.POINTER );
				}
			}, MouseOverEvent.getType());
			//
			avp.addDomHandler( new MouseOutHandler(){
				@Override
				public void onMouseOut(MouseOutEvent arg0) {
					//
					avp.getElement().getStyle().setBorderColor("white");
					avp.getElement().getStyle().setCursor( Style.Cursor.AUTO );
				}
			}, MouseOutEvent.getType());
			//
			avp.setMai(mai);
			avp.setCellIndex(cellIndex);
			avp.setRowIndex(rowIndex);
			//
			Label lblSubject = new Label( mai.getSubjectName() );
			lblSubject.setStyleName("agendaSubjectText");
			avp.add( lblSubject );
			//
			Label lblNormal = new Label( mai.getProfName() + " - " + mai.getClassroomName());
			lblNormal.setStyleName("agendaNormalText");
			avp.add( lblNormal);
			//
			((VerticalPanel) tblAgenda.getWidget(rowIndex + i, cellIndex)).add(avp);
		}
		//
		if ( (colorIndex+1) < ClassPeriodValues.colors.size() ) colorIndex++; else colorIndex = 0;
	}

	
	
	/* 
	 * */
	@Override
	public void setStudentListData(List<BulletinProxy> students) {
		//
		lstStudents.clear();
		lstStudents.addItem("-");
		for (BulletinProxy bulletin : students) {
			lstStudents.addItem( bulletin.getStudentName(), bulletin.getId().toString() );
		}
	}
	
	
	/*
	 * */
	@UiHandler("lstStudents")
	void onLstStudentsChange(ChangeEvent event) {
		//
		if (getUiHandlers() != null)
			getUiHandlers().onStudentChange(
					lstClasses.getValue( lstClasses.getSelectedIndex() ), 
					lstProfiles.getValue( lstProfiles.getSelectedIndex() ), 
					lstStudents.getValue( lstStudents.getSelectedIndex() ));
	}
	
	
	/*
	 * */
	public void clearTable() {
		//
		VerticalPanel vp;
		for (int i=1; i<tblAgenda.getRowCount(); i++) {
			for (int k=1; k<tblAgenda.getCellCount(0); k++) {
					vp = ((VerticalPanel)tblAgenda.getWidget(i,k));
					if (vp == null)
						continue;
					for (int j=1; j<vp.getWidgetCount(); j++)				// starting with 1 because the first widget is a label
						((AgendaVerticalPanel) vp.getWidget(j)).removeFromParent();
			}
		}
	}
}
