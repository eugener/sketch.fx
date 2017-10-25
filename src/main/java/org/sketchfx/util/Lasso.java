package org.sketchfx.util;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.Objects;
import java.util.function.Consumer;

public class Lasso extends AbstractDragSupport {

    private Rectangle rect;
    private Pane lassoPane;
    private Consumer<Rectangle> lassoDone;

    public Lasso(Pane clickPane, Pane lassoPane, Consumer<Rectangle> lassoDone ) {

        super(clickPane);
        this.lassoPane = Objects.requireNonNull(lassoPane);
        this.lassoDone = Objects.requireNonNull(lassoDone);

    }

    @Override
    protected void dragBegin(Point2D dragStart) {
        rect = new Rectangle( dragStart.getX(), dragStart.getY(), 0, 0);
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
        lassoDone.accept(rect);
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
