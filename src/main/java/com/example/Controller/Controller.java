package com.example.Controller;


public class Controller {
	private ChatController chatController;
	private ConflictsController conflictsController;
	private NetworkController networkController;
	private FolderController folderController;
	private NameEntryController nameEntryController;

	public Controller() {
		this.chatController = new ChatController();
		this.conflictsController = new ConflictsController();
		this.networkController = new NetworkController();
		this.folderController = new FolderController();
		this.nameEntryController = new NameEntryController();

		this.chatController.setController(this);
		this.conflictsController.setController(this);
		this.networkController.setController(this);
		this.folderController.setController(this);
		this.nameEntryController.setController(this);
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
	
}
