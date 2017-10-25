package org.sketchfx.util;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.function.Consumer;

public class SingleUseLasso extends Lasso {
    public SingleUseLasso(Pane clickPane, Pane lassoPane, Consumer<Rectangle> lassoDone) {
        super(clickPane, lassoPane, lassoDone);
    }

    @Override
    protected void dragEnd() {
        super.dragEnd();
        suspend();
    }

}
