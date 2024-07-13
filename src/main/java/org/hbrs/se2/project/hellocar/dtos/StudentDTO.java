package org.hbrs.se2.project.hellocar.dtos;

import com.vaadin.flow.server.StreamResource;

import java.util.Date;
import java.util.List;

public interface StudentDTO extends UserDTO {
    String getFirstName();
    String getLastName();
    int getMatrikelnummer();
    Date getDateOfBirth();
    String getAusbildung();
    String getBerufserfahrung();
    String getStudiengang();
    List<String> getSkills();
    List<String> getSprachen();
    StreamResource getLebenslauf();
}
