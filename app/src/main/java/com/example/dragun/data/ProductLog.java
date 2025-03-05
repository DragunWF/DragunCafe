package com.example.dragun.data;

public class ProductLog extends Model {
    private String description, datetime;

    public ProductLog(String description, String datetime) {
        this.description = description;
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "ProductLog{" +
                "description='" + description + '\'' +
                ", datetime='" + datetime + '\'' +
                ", id=" + id +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
