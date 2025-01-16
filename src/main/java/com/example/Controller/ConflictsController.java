package com.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

public class ConflictsController {
	@FXML
    private TextArea textAreaLeft;

    @FXML
    private TextArea textAreaRight;

    private Controller ctrl;

    public void setController(Controller ctrl) {
        this.ctrl = ctrl;
    }

   

    // Action pour le bouton de gauche
    public void onSelectLeft() {
        showAlert("Zone gauche sélectionnée", textAreaLeft.getText());
    }

    // Action pour le bouton de droite
    public void onSelectRight() {
        showAlert("Zone droite sélectionnée", textAreaRight.getText());
    }

    // Méthode utilitaire pour afficher un message
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

