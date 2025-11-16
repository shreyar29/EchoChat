package com.echochat;
import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;
    private static String username;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println(in.readLine()); // "Enter your username:"
            username = userInput.readLine();
            out.println(username);

            // Thread to listen for messages from the server
            new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            }).start();

            // Sending messages
            while (true) {
                System.out.println("Enter recipient's name: ");
                String recipient = userInput.readLine(); // Get recipient's username
                System.out.println("Enter message: ");
                String message = userInput.readLine(); // Get message content

                // Correctly formatted message
                String formattedMessage = "MSG:" + recipient + ":" + username + ":" + message;
                out.println(formattedMessage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
