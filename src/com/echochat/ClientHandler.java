package com.echochat;
import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler implements Runnable {
    private static Set<ClientHandler> clients = new HashSet<>();
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public String getUsername() {
        return username;
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Read username from client
            username = in.readLine(); 
            System.out.println("User connected: " + username); // Debugging line
            clients.add(this);

            // Notify all clients about the updated user list
            broadcastUserList(); 

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received message: " + message); // Debugging output

                if (message.startsWith("MSG:")) {
                    String[] parts = message.split(":", 4); // Limit split to 4 parts

                    // Check if message has enough parts
                    if (parts.length < 4) { 
                        System.out.println("⚠️ Invalid message format received: " + message);
                        continue; // Skip processing this message
                    }

                    String recipient = parts[1].trim();
                    String sender = parts[2].trim();
                    String msgContent = parts[3].trim();

                    System.out.println("📩 Message from " + sender + " to " + recipient + ": " + msgContent);
                    sendPrivateMessage(recipient, sender + ": " + msgContent);
                }
            }


        } catch (IOException e) {
            System.out.println(username + " disconnected.");
        } finally {
            clients.remove(this);
            broadcastUserList();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Broadcasts the updated user list to all connected clients
    private static void broadcastUserList() {
        StringBuilder userList = new StringBuilder("USERLIST:");
        for (ClientHandler client : clients) {
            userList.append(client.getUsername()).append(",");
        }

        String finalUserList = userList.toString();
        for (ClientHandler client : clients) {
            client.sendMessage(finalUserList);
        }
    }

    // Sends a private message to a specific user
    private void sendPrivateMessage(String recipient, String message) {
    boolean sent = false;
    for (ClientHandler client : clients) {
        if (client.getUsername() != null && client.getUsername().equals(recipient)) {
            System.out.println("Sending message to: " + recipient);
            client.sendMessage(message);
            sent = true;
            break;
        }
    }
    if (!sent) {
        System.out.println("Failed to send message. Recipient not found: " + recipient);
    }
}

}