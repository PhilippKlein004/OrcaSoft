package org.hbrs.se2.project.hellocar.dtos.impl;

import com.vaadin.flow.server.StreamResource;
import org.hbrs.se2.project.hellocar.dtos.BewerbungDTO;

import java.io.File;
import java.util.Date;

public class BewerbungDTOImpl implements BewerbungDTO {

    private int id;
    private int studentID;
    private int ausschreibungID;
    private String inhalt;
    private Date datum;
    private int status;
    private StreamResource motivationsschreibenResource;
    private boolean lebenslaufAttached;
    private StreamResource lebenslaufFile;
    private File motivationsschreibenFile;

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public int getStudentID() {
        return this.studentID;
    }

    @Override
    public int getAusschreibungID() {
        return this.ausschreibungID;
    }

    @Override
    public Date getDate() {
        return this.datum;
    }

    @Override
    public String getInhalt() {
        return this.inhalt;
    }

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public StreamResource getMotivationsschreibenResource() {
        return this.motivationsschreibenResource;
    }

    @Override
    public File getMotivationsschreibenFile() {
        return this.motivationsschreibenFile;
    }

    @Override
    public boolean isLebenslaufAttached() {
        return this.lebenslaufAttached;
    }

    @Override
    public StreamResource getLebenslaufResource() {
        return this.lebenslaufFile;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setDate(Date datum) {
        this.datum = datum;
    }

    public void setInhalt(String inhalt) {
        this.inhalt = inhalt;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setLebenslaufAttached(boolean lebenslauf) {
        this.lebenslaufAttached = lebenslauf;
    }

    public void setMotivationsschreibenResource(StreamResource streamResource) {
        this.motivationsschreibenResource = streamResource;
    }

    public void setLebenslaufFile(StreamResource streamResource) {
        this.lebenslaufFile = streamResource;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public void setAusschreibungID(int ausschreibungID) {
        this.ausschreibungID = ausschreibungID;
    }

    public void setMotivationsschreibenFile(File motivationsschreibenFile) {
        this.motivationsschreibenFile = motivationsschreibenFile;
    }

}
