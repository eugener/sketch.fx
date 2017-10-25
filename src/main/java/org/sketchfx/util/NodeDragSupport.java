package org.sketchfx.util;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;

import java.util.Objects;
import java.util.function.BiConsumer;

public class NodeDragSupport<T extends Node> extends AbstractDragSupport<T> {

    private final BiConsumer<T, Point2D> drag;

    private NodeDragSupport(T node, BiConsumer<T, Point2D> drag) {

        super(node);
        this.drag = Objects.requireNonNull(drag);

    }

    protected void drag( T node, Point2D deltas ) {
        drag.accept( node, deltas);
    }

    public static <T extends Node> void configure(T node, BiConsumer<T, Point2D> drag ) {
        new NodeDragSupport<T>(node, drag).start();
    }


}
