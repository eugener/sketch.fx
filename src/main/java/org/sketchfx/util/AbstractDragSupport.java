package org.sketchfx.util;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;

import java.util.Objects;

public abstract class AbstractDragSupport<T extends Node> {

    private Point2D dragStart;
    private final T node;

    private EventHandler<MouseEvent> mousePressed;
    private EventHandler<MouseEvent> mouseDragged;
    private EventHandler<MouseEvent> mouseReleased;

    private boolean suspended = true;

    AbstractDragSupport(T node) {

        this.node = Objects.requireNonNull(node);

        mousePressed = e -> {
            dragStart = new Point2D( e.getX(), e.getY());
            dragBegin(dragStart);
        };

        mouseDragged = e -> {
            Point2D currentLocation = new Point2D( e.getX(), e.getY());
            drag( node, currentLocation.subtract(dragStart));
            dragStart = currentLocation;
        };

        mouseReleased = e -> {
            dragStart = null;
            dragEnd();
        };

    }

    protected Point2D getDragStart() {
        return dragStart;
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
