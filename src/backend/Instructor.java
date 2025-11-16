
package backend;

import java.util.ArrayList;
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
    }

    public List<String> getCreatedCourses() {
        return createdCourses;
    }

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