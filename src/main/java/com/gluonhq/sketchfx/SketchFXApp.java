package com.gluonhq.sketchfx;

import com.sun.javafx.css.StyleManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SketchFXApp extends Application {

    public static void main(String[] args) {
        Application.launch(SketchFXApp.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
//        Application.setUserAgentStylesheet(null);
//        StyleManager.getInstance().addUserAgentStylesheet("/sketchfx.css");
        openBrowser(stage);
    }

    public static Browser openBrowser(Stage stage) {
        Browser browser = new Browser();
        Scene scene = new Scene(browser);
        scene.getStylesheets().add("/sketchfx.css");

        if (stage == null) stage = new Stage();
        stage.setTitle("Sketch.FX");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
        return browser;
    }
}
