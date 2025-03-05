package com.example.dragun.data;

public class SellableProduct extends Model {
    private String productName, category;
    private double price;

    public SellableProduct(String productName, double price, String category) {
        this.productName = productName;
        this.category = category;
        this.price = price;
    }

    @Override
    public String toString() {
        return "SellableProduct{" +
                "productName='" + productName + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", id=" + id +
                '}';
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
