package com.example.Controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {

	private ChatController chatController;
	private NameEntryController nameEntryController;

	public Controller() {
		this.chatController = new ChatController();
		this.nameEntryController = new NameEntryController();
	}


}
