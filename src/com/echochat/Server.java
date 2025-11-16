package com.echochat;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private static Map<String, ClientHandler> clients = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Server started...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                username = in.readLine();
                synchronized (clients) {
                    clients.put(username, this);
                }

                broadcast("USERLIST:" + String.join(",", clients.keySet()) + ",");
                System.out.println(username + " has joined.");

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("MSG:")) {
                        String[] parts = message.split(":", 4);
                        if (parts.length == 4) {
                            String recipient = parts[1];
                            String sender = parts[2];
                            String msgContent = parts[3];

                            String formattedMessage = sender + ": " + msgContent;
                            sendMessage(recipient, formattedMessage);
                            out.println(formattedMessage);
                        } else {
                            out.println("Invalid message format.");
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(username + " disconnected.");
            } finally {
                synchronized (clients) {
                    clients.remove(username);
                    broadcast("USERLIST:" + String.join(",", clients.keySet()) + ",");
                }
            }
        }

        private void sendMessage(String recipient, String message) {
            ClientHandler recipientHandler = clients.get(recipient);
            if (recipientHandler != null) {
                recipientHandler.out.println(message);
            }
        }

        private void broadcast(String message) {
            for (ClientHandler client : clients.values()) {
                client.out.println(message);
            }
        }
    }
}
