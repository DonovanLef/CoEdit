package com.example.Controller;


import com.example.Model.Folder;


import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
    private Button btnAdd;             // Bouton pour ouvrir le fichier sélectionné
    @FXML
    private TextField fileNameTextField; 
    
    private Folder folder;

    @FXML
    public void initialize() {
        // Initialiser le dossier avec des fichiers
        folder = new Folder();

        // Ajouter des fichiers pour l'exemple
        folder.scanDocumentsFolder();
        // Ajouter la liste des fichiers au ListView
        fileListView.getItems().addAll(folder.getFileNames());

        

        fileListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-clic
                String selectedFile = fileListView.getSelectionModel().getSelectedItem();
                if (selectedFile != null) {
                    openFile(selectedFile);
                }
            }
        });
        fileListView.setOnEditCommit(event -> {
            String newName = event.getNewValue();
            folder.createFile(newName); // Créer le fichier avec le nom final
            // Mettre à jour la liste
            int index = event.getIndex();
            fileListView.getItems().set(index, newName);
        });
    }

    // Ouvrir le fichier sélectionné dans le ListView
    @FXML
    private void openFile(String file) {
            File selectedFile = folder.getFile(file);

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
    @FXML
    public void createFile() {
        // Récupérer le nom du fichier à partir du TextField
        String fileName = fileNameTextField.getText().trim();
        if (!fileName.endsWith(".txt"))fileName = fileName + ".txt";

        // Vérifier si le nom est valide (non vide)
        if (fileName.isEmpty()) {
            // Afficher une alerte si le nom est vide
            showAlert("Erreur", "Le nom du fichier ne peut pas être vide.", AlertType.ERROR);
            return;
        }

        String erreur = folder.createFile(fileName);
        // Créer un nouveau fichier dans le dossier documents
        if (erreur.length() > 0) {
            // Afficher une alerte si le fichier existe déjà
            String[] parts = erreur.split(":");
            showAlert(parts[0], parts[1], AlertType.WARNING);
        } else {
            // Ajouter le nom du fichier à la liste
            fileListView.getItems().add(fileName);
        }
    }
    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    
}
