package org.hbrs.se2.project.hellocar.dtos;

import com.vaadin.flow.server.StreamResource;

import java.io.File;
import java.util.Date;

public interface BewerbungDTO {
    int getID();
    int getStudentID();
    int getAusschreibungID();
    Date getDate();
    String getInhalt();
    int getStatus();
    StreamResource getMotivationsschreibenResource();
    File getMotivationsschreibenFile();
    boolean isLebenslaufAttached();
    StreamResource getLebenslaufResource();
}
