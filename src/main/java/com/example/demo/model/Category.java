package com.example.demo.model;


public class Category {
    private int id;

    private String category;
    private String description;

    public Category() {
    }

    public Category(String category, String description) {
        this.category = category;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
                "Categoria: " + category + "\n" +
                "Descrição: " + description + "\n\n\n";
    }
}
