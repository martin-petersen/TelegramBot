package com.example.demo.model;


public class Item {
    private int id;

    private Location location;
    private Category category;
    private int tombo;
    private String item;
    private String description;

    public Item() {
    }

    public Item(Location location, Category category, String item, String description) {
        this.location = location;
        this.category = category;
        this.item = item;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    public int getTombo() {
        return tombo;
    }

    public void setTombo(int tombo) {
        this.tombo = tombo;
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
                "Tombo: " + tombo + "\n" +
                "Local: " + location.getLocation() + "\n" +
                "Categoria: " + category.getCategory() + "\n" +
                "Item: " + item + "\n" +
                "Descrição: " + description +
                "\n\n\n";
    }
}
