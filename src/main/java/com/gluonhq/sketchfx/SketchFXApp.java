package com.gluonhq.sketchfx;

import com.gluonhq.sketchfx.element.Configuration;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Optional;

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

    public static Browser openBrowser( final Stage primaryStage) {
        Browser browser = new Browser();
        Scene scene = new Scene(browser);
        scene.getStylesheets().add("/sketchfx.css");

        final Stage stage = Optional.ofNullable(primaryStage).orElse(new Stage());
        stage.setTitle("Sketch.FX");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.setOnCloseRequest( e -> storeConfig(stage));
        loadConfig(stage);
        stage.show();
        return browser;
    }

    private static Configuration config = new Configuration("sketchfx");


    private static void loadConfig( Stage stage ) {

        if ( config.load()) {
            stage.setX( config.getDouble("stage.x") );
            stage.setY( config.getDouble("stage.y") );
            stage.setWidth( config.getDouble("stage.width") );
            stage.setHeight( config.getDouble("stage.height") );
        }

    }

    private static void storeConfig( Stage stage ) {

        config.setDouble("stage.x", stage.getX() );
        config.setDouble("stage.y", stage.getY() );
        config.setDouble("stage.width", stage.getWidth() );
        config.setDouble("stage.height", stage.getHeight() );
        config.store();

    }

}
