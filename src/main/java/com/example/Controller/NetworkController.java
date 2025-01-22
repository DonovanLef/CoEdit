package com.example.Controller;

import java.util.Arrays;

import com.example.Model.Document;
import com.example.Model.Folder;
import com.example.Model.LineModel;
import com.example.Model.NetworkModel;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class NetworkController {
	
	private static Integer startReceived = null;

	private NetworkModel networkModel;
	public NetworkController() {
		this.networkModel = new NetworkModel();
	}

	public void handleReceive(byte[] bytes) {

		//Transformer les 16premiers byte en decimal
		int code = this.networkModel.getCode(bytes);
		byte[] serial = Arrays.copyOfRange(bytes, 2, bytes.length);

		System.out.println(code);
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
			// Skip si c'est nous qui avions envoyé la demande 202;
			Controller.ctrl.getChatController().sendDocuments();
		}

		// récéption d'un document, uniquement à la connexion
		if (code == 203) {
			StarterController starter = Controller.ctrl.getStarterController();

			// si quelqu'un se reco pendant que je recup
			if ( starter.lasttime < (System.currentTimeMillis() - 5000) ) {
				System.out.println("fini");
				return;
			}
			Document doc = Document.restoreByBytes(serial);


			if (doc == null) return;

			// ça c'est le cas où on l'a déjà reçu
			if ( starter.documentsReceived.containsKey(doc.getName())) return;

			// ça c'est le cas où on l'a déjà
			if ( !DocumentController.getDocumentsMap().containsKey(doc.getName()) ){
				Controller.ctrl.getFolderController().createDocument(doc);
			}
			starter.documentsReceived.put(doc.getName(), doc);
			starter.updateLastTime();
		}

	}

	public byte[] IntToByte(short value) {
		return this.networkModel.IntToByteArray(value);
	}
	public byte[] concatenateByteArrays(byte[] array1, byte[] array2) {
		return this.networkModel.concatenateByteArrays(array1, array2);
	}
	
}
