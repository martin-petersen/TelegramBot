package com.example.demo.model;


public class Location {
    private int id;

    private String location;
    private String description;

    public Location() {
    }

    public Location(String location, String description) {
        this.location = location;
        this.description = description;
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


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ID: " + id + "\n" +
                "Local: " + location + "\n" +
                "Descrição: " + description + "\n\n\n";
    }
}
