package org.sketchfx.canvas;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import org.sketchfx.element.VisualElement;
import org.sketchfx.util.SingleUseLasso;

import java.util.Optional;
import java.util.function.Function;

public class BrowserCanvas extends StackPane {

    private Pane elementLayer = new Pane();
    private Pane controlLayer = new Pane();

    private SelectionModel<VisualElement> selectionModel = new SelectionModel<>( this::elementRemoved, this::elementAdded );

    public BrowserCanvas() {

        getStylesheets().add(BrowserCanvas.class.getResource("/sketchfx.css").toExternalForm());
        getStyleClass().add("browser-canvas");

        getChildren().addAll(elementLayer, controlLayer);

        // setting mouse transparent does not work as all children will not respond to events
        controlLayer.setPickOnBounds(false);

        elementLayer.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
           selectionModel.clear();
        });

    }

    public void add( VisualElement element ) {
        elementLayer.getChildren().add((Node)element);
    }


    private SingleUseLasso creationLasso = new SingleUseLasso( elementLayer, controlLayer );

    public void initAdd( Function<Rectangle, ? extends VisualElement> element ) {

          creationLasso.setOnFinished(rect -> {
                VisualElement e = element.apply(rect);
                e.addEventFilter(MouseEvent.MOUSE_PRESSED, ex -> selectionModel.add(e, !ex.isShiftDown()));
                add(e);
          });
          creationLasso.start();

    }



    private void elementRemoved( VisualElement e ) {
        findSelectionDecorator(e).ifPresent( decorator -> {
            decorator.unbind();
            controlLayer.getChildren().remove(decorator);
        });
    }

    private void elementAdded( VisualElement e ) {
        if ( !findSelectionDecorator(e).isPresent()) {
            controlLayer.getChildren().setAll(new SelectionDecorator(e));
        }
    }

    private Optional<SelectionDecorator> findSelectionDecorator(VisualElement element ) {
        for ( Node node: controlLayer.getChildren()) {
            if ( node instanceof SelectionDecorator && ((SelectionDecorator)node).getOwner() == element) {
                return Optional.of((SelectionDecorator) node);
            }
        }
        return Optional.empty();
    }


}



