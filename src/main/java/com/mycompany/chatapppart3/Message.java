/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapppart3;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;

public class Message {
    private String messageID;
    private String recipient;
    private String messageBody;
    private int messageNumber;

    public Message(String recipient, String messageBody, int messageNumber) {
        this.messageID = generateMessageID();
        this.recipient = recipient;
        this.messageBody = messageBody;
        this.messageNumber = messageNumber;
    }

    private String generateMessageID() {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) sb.append(rnd.nextInt(10));
        return sb.toString();
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    public boolean checkRecipientCell() {
        String regex = "^\\+27\\d{9}$";
        return recipient != null && Pattern.matches(regex, recipient);
    }

    public String validateMessageLength() {
        int len = (messageBody == null) ? 0 : messageBody.length();
        if (len <= 250) return "Message ready to send.";
        int over = len - 250;
        return "Message exceeds 250 characters by " + over + ", please reduce size.";
    }

    public String createMessageHash() {
        String firstTwo = (messageID.length() >= 2) ? messageID.substring(0, 2) : String.format("%02d", Integer.parseInt(messageID));
        String firstLast = getFirstAndLastWordsUpper();
        return firstTwo + ":" + messageNumber + ":" + firstLast;
    }

    private String getFirstAndLastWordsUpper() {
        if (messageBody == null || messageBody.trim().isEmpty()) return "";
        String[] words = messageBody.trim().split("\\s+");
        String first = words[0].replaceAll("^[^a-zA-Z0-9]+|[^a-zA-Z0-9]+$", "");
        String last = words[words.length - 1].replaceAll("^[^a-zA-Z0-9]+|[^a-zA-Z0-9]+$", "");
        return (first + last).toUpperCase();
    }

    // Use JOptionPane for choices; returns result string
    public String sentMessage() {
        String[] options = {"Send Message", "Disregard Message", "Store Message to send later"};
        int sel = JOptionPane.showOptionDialog(null,
                "Choose an action for this message:",
                "Message Action",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (sel == 0) {
            String display = "MessageID: " + messageID + "\n"
                    + "Message Hash: " + createMessageHash() + "\n"
                    + "Recipient: " + recipient + "\n"
                    + "Message: " + messageBody;
            JOptionPane.showMessageDialog(null, display, "Message Sent", JOptionPane.INFORMATION_MESSAGE);
            return "Message successfully sent.";
        } else if (sel == 1) {
            JOptionPane.showMessageDialog(null, "Message disregarded.\nPress 0 to delete message.", "Disregard", JOptionPane.INFORMATION_MESSAGE);
            return "Press 0 to delete message.";
        } else if (sel == 2) {
            boolean stored = storeMessage();
            if (stored) {
                JOptionPane.showMessageDialog(null, "Message successfully stored.", "Stored", JOptionPane.INFORMATION_MESSAGE);
                return "Message successfully stored.";
            } else {
                JOptionPane.showMessageDialog(null, "Failed to store message.", "Error", JOptionPane.ERROR_MESSAGE);
                return "Failed to store message.";
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid choice.", "Error", JOptionPane.ERROR_MESSAGE);
            return "Invalid choice.";
        }
    }

    public String printMessage() {
        return "MessageID: " + messageID +
                ", Message Hash: " + createMessageHash() +
                ", Recipient: " + recipient +
                ", Message: " + messageBody;
    }

    public int returnTotalMessages() {
        return messageNumber + 1;
    }

    // Store message JSON in data/messages.json
    public boolean storeMessage() {
        try {
            File dataDir = new File(System.getProperty("user.dir"), "data");
            if (!dataDir.exists()) dataDir.mkdirs();

            File file = new File(dataDir, "messages.json");
            JSONArray arr = new JSONArray();

            if (file.exists()) {
                String content = new String(Files.readAllBytes(file.toPath()));
                if (!content.trim().isEmpty()) {
                    try {
                        arr = new JSONArray(content);
                    } catch (org.json.JSONException je) {
                        arr = new JSONArray();
                    }
                }
            }

            JSONObject obj = new JSONObject();
            obj.put("messageID", messageID);
            obj.put("messageHash", createMessageHash());
            obj.put("recipient", recipient);
            obj.put("message", messageBody);
            obj.put("messageNumber", messageNumber);

            arr.put(obj);

            try (FileWriter fw = new FileWriter(file, false)) {
                fw.write(arr.toString(4));
            }
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }

    // getters
    public String getMessageID() { return messageID; }
    public String getRecipient() { return recipient; }
    public String getMessageBody() { return messageBody; }
    public int getMessageNumber() { return messageNumber; }
}