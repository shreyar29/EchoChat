package com.echochat;
import java.io.*;
import java.net.*;

public class FileTransfer {
    private static final int PORT = 12346;

    public static void sendFile(String filePath, String recipientIP) {
        try (Socket socket = new Socket(recipientIP, PORT);
             FileInputStream fileInputStream = new FileInputStream(filePath);
             OutputStream out = socket.getOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            System.out.println("File sent successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void receiveFile(String savePath) {
        try (ServerSocket serverSocket = new ServerSocket(PORT);
             Socket socket = serverSocket.accept();
             FileOutputStream fileOutputStream = new FileOutputStream(savePath);
             InputStream in = socket.getInputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            System.out.println("File received successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
