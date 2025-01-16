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
        
        Document doc = new Document();
        doc.setName("Matthewthewthew");
        doc.addLine(new LineModel("Hello"));
        ctrl.getFolderController().createDocument(doc);
        //concatenate the two byte arrays
        //ctrl.getNetworkController().handleReceive(concatenateByteArrays(byteArray, doc.toByteArray()));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Éditeur partagé");
        stage.setScene(scene);
        stage.show();
    }

    public static byte[] concatenateByteArrays(byte[] array1, byte[] array2) {
        int length1 = array1.length;
        int length2 = array2.length;

        // Create a new byte array to hold the concatenated result
        byte[] result = new byte[length1 + length2];

        // Copy elements from the first array
        System.arraycopy(array1, 0, result, 0, length1);

        // Copy elements from the second array
        System.arraycopy(array2, 0, result, length1, length2);

        return result;
    }
    public static void main(String[] args) {
        launch();
    }
}
