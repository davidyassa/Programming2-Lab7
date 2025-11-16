/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JsonDatabaseManager {

    private final Path usersPath;
    private final Path coursesPath;
    private final List<User> users;
    private final List<Course> courses;

    public JsonDatabaseManager(String usersFile, String coursesFile) {
        this.usersPath = Paths.get(usersFile);
        this.coursesPath = Paths.get(coursesFile);
        this.users = new ArrayList<>();
        this.courses = new ArrayList<>();
        initializeFiles();
        loadUsers();
        loadCourses();
    }

    private void initializeFiles() {
        try {
            if (Files.notExists(usersPath)) {
                Files.write(usersPath, "{\"users\":[]}".getBytes(StandardCharsets.UTF_8));
            }
            if (Files.notExists(coursesPath)) {
                Files.write(coursesPath, "{\"courses\":[]}".getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public synchronized List<Course> getCourses() {
        return new ArrayList<>(courses);
    }

    public synchronized User findUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    public synchronized User findUserById(String userId) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    public synchronized Course findCourseById(String courseId) {
        for (Course course : courses) {
            if (course.getCourseId().equals(courseId)) {
                return course;
            }
        }
        return null;
    }

    public synchronized void addUser(User user) {
        users.add(user);
        saveUsers();
    }

    public synchronized void updateUsers() {
        saveUsers();
    }

    public synchronized void addCourse(Course course) {
        courses.add(course);
        saveCourses();
    }

    public synchronized void removeCourse(String courseId) {
        courses.removeIf(c -> c.getCourseId().equals(courseId));
        saveCourses();
    }

    public synchronized void updateCourses() {
        saveCourses();
    }

    public synchronized List<Course> getCoursesForInstructor(String instructorId) {
        List<Course> list = new ArrayList<>();
        for (Course course : courses) {
            if (course.getInstructorId().equals(instructorId)) {
                list.add(course);
            }
        }
        return list;
    }

    public synchronized List<Course> getCoursesForStudent(Student student) {
        List<Course> list = new ArrayList<>();
        for (String courseId : student.getEnrolledCourses()) {
            Course course = findCourseById(courseId);
            if (course != null) {
                list.add(course);
            }
        }
        return list;
    }

    public synchronized void enrollStudentInCourse(Student student, Course course) {
        student.enrollCourse(course.getCourseId());
        course.enrollStudent(student.getUserId());
        saveUsers();
        saveCourses();
    }

    public synchronized void markLessonCompleted(Student student, String courseId, String lessonId) {
        student.markLessonCompleted(courseId, lessonId);
        saveUsers();
    }

    public synchronized String generateUserId() {
        String id;
        do {
            id = "U-" + UUID.randomUUID().toString();
        } while (findUserById(id) != null);
        return id;
    }

    public synchronized String generateCourseId() {
        String id;
        do {
            id = "C-" + UUID.randomUUID().toString();
        } while (findCourseById(id) != null);
        return id;
    }

    public synchronized String generateLessonId(Course course) {
        String id;
        do {
            id = "L-" + UUID.randomUUID().toString();
        } while (course.findLesson(id) != null);
        return id;
    }

    private void loadUsers() {
        users.clear();
        try {
            String content = Files.readString(usersPath, StandardCharsets.UTF_8);
            Object parsed = SimpleJson.parse(content);
            if (parsed instanceof Map) {
                Object usersObj = ((Map<?, ?>) parsed).get("users");
                if (usersObj instanceof List) {
                    for (Object obj : (List<?>) usersObj) {
                        if (obj instanceof Map) {
                            User user = User.fromMap(castMap(obj));
                            users.add(user);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCourses() {
        courses.clear();
        try {
            String content = Files.readString(coursesPath, StandardCharsets.UTF_8);
            Object parsed = SimpleJson.parse(content);
            if (parsed instanceof Map) {
                Object coursesObj = ((Map<?, ?>) parsed).get("courses");
                if (coursesObj instanceof List) {
                    for (Object obj : (List<?>) coursesObj) {
                        if (obj instanceof Map) {
                            courses.add(Course.fromMap(castMap(obj)));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUsers() {
        Map<String, Object> root = new HashMap<>();
        List<Object> userList = new ArrayList<>();
        for (User user : users) {
            userList.add(user.toMap());
        }
        root.put("users", userList);
        write(usersPath, SimpleJson.stringify(root));
    }

    private void saveCourses() {
        Map<String, Object> root = new HashMap<>();
        List<Object> courseList = new ArrayList<>();
        for (Course course : courses) {
            courseList.add(course.toMap());
        }
        root.put("courses", courseList);
        write(coursesPath, SimpleJson.stringify(root));
    }

    private void write(Path path, String data) {
        try {
            Files.writeString(path, data, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> castMap(Object obj) {
        return (Map<String, Object>) obj;
    }
}
