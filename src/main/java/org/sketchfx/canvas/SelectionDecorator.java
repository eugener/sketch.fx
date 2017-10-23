package org.sketchfx.canvas;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.sketchfx.element.VisualElement;

import java.util.Objects;

public class SelectionDecorator extends Control {

    private final VisualElement owner;

    SelectionDecorator( VisualElement owner ) {
        this.owner = Objects.requireNonNull(owner);
        bindToOwner();
    }

    private void bindToOwner() {

        // first make sure selection has the same location and size
        setLayoutX(owner.getLayoutX());
        setLayoutY(owner.getLayoutY());
        setPrefWidth(owner.getPrefWidth());
        setPrefHeight(owner.getPrefHeight());

        // then bind it the owner  element
        owner.layoutXProperty().bindBidirectional(layoutXProperty());
        owner.layoutYProperty().bindBidirectional(layoutYProperty());
        owner.prefWidthProperty().bindBidirectional(prefWidthProperty());
        owner.prefHeightProperty().bindBidirectional(prefHeightProperty());
    }


    public void unbind() {
        owner.layoutXProperty().unbindBidirectional(layoutXProperty());
        owner.layoutYProperty().unbindBidirectional(layoutYProperty());
        owner.prefWidthProperty().unbindBidirectional(prefWidthProperty());
        owner.prefHeightProperty().unbindBidirectional(prefHeightProperty());
    }

    public VisualElement getOwner() {
        return owner;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new SelectionDecoratorSkin(this);
    }
}
