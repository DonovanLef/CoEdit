package com.example.Model;

import java.util.Arrays;

import com.example.Controller.Controller;

public class NetworkModel {

	public int getCode(byte[] bytes) {
		int value = 0;
		// Assurez-vous qu'il y a au moins 2 octets dans le tableau
		if (bytes.length < 2) {
			throw new IllegalArgumentException("Le tableau doit contenir au moins 2 octets.");
		}
		// Récupérer les 2 premiers octets (16 bits)
		value = (bytes[0] & 0xFF) << 8;  // Premier octet (bits de poids fort)
		value |= (bytes[1] & 0xFF);      // Deuxième octet (bits de poids faible)
		return value;
	}



	// Cette méthode est utilisée pour la modification de lignes
	public LineModel handle100(byte[] bytes) {
		return LineModel.restoreByBytes(bytes);

	}
	public Document handle200(byte[] bytes) {
		return Document.restoreByBytes(bytes);

	}
}
