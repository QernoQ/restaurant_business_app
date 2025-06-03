package model;

/**
 * The `Boss` class represents an employee with the position of a boss within the restaurant system.
 * It extends the `Person` class, inheriting basic personal information.
 */
public class Boss extends Person {
    /**
     * Constructs a new `Boss` object.
     *
     * @param name     The first name of the boss.
     * @param surname  The surname of the boss.
     * @param age      The age of the boss.
     * @param id       The unique identification number of the boss.
     * @param position The position of the boss, which should always be `PositionEnum.BOSS`.
     */
    public Boss(String name, String surname, int age, int id, PositionEnum position) {
        super(name, surname, age, id, position);
    }
}