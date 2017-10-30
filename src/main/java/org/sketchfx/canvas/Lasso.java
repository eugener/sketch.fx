package org.sketchfx.canvas;

import io.reactivex.Observable;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import org.sketchfx.util.NodeDragSupport;

public class Lasso extends NodeDragSupport<Node> {

    private static int HINT_OFFSET = 5;

    private Point2D start;
    private Rectangle rect;
    private Label sizeHint;

    private final Pane lassoPane;
    private final Observable<Rectangle> finsihedEvents;

    public Lasso(BrowserCanvas canvas, boolean singleUse) {

        super(canvas);
        lassoPane = canvas.controlLayer;

        rect = new Rectangle( 0, 0, 0, 0);
        rect.getStyleClass().add("canvas-selection");

        sizeHint = new Label();
        sizeHint.getStyleClass().add("canvas-hint");

        getDragStartEvents().map(lassoPane::sceneToLocal).subscribe( p -> {
            start = p;
            rect.setX(p.getX());
            rect.setY(p.getY());
            rect.setWidth(0);
            rect.setHeight(0);

            lassoPane.getChildren().addAll(rect, sizeHint);
        });


        getDragEvents().map(lassoPane::sceneToLocal).subscribe( p -> {

            sizeHint.setLayoutX( p.getX() + HINT_OFFSET );
            sizeHint.setLayoutY( p.getY() + HINT_OFFSET );
            sizeHint.setText( rect.getWidth() + " x " + rect.getHeight() );

            rect.setX( Math.min( start.getX(), p.getX() ));
            rect.setY( Math.min( start.getY(), p.getY() ));

            Point2D deltas = p.subtract(start);

            rect.setWidth( Math.abs(deltas.getX()));
            rect.setHeight( Math.abs(deltas.getY()));

        });


        finsihedEvents = getDragEndEvents().map( e -> {
            lassoPane.getChildren().removeAll(rect, sizeHint);
            return rect;
        }).share();

        if ( singleUse ) {
            getFinsihedEvents().subscribe(r -> suspend());
        } else {
            getFinsihedEvents().subscribe();
        }

    }

    public Observable<Rectangle> getFinsihedEvents() {
        return finsihedEvents;
    }

}
