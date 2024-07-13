package org.hbrs.se2.project.hellocar.dtos;

import com.vaadin.flow.server.StreamResource;

import java.util.Date;
import java.util.List;

public interface AusschreibungDTO {
    Integer getId();
    String getBeschaeftigung();
    String getCompany();
    StreamResource getCompanyLogo();
    String getTitel();
    String getBeschreibung();
    Date getStartDatum();
    Date getEndDatum();
    Integer getVerguetung();
    List<String> getSkills();
}
