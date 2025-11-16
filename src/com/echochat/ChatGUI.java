package com.echochat;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class ChatGUI {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;
    private String username;
    private PrintWriter out;
    private BufferedReader in;

    private JFrame frame;
    private JTextArea chatArea;
    private JTextField messageField;
    private JComboBox<String> userDropdown;
    private JButton sendButton;

    public ChatGUI(String username1, Socket socket) {
        frame = new JFrame("Chat Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(44, 62, 80)); // Dark background

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setBackground(new Color(52, 73, 94)); // Darker grey
        chatArea.setForeground(Color.WHITE);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        userDropdown = new JComboBox<>();
        userDropdown.setBackground(new Color(52, 152, 219)); // Blue
        userDropdown.setForeground(Color.WHITE);
        userDropdown.setFont(new Font("Arial", Font.BOLD, 14));

        messageField = new JTextField();
        messageField.setBackground(new Color(236, 240, 241)); // Light grey
        messageField.setFont(new Font("Arial", Font.PLAIN, 14));

        sendButton = new JButton("Send");
        sendButton.setBackground(new Color(41, 128, 185)); // Bright blue
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("Arial", Font.BOLD, 14));

        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(new Color(44, 62, 80));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(userDropdown, BorderLayout.WEST);
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        frame.add(chatScrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

        connectToServer();
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            username = JOptionPane.showInputDialog("Enter your username:");
            out.println(username);

            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        if (message.startsWith("USERLIST:")) {
                            updateUserList(message.substring(9));
                        } else {
                            chatArea.append(message + "\n");
                        }
                    }
                } catch (IOException e) {
                    chatArea.append("Connection lost.\n");
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Could not connect to the server.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUserList(String userList) {
        SwingUtilities.invokeLater(() -> {
            userDropdown.removeAllItems();
            for (String user : userList.split(",")) {
                if (!user.isEmpty() && !user.equals(username)) {
                    userDropdown.addItem(user);
                }
            }
        });
    }

    private void sendMessage() {
        String recipient = (String) userDropdown.getSelectedItem();
        String message = messageField.getText().trim();

        if (recipient != null && !message.isEmpty()) {
            String formattedMessage = "MSG:" + recipient + ":" + username + ":" + message;
            out.println(formattedMessage);
            messageField.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                String username = JOptionPane.showInputDialog("Enter your username:");
                if (username != null && !username.trim().isEmpty()) {
                    Socket socket = new Socket("localhost", 12345);
                    new ChatGUI(username, socket);
                } else {
                    JOptionPane.showMessageDialog(null, "Username cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Could not connect to server!", "Connection Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}
