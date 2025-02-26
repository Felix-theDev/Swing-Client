package com.felix.swingclient;

import javax.swing.*;
import java.awt.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;

public class AddCommentFrame extends JFrame {
    private String authToken;
    private Long ticketId;
    private JTextArea commentField;
    private JButton submitButton;

    public AddCommentFrame(String authToken, Long ticketId) {
        this.authToken = authToken;
        this.ticketId = ticketId;

        setTitle("Add Comment");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        commentField = new JTextArea(5, 30);
        submitButton = new JButton("Submit");

        add(new JScrollPane(commentField), BorderLayout.CENTER);
        add(submitButton, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> submitComment());

        setLocationRelativeTo(null);
    }

    private void submitComment() {
        try {
            URL url = new URL("http://localhost:9090/api/tickets/" + ticketId + "/comments");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + authToken);
            conn.setDoOutput(true);

            String jsonInput = String.format("{\"comment\":\"%s\"}", commentField.getText());

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes());
                os.flush();
            }

            if (conn.getResponseCode() == 200) {
                JOptionPane.showMessageDialog(this, "Comment added successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding comment.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to connect to API.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
