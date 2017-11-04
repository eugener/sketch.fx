package org.sketchfx.util;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.sources.Change;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;

import java.util.Objects;
import java.util.function.BiConsumer;

public class NodeDragSupport<T extends Node> {

    private Point2D sceneDragStart;

    private final Observable<Point2D> dragStartEvents;
    private final Observable<Point2D> dragEvents;
    private final Observable<Point2D> dragDeltaEvents;
    private final Observable<Point2D> dragEndEvents;

    private final Observable<Change<Boolean>> suspentionEvents;

    public static <T extends Node> void configure(T node, BiConsumer<T, Point2D> drag ) {
        NodeDragSupport<T> ns = new NodeDragSupport<T>(node);
        ns.getDragDeltaEvents().subscribe(deltas -> drag.accept( node, deltas) );
        ns.activate();
    }

    public NodeDragSupport(T node) {

        Objects.requireNonNull(node);

        dragStartEvents = JavaFxObservable
                        .eventsOf( node, MouseEvent.MOUSE_PRESSED)
                        .filter( this::isActive )
                        .map( this::toPoint )
                        .share();

        dragEvents = JavaFxObservable
                .eventsOf( node, MouseDragEvent.MOUSE_DRAGGED)
                .filter( this::isActive )
                .map(this::toPoint)
                .share();

        dragDeltaEvents = dragEvents.map(p -> p.subtract(sceneDragStart)).share();

        dragEndEvents = JavaFxObservable
                .eventsOf( node, MouseEvent.MOUSE_RELEASED)
                .filter( this::isActive )
                .map(this::toPoint)
                .share();

        suspentionEvents = JavaFxObservable.changesOf( suspendedProperty());

        // default event functionality
        dragStartEvents.subscribe( p -> sceneDragStart = p);
        dragDeltaEvents.subscribe( p -> sceneDragStart = sceneDragStart.add(p));
        dragEndEvents.subscribe(p -> sceneDragStart = p);

    }

    private boolean isActive( MouseEvent e ) {
        return !isSuspended();
    }

    private Point2D toPoint( MouseEvent e ) {
        return new Point2D( e.getSceneX(), e.getSceneY());
    }

    public Observable<Change<Boolean>> getSuspentionEvents() {
        return suspentionEvents;
    }

    public Observable<Point2D> getDragStartEvents() {
        return dragStartEvents;
    }

    public Observable<Point2D> getDragEvents() {
        return dragEvents;
    }

    public Observable<Point2D> getDragDeltaEvents() {
        return dragDeltaEvents;
    }

    public Observable<Point2D> getDragEndEvents() {
        return dragEndEvents;
    }

    // suspendedProperty
    private final BooleanProperty suspendedProperty = new SimpleBooleanProperty(this, "suspended", true );
    public final BooleanProperty suspendedProperty() {
       return suspendedProperty;
    }
    public final boolean isSuspended() {
       return suspendedProperty.get();
    }
    public final void setSuspended(boolean value) {
        suspendedProperty.set(value);
    }

    public void suspend() {
        setSuspended(true);
    }

    public void activate() {
        setSuspended(false);
    }


}
