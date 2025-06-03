package model;

import java.io.Serializable;
import java.util.List;
/**
 * @author Patryk Bocheński
 */
/**
 * The `Bill` class represents a customer's order in the restaurant system.
 * It encapsulates the state of an order, including its items, unique identifier,
 * and whether it's currently active or completed. This class is serializable
 * to allow for easy transmission between client and server applications.
 */
public class Bill implements Serializable {
    /**
     * A boolean flag indicating whether the bill is currently active.
     * `true` if the bill is open and being processed, `false` if it's closed or completed.
     */
    public boolean active;
    /**
     * A boolean flag indicating whether the food items on the bill have been ordered (prepared).
     * `true` if the kitchen has started/finished preparing the order, `false` otherwise.
     */
    public boolean order;
    /**
     * A list of `Food` objects that constitute the current order on this bill.
     */
    List<Food> currentOrder;
    /**
     * The unique identifier for this bill.
     */
    int billId;

    /**
     * Constructs a new `Bill` object.
     *
     * @param active       A boolean indicating if the bill is active.
     * @param order        A boolean indicating if the order associated with the bill has been sent to the kitchen.
     * @param currentOrder A list of `Food` objects representing the items on this bill.
     * @param billId       The unique ID for this bill.
     */
    public Bill(boolean active,boolean order,List<Food> currentOrder,int billId) {
        this.active = active;
        this.currentOrder = currentOrder;
        this.billId = billId;
        this.order = order;
    }


    public int getBillId() {
        return billId;
    }

    public List<Food> getCurrentOrder() {
        return (List<Food>) currentOrder;
    }


    public boolean isActive() {
        return active;
    }

    public boolean isDone() {
        return order;
    }

    @Override
    public String toString() {
        return currentOrder.toString() + " Id: " + billId + " Active: " + active + " Order: " + order;
    }
}