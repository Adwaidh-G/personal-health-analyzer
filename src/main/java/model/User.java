package model;

public class User {
    private double height; // meters
    private double weight; // kg

    public User(double height, double weight) {
        this.height = height;
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public double calculateBMI() {
        return weight / (height * height);
    }
}
