package com.example.rentalService.data;

import java.util.ArrayList;
import java.util.List;

public class Car {
    private String plateNumber;
    private String brand;
    private String model;
    private int year;
    private String color;
    private int price;
    private ArrayList<Rental> rentals = new ArrayList<>();

    public Car(String plateNumber, String brand, String model, int year, String color, int price) {
        this.plateNumber = plateNumber;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.price = price;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public void addRental(Rental rental) {
        rentals.add(rental);
    }

    public Rental getActiveRental() {
        for (Rental rental : rentals) {
            if (rental.isActive()) {
                return rental;
            }
        }
        return null;
    }

    public void endActiveRental() {
        for (Rental rental : rentals) {
            if (rental.isActive()) {
                rental.setActive(false);
            }
        }
    }

    public boolean isRented() {
        for (Rental rental : rentals) {
            if (rental.isActive()) {
                return true;
            }
        }
        return false;
    }
}
