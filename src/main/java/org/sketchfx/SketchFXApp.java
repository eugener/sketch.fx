package org.sketchfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Optional;


public class SketchFXApp extends Application {

    public static void main(String[] args) {
        Application.launch(SketchFXApp.class, args);
    }

    private Browser browser = new Browser();

    private Configuration config = new Configuration("sketchfx", false);


    @Override
    public void start(Stage stage) throws Exception {
//        Application.setUserAgentStylesheet(null);
//        StyleManager.getInstance().addUserAgentStylesheet("/sketchfx.css");


        stage.setOnCloseRequest( e -> storeConfig(stage));
        loadConfig(stage);
        openBrowser(stage);
    }

    private void openBrowser(final Stage primaryStage) {
        Scene scene = new Scene(browser);
        scene.getStylesheets().add("/sketchfx.css");
        final Stage stage = Optional.ofNullable(primaryStage).orElse(new Stage());
        stage.setTitle("Sketch.FX");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

    private void loadConfig( Stage stage ) {

        if ( config.load()) {
            stage.setX( config.getDouble("stage.x") );
            stage.setY( config.getDouble("stage.y") );
            stage.setWidth( config.getDouble("stage.width") );
            stage.setHeight( config.getDouble("stage.height") );
        }

    }

    private void storeConfig( Stage stage ) {

        config.setDouble("stage.x", stage.getX() );
        config.setDouble("stage.y", stage.getY() );
        config.setDouble("stage.width", stage.getWidth() );
        config.setDouble("stage.height", stage.getHeight() );
        config.store();

    }

}
