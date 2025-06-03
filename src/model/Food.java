package model;


import java.io.Serializable;
/**
 * @author Patryk Bocheński
 */
/**
 * The `Food` class represents a single food or drink item available in the restaurant menu.
 * It stores details such as the item's name, price, and its category.
 * This class is serializable, allowing `Food` objects to be easily transmitted
 * between different parts of a distributed system, such as between a client and a server.
 */
public class Food implements Serializable {

    /**
     * The name of the food item (e.g., "Spaghetti Bolognese", "Coca-Cola").
     */
    private String name;
    /**
     * The price of the food item.
     */
    private double price;
    /**
     * The category to which the food item belongs (e.g., DESSERTS, MAIN_COURSES, COLD_DRINKS).
     */
    private FoodCategoryEnum category;

    /**
     * Constructs a new `Food` object with the specified name, price, and category.
     *
     * @param name     The name of the food item.
     * @param price    The price of the food item.
     * @param category The category of the food item, as defined by `FoodCategoryEnum`.
     */
    public Food(String name, double price, FoodCategoryEnum category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getName() {
        return name;
    }


    public double getPrice() {
        return price;
    }


    public FoodCategoryEnum getCategory() {
        return category;
    }


    @Override
    public String toString() {
        return String.format("%s - %.0f $", name, price);
    }
}