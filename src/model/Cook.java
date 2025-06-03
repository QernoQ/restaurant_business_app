package model;

/**
 * The `Cook` class represents an employee with the position of a cook within the restaurant system.
 * It extends the `Person` class, inheriting basic personal information such as name, surname, age, and ID.
 */
public class Cook extends Person {
    /**
     * Constructs a new `Cook` object.
     *
     * @param name     The first name of the cook.
     * @param surname  The surname of the cook.
     * @param age      The age of the cook.
     * @param id       The unique identification number of the cook.
     * @param position The position of the cook, which should always be `PositionEnum.COOK`.
     */
    public Cook(String name, String surname, int age, int id, PositionEnum position) {
        super(name, surname, age, id, position);
    }
}