package org.hbrs.se2.project.hellocar.test.testUtil;

import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.hbrs.se2.project.hellocar.util.AvatarItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class AvatarItemTest {

    private AvatarItem avatarItem;
    private Avatar avatar;

    @BeforeEach
    void setUp() {
        avatarItem = new AvatarItem();
        avatar = new Avatar();
    }


    @Test
    public void testDefaultConstructor() {
        HorizontalLayout content = avatarItem.getContent();
        assertEquals(FlexComponent.Alignment.CENTER, content.getAlignItems());

        VerticalLayout column = (VerticalLayout) content.getComponentAt(0);
        assertFalse(column.isPadding());
        assertFalse(column.isSpacing());

        Span description = (Span) column.getComponentAt(1);
        assertEquals("var(--lumo-secondary-text-color)", description.getStyle().get("color"));
        assertEquals("var(--lumo-font-size-s)", description.getStyle().get("font-size"));

        assertEquals("var(--lumo-line-height-m)", content.getStyle().get("line-height"));
    }

    @Test
    public void testConstructorWithParameters() {
        String headingText = "Test Heading";
        String descriptionText = "Test Description";


        avatarItem = new AvatarItem(headingText, descriptionText, avatar);

        HorizontalLayout content = avatarItem.getContent();
        VerticalLayout column = (VerticalLayout) content.getComponentAt(1);

        Span heading = (Span) column.getComponentAt(0);
        Span description = (Span) column.getComponentAt(1);

        assertEquals(headingText, heading.getText());
        assertEquals(descriptionText, description.getText());
        assertEquals(avatar, content.getComponentAt(0));
    }

    @Test
    public void testSetHeading() {
        String headingText = "New Heading";
        avatarItem.setHeading(headingText);

        VerticalLayout column = (VerticalLayout) avatarItem.getContent().getComponentAt(0);
        Span heading = (Span) column.getComponentAt(0);

        assertEquals(headingText, heading.getText());
    }

    @Test
    public void testSetDescription() {
        String descriptionText = "New Description";
        avatarItem.setDescription(descriptionText);

        VerticalLayout column = (VerticalLayout) avatarItem.getContent().getComponentAt(0);
        Span description = (Span) column.getComponentAt(1);

        assertEquals(descriptionText, description.getText());
    }

    @Test
    public void testSetAvatar() {
        Avatar newAvatar = new Avatar();
        avatarItem.setAvatar(avatar);
        avatarItem.setAvatar(newAvatar);

        HorizontalLayout content = avatarItem.getContent();

        assertEquals(newAvatar, content.getComponentAt(0));
    }

}