package org.sketchfx.element;

import javafx.scene.control.Control;
import org.sketchfx.util.NodeDragSupport;

import java.util.UUID;

public abstract class VisualElement extends Control {

    public static String STYLE_CLASS = "shape-element";

    private String id = UUID.randomUUID().toString();

    VisualElement() {
        NodeDragSupport.configure( this, (element, deltas) ->
            relocate(getLayoutX() + deltas.getX(), getLayoutY() + deltas.getY())
        );
    }

    VisualElement( double x, double y, double width, double height ) {
        this();
        setLayoutX(x);
        setLayoutY(y);
        setPrefWidth(width);
        setPrefHeight(height);


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VisualElement)) return false;

        VisualElement that = (VisualElement) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
