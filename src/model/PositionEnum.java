package model;

/**
 * The `PositionEnum` enumeration defines the different roles or positions
 * available for employees within the restaurant management system.
 * This ensures consistency and simplifies role-based access control or
 * functionality within the application.
 */
/**
 * @author Patryk Bocheński
 */
public enum PositionEnum {
    /**
     * Represents the management role with the highest level of authority.
     */
    Boss,
    /**
     * Represents the role responsible for food preparation in the kitchen.
     */
    Cook,
    /**
     * Represents the role responsible for serving customers and managing bills.
     */
    Waiter,
}