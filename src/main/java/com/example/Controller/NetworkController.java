package com.example.Controller;

import java.util.Arrays;

import com.example.Model.LineModel;
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
	public void handleReceive(byte[] bytes) {
		//Transformer les 16premiers byte en decimal

		int code = this.networkModel.getCode(bytes);
		byte[] serial = Arrays.copyOfRange(bytes, 2, bytes.length);

		// Modification d'une ligne
		if (code == 100) {
			LineModel line = this.networkModel.handle200(serial);
			this.ctrl.getChatController().handleCreateLine(line);
			
		}
		// Creation d'un document
		if (code == 200) {

		}
		// Suppression d'un document
		if (code == 201) {

		}
		// demande de modification
		if (code == 202) {

		}

		//String res  = this.networkModel.handleReceive(message);	
		//this.ctrl.getChatController().onMessageReceived(res);
	}
}
