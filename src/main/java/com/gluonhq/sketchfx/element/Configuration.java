package com.gluonhq.sketchfx.element;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Configuration {

    private final Path path;
    private final Properties props = new Properties();

    public Configuration( String configPrefix ) {
        this.path = Paths.get(System.getProperty("user.home"), ".sketchfx", configPrefix + ".properties");
    }

    public boolean load() {
        if (Files.exists(path)) {
            try {
                props.load(Files.newBufferedReader(path));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void store() {
        try {
            Files.createDirectories(path.getParent());
            props.store(Files.newBufferedWriter(path), "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(String key) {
        return props.getProperty(key);
    }

    public String getString(String key, String defaultValue ) {
        return props.getProperty(key, defaultValue);
    }

    public void setString( String key, String value ) {
        props.setProperty(key, value);
    }

    public double getDouble(String key) {
        return Double.valueOf(props.getProperty(key));
    }

    public double getString(String key, double defaultValue ) {
        String value = props.getProperty(key);
        return value == null? defaultValue: Double.valueOf(value);
    }

    public void setDouble( String key, double value ) {
        props.setProperty(key, Double.valueOf(value).toString());
    }


}
