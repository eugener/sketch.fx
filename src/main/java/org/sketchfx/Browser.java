package org.sketchfx;

import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.shape.Rectangle;
import org.sketchfx.canvas.BrowserCanvas;
import javafx.scene.layout.BorderPane;
import org.sketchfx.element.ElementFactory;
import org.sketchfx.element.VisualElement;

import java.util.function.Function;


public class Browser extends BorderPane {

    private final BrowserCanvas canvas = new BrowserCanvas();
    private final ToolBar toolbar = new ToolBar();

    public Browser() {

        MenuButton btnInsertMenu = new MenuButton("Insert");
        btnInsertMenu.setFocusTraversable(false);

        btnInsertMenu.getItems().addAll(
             buildMenuItem("Oval...", ElementFactory::getOval),
             buildMenuItem("Rectangle...", ElementFactory::getRectangle)
        );
        toolbar.getItems().add(btnInsertMenu);

        setTop(toolbar);
        setCenter(canvas);
        setPrefSize(1000, 650);

    }

    private MenuItem buildMenuItem( String title, Function<Rectangle, ? extends VisualElement> createElement ) {
        MenuItem menuItem = new MenuItem(title);
        menuItem.setOnAction( e -> canvas.initAdd(createElement));
        return menuItem;
    }
}
