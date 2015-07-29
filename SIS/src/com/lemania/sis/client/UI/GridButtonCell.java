package com.lemania.sis.client.UI;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class GridButtonCell extends ButtonCell {
	
	//
	@Override
	public void render(final Context context, final SafeHtml data,
			final SafeHtmlBuilder sb) {
		sb.appendHtmlConstant("<button type=\"button\" class=\"gridButton\" tabindex=\"-1\">");
		if (data != null) {
			sb.append(data);
		}
		sb.appendHtmlConstant("</button>");
	}
	
}
