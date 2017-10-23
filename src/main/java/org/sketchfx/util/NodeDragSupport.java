package org.sketchfx.util;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;

import java.util.Objects;
import java.util.function.BiConsumer;

public class NodeDragSupport<T extends Node> {

    private Point2D dragStart;

    private NodeDragSupport(T node, BiConsumer<T, Point2D> drag ) {

        Objects.requireNonNull(node);
        Objects.requireNonNull(drag);

        node.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent e) -> {
            dragStart = new Point2D( e.getSceneX(), e.getSceneY());
        });

        node.addEventFilter(MouseDragEvent.MOUSE_DRAGGED, (MouseEvent e) ->  {
            Point2D currentLocation = new Point2D( e.getSceneX(), e.getSceneY());
            drag.accept( node, currentLocation.subtract(dragStart));
            dragStart = currentLocation;
        });

        node.addEventFilter(MouseDragEvent.MOUSE_DRAG_RELEASED, (MouseDragEvent e) -> dragStart = null);

    }

    public static <T extends Node> void configure(T node, BiConsumer<T, Point2D> drag ) {
        new NodeDragSupport<T>(node, drag);
    }


}
