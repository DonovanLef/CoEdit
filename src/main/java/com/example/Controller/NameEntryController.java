package com.example.Controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NameEntryController {

	@FXML
	private TextField nameField;

	@FXML
	private void onNameSubmit() {
		String userName = nameField.getText();
		if (!userName.isEmpty()) {
			try {
				// Charger le fichier FXML de la nouvelle vue
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/View/ChatView.fxml"));
				Parent chatView = loader.load();

				// Optionnel : Passer des données au contrôleur de ChatView
				// ChatController controller = loader.getController();
				// controller.setUserName(userName);

				// Obtenir la scène actuelle et la remplacer par la nouvelle
				Stage stage = (Stage) nameField.getScene().getWindow();
				stage.setScene(new Scene(chatView));
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Veuillez entrer un nom.");
		}
	}
}
