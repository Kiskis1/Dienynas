package lt.viko.eif.dienynas.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Student {
    private String code;
    private String fullName;
    private List<Integer> grades;

    public Student() {
    }

    public Student(String code, String fullName, List<Integer> grades) {
        this.code = code;
        this.fullName = fullName;
        this.grades = grades;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<Integer> getGrades() {
        return grades;
    }

    public void setGrades(List<Integer> grades) {
        this.grades = grades;
    }

    @Exclude
    public String getCodeFullName() {
        return getCode() + "\n" + getFirstName() + "\n" + getLastName();
    }

    @Exclude
    private String getFirstName() {
        String[] s = getFullName().split(" ");
        return s[0];
    }

    @Exclude
    private String getLastName() {
        String[] s = getFullName().split(" ");
        if (s.length == 1) return "";
        return s[1];
    }

    @Exclude
    public void setStudentByCodeFullName(String full) {
        String[] s = full.split("\n");
        this.code = s[0];
        this.fullName = s[1] + " " + s[2];
    }

    @NonNull
    @Override
    public String toString() {
        return "Student{" +
                "code='" + code + '\'' +
                ", fullName='" + fullName + '\'' +
                ", grades=" + grades +
                '}';
    }
}
