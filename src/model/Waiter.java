package model;

/**
 * The `Waiter` class represents an employee with the position of a waiter within the restaurant system.
 * It extends the `Person` class, inheriting basic personal information.
 */
public class Waiter extends Person {
    /**
     * Constructs a new `Waiter` object.
     *
     * @param name     The first name of the waiter.
     * @param surname  The surname of the waiter.
     * @param age      The age of the waiter.
     * @param id       The unique identification number of the waiter.
     * @param position The position of the waiter, which should always be `PositionEnum.WAITER`.
     */
    public Waiter(String name, String surname, int age, int id, PositionEnum position) {
        super(name, surname, age, id, position);
    }
}