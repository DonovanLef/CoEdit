package com.example.Controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ScreenLockController {

    private boolean isOpen;

    public ScreenLockController () {
        this.isOpen = false;
    }

	@FXML
	public void closeLock(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/View/FolderView.fxml"));
            Parent root = loader.load();

            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            Stage newStage = new Stage();
            newStage.setTitle("Nouvelle Vue");
            newStage.setScene(new Scene(root));
            newStage.show();

            this.isOpen = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

    public void openLock(Button btn) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/View/LockView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btn.getScene().getWindow();
            stage.setTitle("Merge en cours");
            stage.setScene(new Scene(root));
            stage.show();

            this.isOpen = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
