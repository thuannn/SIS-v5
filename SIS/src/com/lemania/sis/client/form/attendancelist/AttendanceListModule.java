package com.lemania.sis.client.form.attendancelist;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class AttendanceListModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		bindPresenter(AttendanceListPresenter.class,
				AttendanceListPresenter.MyView.class, AttendanceListView.class,
				AttendanceListPresenter.MyProxy.class);
	}
}
