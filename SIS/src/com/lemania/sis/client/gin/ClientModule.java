package com.lemania.sis.client.gin;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import com.lemania.sis.client.form.branches.FrmBrancheAddPresenter;
import com.lemania.sis.client.form.branches.FrmBrancheAddView;
import com.lemania.sis.client.form.branches.FrmBrancheListPresenter;
import com.lemania.sis.client.form.branches.FrmBrancheListView;
import com.lemania.sis.client.form.bulletincreation.FrmBulletinCreationPresenter;
import com.lemania.sis.client.form.bulletincreation.FrmBulletinCreationView;
import com.lemania.sis.client.form.bulletinmgt.FrmBulletinManagementPresenter;
import com.lemania.sis.client.form.bulletinmgt.FrmBulletinManagementView;
import com.lemania.sis.client.form.bulletins.FrmBulletinViewDetailPresenter;
import com.lemania.sis.client.form.bulletins.FrmBulletinViewDetailView;
import com.lemania.sis.client.form.bulletins.FrmBulletinViewSummaryPresenter;
import com.lemania.sis.client.form.bulletins.FrmBulletinViewSummaryView;
import com.lemania.sis.client.form.classroom.ClassroomModule;
import com.lemania.sis.client.form.evaluations.FrmEvaluationInputPresenter;
import com.lemania.sis.client.form.evaluations.FrmEvaluationInputStudentPresenter;
import com.lemania.sis.client.form.evaluations.FrmEvaluationInputStudentView;
import com.lemania.sis.client.form.evaluations.FrmEvaluationInputView;
import com.lemania.sis.client.form.evaluations.FrmEvaluationReportListPresenter;
import com.lemania.sis.client.form.evaluations.FrmEvaluationReportListView;
import com.lemania.sis.client.form.homepage.HomePresenter;
import com.lemania.sis.client.form.homepage.HomeView;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.form.mainpage.MainPageView;
import com.lemania.sis.client.form.markinput.FrmMarkInputPresenter;
import com.lemania.sis.client.form.markinput.FrmMarkInputView;
import com.lemania.sis.client.form.masteragenda.MasterAgendaModule;
import com.lemania.sis.client.place.DefaultPlace;
import com.lemania.sis.client.place.NameTokens;
import com.lemania.sis.client.presenter.EcolePresenter;
import com.lemania.sis.client.view.ContactView;
import com.lemania.sis.client.view.CoursAddView;
import com.lemania.sis.client.view.CoursView;
import com.lemania.sis.client.view.EcoleAddView;
import com.lemania.sis.client.view.EcoleView;
import com.lemania.sis.client.view.FrmClasseAddView;
import com.lemania.sis.client.view.FrmClasseListView;
import com.lemania.sis.client.view.SettingsView;
import com.lemania.sis.client.presenter.EcoleAddPresenter;
import com.lemania.sis.client.presenter.ContactPresenter;
import com.lemania.sis.client.presenter.SettingsPresenter;
import com.lemania.sis.client.place.SISPlaceManager;
import com.lemania.sis.client.presenter.CoursAddPresenter;
import com.lemania.sis.client.presenter.CoursPresenter;
import com.lemania.sis.client.presenter.FrmClasseListPresenter;
import com.lemania.sis.client.presenter.FrmClasseAddPresenter;
import com.lemania.sis.client.form.periodmanagement.PeriodManagementModule;
import com.lemania.sis.client.form.studentagenda.StudentAgendaModule;
import com.lemania.sis.client.form.studentmgt.StudentAddPresenter;
import com.lemania.sis.client.form.studentmgt.StudentAddView;
import com.lemania.sis.client.form.studentmgt.StudentPresenter;
import com.lemania.sis.client.form.studentmgt.StudentView;
import com.lemania.sis.client.form.studylogmgt.StudyLogManagementModule;
import com.lemania.sis.client.form.studylogstudent.StudyLogStudentModule;
import com.lemania.sis.client.form.subjects.FrmSubjectAddPresenter;
import com.lemania.sis.client.form.subjects.FrmSubjectAddView;
import com.lemania.sis.client.form.subjects.FrmSubjectListPresenter;
import com.lemania.sis.client.form.subjects.FrmSubjectListView;
import com.lemania.sis.client.form.usermgt.UserManagementPresenter;
import com.lemania.sis.client.form.usermgt.UserManagementView;
import com.lemania.sis.client.form.userprofile.FrmPasswordPresenter;
import com.lemania.sis.client.form.userprofile.FrmPasswordView;
import com.lemania.sis.client.form.profagenda.ProfessorAgendaModule;
import com.lemania.sis.client.form.professors.ProfsAddPresenter;
import com.lemania.sis.client.form.professors.ProfsAddView;
import com.lemania.sis.client.form.professors.ProfsPresenter;
import com.lemania.sis.client.form.professors.ProfsView;
import com.lemania.sis.client.form.profilemgt.ProfileManagementPresenter;
import com.lemania.sis.client.form.profilemgt.ProfileManagementView;
import com.lemania.sis.client.popup.periodlistpopup.PeriodListPopupModule;
import com.lemania.sis.client.form.parentmgt.ParentManagementModule;
import com.lemania.sis.client.popup.parentprofile.ParentProfileModule;
import com.lemania.sis.client.form.motifabsence.MotifAbsenceModule;
import com.lemania.sis.client.form.attendancelist.AttendanceListModule;
import com.lemania.sis.client.form.absencelist.AbsenceListModule;
import com.lemania.sis.client.form.absencemgt.AbsenceManagementPresenter;
import com.lemania.sis.client.form.absencemgt.AbsenceManagementView;
import com.lemania.sis.client.popup.absenceinput.AbsenceInputPresenter;
import com.lemania.sis.client.popup.absenceinput.AbsenceInputView;

public class ClientModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		
		install(new AttendanceListModule());
		install(new MotifAbsenceModule());
		install(new ParentProfileModule());
		install(new ParentManagementModule());
		install(new PeriodListPopupModule());
		install(new ProfessorAgendaModule());
		install(new StudentAgendaModule());
		install(new PeriodManagementModule());
		install(new MasterAgendaModule());
		install(new ClassroomModule());
		install(new AbsenceListModule());
		install(new StudyLogManagementModule());
		install(new StudyLogStudentModule());
		
		// Singletons
		install(new DefaultModule(SISPlaceManager.class));
		
		// Set DefaultPlace to homepage
		bindConstant().annotatedWith(DefaultPlace.class).to(
				NameTokens.homepage);
		
		bindPresenter(MainPagePresenter.class, MainPagePresenter.MyView.class,
				MainPageView.class, MainPagePresenter.MyProxy.class);

		bindPresenter(HomePresenter.class, HomePresenter.MyView.class,
				HomeView.class, HomePresenter.MyProxy.class);

		bindPresenter(EcolePresenter.class, EcolePresenter.MyView.class,
				EcoleView.class, EcolePresenter.MyProxy.class);

		bindPresenter(EcoleAddPresenter.class, EcoleAddPresenter.MyView.class,
				EcoleAddView.class, EcoleAddPresenter.MyProxy.class);

		bindPresenter(ProfsPresenter.class, ProfsPresenter.MyView.class,
				ProfsView.class, ProfsPresenter.MyProxy.class);

		bindPresenter(ProfsAddPresenter.class, ProfsAddPresenter.MyView.class,
				ProfsAddView.class, ProfsAddPresenter.MyProxy.class);

		bindPresenter(ContactPresenter.class, ContactPresenter.MyView.class,
				ContactView.class, ContactPresenter.MyProxy.class);

		bindPresenter(UserManagementPresenter.class,
				UserManagementPresenter.MyView.class, UserManagementView.class,
				UserManagementPresenter.MyProxy.class);

		bindPresenter(SettingsPresenter.class, SettingsPresenter.MyView.class,
				SettingsView.class, SettingsPresenter.MyProxy.class);

		bindPresenter(FrmPasswordPresenter.class,
				FrmPasswordPresenter.MyView.class, FrmPasswordView.class,
				FrmPasswordPresenter.MyProxy.class);

		bindPresenter(StudentPresenter.class, StudentPresenter.MyView.class,
				StudentView.class, StudentPresenter.MyProxy.class);

		bindPresenter(StudentAddPresenter.class,
				StudentAddPresenter.MyView.class, StudentAddView.class,
				StudentAddPresenter.MyProxy.class);

		bindPresenter(FrmBrancheListPresenter.class,
				FrmBrancheListPresenter.MyView.class, FrmBrancheListView.class,
				FrmBrancheListPresenter.MyProxy.class);

		bindPresenter(FrmBrancheAddPresenter.class,
				FrmBrancheAddPresenter.MyView.class, FrmBrancheAddView.class,
				FrmBrancheAddPresenter.MyProxy.class);

		bindPresenter(FrmSubjectListPresenter.class,
				FrmSubjectListPresenter.MyView.class, FrmSubjectListView.class,
				FrmSubjectListPresenter.MyProxy.class);

		bindPresenter(FrmSubjectAddPresenter.class,
				FrmSubjectAddPresenter.MyView.class, FrmSubjectAddView.class,
				FrmSubjectAddPresenter.MyProxy.class);

		bindPresenter(CoursAddPresenter.class, CoursAddPresenter.MyView.class,
				CoursAddView.class, CoursAddPresenter.MyProxy.class);

		bindPresenter(CoursPresenter.class, CoursPresenter.MyView.class,
				CoursView.class, CoursPresenter.MyProxy.class);

		bindPresenter(FrmClasseListPresenter.class,
				FrmClasseListPresenter.MyView.class, FrmClasseListView.class,
				FrmClasseListPresenter.MyProxy.class);

		bindPresenter(FrmClasseAddPresenter.class,
				FrmClasseAddPresenter.MyView.class, FrmClasseAddView.class,
				FrmClasseAddPresenter.MyProxy.class);

		bindPresenter(ProfileManagementPresenter.class,
				ProfileManagementPresenter.MyView.class,
				ProfileManagementView.class,
				ProfileManagementPresenter.MyProxy.class);

		bindPresenter(FrmBulletinCreationPresenter.class,
				FrmBulletinCreationPresenter.MyView.class,
				FrmBulletinCreationView.class,
				FrmBulletinCreationPresenter.MyProxy.class);

		bindPresenter(FrmMarkInputPresenter.class,
				FrmMarkInputPresenter.MyView.class, FrmMarkInputView.class,
				FrmMarkInputPresenter.MyProxy.class);

		bindPresenter(FrmBulletinViewDetailPresenter.class,
				FrmBulletinViewDetailPresenter.MyView.class,
				FrmBulletinViewDetailView.class,
				FrmBulletinViewDetailPresenter.MyProxy.class);

		bindPresenter(FrmBulletinManagementPresenter.class,
				FrmBulletinManagementPresenter.MyView.class,
				FrmBulletinManagementView.class,
				FrmBulletinManagementPresenter.MyProxy.class);

		bindPresenter(FrmBulletinViewSummaryPresenter.class,
				FrmBulletinViewSummaryPresenter.MyView.class,
				FrmBulletinViewSummaryView.class,
				FrmBulletinViewSummaryPresenter.MyProxy.class);

		bindPresenter(FrmEvaluationReportListPresenter.class,
				FrmEvaluationReportListPresenter.MyView.class,
				FrmEvaluationReportListView.class,
				FrmEvaluationReportListPresenter.MyProxy.class);

		bindPresenter(FrmEvaluationInputPresenter.class,
				FrmEvaluationInputPresenter.MyView.class,
				FrmEvaluationInputView.class,
				FrmEvaluationInputPresenter.MyProxy.class);

		bindPresenter(FrmEvaluationInputStudentPresenter.class,
				FrmEvaluationInputStudentPresenter.MyView.class,
				FrmEvaluationInputStudentView.class,
				FrmEvaluationInputStudentPresenter.MyProxy.class);

		bindPresenter(AbsenceManagementPresenter.class,
				AbsenceManagementPresenter.MyView.class,
				AbsenceManagementView.class,
				AbsenceManagementPresenter.MyProxy.class);

		bindPresenterWidget(AbsenceInputPresenter.class,
				AbsenceInputPresenter.MyView.class, AbsenceInputView.class);
	}
}