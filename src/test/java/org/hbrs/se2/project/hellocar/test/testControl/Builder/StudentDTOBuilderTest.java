package org.hbrs.se2.project.hellocar.test.testControl.Builder;

import com.vaadin.flow.server.StreamResource;
import org.hbrs.se2.project.hellocar.util.builder.StudentDTOBuilder;
import org.hbrs.se2.project.hellocar.dtos.impl.StudentDTOImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudentDTOBuilderTest {

    private StudentDTOBuilder builder;

    @BeforeEach
    public void setUp() {
        builder = new StudentDTOBuilder();
    }

    @Test
    public void testCreateStudent() {
        builder.createStudent();
        StudentDTOImpl student = builder.build();
        assertNotNull(student, "Student should not be null after creation");
    }

    @Test
    public void testWithId() {
        builder.createStudent().withId(123);
        StudentDTOImpl student = builder.build();
        assertEquals(123, student.getId(), "ID should be set correctly");
    }

    @Test
    public void testWithEmail() {
        builder.createStudent().withEmail("student@example.com");
        StudentDTOImpl student = builder.build();
        assertEquals("student@example.com", student.getEmail(), "Email should be set correctly");
    }

    @Test
    public void testWithStrasse() {
        builder.createStudent().withStrasse("Teststraße");
        StudentDTOImpl student = builder.build();
        assertEquals("Teststraße", student.getStrasse(), "Strasse should be set correctly");
    }

    @Test
    public void testWithOrt() {
        builder.createStudent().withOrt("Testort");
        StudentDTOImpl student = builder.build();
        assertEquals("Testort", student.getOrt(), "Ort should be set correctly");
    }

    @Test
    public void testWithPassword() {
        builder.createStudent().withPassword("password123");
        StudentDTOImpl student = builder.build();
        assertEquals("password123", student.getPassword(), "Password should be set correctly");
    }

    @Test
    public void testWithPlz() {
        builder.createStudent().withPlz(12345);
        StudentDTOImpl student = builder.build();
        assertEquals(12345, student.getPLZ(), "PLZ should be set correctly");
    }

    @Test
    public void testWithHausnummer() {
        builder.createStudent().withHausnummer(10);
        StudentDTOImpl student = builder.build();
        assertEquals(10, student.getHausnummer(), "Hausnummer should be set correctly");
    }

    @Test
    public void testWithProfilePicture() {
        StreamResource resource = new StreamResource("test.jpg", () -> null);
        builder.createStudent().withProfilePicture(resource);
        StudentDTOImpl student = builder.build();
        assertEquals(resource, student.getProfilePicture(), "Profile picture should be set correctly");
    }

    @Test
    public void testWithFirstName() {
        builder.createStudent().withfirstName("John");
        StudentDTOImpl student = builder.build();
        assertEquals("John", student.getFirstName(), "First name should be set correctly");
    }

    @Test
    public void testWithLastName() {
        builder.createStudent().withLastName("Doe");
        StudentDTOImpl student = builder.build();
        assertEquals("Doe", student.getLastName(), "Last name should be set correctly");
    }

    @Test
    public void testWithDateOfBirth() {
        Date dateOfBirth = new Date();
        builder.createStudent().withDateOfBirth(dateOfBirth);
        StudentDTOImpl student = builder.build();
        assertEquals(dateOfBirth, student.getDateOfBirth(), "Date of birth should be set correctly");
    }

    @Test
    public void testWithMatrikelnummer() {
        builder.createStudent().withMatrikelnummer(987654);
        StudentDTOImpl student = builder.build();
        assertEquals(987654, student.getMatrikelnummer(), "Matrikelnummer should be set correctly");
    }

    @Test
    public void testWithAusbildung() {
        builder.createStudent().withAusbildung("Informatik");
        StudentDTOImpl student = builder.build();
        assertEquals("Informatik", student.getAusbildung(), "Ausbildung should be set correctly");
    }

    @Test
    public void testWithBerufserfahrung() {
        builder.createStudent().withBerufserfahrung("Softwareentwickler");
        StudentDTOImpl student = builder.build();
        assertEquals("Softwareentwickler", student.getBerufserfahrung(), "Berufserfahrung should be set correctly");
    }

    @Test
    public void testWithStudiengang() {
        builder.createStudent().withStudiengang("Informatik");
        StudentDTOImpl student = builder.build();
        assertEquals("Informatik", student.getStudiengang(), "Studiengang should be set correctly");
    }

    @Test
    public void testWithSkills() {
        List<String> skills = Arrays.asList("Java", "Spring");
        builder.createStudent().withSkills(skills);
        StudentDTOImpl student = builder.build();
        assertEquals(skills, student.getSkills(), "Skills should be set correctly");
    }

    @Test
    public void testWithSprachen() {
        List<String> sprachen = Arrays.asList("Deutsch", "Englisch");
        builder.createStudent().withSprachen(sprachen);
        StudentDTOImpl student = builder.build();
        assertEquals(sprachen, student.getSprachen(), "Sprachen should be set correctly");
    }

    @Test
    public void testReset() {
        builder.createStudent().withId(123).reset();
        StudentDTOImpl student = builder.build();
        assertNotEquals(123, student.getId(), "ID should be reset");
    }

    @Test
    public void testSetFrom() {
        StudentDTOImpl source = new StudentDTOImpl();
        source.setId(456);
        source.setEmail("source@example.com");

        builder.setFrom(source);
        StudentDTOImpl student = builder.build();

        assertEquals(456, student.getId(), "ID should be copied from source");
        assertEquals("source@example.com", student.getEmail(), "Email should be copied from source");
    }
}
