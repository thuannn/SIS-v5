//package com.lemania.sis.client.event;
//
//import com.google.gwt.event.shared.GwtEvent;
//import com.google.gwt.event.shared.EventHandler;
//import com.google.gwt.event.shared.HasHandlers;
//import com.google.gwt.event.shared.HandlerRegistration;
//
//public class PageAfterSelectEvent extends
//		GwtEvent<PageAfterSelectEvent.PageAfterSelectHandler> {
//	//
//	private String tokenName = "";
//
//	public static Type<PageAfterSelectHandler> TYPE = new Type<PageAfterSelectHandler>();
//
//	public interface PageAfterSelectHandler extends EventHandler {
//		void onPageAfterSelect(PageAfterSelectEvent event);
//	}
//
//	public interface PageAfterSelectHasHandlers extends HasHandlers {
//		HandlerRegistration addPageAfterSelectHandler(PageAfterSelectHandler handler);
//	}
//
//	public PageAfterSelectEvent() {
//	}
//	
//	public PageAfterSelectEvent(String tokenName) {
//		this.tokenName = tokenName;
//	}
//
//	@Override
//	protected void dispatch(PageAfterSelectHandler handler) {
//		handler.onPageAfterSelect(this);
//	}
//
//	@Override
//	public Type<PageAfterSelectHandler> getAssociatedType() {
//		return TYPE;
//	}
//
//	public static Type<PageAfterSelectHandler> getType() {
//		return TYPE;
//	}
//
//	public static void fire(HasHandlers source) {
//		source.fireEvent(new PageAfterSelectEvent());
//	}
//	
//	public String getTokenName() {
//		return this.tokenName;
//	}
//}
