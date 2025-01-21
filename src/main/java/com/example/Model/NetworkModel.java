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
	public byte[] IntToByteArray( short data ) {    
		byte[] result = new byte[2];
		result[0] = (byte) ((data & 0x0000FF00) >> 8);
		result[1] = (byte) ((data & 0x000000FF) >> 0);
		return result;        
	}
	public  byte[] concatenateByteArrays(byte[] array1, byte[] array2) {
        int length1 = array1.length;
        int length2 = array2.length;

        // Create a new byte array to hold the concatenated result
        byte[] result = new byte[length1 + length2];

        // Copy elements from the first array
        System.arraycopy(array1, 0, result, 0, length1);

        // Copy elements from the second array
        System.arraycopy(array2, 0, result, length1, length2);

        return result;
    }
}
