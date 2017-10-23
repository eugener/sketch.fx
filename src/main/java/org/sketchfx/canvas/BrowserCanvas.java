package org.sketchfx.canvas;

import javafx.scene.input.MouseEvent;
import org.sketchfx.element.VisualElement;
import org.sketchfx.element.CircleElement;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.Optional;

public class BrowserCanvas extends StackPane {

    private Pane elementLayer = new Pane();
    private Pane controlLayer = new Pane();

    private SelectionModel<VisualElement> selectionModel = new SelectionModel<>( this::elementRemoved, this::elementAdded );

    enum MouseMode {
        SELECT,
        INSERT
    }

    public BrowserCanvas() {

        getStylesheets().add(BrowserCanvas.class.getResource("/sketchfx.css").toExternalForm());
        getStyleClass().add("browser-canvas");

        getChildren().addAll(elementLayer, controlLayer);

        // setting mouse transparent does not work as all children will not respond to events
        controlLayer.setPickOnBounds(false);

        elementLayer.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent e) -> {

            if ( e.isControlDown()) {//MouseMode.INSERT == getMouseMode() ) {
                CircleElement element = new CircleElement(e.getX(), e.getY(), 100, 100);
                element.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent ex) -> {
                    selectionModel.add(element, !ex.isShiftDown());
                });
                add(element);
            } else {
                selectionModel.clear();
            }

        });

    }

    public void add( VisualElement element ) {
        elementLayer.getChildren().add((Node)element);
    }

    private void elementRemoved( VisualElement e ) {
        findSelectionDecorator(e).ifPresent( decorator -> {
            decorator.unbind();
            controlLayer.getChildren().remove(decorator);
        });
    }

    private void elementAdded( VisualElement e ) {
        if ( !findSelectionDecorator(e).isPresent()) {
            controlLayer.getChildren().setAll(new SelectionDecorator(e));
        }
    }

    private Optional<SelectionDecorator> findSelectionDecorator(VisualElement element ) {
        for ( Node node: controlLayer.getChildren()) {
            if ( node instanceof SelectionDecorator && ((SelectionDecorator)node).getOwner() == element) {
                return Optional.of((SelectionDecorator) node);
            }
        }
        return Optional.empty();
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

