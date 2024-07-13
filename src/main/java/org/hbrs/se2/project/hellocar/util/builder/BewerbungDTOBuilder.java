package org.hbrs.se2.project.hellocar.util.builder;

import com.vaadin.flow.server.StreamResource;
import org.hbrs.se2.project.hellocar.dtos.impl.BewerbungDTOImpl;

import java.io.File;
import java.util.Date;

public class BewerbungDTOBuilder implements Builder<BewerbungDTOImpl> {

    private BewerbungDTOImpl bewerbungDTO;

    public BewerbungDTOBuilder createBewerbung() {
        this.bewerbungDTO = new BewerbungDTOImpl();
        return this;
    }

    public BewerbungDTOBuilder withID(int id) {
        this.bewerbungDTO.setID(id);
        return this;
    }

    public BewerbungDTOBuilder withStudentID(int studentID) {
        this.bewerbungDTO.setStudentID(studentID);
        return this;
    }

    public BewerbungDTOBuilder withAusschreibungID(int ausschreibungID) {
        this.bewerbungDTO.setAusschreibungID(ausschreibungID);
        return this;
    }

    public BewerbungDTOBuilder withInhalt(String inhalt) {
        this.bewerbungDTO.setInhalt(inhalt);
        return this;
    }

    public BewerbungDTOBuilder withDate(Date datum) {
        this.bewerbungDTO.setDate(datum);
        return this;
    }

    public BewerbungDTOBuilder withStatus(int status) {
        this.bewerbungDTO.setStatus(status);
        return this;
    }

    public BewerbungDTOBuilder withMotivationsschreibenResource(StreamResource motivationsschreibenResource) {
        this.bewerbungDTO.setMotivationsschreibenResource(motivationsschreibenResource);
        return this;
    }

    public BewerbungDTOBuilder withLebenslaufAttached(boolean lebenslaufAttached) {
        this.bewerbungDTO.setLebenslaufAttached(lebenslaufAttached);
        return this;
    }

    public BewerbungDTOBuilder withLebenslaufFile(StreamResource lebenslaufFile) {
        this.bewerbungDTO.setLebenslaufFile(lebenslaufFile);
        return this;
    }

    public BewerbungDTOBuilder withMotivationsschreibenFile(File motivationsschreibenFile) {
        this.bewerbungDTO.setMotivationsschreibenFile(motivationsschreibenFile);
        return this;
    }

    public BewerbungDTOImpl build() {
        return this.bewerbungDTO;
    }

    public void reset() {
        this.bewerbungDTO = new BewerbungDTOImpl();
    }

    public BewerbungDTOBuilder setFrom(BewerbungDTOImpl instance) {
        this.bewerbungDTO.setID(instance.getID());
        this.bewerbungDTO.setStudentID(instance.getStudentID());
        this.bewerbungDTO.setAusschreibungID(instance.getAusschreibungID());
        this.bewerbungDTO.setInhalt(instance.getInhalt());
        this.bewerbungDTO.setDate(instance.getDate());
        this.bewerbungDTO.setStatus(instance.getStatus());
        this.bewerbungDTO.setMotivationsschreibenResource(instance.getMotivationsschreibenResource());
        this.bewerbungDTO.setLebenslaufAttached(instance.isLebenslaufAttached());
        this.bewerbungDTO.setLebenslaufFile(instance.getLebenslaufResource());
        this.bewerbungDTO.setMotivationsschreibenFile(instance.getMotivationsschreibenFile());
        return this;
    }
}
