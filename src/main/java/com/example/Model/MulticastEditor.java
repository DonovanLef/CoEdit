package com.example.Model;

import java.net.*;
import java.io.*;
import java.util.function.Consumer;

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
    public void sendLine(byte[] line) {
        
            try {
                DatagramPacket packet = new DatagramPacket(line, line.length, group, PORT);
                socket.send(packet);
            } catch (Exception e) {
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

    // Envoi du texte complet (à appeler lors de la sauvegarde)
    public void sendText(String text) {
        sendMessage("TEXT_UPDATE:" + text);
    }
    
}
