package com.felix.swingclient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.util.Scanner;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginFrame() {
        setTitle("IT Support Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 10)); // Adjusted to 4 rows to accommodate the register button

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        // Add components to the frame
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(registerButton);

        // Add action listeners
        loginButton.addActionListener(new LoginAction());
        registerButton.addActionListener(e -> {
            new RegisterFrame().setVisible(true); // Open the RegisterFrame
            dispose(); // Close the LoginFrame
        });

        setLocationRelativeTo(null); // Center the window
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            String token = authenticate(username, password);
            if (token != null) {
                JOptionPane.showMessageDialog(LoginFrame.this, "Login Successful!");
                dispose(); // Close the LoginFrame
                new DashboardFrame(token).setVisible(true); // Open the Dashboard
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String authenticate(String username, String password) {
        try {
            URL url = new URL("http://localhost:9090/api/auth/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setDoOutput(true);

            String jsonInput = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);

            System.out.println(jsonInput);
            System.out.println("Username " + username);
            System.out.println("Password " + password);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes());
                os.flush();
            }

            System.out.println("Response Code: " + conn.getResponseCode());

            if (conn.getResponseCode() == 200) {
                Scanner scanner = new Scanner(conn.getInputStream()).useDelimiter("\\A");
                String response = scanner.hasNext() ? scanner.next() : "";
                scanner.close();

                System.out.println("Server Response: " + response);
                return response.replace("{\"token\":\"", "").replace("\"}", "");
            } else {
                // Check if error stream is available
                InputStream errorStream = conn.getErrorStream();
                if (errorStream != null) {
                    Scanner scanner = new Scanner(errorStream).useDelimiter("\\A");
                    String errorResponse = scanner.hasNext() ? scanner.next() : "";
                    scanner.close();
                    System.out.println("Error Response: " + errorResponse);
                } else {
                    System.out.println("Error stream is null.");
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


}