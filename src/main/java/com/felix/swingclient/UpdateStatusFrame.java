package com.felix.swingclient;

import javax.swing.*;
import java.awt.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;

public class UpdateStatusFrame extends JFrame {
    private String authToken;
    private Long ticketId;
    private JComboBox<String> statusBox;
    private JButton updateButton;

    public UpdateStatusFrame(String authToken, Long ticketId) {
        this.authToken = authToken;
        this.ticketId = ticketId;

        setTitle("Update Ticket Status");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(2, 2, 10, 10));

        JLabel statusLabel = new JLabel("New Status:");
        statusBox = new JComboBox<>(new String[]{"OPEN", "IN_PROGRESS", "RESOLVED", "CLOSED"});
        updateButton = new JButton("Update");

        add(statusLabel);
        add(statusBox);
        add(new JLabel());
        add(updateButton);

        updateButton.addActionListener(e -> updateStatus());

        setLocationRelativeTo(null);
    }

    private void updateStatus() {
        try {
            URL url = new URL("http://localhost:9090/api/tickets/" + ticketId + "/status");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + authToken);
            conn.setDoOutput(true);

            String jsonInput = String.format("{\"status\":\"%s\"}", statusBox.getSelectedItem());

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes());
                os.flush();
            }

            if (conn.getResponseCode() == 200) {
                JOptionPane.showMessageDialog(this, "Ticket status updated successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error updating status.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to connect to API.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
