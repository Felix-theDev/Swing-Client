package com.felix.swingclient;

import javax.swing.*;
import java.awt.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;

public class CreateTicketFrame extends JFrame {
    private JTextField titleField;
    private JTextArea descriptionField;
    private JComboBox<String> priorityBox, categoryBox;
    private JButton submitButton;
    private String authToken;

    public CreateTicketFrame(String authToken) {
        this.authToken = authToken;

        setTitle("Create Ticket");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField();

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionField = new JTextArea(3, 20);

        JLabel priorityLabel = new JLabel("Priority:");
        priorityBox = new JComboBox<>(new String[]{"LOW", "MEDIUM", "HIGH"});

        JLabel categoryLabel = new JLabel("Category:");
        categoryBox = new JComboBox<>(new String[]{"NETWORK", "HARDWARE", "SOFTWARE", "OTHER"});

        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> submitTicket());

        add(titleLabel);
        add(titleField);
        add(descriptionLabel);
        add(descriptionField);
        add(priorityLabel);
        add(priorityBox);
        add(categoryLabel);
        add(categoryBox);
        add(new JLabel());
        add(submitButton);

        setLocationRelativeTo(null);
    }

    private void submitTicket() {
        try {
            URL url = new URL("http://localhost:9090/api/tickets");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + authToken);
            conn.setDoOutput(true);

            String jsonInput = String.format(
                    "{\"title\":\"%s\",\"description\":\"%s\",\"priority\":\"%s\",\"category\":\"%s\"}",
                    titleField.getText(), descriptionField.getText(),
                    priorityBox.getSelectedItem(), categoryBox.getSelectedItem()
            );

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes());
                os.flush();
            }

            if (conn.getResponseCode() == 201) {
                JOptionPane.showMessageDialog(this, "Ticket Created Successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error Creating Ticket", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to connect to API", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
