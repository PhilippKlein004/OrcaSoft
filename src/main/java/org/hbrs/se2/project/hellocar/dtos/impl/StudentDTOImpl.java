package org.hbrs.se2.project.hellocar.dtos.impl;

import com.vaadin.flow.server.StreamResource;
import org.hbrs.se2.project.hellocar.dtos.StudentDTO;

import java.io.File;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class StudentDTOImpl extends UserDTOImpl implements StudentDTO {

    private String firstName;
    private String lastName;
    private Date birthDate;
    private int matrikelnummer;
    private String ausbildung;
    private String berufserfahrung;
    private String studiengang;
    private List<String> skills;
    private List<String> sprachen;
    private StreamResource lebenslauf;

    @Override
    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public int getMatrikelnummer() {
        return this.matrikelnummer;
    }

    public void setMatrikelnummer(int matrikelnummer) {
        this.matrikelnummer = matrikelnummer;
    }

    @Override
    public Date getDateOfBirth() {
        return this.birthDate;
    }

    public LocalDate getDateOfBirthAsLocalDate() {
        return LocalDate.of( birthDate.getYear()+1900, birthDate.getMonth()+1, birthDate.getDate());
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.birthDate = dateOfBirth;
    }

    @Override
    public String getAusbildung() {
        return this.ausbildung;
    }

    public void setAusbildung(String ausbildung) {
        this.ausbildung = ausbildung;
    }

    @Override
    public String getBerufserfahrung() {
        return this.berufserfahrung;
    }

    public void setBerufserfahrung(String berufserfahrung) {
        this.berufserfahrung = berufserfahrung;
    }

    @Override
    public String getStudiengang() {
        return this.studiengang;
    }

    @Override
    public List<String> getSkills() {
        return this.skills;
    }

    @Override
    public List<String> getSprachen() {
        return this.sprachen;
    }

    @Override
    public StreamResource getLebenslauf() {
        return this.lebenslauf;
    }

    public void setStudiengang(String studiengang) {
        this.studiengang = studiengang;
    }

    public void setSkills( List<String> skills ) {
        this.skills = skills;
    }

    public void setSprachen( List<String> sprachen ) {
        this.sprachen = sprachen;
    }

    public void setLebenslauf( StreamResource lebenslauf ) {
        this.lebenslauf = lebenslauf;
    }

}
