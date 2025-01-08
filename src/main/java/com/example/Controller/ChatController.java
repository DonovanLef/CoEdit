package com.example.Controller;

import com.example.Model.MulticastEditor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ChatController {
    @FXML
    private ListView<String> messageList; // Liste des messages
    @FXML
    private TextField messageInput; // Zone de texte pour saisir un message

    private MulticastEditor multicastEditor;

    @FXML
    public void initialize() {
        try {
            // Initialisation du modèle avec un callback pour recevoir les messages
            multicastEditor = new MulticastEditor(message -> {
                // Mise à jour de l'interface utilisateur sur le thread JavaFX
                Platform.runLater(() -> messageList.getItems().add(message));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode appelée lorsque le bouton "Envoyer" est cliqué
    @FXML
    public void onSendMessage() {
        String message = messageInput.getText();
        if (!message.isEmpty()) {
            multicastEditor.sendMessage(message); // Envoi du message via le modèle
            messageInput.clear(); // Effacer le champ de texte
        }
    }
}
