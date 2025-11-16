
package backend;

import java.util.ArrayList;
<<<<<<< HEAD
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
=======
import java.util.List;
import java.util.UUID;

public class Instructor extends User {

    private List<String> createdCourses = new ArrayList<>();

    public Instructor(String userId, String username, String email, String passwordHash) {
        super(userId, "INSTRUCTOR", username, email, passwordHash);
    }

    public Instructor(String userId, String username, String email, String passwordHash, List<String> createdCourses) {
        super(userId, "INSTRUCTOR", username, email, passwordHash);
        if (createdCourses != null) {
            this.createdCourses = createdCourses;
        }
>>>>>>> origin/space-mariam
    }

    public List<String> getCreatedCourses() {
        return createdCourses;
    }

<<<<<<< HEAD
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
=======
    public void addCreatedCourse(String courseId) {
        createdCourses.add(courseId);
    }


    public Course createCourse(String title, String description) {
        Course c = new Course(
                UUID.randomUUID().toString(),
                title,
                description,
                this.getUserId()
        );

        JsonDatabaseManager.courses.add(c);
        this.createdCourses.add(c.getCourseId());
        JsonDatabaseManager.saveCourses();
        JsonDatabaseManager.saveUsers();

        return c;
    }

    public void editCourse(Course course, String newTitle, String newDescription) {
        if (newTitle != null) course.setTitle(newTitle);
        if (newDescription != null) course.setDescription(newDescription);
        JsonDatabaseManager.saveCourses();
    }
    
    public void deleteCourse(Course course) {
        JsonDatabaseManager.courses.remove(course);
        this.createdCourses.remove(course.getCourseId());
        
        JsonDatabaseManager.saveCourses();
        JsonDatabaseManager.saveUsers();
    }


    public Lesson addLesson(Course course, String lessonTitle, String content) {
        Lesson lesson = new Lesson(
                UUID.randomUUID().toString(),
                lessonTitle,
                content
        );
        course.getLessons().add(lesson);
        JsonDatabaseManager.saveCourses();
        return lesson;
    }

    public void editLesson(Course course, String lessonId, String newTitle, String newContent) {
        for (Lesson l : course.getLessons()) {
            if (l.getLessonId().equals(lessonId)) {
                if (newTitle != null) l.setTitle(newTitle);
                if (newContent != null) l.setContent(newContent);
                break;
            }
        }
        JsonDatabaseManager.saveCourses();
    }

    public void deleteLesson(Course course, String lessonId) {
        course.getLessons().removeIf(l -> l.getLessonId().equals(lessonId));
        JsonDatabaseManager.saveCourses();
    }

    public List<String> viewEnrolledStudents(Course course) {
        return course.getEnrolledStudents();
    }
}
>>>>>>> origin/space-mariam
