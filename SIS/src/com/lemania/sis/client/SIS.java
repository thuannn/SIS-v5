package com.lemania.sis.client;

import com.google.gwt.core.client.EntryPoint;
import com.lemania.sis.client.gin.ClientGinjector;
import com.google.gwt.core.client.GWT;
import com.gwtplatform.mvp.client.DelayedBindRegistry;

public class SIS implements EntryPoint {

	public final ClientGinjector ginjector = GWT.create(ClientGinjector.class);

	@Override
	public final void onModuleLoad() {
		//
		// This is required for Gwt-Platform proxy's generator
		DelayedBindRegistry.bind(ginjector);
		//
		ginjector.getPlaceManager().revealCurrentPlace();
	}
}
