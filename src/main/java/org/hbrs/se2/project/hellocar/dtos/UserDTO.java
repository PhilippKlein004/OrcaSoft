package org.hbrs.se2.project.hellocar.dtos;

import com.vaadin.flow.server.StreamResource;

public interface UserDTO {
    int getId();
    String getEmail();
    String getPassword();
    String getStrasse();
    int getHausnummer();
    String getOrt();
    int getPLZ();
    StreamResource getProfilePicture();
}
