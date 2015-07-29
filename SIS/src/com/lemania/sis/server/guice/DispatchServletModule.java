package com.lemania.sis.server.guice;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.googlecode.objectify.ObjectifyFilter;
import com.gwtplatform.dispatch.shared.ActionImpl;
import com.gwtplatform.dispatch.server.guice.DispatchServiceImpl;

public class DispatchServletModule extends ServletModule {

	@Override
	public void configureServlets() {
		//
		serve("/" + ActionImpl.DEFAULT_SERVICE_NAME)
				.with(DispatchServiceImpl.class);
		
		
		filter("/*").through(ObjectifyFilter.class);		
		bind(ObjectifyFilter.class).in(Singleton.class);
	}
}
