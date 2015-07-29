package com.lemania.sis.client.UI;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.lemania.sis.shared.masteragendaitem.MasterAgendaItemProxy;

public class AgendaVerticalPanel extends VerticalPanel {
	//
	private MasterAgendaItemProxy mai;
	private int rowIndex;
	private int cellIndex;

	public MasterAgendaItemProxy getMai() {
		return mai;
	}

	public void setMai(MasterAgendaItemProxy mai) {
		this.mai = mai;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public int getCellIndex() {
		return cellIndex;
	}

	public void setCellIndex(int cellIndex) {
		this.cellIndex = cellIndex;
	}	
	
	
}
