package org.sketchfx;

import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import org.sketchfx.canvas.BrowserCanvas;
import javafx.scene.layout.BorderPane;
import org.sketchfx.element.ElementFactory;

public class Browser extends BorderPane {

    private final BrowserCanvas canvas = new BrowserCanvas();
    private final ToolBar toolbar = new ToolBar();

    public Browser() {

        MenuButton btnInsertMenu = new MenuButton("Insert");
        btnInsertMenu.setFocusTraversable(false);
        MenuItem mnOval = new MenuItem("Oval");
        mnOval.setOnAction( e -> {
//            canvas.setMode(BrowserCanvas.Mode.INSERT);
            canvas.initAdd(ElementFactory::getOval);
        });
        MenuItem mnRect = new MenuItem("Rectangle");
        mnOval.setOnAction( e -> {
//            canvas.setMode(BrowserCanvas.Mode.INSERT);
            canvas.initAdd(ElementFactory::getRectangle);
        });

        btnInsertMenu.getItems().addAll(mnOval, mnRect);
        toolbar.getItems().add(btnInsertMenu);

        setTop(toolbar);
        setCenter(canvas);
        setPrefSize(1000, 650);
    }
}
