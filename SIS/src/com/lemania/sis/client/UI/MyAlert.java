package com.lemania.sis.client.UI;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MyAlert extends DialogBox {

	public MyAlert(String msg) {
		
		// Set title
		setText("Alert");

		// Enable animation.
		setAnimationEnabled(true);

		// Enable glass background.
		setGlassEnabled(true);

		// Set modal
		setModal(true);

		// DialogBox is a SimplePanel, so you have to set its widget property to
		// whatever you want its contents to be.
		Button ok = new Button("OK");
		ok.setStyleName("gridButton");
		ok.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				MyAlert.this.hide();
			}
		});
		
		// Label for message
		Label lbl = new Label( msg );
		lbl.setStyleName("txtAlert");

		// Vertical Panel
		VerticalPanel vp = new VerticalPanel();
		vp.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
		vp.setSpacing(10);
		//
		vp.add(lbl);
		vp.add(ok);

		//
		setWidget(vp);
	}
}