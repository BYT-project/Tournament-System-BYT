package com.example.tournamentsystembyt.model;

public class Ticket {
    private double price;
    private String type;
    private String status;
    private String id;
    public static final double TAX_FEE = 0.05; // 5% tax static final variable
    public Ticket(String id, double price, String type, String status) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Ticket id cannot be empty");
        }
        validatePrice(price);
        this.price = price;
        this.type = type;
        this.status = status;

    }
    public String getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }
    public String getType() {
        return type;
    }
    public String getStatus() {
        return status;
    }
    private void validatePrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
    }
    public void setPrice(double price) {
        validatePrice(price);
        this.price = price;
    }
    private void validateType(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty.");
        }
    }
    public void setType(String type) {
        validateType(type);
        this.type = type;
    }
    private void validateStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty.");
        }
    }
    public void setStatus(String status) {
        validateStatus(status);
        this.status = status;
    }
    // Complex bahavior: Calculate total price including tax
    public double calculateTotalPrice() {
        return price + (price * TAX_FEE);
    }
    @Override
    public String toString() {
        return "Ticket{" +
                "price=" + price +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

}
