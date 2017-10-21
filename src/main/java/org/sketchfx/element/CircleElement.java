package org.sketchfx.element;

import javafx.scene.control.Skin;

public class CircleElement extends VisualElement {

    public CircleElement(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    protected Skin<CircleElement> createDefaultSkin() {
        return new CircleElementSkin(this);
    }
}
