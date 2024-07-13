package org.hbrs.se2.project.hellocar.test.testControl.Builder;

import com.vaadin.flow.server.StreamResource;
import org.hbrs.se2.project.hellocar.util.builder.CompanyDTOBuilder;
import org.hbrs.se2.project.hellocar.dtos.impl.CompanyDTOImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CompanyDTOBuilderTest {

    private CompanyDTOBuilder builder;

    @BeforeEach
    public void setUp() {
        builder = new CompanyDTOBuilder();
    }

    @Test
    public void testCreateCompany() {
        builder.createCompany();
        CompanyDTOImpl company = builder.build();
        assertNotNull(company, "Company should not be null after creation");
    }

    @Test
    public void testWithId() {
        builder.createCompany().withId(123);
        CompanyDTOImpl company = builder.build();
        assertEquals(123, company.getId(), "ID should be set correctly");
    }

    @Test
    public void testWithEmail() {
        builder.createCompany().withEmail("test@example.com");
        CompanyDTOImpl company = builder.build();
        assertEquals("test@example.com", company.getEmail(), "Email should be set correctly");
    }

    @Test
    public void testWithStrasse() {
        builder.createCompany().withStrasse("Teststraße");
        CompanyDTOImpl company = builder.build();
        assertEquals("Teststraße", company.getStrasse(), "Strasse should be set correctly");
    }

    @Test
    public void testWithOrt() {
        builder.createCompany().withOrt("Testort");
        CompanyDTOImpl company = builder.build();
        assertEquals("Testort", company.getOrt(), "Ort should be set correctly");
    }

    @Test
    public void testWithPassword() {
        builder.createCompany().withPassword("password123");
        CompanyDTOImpl company = builder.build();
        assertEquals("password123", company.getPassword(), "Password should be set correctly");
    }

    @Test
    public void testWithPLZ() {
        builder.createCompany().withPLZ(12345);
        CompanyDTOImpl company = builder.build();
        assertEquals(12345, company.getPLZ(), "PLZ should be set correctly");
    }

    @Test
    public void testWithHausnummer() {
        builder.createCompany().withHausnummer(10);
        CompanyDTOImpl company = builder.build();
        assertEquals(10, company.getHausnummer(), "Hausnummer should be set correctly");
    }

    @Test
    public void testWithProfilePicture() {
        StreamResource resource = new StreamResource("test.jpg", () -> null);
        builder.createCompany().withProfilePicture(resource);
        CompanyDTOImpl company = builder.build();
        assertEquals(resource, company.getProfilePicture(), "Profile picture should be set correctly");
    }

    @Test
    public void testWithBezeichnung() {
        builder.createCompany().withBezeichnung("TestBezeichnung");
        CompanyDTOImpl company = builder.build();
        assertEquals("TestBezeichnung", company.getBezeichnung(), "Bezeichnung should be set correctly");
    }

    @Test
    public void testWithBranche() {
        builder.createCompany().withBranche("TestBranche");
        CompanyDTOImpl company = builder.build();
        assertEquals("TestBranche", company.getBranche(), "Branche should be set correctly");
    }

    @Test
    public void testWithBio() {
        builder.createCompany().withBio("TestBio");
        CompanyDTOImpl company = builder.build();
        assertEquals("TestBio", company.getBio(), "Bio should be set correctly");
    }

    @Test
    public void testWithPhone() {
        builder.createCompany().withPhone("123456789");
        CompanyDTOImpl company = builder.build();
        assertEquals("123456789", company.getPhone(), "Phone should be set correctly");
    }

    @Test
    public void testWithWebsite() {
        builder.createCompany().withWebsite("www.example.com");
        CompanyDTOImpl company = builder.build();
        assertEquals("www.example.com", company.getWebsite(), "Website should be set correctly");
    }

    @Test
    public void testWithIBAN() {
        builder.createCompany().withIBAN("DE1234567890");
        CompanyDTOImpl company = builder.build();
        assertEquals("DE1234567890", company.getIBAN(), "IBAN should be set correctly");
    }

    @Test
    public void testReset() {
        builder.createCompany().withId(123).reset();
        CompanyDTOImpl company = builder.build();
        assertNotEquals(123, company.getId(), "ID should be reset");
    }

    @Test
    public void testSetFrom() {
        CompanyDTOImpl source = new CompanyDTOImpl();
        source.setId(456);
        source.setEmail("source@example.com");

        builder.setFrom(source);
        CompanyDTOImpl company = builder.build();

        assertEquals(456, company.getId(), "ID should be copied from source");
        assertEquals("source@example.com", company.getEmail(), "Email should be copied from source");
    }
}
