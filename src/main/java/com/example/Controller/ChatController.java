package com.example.Controller;

import com.example.Model.MulticastEditor;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ChatController {

    @FXML
    private TextArea sharedTextArea; // Zone de texte partagée
    @FXML
    private TextField messageInput;  // Champ pour taper un message
    @FXML
    private Button saveButton;      // Bouton pour enregistrer

    private MulticastEditor multicastEditor;


    @FXML
    public void initialize() {
        try {
            // Initialisation du MulticastEditor avec un callback pour recevoir les messages
            multicastEditor = new MulticastEditor(this::onMessageReceived);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Lors de la modification du texte dans le TextArea, on envoie les modifications
        sharedTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            multicastEditor.sendMessage("TEXT_UPDATE:" + newValue);
        });
    }

    // Méthode appelée lorsqu'un message est reçu
    private void onMessageReceived(String message) {
        int caretPosition = sharedTextArea.getCaretPosition();
        // Si le message est un texte complet, mettez à jour la zone de texte
        if (message.startsWith("TEXT_UPDATE:")) {
            String updatedText = message.substring(12); // Extraire le texte après "TEXT_UPDATE:"
            sharedTextArea.setText(updatedText);
            sharedTextArea.positionCaret(caretPosition);
        }
    }

 


    // Méthode appelée lorsqu'on clique sur "Enregistrer"
    @FXML
    private void onSave() {
        String textToSave = sharedTextArea.getText();
        
        // Enregistrez le texte dans un fichier local
        try {
            File file = new File("document.txt"); // Utilisez le chemin que vous préférez
            FileWriter writer = new FileWriter(file);
            writer.write(textToSave);
            writer.close();
            System.out.println("Document sauvegardé !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
