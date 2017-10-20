package com.gluonhq.sketchfx.element;

import javafx.scene.control.Skin;

public class CircleElement extends VisualElement {

    @Override
    protected Skin<CircleElement> createDefaultSkin() {
        return new CircleElementSkin(this);
    }
}
