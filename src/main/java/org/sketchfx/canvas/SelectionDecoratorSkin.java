package org.sketchfx.canvas;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import org.sketchfx.element.VisualElement;
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

    private static double SIZE = 7;
    private static double HALF_SIZE = SIZE / 2;

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
            this.getHandleType().resizeRelocate( decorator.getOwner(), deltas)
        );

    }

    public HandleType getHandleType() {
        return handleType;
    }

    void configurePosition() {
        setLayoutX( computeX() );
        setLayoutY( computeY() );
    }

    private double computeX() {

        double x = selectionArea.getLayoutX() - Handle.HALF_SIZE ;
        switch ( handleType.getHpos()) {
            case LEFT  : return x;
            case CENTER: return x + selectionArea.getWidth()/2;
            case RIGHT : return x + selectionArea.getWidth();
            default    : return x;
        }
    }

    private double computeY() {
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
    MIDDLE_TOP   (HPos.CENTER,VPos.TOP,    Cursor.V_RESIZE),
    RIGHT_TOP    (HPos.RIGHT, VPos.TOP,    Cursor.NE_RESIZE),
    LEFT_MIDDLE  (HPos.LEFT,  VPos.CENTER, Cursor.H_RESIZE),
    RIGHT_MIDDLE (HPos.RIGHT, VPos.CENTER, Cursor.H_RESIZE),
    LEFT_BOTTOM  (HPos.LEFT,  VPos.BOTTOM, Cursor.SW_RESIZE),
    MIDDLE_BOTTOM(HPos.CENTER,VPos.BOTTOM, Cursor.V_RESIZE),
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

    public void resizeRelocate(VisualElement node, Point2D deltas ) {

        switch (this) {

            case LEFT_TOP  :
                node.setLayoutX(node.getLayoutX() + deltas.getX());
                node.setLayoutY(node.getLayoutY() + deltas.getY());
                node.setPrefWidth(node.getPrefWidth() - deltas.getX());
                node.setPrefHeight(node.getPrefHeight() - deltas.getY());
                break;
            case MIDDLE_TOP:
                node.setLayoutY(node.getLayoutY() + deltas.getY());
                node.setPrefHeight(node.getPrefHeight() - deltas.getY());
                break;

//            case RIGHT_TOP : selectionArea.resize(selectionArea.getWidth() + deltas.getX(),selectionArea.getHeight() + deltas.getY());

        }


    }

}




