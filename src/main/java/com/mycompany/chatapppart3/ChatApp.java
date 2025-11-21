/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.chatapppart3;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * QuickChat Main App - with Part 3 functionality!
 */
public class ChatApp {

    public static void main(String[] args) {
        // Registration using JOptionPane (one field per popup)
        Login user = null;
        while (true) {
            String firstName = JOptionPane.showInputDialog(null, "Enter your first name:", "Registration - First Name", JOptionPane.QUESTION_MESSAGE);
            if (firstName == null) return;

            String lastName = JOptionPane.showInputDialog(null, "Enter your last name:", "Registration - Last Name", JOptionPane.QUESTION_MESSAGE);
            if (lastName == null) return;

            String username = JOptionPane.showInputDialog(null, "Create a username (must contain '_' and be no more than 5 characters):", "Registration - Username", JOptionPane.QUESTION_MESSAGE);
            if (username == null) return;

            String password = JOptionPane.showInputDialog(null, "Create a password (min 8 chars, include uppercase, digit, special char):", "Registration - Password", JOptionPane.QUESTION_MESSAGE);
            if (password == null) return;

            String cellphone = JOptionPane.showInputDialog(null, "Enter your South African cell phone number (format: +27XXXXXXXXX):", "Registration - Cellphone", JOptionPane.QUESTION_MESSAGE);
            if (cellphone == null) return;

            user = new Login(firstName.trim(), lastName.trim(), username.trim(), password, cellphone.trim());

            String regStatus = user.registerUser();
            JOptionPane.showMessageDialog(null, regStatus, "Registration Status", JOptionPane.INFORMATION_MESSAGE);

            if (regStatus.equals("User has been registered successfully!")) break;

            int retry = JOptionPane.showConfirmDialog(null, "Registration failed. Try again?", "Retry Registration", JOptionPane.YES_NO_OPTION);
            if (retry != JOptionPane.YES_OPTION) return;
        }

        // Login using JOptionPane
        while (true) {
            String loginUsername = JOptionPane.showInputDialog(null, "Enter username:", "Login - Username", JOptionPane.QUESTION_MESSAGE);
            if (loginUsername == null) return;

            String loginPassword = JOptionPane.showInputDialog(null, "Enter password:", "Login - Password", JOptionPane.QUESTION_MESSAGE);
            if (loginPassword == null) return;

            String loginStatus = user.returnLoginStatus(loginUsername.trim(), loginPassword);
            JOptionPane.showMessageDialog(null, loginStatus, "Login Status", JOptionPane.INFORMATION_MESSAGE);

            if (loginStatus.startsWith("Welcome")) break;

            int retry = JOptionPane.showConfirmDialog(null, "Login failed. Try again?", "Retry Login", JOptionPane.YES_NO_OPTION);
            if (retry != JOptionPane.YES_OPTION) return;
        }

        // QuickChat welcome
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat.", "Welcome", JOptionPane.INFORMATION_MESSAGE);

        // Ask how many messages they want to enter
        int numMessagesAllowed = 0;
        while (true) {
            String input = JOptionPane.showInputDialog(null, "How many messages would you like to enter?", "Message Count", JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                numMessagesAllowed = 1;
                break;
            }
            try {
                numMessagesAllowed = Integer.parseInt(input.trim());
                if (numMessagesAllowed <= 0) {
                    JOptionPane.showMessageDialog(null, "Please enter a positive integer.", "Invalid", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                break;
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Invalid", JOptionPane.ERROR_MESSAGE);
            }
        }

        // PART 3 ARRAYS
        List<Message> sentMessages = new ArrayList<>();
        List<Message> storedMessages = new ArrayList<>();
        List<Message> disregardedMessages = new ArrayList<>();
        List<String> messageHashes = new ArrayList<>();
        List<String> messageIDs = new ArrayList<>();
        int totalMessagesSent = 0;

        // Literal for-loop to handle the allowed number of messages
        for (int i = 0; i < numMessagesAllowed; ) {
            String menu = "QuickChat Menu (message " + (i + 1) + " of " + numMessagesAllowed + ")\n"
                    + "1) Send Message\n"
                    + "2) Show recently sent messages (last 5)\n"
                    + "3) Show stored messages\n"
                    + "4) Show Expanded Message Features\n"
                    + "5) Quit\n\n"
                    + "Enter option (1-5):";

            String selection = JOptionPane.showInputDialog(null, menu, "QuickChat Menu", JOptionPane.QUESTION_MESSAGE);
            if (selection == null) {
                int confirm = JOptionPane.showConfirmDialog(null, "Do you want to quit entirely?", "Confirm Quit", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) break;
                else continue;
            }
            selection = selection.trim();

            switch (selection) {
                case "1": {
                    String recipient = JOptionPane.showInputDialog(null, "Enter recipient cellphone (format +27XXXXXXXXX):", "Recipient", JOptionPane.QUESTION_MESSAGE);
                    if (recipient == null) {
                        JOptionPane.showMessageDialog(null, "Recipient input cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                        continue;
                    }

                    String body = JOptionPane.showInputDialog(null, "Enter message (max 250 characters):", "Message", JOptionPane.QUESTION_MESSAGE);
                    if (body == null) {
                        JOptionPane.showMessageDialog(null, "Message input cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                        continue;
                    }

                    Message msg = new Message(recipient.trim(), body, i);

                    if (!msg.checkRecipientCell()) {
                        JOptionPane.showMessageDialog(null, "Cell phone number is incorrectly formatted.", "Invalid recipient", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    String validation = msg.validateMessageLength();
                    if (!validation.equals("Message ready to send.")) {
                        JOptionPane.showMessageDialog(null, validation, "Invalid message", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    String action = msg.sentMessage();

                    if (action.equals("Message successfully sent.")) {
                        sentMessages.add(msg);
                        totalMessagesSent++;
                    } else if (action.equals("Message successfully stored.")) {
                        storedMessages.add(msg);
                    } else if (action.equals("Press 0 to delete message.")) {
                        disregardedMessages.add(msg);
                    } else {
                        continue;
                    }
                    // Add Part 3 arrays collection
                    messageHashes.add(msg.createMessageHash());
                    messageIDs.add(msg.getMessageID());
                    i++;
                    break;
                }
                case "2":
                    if (sentMessages.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No recently sent messages.", "Recently Sent", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        int start = Math.max(0, sentMessages.size() - 5);
                        for (int j = start; j < sentMessages.size(); j++) {
                            Message m = sentMessages.get(j);
                            sb.append("Message ID: ").append(m.getMessageID()).append("\n");
                            sb.append("Hash: ").append(m.createMessageHash()).append("\n");
                            sb.append("Recipient: ").append(m.getRecipient()).append("\n");
                            sb.append("Message: ").append(m.getMessageBody()).append("\n");
                            sb.append("-------------------------\n");
                        }
                        JOptionPane.showMessageDialog(null, sb.toString(), "Recently Sent (last 5)", JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;
                case "3": {
                    List<Message> loadedStoredMessages = loadStoredMessages();
                    if (loadedStoredMessages.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No stored messages found.", "Stored Messages", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (Message m : loadedStoredMessages) {
                            sb.append("Message ID: ").append(m.getMessageID()).append("\n");
                            sb.append("Message Hash: ").append(m.createMessageHash()).append("\n");
                            sb.append("Recipient: ").append(m.getRecipient()).append("\n");
                            sb.append("Message: ").append(m.getMessageBody()).append("\n");
                            sb.append("-------------------------\n");
                        }
                        JOptionPane.showMessageDialog(null, sb.toString(), "Stored Messages", JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;
                }
                case "4":
                    launchExpandedMessageFeatures(sentMessages, storedMessages, disregardedMessages, messageHashes, messageIDs);
                    break;
                case "5":
                    int confirmQuit = JOptionPane.showConfirmDialog(null, "Do you want to quit and show summary now?", "Confirm Quit", JOptionPane.YES_NO_OPTION);
                    if (confirmQuit == JOptionPane.YES_OPTION) i = numMessagesAllowed;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid option. Please enter 1-5.", "Invalid", JOptionPane.ERROR_MESSAGE);
            }
        }
        JOptionPane.showMessageDialog(null, "Total number of messages successfully sent: " + totalMessagesSent, "Summary", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    // Load messages from data/messages.json into array for Part 3 features (storedMessages)
    private static List<Message> loadStoredMessages() {
        List<Message> stored = new ArrayList<>();
        File file = new File(System.getProperty("user.dir"), "data/messages.json");
        if (!file.exists() || file.length() == 0) return stored;
        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            JSONArray arr = new JSONArray(content);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                Message m = new Message(
                        o.optString("recipient", ""),
                        o.optString("message", ""),
                        o.optInt("messageNumber", 0)
                );
                stored.add(m);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stored;
    }

    private static void launchExpandedMessageFeatures(
            List<Message> sentMessages,
            List<Message> storedMessages,
            List<Message> disregardedMessages,
            List<String> messageHashes,
            List<String> messageIDs
    ) {
        String menu =
                "Expanded Message Features Menu\n"
                + "a) Display sender/recipient of all sent messages\n"
                + "b) Display longest sent message\n"
                + "c) Search for Message ID\n"
                + "d) Search for all messages regarding a recipient\n"
                + "e) Delete message by Message Hash\n"
                + "f) Display full sent messages report\n"
                + "g) Back\n";
        while (true) {
            String sel = JOptionPane.showInputDialog(null, menu, "Expanded Features", JOptionPane.QUESTION_MESSAGE);
            if (sel == null || sel.trim().equalsIgnoreCase("g")) break;
            switch (sel.trim().toLowerCase()) {
                case "a": {
                    if (sentMessages.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No sent messages.", "Display Sender/Recipient", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (Message m : sentMessages) {
                            sb.append("Sender: YOU\nRecipient: " + m.getRecipient() + "\nMessage: " + m.getMessageBody() + "\n---\n");
                        }
                        JOptionPane.showMessageDialog(null, sb.toString(), "Sender & Recipient", JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;
                }
                case "b": {
                    Message longest = null;
                    int maxLen = 0;
                    for (Message m : sentMessages) {
                        if (m.getMessageBody().length() > maxLen) {
                            maxLen = m.getMessageBody().length();
                            longest = m;
                        }
                    }
                    if (longest != null)
                        JOptionPane.showMessageDialog(null, "Longest message: " + longest.getMessageBody(), "Longest Message", JOptionPane.INFORMATION_MESSAGE);
                    else
                        JOptionPane.showMessageDialog(null, "No sent messages.", "Longest Message", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
                case "c": {
                    String inputID = JOptionPane.showInputDialog(null, "Enter Message ID to search:", "Search Message ID", JOptionPane.QUESTION_MESSAGE);
                    if (inputID == null) continue;
                    boolean found = false;
                    for (Message m : sentMessages) {
                        if (m.getMessageID().equals(inputID)) {
                            JOptionPane.showMessageDialog(null, "Recipient: " + m.getRecipient() + "\nMessage: " + m.getMessageBody(), "Found by MessageID", JOptionPane.INFORMATION_MESSAGE);
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                        JOptionPane.showMessageDialog(null, "Message ID not found in sent messages.", "Not Found", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                case "d": {
                    String rec = JOptionPane.showInputDialog(null, "Enter recipient to search all messages for:", "Search by Recipient", JOptionPane.QUESTION_MESSAGE);
                    if (rec == null) continue;
                    StringBuilder sb = new StringBuilder();
                    for (Message m : sentMessages) {
                        if (m.getRecipient().equals(rec)) {
                            sb.append(m.getMessageBody()).append("\n");
                        }
                    }
                    for (Message m : storedMessages) {
                        if (m.getRecipient().equals(rec)) {
                            sb.append(m.getMessageBody()).append("\n");
                        }
                    }
                    if (sb.length() > 0)
                        JOptionPane.showMessageDialog(null, sb.toString(), "Messages for recipient " + rec, JOptionPane.INFORMATION_MESSAGE);
                    else
                        JOptionPane.showMessageDialog(null, "No messages for the recipient.", "No Match", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
                case "e": {
                    String hash = JOptionPane.showInputDialog(null, "Enter message hash to delete:", "Delete by Hash", JOptionPane.QUESTION_MESSAGE);
                    if (hash == null) continue;
                    boolean deleted = false;
                    Iterator<Message> iter = sentMessages.iterator();
                    while (iter.hasNext()) {
                        Message m = iter.next();
                        if (m.createMessageHash().equals(hash)) {
                            iter.remove();
                            messageHashes.remove(hash);
                            JOptionPane.showMessageDialog(null, "Message \"" + m.getMessageBody() + "\" successfully deleted.", "Delete", JOptionPane.INFORMATION_MESSAGE);
                            deleted = true;
                            break;
                        }
                    }
                    if (!deleted)
                        JOptionPane.showMessageDialog(null, "Message hash not found in sent messages.", "Delete", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                case "f": {
                    StringBuilder sb = new StringBuilder();
                    sb.append("==== Sent Messages Report ====\n");
                    for (Message m : sentMessages) {
                        sb.append("Message Hash: ").append(m.createMessageHash()).append("\n");
                        sb.append("Recipient: ").append(m.getRecipient()).append("\n");
                        sb.append("Message: ").append(m.getMessageBody()).append("\n");
                        sb.append("----------------------------\n");
                    }
                    if (sentMessages.isEmpty())
                        sb.append("No sent messages found.\n");
                    JOptionPane.showMessageDialog(null, sb.toString(), "Sent Messages Report", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
                default:
                    JOptionPane.showMessageDialog(null, "Invalid option. Please choose a-f.", "Invalid", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}