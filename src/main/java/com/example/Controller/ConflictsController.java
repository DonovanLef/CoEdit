package com.example.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.example.Model.Document;
import com.example.Model.LineModel;

public class ConflictsController {
    @FXML
    private TextArea textAreaLeft;

    @FXML
    private TextArea textAreaRight;

    private Iterator<Map.Entry<Document, Document>> iterator;
    private Map.Entry<Document, Document> currentEntry;

    private Document leftDoc;
    private Document rightDoc;

    private Button btn;
    private Stage stageFolder;

    @FXML
    public void initialize() {
        // Initialisation
    }

    public void setDocuments(Map<Document, Document> docToMerge) {
        this.iterator = docToMerge.entrySet().iterator();
        showNextPair();
    }

    private void showNextPair() {
        if (iterator != null && iterator.hasNext()) {
            currentEntry = iterator.next();
            leftDoc = (Document) currentEntry.getKey();
            rightDoc = (Document) currentEntry.getValue();

            textAreaLeft.setText(getTextByDoc(leftDoc));
            textAreaRight.setText(getTextByDoc(rightDoc));
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fin");
            alert.setContentText("Plus de documents à comparer.");
            alert.showAndWait();

            // Controller.ctrl.getMulticastEditor().sendUnlockTextArea();
            // Controller.ctrl.getMulticastEditor().sendUnlockTextArea();

            // Stage stage = (Stage) textAreaLeft.getScene().getWindow();                
            // stage.setScene(stageFolder.getScene());
            // stage.show();

            try {
                Controller.ctrl.getMulticastEditor().sendUnlockTextArea();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/View/FolderView.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) textAreaLeft.getScene().getWindow();                
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getTextByDoc(Document doc) {
        StringBuilder newText = new StringBuilder();
        for (LineModel line : doc.getLines()) {
            newText.append(line.getLine()).append("\n");
        }
        return newText.toString();
    }

    public void onSelectLeft() {
        showAlert("Zone gauche sélectionnée", textAreaLeft.getText());
        Controller.ctrl.getMulticastEditor().sendDocument(leftDoc, Controller.ctrl);
        showNextPair();
    }

    public void onSelectRight() {
        showAlert("Zone droite sélectionnée", textAreaRight.getText());
        Controller.ctrl.getMulticastEditor().sendDocument(rightDoc, Controller.ctrl);
        showNextPair();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setButton(Button btnAdd) {
        this.btn = btnAdd;
    }

    public void setFolderStage(Stage stage) {
        stageFolder = stage;
    }
}
