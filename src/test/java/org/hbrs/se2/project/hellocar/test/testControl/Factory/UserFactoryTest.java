package org.hbrs.se2.project.hellocar.test.testControl.Factory;

import com.vaadin.flow.server.StreamResource;
import org.hbrs.se2.project.hellocar.control.factories.UserFactory;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.CompanyDTOImpl;
import org.hbrs.se2.project.hellocar.dtos.impl.StudentDTOImpl;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserFactoryTest {


    @Test
    public void testCreateDefaultUserDTOWithRoles() {
        int id = 1;
        String email = "test@example.com";
        List<String> roles = Arrays.asList("role1", "role2");

        UserDTO result = UserFactory.createDefaultUserDTO(id, email, roles);

        assertEquals(id, result.getId());
        assertEquals(email, result.getEmail());
    }

    @Test
    public void testCreateDefaultUserDTOWithAddress() {
        int id = 1;
        String email = "test@example.com";
        String strasse = "Teststraße";
        String ort = "Teststadt";
        int plz = 12345;
        int haus_nr = 10;
        StreamResource profilePicture = new StreamResource("profile.jpg", () -> null);

        UserDTO userDTO = UserFactory.createDefaultUserDTO(id, email, strasse, ort, plz, haus_nr, profilePicture);

        assertEquals(id, userDTO.getId());
        assertEquals(email, userDTO.getEmail());
        assertEquals(strasse, userDTO.getStrasse());
        assertEquals(ort, userDTO.getOrt());
        assertEquals(plz, userDTO.getPLZ());
        assertEquals(haus_nr, userDTO.getHausnummer());
        assertEquals(profilePicture, userDTO.getProfilePicture());
    }

    @Test
    public void testCreateStudentDTO() {
        int id = 1;
        String email = "test@example.com";
        String strasse = "Teststraße";
        String ort = "Teststadt";
        int plz = 12345;
        String firstName = "John";
        String lastName = "Doe";
        int matrikelnummer = 987654;
        Date date_of_birth = new Date();
        String ausbildung = "Bachelor";
        String berufserfahrung = "2 Jahre";
        String FK_Studiengang = "Informatik";
        int haus_nr = 10;
        List<String> skills = Arrays.asList("Java", "Python");
        List<String> sprachen = Arrays.asList("Deutsch", "Englisch");
        StreamResource profilePicture = new StreamResource("profile.jpg", () -> null);

        StudentDTOImpl studentDTO = UserFactory.createStudentDTO(id, email, strasse, ort, plz, firstName, lastName, matrikelnummer, date_of_birth, ausbildung, berufserfahrung, FK_Studiengang, haus_nr, skills, sprachen, profilePicture);

        assertEquals(id, studentDTO.getId());
        assertEquals(email, studentDTO.getEmail());
        assertEquals(strasse, studentDTO.getStrasse());
        assertEquals(ort, studentDTO.getOrt());
        assertEquals(plz, studentDTO.getPLZ());
        assertEquals(haus_nr, studentDTO.getHausnummer());
        assertEquals(firstName, studentDTO.getFirstName());
        assertEquals(lastName, studentDTO.getLastName());
        assertEquals(matrikelnummer, studentDTO.getMatrikelnummer());
        assertEquals(date_of_birth, studentDTO.getDateOfBirth());
        assertEquals(ausbildung, studentDTO.getAusbildung());
        assertEquals(berufserfahrung, studentDTO.getBerufserfahrung());
        assertEquals(FK_Studiengang, studentDTO.getStudiengang());
        assertEquals(skills, studentDTO.getSkills());
        assertEquals(sprachen, studentDTO.getSprachen());
        assertEquals(profilePicture, studentDTO.getProfilePicture());
    }

    @Test
    public void testCreateCompanyDTO() {
        int id = 1;
        String email = "test@example.com";
        String strasse = "Teststraße";
        String ort = "Teststadt";
        int plz = 12345;
        String FK_Branche = "IT";
        String bezeichnung = "TestCompany";
        String bio = "A test company";
        String phone = "1234567890";
        String website = "www.testcompany.com";
        String iban = "DE1234567890";
        int haus_nr = 10;
        StreamResource profilePicture = new StreamResource("profile.jpg", () -> null);

        CompanyDTOImpl companyDTO = UserFactory.createCompanyDTO(id, email, strasse, ort, plz, FK_Branche, bezeichnung, bio, phone, website, iban, haus_nr, profilePicture);

        assertEquals(id, companyDTO.getId());
        assertEquals(email, companyDTO.getEmail());
        assertEquals(strasse, companyDTO.getStrasse());
        assertEquals(ort, companyDTO.getOrt());
        assertEquals(plz, companyDTO.getPLZ());
        assertEquals(haus_nr, companyDTO.getHausnummer());
        assertEquals(FK_Branche, companyDTO.getBranche());
        assertEquals(bezeichnung, companyDTO.getBezeichnung());
        assertEquals(bio, companyDTO.getBio());
        assertEquals(phone, companyDTO.getPhone());
        assertEquals(website, companyDTO.getWebsite());
        assertEquals(iban, companyDTO.getIBAN());
        assertEquals(profilePicture, companyDTO.getProfilePicture());
    }

    @Test
    public void testCreateStudentDTOImplRegistration() {
        String email = "student@example.com";
        String password = "password";
        String FK_Studiengang = "Informatik";

        StudentDTOImpl result = UserFactory.createStudentDTOImplRegistration(email, password, FK_Studiengang);

        assertEquals(email, result.getEmail());
        assertEquals(password, result.getPassword());
        assertEquals(FK_Studiengang, result.getStudiengang());
    }

    @Test
    public void testCreateCompanyDTOImplRegistration() {
        String email = "company@example.com";
        String password = "password";
        String bezeichnung = "TestCompany";
        String FK_Branche = "IT";

        CompanyDTOImpl result = UserFactory.createCompanyDTOImplRegistration(email, password, bezeichnung, FK_Branche);

        assertEquals(email, result.getEmail());
        assertEquals(password, result.getPassword());
        assertEquals(bezeichnung, result.getBezeichnung());
        assertEquals(FK_Branche, result.getBranche());
    }
}