package com.example.Controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.example.Model.Document;
import com.example.Model.Folder;
import com.example.Model.LineModel;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
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
    @FXML
    private ContextMenu contextMenu;

    private Folder folder;

    private ScreenLockController screenLockController;

    private Controller ctrl;

    public void setController(Controller ctrl) {
        this.ctrl = ctrl;
    }

    @FXML
    public void initialize() {

        this.screenLockController = new ScreenLockController();

        Controller.ctrl.setFolderController(this);
        // Initialiser le dossier avec des fichiers
        this.folder = new Folder();
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

        // Ajouter un menu contextuel (clic droit) à chaque élément de la liste
        fileListView.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            };

            // Création du menu contextuel
            ContextMenu contextMenu = new ContextMenu();

            MenuItem openItem = new MenuItem("Ouvrir");
            openItem.setOnAction(event -> {
                String file = cell.getItem();
                if (file != null) {
                    openFile(file);
                }
            });

            MenuItem deleteItem = new MenuItem("Supprimer");
            deleteItem.setOnAction(event -> {
                String file = cell.getItem();
                if (file != null) {
                    folder.deleteFile(file); // Supprimer le fichier du dossier
                    fileListView.getItems().remove(file); // Supprimer de la liste
                }
            });

            MenuItem renameItem = new MenuItem("Renommer");
            renameItem.setOnAction(event -> {
                String oldName = cell.getItem();
                if (oldName != null) {
                    TextInputDialog dialog = new TextInputDialog(oldName);
                    dialog.setTitle("Renommer le fichier");
                    dialog.setHeaderText("Entrez le nouveau nom du fichier :");
                    dialog.setContentText("Nom du fichier :");

                    dialog.showAndWait().ifPresent(newName -> {
                        if (!newName.trim().isEmpty()) {
                            folder.renameFile(oldName, newName); // Renommer le fichier dans le dossier
                            int index = fileListView.getItems().indexOf(oldName);
                            fileListView.getItems().set(index, newName); // Mettre à jour la liste
                        }
                    });
                }
            });

            contextMenu.getItems().addAll(openItem, deleteItem, renameItem);

            // Afficher le menu contextuel sur clic droit
            cell.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY && !cell.isEmpty()) {
                    contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                } else {
                    contextMenu.hide();
                }
            });

            return cell;
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
        if (!fileName.endsWith(".txt"))fileName = fileName + ".ser";

        // Vérifier si le nom est valide (non vide)
        if (fileName.isEmpty()) {
            // Afficher une alerte si le nom est vide
            showAlert("Erreur", "Nom de fichier vide : ne peut pas être vide.", AlertType.ERROR);
            return;
        }

        if (!fileName.endsWith(".txt")) {
            fileName = fileName + ".txt";
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

            Document doc = new Document();
            doc.setName(fileName);
            ArrayList<LineModel> newLines = new ArrayList<>();
            newLines.add(new LineModel(Controller.ctrl.getUsername()));
            doc.setLines(newLines);
            doc.save(Folder.PATH);
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

    public void createDocument(Document doc) {
        doc.save(Folder.PATH);
    }
    public void deleteDocument(String name) {
        
        fileListView.getItems().remove(name);
    }
}
