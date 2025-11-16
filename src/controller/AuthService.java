
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import backend.Instructor;
import backend.JsonDatabaseManager;
import backend.Student;
import backend.User;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthService {

    private final JsonDatabaseManager databaseManager;
    private User currentUser;

    public AuthService(JsonDatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public synchronized User signup(String role, String username, String email, String password) {
        validateSignupInputs(role, username, email, password);
        if (databaseManager.findUserByEmail(email) != null) {
            throw new IllegalArgumentException("Email already exists.");
        }
        String userId = databaseManager.generateUserId();
        String passwordHash = hashPassword(password);
        User user;
        if ("INSTRUCTOR".equalsIgnoreCase(role)) {
            user = new Instructor(userId, username, email, passwordHash);
        } else if ("STUDENT".equalsIgnoreCase(role)) {
            user = new Student(userId, username, email, passwordHash);
        } else {
            throw new IllegalArgumentException("Invalid role.");
        }
        databaseManager.addUser(user);
        return user;
    }

    public synchronized User login(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Email and password are required.");
        }
        User user = databaseManager.findUserByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        if (!user.getPasswordHash().equals(hashPassword(password))) {
            throw new IllegalArgumentException("Invalid credentials.");
        }
        currentUser = user;
        return user;
    }

    public synchronized void logout() {
        currentUser = null;
    }

    public synchronized User getCurrentUser() {
        return currentUser;
    }

    private void validateSignupInputs(String role, String username, String email, String password) {
        if (role == null || role.isEmpty()) {
            throw new IllegalArgumentException("Role is required.");
        }
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (email == null || email.isEmpty() || !isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email.");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }
    }

    public boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 unavailable", e);
        }
    }
}
