package org.sketchfx.canvas;

import org.sketchfx.element.VisualElement;
import org.sketchfx.element.CircleElement;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class BrowserCanvas extends StackPane {

    private Pane elementLayer = new Pane();
    private Pane controlLayer = new Pane();

    private SelectionModel<VisualElement> selectionModel = new SelectionModel<>(

           element -> controlLayer.getChildren().removeIf( node ->
                   node instanceof  ElementSelectionDecorator &&
                           ((ElementSelectionDecorator)node).getOwner() == element ),

           element -> controlLayer.getChildren().setAll(new ElementSelectionDecorator(element)
    ));

    enum MouseMode {
        SELECT,
        INSERT
    }

    public BrowserCanvas() {

        getStylesheets().add(BrowserCanvas.class.getResource("/sketchfx.css").toExternalForm());
        getStyleClass().add("browser-canvas");

        getChildren().addAll(elementLayer, controlLayer);
        controlLayer.setMouseTransparent(true);

        elementLayer.setOnMouseClicked(e -> {

            if ( e.isControlDown()) {//MouseMode.INSERT == getMouseMode() ) {
                CircleElement element = new CircleElement(e.getX(), e.getY(), 100, 100);
                element.setOnMouseClicked( ex -> selectionModel.add(element, !ex.isShiftDown()));
                add(element);
            } else {
                selectionModel.clear();
            }

        });

    }

    public void add( VisualElement element ) {
        elementLayer.getChildren().add((Node)element);
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

