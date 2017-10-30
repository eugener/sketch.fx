package org.sketchfx.canvas;

import io.reactivex.rxjavafx.observables.JavaFxObservable;
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


    private Lasso creationLasso = new Lasso( this, true );

    public void initAdd( Function<Rectangle, ? extends VisualElement> element ) {

        creationLasso.getFinsihedEvents()
                     .firstElement()
                     .subscribe( rect ->  {
                            VisualElement e = element.apply(rect);
                            e.addEventFilter(MouseEvent.MOUSE_PRESSED, ex -> selectionModel.add(e, !ex.isShiftDown()));
                            add(e);
                      });

        creationLasso.start();

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



