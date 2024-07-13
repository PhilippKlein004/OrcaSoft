package org.hbrs.se2.project.hellocar.util.builder;

import com.vaadin.flow.server.StreamResource;
import org.hbrs.se2.project.hellocar.dtos.impl.StudentDTOImpl;

import java.util.Date;
import java.util.List;

public class StudentDTOBuilder implements Builder<StudentDTOImpl>{

    private StudentDTOImpl student;

    public StudentDTOBuilder createStudent() {
        student = new StudentDTOImpl();
        return this;
    }
    public StudentDTOBuilder withId(int id) {
        student.setId(id);
        return this;
    }

    public StudentDTOBuilder withEmail(String email) {
        student.setEmail(email);
        return this;
    }

    public StudentDTOBuilder withStrasse(String strasse) {
        student.setStrasse(strasse);
        return this;
    }

    public StudentDTOBuilder withOrt(String ort) {
        student.setOrt(ort);
        return this;
    }

    public StudentDTOBuilder withPassword(String password) {
        student.setPassword(password);
        return this;
    }

    public StudentDTOBuilder withPlz(int plz) {
        student.setPlz(plz);
        return this;
    }

    public StudentDTOBuilder withHausnummer(int hausnummer) {
        student.setHausnummer(hausnummer);
        return this;
    }

    public StudentDTOBuilder withProfilePicture(StreamResource profilePicture) {
        student.setProfilePicture(profilePicture);
        return this;
    }
    public StudentDTOBuilder withfirstName(String firstName) {
        student.setFirstName(firstName);
        return this;
    }

    public StudentDTOBuilder withLastName(String lastName) {
        student.setLastName(lastName);
        return this;
    }

    public StudentDTOBuilder withDateOfBirth(Date dateOfBirth) {
        student.setDateOfBirth(dateOfBirth);
        return this;
    }

    public StudentDTOBuilder withMatrikelnummer(int matrikelnummer) {
        student.setMatrikelnummer(matrikelnummer);
        return this;
    }

    public StudentDTOBuilder withAusbildung(String ausbildung) {
        student.setAusbildung(ausbildung);
        return this;
    }

    public StudentDTOBuilder withBerufserfahrung(String berufserfahrung) {
        student.setBerufserfahrung(berufserfahrung);
        return this;
    }

    public StudentDTOBuilder withStudiengang(String studiengang) {
        student.setStudiengang(studiengang);
        return this;
    }

    public StudentDTOBuilder withSkills(List<String> skills) {
        student.setSkills(skills);
        return this;
    }

    public StudentDTOBuilder withSprachen(List<String> sprachen) {
        student.setSprachen(sprachen);
        return this;
    }

    @Override
    public StudentDTOImpl build() {
        return student;
    }

    @Override
    public void reset() {
        student = new StudentDTOImpl();
    }

    @Override
    public StudentDTOBuilder setFrom(StudentDTOImpl instance) {
        student = new StudentDTOImpl();
        student.setId(instance.getId());
        student.setEmail(instance.getEmail());
        student.setStrasse(instance.getStrasse());
        student.setOrt(instance.getOrt());
        student.setPassword(instance.getPassword());
        student.setPlz(instance.getPLZ());
        student.setHausnummer(instance.getHausnummer());
        student.setProfilePicture(instance.getProfilePicture());
        student.setFirstName(instance.getFirstName());
        student.setLastName(instance.getLastName());
        student.setMatrikelnummer(instance.getMatrikelnummer());
        student.setAusbildung(instance.getAusbildung());
        student.setBerufserfahrung(instance.getBerufserfahrung());
        student.setStudiengang(instance.getStudiengang());
        student.setSkills(instance.getSkills());
        student.setSprachen(instance.getSprachen());
        return this;
    }
}
