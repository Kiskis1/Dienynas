package lt.viko.eif.dienynas.models;

import androidx.annotation.NonNull;

import java.util.List;

public class Destytojas {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private List<Group> group;

    public Destytojas() {
    }

    public Destytojas(int id, String firstName, String lastName, String email, List<Group> group) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Group> getGroup() {
        return group;
    }

    public void setGroup(List<Group> group) {
        this.group = group;
    }

    @NonNull
    @Override
    public String toString() {
        return "Destytojas{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", group=" + group +
                '}';
    }
}
