package model;

import javax.swing.text.Position;
import java.io.Serializable;

public class Person implements Serializable {
    protected String name;
    protected String surname;
    protected int age;

    protected int id;
    PositionEnum position;

    public Person(String name, String surname, int age, int id, PositionEnum position ) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.id = id;
        this.position = position;
    }
    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public String getSurname() {
        return surname;
    }

    public PositionEnum getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Name: " + name + " Surname: " + surname + " Age: " + age + " ID: " + id + " Position: " + position;
    }
}
