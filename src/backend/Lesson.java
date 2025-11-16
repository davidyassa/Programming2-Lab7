/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lesson {

    private final String lessonId;
    private String title;
    private String content;
    private final List<String> resources;

    public Lesson(String lessonId, String title, String content) {
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
        this.resources = new ArrayList<>();
    }

    public Lesson(String lessonId, String title, String content, List<String> resources) {
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
        this.resources = new ArrayList<>(resources);
    }

    public String getLessonId() {
        return lessonId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getResources() {
        return resources;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("lessonId", lessonId);
        map.put("title", title);
        map.put("content", content);
        map.put("resources", new ArrayList<>(resources));
        return map;
    }

    public static Lesson fromMap(Map<String, Object> data) {
        String lessonId = (String) data.get("lessonId");
        String title = (String) data.get("title");
        String content = (String) data.get("content");
        List<String> resources = new ArrayList<>();
        Object resourcesObj = data.get("resources");
        if (resourcesObj instanceof List) {
            for (Object obj : (List<?>) resourcesObj) {
                if (obj != null) {
                    resources.add(obj.toString());
                }
            }
        }
        return new Lesson(lessonId, title, content, resources);
    }

    @Override
    public String toString() {
        return title;
    }
}
