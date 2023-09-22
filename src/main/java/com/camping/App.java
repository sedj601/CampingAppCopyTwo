package com.camping;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

/**
 * JavaFX App
 */
public class App extends Application {
    @Override
    public void start(Stage primaryStage) {        
        try 
        {
            Locale defaultLocale = Locale.getDefault();
            System.out.println("default Locale: " + defaultLocale.toLanguageTag());
            ResourceBundle resourceBundle = ResourceBundle.getBundle("com.camping.resourcebundle.main", defaultLocale);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("primary.fxml"), resourceBundle);
            VBox root = fxmlLoader.load();
            PrimaryController primaryController = fxmlLoader.getController();            
            
            DataModel dataModel = new DataModel();
            primaryController.initDataModel(dataModel, resourceBundle);
            
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}