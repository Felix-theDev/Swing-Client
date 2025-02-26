Swing Client for IT Support Ticket System

📌 Overview

This is the Swing Client for the IT Support Ticket System. It provides a graphical user interface for employees to create and track IT tickets and for IT support staff to manage them.

🛠️ Requirements

Java 17 or later

Backend API (Spring Boot application) running and accessible

Network Connectivity to communicate with the backend

🚀 Installation & Usage

1️⃣ Build & Package (If Needed)

If you need to build the client from source, navigate to the root directory and run:

mvn clean package

The JAR file will be generated in the target/ directory.

2️⃣ Running the Swing Client

To start the application, navigate to the project root and run:

java -jar target/swing-client-1.0-SNAPSHOT.jar

This will launch the Swing-based UI for the IT Support Ticket System.

📝 Features

Ticket Creation: Employees can create IT support tickets.

Status Tracking: View ticket status and updates.

Role-Based Access: Employees manage their own tickets; IT support handles all tickets.

Audit Log: Tracks status changes and comments.
