package com.lemania.sis.client.popup.periodlistpopup;

import java.util.List;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewWithUiHandlers;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.ListDataProvider;
import com.lemania.sis.shared.period.PeriodProxy;
import com.lemania.sis.shared.perioditem.PeriodItemProxy;
import com.google.gwt.user.client.ui.ListBox;

public class PeriodListPopupView extends
		PopupViewWithUiHandlers<PeriodListPopupUiHandlers> implements
		PeriodListPopupPresenter.MyView {
	public interface Binder extends UiBinder<PopupPanel, PeriodListPopupView> {
	}

	@Inject
	PeriodListPopupView(Binder uiBinder, EventBus eventBus) {
		super(eventBus);

		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiField(provided=true) CellTable<PeriodItemProxy> tblPeriods = new CellTable<PeriodItemProxy>();
	@UiField Button cmdClose;
	@UiField Button cmdAdd;
	@UiField TextBox txtNote;
	@UiField ListBox lstFromHour;
	@UiField ListBox lstFromMinute;
	@UiField ListBox lstToHour;
	@UiField ListBox lstToMinute;
	
	
	//
	ListDataProvider<PeriodItemProxy> dataPeriods = new ListDataProvider<PeriodItemProxy>();
	
	
	/*
	 * */
	@UiHandler("cmdClose")
	void onCmdCloseClick(ClickEvent event) {
		//
		getUiHandlers().hidePopup();
	}
	
	
	/*
	 * */
	@UiHandler("cmdAdd")
	void onCmdAddClick(ClickEvent event) {
		//
		this.getUiHandlers().addPeriod( 
				Integer.parseInt( lstFromHour.getValue( lstFromHour.getSelectedIndex()) ),
				Integer.parseInt( lstFromMinute.getValue( lstFromMinute.getSelectedIndex()) ),
				Integer.parseInt( lstToHour.getValue( lstToHour.getSelectedIndex()) ),
				Integer.parseInt( lstToMinute.getValue( lstToMinute.getSelectedIndex()) ),
				txtNote.getText(), true );
	}


	/*
	 * */
	@Override
	public void addNewPeriodItem(PeriodItemProxy pi) {
		//
		dataPeriods.getList().add(pi);
		dataPeriods.flush();
	}


	@Override
	public void initializeUI() {
		//
		lstFromHour.clear();
		lstToHour.clear();
		for (int i=1; i<25; i++) {
			lstFromHour.addItem( Integer.toString(i), Integer.toString(i));
			lstToHour.addItem( Integer.toString(i), Integer.toString(i));
		}
		//
		lstFromMinute.clear();
		lstToMinute.clear();
		for (int i=0; i<61; i++) {
			lstFromMinute.addItem( Integer.toString(i), Integer.toString(i));
			lstToMinute.addItem( Integer.toString(i), Integer.toString(i));
		}
		//
		initializeTable();
	}


	/*
	 * */
	private void initializeTable() {
		//
	    Column<PeriodItemProxy, String> colFromHour = new Column<PeriodItemProxy, String>(new EditTextCell()) {
		      @Override
		      public String getValue(PeriodItemProxy object) {
		        return Integer.toString( object.getFromHour() );
		      }
	    };
	    tblPeriods.addColumn(colFromHour, "De");
	    //
	    Column<PeriodItemProxy, String> colFromMinute = new Column<PeriodItemProxy, String>(new EditTextCell()) {
		      @Override
		      public String getValue(PeriodItemProxy object) {
		        return Integer.toString( object.getFromMinute() );
		      }
	    };
	    tblPeriods.addColumn(colFromMinute, "");
	    //
	    Column<PeriodItemProxy, String> colToHour = new Column<PeriodItemProxy, String>(new EditTextCell()) {
		      @Override
		      public String getValue(PeriodItemProxy object) {
		        return Integer.toString( object.getToHour() );
		      }
	    };
	    tblPeriods.addColumn(colToHour, "A");
	    //
	    Column<PeriodItemProxy, String> colToMinute = new Column<PeriodItemProxy, String>(new EditTextCell()) {
		      @Override
		      public String getValue(PeriodItemProxy object) {
		        return Integer.toString( object.getToMinute() );
		      }
	    };
	    tblPeriods.addColumn(colToMinute, "");
	    //
	    Column<PeriodItemProxy, String> colNote = new Column<PeriodItemProxy, String>(new EditTextCell()) {
		      @Override
		      public String getValue(PeriodItemProxy object) {
		        return object.getNote();
		      }
	    };
	    tblPeriods.addColumn(colNote, "");
	    // 
	    CheckboxCell cellActive = new CheckboxCell();
	    Column<PeriodItemProxy, Boolean> colActive = new Column<PeriodItemProxy, Boolean>(cellActive) {
	    	@Override
	    	public Boolean getValue(PeriodItemProxy period){
	    		return period.isActive();
	    	}	    	
	    };
	    tblPeriods.addColumn(colActive, "Active");
	    //
		dataPeriods.addDataDisplay( tblPeriods );
	}


	/*
	 * */
	@Override
	public void resetUI() {
		//
		lstFromHour.setSelectedIndex(0);
		lstFromMinute.setSelectedIndex(0);
		lstToHour.setSelectedIndex(0);
		lstToMinute.setSelectedIndex(0);
		txtNote.setText("");
	}


	/*
	 * */
	@Override
	public void setPeriodData(List<PeriodItemProxy> periods) {
		//
		dataPeriods.getList().clear();
		dataPeriods.setList(periods);
		dataPeriods.flush();
	}
}
