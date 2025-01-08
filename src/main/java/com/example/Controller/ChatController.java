package com.example.Controller;

import com.example.Model.LineModel;
import com.example.Model.MulticastEditor;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ChatController {

    private ArrayList<LineModel> lines;

    @FXML
    private TextArea sharedTextArea; // Zone de texte partagée
    @FXML
    private TextField messageInput;  // Champ pour taper un message
    @FXML
    private Button saveButton;      // Bouton pour enregistrer

    private MulticastEditor multicastEditor;

    public static ArrayList<LineModel> readLinesFromFile(String filePath) {
        ArrayList<LineModel> lineModels = new ArrayList<>();
    
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Supposons que chaque ligne est au format "<?'idLine'; line"
                if (line.startsWith("<?") && line.contains(";>")) {
                    // Supprimer les balises "<?" et ">"
                    line = line.substring(2, line.length());
                    // Diviser la ligne en idLine et le contenu
                    String[] parts = line.split(";>");

                    // S'assurer que parts contient deux éléments après le split
                    if (parts.length >= 2) {
                        lineModels.add(new LineModel(Long.parseLong(parts[0]), parts[1]));
                    } else {
                        System.err.println("Ligne mal formée : " + line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return lineModels;
    }
    

    @FXML
    public void initialize() {
        
        lines = readLinesFromFile("document.txt");
        String textArea = "";
        for (LineModel lineModel : lines) {
            textArea += lineModel.getLine();
            textArea += "\n";
        }
        sharedTextArea.setText(textArea);

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
