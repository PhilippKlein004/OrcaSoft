package org.hbrs.se2.project.hellocar.test;

import com.vaadin.flow.server.StreamResource;
import org.hbrs.se2.project.hellocar.dtos.impl.AusschreibungDTOImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AusschreibungDTOImplTest {

    private AusschreibungDTOImpl dto;

    @BeforeEach
    public void setUp() {
        dto = new AusschreibungDTOImpl();
    }

    @Test
    public void testGetAndSetId() {
        int id = 1;
        dto.setId(id);
        assertEquals(id, dto.getId());
    }

    @Test
    public void testGetAndSetBeschaeftigung() {
        String beschaeftigung = "Testbeschaeftigung";
        dto.setBeschaeftigung(beschaeftigung);
        assertEquals(beschaeftigung, dto.getBeschaeftigung());
    }

    @Test
    public void testGetAndSetCompany() {
        String company = "TestCompany";
        dto.setCompany(company);
        assertEquals(company, dto.getCompany());
    }

    @Test
    public void testGetAndSetCompanyLogo() {
        StreamResource companyLogo = new StreamResource("logo", () -> null);
        dto.setCompanyLogo(companyLogo);
        assertEquals(companyLogo, dto.getCompanyLogo());
    }

    @Test
    public void testGetAndSetTitel() {
        String titel = "TestTitel";
        dto.setTitel(titel);
        assertEquals(titel, dto.getTitel());
    }

    @Test
    public void testGetAndSetBeschreibung() {
        String beschreibung = "Testbeschreibung";
        dto.setBeschreibung(beschreibung);
        assertEquals(beschreibung, dto.getBeschreibung());
    }

    @Test
    public void testGetAndSetStartDatum() {
        Date startDatum = new Date();
        dto.setStartDatum(startDatum);
        assertEquals(startDatum, dto.getStartDatum());
    }

    @Test
    public void testGetAndSetEndDatum() {
        Date endDatum = new Date();
        dto.setEndDatum(endDatum);
        assertEquals(endDatum, dto.getEndDatum());
    }

    @Test
    public void testGetAndSetVerguetung() {
        int verguetung = 1000;
        dto.setVerguetung(verguetung);
        assertEquals(verguetung, dto.getVerguetung());
    }

    @Test
    public void testGetAndSetSkills() {
        List<String> skills = new ArrayList<>();
        skills.add("Skill1");
        skills.add("Skill2");
        dto.setSkills(skills);
        assertEquals(skills, dto.getSkills());
    }

    @Test
    public void testGetAndSetCompanyNull() {
        dto.setCompany(null);
        assertNull(dto.getCompany());
    }

    @Test
    public void testGetStartDatumLocal() {
        Date startDatum = new Date();
        dto.setStartDatum(startDatum);
        assertEquals(startDatum.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate(), dto.getStartDatumLocal());
    }

    @Test
    public void testGetEndDatumLocal() {
        Date endDatum = new Date();
        dto.setEndDatum(endDatum);
        assertEquals(endDatum.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate(), dto.getEndDatumLocal());
    }
}
