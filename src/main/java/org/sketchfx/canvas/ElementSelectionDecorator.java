package org.sketchfx.canvas;

import org.sketchfx.element.VisualElement;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO convert to control
class ElementSelectionDecorator extends Group {

    private static final Logger logger = LoggerFactory.getLogger(ElementSelectionDecorator.class);

    private final VisualElement owner;
    private Rectangle rect = new Rectangle();

    ElementSelectionDecorator(VisualElement owner) {
        this.owner = owner;

        rect.getStyleClass().add("shape-selection-rect");

        bindToElement();

        getChildren().addAll(rect);

        for( HandleType handleType: HandleType.values() ) {
            getChildren().add(new Handle(rect, handleType));
        }

        this.sceneProperty().addListener( (o,os,ns) -> { if (ns == null) unbind();  } );

    }

    public VisualElement getOwner() {
        return owner;
    }

    private void bindToElement() {

        logger.debug("Bind to " + owner);

        rect.layoutXProperty().bindBidirectional(owner.layoutXProperty());
        rect.layoutYProperty().bindBidirectional(owner.layoutYProperty());
        rect.widthProperty().bindBidirectional(owner.prefWidthProperty());
        rect.heightProperty().bindBidirectional(owner.prefHeightProperty());

    }

    private void unbind() {

        logger.debug("Unbind from " + owner);

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
        layoutXProperty().bind( getBinding(handleType.getHpos(), rect));
        layoutYProperty().bind( getBinding(handleType.getVpos(), rect));
    }

    private DoubleBinding getBinding( HPos pos, Rectangle rect ) {

        DoubleBinding initialBinding = rect.layoutXProperty().subtract(Handle.HALF_SIZE);
        switch (pos) {
            case LEFT  : return initialBinding;
            case CENTER: return initialBinding.add(rect.getWidth()/2);
            case RIGHT : return initialBinding.add(rect.getWidth());
            default    : return null;
        }
    }

    private DoubleBinding getBinding( VPos pos, Rectangle rect ) {
        DoubleBinding initialBinding = rect.layoutYProperty().subtract(Handle.HALF_SIZE);
        switch (pos) {
            case TOP   : return initialBinding;
            case CENTER: return initialBinding.add(rect.getHeight()/2);
            case BOTTOM: return initialBinding.add(rect.getHeight());
            default    : return null;
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

