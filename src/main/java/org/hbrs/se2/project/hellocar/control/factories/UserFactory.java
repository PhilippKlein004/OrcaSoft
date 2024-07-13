package org.hbrs.se2.project.hellocar.control.factories;

import com.vaadin.flow.server.StreamResource;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.CompanyDTOImpl;
import org.hbrs.se2.project.hellocar.dtos.impl.StudentDTOImpl;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;

import java.io.File;
import java.util.Date;
import java.util.List;

public abstract class UserFactory {

    public static UserDTO createDefaultUserDTO( int id, String email, List<String> roles ) {

        UserDTOImpl tmp = new UserDTOImpl();
        tmp.setId(id);
        tmp.setEmail(email);
        return tmp;

    }

    public static UserDTO createDefaultUserDTO( int id, String email, String strasse, String ort, int plz, int haus_nr, StreamResource profilePicture ) {

        UserDTOImpl tmp = new UserDTOImpl();
        tmp.setId(id);
        tmp.setEmail(email);
        tmp.setStrasse(strasse);
        tmp.setOrt(ort);
        tmp.setPlz(plz);
        tmp.setHausnummer(haus_nr);
        tmp.setProfilePicture(profilePicture);
        return tmp;

    }

    public static StudentDTOImpl createStudentDTO( int id, String email, String strasse, String ort, int plz, String firstName, String lastName, int matrikelnummer, Date date_of_birth, String ausbildung, String berufserfahrung, String FK_Studiengang, int haus_nr, List<String> skills, List<String> sprachen, StreamResource profilePicture ) {

        StudentDTOImpl tmp = new StudentDTOImpl();
        tmp.setId(id);
        tmp.setEmail(email);
        tmp.setStrasse(strasse);
        tmp.setHausnummer(haus_nr);
        tmp.setOrt(ort);
        tmp.setPlz(plz);

        tmp.setFirstName(firstName);
        tmp.setLastName(lastName);
        tmp.setMatrikelnummer(matrikelnummer);
        tmp.setDateOfBirth(date_of_birth);
        tmp.setAusbildung(ausbildung);
        tmp.setBerufserfahrung(berufserfahrung);
        tmp.setStudiengang(FK_Studiengang);

        tmp.setSkills(skills);
        tmp.setSprachen(sprachen);
        tmp.setProfilePicture(profilePicture);

        return tmp;

    }

    public static StudentDTOImpl createStudentDTO( int id, String email, String strasse, String ort, int plz, String firstName, String lastName, int matrikelnummer, Date date_of_birth, String ausbildung, String berufserfahrung, String FK_Studiengang, int haus_nr, List<String> skills, List<String> sprachen, StreamResource profilePicture, StreamResource lebenslauf ) {

        StudentDTOImpl tmp = UserFactory.createStudentDTO(id, email, strasse, ort, plz, firstName, lastName, matrikelnummer, date_of_birth, ausbildung, berufserfahrung, FK_Studiengang, haus_nr, skills, sprachen, profilePicture);
        tmp.setLebenslauf( lebenslauf );
        return tmp;

    }

    public static CompanyDTOImpl createCompanyDTO( int id, String email, String strasse, String ort, int plz, String FK_Branche, String bezeichnung, String bio, String phone, String website, String iban, int haus_nr, StreamResource profilePicture ) {

        CompanyDTOImpl tmp = new CompanyDTOImpl();
        tmp.setId(id);
        tmp.setEmail(email);
        tmp.setStrasse(strasse);
        tmp.setHausnummer(haus_nr);
        tmp.setOrt(ort);
        tmp.setPlz(plz);

        tmp.setBranche(FK_Branche);
        tmp.setBezeichnung(bezeichnung);
        tmp.setBio(bio);
        tmp.setPhone(phone);
        tmp.setWebsite(website);
        tmp.setIBAN(iban);
        tmp.setProfilePicture(profilePicture);

        return tmp;

    }

    public static StudentDTOImpl createStudentDTOImplRegistration(String email, String password, String FK_Studiengang ) {

        StudentDTOImpl tmp = new StudentDTOImpl();
        tmp.setEmail( email );
        tmp.setPassword( password );
        tmp.setStudiengang( FK_Studiengang );
        return tmp;

    }

    public static CompanyDTOImpl createCompanyDTOImplRegistration(String email, String password, String bezeichnung, String FK_Branche ) {

        CompanyDTOImpl tmp = new CompanyDTOImpl();
        tmp.setEmail( email );
        tmp.setPassword( password );
        tmp.setBezeichnung( bezeichnung );
        tmp.setBranche( FK_Branche );
        return tmp;

    }

}
