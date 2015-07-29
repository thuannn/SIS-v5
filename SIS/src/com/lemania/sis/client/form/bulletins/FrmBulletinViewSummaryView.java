package com.lemania.sis.client.form.bulletins;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.ClasseProxy;
import com.lemania.sis.shared.bulletin.BulletinProxy;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectProxy;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class FrmBulletinViewSummaryView extends ViewWithUiHandlers<FrmBulletinViewSummaryUiHandler> implements
		FrmBulletinViewSummaryPresenter.MyView {

	private final Widget widget;
	
	
	// Thuan
	List<BulletinProxy> bulletins = new ArrayList<BulletinProxy>();
	List<BulletinSubjectProxy> curSubjects = new ArrayList<BulletinSubjectProxy>();
	List<ClasseProxy> classes;
	

	public interface Binder extends UiBinder<Widget, FrmBulletinViewSummaryView> {
	}

	@Inject
	public FrmBulletinViewSummaryView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	@UiField ListBox lstClasses;
	@UiField ListBox lstBulletins;
	@UiField FlexTable tblNotes;
	@UiField Label lblStudentName;
	@UiField Label lblBulletinTitle;
	@UiField Label lblClassName;
	@UiField VerticalPanel pnlBulletin;
	@UiField Button cmdPrint;
	@UiField VerticalPanel pnlMain;
	@UiField VerticalPanel pnlMainBulletin;
	@UiField TextArea txtDirectionRemarque;
	@UiField Button cmdSaveRemarques;
	@UiField Image imgLogo;
	@UiField Label txtAddress1;
	@UiField Label txtAddress2;
	@UiField Label lblYear;
	@UiField Label lblDate;
	@UiField VerticalPanel pnlBulletinNotes;
	@UiField HTML lblConditionMatu;
	@UiField HTML lblConditionES;
	@UiField HTML lblConditionBac;
	@UiField ListBox lstModels;
	@UiField AbsolutePanel pnlWhiteBackground;
	@UiField HorizontalPanel pnlLogo;
	@UiField Grid pnlDescription;
	@UiField VerticalPanel pnlRemarque;
	
	
	/*
	 * */
	@Override
	public void resetForm() {
		//
		lstClasses.setSelectedIndex(0);
		lstBulletins.clear();
		//
		initializeModelList();				
	}

	/*
	 * */
	private void initializeModelList() {
		//
		lstModels.clear();
		lstModels.addItem("Logo - Ecole Lémania", "lemania");
		lstModels.addItem("Logo - Pierre Viret", "pierreviret");
		lstModels.addItem("Baccalauréat Français", "bacfrancais");
//		lstModels.addItem("Baccalauréat Français - Bac Blanc uniquement", "bacblanc");
		lstModels.addItem("Baccalauréat Français - Bac Blanc T1", "bacblanct1");
		lstModels.addItem("Baccalauréat Français - Bac Blanc T2", "bacblanct2");
		lstModels.addItem("Baccalauréat Français - Bac Blanc T3", "bacblanct3");
		lstModels.addItem("Prématurité - T1, T2, T3, T4", "prematurite");
		lstModels.addItem("Prématurité - T1, T2", "prematurite12");
		lstModels.addItem("Prématurité - T3, T4", "prematurite34");
		lstModels.addItem("Maturité Suisse", "maturitesuisse");
		lstModels.setSelectedIndex(0);
	}

	/*
	 * */
	@Override
	public void setClasseList(List<ClasseProxy> classes) {
		//
		this.classes = classes;
		//
		lstClasses.clear();
		lstClasses.addItem("-","");
		for (ClasseProxy clazz : classes) {
			lstClasses.addItem( clazz.getClassName(), clazz.getId().toString());
		}
	}
	
	
	/*
	 * */
	@UiHandler("lstClasses")
	void onLstClassesChange(ChangeEvent event) {
		//
		if (getUiHandlers() != null)
			getUiHandlers().onClassChange(lstClasses.getValue(lstClasses.getSelectedIndex()));
	}

	/*
	 * */
	@Override
	public void setStudentListData(List<BulletinProxy> bulletins) {
		//
		if (this.bulletins != null)
			this.bulletins.clear();
		this.bulletins.addAll(bulletins);
		//
		lstBulletins.clear();
		lstBulletins.addItem("-","");
		for (BulletinProxy bulletin : bulletins) {
			lstBulletins.addItem( bulletin.getStudentName() + " - " + bulletin.getClasseName() + " - " + bulletin.getYear(), 
					bulletin.getId().toString());
		}
	}
	
	
	/*
	 * */
	@UiHandler("lstBulletins")
	void onLstBulletinsChange(ChangeEvent event) {
		if (getUiHandlers() != null) {
			lblStudentName.setText( bulletins.get(lstBulletins.getSelectedIndex()-1).getStudentName());
			lblClassName.setText( lstClasses.getItemText(lstClasses.getSelectedIndex()));
			lblYear.setText("Année scolaire : " 
						+ bulletins.get(lstBulletins.getSelectedIndex()-1).getYear() + "-" 
						+ (Integer.parseInt(bulletins.get(lstBulletins.getSelectedIndex()-1).getYear())+1));
			//
			getUiHandlers().onBulletinChange( lstBulletins.getValue(lstBulletins.getSelectedIndex()));
		}
	}

	
	/*
	 * */
	private void initializeTableMatu() {
		//
		tblNotes.removeAllRows();
		//
		tblNotes.setText(0, 0, "Groupe");
		tblNotes.setText(0, 1, "Coef");
		tblNotes.setText(0, 2, "T1");
		tblNotes.setText(0, 3, "Exa. T1");
		tblNotes.setText(0, 4, "T2");
		tblNotes.setText(0, 5, "Exa. Semes.");
		tblNotes.setText(0, 6, "Moyenne Semes.");
		tblNotes.setText(0, 7, "Remarques relatives à la période d'évaluation");
		//
		lblConditionMatu.setVisible(true);
		lblConditionES.setVisible(false);
		lblConditionBac.setVisible(false);
		//
		lblBulletinTitle.setText("BULLETIN DE NOTES");
	}
	
	
	/*
	 * */
	private void initializeBacTable() {
		//
		tblNotes.removeAllRows();
		//
		tblNotes.setText(0, 0, "Groupe");
		tblNotes.setText(0, 1, "Coef");
		tblNotes.setText(0, 2, "T1");
		tblNotes.setText(0, 3, "Bac Blanc/DS");
		tblNotes.setText(0, 4, "T2");
		tblNotes.setText(0, 5, "Bac Blanc/DS");
		tblNotes.setText(0, 6, "T3");
		tblNotes.setText(0, 7, "Bac Blanc/DS");
		tblNotes.setText(0, 8, "Moyenne Annuelle");
		tblNotes.setText(0, 9, "Remarques relatives à la période d'évaluation");
		tblNotes.getRowFormatter().setStyleName(0, "bulletinHeader");
		//
		lblConditionMatu.setVisible(false);
		lblConditionES.setVisible(false);
		lblConditionBac.setVisible(true);
		//
		lblBulletinTitle.setText("BULLETIN DE NOTES");
	}
	
	
	/*
	 * */
	private void initializeESTable() {
		//
		tblNotes.removeAllRows();
		//
		tblNotes.setText(0, 0, "Groupe");
		tblNotes.setText(0, 1, "Coef");
		tblNotes.setText(0, 2, "T1");
		tblNotes.setText(0, 3, "T2");
		tblNotes.setText(0, 4, "T3");
		tblNotes.setText(0, 5, "Moyenne Annuelle");
		tblNotes.getRowFormatter().setStyleName(0, "bulletinHeader");
		//
		lblConditionMatu.setVisible(false);
		lblConditionES.setVisible(true);
		lblConditionBac.setVisible(false);
		//
		lblBulletinTitle.setText("BULLETIN DE NOTES");
	}
	
	
	/*
	 * */
	@Override
	public void drawBulletinSubjectList(List<BulletinSubjectProxy> subjects) {
		//
		if (classes.get(lstClasses.getSelectedIndex()-1).getProgrammeName().toLowerCase().contains("matu")) {
			if ( classes.get(lstClasses.getSelectedIndex()-1).getClassName().toLowerCase().contains("prématurité") )
				drawPrematurite( subjects, false );
			else
				drawMatuBulletin( subjects, false );
		}
		//
		if (classes.get(lstClasses.getSelectedIndex()-1).getProgrammeName().toLowerCase().contains("bacc"))
			drawBacBulletin( subjects, false );
		//
		if (classes.get(lstClasses.getSelectedIndex()-1).getProgrammeName().toLowerCase().contains("second"))
			drawESBulletin( subjects );
		//
		onLstModelsChange( null );
	}
	
	
	/*
	 * */
	private void drawPrematurite(List<BulletinSubjectProxy> subjects, boolean redraw) {
		//
		if (!redraw) {
			curSubjects.clear();
			curSubjects.addAll(subjects);
		}
		//
		initializePrematurite();
		//
		Integer rowStart = 1;
		Integer rowCount = 0;		
		Double totalMoyenne = 0.0;
		Double totalCoef = 0.0;
		//
		for (int i = rowStart; i< (subjects.size()+rowStart); i++) {
			tblNotes.setText(i, 0, subjects.get( rowCount ).getSubjectName());
			tblNotes.setText(i, 1, subjects.get( rowCount ).getSubjectCoef().toString());
			tblNotes.setText(i, 2, subjects.get( rowCount ).getT1().toString());
			tblNotes.setText(i, 3, subjects.get( rowCount ).getExamT1().toLowerCase());
			tblNotes.setText(i, 4, subjects.get( rowCount ).getT2().toString());
			tblNotes.setText(i, 5, subjects.get( rowCount ).getExamT2().toString());
			tblNotes.setText(i, 6, subjects.get( rowCount ).getT3().toString());
			tblNotes.setText(i, 7, subjects.get( rowCount ).getExamT3().toString());
			tblNotes.setText(i, 8, subjects.get( rowCount ).getT4().toString());
			tblNotes.setText(i, 9, subjects.get( rowCount ).getExamT4().toString());
			tblNotes.setText(i, 10, subjects.get( rowCount ).getAn());
			tblNotes.setText(i, 11, (
					(!subjects.get(rowCount).getT3().equals("") || !subjects.get(rowCount).getExamT3().equals("")) ? subjects.get(rowCount).getRemarqueT3()
							: ( (!subjects.get(rowCount).getT2().equals("") || !subjects.get(rowCount).getExamT2().equals("")) ? subjects.get(rowCount).getRemarqueT2()
									: subjects.get(rowCount).getRemarqueT1() ) ) );		
			//
			if ( !subjects.get( rowCount ).getAn().isEmpty() ){
				totalMoyenne = totalMoyenne + Double.parseDouble(subjects.get( rowCount ).getAn()) * subjects.get( rowCount ).getSubjectCoef();
				totalCoef = totalCoef + subjects.get( rowCount ).getSubjectCoef();
			}
			//
			rowCount++;
		}
				
		//
		rowCount++;
		tblNotes.setText(rowCount, 0, "Moyenne :");
		tblNotes.setText(rowCount, 1, totalCoef.toString());
		tblNotes.setText(rowCount, 2, "");
		tblNotes.setText(rowCount, 3, "");
		tblNotes.setText(rowCount, 4, "");
		tblNotes.setText(rowCount, 5, "");
		tblNotes.setText(rowCount, 6, "");
		tblNotes.setText(rowCount, 7, "");
		tblNotes.setText(rowCount, 8, "");
		tblNotes.setText(rowCount, 9, "");
		tblNotes.setText(rowCount, 10, String.valueOf((double)Math.round(totalMoyenne/totalCoef*10)/10));
		tblNotes.setText(rowCount, 11, "");
		for (int i=0; i<tblNotes.getCellCount(rowCount); i++)
			tblNotes.getCellFormatter().setStyleName(rowCount, i, "subjectLine");
		
		//
		txtDirectionRemarque.setText( bulletins.get(lstBulletins.getSelectedIndex()-1).getRemarqueDirection() );
		
		//
		styleBacTable();
	}
	
	
	/**/
	private void drawPrematurite12() {
		//
		if (curSubjects.isEmpty())
			return;
		//
		List<BulletinSubjectProxy> subjects = new ArrayList<BulletinSubjectProxy>();
		subjects.addAll(curSubjects);
		//
		initializePrematurite12();
		//
		Integer rowStart = 1;
		Integer rowCount = 0;		
		Double totalMoyenne = 0.0;
		Double totalCoef = 0.0;
		//
		for (int i = rowStart; i< (subjects.size()+rowStart); i++) {
			tblNotes.setText(i, 0, subjects.get( rowCount ).getSubjectName());
			tblNotes.setText(i, 1, subjects.get( rowCount ).getSubjectCoef().toString());
			tblNotes.setText(i, 2, subjects.get( rowCount ).getT1().toString());
			tblNotes.setText(i, 3, subjects.get( rowCount ).getExamT1().toLowerCase());
			tblNotes.setText(i, 4, subjects.get( rowCount ).getT2().toString());
			tblNotes.setText(i, 5, subjects.get( rowCount ).getExamT2().toString());			
			tblNotes.setText(i, 6, subjects.get( rowCount ).getAn());
			tblNotes.setText(i, 7, (										
					(!subjects.get(rowCount).getT3().equals("") || !subjects.get(rowCount).getExamT3().equals("")) ? subjects.get(rowCount).getRemarqueT3()
							: ( (!subjects.get(rowCount).getT2().equals("") || !subjects.get(rowCount).getExamT2().equals("")) ? subjects.get(rowCount).getRemarqueT2()
									: subjects.get(rowCount).getRemarqueT1() ) ) );	
			//
			if ( !subjects.get( rowCount ).getAn().isEmpty() ){
				totalMoyenne = totalMoyenne + Double.parseDouble(subjects.get( rowCount ).getAn()) * subjects.get( rowCount ).getSubjectCoef();
				totalCoef = totalCoef + subjects.get( rowCount ).getSubjectCoef();
			}
			//
			rowCount++;
		}
				
		//
		rowCount++;
		tblNotes.setText(rowCount, 0, "Moyenne :");
		tblNotes.setText(rowCount, 1, totalCoef.toString());
		tblNotes.setText(rowCount, 2, "");
		tblNotes.setText(rowCount, 3, "");
		tblNotes.setText(rowCount, 4, "");
		tblNotes.setText(rowCount, 5, "");
		tblNotes.setText(rowCount, 6, String.valueOf((double)Math.round(totalMoyenne/totalCoef*10)/10));
		tblNotes.setText(rowCount, 7, "");
		for (int i=0; i<tblNotes.getCellCount(rowCount); i++)
			tblNotes.getCellFormatter().setStyleName(rowCount, i, "subjectLine");
		
		//
		txtDirectionRemarque.setText( bulletins.get(lstBulletins.getSelectedIndex()-1).getRemarqueDirection() );
		
		//
		styleBacTable();
	}
	
	
	/**/
	private void drawPrematurite34() {
		//
		if (curSubjects.isEmpty())
			return;
		//
		List<BulletinSubjectProxy> subjects = new ArrayList<BulletinSubjectProxy>();
		subjects.addAll(curSubjects);
		//
		initializePrematurite34();
		//
		Integer rowStart = 1;
		Integer rowCount = 0;		
		Double totalMoyenne = 0.0;
		Double totalCoef = 0.0;
		//
		for (int i = rowStart; i< (subjects.size()+rowStart); i++) {
			tblNotes.setText(i, 0, subjects.get( rowCount ).getSubjectName());
			tblNotes.setText(i, 1, subjects.get( rowCount ).getSubjectCoef().toString());
			tblNotes.setText(i, 2, subjects.get( rowCount ).getT3().toString());
			tblNotes.setText(i, 3, subjects.get( rowCount ).getExamT3().toString());
			tblNotes.setText(i, 4, subjects.get( rowCount ).getT4().toString());
			tblNotes.setText(i, 5, subjects.get( rowCount ).getExamT4().toString());
			tblNotes.setText(i, 6, subjects.get( rowCount ).getAn());
			tblNotes.setText(i, 7, (
					(!subjects.get(rowCount).getT3().equals("") || !subjects.get(rowCount).getExamT3().equals("")) ? subjects.get(rowCount).getRemarqueT3()
							: ( (!subjects.get(rowCount).getT2().equals("") || !subjects.get(rowCount).getExamT2().equals("")) ? subjects.get(rowCount).getRemarqueT2()
									: subjects.get(rowCount).getRemarqueT1() ) ) );		
			//
			if ( !subjects.get( rowCount ).getAn().isEmpty() ){
				totalMoyenne = totalMoyenne + Double.parseDouble(subjects.get( rowCount ).getAn()) * subjects.get( rowCount ).getSubjectCoef();
				totalCoef = totalCoef + subjects.get( rowCount ).getSubjectCoef();
			}
			//
			rowCount++;
		}
				
		//
		rowCount++;
		tblNotes.setText(rowCount, 0, "Moyenne :");
		tblNotes.setText(rowCount, 1, totalCoef.toString());
		tblNotes.setText(rowCount, 2, "");
		tblNotes.setText(rowCount, 3, "");
		tblNotes.setText(rowCount, 4, "");		
		tblNotes.setText(rowCount, 5, "");
		tblNotes.setText(rowCount, 6, String.valueOf((double)Math.round(totalMoyenne/totalCoef*10)/10));
		tblNotes.setText(rowCount, 7, "");
		for (int i=0; i<tblNotes.getCellCount(rowCount); i++)
			tblNotes.getCellFormatter().setStyleName(rowCount, i, "subjectLine");
		
		//
		txtDirectionRemarque.setText( bulletins.get(lstBulletins.getSelectedIndex()-1).getRemarqueDirection() );
		
		//
		styleBacTable();
	}
	

	/*
	 * */
	private void initializePrematurite() {	
		//
		tblNotes.removeAllRows();
		//
		tblNotes.setText(0, 0, "Groupe");
		tblNotes.setText(0, 1, "Coef");
		tblNotes.setText(0, 2, "T1");
		tblNotes.setText(0, 3, "Examen");
		tblNotes.setText(0, 4, "T2");
		tblNotes.setText(0, 5, "Examen");
		tblNotes.setText(0, 6, "T3");
		tblNotes.setText(0, 7, "Examen");
		tblNotes.setText(0, 8, "T4");
		tblNotes.setText(0, 9, "Examen");
		tblNotes.setText(0, 10, "Moyenne Annuelle");
		tblNotes.setText(0, 11, "Remarques relatives à la période d'évaluation");
		tblNotes.getRowFormatter().setStyleName(0, "bulletinHeader");
		//
		lblConditionMatu.setVisible(true);
		lblConditionES.setVisible(false);
		lblConditionBac.setVisible(false);
		//
		lblBulletinTitle.setText("BULLETIN DE NOTES");
	}
	
	
	/**/
	private void initializePrematurite12() {	
		//
		tblNotes.removeAllRows();
		//
		tblNotes.setText(0, 0, "Groupe");
		tblNotes.setText(0, 1, "Coef");
		tblNotes.setText(0, 2, "T1");
		tblNotes.setText(0, 3, "Examen");
		tblNotes.setText(0, 4, "T2");
		tblNotes.setText(0, 5, "Examen");		
		tblNotes.setText(0, 6, "Moyenne Annuelle");
		tblNotes.setText(0, 7, "Remarques relatives à la période d'évaluation");
		//
		tblNotes.getRowFormatter().setStyleName(0, "bulletinHeader");
		//
		lblConditionMatu.setVisible(true);
		lblConditionES.setVisible(false);
		lblConditionBac.setVisible(false);
		//
		lblBulletinTitle.setText("BULLETIN DE NOTES");
	}
	
	
	/**/
	private void initializePrematurite34() {	
		//
		tblNotes.removeAllRows();
		//
		tblNotes.setText(0, 0, "Groupe");
		tblNotes.setText(0, 1, "Coef");		
		tblNotes.setText(0, 2, "T3");
		tblNotes.setText(0, 3, "Examen");
		tblNotes.setText(0, 4, "T4");
		tblNotes.setText(0, 5, "Examen");
		tblNotes.setText(0, 6, "Moyenne Annuelle");
		tblNotes.setText(0, 7, "Remarques relatives à la période d'évaluation");
		//
		tblNotes.getRowFormatter().setStyleName(0, "bulletinHeader");
		//
		lblConditionMatu.setVisible(true);
		lblConditionES.setVisible(false);
		lblConditionBac.setVisible(false);
		//
		lblBulletinTitle.setText("BULLETIN DE NOTES");
	}
	

	/**/
	private void drawBacBulletin(List<BulletinSubjectProxy> subjects, boolean isRedraw) {
		//
		if (!isRedraw) {
			curSubjects.clear();
			curSubjects.addAll(subjects);
		}
		//
		initializeBacTable();
		//
		Integer rowStart = 1;
		Integer rowCount = 0;		
		Double totalMoyenne = 0.0;
		Double totalCoef = 0.0;
		//
		Double totalMoyenneT1 = 0.0;
		Double totalCoefT1 = 0.0;
		//
		Double totalMoyenneT2 = 0.0;
		Double totalCoefT2 = 0.0;
		//
		Double totalMoyenneT3 = 0.0;
		Double totalCoefT3 = 0.0;
		//
		for (int i = rowStart; i< (subjects.size()+rowStart); i++) {
			tblNotes.setText(i, 0, subjects.get( rowCount ).getSubjectName());
			tblNotes.setText(i, 1, subjects.get( rowCount ).getSubjectCoef().toString());
			tblNotes.setText(i, 2, subjects.get( rowCount ).getT1().toString());
			tblNotes.setText(i, 3, subjects.get( rowCount ).getExamT1().toLowerCase());
			tblNotes.setText(i, 4, subjects.get( rowCount ).getT2().toString());
			tblNotes.setText(i, 5, subjects.get( rowCount ).getExamT2().toString());
			tblNotes.setText(i, 6, subjects.get( rowCount ).getT3().toString());
			tblNotes.setText(i, 7, subjects.get( rowCount ).getExamT3().toString());
			tblNotes.setText(i, 8, subjects.get( rowCount ).getAn());
			tblNotes.setText(i, 9, (
					(!subjects.get(rowCount).getT3().equals("") || !subjects.get(rowCount).getExamT3().equals("")) ? subjects.get(rowCount).getRemarqueT3()
							: ( (!subjects.get(rowCount).getT2().equals("") || !subjects.get(rowCount).getExamT2().equals("")) ? subjects.get(rowCount).getRemarqueT2()
									: subjects.get(rowCount).getRemarqueT1() ) ) );		
			//
			if ( !subjects.get( rowCount ).getAn().isEmpty() ){
				//
				totalMoyenne = totalMoyenne + Double.parseDouble(subjects.get( rowCount ).getAn()) * subjects.get( rowCount ).getSubjectCoef();
				totalCoef = totalCoef + subjects.get( rowCount ).getSubjectCoef();
				//
				if ( !subjects.get( rowCount ).getT1().isEmpty() ) {
					totalMoyenneT1 = totalMoyenneT1 + Double.parseDouble(subjects.get( rowCount ).getT1()) * subjects.get( rowCount ).getSubjectCoef();
					totalCoefT1 = totalCoefT1 + subjects.get( rowCount ).getSubjectCoef();
				}
				if ( !subjects.get( rowCount ).getT2().isEmpty() ) {
					totalMoyenneT2 = totalMoyenneT2 + Double.parseDouble(subjects.get( rowCount ).getT2()) * subjects.get( rowCount ).getSubjectCoef();
					totalCoefT2 = totalCoefT2 + subjects.get( rowCount ).getSubjectCoef();
				}
				if ( !subjects.get( rowCount ).getT3().isEmpty() ) {
					totalMoyenneT3 = totalMoyenneT3 + Double.parseDouble(subjects.get( rowCount ).getT3()) * subjects.get( rowCount ).getSubjectCoef();
					totalCoefT3 = totalCoefT3 + subjects.get( rowCount ).getSubjectCoef();
				}
			}
			//
			rowCount++;
		}
				
		//
		rowCount++;
		tblNotes.setText(rowCount, 0, "Moyenne :");
		tblNotes.setText(rowCount, 1, totalCoef.toString());
		tblNotes.setText(rowCount, 2, (totalMoyenneT1>0)? String.valueOf((double)Math.round(totalMoyenneT1/totalCoefT1*10)/10) : "");
		tblNotes.setText(rowCount, 3, "");
		tblNotes.setText(rowCount, 4, (totalMoyenneT2>0)? String.valueOf((double)Math.round(totalMoyenneT2/totalCoefT2*10)/10) : "");
		tblNotes.setText(rowCount, 5, "");
		tblNotes.setText(rowCount, 6, (totalMoyenneT3>0)? String.valueOf((double)Math.round(totalMoyenneT3/totalCoefT3*10)/10) : "");
		tblNotes.setText(rowCount, 7, "");
		tblNotes.setText(rowCount, 8, String.valueOf((double)Math.round(totalMoyenne/totalCoef*10)/10));
		tblNotes.setText(rowCount, 9, "");
		for (int i=0; i<tblNotes.getCellCount(rowCount); i++)
			tblNotes.getCellFormatter().setStyleName(rowCount, i, "subjectLine");
		
		//
		txtDirectionRemarque.setText( bulletins.get(lstBulletins.getSelectedIndex()-1).getRemarqueDirection() );
		
		//
		styleBacTable();		
	}
	
	
	/*
	 * */
	private void drawESBulletin(List<BulletinSubjectProxy> subjects) {
		//
		initializeESTable();
		//
		Integer rowStart = 1;
		Integer rowCount = 0;
		Double totalMoyenne = 0.0;
		Double totalCoef = 0.0;
		//
		Double totalMoyenneT1 = 0.0;
		Double totalCoefT1 = 0.0;
		//
		Double totalMoyenneT2 = 0.0;
		Double totalCoefT2 = 0.0;
		//
		Double totalMoyenneT3 = 0.0;
		Double totalCoefT3 = 0.0;
		//
		Integer applicationComportementSubject = -1;
		//
		for (int i = rowStart; i< (subjects.size()+rowStart); i++) {
			if (subjects.get(rowCount).getSubjectName().toLowerCase().contains("application") &&
					subjects.get(rowCount).getSubjectName().toLowerCase().contains("comportement") ) {
				applicationComportementSubject = rowCount;
				rowCount++;
				continue;
			}
			
			tblNotes.setText(i, 0, subjects.get( rowCount ).getSubjectName());
			tblNotes.setText(i, 1, subjects.get( rowCount ).getSubjectCoef().toString());
			tblNotes.setText(i, 2, subjects.get( rowCount ).getT1().toString());
			tblNotes.setText(i, 3, subjects.get( rowCount ).getT2().toString());
			tblNotes.setText(i, 4, subjects.get( rowCount ).getT3().toString());
			tblNotes.setText(i, 5, subjects.get( rowCount ).getAn());
			//
			if ( !subjects.get( rowCount ).getAn().isEmpty() ){
				//
				totalMoyenne = totalMoyenne + Double.parseDouble(subjects.get( rowCount ).getAn()) * subjects.get( rowCount ).getSubjectCoef();
				totalCoef = totalCoef + subjects.get( rowCount ).getSubjectCoef();
				//
				if ( !subjects.get( rowCount ).getT1().isEmpty() ) {
					totalMoyenneT1 = totalMoyenneT1 + Double.parseDouble(subjects.get( rowCount ).getT1()) * subjects.get( rowCount ).getSubjectCoef();
					totalCoefT1 = totalCoefT1 + subjects.get( rowCount ).getSubjectCoef();
				}
				if ( !subjects.get( rowCount ).getT2().isEmpty() ) {
					totalMoyenneT2 = totalMoyenneT2 + Double.parseDouble(subjects.get( rowCount ).getT2()) * subjects.get( rowCount ).getSubjectCoef();
					totalCoefT2 = totalCoefT2 + subjects.get( rowCount ).getSubjectCoef();
				}
				if ( !subjects.get( rowCount ).getT3().isEmpty() ) {
					totalMoyenneT3 = totalMoyenneT3 + Double.parseDouble(subjects.get( rowCount ).getT3()) * subjects.get( rowCount ).getSubjectCoef();
					totalCoefT3 = totalCoefT3 + subjects.get( rowCount ).getSubjectCoef();
				}
			}
			//
			rowCount++;
		}
		//
		if (applicationComportementSubject > -1) {
			//
			tblNotes.setText(subjects.size()+rowStart, 0, subjects.get( applicationComportementSubject ).getSubjectName());
			tblNotes.setText(subjects.size()+rowStart, 1, subjects.get( applicationComportementSubject ).getSubjectCoef().toString());
			tblNotes.setText(subjects.size()+rowStart, 2, subjects.get( applicationComportementSubject ).getT1().toString());
			tblNotes.setText(subjects.size()+rowStart, 3, subjects.get( applicationComportementSubject ).getT2().toString());
			tblNotes.setText(subjects.size()+rowStart, 4, subjects.get( applicationComportementSubject ).getT3().toString());
			tblNotes.setText(subjects.size()+rowStart, 5, subjects.get( applicationComportementSubject ).getAn());
			//
			if ( !subjects.get( applicationComportementSubject ).getAn().isEmpty() ){
				totalMoyenne = totalMoyenne + Double.parseDouble(subjects.get( applicationComportementSubject ).getAn()) * subjects.get( applicationComportementSubject ).getSubjectCoef();
				totalCoef = totalCoef + subjects.get( applicationComportementSubject ).getSubjectCoef();
				//
				if ( !subjects.get( applicationComportementSubject ).getT1().isEmpty() ) {
					totalMoyenneT1 = totalMoyenneT1 + Double.parseDouble(subjects.get( applicationComportementSubject ).getT1()) * subjects.get( applicationComportementSubject ).getSubjectCoef();
					totalCoefT1 = totalCoefT1 + subjects.get( applicationComportementSubject ).getSubjectCoef();
				}
				if ( !subjects.get( applicationComportementSubject ).getT2().isEmpty() ) {
					totalMoyenneT2 = totalMoyenneT2 + Double.parseDouble(subjects.get( applicationComportementSubject ).getT2()) * subjects.get( applicationComportementSubject ).getSubjectCoef();
					totalCoefT2 = totalCoefT2 + subjects.get( applicationComportementSubject ).getSubjectCoef();
				}
				if ( !subjects.get( applicationComportementSubject ).getT3().isEmpty() ) {
					totalMoyenneT3 = totalMoyenneT3 + Double.parseDouble(subjects.get( applicationComportementSubject ).getT3()) * subjects.get( applicationComportementSubject ).getSubjectCoef();
					totalCoefT3 = totalCoefT3 + subjects.get( applicationComportementSubject ).getSubjectCoef();
				}
			}
			//
			rowCount++;
		}
		
		//
		rowCount++;
		tblNotes.setText(rowCount, 0, "Moyenne :");
		tblNotes.setText(rowCount, 1, totalCoef.toString());
		tblNotes.setText(rowCount, 2, (totalMoyenneT1>0)? String.valueOf((double)Math.round(totalMoyenneT1/totalCoefT1*10)/10) : "");
		tblNotes.setText(rowCount, 3, (totalMoyenneT2>0)? String.valueOf((double)Math.round(totalMoyenneT2/totalCoefT2*10)/10) : "");
		tblNotes.setText(rowCount, 4, (totalMoyenneT3>0)? String.valueOf((double)Math.round(totalMoyenneT3/totalCoefT3*10)/10) : "");
		tblNotes.setText(rowCount, 5, String.valueOf((double)Math.round(totalMoyenne/totalCoef*10)/10));
		for (int i=0; i<tblNotes.getCellCount(rowCount); i++)
			tblNotes.getCellFormatter().setStyleName(rowCount, i, "subjectLine");
		
		//
		txtDirectionRemarque.setText( bulletins.get(lstBulletins.getSelectedIndex()-1).getRemarqueDirection() );
		
		//
		styleESTable();
	}
	

	/*
	 * */
	public void drawMatuBulletin( List<BulletinSubjectProxy> subjects, boolean isRedraw ) {
		//
		if ( !isRedraw ) {
			curSubjects.clear();
			curSubjects.addAll(subjects);
		}
		//
		initializeTableMatu();
		//
		Integer rowStart = 1;
		Integer rowCount = 0;
		Double totalMoyenne = 0.0;
		Double totalCoef = 0.0;
		//
		for (int i = rowStart; i< (subjects.size()+rowStart); i++) {
			tblNotes.setText(i, 0, subjects.get( rowCount ).getSubjectName());
			tblNotes.setText(i, 1, subjects.get( rowCount ).getSubjectCoef().toString());
			tblNotes.setText(i, 2, subjects.get( rowCount ).getT1().toString());
			tblNotes.setText(i, 3, subjects.get( rowCount ).getExamT1().toLowerCase());
			tblNotes.setText(i, 4, subjects.get( rowCount ).getT2().toString());
			tblNotes.setText(i, 5, subjects.get( rowCount ).getExamT2().toString());
			tblNotes.setText(i, 6, subjects.get( rowCount ).getAn());
			tblNotes.setText(i, 7, (
					(!subjects.get(rowCount).getT3().equals("") || !subjects.get(rowCount).getExamT3().equals("")) ? subjects.get(rowCount).getRemarqueT3()
							: ( (!subjects.get(rowCount).getT2().equals("") || !subjects.get(rowCount).getExamT2().equals("")) ? subjects.get(rowCount).getRemarqueT2()
									: subjects.get(rowCount).getRemarqueT1() ) ) );
			//
			if ( !subjects.get( rowCount ).getAn().isEmpty() ){
				totalMoyenne = totalMoyenne + Double.parseDouble(subjects.get( rowCount ).getAn()) * subjects.get( rowCount ).getSubjectCoef();
				totalCoef = totalCoef + subjects.get( rowCount ).getSubjectCoef();
			}
			//
			rowCount++;
		}
		
		//
		rowCount++;
		tblNotes.setText(rowCount, 0, "Moyenne :");
		tblNotes.setText(rowCount, 1, totalCoef.toString());
		tblNotes.setText(rowCount, 2, "");
		tblNotes.setText(rowCount, 3, "");
		tblNotes.setText(rowCount, 4, "");
		tblNotes.setText(rowCount, 5, "");
		tblNotes.setText(rowCount, 6, String.valueOf((double)Math.round(totalMoyenne/totalCoef*10)/10));
		tblNotes.setText(rowCount, 7, "");
		for (int i=0; i<tblNotes.getCellCount(rowCount); i++)
			tblNotes.getCellFormatter().setStyleName(rowCount, i, "subjectLine");
		//
		txtDirectionRemarque.setText( bulletins.get(lstBulletins.getSelectedIndex()-1).getRemarqueDirection() );
		
		//
		styleTableMatu();
	}

	
	/*
	 * */
	private void styleTableMatu() {
		//		
		tblNotes.setCellSpacing(0);
		tblNotes.setCellPadding(3);
		//
		for (int i=1; i<tblNotes.getCellCount(0)-1; i++)
			tblNotes.getCellFormatter().setStyleName(0, i, "bulletinHeaderNote");
		tblNotes.getCellFormatter().setStyleName(0, 0, "bulletinHeader");
		tblNotes.getCellFormatter().setStyleName(0, tblNotes.getCellCount(0)-1, "bulletinHeader");		
		//
		for (int i=0; i<tblNotes.getCellCount(0); i++) {
			for (int j=1; j<tblNotes.getRowCount(); j++) {
				if (tblNotes.isCellPresent(j, i)) {
					if (tblNotes.getCellFormatter().getStyleName(j, i).equals(""))
						tblNotes.getCellFormatter().setStyleName(j, i, "bulletinBrancheLine");
				}				
			}
		}
	}
	
	
	/*
	 * */
	private void styleBacTable() {
		//		
		tblNotes.setCellSpacing(0);
		tblNotes.setCellPadding(3);		
		//
		for (int i=1; i<tblNotes.getCellCount(0)-1; i++)
			tblNotes.getCellFormatter().setStyleName(0, i, "bulletinHeaderNote");
		tblNotes.getCellFormatter().setStyleName(0, 0, "bulletinHeader");
		tblNotes.getCellFormatter().setStyleName(0, tblNotes.getCellCount(0)-1, "bulletinHeader");		
		//
		for (int i=0; i<tblNotes.getCellCount(0); i++)
			for (int j=1; j<tblNotes.getRowCount(); j++) {
				if (tblNotes.isCellPresent(j, i)) {
					if (tblNotes.getCellFormatter().getStyleName(j, i).equals(""))
						tblNotes.getCellFormatter().setStyleName(j, i, "bulletinBrancheLine");
				}
			}
	}
	
	
	/*
	 * */
	private void styleESTable() {
		//		
		tblNotes.setCellSpacing(0);
		tblNotes.setCellPadding(3);
		//
		for (int i=1; i<tblNotes.getCellCount(0)-1; i++)
			tblNotes.getCellFormatter().setStyleName(0, i, "bulletinHeaderNote");
		tblNotes.getCellFormatter().setStyleName(0, 0, "bulletinHeader");
		tblNotes.getCellFormatter().setStyleName(0, tblNotes.getCellCount(0)-1, "bulletinHeader");
		//
		for (int i=0; i<6; i++)
			for (int j=1; j<tblNotes.getRowCount(); j++) {
				if (tblNotes.isCellPresent(j, i)) {
					if (tblNotes.getCellFormatter().getStyleName(j, i).equals(""))
						tblNotes.getCellFormatter().setStyleName(j, i, "bulletinBrancheLine");
				}
			}
	}
		
	
	/*
	 * */
	@UiHandler("cmdPrint")
	void onCmdPrintClick(ClickEvent event) {
		// 		
		pnlBulletin.setVisible(true);
		// For Internet Explorer only
		distributeLineHeight();
		//
		PopupPanel popup = new PopupPanel(true) {
			@Override
			  protected void onPreviewNativeEvent(final NativePreviewEvent event) {
			    super.onPreviewNativeEvent(event);
			    switch (event.getTypeInt()) {
			        case Event.ONKEYDOWN:
			            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
			                hide();
			            }
			            break;
			    }
			}
		};
		popup.setStyleName("whitePanel");
		popup.add( pnlBulletin );
		//
		pnlWhiteBackground.setHeight( pnlBulletin.getOffsetHeight() + "px");
		pnlWhiteBackground.setVisible(true);
		//
		popup.addCloseHandler(new CloseHandler<PopupPanel>() {
			public void onClose(CloseEvent<PopupPanel> event) {
				pnlMainBulletin.add(pnlBulletin);
				pnlBulletinNotes.setHeight("100%");
				//
				pnlWhiteBackground.setVisible(false);
			}
		});
		//
		popup.show();
	}
	
	
	/*
	 * */
	private void distributeLineHeight() {
		//
		int margin = 
				NotificationValues.bulletinPageHeight 
				- pnlRemarque.getOffsetHeight()
				- pnlLogo.getOffsetHeight()
				- pnlDescription.getOffsetHeight();
		tblNotes.setHeight( margin + "px" );
	}
	
	
	/*
	 * */
	@UiHandler("cmdSaveRemarques")
	void onCmdSaveRemarquesClick(ClickEvent event) {
		if (this.getUiHandlers() != null)
			this.getUiHandlers().saveBulletinRemarques( lstBulletins.getValue(lstBulletins.getSelectedIndex()), txtDirectionRemarque.getText() );
	}

	
	/*
	 * */
	@Override
	public void saveRemarqueDirection(BulletinProxy bp) {
		//
		bulletins.set( (lstBulletins.getSelectedIndex()-1), bp);
	}

	
	/*
	 * */
	@Override
	public void drawPierreViretInterface() {
		// 
		imgLogo.setUrl("/images/logo_pv.jpg");
		txtAddress1.setText("College Pierre Viret - Chemin des Cèdres 3, 1004 Lausanne");
		txtAddress2.setText("Tél.: + 41 21 643 77 07 - Fax: + 41 21 643 77 08 - E-mail: info@pierreviret.ch");
	}

	
	/*
	 * */
	@Override
	public void drawDate(String date) {
		//
		lblDate.setText(date);
	}
	
	/*
	 * */
	@UiHandler("lstModels")
	void onLstModelsChange(ChangeEvent event) {
		//
		if (lstModels.getValue(lstModels.getSelectedIndex()).equals("lemania")){
			imgLogo.setUrl("/images/logo_lemania.png");
			txtAddress1.setText("Chemin de Préville 3 - CP 550, 1003 Lausanne, Suisse - Tel.: +41 21 320 15 01 - Fax.: +41 312 67 00");
			txtAddress2.setText("Email: info@lemania.ch - Site internet : www.lemania.ch");
		}
		
		//
		if (lstModels.getValue(lstModels.getSelectedIndex()).equals("pierreviret")){
			imgLogo.setUrl("/images/logo_pv.jpg");
			txtAddress1.setText("Chemin des Cèdres 3, 1004 Lausanne, Suisse - Tel.: + 41 21 643 77 07 - Fax.: + 41 21 643 77 08");
			txtAddress2.setText("Email: info@pierreviret.ch - Site internet : www.pierreviret.ch");
		}
		
		//
		if (lstModels.getValue(lstModels.getSelectedIndex()).equals("prematurite12")){
			drawPrematurite12();
		}
		//
		if (lstModels.getValue(lstModels.getSelectedIndex()).equals("prematurite34")){
			drawPrematurite34();
		}
		//
		if (lstModels.getValue(lstModels.getSelectedIndex()).equals("prematurite")){
			drawPrematurite(curSubjects, true);
		}
		
		//
		// Bac Blanc
//		if (lstModels.getValue(lstModels.getSelectedIndex()).equals("bacblanc")){
//			drawBacBlanc(curSubjects, 0);
//		}
		// T1
		if (lstModels.getValue(lstModels.getSelectedIndex()).equals("bacblanct1")){
			drawBacBlanc(curSubjects, 1);
		}
		// T2
		if (lstModels.getValue(lstModels.getSelectedIndex()).equals("bacblanct2")){
			drawBacBlanc(curSubjects, 2);
		}
		// T3
		if (lstModels.getValue(lstModels.getSelectedIndex()).equals("bacblanct3")){
			drawBacBlanc(curSubjects, 3);
		}
		
		//
		if (lstModels.getValue(lstModels.getSelectedIndex()).equals("bacfrancais")){
			drawBacBulletin(curSubjects, true);
		}
		
		//
		if (lstModels.getValue(lstModels.getSelectedIndex()).equals("maturitesuisse")){
			drawMatuBulletin(curSubjects, true);
		}
	}

	
	
	/*
	 * Show bulletins with only bac blanc notes
	 * */
	private void drawBacBlanc(List<BulletinSubjectProxy> subjects, int quarter) {
		//
		initializeBacTable();
		//
		lblBulletinTitle.setText("BACCALAUREAT BLANC");
		//
		Integer rowStart = 1;
		Integer rowCount = 0;
		Double totalCoef = 0.0;
		//
		Double totalMoyenneT1 = 0.0;
		Double totalCoefT1 = 0.0;
		//
		Double totalMoyenneT2 = 0.0;
		Double totalCoefT2 = 0.0;
		//
		Double totalMoyenneT3 = 0.0;
		Double totalCoefT3 = 0.0;
		// 
		// Only show the examen notes
		for (int i = rowStart; i< (subjects.size()+rowStart); i++) {
			//
			tblNotes.setText(i, 0, subjects.get( rowCount ).getSubjectName());
			tblNotes.setText(i, 1, subjects.get( rowCount ).getSubjectCoef().toString());
			tblNotes.setText(i, 2, "" );
			tblNotes.setText(i, 3, (quarter == 1 ) ? subjects.get( rowCount ).getExamT1().toLowerCase() : "");
			tblNotes.setText(i, 4, "" );
			tblNotes.setText(i, 5, (quarter == 2 ) ? subjects.get( rowCount ).getExamT2().toString() : "");
			tblNotes.setText(i, 6, "" );
			tblNotes.setText(i, 7, (quarter == 3 ) ? subjects.get( rowCount ).getExamT3().toString() : "");
			tblNotes.setText(i, 8, "" );
			tblNotes.setText(i, 9, ( "" )) ;
/* No comment for bac blanc report */
//					(!subjects.get(rowCount).getT3().equals("") || !subjects.get(rowCount).getExamT3().equals("")) ? subjects.get(rowCount).getRemarqueT3()
//							: ( (!subjects.get(rowCount).getT2().equals("") || !subjects.get(rowCount).getExamT2().equals("")) ? subjects.get(rowCount).getRemarqueT2()
//									: subjects.get(rowCount).getRemarqueT1() ) ) );		
			//
			totalCoef = totalCoef + subjects.get( rowCount ).getSubjectCoef();
			//
			if ( !subjects.get( rowCount ).getExamT1().isEmpty() ) {
				totalMoyenneT1 = totalMoyenneT1 + Double.parseDouble(subjects.get( rowCount ).getExamT1()) * subjects.get( rowCount ).getSubjectCoef();
				totalCoefT1 = totalCoefT1 + subjects.get( rowCount ).getSubjectCoef();
			}
			if ( !subjects.get( rowCount ).getExamT2().isEmpty() ) {
				totalMoyenneT2 = totalMoyenneT2 + Double.parseDouble(subjects.get( rowCount ).getExamT2()) * subjects.get( rowCount ).getSubjectCoef();
				totalCoefT2 = totalCoefT2 + subjects.get( rowCount ).getSubjectCoef();
			}
			if ( !subjects.get( rowCount ).getExamT3().isEmpty() ) {
				totalMoyenneT3 = totalMoyenneT3 + Double.parseDouble(subjects.get( rowCount ).getExamT3()) * subjects.get( rowCount ).getSubjectCoef();
				totalCoefT3 = totalCoefT3 + subjects.get( rowCount ).getSubjectCoef();
			}
			//
			rowCount++;
		}
		//
		rowCount++;
		tblNotes.setText(rowCount, 0, "Moyenne :");
		tblNotes.setText(rowCount, 1, (quarter == 1 ) ? totalCoefT1.toString() : 
			(quarter == 2 ) ? totalCoefT2.toString() :
				(quarter == 3 ) ? totalCoefT3.toString() : "" );
		//
		tblNotes.setText(rowCount, 2, "");
		tblNotes.setText(rowCount, 3, (quarter == 1 ) ? ((totalMoyenneT1>0)? String.valueOf((double)Math.round(totalMoyenneT1/totalCoefT1*10)/10) : "") : "");
		tblNotes.setText(rowCount, 4, "");
		tblNotes.setText(rowCount, 5, (quarter == 2 ) ? ((totalMoyenneT2>0)? String.valueOf((double)Math.round(totalMoyenneT2/totalCoefT2*10)/10) : "") : "");
		tblNotes.setText(rowCount, 6, "");
		tblNotes.setText(rowCount, 7, (quarter == 3 ) ? ((totalMoyenneT3>0)? String.valueOf((double)Math.round(totalMoyenneT3/totalCoefT3*10)/10) : "") : "");
		tblNotes.setText(rowCount, 8, "");
		tblNotes.setText(rowCount, 9, "");
		for (int i=0; i<tblNotes.getCellCount(rowCount); i++)
			tblNotes.getCellFormatter().setStyleName(rowCount, i, "subjectLine");
		//
		txtDirectionRemarque.setText( bulletins.get(lstBulletins.getSelectedIndex()-1).getRemarqueDirection() );
		//
		styleBacTable();
		
		// Hide columns base on the selected quarter
		for (int row = 0; row < tblNotes.getRowCount(); row++ ) {
			if (quarter == 3) {
				tblNotes.getFlexCellFormatter().setVisible(row, 2, false);
				tblNotes.getFlexCellFormatter().setVisible(row, 3, false);
				tblNotes.getFlexCellFormatter().setVisible(row, 4, false);
				tblNotes.getFlexCellFormatter().setVisible(row, 5, false);
				tblNotes.getFlexCellFormatter().setVisible(row, 6, false);
			}
			if (quarter == 2) {
				tblNotes.getFlexCellFormatter().setVisible(row, 2, false);
				tblNotes.getFlexCellFormatter().setVisible(row, 3, false);
				tblNotes.getFlexCellFormatter().setVisible(row, 4, false);
				tblNotes.getFlexCellFormatter().setVisible(row, 6, false);
				tblNotes.getFlexCellFormatter().setVisible(row, 7, false);
			}
			if (quarter == 1) {
				tblNotes.getFlexCellFormatter().setVisible(row, 2, false);
				tblNotes.getFlexCellFormatter().setVisible(row, 4, false);
				tblNotes.getFlexCellFormatter().setVisible(row, 5, false);
				tblNotes.getFlexCellFormatter().setVisible(row, 6, false);
				tblNotes.getFlexCellFormatter().setVisible(row, 7, false);
			}
		}
	}
}
