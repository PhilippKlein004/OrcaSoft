package org.hbrs.se2.project.hellocar.dtos.impl;

import com.vaadin.flow.component.html.Image;
import org.hbrs.se2.project.hellocar.dtos.CompanyDTO;

public class CompanyDTOImpl extends UserDTOImpl implements CompanyDTO {

    private String bezeichnung;
    private String branche;
    private String bio;
    private String phone;
    private String website;
    private String IBAN;

    @Override
    public String getBranche() {
        return this.branche;
    }

    public void setBranche(String branche) {
        this.branche = branche;
    }

    @Override
    public String getBezeichnung() {
        return this.bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    @Override
    public String getBio() {
        return this.bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getWebsite() {
        return this.website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String getIBAN() {
        return this.IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

}
