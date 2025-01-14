package com.example.Controller;


import java.io.File;
import java.io.IOException;

import com.example.Model.Folder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    private ScreenLockController screenLockController;

    private Controller ctrl;

    public void setController(Controller ctrl) {
        this.ctrl = ctrl;
    }

    @FXML
    public void initialize() {

        this.screenLockController = new ScreenLockController();

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
        fileNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            fileNameTextField.setText(newValue.replaceAll("[^A-Za-z0-9\\-]", ""));
        });
        
    }

    // Ouvrir le fichier sélectionné dans le ListView
    @FXML
    private void openFile(String file) {
            File selectedFile = folder.getFile(file);

            try {
				// Charger le fichier FXML de la nouvelle vue
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/View/ChatView.fxml"));
				Parent chatView = loader.load();

				ChatController chatController = loader.getController();
                chatController.setFile(selectedFile);
				Stage stage = (Stage) btnAdd.getScene().getWindow();
				stage.setScene(new Scene(chatView));
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
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

    @FXML
    private void lockScreen(ActionEvent event) {
        this.screenLockController.openLock(event);
    }

}
