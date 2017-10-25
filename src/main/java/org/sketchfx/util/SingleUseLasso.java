package org.sketchfx.util;

import javafx.scene.layout.Pane;

public class SingleUseLasso extends Lasso {

    public SingleUseLasso(Pane clickPane, Pane lassoPane) {
        super(clickPane, lassoPane);
    }


    @Override
    protected void dragEnd() {
        super.dragEnd();
        suspend();
    }

}
