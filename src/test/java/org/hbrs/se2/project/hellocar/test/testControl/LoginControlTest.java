package org.hbrs.se2.project.hellocar.test.testControl;

import org.hbrs.se2.project.hellocar.control.LoginControl;
import org.hbrs.se2.project.hellocar.control.SessionController;
import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.hbrs.se2.project.hellocar.dao.UserDAO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.CompanyDTOImpl;
import org.hbrs.se2.project.hellocar.dtos.impl.StudentDTOImpl;
import org.hbrs.se2.project.hellocar.util.Globals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginControlTest {

    @Mock
    private SessionController sessionController;

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private LoginControl loginControl;

    private static final String USERNAME = "test@test.de";
    private static final String PASSWORD = "abc123@";

    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        userDTO = mock(UserDTO.class);
    }

    @Test
    public void testAuthenticateStudent() throws DatabaseException, SQLException {
        StudentDTOImpl studentDTO = mock(StudentDTOImpl.class);

        when(userDAO.findUserByUseridAndPassword(USERNAME, PASSWORD)).thenReturn(userDTO);
        when(userDAO.isUserStudent(userDTO.getId())).thenReturn(true);
        when(userDAO.fuseUserDTOWithExtractedStudentData(userDTO, userDTO.getId())).thenReturn(studentDTO);

        boolean result = loginControl.authenticate(USERNAME, PASSWORD);

        assertTrue(result);
        verify(sessionController).setCurrentUser(studentDTO);
        verify(sessionController).redirectToPage(Globals.Pages.SEARCH);
    }

    @Test
    public void testAuthenticateCompany() throws DatabaseException, SQLException {
        CompanyDTOImpl companyDTO = mock(CompanyDTOImpl.class);


        when(userDAO.findUserByUseridAndPassword(USERNAME, PASSWORD)).thenReturn(userDTO);
        when(userDAO.isUserStudent(userDTO.getId())).thenReturn(false);
        when(userDAO.fuseUserDTOWithExtractedCompanyData(userDTO, userDTO.getId())).thenReturn(companyDTO);

        boolean result = loginControl.authenticate(USERNAME, PASSWORD);

        assertTrue(result);
        verify(sessionController).setCurrentUser(companyDTO);
        verify(sessionController).redirectToPage(Globals.Pages.SEARCH);
    }

    @Test
    public void testAuthenticateUserNotFound() throws DatabaseException, SQLException {
        when(userDAO.findUserByUseridAndPassword(USERNAME, PASSWORD)).thenThrow(new DatabaseException("User not found"));

        DatabaseException thrown = assertThrows(DatabaseException.class, () -> loginControl.authenticate(USERNAME, PASSWORD));

        assertEquals("User not found", thrown.getMessage());
        verify(sessionController, never()).setCurrentUser(any());
        verify(sessionController, never()).redirectToPage(anyString());
    }

    @Test
    public void testAuthenticateSQLException() throws DatabaseException, SQLException {
        when(userDAO.findUserByUseridAndPassword(USERNAME, PASSWORD)).thenThrow(new SQLException("Database error"));

        SQLException thrown = assertThrows(SQLException.class, () -> loginControl.authenticate(USERNAME, PASSWORD));

        assertEquals("Database error", thrown.getMessage());
        verify(sessionController, never()).setCurrentUser(any());
        verify(sessionController, never()).redirectToPage(anyString());
    }
}
