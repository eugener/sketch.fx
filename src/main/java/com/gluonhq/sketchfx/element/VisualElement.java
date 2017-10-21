package com.gluonhq.sketchfx.element;

import javafx.geometry.Point2D;
import javafx.scene.control.Control;

public abstract class VisualElement extends Control {

    public static String STYLE_CLASS = "shape-element";

    private Point2D dragStart;

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
