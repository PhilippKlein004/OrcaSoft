package org.hbrs.se2.project.hellocar.test.testControl.Builder;

import com.vaadin.flow.server.StreamResource;
import org.hbrs.se2.project.hellocar.util.builder.AusschreibungDTOBuilder;
import org.hbrs.se2.project.hellocar.dtos.impl.AusschreibungDTOForInsert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AusschreibungDTOBuilderTest {

    private AusschreibungDTOBuilder builder;

    @BeforeEach
    public void setUp() {
        builder = new AusschreibungDTOBuilder();
    }

    @Test
    public void testCreateAusschreibung() {
        builder.createAusschreibung();
        assertNotNull(builder.build());
    }

    @Test
    public void testWithId() {
        builder.createAusschreibung().withId(1);
        assertEquals(1, builder.build().getId());
    }

    @Test
    public void testWithBeschaeftigung() {
        builder.createAusschreibung().withBeschaeftigung("Developer");
        assertEquals("Developer", builder.build().getBeschaeftigung());
    }

    @Test
    public void testWithCompany() {
        builder.createAusschreibung().withCompany("Test Company");
        assertEquals("Test Company", builder.build().getCompany());
    }

    @Test
    public void testWithCompanyLogo() {
        StreamResource logo = new StreamResource("logo.png", () -> null);
        builder.createAusschreibung().withCompanyLogo(logo);
        assertEquals(logo, builder.build().getCompanyLogo());
    }

    @Test
    public void testWithTitel() {
        builder.createAusschreibung().withTitel("Test Titel");
        assertEquals("Test Titel", builder.build().getTitel());
    }

    @Test
    public void testWithBeschreibung() {
        builder.createAusschreibung().withBeschreibung("Test Beschreibung");
        assertEquals("Test Beschreibung", builder.build().getBeschreibung());
    }

    @Test
    public void testWithStartDatum() {
        Date startDate = new Date();
        builder.createAusschreibung().withStartDatum(startDate);
        assertEquals(startDate, builder.build().getStartDatum());
    }

    @Test
    public void testWithEndDatum() {
        Date endDate = new Date();
        builder.createAusschreibung().withEndDatum(endDate);
        assertEquals(endDate, builder.build().getEndDatum());
    }

    @Test
    public void testWithVerguetung() {
        builder.createAusschreibung().withVerguetung(5000);
        assertEquals(5000, builder.build().getVerguetung());
    }

    @Test
    public void testWithSkills() {
        List<String> skills = Arrays.asList("Java", "Spring");
        builder.createAusschreibung().withSkills(skills);
        assertEquals(skills, builder.build().getSkills());
    }

    @Test
    public void testBuild() {
        AusschreibungDTOForInsert dto = builder.createAusschreibung()
                .withId(1)
                .withBeschaeftigung("Developer")
                .withCompany("Test Company")
                .withTitel("Test Titel")
                .withBeschreibung("Test Beschreibung")
                .withStartDatum(new Date())
                .withEndDatum(new Date())
                .withVerguetung(5000)
                .withSkills(Arrays.asList("Java", "Spring"))
                .build();

        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("Developer", dto.getBeschaeftigung());
        assertEquals("Test Company", dto.getCompany());
        assertEquals("Test Titel", dto.getTitel());
        assertEquals("Test Beschreibung", dto.getBeschreibung());
        assertNotNull(dto.getStartDatum());
        assertNotNull(dto.getEndDatum());
        assertEquals(5000, dto.getVerguetung());
        assertEquals(Arrays.asList("Java", "Spring"), dto.getSkills());
    }

    @Test
    public void testReset() {
        builder.createAusschreibung().withId(1);
        builder.reset();
        assertNotEquals(1, builder.build().getId());
    }

    @Test
    public void testSetFrom() {
        AusschreibungDTOForInsert original = new AusschreibungDTOForInsert();
        original.setId(1);
        original.setBeschaeftigung("Developer");
        original.setCompany("Test Company");
        original.setTitel("Test Titel");
        original.setBeschreibung("Test Beschreibung");
        original.setStartDatum(new Date());
        original.setEndDatum(new Date());
        original.setVerguetung(5000);
        original.setSkills(Arrays.asList("Java", "Spring"));

        builder.createAusschreibung().setFrom(original);

        AusschreibungDTOForInsert dto = builder.build();
        assertEquals(original.getId(), dto.getId());
        assertEquals(original.getBeschaeftigung(), dto.getBeschaeftigung());
        assertEquals(original.getCompany(), dto.getCompany());
        assertEquals(original.getTitel(), dto.getTitel());
        assertEquals(original.getBeschreibung(), dto.getBeschreibung());
        assertEquals(original.getStartDatum(), dto.getStartDatum());
        assertEquals(original.getEndDatum(), dto.getEndDatum());
        assertEquals(original.getVerguetung(), dto.getVerguetung());
        assertEquals(original.getSkills(), dto.getSkills());
    }
}
