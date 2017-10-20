package com.gluonhq.sketchfx.canvas;

import com.gluonhq.sketchfx.element.VisualElement;
import com.gluonhq.sketchfx.element.CircleElement;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class BrowserCanvas extends StackPane {

    private Pane shapeLayer   = new Pane();
    private Pane controlLayer = new Pane();

    enum MouseMode {
        SELECT,
        INSERT
    }

    public BrowserCanvas() {
        getChildren().addAll(shapeLayer, controlLayer);
        controlLayer.setMouseTransparent(true);
        setStyle("-fx-background-color: white");

        shapeLayer.setOnMouseClicked( e -> {

            if ( e.isControlDown()) {//MouseMode.INSERT == getMouseMode() ) {
//                ShapeCircle element = new ShapeCircle();
//                element.setCenterX(e.getX());
//                element.setCenterY(e.getY());
//                element.setRadius(100);
//                element.setOnMouseClicked( x ->
//                    controlLayer.getChildren().setAll(new ElementSelectionControl(element))
//                );
//                add(element);
//            } else if ( e.isAltDown() ) {

                CircleElement element = new CircleElement();
                element.setLayoutX(e.getX());
                element.setLayoutY(e.getY());
                element.setPrefWidth(100);
                element.setPrefHeight(100);
                element.setOnMouseClicked( x ->
                    controlLayer.getChildren().setAll(new ElementSelectionControl(element))
                );

                add(element);
            }


        });

    }

    public void add( VisualElement element ) {
        shapeLayer.getChildren().add((Node)element);
    }

    // mouseModeProperty
    private final ObjectProperty<MouseMode> mouseModeProperty =
            new SimpleObjectProperty<>(this, "mouseMode", MouseMode.SELECT);

    public final ObjectProperty<MouseMode> mouseModeProperty() {
       return mouseModeProperty;
    }
    public final MouseMode getMouseMode() {
       return mouseModeProperty.get();
    }
    public final void setMouseMode(MouseMode value) {
        mouseModeProperty.set(value);
    }





}
