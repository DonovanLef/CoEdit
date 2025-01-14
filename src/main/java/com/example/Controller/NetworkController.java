package com.example.Controller;

import com.example.Model.NetworkModel;

public class NetworkController {
	
	private Controller ctrl;
	private NetworkModel networkModel;
	public NetworkController() {
		this.networkModel = new NetworkModel();
	}
	public void setController(Controller ctrl) {
		this.ctrl = ctrl;
	}
	public void handleRequest(String message) {
		String res  = this.networkModel.handleRequest(message);	
		this.ctrl.getChatController().onMessageReceived(res);
	}
}
