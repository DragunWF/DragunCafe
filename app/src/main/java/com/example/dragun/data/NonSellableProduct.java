package com.example.dragun.data;

public class NonSellableProduct extends Model {
    private String productName, unit;
    private int stock;

    public NonSellableProduct(String productName, String unit, int stock) {
        this.productName = productName;
        this.unit = unit;
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "NonSellableProduct{" +
                "productName='" + productName + '\'' +
                ", unit='" + unit + '\'' +
                ", stock=" + stock +
                ", id=" + id +
                '}';
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
