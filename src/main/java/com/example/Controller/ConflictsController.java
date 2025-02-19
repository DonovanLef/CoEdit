package com.example.Controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.example.Model.Document;
import com.example.Model.LineModel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ConflictsController {
    @FXML
    private TextArea textAreaLeft;

    @FXML
    private TextArea textAreaRight;

    @FXML
    private Label labelLeft;

    @FXML
    private Label labelRight;

    private Iterator<Map.Entry<Document, Document>> iterator;
    private Map.Entry<Document, Document> currentEntry;

    private Document leftDoc;
    private Document rightDoc;

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

            labelLeft.setText(leftDoc.getName() + " entrant");
            textAreaLeft.setText(getTextByDoc(leftDoc));
            labelRight.setText(rightDoc.getName() + " actuel");
            textAreaRight.setText(getTextByDoc(rightDoc));
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fin");
            alert.setContentText("Plus de documents à comparer.");
            alert.showAndWait();

            try {
                Controller.ctrl.getMulticastEditor().sendUnlockTextArea();
                Controller.ctrl.getStarterController().clearDocumentsReceived();
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

}
