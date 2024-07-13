package org.hbrs.se2.project.hellocar.control.factories;

import org.hbrs.se2.project.hellocar.dtos.AusschreibungDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.AusschreibungDTOForInsert;
import org.hbrs.se2.project.hellocar.dtos.impl.AusschreibungDTOImpl;

import java.util.Date;
import java.util.List;

public abstract class AusschreibungFactory {

    public static AusschreibungDTOForInsert createAusschreibungDTOImplForInsert(String titel, String beschreibung, String company, List<String> skills, String beschaeftigung, int verguetung, Date startDate, Date endDate) {

        AusschreibungDTOForInsert dto = new AusschreibungDTOForInsert();
        dto.setTitel(titel);
        dto.setBeschreibung(beschreibung);
        dto.setSkills(skills);
        dto.setBeschaeftigung(beschaeftigung);
        dto.setVerguetung(verguetung);
        dto.setStartDatum(startDate);
        dto.setEndDatum(endDate);
        dto.setCompany(company);

        return dto;

    }

    public static AusschreibungDTOImpl createAusschreibungDTOImpl(String titel, String beschreibung, String company, List<String> skills, String beschaeftigung, int verguetung, Date startDate, Date endDate, int ID) {

        AusschreibungDTOImpl dto = new AusschreibungDTOImpl();
        dto.setTitel(titel);
        dto.setBeschreibung(beschreibung);
        dto.setSkills(skills);
        dto.setBeschaeftigung(beschaeftigung);
        dto.setVerguetung(verguetung);
        dto.setStartDatum(startDate);
        dto.setEndDatum(endDate);
        dto.setCompany(company);
        dto.setId(ID);

        return dto;

    }

}
