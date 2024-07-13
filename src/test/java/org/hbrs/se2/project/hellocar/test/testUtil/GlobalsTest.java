package org.hbrs.se2.project.hellocar.test.testUtil;

import org.hbrs.se2.project.hellocar.util.Globals;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GlobalsTest {

    @Test
    public void testCurrentUserField() {
        assertNotNull(Globals.CURRENT_USER);
        assertEquals("current_User", Globals.CURRENT_USER);
    }

    @Test
    public void testPagesFields() {
        assertNotNull(Globals.Pages.SEARCH);
        assertEquals("search", Globals.Pages.SEARCH);

        assertNotNull(Globals.Pages.REGISTER);
        assertEquals("registration", Globals.Pages.REGISTER);

        assertNotNull(Globals.Pages.LOGIN_VIEW);
        assertEquals("login", Globals.Pages.LOGIN_VIEW);

        assertNotNull(Globals.Pages.MAIN_VIEW);
        assertEquals("", Globals.Pages.MAIN_VIEW);

        assertNotNull(Globals.Pages.PROFILE);
        assertEquals("profile", Globals.Pages.PROFILE);

        assertNotNull(Globals.Pages.PROFIL);
        assertEquals("profil", Globals.Pages.PROFIL);

        assertNotNull(Globals.Pages.STELLENANGEBOT);
        assertEquals("stellenangebot", Globals.Pages.STELLENANGEBOT);
    }

    @Test
    public void testRolesFields() {
        assertNotNull(Globals.Roles.COMPANY);
        assertEquals("company", Globals.Roles.COMPANY);

        assertNotNull(Globals.Roles.USER);
        assertEquals("user", Globals.Roles.USER);
    }

    @Test
    public void testErrorsFields() {
        assertNotNull(Globals.Errors.NOUSERFOUND);
        assertEquals("nouser", Globals.Errors.NOUSERFOUND);

        assertNotNull(Globals.Errors.SQLERROR);
        assertEquals("sql", Globals.Errors.SQLERROR);

        assertNotNull(Globals.Errors.DATABASE);
        assertEquals("database", Globals.Errors.DATABASE);
    }
}
