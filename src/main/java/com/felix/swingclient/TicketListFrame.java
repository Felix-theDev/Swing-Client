package com.felix.swingclient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class TicketListFrame extends JFrame {
    private String authToken;
    private JTable ticketTable;
    private JButton updateStatusButton, addCommentButton;

    public TicketListFrame(String authToken) {
        this.authToken = authToken;

        setTitle("View Tickets");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table for displaying tickets
        ticketTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(ticketTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        updateStatusButton = new JButton("Update Status");
        addCommentButton = new JButton("Add Comment");

        buttonPanel.add(updateStatusButton);
        buttonPanel.add(addCommentButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event listeners
        updateStatusButton.addActionListener(e -> updateTicketStatus());
        addCommentButton.addActionListener(e -> addCommentToTicket());

        fetchTickets();
        setLocationRelativeTo(null);
    }

    private void fetchTickets() {
        try {
            URL url = new URL("http://localhost:9090/api/tickets");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + authToken);

            if (conn.getResponseCode() == 200) {
                Scanner scanner = new Scanner(conn.getInputStream()).useDelimiter("\\A");
                String response = scanner.hasNext() ? scanner.next() : "";
                scanner.close();

                JSONArray tickets = new JSONArray(response);
                String[] columnNames = {"ID", "Title", "Status", "Priority", "Category"};
                DefaultTableModel model = new DefaultTableModel(columnNames, 0);

                for (int i = 0; i < tickets.length(); i++) {
                    JSONObject ticket = tickets.getJSONObject(i);
                    model.addRow(new Object[]{
                            ticket.getLong("id"),
                            ticket.getString("title"),
                            ticket.getString("status"),
                            ticket.getString("priority"),
                            ticket.getString("category")
                    });
                }

                ticketTable.setModel(model);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to fetch tickets", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching tickets.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTicketStatus() {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a ticket to update.");
            return;
        }

        Long ticketId = (Long) ticketTable.getValueAt(selectedRow, 0);
        new UpdateStatusFrame(authToken, ticketId).setVisible(true);
    }

    private void addCommentToTicket() {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a ticket to add a comment.");
            return;
        }

        Long ticketId = (Long) ticketTable.getValueAt(selectedRow, 0);
        new AddCommentFrame(authToken, ticketId).setVisible(true);
    }
}
