package com.example.Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.example.Model.Document;
import com.example.Model.Folder;
import com.example.Model.LineModel;
import com.example.Model.MulticastEditor;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ChatController {

    private ArrayList<LineModel> lines;

    @FXML
    private TextArea sharedTextArea;
    @FXML
    private TextField messageInput;
    @FXML
    private Button saveButton;


    private File file;

    private final ChangeListener<String> textAreaChangeListener = (observable, oldValue, newValue) -> {
        if (!oldValue.equals(newValue)) {
            updateLineModels(newValue);
        }
    };

    // Sauvegarder la position du caret avant modification
    private int savedCaretPosition = 0;
    private int savedLineCaretPosition = 0;
    private int charBefore;

    public Button getButton() {
        return saveButton;
    }

    public void saveCaretPosition() {
        savedCaretPosition = sharedTextArea.getCaretPosition();

        this.savedLineCaretPosition = getLineIndexFromPosition(savedCaretPosition);
        int charBef = 0;

        for (int i = 0; i < this.savedLineCaretPosition; i++) {
            charBef += lines.get(i).getLine().length();
        }

        this.charBefore = charBef;
    }

    private int getLineIndexFromPosition(int position) {
        String text = sharedTextArea.getText();
        String[] lines = text.split("\n");

        // Si la position est en dehors du texte
        if (position < 0 || position > text.length()) {
            throw new IllegalArgumentException("La position du caret est invalide.");
        }

        int currentPos = 0;
        for (int index = 0; index < lines.length; index++) {
            // Vérifier si la position se trouve dans cette ligne
            if (currentPos <= position && position < currentPos + lines[index].length() + 1) { // +1 pour le caractère
                                                                                               // de saut de ligne
                return index;
            }
            currentPos += lines[index].length() + 1; // Ajouter la longueur de la ligne + saut de ligne
        }

        // Si la position dépasse la longueur du texte, renvoyer la dernière ligne
        return lines.length - 1;
    }

    private void adjustCaretPositionForChanges() {

        int charBef = 0;
        for (int i = 0; i < this.savedLineCaretPosition; i++) {
            charBef += this.lines.get(i).getLine().length();

        }

        int newPos = savedCaretPosition + (charBef - this.charBefore);

        sharedTextArea.positionCaret(newPos);
    }

    @FXML
    public void initialize() {
        Controller.ctrl.setChatController(this);

        sharedTextArea.textProperty().addListener(textAreaChangeListener);

    }

    private void updateLineModels(String newText) {
        String[] newLines = newText.split("\n");
        int minLength = Math.min(newLines.length, lines.size());

        // Mise à jour ou insertion des lignes existantes ou nouvelles
        for (int i = 0; i < minLength; i++) {
            if (!lines.get(i).getLine().equals(newLines[i])) {
                lines.get(i).setLine(newLines[i], Controller.ctrl.getUsername());
                Controller.ctrl.getMulticastEditor().sendLine(lines.get(i), Controller.ctrl);
            }
        }

        // Ajout des nouvelles lignes
        for (int i = minLength; i < newLines.length; i++) {
            LineModel newLineModel = new LineModel(newLines[i], Controller.ctrl.getUsername(), this.file.getName());
            lines.add(newLineModel);
            Controller.ctrl.getMulticastEditor().sendLine(newLineModel, Controller.ctrl);
        }

        // Suppression des lignes excédentaires
        while (lines.size() > newLines.length) {
            lines.remove(lines.size() - 1);
        }

        // // Debug : Afficher les identifiants et le contenu
        // lines.forEach(line -> System.out.println(
        //         "ID: " + line.getIdLine() + " | Content: " + line.getLine() + " | Created : " + line.getCreatedBy()));
    }

    public void setTextArea() {

        // Appliquer les modifications au TextArea
        StringBuilder newText = new StringBuilder();
        for (LineModel line : lines) {
            newText.append(line.getLine()).append("\n");
        }
        sharedTextArea.setText(newText.toString());

        // Vérifier et ajuster la position du caret après la mise à jour
        try {
            adjustCaretPositionForChanges();
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur de position de caret: " + e.getMessage());
        }
    }

    public void addLineCurrentDoc(LineModel other) {
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

    public void handleCreateLine(LineModel line) {
        System.out.println(line.getLine());

        if (line.getDocName().equals(this.file.getName())) {
            if (line.getModifiedBy() != null && line.getModifiedBy().equals(Controller.ctrl.getUsername())) return;
            double savedScrollY = 0;
            double savedScrollX = 0;
    
            ScrollBar scrollBarVertical = (ScrollBar) sharedTextArea.lookup(".scroll-bar:vertical");
            ScrollBar scrollBarHorizontal = (ScrollBar) sharedTextArea.lookup(".scroll-bar:horizontal");
    
            if (scrollBarVertical != null) {
                savedScrollY = scrollBarVertical.getValue();
            }
            if (scrollBarHorizontal != null) {
                savedScrollX = scrollBarHorizontal.getValue();
            }
    
            saveCaretPosition();
    
            this.addLineCurrentDoc(line);
            this.setTextArea();
    
            scrollBarVertical = (ScrollBar) sharedTextArea.lookup(".scroll-bar:vertical");
            scrollBarHorizontal = (ScrollBar) sharedTextArea.lookup(".scroll-bar:horizontal");
        
            if (scrollBarVertical != null) {
                scrollBarVertical.setValue(savedScrollY);
            }
            if (scrollBarHorizontal != null) {
                scrollBarHorizontal.setValue(savedScrollX);
            }
        }
        else {
            this.addLine(line);
        }

    }

    public void addLine(LineModel other) {
        Document doc = null;
        for (Document document : DocumentController.getDocuments()) {
            if (document.getName().equals(other.getDocName())) {
                doc = document;
                break;
            }
        }
    
        if (doc == null) return; 

        Iterator<LineModel> iterator = doc.getLines().iterator();
        while (iterator.hasNext()) {
            LineModel lineModel = iterator.next();
            if (lineModel.getIdLine().equals(other.getIdLine())) {
                lineModel.setLine(other.getLine(), Controller.ctrl.getUsername());
                return;
            }
        }
        doc.getLines().add(other);
    }

    public void sendDocuments() {
        for (Document doc : DocumentController.getDocuments()) {
            if (this.file != null && doc.getName().equals(this.file.getName())) {
                doc.setLines(lines);
            }
            try {
                byte[] v = Controller.ctrl.getNetworkController().IntToByte((short)203);
                byte[] d = doc.toByteArray();
                byte[] data=  Controller.ctrl.getNetworkController().concatenateByteArrays(v, d);
                //two array in one
                Controller.ctrl.getMulticastEditor().sendData(data);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void askDocuments(){
        Controller.ctrl.getMulticastEditor().sendData((short)202, new byte[0]);
    }

    // Méthode appelée lorsqu'on clique sur "Enregistrer"
    @FXML
    private void onSave() {
        for (Document document : DocumentController.getDocuments()) {
            System.out.println(document.getName());
            if (document.getName().equals(this.file.getName())) {
                document.setLines(lines);
            }
            for (LineModel lineModel : document.getLines()) {
                System.out.println(lineModel.getLine());
            }
            document.save(Folder.PATH);
        }
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

            Document doc = null;

            for (Document document : DocumentController.getDocuments()) {
                if (document.getName().equals(this.file.getName()))
                    doc = document;
            }

            if (doc != null)
                lines = (ArrayList<LineModel>) doc.getLines();
            else {
                lines = new ArrayList<>();
                lines.add(new LineModel(Controller.ctrl.getUsername(), this.file.getName()));
            }

            this.setTextArea();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lockTextArea(boolean disable) {
        if (this.sharedTextArea == null) return;
        this.sharedTextArea.setDisable(disable);
    }

}
