/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Instructor extends User {

    private final List<String> createdCourses;

    public Instructor(String userId, String username, String email, String passwordHash) {
        super(userId, "INSTRUCTOR", username, email, passwordHash);
        this.createdCourses = new ArrayList<>();
    }

    public Instructor(String userId, String username, String email, String passwordHash,
            List<String> createdCourses) {
        super(userId, "INSTRUCTOR", username, email, passwordHash);
        this.createdCourses = new ArrayList<>(createdCourses);
    }

    public List<String> getCreatedCourses() {
        return createdCourses;
    }

    public void addCourse(String courseId) {
        if (!createdCourses.contains(courseId)) {
            createdCourses.add(courseId);
        }
    }

    public void removeCourse(String courseId) {
        createdCourses.remove(courseId);
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", getUserId());
        map.put("role", getRole());
        map.put("username", getUsername());
        map.put("email", getEmail());
        map.put("passwordHash", getPasswordHash());
        map.put("createdCourses", new ArrayList<>(createdCourses));
        return map;
    }

    public static Instructor fromMap(Map<String, Object> data) {
        String userId = (String) data.get("userId");
        String username = (String) data.get("username");
        String email = (String) data.get("email");
        String passwordHash = (String) data.get("passwordHash");
        List<String> created = new ArrayList<>();
        Object createdObj = data.get("createdCourses");
        if (createdObj instanceof List) {
            for (Object obj : (List<?>) createdObj) {
                if (obj != null) {
                    created.add(obj.toString());
                }
            }
        }
        return new Instructor(userId, username, email, passwordHash, created);
    }
}
