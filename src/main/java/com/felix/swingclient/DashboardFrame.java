package com.felix.swingclient;

import javax.swing.*;
import java.awt.*;
import java.awt.GridLayout;


public class DashboardFrame extends JFrame {
    private String authToken;

    public DashboardFrame(String authToken) {
        this.authToken = authToken;

        setTitle("IT Support Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 10, 10));

        JButton viewTicketsButton = new JButton("View Tickets");
        JButton createTicketButton = new JButton("Create Ticket");
        JButton logoutButton = new JButton("Logout");

        viewTicketsButton.addActionListener(e -> new TicketListFrame(authToken).setVisible(true));
        createTicketButton.addActionListener(e -> new CreateTicketFrame(authToken).setVisible(true));
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        add(viewTicketsButton);
        add(createTicketButton);
        add(logoutButton);

        setLocationRelativeTo(null);
    }
}
