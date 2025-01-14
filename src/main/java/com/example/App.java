package com.example;

import com.example.Controller.Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Controller ctrl = new Controller();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View/ConflictsView.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Éditeur partagé");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
