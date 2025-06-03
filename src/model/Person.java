package model;

import javax.swing.text.Position;
import java.io.Serializable;

/**
 * The `Person` class serves as a base class for all individuals within the restaurant management system.
 * It encapsulates common personal information such as name, surname, age, a unique identifier,
 * and their position within the organization. This class is serializable for network transmission.
 */
public class Person implements Serializable {
    /**
     * The first name of the person.
     */
    protected String name;
    /**
     * The surname (last name) of the person.
     */
    protected String surname;
    /**
     * The age of the person.
     */
    protected int age;

    /**
     * A unique identification number for the person.
     */
    protected int id;
    /**
     * The position or role of the person within the restaurant, defined by `PositionEnum`.
     */
    PositionEnum position;

    /**
     * Constructs a new `Person` object with the specified details.
     *
     * @param name     The first name of the person.
     * @param surname  The surname of the person.
     * @param age      The age of the person.
     * @param id       The unique identification number of the person.
     * @param position The position of the person within the restaurant.
     */
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
    /**
     * Returns a string representation of the `Person` object.
     * This includes the name, surname, age, ID, and position of the person.
     *
     * @return A `String` containing the person's details.
     */
    @Override
    public String toString() {
        return "Name: " + name + " Surname: " + surname + " Age: " + age + " ID: " + id + " Position: " + position;
    }
}