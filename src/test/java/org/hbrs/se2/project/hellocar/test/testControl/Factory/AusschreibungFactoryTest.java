package org.hbrs.se2.project.hellocar.test.testControl.Factory;

import org.hbrs.se2.project.hellocar.control.factories.AusschreibungFactory;
import org.hbrs.se2.project.hellocar.dtos.impl.AusschreibungDTOForInsert;
import org.hbrs.se2.project.hellocar.dtos.impl.AusschreibungDTOImpl;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class AusschreibungFactoryTest {

    @Test
    public void testCreateAusschreibungDTOImplForInsert() {
        String titel = "Testtitel";
        String beschreibung = "Testbeschreibung";
        String company = "Testfirma";
        List<String> skills = Arrays.asList("Java", "Spring");
        String beschaeftigung = "Vollzeit";
        int verguetung = 5000;
        Date startDate = new Date();
        Date endDate = new Date();

        AusschreibungDTOForInsert ausschreibungDTOForInsert = AusschreibungFactory.createAusschreibungDTOImplForInsert(titel, beschreibung, company, skills, beschaeftigung, verguetung, startDate, endDate);

        assertEquals(titel, ausschreibungDTOForInsert.getTitel());
        assertEquals(beschreibung, ausschreibungDTOForInsert.getBeschreibung());
        assertEquals(company, ausschreibungDTOForInsert.getCompany());
        assertEquals(skills, ausschreibungDTOForInsert.getSkills());
        assertEquals(beschaeftigung, ausschreibungDTOForInsert.getBeschaeftigung());
        assertEquals(startDate, ausschreibungDTOForInsert.getStartDatum());
        assertEquals(endDate, ausschreibungDTOForInsert.getEndDatum());
        assertEquals(Integer.parseInt(String.valueOf(verguetung)), Integer.parseInt(String.valueOf(ausschreibungDTOForInsert.getVerguetung())));
    }

    @Test
    public void testCreateAusschreibungDTOImpl() {
        String titel = "Testtitel";
        String beschreibung = "Testbeschreibung";
        String company = "Testfirma";
        List<String> skills = Arrays.asList("Java", "Spring");
        String beschaeftigung = "Vollzeit";
        int verguetung = 5000;
        Date startDate = new Date();
        Date endDate = new Date();
        int id = 123;

        AusschreibungDTOImpl ausschreibungDTOImpl = AusschreibungFactory.createAusschreibungDTOImpl(titel, beschreibung, company, skills, beschaeftigung, verguetung, startDate, endDate, id);

        assertEquals(titel, ausschreibungDTOImpl.getTitel());
        assertEquals(beschreibung, ausschreibungDTOImpl.getBeschreibung());
        assertEquals(company, ausschreibungDTOImpl.getCompany());
        assertEquals(skills, ausschreibungDTOImpl.getSkills());
        assertEquals(beschaeftigung, ausschreibungDTOImpl.getBeschaeftigung());
        assertEquals(Integer.parseInt(String.valueOf(verguetung)), Integer.parseInt(String.valueOf(ausschreibungDTOImpl.getVerguetung())));
        assertEquals(startDate, ausschreibungDTOImpl.getStartDatum());
        assertEquals(endDate, ausschreibungDTOImpl.getEndDatum());
        assertEquals(Integer.parseInt(String.valueOf(id)), Integer.parseInt(String.valueOf(ausschreibungDTOImpl.getId())));
    }

}