package org.hbrs.se2.project.hellocar.test.testControl;

import org.hbrs.se2.project.hellocar.control.UserControl;
import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.hbrs.se2.project.hellocar.dao.UserDAO;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControlTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserControl userControl = new UserControl() {};

    private static final String EXISTING_EMAIL = "existing@example.com";
    private static final String NON_EXISTING_EMAIL = "nonexisting@example.com";
    private static final String EXISTING_COMPANY = "ExistingCompany";
    private static final String NON_EXISTING_COMPANY = "NonExistingCompany";


    @Test
    public void testDoesMailExist_mailExists() throws Exception {
        when(userDAO.doesUserExist(EXISTING_EMAIL)).thenReturn(true);

        assertThrows(DatabaseException.class, () -> userControl.doesMailExist(EXISTING_EMAIL));
    }

    @Test
    public void testDoesMailExist_mailDoesNotExist() throws Exception {
        when(userDAO.doesUserExist(NON_EXISTING_EMAIL)).thenReturn(false);

        userControl.doesMailExist(NON_EXISTING_EMAIL);

        verify(userDAO, times(1)).doesUserExist(NON_EXISTING_EMAIL);
    }

    @Test
    public void testDoesMailExist_databaseException() throws Exception {
        when(userDAO.doesUserExist(anyString())).thenThrow(new DatabaseException("Database error"));

        assertThrows(DatabaseException.class, () -> userControl.doesMailExist(NON_EXISTING_EMAIL));
    }

    @Test
    public void testDoesMailExist_sqlException() throws Exception {
        when(userDAO.doesUserExist(anyString())).thenThrow(new SQLException("SQL error"));

        assertThrows(DatabaseException.class, () -> userControl.doesMailExist(NON_EXISTING_EMAIL));
    }

    @Test
    public void testDoesCompanyNameExist_companyExists() throws Exception {
        when(userDAO.doesCompanyNameExist(EXISTING_COMPANY)).thenReturn(true);

        assertThrows(DatabaseException.class, () -> userControl.doesCompanyNameExist(EXISTING_COMPANY));
    }

    @Test
    public void testDoesCompanyNameExist_companyDoesNotExist() throws Exception {
        when(userDAO.doesCompanyNameExist(NON_EXISTING_COMPANY)).thenReturn(false);

        userControl.doesCompanyNameExist(NON_EXISTING_COMPANY);

        verify(userDAO, times(1)).doesCompanyNameExist(NON_EXISTING_COMPANY);
    }

    @Test
    public void testDoesCompanyNameExist_databaseException() throws Exception {
        when(userDAO.doesCompanyNameExist(anyString())).thenThrow(new DatabaseException("Database error"));

        assertThrows(DatabaseException.class, () -> userControl.doesCompanyNameExist(NON_EXISTING_COMPANY));
    }

    @Test
    public void testDoesCompanyNameExist_sqlException() throws Exception {
        when(userDAO.doesCompanyNameExist(anyString())).thenThrow(new SQLException("SQL error"));

        assertThrows(DatabaseException.class, () -> userControl.doesCompanyNameExist(NON_EXISTING_COMPANY));
    }
}
