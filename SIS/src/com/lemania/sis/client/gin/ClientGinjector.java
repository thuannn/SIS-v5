package com.lemania.sis.client.gin;

import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.lemania.sis.client.AdminGateKeeper;
import com.lemania.sis.client.LoggedInGatekeeper;
import com.lemania.sis.client.ProfessorGateKeeper;
import com.lemania.sis.client.form.attendancelist.AttendanceListPresenter;
import com.lemania.sis.client.form.branches.FrmBrancheAddPresenter;
import com.lemania.sis.client.form.branches.FrmBrancheListPresenter;
import com.lemania.sis.client.form.bulletincreation.FrmBulletinCreationPresenter;
import com.lemania.sis.client.form.bulletinmgt.FrmBulletinManagementPresenter;
import com.lemania.sis.client.form.bulletins.FrmBulletinViewDetailPresenter;
import com.lemania.sis.client.form.bulletins.FrmBulletinViewSummaryPresenter;
import com.lemania.sis.client.form.classroom.ClassroomPresenter;
import com.lemania.sis.client.form.evaluations.FrmEvaluationInputPresenter;
import com.lemania.sis.client.form.evaluations.FrmEvaluationInputStudentPresenter;
import com.lemania.sis.client.form.evaluations.FrmEvaluationReportListPresenter;
import com.lemania.sis.client.form.homepage.HomePresenter;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.form.markinput.FrmMarkInputPresenter;
import com.lemania.sis.client.form.masteragenda.MasterAgendaPresenter;
import com.lemania.sis.client.form.motifabsence.MotifAbsencePresenter;
import com.lemania.sis.client.form.parentmgt.ParentManagementPresenter;
import com.lemania.sis.client.form.periodmanagement.PeriodManagementPresenter;
import com.lemania.sis.client.form.profagenda.ProfessorAgendaPresenter;
import com.lemania.sis.client.form.professors.ProfsAddPresenter;
import com.lemania.sis.client.form.professors.ProfsPresenter;
import com.lemania.sis.client.form.profilemgt.ProfileManagementPresenter;
import com.lemania.sis.client.form.studentagenda.StudentAgendaPresenter;
import com.lemania.sis.client.form.studentmgt.StudentAddPresenter;
import com.lemania.sis.client.form.studentmgt.StudentPresenter;
import com.lemania.sis.client.form.studylogmgt.StudyLogManagementPresenter;
import com.lemania.sis.client.form.studylogstudent.StudyLogStudentPresenter;
import com.lemania.sis.client.form.subjects.FrmSubjectAddPresenter;
import com.lemania.sis.client.form.subjects.FrmSubjectListPresenter;
import com.lemania.sis.client.form.usermgt.UserManagementPresenter;
import com.lemania.sis.client.form.userprofile.FrmPasswordPresenter;
import com.lemania.sis.client.presenter.EcolePresenter;
import com.lemania.sis.client.presenter.EcoleAddPresenter;
import com.lemania.sis.client.presenter.ContactPresenter;
import com.lemania.sis.client.presenter.SettingsPresenter;
import com.lemania.sis.client.presenter.CoursAddPresenter;
import com.lemania.sis.client.presenter.CoursPresenter;
import com.lemania.sis.client.presenter.FrmClasseListPresenter;
import com.lemania.sis.client.presenter.FrmClasseAddPresenter;
import com.lemania.sis.client.form.absencelist.AbsenceListPresenter;
import com.lemania.sis.client.form.absencemgt.AbsenceManagementPresenter;

@GinModules({ ClientModule.class })
public interface ClientGinjector extends Ginjector {
	
	EventBus getEventBus();
	
	Provider<MainPagePresenter> getMainPagePresenter();
	
	AsyncProvider<HomePresenter> getHomePresenter();
	
	PlaceManager getPlaceManager();

	AsyncProvider<EcolePresenter> getEcolePresenter();

	AsyncProvider<EcoleAddPresenter> getEcoleAddPresenter();

	AsyncProvider<ProfsPresenter> getProfsPresenter();

	AsyncProvider<ProfsAddPresenter> getProfsAddPresenter();

	AsyncProvider<ContactPresenter> getContactPresenter();	
	
	// Gate keepers
	LoggedInGatekeeper getLoggedInGatekeeper();
	
	AdminGateKeeper getAdminGateKeeper();
	
	ProfessorGateKeeper getProfessorGateKeeper();
	
	// Presenters

	AsyncProvider<UserManagementPresenter> getUserManagementPresenter();

	AsyncProvider<SettingsPresenter> getSettingsPresenter();

	AsyncProvider<FrmPasswordPresenter> getFrmPasswordPresenter();

	AsyncProvider<StudentPresenter> getStudentPresenter();

	AsyncProvider<StudentAddPresenter> getStudentAddPresenter();

	AsyncProvider<FrmBrancheListPresenter> getFrmBrancheListPresenter();

	AsyncProvider<FrmBrancheAddPresenter> getFrmBrancheAddPresenter();

	AsyncProvider<FrmSubjectListPresenter> getSubjectListPresenter();

	AsyncProvider<FrmSubjectAddPresenter> getFrmSubjectAddPresenter();

	AsyncProvider<CoursAddPresenter> getCoursAddPresenter();

	AsyncProvider<CoursPresenter> getCoursPresenter();

	AsyncProvider<FrmClasseListPresenter> getFrmClasseListPresenter();

	AsyncProvider<FrmClasseAddPresenter> getFrmClasseAddPresenter();

	AsyncProvider<ProfileManagementPresenter> getProfileManagementPresenter();

	AsyncProvider<FrmBulletinCreationPresenter> getFrmBulletinCreationPresenter();

	AsyncProvider<FrmMarkInputPresenter> getFrmMarkInputPresenter();

	AsyncProvider<FrmBulletinViewDetailPresenter> getFrmBulletinViewDetailPresenter();

	AsyncProvider<FrmBulletinManagementPresenter> getFrmBulletinManagementPresenter();

	AsyncProvider<FrmBulletinViewSummaryPresenter> getFrmBulletinViewSummaryPresenter();

	AsyncProvider<FrmEvaluationReportListPresenter> getFrmEvaluationReportListPresenter();

	AsyncProvider<FrmEvaluationInputPresenter> getFrmEvaluationInputPresenter();

	AsyncProvider<FrmEvaluationInputStudentPresenter> getFrmEvaluationInputStudentPresenter();
	
	AsyncProvider<ClassroomPresenter> getClassroomPresenter();
	
	AsyncProvider<MasterAgendaPresenter> getFrmMasterAgenda();
	
	AsyncProvider<PeriodManagementPresenter> getPeriodManagementPresenter();
	
	AsyncProvider<StudentAgendaPresenter> getStudentAgendaPresenter();
	
	AsyncProvider<ProfessorAgendaPresenter> getProfessorAgendaPresenter();
	
	AsyncProvider<ParentManagementPresenter> getParentManagementPresenter();
	
	AsyncProvider<MotifAbsencePresenter> getMotifAbsencePresenter();
	
	AsyncProvider<AttendanceListPresenter> getAttendanceListPresenter();

	AsyncProvider<AbsenceManagementPresenter> getAbsenceManagementPresenter();
	
	AsyncProvider<AbsenceListPresenter> getAbsenceListPresenter();
	
	AsyncProvider<StudyLogManagementPresenter> getStudyLogManagementPresenter();
	
	AsyncProvider<StudyLogStudentPresenter> getStudyLogStudentPresenter();
}
