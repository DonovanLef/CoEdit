package com.example.Controller;

public class Controller {

	public static Controller ctrl;

	private ChatController chatController;
	private ConflictsController conflictsController;
	private NetworkController networkController;
	private FolderController folderController;
	private NameEntryController nameEntryController;
	private StarterController starterController;

	public Controller() {
		ctrl = this;
		this.chatController = new ChatController();
		this.conflictsController = new ConflictsController();
		this.networkController = new NetworkController();
		this.folderController = new FolderController();
		this.nameEntryController = new NameEntryController();
		this.starterController = new StarterController();

	}

	public ChatController getChatController() {
		return chatController;
	}
	public ConflictsController getConflictsController() {
		return conflictsController;
	}
	public NetworkController getNetworkController() {
		return networkController;
	}
	public FolderController getFolderController() {
		return folderController;
	}
	public NameEntryController getNameEntryController() {
		return nameEntryController;
	}

	public StarterController getStarterController(){
		return starterController;
	}

	public String getUsername() {
		return nameEntryController.getName();
	}
	
	
 	
}
