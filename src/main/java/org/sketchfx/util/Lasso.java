package org.sketchfx.util;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class Lasso extends AbstractDragSupport {

    private Rectangle rect;
    private Pane lassoPane;

    public Lasso(Pane clickPane, Pane lassoPane ) {
        super(clickPane);
        this.lassoPane = Objects.requireNonNull(lassoPane);
    }

    // onFinishedProperty
    private final ObjectProperty<Consumer<Rectangle>> onFinishedProperty = new SimpleObjectProperty<>(this, "finished");
    public final ObjectProperty<Consumer<Rectangle>> onFinishedProperty() {
        return onFinishedProperty;
    }
    public final Consumer<Rectangle> getOnFinished() {
        return onFinishedProperty.get();
    }
    public final void setOnFinished(Consumer<Rectangle> value) {
        onFinishedProperty.set(value);
    }

    @Override
    protected void dragBegin(Point2D dragStart) {
        Point2D start = lassoPane.sceneToLocal(dragStart);
        rect = new Rectangle( start.getX(), start.getY(), 0, 0);
        rect.setStyle("-fx-fill: transparent; -fx-stroke: deepskyblue;");
        lassoPane.getChildren().add(rect);
    }

    @Override
    protected void drag(Node node, Point2D deltas ) {

        rect.setWidth(rect.getWidth() + deltas.getX() );
        rect.setHeight(rect.getHeight() + deltas.getY() );
    }

    @Override
    protected void dragEnd() {
        lassoPane.getChildren().remove(rect);
        if ( getOnFinished() != null ) getOnFinished().accept(rect);
        rect = null;
    }

    public void suspend() {
        super.suspend();
        lassoPane.setCursor(Cursor.DEFAULT);
    }

    public void start() {
        super.start();
        lassoPane.setCursor(Cursor.CROSSHAIR);
    }
}
