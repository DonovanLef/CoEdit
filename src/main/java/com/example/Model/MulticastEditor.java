package com.example.Model;

import java.net.*;
import java.io.*;
import java.util.function.Consumer;

import com.example.Controller.Controller;

public class MulticastEditor {
    private static final String MULTICAST_ADDRESS = "224.0.0.1";
    private static final int PORT = 4446;

    private MulticastSocket socket;
    private InetAddress group;
    private Consumer<byte[]> messageListener; // Callback pour transmettre les messages à la vue

    public MulticastEditor(Consumer<byte[]> messageListener) throws IOException {
        this.messageListener = messageListener;

        // Initialisation du socket multicast
        socket = new MulticastSocket(PORT);
        group = InetAddress.getByName(MULTICAST_ADDRESS);
        socket.joinGroup(group);

        // Démarrage du thread d'écoute
        new Thread(this::listen).start();
        
    }

    // Écoute des messages en multicast
    private void listen() {
        byte[] buffer = new byte[102400];
        while (true) {
            try {
                
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                // Utiliser le callback pour transmettre le message
                if (messageListener != null) {
                    messageListener.accept(packet.getData());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Envoi d'un message
    public void sendMessage(String message) {
        try {
            byte[] msg = message.getBytes();
            DatagramPacket packet = new DatagramPacket(msg, msg.length, group, PORT);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendData(byte[] data) {
        try {
            DatagramPacket packet = new DatagramPacket(data, data.length, group, PORT);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendData(short code, byte[] data) {
        byte[] result = new byte[2];
		result[0] = (byte) ((code & 0xFF000000) >> 8);
		result[1] = (byte) ((code & 0x00FF0000) >> 0);
        byte[] combined = new byte[result.length + data.length];
        System.arraycopy(result, 0, combined, 0, result.length);
        System.arraycopy(data, 0, combined, result.length, data.length);
        try {
            DatagramPacket packet = new DatagramPacket(combined, combined.length, group, PORT);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Envoi du texte complet (à appeler lors de la sauvegarde)
    public void sendText(String text) {
        sendMessage("TEXT_UPDATE:" + text);
    }

	public void sendLine(LineModel lineModel, Controller ctrl) {
        try {

            byte[] v = ctrl.getNetworkController().IntToByte((short)100);
            byte[] l = lineModel.toByteArray();
            byte[] data=  ctrl.getNetworkController().concatenateByteArrays(v, l);
            //two array in one
            sendData(data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
	}
    
}
