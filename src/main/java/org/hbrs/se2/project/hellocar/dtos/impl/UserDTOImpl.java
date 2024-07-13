package org.hbrs.se2.project.hellocar.dtos.impl;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;

import java.util.List;

public class UserDTOImpl implements UserDTO {

    private int id;
    private String email;
    private String strasse;
    private String ort;
    private String passwort;
    private int plz;
    private int hausnummer;
    private StreamResource profilePicture;

    public UserDTOImpl() {}

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) { this.email = email; }

    public void setStrasse(String strasse) { this.strasse = strasse; }

    public void setHausnummer(int hausnummer) { this.hausnummer = hausnummer; }

    public void setOrt(String ort) { this.ort = ort; }

    public void setPlz(int plz) { this.plz = plz; }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "UserDTOImpl{" +
                "id=" + id +
                '}';
    }

    @Override
    public String getEmail() { return this.email; }

    @Override
    public String getPassword() {
        return this.passwort;
    }

    public String setPassword(String password) {
        return this.passwort = password;
    }

    @Override
    public String getStrasse() {
        return this.strasse;
    }

    @Override
    public int getHausnummer() {
        return this.hausnummer;
    }

    @Override
    public String getOrt() {
        return this.ort;
    }

    @Override
    public int getPLZ() {
        return this.plz;
    }

    @Override
    public StreamResource getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture( StreamResource profilePicture ) {
        this.profilePicture = profilePicture;
    }

}
