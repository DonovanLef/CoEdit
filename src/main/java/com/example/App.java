package com.example;

import java.nio.ByteBuffer;

import com.example.Controller.Controller;
import com.example.Model.Document;
import com.example.Model.LineModel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Controller ctrl = new Controller();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View/FolderView.fxml"));
        byte[] byteArray = ByteBuffer.allocate(2).putShort((short) 200).array();
        
        // Document doc = new Document();
        // doc.setName("Matthewthewthew");
        // doc.addLine(new LineModel(System.currentTimeMillis(), "Hello"));
        // ctrl.getFolderController().createDocument(doc);
    //    Document.deleteFile(doc.getName());
        //concatenate the two byte arrays
        //ctrl.getNetworkController().handleReceive(concatenateByteArrays(byteArray, doc.toByteArray()));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Éditeur partagé");
        stage.setScene(scene);
        stage.show();
    }

   
    public static void main(String[] args) {
        launch();
    }
}
