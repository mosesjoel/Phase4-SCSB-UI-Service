package org.recap.controller;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCaseUT;
import org.recap.PropertyKeyConstants;
import org.recap.ScsbConstants;
import org.recap.model.jpa.InstitutionEntity;
import org.recap.model.usermanagement.UserForm;
import org.recap.repository.jpa.InstitutionDetailsRepository;
import org.recap.security.UserManagementService;
import org.recap.util.UserAuthUtil;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;

public class HomeControllerUT extends BaseTestCaseUT {

    @InjectMocks
    HomeController homeController;

    @Mock
    UserAuthUtil userAuthUtil;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpSession session;

    @Mock
    InstitutionDetailsRepository institutionDetailsRepository;

    @Mock
    UserManagementService userManagementService;

    @Value("${" + PropertyKeyConstants.SCSB_SUPPORT_INSTITUTION + "}")
    private String supportInstitution;

    @Before
    public void setup(){
        UserForm userForm = new UserForm();
        userForm.setUsername("SuperAdmin");
        userForm.setInstitution("1");
        userForm.setPassword("12345");
        UsernamePasswordToken token = new UsernamePasswordToken(userForm.getUsername()+ ScsbConstants.TOKEN_SPLITER +userForm.getInstitution(),userForm.getPassword(),true);
        session.setAttribute(ScsbConstants.REQUEST_PRIVILEGE,token);
    }
    @Test
    public void loadInstitutions(){
        InstitutionEntity institutionEntity = getInstitutionEntity();
        Mockito.when(institutionDetailsRepository.getInstitutionCodes(supportInstitution)).thenReturn(Arrays.asList(institutionEntity));
        homeController.loadInstitutions();
    }
    @Test
    public void fecthingInstituionsFromDBException(){
        Mockito.when(institutionDetailsRepository.getInstitutionCodes(supportInstitution)).thenThrow(new NullPointerException());
        homeController.fecthingInstituionsFromDB();
    }
    @Test
    public void login(){
        Object obj = new Object();
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(request.getSession(false)).thenReturn(session);
        Mockito.when(userAuthUtil.isAuthenticated(request, ScsbConstants.SCSB_SHIRO_SEARCH_URL)).thenReturn(Boolean.TRUE);
        Mockito.when(session.getAttribute(anyString())).thenReturn(obj);
        homeController.login(request);
    }
    @Test
    public void loginException(){
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken();
        usernamePasswordToken.setUsername("token");
        Mockito.when(request.getSession(false)).thenReturn(session);
        Mockito.when(userAuthUtil.isAuthenticated(request, ScsbConstants.SCSB_SHIRO_SEARCH_URL)).thenReturn(Boolean.TRUE);
        homeController.login(request);
    }
    private InstitutionEntity getInstitutionEntity() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setInstitutionCode("UC");
        institutionEntity.setInstitutionName("University of Chicago");
        return institutionEntity;
    }
}
