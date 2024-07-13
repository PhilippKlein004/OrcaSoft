package org.hbrs.se2.project.hellocar.util.builder;

import com.vaadin.flow.server.StreamResource;
import org.hbrs.se2.project.hellocar.dtos.impl.AusschreibungDTOForInsert;

import java.util.Date;
import java.util.List;

public class AusschreibungDTOBuilder implements Builder<AusschreibungDTOForInsert> {

    private AusschreibungDTOForInsert ausschreibung;

    public AusschreibungDTOBuilder createAusschreibung() {
        this.ausschreibung = new AusschreibungDTOForInsert();
        return this;
    }

    public AusschreibungDTOBuilder withId(int id) {
        this.ausschreibung.setId(id);
        return this;
    }

    public AusschreibungDTOBuilder withBeschaeftigung(String beschaeftigung) {
        this.ausschreibung.setBeschaeftigung(beschaeftigung);
        return this;
    }

    public AusschreibungDTOBuilder withCompany(String company) {
        this.ausschreibung.setCompany(company);
        return this;
    }

    public AusschreibungDTOBuilder withCompanyLogo(StreamResource companyLogo) {
        this.ausschreibung.setCompanyLogo(companyLogo);
        return this;
    }

    public AusschreibungDTOBuilder withTitel(String titel) {
        this.ausschreibung.setTitel(titel);
        return this;
    }

    public AusschreibungDTOBuilder withBeschreibung(String beschreibung) {
        this.ausschreibung.setBeschreibung(beschreibung);
        return this;
    }

    public AusschreibungDTOBuilder withStartDatum(Date startDatum) {
        this.ausschreibung.setStartDatum(startDatum);
        return this;
    }

    public AusschreibungDTOBuilder withEndDatum(Date endDatum) {
        this.ausschreibung.setEndDatum(endDatum);
        return this;
    }

    public AusschreibungDTOBuilder withVerguetung(int verguetung) {
        this.ausschreibung.setVerguetung(verguetung);
        return this;
    }

    public AusschreibungDTOBuilder withSkills(List<String> skills) {
        this.ausschreibung.setSkills(skills);
        return this;
    }

    public AusschreibungDTOForInsert build() {
        return this.ausschreibung;
    }

    public void reset() {
        this.ausschreibung = new AusschreibungDTOForInsert();
    }

    public AusschreibungDTOBuilder setFrom(AusschreibungDTOForInsert instance) {
        this.ausschreibung.setId(instance.getId());
        this.ausschreibung.setBeschaeftigung(instance.getBeschaeftigung());
        this.ausschreibung.setCompany(instance.getCompany());
        this.ausschreibung.setCompanyLogo(instance.getCompanyLogo());
        this.ausschreibung.setTitel(instance.getTitel());
        this.ausschreibung.setBeschreibung(instance.getBeschreibung());
        this.ausschreibung.setStartDatum(instance.getStartDatum());
        this.ausschreibung.setEndDatum(instance.getEndDatum());
        this.ausschreibung.setVerguetung(instance.getVerguetung());
        this.ausschreibung.setSkills(instance.getSkills());
        return this;
    }
}
