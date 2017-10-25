package org.sketchfx.element;

import javafx.scene.shape.Rectangle;

public final class ElementFactory {

    private ElementFactory() {
    }

    public static VisualElement getOval(Rectangle rect) {
        return new OvalShape(rect);
    }

    public static VisualElement getRectangle(Rectangle rect) {
        return new RectangleShape(rect);
    }

}
