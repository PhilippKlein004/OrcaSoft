package org.hbrs.se2.project.hellocar.test.testControl;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.server.VaadinSession;
import org.hbrs.se2.project.hellocar.control.SessionController;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.hellocar.util.Globals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionControllerTest {

    @InjectMocks
    private SessionController sessionController;

    @Mock
    private UI uiMock;

    @Mock
    private VaadinSession sessionMock;

    @Mock
    private Page pageMock;

    @BeforeEach
    void setUp() {
        UI.setCurrent(uiMock);
    }

    @Test
    void testGetCurrentUser() {
        UserDTO user = new UserDTOImpl();
        when(uiMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute(Globals.CURRENT_USER)).thenReturn(user);
        UserDTO result = sessionController.getCurrentUser();
        assertEquals(user, result);
    }

    @Test
    void testSetCurrentUser() {
        UserDTO user = new UserDTOImpl();
        when(uiMock.getSession()).thenReturn(sessionMock);
        sessionController.setCurrentUser(user);
        verify(sessionMock).setAttribute(Globals.CURRENT_USER, user);
    }

    @Test
    void testRefreshPage() {
        when(uiMock.getPage()).thenReturn(pageMock);
        sessionController.refreshPage();
        verify(pageMock).reload();
    }

    @Test
    void testGetPage() {
        String currentPage = "somePage";
        sessionController.redirectToPage(currentPage);
        assertEquals(currentPage, sessionController.getPage());
    }

    @Test
    void testRedirectToPage() {
        String pageName = "somePage";
        sessionController.redirectToPage(pageName);
        assertEquals(pageName, sessionController.getPage());
        verify(uiMock).navigate(pageName);
    }

    @Test
    void testLogOutUser() {
        when(uiMock.getSession()).thenReturn(sessionMock);
        sessionController.logOutUser();
        verify(sessionMock).close();
    }
}
