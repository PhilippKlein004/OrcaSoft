package org.hbrs.se2.project.hellocar.dtos.impl;

import com.vaadin.flow.server.StreamResource;
import org.hbrs.se2.project.hellocar.dtos.AusschreibungDTO;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class AusschreibungDTOImpl implements AusschreibungDTO {

    private int id;
    private String title;
    private String beschaeftigung;
    private String company;
    private StreamResource companyLogo;
    private String beschreibung;
    private Date startDatum;
    private Date endDatum;
    private int verguetung;
    private List<String> skills;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getBeschaeftigung() {
        return beschaeftigung;
    }

    @Override
    public String getCompany() {
        return company;
    }

    @Override
    public StreamResource getCompanyLogo() {
        return companyLogo;
    }

    @Override
    public String getTitel() {
        return title;
    }

    @Override
    public String getBeschreibung() {
        return beschreibung;
    }

    @Override
    public Date getStartDatum() {
        return startDatum;
    }

    public LocalDate getStartDatumLocal() {
        return LocalDate.of( startDatum.getYear()+1900, startDatum.getMonth()+1, startDatum.getDate());
    }

    @Override
    public Date getEndDatum() {
        return endDatum;
    }

    public LocalDate getEndDatumLocal() {
        return LocalDate.of( endDatum.getYear()+1900, endDatum.getMonth()+1, endDatum.getDate());
    }

    @Override
    public Integer getVerguetung() {
        return verguetung;
    }

    @Override
    public List<String> getSkills() {
        return skills;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBeschaeftigung(String beschaeftigung) {
        this.beschaeftigung = beschaeftigung;
    }

    public void setCompanyLogo(StreamResource companyLogo) {
        this.companyLogo = companyLogo;
    }

    public void setTitel(String titel) {
        this.title = titel;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public void setStartDatum(Date startDatum) {
        this.startDatum = startDatum;
    }

    public void setEndDatum(Date endDatum) {
        this.endDatum = endDatum;
    }

    public void setVerguetung(int verguetung) {
        this.verguetung = verguetung;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public void setCompany(String company) {
        this.company = company;
    }

}
