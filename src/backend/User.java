/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;


public abstract class User {
    private final String userId;
    private final String role;
    private String username;
    private String email;
    private String passwordHash;

    protected User(String userId, String role, String username, String email, String passwordHash) {
        this.userId = userId;
        this.role = role;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public String getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public abstract java.util.Map<String, Object> toMap();

    public static User fromMap(java.util.Map<String, Object> data) {
        String role = (String) data.get("role");
        if ("STUDENT".equalsIgnoreCase(role)) {
            return Student.fromMap(data);
        } else if ("INSTRUCTOR".equalsIgnoreCase(role)) {
            return Instructor.fromMap(data);
        }
        throw new IllegalArgumentException("Unknown role: " + role);
    }
}
