package com.example.Controller;


import com.example.Model.Folder;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FolderController {

    @FXML
    private ListView<String> fileListView; // Liste des fichiers
    @FXML
    private TextArea fileContentArea;      // Zone pour afficher le contenu du fichier
    @FXML
    private Button openButton;             // Bouton pour ouvrir le fichier sélectionné

    private Folder fileFolder;

    @FXML
    public void initialize() {
        // Initialiser le dossier avec des fichiers
        fileFolder = new Folder();

        // Ajouter des fichiers pour l'exemple
        File file1 = new File("document1.txt");
        File file2 = new File("document2.txt");
        fileFolder.addFile(file1);
        fileFolder.addFile(file2);

        // Ajouter la liste des fichiers au ListView
        fileListView.getItems().addAll(fileFolder.getFileNames());

        // Gérer l'ouverture du fichier
        openButton.setOnAction(event -> openFile());
    }

    // Ouvrir le fichier sélectionné dans le ListView
    @FXML
    private void openFile() {
        String selectedFileName = fileListView.getSelectionModel().getSelectedItem();

        if (selectedFileName != null) {
            File selectedFile = fileFolder.getFile(selectedFileName);

            if (selectedFile != null) {
                // Lire le contenu du fichier et l'afficher dans la zone de texte
                try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    fileContentArea.setText(content.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
