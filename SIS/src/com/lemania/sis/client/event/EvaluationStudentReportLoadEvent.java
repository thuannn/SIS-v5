package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class EvaluationStudentReportLoadEvent
		extends
		GwtEvent<EvaluationStudentReportLoadEvent.EvaluationStudentReportLoadHandler> {
	
	//
	private String evaluationHeaderId = "";
	private String bulletinId = "";

	public static Type<EvaluationStudentReportLoadHandler> TYPE = new Type<EvaluationStudentReportLoadHandler>();

	public interface EvaluationStudentReportLoadHandler extends EventHandler {
		void onEvaluationStudentReportLoad(EvaluationStudentReportLoadEvent event);
	}

	public interface EvaluationStudentReportLoadHasHandlers extends HasHandlers {
		HandlerRegistration addEvaluationStudentReportLoadHandler(
				EvaluationStudentReportLoadHandler handler);
	}

	public EvaluationStudentReportLoadEvent() {
	}
	
	public EvaluationStudentReportLoadEvent(String bulletinId, String evaluationHeaderId) {
		this.bulletinId = bulletinId;
		this.evaluationHeaderId = evaluationHeaderId;
	}

	@Override
	protected void dispatch(EvaluationStudentReportLoadHandler handler) {
		handler.onEvaluationStudentReportLoad(this);
	}

	@Override
	public Type<EvaluationStudentReportLoadHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<EvaluationStudentReportLoadHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new EvaluationStudentReportLoadEvent());
	}

	public String getEvaluationHeaderId() {
		return evaluationHeaderId;
	}

	public void setEvaluationHeaderId(String evaluationHeaderId) {
		this.evaluationHeaderId = evaluationHeaderId;
	}

	public String getBulletinId() {
		return bulletinId;
	}

	public void setBulletinId(String bulletinId) {
		this.bulletinId = bulletinId;
	}
}
