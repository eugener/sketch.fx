package org.sketchfx.element;

import javafx.scene.control.Skin;
import javafx.scene.shape.Rectangle;

public class RectangleShape extends VisualElement {

    public RectangleShape() {
    }

    public RectangleShape(Rectangle area) {
        super(area);
    }

    @Override
    protected Skin<RectangleShape> createDefaultSkin() {
        return new RectangleShapeSkin(this);
    }
}
