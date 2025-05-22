package model;

public class Person {
    protected String name;
    protected String surname;
    protected int age;

    protected int id;

    protected int access; // idk czy sie przyda

    public Person(String name, String surname, int age, int id, int access) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.id = id;
        this.access = access;
    }
}
