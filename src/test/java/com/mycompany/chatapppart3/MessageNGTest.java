/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */
package com.mycompany.chatapppart3;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class MessageNGTest {

    @Test
    public void testMessageLengthSuccess() {
        Message m = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight", 0);
        assertEquals(m.validateMessageLength(), "Message ready to send.");
    }

    @Test
    public void testMessageLengthFailure() {
        StringBuilder longMsg = new StringBuilder();
        for (int i = 0; i < 260; i++) longMsg.append("a");
        Message m = new Message("+27718693002", longMsg.toString(), 0);
        String result = m.validateMessageLength();
        assertTrue(result.startsWith("Message exceeds 250 characters by"));
    }

    @Test
    public void testRecipientCorrectFormat() {
        Message m = new Message("+27718693002", "Hello", 0);
        assertTrue(m.checkRecipientCell());
    }

    @Test
    public void testRecipientIncorrectFormat() {
        Message m = new Message("08575975889", "Hello", 0);
        assertFalse(m.checkRecipientCell());
    }

    @Test
    public void testMessageHashForTestCase1() {
        Message m = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight", 0);
        String hash = m.createMessageHash();
        assertTrue(hash.contains(":0:"));
        assertTrue(hash.endsWith("HITONIGHT"));
    }

    @Test
    public void testMessageIDGenerated() {
        Message m = new Message("+27718693002", "Hi", 1);
        String id = m.getMessageID();
        assertNotNull(id);
        assertEquals(id.length(), 10);
    }

    @Test
    public void testStoreMessageCreatesFile() throws Exception {
        File dataDir = new File(System.getProperty("user.dir"), "data");
        File f = new File(dataDir, "messages.json");
        if (f.exists()) f.delete();

        Message m = new Message("+27718693002", "Test store", 0);
        boolean stored = m.storeMessage();
        assertTrue(stored, "Message should be stored successfully.");
        assertTrue(f.exists(), "data/messages.json file should exist.");
        List<String> lines = Files.readAllLines(f.toPath());
        String content = String.join("\n", lines);
        assertTrue(content.contains("Test store"), "Stored JSON should contain the message text.");
    }
}