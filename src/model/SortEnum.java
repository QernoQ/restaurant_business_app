package model;

/**
 * The `SortEnum` enumeration defines various command types or sorting criteria
 * used for communication between different components of the restaurant management system,
 * particularly between client GUIs and the server.
 * These values dictate actions to be performed or states to be communicated.
 */
/**
 * @author Patryk Bocheński
 */
public enum SortEnum {
    /**
     * Represents a command to add a new entity (worker)
     */
    ADD,
    /**
     * Represents a command to save changes to an existing entity or data.
     */
    SAVE,
    /**
     * Represents a command to remove an entity.
     */
    REMOVE,
    /**
     * Represents the "Boss" role or a command related to boss functionalities.
     */
    BOSS,
    /**
     * Represents the "Cook" role or a command related to cook functionalities.
     */
    COOK,
    /**
     * Represents the "Waiter" role or a command related to waiter functionalities.
     */
    WAITER,
    /**
     * Represents a command to add a new bill.
     */
    ADDBILL,
    /**
     * Represents a command to read or retrieve bill information.
     */
    READBILL,
    /**
     * Represents a command to close a bill.
     */
    CLOSEBILL,
    /**
     * Represents a command to go back or revert an action related to a bill.
     */
    BACKBILL,
    /**
     * Represents a command to save changes to a bill.
     */
    SAVEBILL,
    /**
     * Represents a status indicating that a bill has been successfully closed.
     */
    BILL_CLOSED,
    /**
     * Represents a status indicating that a bill is already locked by another user.
     */
    BILL_ALREADY_LOCKED,
    /**
     * Represents a status indicating that a bill has been successfully locked for editing.
     */
    BILL_LOCK_SUCCESS,
    /**
     * Represents a command to remove an order (e.g., when a cook completes it).
     */
    REMOVEORDER,
    /**
     * Represents a command to save a change after add an item option to an existing bill and save the change.
     */
    ADDITEMSAVE,
}