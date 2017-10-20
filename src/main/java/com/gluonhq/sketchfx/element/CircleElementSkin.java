package com.gluonhq.sketchfx.element;

import javafx.scene.control.SkinBase;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;

class CircleElementSkin extends SkinBase<CircleElement> {


    private Ellipse ellipse = new Ellipse();

    CircleElementSkin(CircleElement control) {
        super(control);

        //ellipse.setStyle("-fx-fill: lightgrey; -fx-stroke: black;");
        ellipse.getStyleClass().add("shape-circle");
        getChildren().add(ellipse);

    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {

        ellipse.setRadiusX( contentWidth/2);
        ellipse.setRadiusY( contentHeight/2);
        ellipse.setCenterX(contentX + ellipse.getRadiusX());
        ellipse.setCenterY(contentY + ellipse.getRadiusY());

    }
}