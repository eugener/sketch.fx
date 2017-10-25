package org.sketchfx.element;

import javafx.scene.control.SkinBase;
import javafx.scene.shape.Rectangle;

class RectangleElementSkin extends SkinBase<RectangleElement> {

    private Rectangle rect = new Rectangle();

    RectangleElementSkin(RectangleElement control) {
        super(control);
        rect.getStyleClass().add(VisualElement.STYLE_CLASS);
        getChildren().add(rect);

    }

//    @Override
//    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
//
//        ellipse.setRadiusX( contentWidth/2);
//        ellipse.setRadiusY( contentHeight/2);
//        ellipse.setCenterX(contentX + ellipse.getRadiusX());
//        ellipse.setCenterY(contentY + ellipse.getRadiusY());
//
//    }
}
