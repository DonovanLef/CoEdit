package com.example;

import java.nio.ByteBuffer;

import com.example.Controller.Controller;
import com.example.Controller.StarterController;
import com.example.Model.Document;
import com.example.Model.LineModel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        new Controller();
        // On demande les datas
        Controller.ctrl.getChatController().askDocuments();
        // Attendre la fin
        // On se connecte, donc on fetch tout, une fois que tout est fetch, on ouvre l'app
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View/FolderView.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Éditeur partagé");
        stage.setScene(scene);
        stage.show();
    }

   
    public static void main(String[] args) {
        launch();
    }
}
