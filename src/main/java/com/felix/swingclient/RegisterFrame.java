package com.felix.swingclient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;

public class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox; // Dropdown for role selection
    private JButton registerButton;
    private JButton loginButton; // Button to switch to the Login Page

    public RegisterFrame() {
        setTitle("Register New User");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10)); // Adjusted to 5 rows to accommodate the login button

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel roleLabel = new JLabel("Role:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        roleBox = new JComboBox<>(new String[]{"EMPLOYEE", "IT_SUPPORT"}); // User Role Selection
        registerButton = new JButton("Register");
        loginButton = new JButton("Login"); // Button to switch to the Login Page

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(roleLabel);
        add(roleBox);
        add(registerButton);
        add(loginButton);

        // Action listener for the Register button
        registerButton.addActionListener(new RegisterAction());

        // Action listener for the Login button
        loginButton.addActionListener(e -> {
            new LoginFrame().setVisible(true); // Open the LoginFrame
            dispose(); // Close the RegisterFrame
        });

        setLocationRelativeTo(null); // Center the window
    }

    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleBox.getSelectedItem();

            if (registerUser(username, password, role)) {
                JOptionPane.showMessageDialog(RegisterFrame.this, "Registration Successful! You can now log in.");
                dispose(); // Close registration window
                new LoginFrame().setVisible(true); // Open the LoginFrame after successful registration
            } else {
                JOptionPane.showMessageDialog(RegisterFrame.this, "Error during registration", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean registerUser(String username, String password, String role) {
        try {
            URL url = new URL("http://localhost:9090/api/auth/register");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInput = String.format(
                    "{\"username\":\"%s\", \"password\":\"%s\", \"role\":\"%s\"}",
                    username, password, role
            );

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes());
                os.flush();
            }

            return conn.getResponseCode() == 200 || conn.getResponseCode() == 201;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}