package org.sketchfx.element;

import javafx.geometry.Point2D;
import javafx.scene.control.Control;

import java.util.UUID;

public abstract class VisualElement extends Control {

    public static String STYLE_CLASS = "shape-element";

    private Point2D dragStart;
    private String id = UUID.randomUUID().toString();

    VisualElement() {

        getStylesheets().add(VisualElement.class.getResource("/sketchfx.css").toExternalForm());
        setupDragging();

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

    private void setupDragging() {
        this.setOnMousePressed( e -> dragStart = new Point2D( e.getSceneX(), e.getSceneY()));
        this.setOnMouseDragged( e ->  {

            Point2D currentLocation = new Point2D( e.getSceneX(), e.getSceneY());
            Point2D delta = currentLocation.subtract(dragStart);
            relocate(getLayoutX() + delta.getX(), getLayoutY() + delta.getY());
            dragStart = currentLocation;

        });
        this.setOnMouseDragReleased( e -> dragStart = null);
    }

}
