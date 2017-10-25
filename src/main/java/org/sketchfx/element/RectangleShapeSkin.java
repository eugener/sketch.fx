package org.sketchfx.element;

import javafx.scene.control.SkinBase;
import javafx.scene.shape.Rectangle;

class RectangleShapeSkin extends SkinBase<RectangleShape> {

    private Rectangle rect = new Rectangle();

    RectangleShapeSkin(RectangleShape control) {
        super(control);
        rect.getStyleClass().add(VisualElement.STYLE_CLASS);
        getChildren().add(rect);

    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        rect.setWidth(contentWidth);
        rect.setHeight(contentHeight);
    }
}
