package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.hbrs.se2.project.hellocar.dao.UserDAO;
import org.hbrs.se2.project.hellocar.dtos.CompanyDTO;
import org.hbrs.se2.project.hellocar.dtos.StudentDTO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.StudentDTOImpl;
import org.hbrs.se2.project.hellocar.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class LoginControl {

    @Autowired
    private SessionController sessionController = new SessionController();

    @Autowired
    private UserDAO userDAO = new UserDAO();

    public boolean authenticate( String username, String password ) throws DatabaseException, SQLException {

        UserDTO tmpUser = null;

        try {

            // Wenn beim Extrahieren das ResultSet leer ist → DatabaseException.USER_NOT_FOUND
            tmpUser = userDAO.findUserByUseridAndPassword(username, password);

            if ( userDAO.isUserStudent( tmpUser.getId() ) ) {
                Globals.IS_STUDENT_USER = true;
                System.out.println("[Login] Student hat sich angemeldet.\n");
                tmpUser = userDAO.fuseUserDTOWithExtractedStudentData( tmpUser, tmpUser.getId() );
            } else {
                Globals.IS_STUDENT_USER = false;
                System.out.println("[Login] Unternehmen hat sich angemeldet.\n");
                tmpUser = userDAO.fuseUserDTOWithExtractedCompanyData( tmpUser, tmpUser.getId() );
            }

        } catch ( DatabaseException e ) {
            throw new DatabaseException( e.getMessage() );
        }

        if ( tmpUser != null ) {
            sessionController.setCurrentUser( tmpUser );
            sessionController.redirectToPage( Globals.Pages.SEARCH );
            return true;
        } else {
            return false;
        }

    }


}
