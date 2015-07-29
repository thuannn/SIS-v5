package com.lemania.sis.client.form.evaluations;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.EvaluationSubjectProxy;

public interface FrmEvaluationInputUiHandler extends UiHandlers {
	//
	void onProfessorSelected( String profId );
	//
	void onAssignmentSelected(String assignmentId);
	//
	void onLstEvaluationHeaderSelected( String profId, String assignmentId, String evaluationHeaderId );
	//
	void updateEvaluation(EvaluationSubjectProxy es, String value, int order);
}
