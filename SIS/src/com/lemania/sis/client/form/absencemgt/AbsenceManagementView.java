package com.lemania.sis.client.form.absencemgt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.lemania.sis.client.UI.DynamicSelectionCell;
import com.lemania.sis.client.UI.GridButtonCell;
import com.lemania.sis.client.values.AbsenceValues;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.absenceitem.AbsenceItemProxy;
import com.lemania.sis.shared.bulletin.BulletinProxy;
import com.lemania.sis.shared.motifabsence.MotifAbsenceProxy;
import com.lemania.sis.shared.parent.ParentProxy;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.ui.HTML;

public class AbsenceManagementView extends ViewWithUiHandlers<AbsenceManagementUiHandlers> implements
		AbsenceManagementPresenter.MyView {

	private final Widget widget;
	
	//
	int selectedAbsentStudentIndex = -1;
	ListDataProvider<AbsenceItemProxy> providerAbsentStudents = new ListDataProvider<AbsenceItemProxy>();
	AbsenceItemProxy selectedAbsentStudent;
	
	int selectedBulletinIndex = -1;
	ListDataProvider<BulletinProxy> providerBulletins = new ListDataProvider<BulletinProxy>();
	BulletinProxy selectedBulletin;
	
	ListDataProvider<AbsenceItemProxy> providerAbsences = new ListDataProvider<AbsenceItemProxy>();
	int selectedAbsenceItemIndex = -1;
	AbsenceItemProxy selectedAbsenceItem;
	
	ListDataProvider<ParentProxy> providerParents = new ListDataProvider<ParentProxy>();
	int selectedParentIndex = -1;
	ParentProxy selectedParent;
	
	ListDataProvider<MotifAbsenceProxy> providerMotifs = new ListDataProvider<MotifAbsenceProxy>();
	List<String> motifList = new ArrayList<String>();
	
	DynamicSelectionCell cellMotifs;
	SuggestBox sgbStudents;
	private final MultiWordSuggestOracle mySuggestions = new MultiWordSuggestOracle();
	
	String selectedStudentId;
	String selectedStudentName;
	
	DialogBox popupSMSEmail = new DialogBox();
	
	private enum messageType {
		SMS,
		Email
	}
	private messageType sendMethod;
	
	//
	

	public interface Binder extends UiBinder<Widget, AbsenceManagementView> {
	}

	@Inject
	public AbsenceManagementView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	@UiField(provided=true) DataGrid<AbsenceItemProxy> tblAbsences = new DataGrid<AbsenceItemProxy>();
	@UiField(provided=true) DataGrid<AbsenceItemProxy> tblStudents = new DataGrid<AbsenceItemProxy>();
	@UiField SimplePager pagerAbsences;
	@UiField Label lblStudentName;
	@UiField DateBox dateFrom;
	@UiField DateBox dateTo;
	@UiField Button cmdFilter;
	@UiField Button cmdAddAbsence;
	@UiField Button cmdPrint;
	@UiField HorizontalPanel pnlNames;
	@UiField VerticalPanel pnlBulletin;
	@UiField HTMLPanel pnlMainAbsenceMgt;
	@UiField VerticalPanel pnlMainBulletin;
	@UiField VerticalPanel pnlMainAbsences;
	@UiField FlexTable tblFlexAbsences;
	@UiField Label lblBulletinStudentName;
	@UiField Label lblBulletinClasse;
	@UiField Label lblToDate;
	@UiField Label lblFromDate;
	@UiField AbsolutePanel pnlWhiteBackground;
	@UiField Label lblSpace;
	@UiField HorizontalPanel non;
	@UiField VerticalPanel pnlDirection;
	@UiField SimplePager pagerStudents;
	@UiField VerticalPanel pnlSMSEmail;
	@UiField Label lblNotifStudentName;
	@UiField ListBox lstParents;
	@UiField Label lblSendMethod;
	@UiField Button cmdClosePopupSMS;
	@UiField Button cmdSend;
	@UiField TextArea txtSendMessage;
	@UiField HTML lblNotificationDates;
	
	
	
	
	/*
	 * */
	@Override
	public void setStudentTableData(List<AbsenceItemProxy> studentList) {
		//
		providerAbsentStudents.getList().clear();
		providerAbsentStudents.setList(studentList);
		providerAbsentStudents.flush();
	}
	
	

	/*
	 * */
	@Override
	public void initializeUI() {
		//
		initializeStudentTable();
		//
		initializeAbsenceTable();
		//
		initializeDateFields();
		//
		initializeSuggestBox();
		//
		initializePopupSMSEmail();
	}
	
	
	
	/*
	 * */
	private void initializePopupSMSEmail() {
		//
		popupSMSEmail.setGlassEnabled( true );
		popupSMSEmail.setText("Envoyer SMS / Email aux parents");
		//
		popupSMSEmail.addCloseHandler( new CloseHandler<PopupPanel>(){

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				//
				pnlSMSEmail.setVisible( false );
				pnlMainAbsenceMgt.add( pnlSMSEmail );
			}
			
		});
	}
	
	
	
	/*
	 * */
	private void showPopupSMSEmail( messageType method ) {
		//
		sendMethod = method;
		lblNotifStudentName.setText( selectedAbsenceItem.getStudentName() );
		txtSendMessage.setText( 
				"Absence de " + selectedAbsenceItem.getStudentName() 
				+ " le " + selectedAbsenceItem.getStrAbsenceDate().substring(6) + "." + selectedAbsenceItem.getStrAbsenceDate().substring(4,6) + "." + selectedAbsenceItem.getStrAbsenceDate().substring(0,4)  
				+ " de "+ selectedAbsenceItem.getPeriodDesc().replace(":", "h") +".\n\n"
				+ "Merci d’en prendre note et de nous faire parvenir rapidement l’éventuelle excuse.\n"
				+ "ECOLE LEMANIA" );
		//
		if ( method == messageType.SMS ) {
			lblSendMethod.setText( "SMS" );
			showNotificationDatesSMS( selectedAbsenceItem );
		}
		if ( method == messageType.Email ) {
			lblSendMethod.setText( "Email" );
			showNotificationDatesEmail( selectedAbsenceItem );
		}
		//
		populateParentList( method );
		//
		pnlSMSEmail.setVisible( true );
		//
		popupSMSEmail.add( pnlSMSEmail );
		popupSMSEmail.center();
	}
	
	

	/*
	 * */
	private void initializeSuggestBox() {
		//
		sgbStudents = new SuggestBox( mySuggestions );
		sgbStudents.addSelectionHandler( new SelectionHandler<Suggestion>(){

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				//
				onStudentSuggestionSelected( event.getSelectedItem().getReplacementString() );
			}
			
		});
		
		pnlNames.add( sgbStudents );
	}
	
	
	
	/*
	 * */
	void onStudentSuggestionSelected( String typedName ) {
		//
		for( BulletinProxy bp : providerBulletins.getList() ) {
			if ( typedName.equals( bp.getStudentName() ) ) {
				//
				selectedStudentId = bp.getStudentId().toString();
				selectedStudentName = bp.getStudentName();
				selectedBulletin = bp;
				lblStudentName.setText( bp.getStudentName() );
				//
				getUiHandlers().filterDate( selectedStudentId.toString(), 
						DateTimeFormat.getFormat("yyyyMMdd").format( dateFrom.getValue() ), 
						DateTimeFormat.getFormat("yyyyMMdd").format( dateTo.getValue() ) );
				break;
			}
		}
	}
	
	
	
	/*
	 * */
	private void initializeDateFields() {
		//
		dateFrom.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy")));
		dateFrom.addValueChangeHandler( new ValueChangeHandler<Date>(){
			int pog = 0;
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				//
				if (pog == 0) {
					pog++;
				} else {
					pog = 0;
				}
			}
		});
		dateFrom.setValue(new Date());
		//
		dateTo.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy")));
		dateTo.addValueChangeHandler( new ValueChangeHandler<Date>(){
			int pog = 0;
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				//
				if (pog == 0) {
					pog++;
				} else {
					pog = 0;
				}
			}
		});
		dateTo.setValue(new Date());
	}

	/*
	 * */
	private void initializeAbsenceTable() {
		//
	    TextColumn<AbsenceItemProxy> colLastName = new TextColumn<AbsenceItemProxy>() {
	      @Override
	      public String getValue(AbsenceItemProxy object) {
	    	  String date = object.getStrAbsenceDate();
	    	  return date.substring(6) + "." + date.substring(4,6) + "." + date.substring(0,4); 
	      } 
	    };
	    tblAbsences.addColumn(colLastName, "Date");
	    tblAbsences.setColumnWidth(colLastName, 80, Unit.PX);
	    
	    //
	    TextColumn<AbsenceItemProxy> colPeriod = new TextColumn<AbsenceItemProxy>() {
	      @Override
	      public String getValue(AbsenceItemProxy object) {
	    	  return object.getPeriodDesc();
	      } 
	    };
	    tblAbsences.addColumn(colPeriod, "Period");
	    tblAbsences.setColumnWidth(colPeriod, 110, Unit.PX);
	    
	    // Add a text column to show the name.
	    TextColumn<AbsenceItemProxy> colType = new TextColumn<AbsenceItemProxy>() {
	      @Override
	      public String getValue(AbsenceItemProxy object) {
	        return AbsenceValues.getCodeFR( object.getCodeAbsenceType() );
	      }
	    };
	    tblAbsences.addColumn(colType, "Type");
	    tblAbsences.setColumnWidth(colType, 50, Unit.PX);
	    
	    // Add a text column to show the name.
	    TextColumn<AbsenceItemProxy> colMinutes = new TextColumn<AbsenceItemProxy>() {
	      @Override
	      public String getValue(AbsenceItemProxy object) {
	        return (object.getLateMinutes() > 0) ? Integer.toString(object.getLateMinutes()) : "";
	      }
	    };
	    tblAbsences.addColumn(colMinutes, "Minutes");
	    tblAbsences.setColumnWidth(colMinutes, 50, Unit.PX);
	    
	    // Prof
	    TextColumn<AbsenceItemProxy> colProf = new TextColumn<AbsenceItemProxy>() {
	      @Override
	      public String getValue(AbsenceItemProxy object) {
	        return object.getProfName();
	      }
	    };
	    tblAbsences.addColumn(colProf, "Professeur");
	    tblAbsences.setColumnWidth(colProf, 15, Unit.PCT);
	    
	    // Cours
	    TextColumn<AbsenceItemProxy> colSubject = new TextColumn<AbsenceItemProxy>() {
	      @Override
	      public String getValue(AbsenceItemProxy object) {
	        return object.getSubjectName();
	      }
	    };
	    tblAbsences.addColumn(colSubject, "Cours");
	    tblAbsences.setColumnWidth(colSubject, 15, Unit.PCT);
	    
	    // -- Comment prof
	    Column<AbsenceItemProxy, String> colProfNote = new Column<AbsenceItemProxy, String> (new GridButtonCell()){
	    	@Override
	    	public String getValue(AbsenceItemProxy bp){
	    		return ( bp.getProfComment().equals("") ? "+" : "..." );
	    	}
	    };
	    colProfNote.setFieldUpdater(new FieldUpdater<AbsenceItemProxy, String>(){
	    	@Override
	    	public void update(int index, final AbsenceItemProxy ps, String value){
	    		selectedAbsenceItemIndex = index;
	    		selectedAbsenceItem = ps;
	    		//
	    		VerticalPanel vp = new VerticalPanel();
	    		vp.setHorizontalAlignment( VerticalPanel.ALIGN_RIGHT );
	    		final PopupPanel pNote = new PopupPanel(true);
	    		final TextArea tNote = new TextArea();
	    		tNote.setStyleName("popupNotePanel");
	    		tNote.setText( ps.getProfComment() );
	    		Button cmdSave = new Button("Fermer");
	    		cmdSave.setStyleName("buttonToolbar");
	    		cmdSave.addClickHandler( new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						//
						pNote.hide();
					}
	    			
	    		});
	    		vp.add( tNote );
	    		vp.add( cmdSave );
	    		pNote.add( vp );
	    		pNote.center();
	    		pNote.show();
	    	}
	    });
	    tblAbsences.setColumnWidth(colProfNote, 50, Unit.PX);
	    tblAbsences.addColumn(colProfNote, "Note Prof");	
	    
	    
	    // ---
	    CheckboxCell cellJustify = new CheckboxCell();
	    Column<AbsenceItemProxy, Boolean> colJustify = new Column<AbsenceItemProxy, Boolean>( cellJustify ) {
	    	@Override
	    	public Boolean getValue(AbsenceItemProxy student){
	    		return student.isJusttified();
	    	}	    	
	    };
	    colJustify.setFieldUpdater( new FieldUpdater<AbsenceItemProxy, Boolean>() {

			@Override
			public void update(int index, AbsenceItemProxy object, Boolean value) {
				//
				getUiHandlers().updateJustifyStatus(object, value);
			}
	    	
	    });
	    tblAbsences.addColumn(colJustify, "Excusée");
	    tblAbsences.setColumnWidth(colJustify, 50, Unit.PX);
	    
	    
	    // ---
	    CheckboxCell cellParent = new CheckboxCell();
	    Column<AbsenceItemProxy, Boolean> colParent = new Column<AbsenceItemProxy, Boolean>( cellParent ) {
	    	@Override
	    	public Boolean getValue(AbsenceItemProxy student){
	    		return student.isParentNotified();
	    	}	    	
	    };
	    colParent.setFieldUpdater( new FieldUpdater<AbsenceItemProxy, Boolean>() {

			@Override
			public void update(int index, AbsenceItemProxy object, Boolean value) {
				//
				getUiHandlers().updateParentNotifiedStatus(object, value);
			}
	    	
	    });
	    tblAbsences.addColumn(colParent, "Notif. Envoyée");
	    tblAbsences.setColumnWidth(colParent, 50, Unit.PX);
	    
	    
//	    // ---
//	    // 07.10.2014 - Not used for the moment
//	    cellMotifs = new DynamicSelectionCell( motifList );
//	    Column<AbsenceItemProxy, String> colMotifs = new Column<AbsenceItemProxy, String>(cellMotifs) {
//			@Override
//			public String getValue(AbsenceItemProxy object) {
//				//
//				if ( !object.getMotifId().equals("") )
//					return getMotifTextById( object.getMotifId() );
//				else
//					return "";
//			}
//	    };
//	    colMotifs.setFieldUpdater( new FieldUpdater<AbsenceItemProxy, String>(){
//
//			@Override
//			public void update(int index, AbsenceItemProxy object, String value) {
//				//
//				selectedAbsenceItemIndex = index;
//				selectedAbsenceItem = object;
//				for (MotifAbsenceProxy ma : providerMotifs.getList()) {
//					if ( value.equals( ma.getMotifCode() + " - " + ma.getMotifLabel() )) {
//						getUiHandlers().updateMotif( object, ma.getId().toString() );
//						break;
//					}
//				}
//			}
//	    	
//	    });
//	    tblAbsences.addColumn(colMotifs, "Motif");
//	    tblAbsences.setColumnWidth(colMotifs, 100, Unit.PX);
	    
	    
	    // -- comment admin
	    Column<AbsenceItemProxy, String> colAdminNote = new Column<AbsenceItemProxy, String> (new GridButtonCell()){
	    	@Override
	    	public String getValue(AbsenceItemProxy bp){
	    		return ( bp.getAdminComment().equals("") ? "+" : "..." );
	    	}
	    };
	    colAdminNote.setFieldUpdater(new FieldUpdater<AbsenceItemProxy, String>(){
	    	@Override
	    	public void update(int index, final AbsenceItemProxy ps, String value){
	    		selectedAbsenceItemIndex = index;
	    		selectedAbsenceItem = ps;
	    		//
	    		VerticalPanel vp = new VerticalPanel();
	    		vp.setHorizontalAlignment( VerticalPanel.ALIGN_RIGHT );
	    		final PopupPanel pNote = new PopupPanel(true);
	    		final TextArea tNote = new TextArea();
	    		tNote.setStyleName("popupNotePanel");
	    		tNote.setText( ps.getAdminComment() );
	    		Button cmdSave = new Button("Valider");
	    		cmdSave.setStyleName("buttonToolbar");
	    		cmdSave.addClickHandler( new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						//
						getUiHandlers().updateAdminComment( ps, tNote.getText() );
						pNote.hide();
					}
	    			
	    		});
	    		vp.add( tNote );
	    		vp.add( cmdSave );
	    		pNote.add( vp );
	    		pNote.center();
	    		pNote.show();
	    	}
	    });
	    tblAbsences.setColumnWidth(colAdminNote, 50, Unit.PX);
	    tblAbsences.addColumn(colAdminNote, "Note Admin");	
	    
	    
	    // -- SMS
	    Column<AbsenceItemProxy, String> colSMS = new Column<AbsenceItemProxy, String> (new GridButtonCell()){
	    	@Override
	    	public String getValue(AbsenceItemProxy bp){
	    		return "SMS";
	    	}
	    };
	    colSMS.setFieldUpdater(new FieldUpdater<AbsenceItemProxy, String>(){
	    	@Override
	    	public void update(int index, AbsenceItemProxy ps, String value){
	    		selectedAbsenceItemIndex = index;
	    		selectedAbsenceItem = ps;
	    		//
	    		showPopupSMSEmail( messageType.SMS );
	    	}
	    });
	    tblAbsences.setColumnWidth(colSMS, 75, Unit.PX);
	    tblAbsences.addColumn(colSMS, "");	
	    
	    
	    // -- Email
	    Column<AbsenceItemProxy, String> colEmail = new Column<AbsenceItemProxy, String> (new GridButtonCell()){
	    	@Override
	    	public String getValue(AbsenceItemProxy bp){
	    		return "Email";
	    	}
	    };
	    colEmail.setFieldUpdater(new FieldUpdater<AbsenceItemProxy, String>(){
	    	@Override
	    	public void update(int index, AbsenceItemProxy ps, String value){
	    		selectedAbsenceItemIndex = index;
	    		selectedAbsenceItem = ps;
	    		//
	    		showPopupSMSEmail( messageType.Email );
	    	}
	    });
	    tblAbsences.setColumnWidth(colEmail, 75, Unit.PX);
	    tblAbsences.addColumn(colEmail, "");	
	    
	   
	    // -- Delete
	    Column<AbsenceItemProxy, String> colDelete = new Column<AbsenceItemProxy, String> (new GridButtonCell()){
	    	@Override
	    	public String getValue(AbsenceItemProxy bp){
	    		return "X";
	    	}
	    };
	    colDelete.setFieldUpdater(new FieldUpdater<AbsenceItemProxy, String>(){
	    	@Override
	    	public void update(int index, AbsenceItemProxy ps, String value){
	    		selectedAbsenceItemIndex = index;
	    		selectedAbsenceItem = ps;
	    		getUiHandlers().removeAbsenceItem( ps );
	    	}
	    });
	    tblAbsences.setColumnWidth(colDelete, 50, Unit.PX);
	    tblAbsences.addColumn(colDelete, "");	   
	    
	    
	    // 
	    //	Selection model
	    final SingleSelectionModel<AbsenceItemProxy> selectionModel = new SingleSelectionModel<AbsenceItemProxy>();
	    tblAbsences.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	//
		    public void onSelectionChange(SelectionChangeEvent event) {
		    	//
		    	selectedAbsenceItem = selectionModel.getSelectedObject();
		    	selectedAbsenceItemIndex = providerAbsences.getList().indexOf(selectedAbsenceItem);
		    }
	    });
	    
	    //
	    pagerAbsences.setDisplay(tblAbsences);
	    
	    //
	    providerAbsences.addDataDisplay(tblAbsences);
	}
	
	

	/* 
	 * Initialize Student table 
	 * */
	public void initializeStudentTable(){
		//
		TextColumn<AbsenceItemProxy> colName = new TextColumn<AbsenceItemProxy>() {
	      @Override
	      public String getValue( AbsenceItemProxy object ) {
	        return object.getStudentName();
	      }
	    };
	    tblStudents.addColumn(colName, "Elève");
	    
	    //
	    // When user select a student, load his absence history
	    final SingleSelectionModel<AbsenceItemProxy> selectionModel = new SingleSelectionModel<AbsenceItemProxy>();
	    tblStudents.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	    	  //
	    	  selectedAbsentStudent = selectionModel.getSelectedObject();
	    	  if (selectedAbsentStudent != null) {
	    		  //
	    		  lblStudentName.setText( selectedAbsentStudent.getStudentName() );
	    		  //
	    		  selectedStudentId = selectedAbsentStudent.getStudentId();
	    		  selectedStudentName = selectedAbsentStudent.getStudentName();
	    		  getUiHandlers().filterDate( selectedStudentId,
	    				  DateTimeFormat.getFormat("yyyyMMdd").format(dateFrom.getValue()),
	    				  DateTimeFormat.getFormat("yyyyMMdd").format(dateTo.getValue()) );
	    	  }
	      }
	    });
	    
	    //
	    pagerStudents.setDisplay(tblStudents);
	    
	    //
	    providerAbsentStudents.addDataDisplay(tblStudents);
	    
	}

	
	/*
	 * */
	@Override
	public void setAbsenceItemTableData(List<AbsenceItemProxy> absenceItems) {
		//
		providerAbsences.getList().clear();
		providerAbsences.getList().addAll(absenceItems);
		providerAbsences.flush();
	}

	
//	/*
//	 * */
//	@Override
//	public void setMotifListData(List<MotifAbsenceProxy> motifs) {
//		//
//		providerMotifs.getList().clear();
//		providerMotifs.getList().addAll(motifs);
//		//
////		// 07.10.2014 - Motif is not needed for the moment
//		cellMotifs.clear();
//		cellMotifs.addOption("Choisir");
//		for(MotifAbsenceProxy ma : motifs)
//			cellMotifs.addOption( ma.getMotifCode() + " - " + ma.getMotifLabel() );
//	}
	
	
	/*
	 * */
	public String getMotifTextById( String motifId ){
		for (MotifAbsenceProxy ma : providerMotifs.getList() ) {
			if (ma.getId().toString().equals(motifId))
				return ma.getMotifCode() + " - " + ma.getMotifLabel();
		}
		return "";
	}

	
	/*
	 * */
	@Override
	public void resetUI() {
		//
		clearData();
		//
		setDaysOfTheMonth();
	}
	
	
	/*
	 * */
	public void clearData() {
		// 
		// Clear absence items
		providerAbsences.getList().clear();
		providerAbsences.flush();
		//
		// Clear student selection
		tblStudents.getSelectionModel().setSelected( selectedAbsentStudent, false );
		providerAbsentStudents.getList().clear();
		providerAbsentStudents.flush();
		//
		sgbStudents.setValue("");
		//
		lblStudentName.setText("");
	}
	

	/*
	 * */
	private void setDaysOfTheMonth() {
		//
		Date date = new Date();
		
		CalendarUtil.setToFirstDayOfMonth(date);
		dateFrom.setValue(date);
		
		CalendarUtil.addMonthsToDate(date, 1);
		CalendarUtil.addDaysToDate(date, -1);
		dateTo.setValue(date);
	}

	/*
	 * */
	@Override
	public void setUpdatedAbsenceItem(AbsenceItemProxy aip) {
		//
		providerAbsences.getList().set( providerAbsences.getList().indexOf(selectedAbsenceItem), aip);
		providerAbsences.flush();
	}
	
	
	
	/* 
	 * Clicking on this buttton will load all students that have absences during the selected date range
	 * */
	@UiHandler("cmdFilter")
	void onCmdFilterClick(ClickEvent event) {
		//
		clearData();
		//
		loadAbsenceByDate();
	}
	
	
	/*
	 * 
	 * */
	void loadAbsenceByDate() {
		//
		String fromDate = DateTimeFormat.getFormat("yyyyMMdd").format( dateFrom.getValue() );
		String toDate = DateTimeFormat.getFormat("yyyyMMdd").format( dateTo.getValue() );
		//
		getUiHandlers().loadAbsentStudens( fromDate, toDate );
	}
	
	
	
	/*
	 * */
	@UiHandler("cmdPrint")
	void onCmdPrintClick(ClickEvent event) {
		//
		PopupPanel popup = new PopupPanel(true) {
			@Override
			  protected void onPreviewNativeEvent(final NativePreviewEvent event) {
			    super.onPreviewNativeEvent(event);
			    switch (event.getTypeInt()) {
			        case Event.ONKEYDOWN:
			            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
			            	//
			                hide();
			            }
			            break;
			    }
			}
		};
		popup.setStyleName("whitePanel");
		popup.add( pnlBulletin );
		//
		drawAbsenceTable();
		//
		distributeLineHeight();
		//
		pnlWhiteBackground.setHeight( pnlBulletin.getOffsetHeight() + "px");
		pnlWhiteBackground.setVisible(true);
		//
		popup.addCloseHandler(new CloseHandler<PopupPanel>() {
			public void onClose(CloseEvent<PopupPanel> event) {
				//
				pnlMainBulletin.add(pnlBulletin);
				//
				pnlWhiteBackground.setVisible(false);
			}
		});
		//
		popup.show();
	}
	
	
	
	/*
	 * */
	private void distributeLineHeight() {
		//
		double pageHeight = 25.7;
		double headerHeight = 3.6;
		double directionSectionHeight = 6;
		double rowHeight = 0.5;
		double whiteSpace = 0;
		double commentHeight = 1.2;
		//
		pnlBulletin.setHeight( pageHeight + "cm" );
		tblFlexAbsences.setHeight( ( tblFlexAbsences.getRowCount() * rowHeight) + "cm" );
		whiteSpace = pageHeight - ( tblFlexAbsences.getRowCount() * rowHeight) - headerHeight - directionSectionHeight - commentHeight;
		lblSpace.setHeight( whiteSpace + "cm");
		//
		StyleInjector.inject(".bulletinCellMargin { height:"+ rowHeight +"cm; font-size: 11px; border-top: 1px solid silver; border-right: 1px solid silver; }", true);
		StyleInjector.inject(".absenceBulletinHeader { height:"+ rowHeight +"cm; font-size: 11px; border-top: 1px solid silver; border-right: 1px solid silver; background-color: palegoldenrod; }", true);
		for (int i=0; i<tblFlexAbsences.getCellCount(0); i++)
			for (int j=0; j<tblFlexAbsences.getRowCount(); j++) {
				if (tblFlexAbsences.isCellPresent(j, i)) {
					if (j > 0)
						tblFlexAbsences.getCellFormatter().setStyleName(j, i, "bulletinCellMargin");
					else
						tblFlexAbsences.getCellFormatter().addStyleName(j, i, "absenceBulletinHeader");
				}
			}
	}
	
	
	
	/*
	 * Show or hide columns to send to parents
	 * */
	void drawAbsenceTable() {
		//
		int headerRow = 1;
		//
		int indexColDate = 0;
		int indexColPeriod = 1;
		int indexColType = 2;
		int indexColMinute = 3;
		int indexColProf = 4;
		int indexColCourse = 5;
		int indexColJustify = 6;
		//
		lblBulletinStudentName.setText( lblStudentName.getText() );
		if ( providerAbsences.getList().size()>0 )
			lblBulletinClasse.setText( providerAbsences.getList().get(0).getClassName() );
		lblFromDate.setText( dateFrom.getTextBox().getText() );
		lblToDate.setText( dateTo.getTextBox().getText() );
		//
		tblFlexAbsences.removeAllRows();
		//
		tblFlexAbsences.setText( 0 , indexColDate, "Date" );
		tblFlexAbsences.setText( 0 , indexColPeriod, "Période" );
		tblFlexAbsences.setText( 0 , indexColType, "Type" );
		tblFlexAbsences.setText( 0 , indexColMinute, "Minutes" );
		tblFlexAbsences.setText( 0 , indexColProf, "Professeur" );
		tblFlexAbsences.setText( 0 , indexColCourse, "Cours" );
		tblFlexAbsences.setText( 0 , indexColJustify, "Excusée" );
		//
		AbsenceItemProxy ai;
		for ( int row = 0; row < providerAbsences.getList().size(); row++ ) {
			ai = providerAbsences.getList().get(row);
			tblFlexAbsences.setText( row + headerRow , indexColDate, ai.getStrAbsenceDate().substring(6) + "." + ai.getStrAbsenceDate().substring(4,6) + "." + ai.getStrAbsenceDate().substring(0,4)  );
			tblFlexAbsences.setText( row + headerRow , indexColPeriod, ai.getPeriodDesc() );
			tblFlexAbsences.setText( row + headerRow , indexColType, AbsenceValues.getCodeFR( ai.getCodeAbsenceType() ) );
			tblFlexAbsences.setText( row + headerRow , indexColMinute, (ai.getLateMinutes()>0) ? Integer.toString( ai.getLateMinutes() ) : "" );
			tblFlexAbsences.setText( row + headerRow , indexColProf, ai.getProfName() );
			tblFlexAbsences.setText( row + headerRow , indexColCourse, ai.getSubjectName() );
			tblFlexAbsences.setText( row + headerRow , indexColJustify, ai.isJusttified() ? "Oui" : " " );
		}
		//
		styleTable();
	}
	
	
	/*
	 * */
	private void styleTable() {
		//		
		tblFlexAbsences.setCellSpacing(0);
		tblFlexAbsences.setCellPadding(3);		
		//
		for (int i=0; i<tblFlexAbsences.getCellCount(0); i++)
			tblFlexAbsences.getCellFormatter().setStyleName(0, i, "bulletinHeader");	
		//
		for (int i=0; i<tblFlexAbsences.getCellCount(0); i++)
			for (int j=1; j<tblFlexAbsences.getRowCount(); j++) {
				if (tblFlexAbsences.isCellPresent(j, i)) {
					if (tblFlexAbsences.getCellFormatter().getStyleName(j, i).equals(""))
						tblFlexAbsences.getCellFormatter().setStyleName(j, i, "bulletinBrancheLine");
				}
			}
	}
	
	
	
	/*
	 * */
	@UiHandler("cmdAddAbsence")
	void onCmdAddAbsenceClick(ClickEvent event) {
		//
		getUiHandlers().showAbsenceInputPopup( selectedStudentId, selectedStudentName );
	}

	
	/*
	 * */
	@Override
	public void removeDeletedAbsenceItem() {
		//
		providerAbsences.getList().remove( providerAbsences.getList().indexOf( selectedAbsenceItem ) );
		providerAbsences.flush();
		//
		selectedAbsenceItem = null;
		selectedAbsenceItemIndex = -1;
	}

	
	/*
	 * */
	@Override
	public void setStudentSuggestboxData(List<BulletinProxy> bulletins) {
		//
		providerBulletins.getList().clear();
		providerBulletins.getList().addAll( bulletins ); 
		//
		mySuggestions.clear();
		for (BulletinProxy bp : bulletins) {
			mySuggestions.add( bp.getStudentName() );
		}
	}
	
	
	/*
	 * */
	@UiHandler("cmdClosePopupSMS")
	void onCmdClosePopupSMSClick(ClickEvent event) {
		//
		popupSMSEmail.hide();
	}
	
	
	
	/*
	 * */
	@UiHandler("cmdSend")
	void onCmdSendClick(ClickEvent event) {
		//
		if ( sendMethod == messageType.Email ) {
			if ( !selectedParent.isAcceptEmail() ) {
				Window.alert( "Le responsable n'a pas choisi cette option de notification.");
				return;
			}
			if ( lstParents.getSelectedIndex() < 1 ) {
				Window.alert( NotificationValues.invalid_input + " - Parents");
				return;
			}
			getUiHandlers().sendEmail( 
					selectedAbsenceItem.getId().toString(),
					selectedAbsenceItem.getStudentName(), 
					selectedParent.getFirstName() + " " + selectedParent.getLastName(),
					selectedParent.geteMail(), 
					txtSendMessage.getText() );
		}
		
		if ( sendMethod == messageType.SMS ) {
			if ( !selectedParent.isAcceptSMS() ) {
				Window.alert( "Le responsable n'a pas choisi cette option de notification.");
				return;
			}
			if ( lstParents.getSelectedIndex() < 1 ) {
				Window.alert( NotificationValues.invalid_input + " - Parents");
				return;
			}
			getUiHandlers().sendSMS (
					selectedAbsenceItem.getId().toString(),
					selectedParent.getPhoneMobile(), 
					txtSendMessage.getText() );
		}
	}
	
	

	/*
	 * */
	@Override
	public void setParentData(List<ParentProxy> parents) {
		//
		providerParents.getList().clear();
		providerParents.getList().addAll( parents );
	}
	
	
	/*
	 * */
	public void populateParentList( messageType type ) {
		//
		lstParents.clear();
		lstParents.addItem("Choisir");
		for ( ParentProxy pp : providerParents.getList() ) {
			if ( pp.isAcceptEmail() && (type == messageType.Email) )
				lstParents.addItem( pp.getFirstName() + " " + pp.getLastName() + " - " + pp.geteMail() + " - " + pp.getPhoneMobile() );
			if ( pp.isAcceptSMS() && (type == messageType.SMS) )
				lstParents.addItem( pp.getFirstName() + " " + pp.getLastName() + " - " + pp.geteMail() + " - " + pp.getPhoneMobile() );
		}
	}
	
	
	/*
	 * */
	@UiHandler("lstParents")
	void onLstParentsChange(ChangeEvent event) {
		//
		selectedParentIndex = lstParents.getSelectedIndex() - 1;
		if ( selectedParentIndex > -1)
			selectedParent = providerParents.getList().get(selectedParentIndex);
	}

	
	/*
	 * */
	@Override
	public void showNotificationDatesEmail(AbsenceItemProxy ai) {
		//
		lblNotificationDates.setText("");
		//
		String date[] = ai.getNotificationDateEmail().split("\\|");
		String formatedDate = "";
		//
		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		builder.appendEscaped("Derniers envois :").appendHtmlConstant("<br/>");
		for (int i=0; i<date.length; i++) {
			if (date[i].length() > 0)
			formatedDate = 
					date[i].substring(6, 8) + "." +
					date[i].substring(4, 6) + "." +
					date[i].substring(0, 4) + " " +
					date[i].substring(8, 10) + ":" +
					date[i].substring(10);
			builder.appendEscaped(formatedDate).appendHtmlConstant("<br/>");
			lblNotificationDates.setHTML( builder.toSafeHtml() );
		}
	}
	
	
	/*
	 * */
	@Override
	public void showNotificationDatesSMS(AbsenceItemProxy ai) {
		//
		lblNotificationDates.setText("");
		//
		String date[] = ai.getNotificationDateSMS().split("\\|");
		String formatedDate = "";
		//
		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		builder.appendEscaped("Derniers envois :").appendHtmlConstant("<br/>");
		for (int i=0; i<date.length; i++) {
			if (date[i].length() > 0)
				formatedDate = 
					date[i].substring(6, 8) + "." +
					date[i].substring(4, 6) + "." +
					date[i].substring(0, 4) + " " +
					date[i].substring(8, 10) + ":" +
					date[i].substring(10);
			builder.appendEscaped(formatedDate).appendHtmlConstant("<br/>");
			lblNotificationDates.setHTML( builder.toSafeHtml() );
		}
	}
}
