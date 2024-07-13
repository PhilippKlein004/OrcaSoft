package org.hbrs.se2.project.hellocar.util.builder;

import com.vaadin.flow.server.StreamResource;
import org.hbrs.se2.project.hellocar.dtos.impl.CompanyDTOImpl;

public class CompanyDTOBuilder implements Builder<CompanyDTOImpl>{

    private CompanyDTOImpl company;

     public CompanyDTOBuilder createCompany() {
          company = new CompanyDTOImpl();
          return this;
     }

     public CompanyDTOBuilder withId(int id) {
         company.setId(id);
         return this;
     }

    public CompanyDTOBuilder withEmail(String email) {
        company.setEmail(email);
        return this;
    }

    public CompanyDTOBuilder withStrasse(String strasse) {
         company.setStrasse(strasse);
         return this;
    }

    public CompanyDTOBuilder withOrt(String ort) {
         company.setOrt(ort);
         return this;
    }

    public CompanyDTOBuilder withPassword(String password) {
         company.setPassword(password);
         return this;
    }

    public CompanyDTOBuilder withPLZ(int plz) {
         company.setPlz(plz);
         return this;
    }

    public CompanyDTOBuilder withHausnummer(int hausnummer) {
         company.setHausnummer(hausnummer);
         return this;
    }

    public CompanyDTOBuilder withProfilePicture(StreamResource profilePicture) {
         company.setProfilePicture(profilePicture);
         return this;
    }


     public CompanyDTOBuilder withBezeichnung(String bezeichnung) {
          company.setBezeichnung(bezeichnung);
          return this;
     }

     public CompanyDTOBuilder withBranche(String branche) {
          company.setBranche(branche);
          return this;
     }

     public CompanyDTOBuilder withBio(String bio) {
          company.setBio(bio);
          return this;
     }

     public CompanyDTOBuilder withPhone(String phone) {
         company.setPhone(phone);
         return this;
     }

     public CompanyDTOBuilder withWebsite(String website) {
         company.setWebsite(website);
         return this;
     }

     public CompanyDTOBuilder withIBAN(String IBAN) {
         company.setIBAN(IBAN);
         return this;
     }


    @Override
    public CompanyDTOImpl build() {
        return company;
    }

    @Override
    public void reset() {
        company = new CompanyDTOImpl();
    }

    @Override
    public Builder<CompanyDTOImpl> setFrom(CompanyDTOImpl instance) {
        company = new CompanyDTOImpl();
        company.setId(instance.getId());
        company.setEmail(instance.getEmail());
        company.setStrasse(instance.getStrasse());
        company.setOrt(instance.getOrt());
        company.setPassword(instance.getPassword());
        company.setPlz(instance.getPLZ());
        company.setHausnummer(instance.getHausnummer());
        company.setProfilePicture(instance.getProfilePicture());
        company.setBezeichnung(instance.getBezeichnung());
        company.setBranche(instance.getBranche());
        company.setBio(instance.getBio());
        company.setPhone(instance.getPhone());
        company.setWebsite(instance.getWebsite());
        company.setIBAN(instance.getIBAN());
        return this;
    }
}
