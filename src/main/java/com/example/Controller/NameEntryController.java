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

	private Controller ctrl;

	public void setController(Controller ctrl) {
        this.ctrl = ctrl;
    }

	@FXML
	private void onNameSubmit() {
		String userName = nameField.getText();
		if (!userName.isEmpty()) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/view/FolderView.fxml"));
				Parent root = loader.load();
	
				Stage stage = new Stage();
				stage.setTitle("Nouvelle Vue");
				stage.setScene(new Scene(root));
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Veuillez entrer un nom.");
		}
	}
}
