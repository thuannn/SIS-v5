package com.lemania.sis.client.form.supervisedstudytracking;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.lemania.sis.client.UI.FieldValidation;
import com.lemania.sis.client.UI.GridButtonCell;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.ProfessorProxy;
import com.lemania.sis.shared.coursesubscription.CourseSubscriptionProxy;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.box.MultiLinePromptMessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.info.Info;

class SupervisedStudyTrackingView extends
		ViewWithUiHandlers<SupervisedStudyTrackingUiHandlers> implements
		SupervisedStudyTrackingPresenter.MyView {

	//
	interface Binder extends UiBinder<Widget, SupervisedStudyTrackingView> {
	}

	@Inject
	SupervisedStudyTrackingView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if (slot == SupervisedStudyTrackingPresenter.SLOT_SupervisedStudyTracking) {
			//
		} else {
			super.setInSlot(slot, content);
		}
	}

	/*------------------------------*/

	//
	@UiField
	DateBox dateFrom;
	@UiField
	Button cmdFilter;
	//
	ListDataProvider<CourseSubscriptionProxy> appliedStudentsDataProvider = new ListDataProvider<CourseSubscriptionProxy>();
	@UiField(provided = true)
	DataGrid<CourseSubscriptionProxy> tblAppliedStudents = new DataGrid<CourseSubscriptionProxy>();
	//
	ListDataProvider<CourseSubscriptionProxy> studentSubscriptionsDataProvider = new ListDataProvider<CourseSubscriptionProxy>();
	@UiField(provided = true)
	DataGrid<CourseSubscriptionProxy> tblStudentSubscriptions = new DataGrid<CourseSubscriptionProxy>();
	//
	@UiField
	ListBox lstProfs;

	/*------------------------------*/

	/*
	 * */
	@Override
	public void initializeUI() {
		//
		initializeDateFields();
		//
		initializeAppliedStudentTable();
		//
		initializeStudentSubscriptionsTable();
	}

	/*
	 * */
	private void initializeDateFields() {
		//
		dateFrom.setFormat(new DateBox.DefaultFormat(DateTimeFormat
				.getFormat("dd.MM.yyyy")));
		dateFrom.addValueChangeHandler(new ValueChangeHandler<Date>() {
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
	}

	/*
	 * Initialize Student table
	 */
	public void initializeAppliedStudentTable() {
		//
		Column<CourseSubscriptionProxy, String> colStudentName = new Column<CourseSubscriptionProxy, String>(
				new TextCell()) {
			@Override
			public String getValue(CourseSubscriptionProxy object) {
				return object.getStudentName();
			}
		};
		tblAppliedStudents.addColumn(colStudentName, "Elève");
		tblAppliedStudents.setColumnWidth(colStudentName, 20, Unit.PCT);

		// Add a text column to show the name.
		Column<CourseSubscriptionProxy, String> colProf = new Column<CourseSubscriptionProxy, String>(
				new TextCell()) {
			@Override
			public String getValue(CourseSubscriptionProxy object) {
				return object.getProfessorName();
			}
		};
		tblAppliedStudents.addColumn(colProf, "Inscrit par professeur");
		tblAppliedStudents.setColumnWidth(colProf, 20, Unit.PCT);

		// Add a text column to show the name.
		Column<CourseSubscriptionProxy, String> colDate = new Column<CourseSubscriptionProxy, String>(
				new TextCell()) {
			@Override
			public String getValue(CourseSubscriptionProxy object) {
				return FieldValidation.swissDateFormat(object.getDate());
			}
		};
		tblAppliedStudents.addColumn(colDate, "Date");
		tblAppliedStudents.setColumnWidth(colDate, 100, Unit.PX);
		
		
		// Add a text column to show the name.
		Column<CourseSubscriptionProxy, String> colNote = new Column<CourseSubscriptionProxy, String>(
				new TextCell()) {
			@Override
			public String getValue(CourseSubscriptionProxy object) {
				return object.getNote1();
			}
		};
		tblAppliedStudents.addColumn(colNote, "A faire");
		

		// Add a selection model to handle user selection.
		final SingleSelectionModel<CourseSubscriptionProxy> selectionModel = new SingleSelectionModel<CourseSubscriptionProxy>();
		tblAppliedStudents.setSelectionModel(selectionModel);
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					//
					public void onSelectionChange(SelectionChangeEvent event) {
						//
						getUiHandlers().loadStudentSubscriptions(
								selectionModel.getSelectedObject());
					}
				});

		//
		appliedStudentsDataProvider.addDataDisplay(tblAppliedStudents);
	}

	/*
	 * Initialize Student table
	 */
	public void initializeStudentSubscriptionsTable() {
		//
		Column<CourseSubscriptionProxy, String> colStudentName = new Column<CourseSubscriptionProxy, String>(
				new TextCell()) {
			@Override
			public String getValue(CourseSubscriptionProxy object) {
				return object.getStudentName();
			}
		};
		tblStudentSubscriptions.setColumnWidth(colStudentName, 20, Unit.PCT);
		tblStudentSubscriptions.addColumn(colStudentName, "Elève");

		// Add a text column to show the name.
		Column<CourseSubscriptionProxy, String> colDate = new Column<CourseSubscriptionProxy, String>(
				new TextCell()) {
			@Override
			public String getValue(CourseSubscriptionProxy object) {
				return FieldValidation.swissDateFormat(object.getDate());
			}
		};
		tblStudentSubscriptions.setColumnWidth(colDate, 100, Unit.PX);
		tblStudentSubscriptions.addColumn(colDate, "Date");

		// Notes
		Column<CourseSubscriptionProxy, String> colNotes = new Column<CourseSubscriptionProxy, String>(
				new TextCell()) {
			@Override
			public String getValue(CourseSubscriptionProxy object) {
				return object.getNote();
			}
		};
		tblStudentSubscriptions.addColumn(colNotes, "Notes");
		
		
		// Prof1
		Column<CourseSubscriptionProxy, String> colProf1 = new Column<CourseSubscriptionProxy, String>(
				new TextCell()) {
			@Override
			public String getValue(CourseSubscriptionProxy object) {
				return object.getProfessor1Name();
			}
		};
		tblStudentSubscriptions.addColumn(colProf1, "Professeur");
		

		// Add a selection model to handle user selection.
		final SingleSelectionModel<CourseSubscriptionProxy> selectionModel = new SingleSelectionModel<CourseSubscriptionProxy>();
		tblStudentSubscriptions.setSelectionModel(selectionModel);
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					//
					public void onSelectionChange(SelectionChangeEvent event) {
						//
					}
				});

		// Note input
		Column<CourseSubscriptionProxy, String> colNoteInput = new Column<CourseSubscriptionProxy, String>(
				new GridButtonCell()) {
			@Override
			public String getValue(CourseSubscriptionProxy bp) {
				return "Noter";
			}
		};
		colNoteInput
				.setFieldUpdater(new FieldUpdater<CourseSubscriptionProxy, String>() {
					@Override
					public void update(int index, CourseSubscriptionProxy ps,
							String value) {
						//
						createMultiPrompt(ps);
					}
				});
		tblStudentSubscriptions.setColumnWidth(colNoteInput, 75, Unit.PX);
		tblStudentSubscriptions.addColumn(colNoteInput, "");

		//
		studentSubscriptionsDataProvider
				.addDataDisplay(tblStudentSubscriptions);
	}

	/*
	 * */
	private void createMultiPrompt(final CourseSubscriptionProxy subscription) {
		//
		if (lstProfs.getSelectedValue().equals("")) {
			(new MessageBox( "Notification", NotificationValues.invalid_input + "Professeur" )).show();
			return;
		}
		//
		final MultiLinePromptMessageBox messageBox = new MultiLinePromptMessageBox(
				"Saisir un commentaire pour " + subscription.getStudentName() + " - " + FieldValidation.swissDateFormat(subscription.getDate()), "Commentaire :");
		messageBox.getField().setValue( subscription.getNote() );
		//
		messageBox.addDialogHideHandler(new DialogHideHandler() {
			@Override
			public void onDialogHide(DialogHideEvent event) {
				if (event.getHideButton() == PredefinedButton.OK) {
					//
					getUiHandlers().saveSubscriptionNote( subscription, messageBox.getValue(), lstProfs.getSelectedValue() );
				} else {
					Info.display("Information", "Saisir un commentaire annulé");
				}
			}
		});
		messageBox.show();
	}

	/*
	 * Clicking on this buttton will load all students that have absences during
	 * the selected date range
	 */
	@UiHandler("cmdFilter")
	void onCmdFilterClick(ClickEvent event) {
		//
		loadAppliedStudentsByDate();
	}
	
	
	/*
	 * */
	@Override
	public void resetForm() {
		//
		appliedStudentsDataProvider.getList().clear();
		appliedStudentsDataProvider.flush();
		//
		studentSubscriptionsDataProvider.getList().clear();
		studentSubscriptionsDataProvider.flush();
	}
	

	/*------------------------------*/

	/*
	 * */
	@Override
	public void setProfListData(List<ProfessorProxy> profs, boolean autoSelect) {
		//
		lstProfs.clear();
		lstProfs.addItem("-", "");
		for (ProfessorProxy prof : profs)
			lstProfs.addItem(prof.getProfName(), prof.getId().toString());
		//
		// autoSelect is true if this user is a professor
		if (autoSelect && (profs.size() > 0))
			lstProfs.setSelectedIndex(1);
	}

	/*
	 * */
	@Override
	public void setAppliedStudentsTableData(List<CourseSubscriptionProxy> list) {
		//
		appliedStudentsDataProvider.getList().clear();
		appliedStudentsDataProvider.setList(list);
		appliedStudentsDataProvider.flush();
		tblAppliedStudents.setPageSize( list.size() );
	}

	/*
	 * */
	private void loadAppliedStudentsByDate() {
		//
		String fromDate = DateTimeFormat.getFormat("yyyyMMdd").format(
				dateFrom.getValue());
		getUiHandlers().loadAppliedStudentsByDate(fromDate);
	}

	/*
	 * */
	@Override
	public void setStudentSubscriptionsTableData(
			List<CourseSubscriptionProxy> list) {
		//
		studentSubscriptionsDataProvider.getList().clear();
		studentSubscriptionsDataProvider.setList(list);
		studentSubscriptionsDataProvider.flush();
		tblStudentSubscriptions.setPageSize( list.size() );
	}

	

}