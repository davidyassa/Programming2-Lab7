/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student extends User {

    private final List<String> enrolledCourses;
    private final Map<String, List<String>> progress;

    public Student(String userId, String username, String email, String passwordHash) {
        super(userId, "STUDENT", username, email, passwordHash);
        this.enrolledCourses = new ArrayList<>();
        this.progress = new HashMap<>();
    }

    public Student(String userId, String username, String email, String passwordHash,
            List<String> enrolledCourses, Map<String, List<String>> progress) {
        super(userId, "STUDENT", username, email, passwordHash);
        this.enrolledCourses = new ArrayList<>(enrolledCourses);
        this.progress = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : progress.entrySet()) {
            this.progress.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
    }

    public List<String> getEnrolledCourses() {
        return enrolledCourses;
    }

    public Map<String, List<String>> getProgress() {
        return progress;
    }

    public void enrollCourse(String courseId) {
        if (!enrolledCourses.contains(courseId)) {
            enrolledCourses.add(courseId);
        }
    }

    public void markLessonCompleted(String courseId, String lessonId) {
        List<String> lessons = progress.computeIfAbsent(courseId, k -> new ArrayList<>());
        if (!lessons.contains(lessonId)) {
            lessons.add(lessonId);
        }
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", getUserId());
        map.put("role", getRole());
        map.put("username", getUsername());
        map.put("email", getEmail());
        map.put("passwordHash", getPasswordHash());
        map.put("enrolledCourses", new ArrayList<>(enrolledCourses));
        Map<String, Object> progressMap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : progress.entrySet()) {
            progressMap.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        map.put("progress", progressMap);
        return map;
    }

    @SuppressWarnings("unchecked")
    public static Student fromMap(Map<String, Object> data) {
        String userId = (String) data.get("userId");
        String username = (String) data.get("username");
        String email = (String) data.get("email");
        String passwordHash = (String) data.get("passwordHash");
        List<String> enrolled = new ArrayList<>();
        Object enrolledObj = data.get("enrolledCourses");
        if (enrolledObj instanceof List) {
            for (Object item : (List<?>) enrolledObj) {
                if (item != null) {
                    enrolled.add(item.toString());
                }
            }
        }
        Map<String, List<String>> progress = new HashMap<>();
        Object progressObj = data.get("progress");
        if (progressObj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) progressObj;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                List<String> lessons = new ArrayList<>();
                if (entry.getValue() instanceof List) {
                    for (Object lesson : (List<?>) entry.getValue()) {
                        if (lesson != null) {
                            lessons.add(lesson.toString());
                        }
                    }
                }
                progress.put(entry.getKey(), lessons);
            }
        }
        return new Student(userId, username, email, passwordHash, enrolled, progress);
    }
}
