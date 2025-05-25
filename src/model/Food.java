package model;


import java.io.Serializable;

public class Food implements Serializable {

    private String name;
    private double price;
    private FoodCategoryEnum category;

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
