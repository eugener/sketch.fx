package org.sketchfx.element;

import javafx.scene.control.Skin;
import javafx.scene.shape.Rectangle;

public class OvalShape extends VisualElement {

    public OvalShape() {
    }

    public OvalShape(Rectangle area) {
        super(area);
    }

    @Override
    protected Skin<OvalShape> createDefaultSkin() {
        return new OvalShapeSkin(this);
    }
}
