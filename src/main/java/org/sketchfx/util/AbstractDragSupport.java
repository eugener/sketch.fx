package org.sketchfx.util;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;

import java.util.Objects;

public abstract class AbstractDragSupport<T extends Node> {

    private Point2D sceneDragStart;
    private final T node;

    private EventHandler<MouseEvent> mousePressed;
    private EventHandler<MouseEvent> mouseDragged;
    private EventHandler<MouseEvent> mouseReleased;

    private boolean suspended = true;

    AbstractDragSupport(T node) {

        this.node = Objects.requireNonNull(node);

        mousePressed = e -> {
            sceneDragStart = new Point2D( e.getSceneX(), e.getSceneY());
            dragBegin(sceneDragStart);
        };

        mouseDragged = e -> {
            Point2D currentLocation = new Point2D( e.getSceneX(), e.getSceneY());
            drag( node, currentLocation.subtract(sceneDragStart));
            sceneDragStart = currentLocation;
        };

        mouseReleased = e -> {
            sceneDragStart = null;
            dragEnd();
        };

    }

    protected Point2D getSceneDragStart() {
        return sceneDragStart;
    }

    protected void dragBegin(Point2D dragStart) {}
    protected void dragEnd() {}
    protected abstract void drag(T node, Point2D currentLocation);


    public void suspend() {
        if ( !suspended ) {
            node.removeEventFilter(MouseEvent.MOUSE_PRESSED, mousePressed);
            node.removeEventFilter(MouseDragEvent.MOUSE_DRAGGED, mouseDragged);
            node.removeEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleased);
            suspended = true;
        }
    }

    public void start() {
        if ( suspended ) {
            node.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressed);
            node.addEventFilter(MouseDragEvent.MOUSE_DRAGGED, mouseDragged);
            node.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleased);
            suspended = false;
        }
    }


}
