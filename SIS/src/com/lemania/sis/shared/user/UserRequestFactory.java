package com.lemania.sis.shared.user;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.lemania.sis.server.bean.user.UserDao;
import com.lemania.sis.server.service.DaoServiceLocator;

public interface UserRequestFactory extends RequestFactory {
	
	@Service(value=UserDao.class, locator=DaoServiceLocator.class)
	interface UserRequestContext extends RequestContext {
		
		Request<List<UserProxy>> listAll();
		Request<List<UserProxy>> listAllByType(String type);
		Request<List<UserProxy>> listAllActive();
		
		Request<Void> save(UserProxy user);
		Request<UserProxy> saveAndReturn(UserProxy newUser);
		
		Request<Void> removeUser(UserProxy user);
		
		Request<UserProxy> authenticateUser(String userName, String password, boolean isGoogleService );
		
		Request<UserProxy> changePassword(String userName, String password, String newPassword);
		Request<Boolean> changeUserName( String currentUserName, String newUserName );
		
		Request<Void> updateUserActiveStatus(String userEmail, Boolean userStatus);
		
		Request<Void> initialize();
		
		Request<Void> fixStudentName();
		
		Request<Boolean> checkClassMasterRole(String userId, String profId);
		
		Request<String> getGoogleLoginURL();
		Request<String> getGoogleLogoutURL( String userEmail );
	}
	
	UserRequestContext userRequest();
}
