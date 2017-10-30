package org.sketchfx.canvas;

import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import org.sketchfx.util.NodeDragSupport;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SelectionDecoratorSkin extends SkinBase<SelectionDecorator> {

    final Rectangle selectionArea = new Rectangle();
    private final List<Handle> handles = getHandles();

    SelectionDecoratorSkin(SelectionDecorator control) {

        super(control);

        selectionArea.getStyleClass().add("shape-selection-area");

        // add ability to drag it around, which moves the owner element too
        NodeDragSupport.configure(selectionArea, (node, deltas) -> {
                SelectionDecorator d = getSkinnable();
                d.relocate(d.getLayoutX() + deltas.getX(), d.getLayoutY() + deltas.getY());
            }
        );

        getChildren().add(selectionArea);
        getChildren().addAll(handles);

    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {

        selectionArea.setX(0);
        selectionArea.setY(0);
        selectionArea.setWidth( contentWidth );
        selectionArea.setHeight( contentHeight );

        handles.forEach(Handle::configurePosition);

    }

    private List<Handle> getHandles() {
        return Arrays
                .stream(HandleType.values())
                .map(ht -> new Handle(ht, this) )
                .collect(Collectors.toList());
    }


}

class Handle extends Rectangle {

    private static double SIZE = 7;
    private static double HALF_SIZE = SIZE / 2;

    private HandleType handleType;
    private Rectangle selectionArea;

    Handle( HandleType handleType, SelectionDecoratorSkin skin ) {

        this.handleType = handleType;
        this.selectionArea = skin.selectionArea;

        getStyleClass().add("shape-selection-handle");
        setWidth(SIZE);
        setHeight(SIZE);

        configurePosition();
        setCursor(handleType.getCursor());
        NodeDragSupport.configure( this, (node, deltas) ->
            node.handleType.resizeRelocate( skin.getSkinnable(), deltas)
        );

    }

    void configurePosition() {
        setLayoutX( getRelativeX() );
        setLayoutY( getRelativeY() );
    }

    private double getRelativeX() {

        double x = selectionArea.getLayoutX() - Handle.HALF_SIZE ;
        switch ( handleType.getHpos() ) {
            case LEFT  : return x;
            case CENTER: return x + selectionArea.getWidth()/2;
            case RIGHT : return x + selectionArea.getWidth();
            default    : return x;
        }
    }

    private double getRelativeY() {
        double y = selectionArea.getLayoutY() - Handle.HALF_SIZE ;
        switch (handleType.getVpos()) {
            case TOP   : return y;
            case CENTER: return y + selectionArea.getHeight()/2;
            case BOTTOM: return y + selectionArea.getHeight();
            default    : return y;
        }
    }

}

enum HandleType {

    LEFT_TOP     (HPos.LEFT,  VPos.TOP,    Cursor.NW_RESIZE),
    CENTER_TOP   (HPos.CENTER,VPos.TOP,    Cursor.V_RESIZE),
    RIGHT_TOP    (HPos.RIGHT, VPos.TOP,    Cursor.NE_RESIZE),
    LEFT_CENTER  (HPos.LEFT,  VPos.CENTER, Cursor.H_RESIZE),
    RIGHT_CENTER (HPos.RIGHT, VPos.CENTER, Cursor.H_RESIZE),
    LEFT_BOTTOM  (HPos.LEFT,  VPos.BOTTOM, Cursor.SW_RESIZE),
    CENTER_BOTTOM(HPos.CENTER,VPos.BOTTOM, Cursor.V_RESIZE),
    RIGHT_BOTTOM (HPos.RIGHT, VPos.BOTTOM, Cursor.SE_RESIZE);

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

    public void resizeRelocate(Region node, Point2D deltas ) {

        if (hpos == HPos.LEFT) {
            node.setLayoutX(node.getLayoutX() + deltas.getX());
            node.setPrefWidth(node.getPrefWidth() - deltas.getX());
        }

        if (hpos == HPos.RIGHT) {
            node.setPrefWidth(node.getPrefWidth() + deltas.getX());
        }

        if (vpos == VPos.TOP) {
            node.setLayoutY(node.getLayoutY() + deltas.getY());
            node.setPrefHeight(node.getPrefHeight() - deltas.getY());
        }

        if (vpos == VPos.BOTTOM) {
            node.setPrefHeight(node.getPrefHeight() + deltas.getY());
        }

    }

}




