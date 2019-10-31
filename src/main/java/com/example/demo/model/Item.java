package com.example.demo.model;

public class Item {
    private int id;
    private String location;
    private String category;
    private String item;
    private String description;

    public Item() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ID: " + id + "\n" +
                "Local: '" + location + "\n" +
                "Categoria: '" + category + "\n" +
                "Item: '" + item + "\n" +
                "Descrição: '" + description + "\n" +
                "\n\n";
    }
}
