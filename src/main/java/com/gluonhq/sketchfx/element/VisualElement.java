package com.gluonhq.sketchfx.element;

import javafx.scene.control.Control;

public abstract class VisualElement extends Control {

    VisualElement() {
        getStylesheets().add(VisualElement.class.getResource("/sketchfx.css").toExternalForm());
    }

}
