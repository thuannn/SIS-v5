package com.lemania.sis.client.form.mainpage;

import java.util.Iterator;

import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lemania.sis.client.values.AppSettingValues;
import com.lemania.sis.client.values.NotificationValues;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.lemania.sis.client.CurrentUser;
import com.sencha.gxt.core.client.Style.LayoutRegion;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.TreeItem;

public class MainPageView extends ViewWithUiHandlers<MainPageUiHandler> implements MainPagePresenter.MyView {
	
	interface MainPageViewUiBinder extends UiBinder<Widget, MainPageView>{}
	private static MainPageViewUiBinder uiBinder = GWT.create(MainPageViewUiBinder.class);

	private final Widget widget;
	@UiField FlowLayoutContainer mainContentPanel;
	@UiField Hyperlink cmdLogout;
	@UiField Label txtWelcome;
	@UiField Label lblCurrentMonth;
//	@UiField Button cmdMenuToggle;
	@UiField Tree treeMenuAdmin;
// 	@UiField DockPanel dockPanel;
	@UiField Tree treeMenuProf;
	@UiField Tree treeMenuEleve;
	@UiField Tree treeMenuParent;
	@UiField HTML htmlProgressBar;
	@UiField Hyperlink cmdProfileManagement;
	@UiField Hyperlink cmdAttribution;
	@UiField Hyperlink cmdAttributionPerson;
	@UiField Hyperlink cmdHome;
	@UiField Hyperlink cmdMarkInput;
	@UiField Hyperlink cmdMarkView;
	@UiField Hyperlink cmdStudentList;
	@UiField Hyperlink cmdStudentAdd;
	@UiField Hyperlink cmdProfList;
	@UiField Hyperlink cmdProfAdd;
	@UiField Hyperlink cmdBrancheList;
	@UiField Hyperlink cmdBrancheAdd;
	@UiField Hyperlink cmdSubjectList;
	@UiField Hyperlink cmdSubjectAdd;
	@UiField Hyperlink cmdClassList;
	@UiField Hyperlink cmdClassAdd;
	@UiField Hyperlink cmdProgrammeList;
	@UiField Hyperlink cmdProgrammeAdd;
	@UiField Hyperlink cmdSchoolList;
	@UiField Hyperlink cmdSchoolAdd;
	@UiField Hyperlink cmdUserManagement;
	@UiField Hyperlink cmdSettings;
	@UiField Hyperlink cmdPassword;
	@UiField Hyperlink cmdHelp;
	@UiField Hyperlink cmdBulletin;
	@UiField VerticalPanel pnlProgressBarIn;
	@UiField VerticalPanel pnlProgressBar;
	@UiField Hyperlink cmdMarkInputProf;
	@UiField Hyperlink cmdMarkViewProf;
	@UiField Hyperlink cmdPasswordProf;
	@UiField Hyperlink cmdHelpProf;
	@UiField Hyperlink cmdMarkViewStudent;
	@UiField Hyperlink cmdPasswordStudent;
	@UiField Hyperlink cmdHelpStudent;
	@UiField Image imgLogo;
	@UiField Label txtCopyright;
	@UiField Hyperlink cmdEvaluationList;
	@UiField Hyperlink cmdEvaluationInputProf;
	@UiField Hyperlink cmdEvaluationInputEleve;
	@UiField Hyperlink cmdEvaluationInputProfProf;
	@UiField Hyperlink cmdEvaluationInputEleveProf;
	@UiField Hyperlink cmdClassroomPage;
	@UiField Hyperlink cmdMasterAgendaPage;
	@UiField Hyperlink cmdPeriodMgt;
	@UiField Hyperlink cmdStudentAgendaPage;
	@UiField Hyperlink cmdClassAgendaPage;
	@UiField Hyperlink cmdProfessorAgendaPage;
	@UiField Hyperlink cmdParentMgt;
	@UiField Hyperlink cmdMotifAbsence;
	@UiField Hyperlink cmdAttendanceListProf;
	@UiField Hyperlink cmdAbsenceMgt;
	@UiField VerticalPanel leftPanel;
	@UiField TreeItem tiAbsences;
	@UiField TreeItem tiTimetable;
	@UiField TreeItem tiParamAbsences;
	@UiField TreeItem tiAbsencesProf;
	@UiField Hyperlink cmdMarkViewParent;
	@UiField Hyperlink cmdAbsencesViewParent;
	@UiField Hyperlink cmdPasswordParent;
	@UiField Hyperlink cmdHelpParent;
	@UiField Hyperlink cmdAbsencesViewProf;
	@UiField Hyperlink cmdStudyLogMgt;
	@UiField Hyperlink cmdStudyLogMgtProf;
	@UiField Hyperlink cmdStudyLogStudent;
	@UiField Hyperlink cmdStudyLogStudentStudent;
	@UiField Hyperlink cmdStudyLogStudentParent;
	
	@UiField BorderLayoutContainer con;
	@UiField FlowLayoutContainer conWest;
//	@UiField SimpleContainer conCenter;
	
	Hyperlink currentSelectedItem;
	
	
	// Thuan
	PopupPanel popup = new PopupPanel(false);
	
		
	/*
	 * */
	public MainPageView() {		
		widget = uiBinder.createAndBindUi(this);
	}
	
	/*
	 * */
	public interface Binder extends UiBinder<Widget, MainPageView> {
	}

	/*
	 * */
	@Override
	public Widget asWidget() {
		return widget;
	}
	
	/*
	 * */
	@Override
	public void setInSlot(Object slot, IsWidget content){
		if (slot == MainPagePresenter.TYPE_SetMainContent) {
			setMainContent(content);
		} else {
			super.setInSlot(slot, content);
		}
	}
	
	private void setMainContent(IsWidget content) {
	    mainContentPanel.clear();
	    
	    if (content != null) {
	      mainContentPanel.add(content);
	    }
	}

	@Override
	public void showUserInfo(CurrentUser currentUser) {
		//
		txtWelcome.setText("Vous êtes connecté(e), " + currentUser.getFullName() + " !");
		cmdLogout.setText("Déconnexion");
		//
		lblCurrentMonth.setText( "L'année scolaire : " + AppSettingValues.schoolYearDescription );
	}

	@Override
	public void initializeUi(CurrentUser currentUser) {
		//
		if ( currentUser!=null && currentUser.isLoggedIn() ) {
			//
			showUserInfo(currentUser);
			//		
			if (currentUser.isAdmin())
				showAdminMenu();
			if (currentUser.isProf())
				showProfMenu();
			if (currentUser.isStudent())
				showStudentMenu();
			if ( currentUser.isParent() )
				showParentMenu();
		}
		else {
			txtWelcome.setText("");
			cmdLogout.setText("");
			lblCurrentMonth.setText("");
			//
			hideMenu();
		}
		//
		hideDevFunctions();
		//
		// Set scrollbar for WEST and CENTER
		conWest.setScrollMode( ScrollMode.AUTO );
		mainContentPanel.setScrollMode( ScrollMode.AUTO );
	}
	
	
	/*
	 * */
	private void hideDevFunctions() {
		//
	}
	

	/*
	 * */
	void showAdminMenu(){
		//
//		dockPanel.add( leftPanel, DockPanel.WEST );
//		dockPanel.setCellWidth( leftPanel, "250px" );
		leftPanel.setVisible(true);
		//
		// 20141110 - After showing the Parent menu, remove completely the others
		addMenuToDom( treeMenuAdmin );
		removeMenuFromDom( treeMenuParent );
		removeMenuFromDom( treeMenuProf );
		removeMenuFromDom( treeMenuEleve );
		//
//		cmdMenuToggle.setVisible(true);
		//
		con.show( LayoutRegion.WEST );
	}
	
	/*
	 * */
	void showProfMenu(){
		//
//		dockPanel.add( leftPanel, DockPanel.WEST );
//		dockPanel.setCellWidth( leftPanel, "250px" );
		leftPanel.setVisible(true);
		//
		// 20141110 - After showing the Parent menu, remove completely the others
		addMenuToDom( treeMenuProf );
		removeMenuFromDom( treeMenuAdmin );
		removeMenuFromDom( treeMenuParent );
		removeMenuFromDom( treeMenuEleve );
		//
//		cmdMenuToggle.setVisible(true);
		//
		con.show( LayoutRegion.WEST );
	}
	
	
	/*
	 * */
	void showStudentMenu(){
		//
//		dockPanel.add( leftPanel, DockPanel.WEST );
//		dockPanel.setCellWidth( leftPanel, "250px" );
		leftPanel.setVisible(true);
		//
		// 20141110 - After showing the Parent menu, remove completely the others
		addMenuToDom( treeMenuEleve );
		removeMenuFromDom( treeMenuAdmin );
		removeMenuFromDom( treeMenuParent );
		removeMenuFromDom( treeMenuProf );
		//
//		cmdMenuToggle.setVisible(true);
		//
		con.show( LayoutRegion.WEST );
	}
	
	
	/*
	 * */
	void showParentMenu(){
		//
//		dockPanel.add( leftPanel, DockPanel.WEST );
//		dockPanel.setCellWidth( leftPanel, "250px" );
		leftPanel.setVisible(true);
		//
		// 20141110 - After showing the Parent menu, remove completely the others
		addMenuToDom( treeMenuParent );
		removeMenuFromDom( treeMenuAdmin );
		removeMenuFromDom( treeMenuProf );
		removeMenuFromDom( treeMenuEleve );
		//
//		cmdMenuToggle.setVisible(true);
		con.show( LayoutRegion.WEST );
	}
	
	
	/*
	 * Remove completely the menu from DOM so that one cannot inspect using developer tool
	 * */
	private void removeMenuFromDom( Tree menu) {
		// 
		menu.setVisible(false);
		leftPanel.remove( menu );
	}
	
	/*
	 * Remove completely the menu from DOM so that one cannot inspect using developer tool
	 * */
	private void addMenuToDom( Tree menu) {
		// 
		menu.setVisible(true);
		leftPanel.add( menu );
	}
	

	/*
	 * */
	void hideMenu(){
		//
//		dockPanel.remove( leftPanel );
		//
		leftPanel.setVisible(false);
		treeMenuProf.setVisible( false );
		treeMenuEleve.setVisible( false );
		treeMenuAdmin.setVisible( false );
		treeMenuParent.setVisible(false);
		//
//		cmdMenuToggle.setVisible(false);
		//
		// Collapse the West pane
		con.hide( LayoutRegion.WEST );
	}

	/*
	 * */
	private void showPanel() {
		//
//		dockPanel.add( leftPanel, DockPanel.WEST );
//		dockPanel.setCellWidth( leftPanel, "250px" );
		leftPanel.setVisible(true);
	}

	/*
	 * */
	private void hidePanel() {
		//
		leftPanel.setVisible(false);
//		dockPanel.remove( leftPanel );
	}

	
	/*
	 * */
	@Override
	public void showProgressBar(boolean visible) {
		//
		if (!pnlProgressBar.getParent().equals(popup))
			popup.add(pnlProgressBar);
		//
		pnlProgressBar.setVisible(visible);
		popup.setSize( Integer.toString(Window.getClientWidth()) + "px", Integer.toString(Window.getClientHeight()) + "px" );
		popup.setStyleName("progressBarPopup");
		if (visible)
			popup.center();
		else {
			popup.hide();
		}
	}

	
	/*
	 * */
	@Override
	public void enableMainPanel(Boolean enabled) {
		//
		enableAllChildren(enabled, mainContentPanel);
	}
	
	
	/**/
	private void enableAllChildren(boolean enable, Widget widget)
	{
	    if (widget instanceof HasWidgets)
	    {
	        Iterator<Widget> iter = ((HasWidgets)widget).iterator();
	        while (iter.hasNext())
	        {
	            Widget nextWidget = iter.next();
	            enableAllChildren(enable, nextWidget);
	            if (nextWidget instanceof FocusWidget)
	            {
	                ((FocusWidget)nextWidget).setEnabled(enable);
	            }
	        }
	    }
	}

	

	/*
	 * Change school logo base on currently selected School in the settings
	 * 20150728 - Add EBSR
	 * */
	@Override
	public void drawSchoolInterface(String schoolCode) {
		//
		if (schoolCode.equals(NotificationValues.pierreViret)) {
			imgLogo.setUrl("images/logo-pv.png");
			txtCopyright.setText("Copyright © Pierre Viret -");
		}
		if (schoolCode.equals(NotificationValues.ecoleLemania)) {
			imgLogo.setUrl("images/logo.png");
			txtCopyright.setText("Copyright © Ecole Lémania -");
		}
		if (schoolCode.equals(NotificationValues.lemaniacollegelaussane)) {
			imgLogo.setUrl("images/logo-lcl.png");
			txtCopyright.setText("Copyright © Lemania College Lausanne -");
		}
		if (schoolCode.equals( NotificationValues.ebsr )) {
			imgLogo.setUrl("images/logo_ebsr.png");
			txtCopyright.setText("Copyright © Ecole Bilingue de Suisse Romande -");
		}
	}
	
	
	/*
	 * 21.07.2015 - with GXT no need this button
	 * */
//	@UiHandler("cmdMenuToggle")
//	void onCmdMenuToggleClick(ClickEvent event) {
//		//
//		if ( leftPanel.isVisible() )
//			hidePanel();
//		else
//			showPanel();
//	}
	
	
	/*
	 * */
	@UiHandler("cmdLogout")
	void onCmdLogoutClick(ClickEvent event) {
		if (getUiHandlers() != null) {			
			getUiHandlers().logOut();
		}
	}
	

	/*
	 * */
	@Override
	public void setWindowEventHanlder() {
		//
//		Window.addResizeHandler( new ResizeHandler() {
//
//			@Override
//			public void onResize(ResizeEvent event) {
//				//
//				leftPanel.setHeight( Window.getClientHeight() - pnlNorth.getOffsetHeight() - 20 + "px");
//			}
//			
//		});
	}
	
	
	/*
	 * Hight light the selected item
	 * */
	public void switchButton(Hyperlink button) {
		//
		if (currentSelectedItem != null)
			currentSelectedItem.setStyleName("");
		//
		button.setStyleName("currentPage");
		currentSelectedItem = button;
	}
	
	/*
	 * */
	@UiHandler("cmdAbsencesViewParent")
	void onCmdAbsencesViewParentClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	
	/*
	 * */
	@UiHandler("cmdMarkViewParent")
	void onCmdMarkViewParentClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	
	/*
	 * */
	@UiHandler("cmdPasswordParent")
	void onCmdPasswordParentClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	
	/*
	 * */
	@UiHandler("cmdHelpParent")
	void onCmdHelpParentClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	
	/*
	 * */
	@UiHandler("cmdAbsencesViewProf")
	void onCmdAbsencesViewProfClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdHome")
	void onCmdHomeClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdStudentList")
	void onCmdStudentListClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdStudentAdd")
	void onCmdStudentAddClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdProfList")
	void onCmdProfListClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdProfAdd")
	void onCmdProfAddClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdParentMgt")
	void onCmdParentMgtClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdProfileManagement")
	void onCmdProfileManagementClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdAttribution")
	void onCmdAttributionClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdAttributionPerson")
	void onCmdAttributionPersonClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdMarkInput")
	void onCmdMarkInputClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdMarkView")
	void onCmdMarkViewClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdBulletin")
	void onCmdBulletinClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdAttendanceList")
	void onCmdAttendanceListClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdAbsenceMgt")
	void onCmdAbsenceMgtClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdEvaluationList")
	void onCmdEvaluationListClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdEvaluationInputProf")
	void onCmdEvaluationInputProfClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdEvaluationInputEleve")
	void onCmdEvaluationInputEleveClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdMasterAgendaPage")
	void onCmdMasterAgendaPageClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdStudentAgendaPage")
	void onCmdStudentAgendaPageClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdProfessorAgendaPage")
	void onCmdProfessorAgendaPageClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdClassAgendaPage")
	void onCmdClassAgendaPageClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdMotifAbsence")
	void onCmdMotifAbsenceClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdMarkViewStudent")
	void onCmdMarkViewStudentClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdPasswordStudent")
	void onCmdPasswordStudentClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdHelpStudent")
	void onCmdHelpStudentClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdHelpProf")
	void onCmdHelpProfClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdPasswordProf")
	void onCmdPasswordProfClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdEvaluationInputEleveProf")
	void onCmdEvaluationInputEleveProfClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdEvaluationInputProfProf")
	void onCmdEvaluationInputProfProfClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdMarkViewProf")
	void onCmdMarkViewProfClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdMarkInputProf")
	void onCmdMarkInputProfClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdAttendanceListProf")
	void onCmdAttendanceListProfClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdHelp")
	void onCmdHelpClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdPassword")
	void onCmdPasswordClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdBrancheList")
	void onCmdBrancheListClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdBrancheAdd")
	void onCmdBrancheAddClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdSubjectList")
	void onCmdSubjectListClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdSubjectAdd")
	void onCmdSubjectAddClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdPeriodMgt")
	void onCmdPeriodMgtClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdClassList")
	void onCmdClassListClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdClassAdd")
	void onCmdClassAddClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdProgrammeList")
	void onCmdProgrammeListClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdProgrammeAdd")
	void onCmdProgrammeAddClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdClassroomPage")
	void onCmdClassroomPageClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdSchoolList")
	void onCmdSchoolListClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdSchoolAdd")
	void onCmdSchoolAddClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdUserManagement")
	void onCmdUserManagementClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdSettings")
	void onCmdSettingsClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdStudyLogMgt")
	void onCmdStudyLogMgtClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdStudyLogMgtProf")
	void onCmdStudyLogMgtProfClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdStudyLogStudent")
	void onCmdStudyLogStudentClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdStudyLogStudentStudent")
	void onCmdStudyLogStudentStudentClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
	@UiHandler("cmdStudyLogStudentParent")
	void onCmdStudyLogStudentParentClick(ClickEvent event) {
		//
		switchButton( (Hyperlink)event.getSource() );
	}
}
