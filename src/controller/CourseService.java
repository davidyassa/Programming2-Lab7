/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package controller;

import backend.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseService {

    private final JsonDatabaseManager dbManager;

    public CourseService(JsonDatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public List<Course> getAllCourses() {
        return dbManager.getCourses();
    }

    public List<Course> getEnrolledCourses(String studentId) {
        User user = dbManager.findUserById(studentId);
        if (user instanceof Student) {
            return dbManager.getCoursesForStudent((Student) user);
        }
        return new ArrayList<>();
    }

    public boolean enrollStudent(String studentId, String courseId) {
        User user = dbManager.findUserById(studentId);
        Course course = dbManager.findCourseById(courseId);
        if (user instanceof Student && course != null) {
            dbManager.enrollStudentInCourse((Student) user, course);
            return true;
        }
        return false;
    }

    public double getCourseProgress(String studentId, String courseId) {
        User user = dbManager.findUserById(studentId);
        Course course = dbManager.findCourseById(courseId);
        if (!(user instanceof Student) || course == null) return 0.0;
        Student student = (Student) user;
        int totalLessons = course.getLessons().size();
        if (totalLessons == 0) return 0.0;
        Map<String, List<String>> progress = student.getProgress();
        List<String> completedLessons = progress.get(courseId);
        int completedCount = (completedLessons == null) ? 0 : completedLessons.size();
        return ((double) completedCount / totalLessons) * 100.0;
    }

    public boolean completeLesson(String studentId, String courseId, String lessonId) {
        User user = dbManager.findUserById(studentId);
        if (user instanceof Student) {
            dbManager.markLessonCompleted((Student) user, courseId, lessonId);
            return true;
        }
        return false;
    }
}