package org.hbrs.se2.project.hellocar.control;

import com.vaadin.flow.component.UI;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.util.Globals;
import org.springframework.stereotype.Component;

@Component
public class SessionController {

    private String currentPage = "";

    /**
     * Gibt den aktuell angemeldeten Nutzer zurück.
     *
     * @return aktuell angemeldeter Nutzer.
     */

    public UserDTO getCurrentUser() {
        return (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
    }

    /**
     * Meldet einen übergebenen Nutzer in das System ein.
     *
     * @param userDTO Nutzer der angemeldet wird.
     */

    public void setCurrentUser( UserDTO userDTO ) {
        UI.getCurrent().getSession().setAttribute( Globals.CURRENT_USER, userDTO );
    }

    /**
     * Lädt die Seite neu.
     */

    public void refreshPage() {
        UI.getCurrent().getPage().reload();
    }

    /**
     * Gibt den Namen der aktuellen Seite zurück.
     *
     * @return Name der Seite.
     */

    public String getPage() {
        return currentPage;
    }

    /**
     * Leitet den Nutzer zu einer übergebenen Seite weiter.
     *
     * @param pageName Name der Seite. Bitte immer aus der Klasse Globals beziehen!
     */

    public void redirectToPage( String pageName ) {
        currentPage = pageName;
        UI.getCurrent().navigate( pageName );
    }

    /**
     * Meldet den aktuell angemeldeten Nutzer ab bzw.
     * schließt die aktuelle Session.
     */

    public void logOutUser() {
        UI.getCurrent().getSession().close();
    }

}
