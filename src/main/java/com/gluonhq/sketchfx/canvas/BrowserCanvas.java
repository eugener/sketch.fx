package com.gluonhq.sketchfx.canvas;

import com.gluonhq.sketchfx.element.VisualElement;
import com.gluonhq.sketchfx.element.CircleElement;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.SingleSelectionModel;
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

        getStylesheets().add(BrowserCanvas.class.getResource("/sketchfx.css").toExternalForm());
        getStyleClass().add("browser-canvas");

        getChildren().addAll(shapeLayer, controlLayer);
        controlLayer.setMouseTransparent(true);


        shapeLayer.setOnMouseClicked( e -> {

            if ( e.isControlDown()) {//MouseMode.INSERT == getMouseMode() ) {
                CircleElement element = new CircleElement(e.getX(), e.getY(), 100, 100);
                element.setOnMouseClicked( x ->
                    controlLayer.getChildren().setAll(new ElementSelectionDecorator(element))
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
