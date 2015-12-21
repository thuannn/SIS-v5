package com.lemania.sis.server.guice;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.googlecode.objectify.ObjectifyFilter;

public class DispatchServletModule extends ServletModule {

	@Override
	public void configureServlets() {
		//
		
// 20151221 - Upgrade to GWTP 1.5.1
//		serve("/" + ActionImpl.DEFAULT_SERVICE_NAME)
//				.with(DispatchServiceImpl.class);
		
		
		filter("/*").through(ObjectifyFilter.class);		
		bind(ObjectifyFilter.class).in(Singleton.class);
	}
}
