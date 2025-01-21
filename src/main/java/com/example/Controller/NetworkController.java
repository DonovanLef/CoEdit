package com.example.Controller;

import java.util.Arrays;

import com.example.Model.Document;
import com.example.Model.LineModel;
import com.example.Model.NetworkModel;

public class NetworkController {
	
	
	private NetworkModel networkModel;
	public NetworkController() {
		this.networkModel = new NetworkModel();
	}
	
	public void handleReceive(byte[] bytes) {
		//Transformer les 16premiers byte en decimal

		int code = this.networkModel.getCode(bytes);
		byte[] serial = Arrays.copyOfRange(bytes, 2, bytes.length);

		// Modification d'une ligne
		if (code == 100) {
			LineModel line = this.networkModel.handle100(serial);
			Controller.ctrl.getChatController().handleCreateLine(line);
			
		}
		// Creation d'un document
		if (code == 200) {
			Document doc = this.networkModel.handle200(serial);
			Controller.ctrl.getFolderController().createDocument(doc);
		}

		// Suppression d'un document
		if (code == 201) {
			String name = new String(serial);
			Controller.ctrl.getFolderController().deleteDocument(name);
		}

		// demande de modification
		if (code == 202) {
			Controller.ctrl.getChatController().sendDocuments();
		}

		//String res  = this.networkModel.handleReceive(message);	
		//this.ctrl.getChatController().onMessageReceived(res);
	}

	public byte[] IntToByte(int value) {
		return this.networkModel.IntToByteArray(value);
	}
	public byte[] concatenateByteArrays(byte[] array1, byte[] array2) {
		return this.networkModel.concatenateByteArrays(array1, array2);
	}
	
}
