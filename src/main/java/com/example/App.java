package com.example;

import java.io.File;
import java.nio.ByteBuffer;

import com.example.Controller.Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Controller ctrl = new Controller();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View/NameView.fxml"));
        byte[] byteArray = ByteBuffer.allocate(2).putShort((short) 200).array();
        
        Scene scene = new Scene(loader.load());
        stage.setTitle("CoEdit - Éditeur partagé");
        File iconFile = new File("src/main/resources/icons/icons_coedit.png");
        stage.getIcons().add(new Image(iconFile.toURI().toString()));
        stage.setScene(scene);
        stage.show();
    }
   
    public static void main(String[] args) {
        launch();
    }
}
