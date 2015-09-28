package com.lemania.sis.client.form.subjects;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.lemania.sis.shared.SubjectProxy;
import com.lemania.sis.shared.SubjectProxy.SubjectProxyProperties;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.Change;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.event.ViewReadyEvent;
import com.sencha.gxt.widget.core.client.event.ViewReadyEvent.ViewReadyHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.DoubleField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.view.client.ListDataProvider;

public class FrmSubjectListView extends
		ViewWithUiHandlers<FrmSubjectListUiHandler> implements
		FrmSubjectListPresenter.MyView {

	private final Widget widget;

	// Thuan
	private int selectedSubject = -1;
	ListDataProvider<SubjectProxy> dataProvider = new ListDataProvider<SubjectProxy>();

	public interface Binder extends UiBinder<Widget, FrmSubjectListView> {
	}

	@Inject
	public FrmSubjectListView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	// 20150729 : Changed to GXT Grid, hide the old grid
//	@UiField(provided = true) DataGrid<SubjectProxy> tblSubjectList = new DataGrid<SubjectProxy>();
//	@UiField SimplePager pagerSubjects;
	
	@UiField FlowLayoutContainer pnlContainer;
	
	//
	ListStore<SubjectProxy> store;
	

	/*
	 * */
	@Override
	public void initializeGrid(final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<SubjectProxy>> loader) {
		//
		SubjectProxyProperties properties = GWT.create(SubjectProxyProperties.class);

		//
		store = new ListStore<SubjectProxy>(properties.getId());
		loader.addLoadHandler(
				new LoadResultListStoreBinding<FilterPagingLoadConfig, SubjectProxy, PagingLoadResult<SubjectProxy>>(store));
		
		//
		ColumnConfig<SubjectProxy, String> colName = new ColumnConfig<SubjectProxy, String>(
				properties.subjectName(), 150, "Nom");
		ColumnConfig<SubjectProxy, String> colName2 = new ColumnConfig<SubjectProxy, String>(
				properties.subjectName2(), 150, "Nom - EN");
		ColumnConfig<SubjectProxy, Double> colCoef = new ColumnConfig<SubjectProxy, Double>(
				properties.defaultCoef(), 150, "Coef");
		ColumnConfig<SubjectProxy, Boolean> colActive = new ColumnConfig<SubjectProxy, Boolean>(
				properties.isActive(), 150, "Active");

		List<ColumnConfig<SubjectProxy, ?>> columns = new ArrayList<ColumnConfig<SubjectProxy, ?>>();
		columns.add( colName );
		columns.add( colName2 );
		columns.add( colCoef );
		columns.add( colActive );

		ColumnModel<SubjectProxy> cm = new ColumnModel<SubjectProxy>(columns);

		//
		Grid<SubjectProxy> gridView = new Grid<SubjectProxy>(store, cm) {
			@Override
			protected void onAfterFirstAttach() {
				super.onAfterFirstAttach();
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					@Override
					public void execute() {
						loader.load();
					}
				});
			}
		};
		gridView.getView().setForceFit(true);
		gridView.setLoadMask(true);
		gridView.setLoader(loader);
		
		//
		// Set the size of this grid to fit the containing container
		gridView.addViewReadyHandler( new ViewReadyHandler() {

			@Override
			public void onViewReady(ViewReadyEvent event) {
				//
				event.getSource().setPixelSize( 
						pnlContainer.getParent().getOffsetWidth(), 
						pnlContainer.getParent().getOffsetHeight() - event.getSource().getAbsoluteTop() );
			}
			
		});
		
		//
		// Grid editing
		GridEditing<SubjectProxy> editing = new GridInlineEditing<SubjectProxy>( gridView );
		editing.addEditor( colName, new TextField() );
		editing.addEditor( colName2, new TextField() );
		editing.addEditor( colCoef, new DoubleField() );
		editing.addEditor( colActive, new CheckBox() );
		//
		editing.addCompleteEditHandler( new CompleteEditHandler<SubjectProxy>() {

			@Override
			public void onCompleteEdit(CompleteEditEvent<SubjectProxy> event) {
				//
				for (Store<SubjectProxy>.Record record : store.getModifiedRecords()) {
				  SubjectProxy model = record.getModel();
				  for (Change<SubjectProxy, ?> change : record.getChanges()) {
					  getUiHandlers().updateSubjectFromStore( change, model ); 
				  }
				}
			}
			
		});

		//
		final PagingToolBar toolBar = new PagingToolBar( 30 );
		toolBar.getElement().getStyle().setProperty("borderBottom", "none");
		toolBar.bind(loader);
		
		//
		pnlContainer.add(gridView);
		pnlContainer.add(toolBar);
	}
	

	/*
	 * 20150728 - Add new column for the second subject name
	 * 20150729 - Hide the old grid, changed to GXT Grid
	 */
	@Override
	public void initializeSubjectTable() {
//		//
//		// Add a text column to show the name.
//		Column<SubjectProxy, String> colSubjectName = new Column<SubjectProxy, String>(
//				new EditTextCell()) {
//			@Override
//			public String getValue(SubjectProxy object) {
//				return object.getSubjectName();
//			}
//		};
//		tblSubjectList.addColumn(colSubjectName, "Nom");
//		// Field updater
//		colSubjectName
//				.setFieldUpdater(new FieldUpdater<SubjectProxy, String>() {
//					@Override
//					public void update(int index, SubjectProxy subject,
//							String value) {
//						if (getUiHandlers() != null) {
//							selectedSubject = index;
//							getUiHandlers().updateSubject(subject, value,
//									subject.getDefaultCoef().toString(),
//									subject.getIsActive());
//						}
//					}
//				});
//
//		// Add a text column to show the name.
//		Column<SubjectProxy, String> colSubjectName2 = new Column<SubjectProxy, String>(
//				new EditTextCell()) {
//			@Override
//			public String getValue(SubjectProxy object) {
//				return object.getSubjectName2();
//			}
//		};
//		tblSubjectList.addColumn(colSubjectName2, "Nom - EN");
//		// Field updater
//		colSubjectName2
//				.setFieldUpdater(new FieldUpdater<SubjectProxy, String>() {
//					@Override
//					public void update(int index, SubjectProxy subject,
//							String value) {
//						if (getUiHandlers() != null) {
//							selectedSubject = index;
//							getUiHandlers().updateSubjectName2(subject, value);
//						}
//					}
//				});
//
//		//
//		Column<SubjectProxy, String> colCoef = new Column<SubjectProxy, String>(
//				new EditTextCell()) {
//			@Override
//			public String getValue(SubjectProxy object) {
//				return object.getDefaultCoef().toString();
//			}
//		};
//		tblSubjectList.addColumn(colCoef, "Coefficient");
//		tblSubjectList.setColumnWidth(colCoef, 20, Unit.PCT);
//		// Field updater
//		colCoef.setFieldUpdater(new FieldUpdater<SubjectProxy, String>() {
//			@Override
//			public void update(int index, SubjectProxy subject, String value) {
//				if (getUiHandlers() != null) {
//					selectedSubject = index;
//					getUiHandlers().updateSubject(subject,
//							subject.getSubjectName(), value,
//							subject.getIsActive());
//				}
//			}
//		});
//
//		//
//		CheckboxCell cellActive = new CheckboxCell();
//		Column<SubjectProxy, Boolean> colActive = new Column<SubjectProxy, Boolean>(
//				cellActive) {
//			@Override
//			public Boolean getValue(SubjectProxy subject) {
//				return subject.getIsActive();
//			}
//		};
//		tblSubjectList.addColumn(colActive, "Active");
//		tblSubjectList.setColumnWidth(colActive, 20, Unit.PCT);
//		// Field updater
//		colActive.setFieldUpdater(new FieldUpdater<SubjectProxy, Boolean>() {
//			@Override
//			public void update(int index, SubjectProxy subject, Boolean value) {
//				if (getUiHandlers() != null) {
//					selectedSubject = index;
//					getUiHandlers().updateSubject(subject,
//							subject.getSubjectName(),
//							subject.getDefaultCoef().toString(), value);
//				}
//			}
//		});
//
//		//
//		pagerSubjects.setDisplay(tblSubjectList);
//		//
//		dataProvider.addDataDisplay(tblSubjectList);
	}

	/*
	 * */
	@Override
	public void setSubjectListData(List<SubjectProxy> subjectList) {
		//
		dataProvider.setList(subjectList);
		dataProvider.flush();
	}

	/*
	 * */
	@Override
	public void refreshUpdatedSubject(SubjectProxy subject) {
		// TODO Auto-generated method stub
		dataProvider.getList().remove(selectedSubject);
		dataProvider.getList().add(selectedSubject, subject);
		dataProvider.refresh();
	}

	
	/*
	 * */
	@Override
	public void commitStoreChange( SubjectProxy updatedSubject ) {
		//
		if (store != null) {
			store.update( updatedSubject );
		}
	}
}
