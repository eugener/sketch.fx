package com.gluonhq.sketchfx;

import com.gluonhq.sketchfx.shape.ShapeCircle;
import com.gluonhq.sketchfx.shape.ShapeSelectionControl;
import com.gluonhq.sketchfx.shape.VisualElement;
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

    BrowserCanvas() {
        getChildren().addAll(shapeLayer, controlLayer);
        controlLayer.setMouseTransparent(true);
        setStyle("-fx-background-color: white");

        shapeLayer.setOnMouseClicked( e -> {

            if ( e.isControlDown()) {//MouseMode.INSERT == getMouseMode() ) {
                ShapeCircle element = new ShapeCircle();
                element.setCenterX(e.getX());
                element.setCenterY(e.getY());
                element.setRadius(100);
                element.setOnMouseClicked( x ->
                    controlLayer.getChildren().setAll(new ShapeSelectionControl(element))
                );
                add(element);
            } else {



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
