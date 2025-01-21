package com.example.Controller;

import com.example.Model.Document;
import com.example.Model.Folder;
import com.example.Model.LineModel;
import com.example.Model.MulticastEditor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatController {

    private ArrayList<LineModel> lines;

    @FXML
    private TextArea sharedTextArea;
    @FXML
    private TextField messageInput;
    @FXML
    private Button saveButton;

    private MulticastEditor multicastEditor;

    private File file;

    @FXML
    public void initialize() {
        Controller.ctrl.setChatController(this);
        try {
            // Initialisation du MulticastEditor avec un callback pour recevoir les messages
            multicastEditor = new MulticastEditor(Controller.ctrl.getNetworkController()::handleReceive);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sharedTextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateLineModels(newValue);
            }
        });

    }

    private void updateLineModels(String newText) {
        System.out.println("pass");
        String[] newLines = newText.split("\n");

        // Synchroniser lines avec newLines
        for (int i = 0; i < newLines.length; i++) {
            if (i < lines.size()) {
                if (!lines.get(i).getLine().equals(newLines[i])) {
                    // Mettre à jour la ligne existante si elle a changé
                    lines.get(i).setLine(newLines[i], Controller.ctrl.getUsername());
                    multicastEditor.sendLine(lines.get(i), Controller.ctrl);
                    System.out.println("envoie");
                }
            } else {
                // Ajouter une nouvelle ligne avec un GUID unique
                LineModel newLineModel = new LineModel(newLines[i], Controller.ctrl.getUsername());
                lines.add(newLineModel);
                multicastEditor.sendLine(newLineModel, Controller.ctrl);
                System.out.println("envoie");
            }
        }

        // Supprimer les lignes excédentaires si nécessaire
        while (lines.size() > newLines.length) {
            lines.remove(lines.size() - 1);
        }

        // Debug : Afficher les identifiants et le contenu
        lines.forEach(line -> System.out.println(
                "ID: " + line.getIdLine() + " | Content: " + line.getLine() + " | Created : " + line.getCreatedBy()));

    }

    private void setTextArea() {
        String textArea = "";
        for (LineModel lineModel : lines) {
            String line = lineModel.getLine();
            textArea += line + "\n";
        }
        sharedTextArea.setText(textArea);
    }

    public void handleCreateLine(LineModel other) {
        int caretPosition = sharedTextArea.getCaretPosition();

        for (LineModel lineModel : lines) {
            if (lineModel.getIdLine().equals(other.getIdLine())) {
                lineModel.setLine(other.getLine(), Controller.ctrl.getUsername());
                break;
            }
        }
        lines.add(other);

        this.setTextArea();
        sharedTextArea.positionCaret(caretPosition);
    }

    public void sendDocuments() {
        for (Document doc : DocumentController.getDocuments()) {
            short code = 203;
            try {
                this.multicastEditor.sendData(code, doc.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode appelée lorsqu'on clique sur "Enregistrer"
    @FXML
    private void onSave() {
        Document doc = new Document();
        doc.setName(this.file.getName());
        doc.setLines(lines);
        doc.save(Folder.PATH);
    }

    @FXML
    private void onBack(ActionEvent event) {
        onSave();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/View/FolderView.fxml"));
            Parent root = loader.load();

            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            Stage stage = new Stage();
            stage.setTitle("Folder Project");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
        }
    }

    public void setFile(File file) {
        this.file = file;
        try {
            // Charger le contenu du fichier dans le TextArea
            BufferedReader reader = new BufferedReader(new FileReader(file));
            reader.close();

            Document doc = Document.restoreByFile(Folder.PATH + file.getName());
            if (doc != null)
                lines = (ArrayList<LineModel>) doc.getLines();
            else {
                lines = new ArrayList<>();
                lines.add(new LineModel(Controller.ctrl.getUsername()));
            }

            this.setTextArea();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
