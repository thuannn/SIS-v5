package com.lemania.sis.client.form.dataexport;

import javax.inject.Inject;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

class DataExportView extends ViewWithUiHandlers<DataExportUiHandlers> implements DataExportPresenter.MyView {
    interface Binder extends UiBinder<Widget, DataExportView> {
    }

    @Inject
    DataExportView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    @Override
    public void setInSlot(Object slot, IsWidget content) {
//        if (slot == DataExportPresenter.SLOT_DataExport) {
//            main.setWidget(content);
//        } else {
//            super.setInSlot(slot, content);
//        }
    }
}