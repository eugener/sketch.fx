package org.sketchfx.canvas;

import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import org.sketchfx.element.VisualElement;

import java.util.Optional;
import java.util.function.Function;

public class BrowserCanvas extends StackPane {

    Pane elementLayer = new Pane();
    CanvasControlLayer controlLayer = new CanvasControlLayer();

    private Lasso creationLasso = buildCreationLasso();
    private Lasso selectionLasso = buildSelectionLasso();


    private SelectionModel<VisualElement> selectionModel = new SelectionModel<>();

    public BrowserCanvas() {

        getStylesheets().add(BrowserCanvas.class.getResource("/sketchfx.css").toExternalForm());
        getStyleClass().add("browser-canvas");

        getChildren().addAll(elementLayer, controlLayer);

        selectionModel.getAdditions().subscribe(controlLayer::addSelectorBy);
        selectionModel.getRemovals().subscribe(controlLayer::removeSelectorBy);

        JavaFxObservable.eventsOf(elementLayer, MouseEvent.MOUSE_PRESSED).subscribe( e -> selectionModel.clear() );

    }

    public void add( VisualElement element ) {
        elementLayer.getChildren().add((Node)element);
    }

    private Lasso buildCreationLasso() {
        Lasso lasso = new Lasso( this, true );
        lasso.getSuspendedEvents().subscribe( suspended ->
            setCursor( suspended.getNewVal()? Cursor.DEFAULT: Cursor.CROSSHAIR)
        );
        lasso.getSuspendedEvents().subscribe( c -> selectionLasso.setSuspended(!c.getNewVal()));
        return lasso;
    }

    private Lasso buildSelectionLasso() {
        Lasso lasso = new Lasso( this, false );
        lasso.getFinsihedEvents().subscribe( rect -> {
            elementLayer.getChildren().stream().filter( n ->
                    n instanceof VisualElement &&
                            n.intersects( rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight())
            ).forEach( n-> selectionModel.add((VisualElement)n) );
        });
        lasso.activate();
        return lasso;
    }


    public void initAdd( Function<Rectangle, ? extends VisualElement> element ) {

        creationLasso.getFinsihedEvents()
                     .firstElement()
                     .subscribe( rect ->  {
                            VisualElement e = element.apply(rect);
                            e.addEventFilter(MouseEvent.MOUSE_PRESSED, ex -> selectionModel.add(e, !ex.isShiftDown()));
                            add(e);
                      });


        creationLasso.activate();

    }

}

class CanvasControlLayer extends Pane {

    CanvasControlLayer() {
        // setting mouse transparent does not work as all the children will not respond to events
        this.setPickOnBounds(false);
    }


    void removeSelectorBy( VisualElement e ) {
        findSelectionDecorator(e).ifPresent( decorator -> {
            decorator.unbind();
            getChildren().remove(decorator);
        });
    }

    void addSelectorBy( VisualElement e ) {
        if ( !findSelectionDecorator(e).isPresent()) {
            getChildren().add(new SelectionDecorator(e));
        }
    }

    private Optional<SelectionDecorator> findSelectionDecorator(VisualElement element) {
        for ( Node node: getChildren()) {
            if ( node instanceof SelectionDecorator && ((SelectionDecorator)node).getOwner() == element) {
                return Optional.of((SelectionDecorator) node);
            }
        }
        return Optional.empty();
    }

}



