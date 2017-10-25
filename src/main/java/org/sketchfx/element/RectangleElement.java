package org.sketchfx.element;

import javafx.scene.control.Skin;
import javafx.scene.shape.Rectangle;

public class RectangleElement extends VisualElement {

    public RectangleElement() {
    }

    public RectangleElement(Rectangle area) {
        super(area);
    }

    @Override
    protected Skin<RectangleElement> createDefaultSkin() {
        return new RectangleElementSkin(this);
    }
}
