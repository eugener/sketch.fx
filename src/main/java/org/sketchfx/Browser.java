package org.sketchfx;

import org.sketchfx.canvas.BrowserCanvas;
import javafx.scene.layout.BorderPane;

public class Browser extends BorderPane{

    private final BrowserCanvas canvas = new BrowserCanvas();

    public Browser() {
        setCenter(canvas);
        setPrefSize(1000, 650);
    }
}
