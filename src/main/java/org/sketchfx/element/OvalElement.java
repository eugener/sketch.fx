package org.sketchfx.element;

import javafx.scene.control.Skin;
import javafx.scene.shape.Rectangle;

public class OvalElement extends VisualElement {

    public OvalElement() {
    }

    public OvalElement(Rectangle area) {
        super(area);
    }

    @Override
    protected Skin<OvalElement> createDefaultSkin() {
        return new OvalElementSkin(this);
    }
}
