package model;

import java.io.Serializable;
import java.util.List;

public class Bill implements Serializable {
    public boolean active;
    public boolean order;
    List<Food> currentOrder;
    int billId;
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
