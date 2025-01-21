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
import java.util.Iterator;

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

    private final ChangeListener<String> textAreaChangeListener = (observable, oldValue, newValue) -> {
        if (!oldValue.equals(newValue)) {
            updateLineModels(newValue);
        }
    };

    private int savedCaretPosition;
    private int savedCaretLineIndex;
    
    private void saveCaretPosition() {
        savedCaretPosition = sharedTextArea.getCaretPosition();
        String[] lines = sharedTextArea.getText().split("\n");
        savedCaretLineIndex = 0;
        int currentPos = 0;
    
        for (int i = 0; i < lines.length; i++) {
            if (currentPos + lines[i].length() >= savedCaretPosition) {
                savedCaretLineIndex = i;
                break;
            }
            currentPos += lines[i].length() + 1; // +1 pour le saut de ligne
        }
    }

    private void adjustCaretPositionForChanges(String oldText, String newText) {
        // Analyser les différences entre l'ancienne et la nouvelle version du texte
        String[] oldLines = oldText.split("\n");
        String[] newLines = newText.split("\n");
    
        int lengthDifference = 0;
    
        // Calculer l'effet des changements sur les lignes précédentes
        for (int i = 0; i < savedCaretLineIndex; i++) {
            int oldLineLength = oldLines[i].length();
            int newLineLength = newLines[i].length();
            lengthDifference += (newLineLength - oldLineLength);
        }
    
        // Si la ligne où se trouve le caret est modifiée, ajuster la position
        if (savedCaretLineIndex < newLines.length) {
            int oldLineLength = oldLines[savedCaretLineIndex].length();
            int newLineLength = newLines[savedCaretLineIndex].length();
            lengthDifference += (newLineLength - oldLineLength);
        }
    
        // Calculer la nouvelle position du caret
        int newCaretPosition = savedCaretPosition + lengthDifference;
    
        // S'assurer que la position du caret est valide (ne pas dépasser la longueur du texte)
        newCaretPosition = Math.min(newCaretPosition, sharedTextArea.getLength());
    
        // Mettre à jour la position du caret
        sharedTextArea.positionCaret(newCaretPosition);
    }
    
    
    
    

    @FXML
    public void initialize() {
        Controller.ctrl.setChatController(this);
        try {
            // Initialisation du MulticastEditor avec un callback pour recevoir les messages
            multicastEditor = new MulticastEditor(Controller.ctrl.getNetworkController()::handleReceive);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sharedTextArea.textProperty().addListener(textAreaChangeListener);

    }

    private void updateLineModels(String newText) {
        String[] newLines = newText.split("\n");
        int minLength = Math.min(newLines.length, lines.size());

        // Mise à jour ou insertion des lignes existantes ou nouvelles
        for (int i = 0; i < minLength; i++) {
            if (!lines.get(i).getLine().equals(newLines[i])) {
                lines.get(i).setLine(newLines[i], Controller.ctrl.getUsername());
                multicastEditor.sendLine(lines.get(i), Controller.ctrl);
            }
        }

        // Ajout des nouvelles lignes
        for (int i = minLength; i < newLines.length; i++) {
            LineModel newLineModel = new LineModel(newLines[i], Controller.ctrl.getUsername());
            lines.add(newLineModel);
            multicastEditor.sendLine(newLineModel, Controller.ctrl);
        }

        // Suppression des lignes excédentaires
        while (lines.size() > newLines.length) {
            lines.remove(lines.size() - 1);
        }

        // Debug : Afficher les identifiants et le contenu
        lines.forEach(line -> System.out.println(
                "ID: " + line.getIdLine() + " | Content: " + line.getLine() + " | Created : " + line.getCreatedBy()));
    }

    public void setTextArea() {
        // 1. Sauvegarder la position du caret avant toute modification du texte
        saveCaretPosition();
    
        // 2. Appliquer les modifications au TextArea
        StringBuilder newText = new StringBuilder();
        for (LineModel line : lines) {
            newText.append(line.getLine()).append("\n");
        }
        sharedTextArea.setText(newText.toString());
    
        // 3. Appliquer la mise à jour du caret après modification du texte
        adjustCaretPositionForChanges(sharedTextArea.getText(), newText.toString());
    }
    
    

    public void addLine(LineModel other) {
        Iterator<LineModel> iterator = lines.iterator();
        while (iterator.hasNext()) {
            LineModel lineModel = iterator.next();
            if (lineModel.getIdLine().equals(other.getIdLine())) {
                lineModel.setLine(other.getLine(), Controller.ctrl.getUsername());
                return;
            }
        }
        lines.add(other);
    }

    public synchronized void handleCreateLine(LineModel line) {
        System.out.println("received : " + line.getIdLine() + " | " + line.getLine());
        int caretPosition = sharedTextArea.getCaretPosition();

        this.addLine(line);
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
