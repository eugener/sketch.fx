package com.gluonhq.sketchfx.shape;

import javafx.scene.shape.Circle;

public class ShapeCircle extends Circle implements VisualElement {

    public ShapeCircle() {
        setStyle("-fx-fill: lightgrey; -fx-stroke: black;");
//        getStyleClass().add(".shape-circle");
    }
}
