/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */
package com.mycompany.chatapppart3;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class UserNGTest {

    @Test
    public void testCorrectUsername() {
        Login user = new Login("Kayise", "Mswane", "kaye_", "Kayise7575.", "+27815176875");
        assertTrue(user.checkUserName());
        assertEquals(user.registerUser(), "User has been registered successfully!");
    }

    @Test
    public void testIncorrectUsername() {
        Login user = new Login("Kayise", "Mswane", "kay!!!!!!!", "Kayise7575.", "+27815176875");
        assertFalse(user.checkUserName());
        assertEquals(user.registerUser(), "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.");
    }

    @Test
    public void testCorrectPassword() {
        Login user = new Login("Kayise", "Mswane", "kaye_", "Kayise7575.", "+27815176875");
        assertTrue(user.checkPasswordComplexity());
    }

    @Test
    public void testIncorrectPassword() {
        Login user = new Login("Kayise", "Mswane", "kaye_", "password", "+27815176875");
        assertFalse(user.checkPasswordComplexity());
    }

    @Test
    public void testCorrectCellPhone() {
        Login user = new Login("Kayise", "Mswane", "kaye_", "Kayise7575.", "+27815176875");
        assertTrue(user.checkCellPhoneNumber());
    }

    @Test
    public void testIncorrectCellPhone() {
        Login user = new Login("Kayise", "Mswane", "kaye_", "Kayise7575.", "08151768");
        assertFalse(user.checkCellPhoneNumber());
    }

    @Test
    public void testLoginSuccess() {
        Login user = new Login("Kayise", "Mswane", "kaye_", "Kayise7575.", "+27815176875");
        assertTrue(user.loginUser("kaye_", "Kayise7575."));
        assertEquals(user.returnLoginStatus("kaye_", "Kayise7575."), "Welcome Kayise, Mswane it is great to see you again.");
    }

    @Test
    public void testLoginFail() {
        Login user = new Login("Kayise", "Mswane", "kaye_", "Kayise7575!", "+27815176875");
        assertFalse(user.loginUser("wrongUser", "wrongPass"));
        assertEquals(user.returnLoginStatus("wrongUser", "wrongPass"), "Username or password incorrect, please try again.");
    }
}