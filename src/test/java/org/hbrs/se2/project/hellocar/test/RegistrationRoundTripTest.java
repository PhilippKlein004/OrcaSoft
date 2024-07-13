package org.hbrs.se2.project.hellocar.test;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import org.hbrs.se2.project.hellocar.util.builder.CompanyDTOBuilder;
import org.hbrs.se2.project.hellocar.util.builder.StudentDTOBuilder;
import org.hbrs.se2.project.hellocar.control.RegistrationControl;
import org.hbrs.se2.project.hellocar.control.SessionController;
import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.hbrs.se2.project.hellocar.dao.*;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.CompanyDTOImpl;
import org.hbrs.se2.project.hellocar.dtos.impl.StudentDTOImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class RegistrationRoundTripTest {

    RegistrationControl registrationControl;

    private StudentDTOImpl studentDTO;


    private CompanyDTOImpl companyDTO;

    //user
    private UserDAO userDAO;
    private SkillDAO skillDAO;
    private SpracheDAO spracheDAO;
    private BrancheDAO brancheDAO;

    @InjectMocks
    private SessionController sessionController;

    @Mock
    private UI uiMock;

    @Mock
    private VaadinSession sessionMock;


    @BeforeEach
    void setUp() {
        UI.setCurrent(uiMock);

        registrationControl = new RegistrationControl();

        skillDAO = new SkillDAO();
        spracheDAO = new SpracheDAO();
        StudiengangDAO studiengangDAO = new StudiengangDAO();
        brancheDAO = new BrancheDAO();

        userDAO = new UserDAO(studiengangDAO, brancheDAO, skillDAO, spracheDAO);

        //student
        StudentDTOBuilder builderStudent = new StudentDTOBuilder();
        studentDTO = new StudentDTOImpl();
        studentDTO = builderStudent.createStudent()
                .withEmail("roundtrip1008@student.de")
                .withPassword("123456A&")
                .withStudiengang("1")
                .build();

        //company
        CompanyDTOBuilder builderCompany = new CompanyDTOBuilder();
        companyDTO = new CompanyDTOImpl();
        companyDTO = builderCompany.createCompany()
                .withBezeichnung("TestCompany1004")
                .withEmail("roundtrip1004@company.de")
                .withPassword("123456A&")
                .withBranche("1")
                .build();

    }

    @AfterEach
    void teardownUser() throws SQLException, DatabaseException {
        if (userDAO.doesUserExist(studentDTO.getEmail())) {
            userDAO.deleteUser(studentDTO.getId());
        }
        else if (userDAO.doesUserExist(companyDTO.getEmail())){
            userDAO.deleteUser(companyDTO.getId());
        }
    }

    @Test
    void testRoundTripStudent() throws DatabaseException, SQLException {

        //Create user
        registrationControl.registerStudentUser(studentDTO);

        //Get user
        UserDTO user  = userDAO.findUserByUseridAndPassword(studentDTO.getEmail(), studentDTO.getPassword());
        //Fuse user with student data
        StudentDTOImpl studentDTOAfterImpl = userDAO.fuseUserDTOWithExtractedStudentData(user, user.getId());

        //Check if user is the same
        assertEquals(studentDTO.getEmail(), studentDTOAfterImpl.getEmail());

        //Check if user obj is not the same
        assertNotSame(studentDTO, studentDTOAfterImpl);

        //Update user and set mandatory fields
        studentDTOAfterImpl.setFirstName("Bob");
        studentDTOAfterImpl.setLastName("Müller");
        studentDTOAfterImpl.setEmail("roundtrip1009@student.de");
        studentDTOAfterImpl.setPassword("123456A&");
        studentDTOAfterImpl.setSprachen(List.of("1", "2"));
        studentDTOAfterImpl.setSkills(List.of("1", "2"));
        studentDTOAfterImpl.setStudiengang("1");
        studentDTOAfterImpl.setDateOfBirth(new Date(1990, Calendar.FEBRUARY, 1));
        studentDTOAfterImpl.setMatrikelnummer(253287326);
        studentDTOAfterImpl.setBerufserfahrung("keine");
        studentDTOAfterImpl.setAusbildung("ebenfalls keine");
        studentDTOAfterImpl.setStrasse("Hauptstrasse");
        studentDTOAfterImpl.setHausnummer(10);
        studentDTOAfterImpl.setOrt("Bonn");
        studentDTOAfterImpl.setPlz(53111);
        userDAO.updateUser(studentDTOAfterImpl);

        //Get user
        user  = userDAO.findUserByUseridAndPassword(studentDTOAfterImpl.getEmail(), studentDTOAfterImpl.getPassword());
        //Fuse user with student data
        StudentDTOImpl studentDTOAfterUpdate = userDAO.fuseUserDTOWithExtractedStudentData(user, user.getId());

        //Check if user is the same
        assertEquals(studentDTOAfterImpl.getEmail(), studentDTOAfterUpdate.getEmail());
        //Check if user obj is not the same
        assertNotSame(studentDTOAfterImpl, studentDTOAfterUpdate);

        //Skills Liste Updaten
        skillDAO.updateSkillsOfStudent(studentDTOAfterUpdate.getId(), List.of("3", "4"));
        assertEquals(List.of(skillDAO.getBezeichnung(3), skillDAO.getBezeichnung(4)), skillDAO.getSkillsOfStudent(studentDTOAfterUpdate.getId()));

        //Sprache aus DB herausnehmen
        assertEquals(List.of(spracheDAO.getBezeichnung(1),spracheDAO.getBezeichnung(2)), studentDTOAfterUpdate.getSprachen());

        // MatrikelNr existiert in DB
        assertTrue(userDAO.doesMatrikelnummerExist(studentDTOAfterUpdate.getMatrikelnummer()));

        //Delete Object from DB
        when(uiMock.getSession()).thenReturn(sessionMock);
        sessionController.logOutUser();
        verify(sessionMock).close();
        userDAO.deleteUser(user.getId());
        assertFalse(userDAO.doesUserExist("roundtrip1009@testing.de"));
    }

    @Test
    void testRoundTripCompany() throws DatabaseException, SQLException {

        //Create user
        registrationControl.registerCompanyUser(companyDTO);

        //Find user
        UserDTO user = userDAO.findUserByUseridAndPassword(companyDTO.getEmail(),companyDTO.getPassword());

        //Fuse user with company data
        CompanyDTOImpl companyDTOAfterImpl = userDAO.fuseUserDTOWithExtractedCompanyData(user, user.getId());

        //Check if user is the same
        assertEquals(companyDTO.getEmail(), companyDTOAfterImpl.getEmail());

        //Check if user obj is not the same
        assertNotSame(companyDTO, companyDTOAfterImpl);

        //Update user and set mandatory fields
        companyDTOAfterImpl.setEmail("roundtrip1005@company.de");
        companyDTOAfterImpl.setPassword("123456A&");
        companyDTOAfterImpl.setBranche("1");
        companyDTOAfterImpl.setBezeichnung("TestCompany1005");
        companyDTOAfterImpl.setWebsite("www.testcompany.de");
        companyDTOAfterImpl.setPhone("12345679");
        companyDTOAfterImpl.setIBAN("DE12345789");
        companyDTOAfterImpl.setBio("schmerz");
        companyDTOAfterImpl.setPhone("+491761111111");
        companyDTOAfterImpl.setStrasse("Nebenstrasse");
        companyDTOAfterImpl.setOrt("Siegburg");
        companyDTOAfterImpl.setHausnummer(200);
        companyDTOAfterImpl.setPlz(51321);
        userDAO.updateUser(companyDTOAfterImpl);

        //Get user
        user  = userDAO.findUserByUseridAndPassword(companyDTOAfterImpl.getEmail(), companyDTOAfterImpl.getPassword());

        //Fuse user with student data
        CompanyDTOImpl companyDTOAfterUpdate = userDAO.fuseUserDTOWithExtractedCompanyData(user, user.getId());

        //Check if user is the same
        assertEquals(companyDTOAfterImpl.getEmail(), companyDTOAfterUpdate.getEmail());
        //Check if user obj is not the same
        assertNotSame(companyDTOAfterImpl, companyDTOAfterUpdate);

        //Name der Company aus DB
        assertEquals(userDAO.getCompanyNameByID(companyDTOAfterUpdate.getId()), companyDTOAfterUpdate.getBezeichnung());


        //Company existiert in der DB
        assertTrue(userDAO.doesCompanyNameExist(companyDTOAfterUpdate.getBezeichnung()));

        //Delete Object from DB
        when(uiMock.getSession()).thenReturn(sessionMock);
        sessionController.logOutUser();
        verify(sessionMock).close();
        userDAO.deleteUser(user.getId());
        assertFalse(userDAO.doesUserExist("roundtrip1005@company.de"));

        //Branche aus DB herausholen
        assertEquals(brancheDAO.getBezeichnung(1), companyDTOAfterUpdate.getBranche());

        /*
        // Exception: Bereits gelöschten User versuchen nochmal zu löschen
        UserDTO finalUser = user;
        assertThrows(DatabaseException.class, ()-> userDAO.deleteUser(finalUser.getId()));




        // Exception: SQL-Exception
        Exception sqlException = assertThrows(SQLException.class, () ->userDAO.deleteUser(12048));
        assertEquals("Ein SQL-Fehler ist aufgetreten!", sqlException.getMessage());



        //Exceptiom: Name existiert nicht in DB
        Exception exception = assertThrows(DatabaseException.class, () -> userDAO.getCompanyNameByID(61));
        assertEquals(DatabaseException.SQL_ERROR, exception.getMessage());

         */

    }



}
