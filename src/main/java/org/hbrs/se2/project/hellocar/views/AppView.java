package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.hbrs.se2.project.hellocar.control.SessionController;
import org.hbrs.se2.project.hellocar.dtos.CompanyDTO;
import org.hbrs.se2.project.hellocar.dtos.StudentDTO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.util.Globals;

/**
 * The main view is a top-level placeholder for other views.
 */
@CssImport("./styles/views/main/main-view.css")
@Route("main")
@JsModule("./styles/shared-styles.js")
public class AppView extends AppLayout implements BeforeEnterObserver {

    private SessionController authorizationControl;
    private final Avatar avatar = new Avatar();

    public AppView() {
        if (getCurrentUser() == null) {
            System.out.println("LOG: In Constructor of App View - No User given!");
        } else {
            setUpUI();
        }
    }

    public void setUpUI() {
        // Anzeige des Toggles über den Drawer
        setPrimarySection(Section.NAVBAR);

        // Erstellung der horizontalen Statusleiste (Header)
        addToNavbar(true, createHeaderContent());
    }

    private boolean checkIfUserIsLoggedIn() {
        // Falls der Benutzer nicht eingeloggt ist, dann wird er auf die Startseite gelenkt
        UserDTO userDTO = this.getCurrentUser();
        if (userDTO == null) {
            UI.getCurrent().navigate(Globals.Pages.LOGIN_VIEW);
            return false;
        }
        return true;
    }

    /**
     * Erzeugung der horizontalen Leiste (Header).
     * @return
     */
    private Component createHeaderContent() {

        // Ein paar Grund-Einstellungen. Alles wird in ein horizontales Layout gesteckt.
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("100%");
        layout.setId("header");
        layout.getThemeList().set("bright", true);
        layout.getStyle().set("box-shadow", "none");
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode( FlexComponent.JustifyContentMode.EVENLY );
        layout.addClassName(LumoUtility.Gap.XSMALL);
        layout.addClassName(LumoUtility.Padding.XSMALL);
        layout.setHeight("65px");

        // Interner Layout
        HorizontalLayout topLeftPanel = new HorizontalLayout();
        topLeftPanel.setWidth("15%");
        topLeftPanel.setJustifyContentMode( FlexComponent.JustifyContentMode.START );
        topLeftPanel.setAlignItems( FlexComponent.Alignment.CENTER );
        topLeftPanel.getStyle().set("margin-left", "15px");

        HorizontalLayout topMiddlePanel = new HorizontalLayout();
        topMiddlePanel.setWidth("70%");
        topMiddlePanel.setJustifyContentMode( FlexComponent.JustifyContentMode.EVENLY );
        topMiddlePanel.setAlignItems( FlexComponent.Alignment.CENTER );

        HorizontalLayout topRightPanel = new HorizontalLayout();
        topRightPanel.setWidth("15%");
        topRightPanel.setJustifyContentMode( FlexComponent.JustifyContentMode.END );
        topRightPanel.setAlignItems( FlexComponent.Alignment.CENTER );
        topRightPanel.getStyle().set("margin-right", "15px");


        // Layout Content
        Button buttonPrimary = new Button();
        Image logo = new Image();
        authorizationControl = new SessionController();

        buttonPrimary.setText("Neue Ausschreibung");
        buttonPrimary.setWidth("200px");
        buttonPrimary.setHeight("45px");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonPrimary.getStyle().set("cursor", "pointer");

        avatar.setName(this.getCurrentNameOfUser());
        avatar.setWidth("45px");
        avatar.setHeight("45px");
        avatar.addClassName("headeravatar");

        logo.setSrc("images/OrcaSoftLogo.png");
        logo.setHeight("45px");

        topRightPanel.setAlignSelf(FlexComponent.Alignment.CENTER, buttonPrimary);
        topRightPanel.setAlignSelf(FlexComponent.Alignment.CENTER, avatar);

        if ( !Globals.IS_STUDENT_USER ) {
            topRightPanel.add(buttonPrimary);
        }

        topRightPanel.add(avatar);
        topLeftPanel.add(logo);

        layout.add( topLeftPanel );
        layout.add( topMiddlePanel );
        layout.add( topRightPanel );


        // Erstelle ein Div-Element, das den Avatar enthält
        Div avatarContainer = new Div(avatar);
        avatarContainer.setHeight("45px");
        avatarContainer.setWidth("45px");

        // Füge das ContextMenu hinzu
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setTarget(avatarContainer);
        contextMenu.setOpenOnClick(true);
        MenuItem profil = contextMenu.addItem("Profil", event -> authorizationControl.redirectToPage(Globals.Pages.PROFIL));
        profil.getStyle().set("cursor", "pointer");

        contextMenu.add(new Hr());
        MenuItem logout = contextMenu.addItem("Logout", event -> {
            logoutUser();
            System.out.println("[Login] Nutzer hat sich vom System abgemeldet.");
        });
        logout.getStyle().set("cursor", "pointer");
        logout.setClassName("text-error");

        // Füge das Div-Element zum Layout hinzu
        topRightPanel.add(avatarContainer);

        /*
         * Listener
         */

        logo.addClickListener(e -> authorizationControl.redirectToPage(Globals.Pages.SEARCH));

        buttonPrimary.addClickListener(e -> {
            StellenangebotView.ausschreibungDTO = null;
            authorizationControl.redirectToPage( Globals.Pages.STELLENANGEBOT );
        });

        logo.getStyle().set("cursor", "pointer");
        avatar.getStyle().set("cursor", "pointer");
        avatar.setImageResource( getCurrentUser().getProfilePicture() );
        logo.addClassName("orcasoftlogo");

        return layout;
    }

    private void logoutUser() {
        UI ui = this.getUI().get();
        ui.getSession().close();
        ui.getPage().setLocation(Globals.Pages.LOGIN_VIEW);
    }
   
    @Override
    protected void afterNavigation() {
        super.afterNavigation();

        // Falls der Benutzer nicht eingeloggt ist, dann wird er auf die Startseite gelenkt
        if ( !checkIfUserIsLoggedIn() ) return;
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    private String getCurrentNameOfUser() {
        if ( !Globals.IS_STUDENT_USER ) return ((CompanyDTO) this.getCurrentUser()).getBezeichnung();
        return ((StudentDTO) this.getCurrentUser()).getFirstName();
    }

    private UserDTO getCurrentUser() {
        return (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
    }


    @Override
    /**
     * Methode wird vor der eigentlichen Darstellung der UI-Components aufgerufen.
     * Hier kann man die finale Darstellung noch abbrechen, wenn z.B. der Nutzer nicht eingeloggt ist
     * Dann erfolgt hier ein ReDirect auf die Login-Seite. Eine Navigation (Methode navigate)
     * ist hier nicht möglich, da die finale Navigation noch nicht stattgefunden hat.
     * Diese Methode in der AppLayout sichert auch den un-authorisierten Zugriff auf die innerliegenden
     * Views (hier: ShowCarsView und EnterCarView) ab.
     *
     */
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (getCurrentUser() == null){
            beforeEnterEvent.rerouteTo(Globals.Pages.LOGIN_VIEW);
        }
    }
}
