package com.example.Controller;

import com.example.Model.LineModel;
import com.example.Model.MulticastEditor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

    private boolean isUserChange = false;
    private boolean enterPressed = false;
    private boolean deletePressed = false;

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
                    // Diviser la ligne en idLine et le contenu
                    String[] parts = line.split(";>");

                    // S'assurer que parts contient deux éléments après le split
                    if (parts.length >= 2) {
                        if (!parts[1].isEmpty())
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
            multicastEditor = new MulticastEditor(this::onMessageReceived);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // sharedTextArea.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
        //     enterPressed = event.getCode() == KeyCode.ENTER;
        //     deletePressed = event.getCode() == KeyCode.BACK_SPACE;
        //     isUserChange = event.getCode().isLetterKey();
        // });


        // sharedTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
        //     if (isUserChange || enterPressed || deletePressed) {
        //         isUserChange = false; 
        //         handleUserTextChange(oldValue, newValue);
        //     }
        // });

        sharedTextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateLineModels(newValue);
            }
        });
    }

    private void updateLineModels(String newText) {
        String[] newLines = newText.split("\n");

        // Synchroniser lines avec newLines
        for (int i = 0; i < newLines.length; i++) {
            if (i < lines.size()) {
                // Mettre à jour la ligne existante
                lines.get(i).setLine(newLines[i]);
            } else {
                // Ajouter une nouvelle ligne avec un GUID unique
                lines.add(new LineModel(newLines[i]));
            }
        }

        // Supprimer les lignes excédentaires si nécessaire
        while (lines.size() > newLines.length) {
            lines.remove(lines.size() - 1);
        }

        // Debug : Afficher les identifiants et le contenu
        lines.forEach(line -> System.out.println("ID: " + line.getIdLine() + " | Content: " + line.getLine()));
    }

    private void setTextArea() {
        String textArea = "";
        for (LineModel lineModel : lines) {
            String line = lineModel.getLine();
            if (!line.isEmpty()) {
                line = line.replace("<!:>", "");
                textArea += line + "\n";
            }

        }
        textArea = textArea.substring(0, textArea.length()-1);
        sharedTextArea.setText(textArea);
    }

    // Méthode appelée lorsqu'un message est reçu
    private void onMessageReceived(String message) {
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
                lines.add(new LineModel());

            this.setTextArea();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getTextWithBalises() {
        String textArea = "";
        for (LineModel lineModel : lines) {
            if (!lineModel.getLine().isEmpty()) {
                textArea += getLineFormat(lineModel) + "\n";
            }
        }
        return textArea;
    }

    private String getLineFormat(LineModel lineModel) {
        return "<?" + lineModel.getIdLine() + ";>" + lineModel.getLine();
    }

    private void handleUserTextChange(String oldText, String newText) {
        // Logique pour traiter le changement de texte
        int caretPosition = sharedTextArea.getCaretPosition();

        String beforeCaret = sharedTextArea.getText().substring(0, caretPosition);
        String afterCaret = sharedTextArea.getText().substring(caretPosition);

        int startOfLine = beforeCaret.lastIndexOf('\n');

        int endOfLine = afterCaret.indexOf('\n');

        if (endOfLine == -1)
            endOfLine = afterCaret.length(); // S'il n'y a pas de '\n', prendre jusqu'à la fin

        if (startOfLine == -1)
            startOfLine = 0;

        System.out.println("beforeCaret : " + beforeCaret + " ||");
        String[] parts = beforeCaret.split("\n");
        System.out.println();
        System.out.println();
        System.out.println();
        for (int i = 0; i < parts.length; i++) {
            System.out.println(parts[i]);
        }
        int lineNumber = beforeCaret.split("\n").length -1;

        System.out.println("--------------- : " + lineNumber);
        System.out.println("--------------- : ");

        if (beforeCaret.endsWith("\n")) {
            System.out.println("pass32");
            lineNumber ++;
        }
        

        String currentLine = sharedTextArea.getText().substring(startOfLine, caretPosition + endOfLine);
        if (currentLine.endsWith("\n")) {
            currentLine = currentLine.substring(0, currentLine.length() - 1);
        }
        else if (currentLine.startsWith("\n")) {
            currentLine = currentLine.substring(1);
        }


        int oldLineCount = oldText.split("\n").length;
        int newLineCount = newText.split("\n").length;

        System.out.println("oldLineCount : " + oldLineCount);
        System.out.println("newLineCount : " + newLineCount);
        System.out.println("currentLine : " + currentLine);

        if (enterPressed) {
            enterPressed = false;
            System.out.println("newLine");
            System.out.println("afterCaret : " + afterCaret);
            afterCaret = afterCaret.substring(1);
            int nextLine = afterCaret.indexOf("\n");
            String newEndOfLine = "";
            if (nextLine != -1)
                newEndOfLine = afterCaret.substring(0, nextLine);
            handleNewLine(caretPosition, currentLine,newEndOfLine ,lineNumber);
        } else if (deletePressed && oldLineCount > newLineCount) {
            deletePressed = false; 
            System.out.println("removeLine");
            if (lineNumber <= lines.size() -1) {
                LineModel lineModel = lines.get(lineNumber);
                lineModel.setLine("");
            }
        } else {
            System.out.println("updateLine");
            updateCurrentLine(currentLine, lineNumber);
        }
    }


    private void handleNewLine(int caretPosition, String currentLine, String endOfLine,int lineNumber) {
        System.out.println("caret : " + caretPosition);
        System.out.println("current : " + currentLine);
        System.out.println("currentLength : " + currentLine.length());
        System.out.println("endOfLine : " + endOfLine);
        System.out.println("lineNb : " + lineNumber);
        if (endOfLine.isEmpty()) {
            System.out.println("test1");
            LineModel newLineModel = new LineModel(System.currentTimeMillis(), "<!:>");
            lines.add(lineNumber+1, newLineModel);
            multicastEditor.sendMessage(getLineFormat(newLineModel));
        } else {
            LineModel lineModel = lines.get(lineNumber);
            lineModel.setLine(currentLine);
            multicastEditor.sendMessage(getLineFormat(lineModel));

            LineModel newLineModel = new LineModel(System.currentTimeMillis(), endOfLine);
            lines.add(lineNumber+1, newLineModel);

            multicastEditor.sendMessage(getLineFormat(newLineModel));
        }
    }

    private void updateCurrentLine(String currentLine, int lineNumber) {
        System.out.println("current : " + currentLine);
        System.out.println("lineNb : " + lineNumber);
        System.out.println("lineNb : " + lineNumber);
        LineModel currentLineModel = (lines.size() >= lineNumber && lineNumber >= 0) ? lines.get(lineNumber) 
                : lines.get(lines.size() - 1);

        if (currentLine.length() == 0) currentLine = "<!:>";
        if (currentLineModel != null) {
            currentLineModel.setLine(currentLine);
        }
        

        multicastEditor.sendMessage(getLineFormat(currentLineModel));
    }


}
