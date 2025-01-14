package com.example.Controller;

import com.example.Model.LineModel;
import com.example.Model.MulticastEditor;
import com.example.Model.NetworkModel;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
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
    private TextField messageInput; // Champ pour taper un message
    @FXML
    private Button saveButton; // Bouton pour enregistrer

    private MulticastEditor multicastEditor;

    private File file;

    private Controller ctrl;

    public void setController(Controller ctrl) {
        this.ctrl = ctrl;
    }

    public ArrayList<LineModel> readLinesFromFile(String fileRepo) {
        String filePath = fileRepo + "/" + this.file.getName();
        ArrayList<LineModel> lineModels = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Supposons que chaque ligne est au format "<?'idLine'; line"
                if (line.startsWith("<?") && line.contains(";>")) {
                    // Supprimer les balises "<?" et ">"
                    line = line.substring(2, line.length());
                    System.out.println(line);
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
        try {
            // Initialisation du MulticastEditor avec un callback pour recevoir les messages
            multicastEditor = new MulticastEditor(this.ctrl.getNetworkModel()::handleRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sharedTextArea.setOnKeyReleased(event -> {
            if ((event.getCode().isLetterKey() || event.getCode().isDigitKey() || event.getCode().isWhitespaceKey()
                    || event.getCode().isKeypadKey()) && event.getCode() != KeyCode.ENTER) {
                int caretPosition = sharedTextArea.getCaretPosition();

                String beforeCaret = sharedTextArea.getText().substring(0, caretPosition);
                String afterCaret = sharedTextArea.getText().substring(caretPosition);

                int startOfLine = beforeCaret.lastIndexOf('\n');

                int endOfLine = afterCaret.indexOf('\n');

                if (endOfLine == -1)
                    endOfLine = afterCaret.length(); // S'il n'y a pas de '\n', prendre jusqu'à la fin

                if (startOfLine == -1)
                    startOfLine = 0;

                String currentLine = sharedTextArea.getText().substring(startOfLine, caretPosition + endOfLine).trim();

                int lineNumber = beforeCaret.split("\n").length;

                LineModel currentLineModel = null;

                if (lines.size() >= lineNumber && lineNumber >= 0)
                    currentLineModel = lines.get(lineNumber - 1);

                if (currentLineModel == null) {
                    currentLineModel = lines.get(lines.size() - 1);
                } else {
                    currentLineModel.setLine(currentLine);
                }

                multicastEditor.sendMessage(getLineFormat(currentLineModel));
            } else if (event.getCode() == KeyCode.ENTER) {
                lines.add(new LineModel(System.currentTimeMillis()));
            }
        });

    }

    private void setTextArea() {
        String textArea = "";
        for (LineModel lineModel : lines) {
            textArea += lineModel.getLine() + "\n";
        }
        sharedTextArea.setText(textArea);
    }

    // Méthode appelée lorsqu'un message est reçu
    public void onMessageReceived(String message) {
        if (message.startsWith("<?") && message.contains(";>")) {
            // Supprimer les balises "<?" et ">"
            message = message.substring(2, message.length());

            // Diviser la ligne en idLine et le contenu
            String[] parts = message.split(";>");

            if (parts.length >= 2) {
                long idLine = Long.parseLong(parts[0]);
                String line = parts[1];

                boolean isUpdate = false;
                for (LineModel lineModel : lines) {
                    if (lineModel.getIdLine() == idLine) {
                        lineModel.setLine(line);
                        isUpdate = true;
                    }
                }

                if (!isUpdate) {
                    lines.add(new LineModel(idLine, line));
                }
            }
        }

        int caretPosition = sharedTextArea.getCaretPosition();
        this.setTextArea();
        sharedTextArea.positionCaret(caretPosition);
    }

    // Méthode appelée lorsqu'on clique sur "Enregistrer"
    @FXML
    private void onSave() {

        // Enregistrez le texte dans un fichier local
        try {
            File file = new File("documents/" + this.file.getName()); // Utilisez le chemin que vous préférez
            FileWriter writer = new FileWriter(file);
            writer.write(getTextWithBalises());
            writer.close();
            System.out.println("Document sauvegardé !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFile(File file) {
        this.file = file;
        try {
            // Charger le contenu du fichier dans le TextArea
            BufferedReader reader = new BufferedReader(new FileReader(file));
            reader.close();

            // Mettre le contenu dans la zone de texte
            lines = readLinesFromFile("documents/");
            if (lines.size() == 0)
                lines.add(new LineModel(System.currentTimeMillis()));
    
            for (LineModel lineModel : lines) {
                System.out.println(getLineFormat(lineModel));
            }
            this.setTextArea();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getTextWithBalises() {
        String textArea = "";
        for (LineModel lineModel : lines) {
            textArea += getLineFormat(lineModel);
        }
        return textArea;
    }

    private String getLineFormat(LineModel lineModel) {
        return "<?" + lineModel.getIdLine() + ";>" + lineModel.getLine();
    }
}
