package org.sketchfx.canvas;

import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.SkinBase;
import javafx.scene.shape.Rectangle;
import org.sketchfx.util.NodeDragSupport;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SelectionDecoratorSkin extends SkinBase<SelectionDecorator> {

    private final Rectangle selectionArea = new Rectangle();
    private final List<Handle> handles = HandleType.handles(selectionArea, getSkinnable());

    SelectionDecoratorSkin(SelectionDecorator control) {

        super(control);

        selectionArea.getStyleClass().add("shape-selection-area");
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

        for( Handle handle: handles ) {
            handle.configurePosition();
        }

    }

}

class Handle extends Rectangle {

    static double SIZE = 7;
    static double HALF_SIZE = SIZE / 2;

    private HandleType handleType;
    private Rectangle selectionArea;

    Handle( Rectangle selectionArea, HandleType handleType, SelectionDecorator decorator ) {

        this.selectionArea = selectionArea;
        this.handleType = handleType;

        getStyleClass().add("shape-selection-handle");
        setWidth(SIZE);
        setHeight(SIZE);

        configurePosition();
        setCursor(handleType.getCursor());
        NodeDragSupport.configure( this, (node, deltas) ->
            this.getHandleType().resizeRelocate( decorator, deltas)
        );

    }

    public HandleType getHandleType() {
        return handleType;
    }

    void configurePosition() {
        setLayoutX( handleType.getLayoutX( selectionArea ) );
        setLayoutY( handleType.getLayoutY( selectionArea ) );
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

    public static List<Handle> handles(Rectangle selectionArea, SelectionDecorator decorator ) {
        return Arrays.stream(HandleType.values())
                .map(ht -> new Handle(selectionArea, ht, decorator) )
                .collect(Collectors.toList());
    }

    public void resizeRelocate(SelectionDecorator node, Point2D deltas ) {

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

    double getLayoutX(Rectangle selectionArea ) {

        double x = selectionArea.getLayoutX() - Handle.HALF_SIZE ;
        switch ( hpos ) {
            case LEFT  : return x;
            case CENTER: return x + selectionArea.getWidth()/2;
            case RIGHT : return x + selectionArea.getWidth();
            default    : return x;
        }
    }

    double getLayoutY(Rectangle selectionArea ) {
        double y = selectionArea.getLayoutY() - Handle.HALF_SIZE ;
        switch (vpos) {
            case TOP   : return y;
            case CENTER: return y + selectionArea.getHeight()/2;
            case BOTTOM: return y + selectionArea.getHeight();
            default    : return y;
        }
    }

}




