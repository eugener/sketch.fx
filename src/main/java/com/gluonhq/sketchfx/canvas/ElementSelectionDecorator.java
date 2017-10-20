package com.gluonhq.sketchfx.canvas;

import com.gluonhq.sketchfx.element.VisualElement;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;
import java.util.stream.Collectors;

//TODO convert to control
class ElementSelectionDecorator extends Group {

    private final VisualElement element;
    private Rectangle rect = new Rectangle();

    ElementSelectionDecorator(VisualElement element) {
        this.element = element;

        rect.getStyleClass().add("shape-selection-rect");

        bindToElement();

        getChildren().addAll(rect);

        for( HandleType handleType: HandleType.values() ) {
            getChildren().add(new Handle(rect, handleType));
        }

        this.sceneProperty().addListener( (o,os,ns) -> { if (ns == null) unbind();  } );

    }

    private void bindToElement() {

        rect.layoutXProperty().bindBidirectional(element.layoutXProperty());
        rect.layoutYProperty().bindBidirectional(element.layoutYProperty());
        rect.widthProperty().bindBidirectional(element.prefWidthProperty());
        rect.heightProperty().bindBidirectional(element.prefHeightProperty());

    }

    private void unbind() {

        rect.layoutXProperty().unbind();
        rect.layoutYProperty().unbind();
        rect.widthProperty().unbind();
        rect.heightProperty().unbind();

        for( Node n: getChildren() ) {
            if (n instanceof  Handle) ((Handle)n).unbind();
        }

    }

}

class Handle extends Rectangle {

    private static double SIZE = 7;
    private static double HALF_SIZE = SIZE / 2;

    private HandleType handleType;

    Handle( Rectangle rect, HandleType handleType ) {

        this.handleType = handleType;

        getStyleClass().add("shape-selection-handle");
        setWidth(SIZE);
        setHeight(SIZE);
        configureHandle(rect);
    }

    public HandleType getHandleType() {
        return handleType;
    }

    private void configureHandle( Rectangle rect ) {
        configurePosition(rect);
        setCursor(handleType.getCursor());
    }

    private void configurePosition( Rectangle rect ) {

        switch (handleType.getHpos()) {
            case LEFT  : layoutXProperty().bind(rect.layoutXProperty().subtract(Handle.HALF_SIZE)); break;
            case CENTER: layoutXProperty().bind(rect.layoutXProperty().subtract(Handle.HALF_SIZE).add(rect.getWidth()/2)); break;
            case RIGHT : layoutXProperty().bind(rect.layoutXProperty().subtract(Handle.HALF_SIZE).add(rect.getWidth())); break;
        }

        switch (handleType.getVpos()) {
            case TOP   : layoutYProperty().bind(rect.layoutYProperty().subtract(Handle.HALF_SIZE)); break;
            case CENTER: layoutYProperty().bind(rect.layoutYProperty().subtract(Handle.HALF_SIZE).add(rect.getHeight()/2)); break;
            case BOTTOM: layoutYProperty().bind(rect.layoutYProperty().subtract(Handle.HALF_SIZE).add(rect.getHeight())); break;
        }


    }

    void unbind() {
        layoutXProperty().unbind();
        layoutYProperty().unbind();
    }

}


enum HandleType {

    LEFT_TOP     (HPos.LEFT,  VPos.TOP,    Cursor.NE_RESIZE),
    MIDDLE_TOP   (HPos.CENTER,VPos.TOP,    Cursor.V_RESIZE),
    RIGHT_TOP    (HPos.RIGHT, VPos.TOP,    Cursor.NW_RESIZE),
    LEFT_MIDDLE  (HPos.LEFT,  VPos.CENTER, Cursor.H_RESIZE),
    RIGHT_MIDDLE (HPos.RIGHT, VPos.CENTER, Cursor.H_RESIZE),
    LEFT_BOTTOM  (HPos.LEFT,  VPos.BOTTOM, Cursor.SE_RESIZE),
    MIDDLE_BOTTOM(HPos.CENTER,VPos.BOTTOM, Cursor.V_RESIZE),
    RIGHT_BOTTOM (HPos.RIGHT, VPos.BOTTOM, Cursor.SW_RESIZE);

    private final HPos hpos;
    private final VPos vpos;
    private final Cursor cursor;

    HandleType(HPos hpos, VPos vpos, Cursor cursor ) {
        this.hpos = hpos;
        this.vpos = vpos;
        this.cursor = cursor;
    }

    public HPos getHpos() {
        return hpos;
    }

    public VPos getVpos() {
        return vpos;
    }

    public Cursor getCursor() {
        return cursor;
    }
}

