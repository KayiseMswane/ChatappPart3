/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */
package com.mycompany.chatapppart3;

import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.util.*;

public class ChatAppNGTest {
    List<Message> sentMessages, storedMessages;
    List<String> messageHashes, messageIDs;

    @BeforeMethod
    public void setUp() {
        // Populate arrays with test data:
        sentMessages = new ArrayList<>();
        storedMessages = new ArrayList<>();
        messageHashes = new ArrayList<>();
        messageIDs = new ArrayList<>();
        
        // Message 1: Sent
        Message m1 = new Message("+27834557896", "Did you get the cake?", 0);
        sentMessages.add(m1);
        messageHashes.add(m1.createMessageHash());
        messageIDs.add(m1.getMessageID());

        // Message 2: Stored
        Message m2 = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.", 1);
        storedMessages.add(m2);

        // Message 3: Disregarded
        Message m3 = new Message("+27834484567", "Yohoooo, I am at your gate.", 2);
        // not added to sent or stored

        // Message 4: Sent
        Message m4 = new Message("0838884567", "It is dinner time !", 3);
        sentMessages.add(m4);
        messageHashes.add(m4.createMessageHash());
        messageIDs.add(m4.getMessageID());

        // Message 5: Stored
        Message m5 = new Message("+27838884567", "Ok, I am leaving without you.", 4);
        storedMessages.add(m5);
    }

    @Test
    public void testSentMessagesArrayContents() {
        List<String> actualMessages = new ArrayList<>();
        for (Message m : sentMessages) {
            actualMessages.add(m.getMessageBody());
        }
        assertEquals(actualMessages, Arrays.asList("Did you get the cake?", "It is dinner time !"));
    }

   @Test
public void testLongestMessageBody() {
    Message longest = null;
    int maxLen = 0;
    for (Message m : sentMessages) {
        if (m.getMessageBody().length() > maxLen) {
            maxLen = m.getMessageBody().length();
            longest = m;
        }
    }
    assertEquals(longest.getMessageBody(), "Did you get the cake?");
}

    @Test
    public void testSearchMessageID() {
        String searchID = sentMessages.get(1).getMessageID();
        Message actual = null;
        for (Message m : sentMessages) {
            if (m.getMessageID().equals(searchID)) {
                actual = m; break;
            }
        }
        assertNotNull(actual);
        assertEquals(actual.getRecipient(), "0838884567");
        assertEquals(actual.getMessageBody(), "It is dinner time !");
    }

    @Test
    public void testSearchMessagesForRecipient() {
        String rec = "+27838884567";
        List<String> results = new ArrayList<>();
        for (Message m : storedMessages) if (m.getRecipient().equals(rec)) results.add(m.getMessageBody());
        assertEquals(results, Arrays.asList(
                "Where are you? You are late! I have asked you to be on time.",
                "Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteMessageByHash() {
        String hashToDelete = messageHashes.get(0);
        Iterator<Message> iter = sentMessages.iterator();
        boolean deleted = false;
        while (iter.hasNext()) {
            Message m = iter.next();
            if (m.createMessageHash().equals(hashToDelete)) {
                iter.remove();
                deleted = true;
                break;
            }
        }
        assertTrue(deleted);
        List<String> remainingMessages = new ArrayList<>();
        for (Message m : sentMessages) remainingMessages.add(m.getMessageBody());
        assertEquals(remainingMessages, Collections.singletonList("It is dinner time !"));
    }

    @Test
    public void testSentMessagesReport() {
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append(m.createMessageHash()).append("\n");
            sb.append(m.getRecipient()).append("\n");
            sb.append(m.getMessageBody()).append("\n");
        }
        String report = sb.toString();
        assertTrue(report.contains("It is dinner time !"));
        assertTrue(report.contains("0838884567"));
    }
}
