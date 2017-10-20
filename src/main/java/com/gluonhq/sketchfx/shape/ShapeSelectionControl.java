package com.gluonhq.sketchfx.shape;

import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

public class ShapeSelectionControl extends Group {

    private final Node node;

    public ShapeSelectionControl( Node node ) {
        this.node = node;
        Rectangle rect = new Rectangle();
        rect.setStyle("-fx-fill: transparent; -fx-stroke: black; -fx-stroke-width: .5; -fx-opacity: .3");

        Bounds bounds = node.getBoundsInParent();
        setLayoutX(bounds.getMinX());
        setLayoutY(bounds.getMinY());
        rect.setWidth(bounds.getWidth());
        rect.setHeight(bounds.getHeight());


        getChildren().addAll(rect);
        for( HandleType handleType: HandleType.values() ) {
            getChildren().add(new Handle(rect, handleType));
        }

    }

    static class Handle extends Rectangle {

        static double SIZE = 7;
        static  double HALF_SIZE = SIZE / 2;

        private HandleType handleType;

        Handle( Rectangle rect, HandleType handleType ) {
            this.handleType = handleType;
            setStyle("-fx-fill: white; -fx-stroke: darkgrey; -fx-stroke-width: .7; -fx-effect: dropshadow(three-pass-box, grey, 2, 0, 0, 0);");
            setWidth(SIZE);
            setHeight(SIZE);
            handleType.configureHandle(this, rect);
        }

        public HandleType getHandleType() {
            return handleType;
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

        public void configureHandle( Handle handle, Rectangle rect ) {
            configurePosition(handle,rect);
            handle.setCursor(cursor);
        }

        private void configurePosition( Handle handle, Rectangle rect ) {

            switch (hpos) {
                case LEFT  : handle.xProperty().bind(rect.xProperty().subtract(Handle.HALF_SIZE)); break;
                case CENTER: handle.xProperty().bind(rect.xProperty().subtract(Handle.HALF_SIZE).add(rect.getWidth()/2)); break;
                case RIGHT : handle.xProperty().bind(rect.xProperty().subtract(Handle.HALF_SIZE).add(rect.getWidth())); break;
            }

            switch (vpos) {
                case TOP   : handle.yProperty().bind(rect.yProperty().subtract(Handle.HALF_SIZE)); break;
                case CENTER: handle.yProperty().bind(rect.yProperty().subtract(Handle.HALF_SIZE).add(rect.getHeight()/2)); break;
                case BOTTOM: handle.yProperty().bind(rect.yProperty().subtract(Handle.HALF_SIZE).add(rect.getHeight())); break;
            }


        }

    }

}
