package lt.viko.eif.dienynas.models;

import java.util.List;

public class Group {
    private String id;
    private String name;
    private List<String> task;
    private List<Student> students;

    public Group() {
    }

    public Group(String id, String name, List<String> task, List<Student> students) {
        this.id = id;
        this.name = name;
        this.task = task;
        this.students = students;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTask() {
        return task;
    }

    public void setTask(List<String> task) {
        this.task = task;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", task=" + task +
                ", students=" + students +
                '}';
    }
}
