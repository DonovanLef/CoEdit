package com.example.Controller;

import com.example.Model.MulticastEditor;

public class Controller {

	public static Controller ctrl;

	private ChatController chatController;
	private ConflictsController conflictsController;
	private NetworkController networkController;
	private FolderController folderController;
	private NameEntryController nameEntryController;
	private StarterController starterController;
	private MulticastEditor multicastEditor;

	public Controller() {
		ctrl = this;
		this.chatController = new ChatController();
		this.conflictsController = new ConflictsController();
		this.networkController = new NetworkController();
		this.folderController = new FolderController();
		this.nameEntryController = new NameEntryController();
		this.starterController = new StarterController();

		try {
            // Initialisation du MulticastEditor avec un callback pour recevoir les messages
            multicastEditor = new MulticastEditor(getNetworkController()::handleReceive);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

	public void setFolderController(FolderController folderController) {
		this.folderController = folderController;
	}

	public void setChatController(ChatController chatController) {
		this.chatController = chatController;
	}
	public MulticastEditor getMulticastEditor() {
		return multicastEditor;
	}

	
	

	public void setNameEntryController(NameEntryController nameEntryController) {
		this.nameEntryController = nameEntryController;
	}
	
	
 	
}
