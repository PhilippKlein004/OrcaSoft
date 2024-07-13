package org.hbrs.se2.project.hellocar.dtos;

public interface CompanyDTO extends UserDTO {
    String getBezeichnung();
    String getBranche();
    String getBio();
    String getPhone();
    String getWebsite();
    String getIBAN();
}
