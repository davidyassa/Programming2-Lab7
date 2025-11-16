/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Course {

    private final String courseId;
    private String title;
    private String description;
    private String instructorId;
    private final List<Lesson> lessons;
    private final List<String> students;

    public Course(String courseId, String title, String description, String instructorId) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        this.lessons = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public Course(String courseId, String title, String description, String instructorId,
            List<Lesson> lessons, List<String> students) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        this.lessons = new ArrayList<>(lessons);
        this.students = new ArrayList<>(students);
    }

    public String getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public List<String> getStudents() {
        return students;
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public void removeLesson(String lessonId) {
        lessons.removeIf(l -> l.getLessonId().equals(lessonId));
    }

    public Lesson findLesson(String lessonId) {
        for (Lesson lesson : lessons) {
            if (lesson.getLessonId().equals(lessonId)) {
                return lesson;
            }
        }
        return null;
    }

    public void enrollStudent(String studentId) {
        if (!students.contains(studentId)) {
            students.add(studentId);
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("courseId", courseId);
        map.put("title", title);
        map.put("description", description);
        map.put("instructorId", instructorId);
        List<Object> lessonList = new ArrayList<>();
        for (Lesson lesson : lessons) {
            lessonList.add(lesson.toMap());
        }
        map.put("lessons", lessonList);
        map.put("students", new ArrayList<>(students));
        return map;
    }

    @SuppressWarnings("unchecked")
    public static Course fromMap(Map<String, Object> data) {
        String courseId = (String) data.get("courseId");
        String title = (String) data.get("title");
        String description = (String) data.get("description");
        String instructorId = (String) data.get("instructorId");
        List<Lesson> lessonList = new ArrayList<>();
        Object lessonsObj = data.get("lessons");
        if (lessonsObj instanceof List) {
            for (Object obj : (List<?>) lessonsObj) {
                if (obj instanceof Map) {
                    lessonList.add(Lesson.fromMap((Map<String, Object>) obj));
                }
            }
        }
        List<String> students = new ArrayList<>();
        Object studentsObj = data.get("students");
        if (studentsObj instanceof List) {
            for (Object obj : (List<?>) studentsObj) {
                if (obj != null) {
                    students.add(obj.toString());
                }
            }
        }
        return new Course(courseId, title, description, instructorId, lessonList, students);
    }

    @Override
    public String toString() {
        return title;
    }
}
