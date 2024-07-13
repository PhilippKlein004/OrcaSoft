package org.hbrs.se2.project.hellocar.control.factories;

import com.vaadin.flow.server.StreamResource;
import org.hbrs.se2.project.hellocar.dtos.BewerbungDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.BewerbungDTOImpl;

import java.io.File;
import java.util.Date;

public abstract class BewerbungFactory {

    public static BewerbungDTOImpl createBewerbungForInsert( Date datum, String inhalt, int status, File motivationsschreiben, boolean isLebenslaufAttached, int ID_Student, int ID_Ausschreibung ) {
        BewerbungDTOImpl tmp = new BewerbungDTOImpl();
        tmp.setDate(datum);
        tmp.setInhalt(inhalt);
        tmp.setStatus(status);
        tmp.setMotivationsschreibenFile(motivationsschreiben);
        tmp.setLebenslaufAttached(isLebenslaufAttached);
        tmp.setStudentID(ID_Student);
        tmp.setAusschreibungID(ID_Ausschreibung);
        return tmp;
    }

    public static BewerbungDTOImpl createBewerbung( int ID, Date datum, String inhalt, int status, StreamResource motivationsschreiben, boolean isLebenslaufAttached, StreamResource lebenslauf, int studentID ) {
        BewerbungDTOImpl tmp = new BewerbungDTOImpl();
        tmp.setID(ID);
        tmp.setDate(datum);
        tmp.setInhalt(inhalt);
        tmp.setStatus(status);
        tmp.setMotivationsschreibenResource(motivationsschreiben);
        tmp.setLebenslaufAttached(isLebenslaufAttached);
        tmp.setLebenslaufFile(lebenslauf);
        tmp.setStudentID(studentID);
        return tmp;
    }

}
