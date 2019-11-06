package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.Item;
import com.example.demo.model.Location;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ItemCommandController {
    public ItemCommandController() {
    }

    //REQUISIÇÔES GET:

    //GET ALL
    public List<Item> listallItems() throws IOException {
        URL url = new URL("https://manage-bot-ufrn.herokuapp.com/items");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        ObjectMapper obj = new ObjectMapper();
        return obj.readValue(content.toString(), new TypeReference<List<Item>>() {});

    }


    //GET BY ID
    public Item itemByID(String id) throws IOException {
        URL url = new URL("https://manage-bot-ufrn.herokuapp.com/items/" + id);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        ObjectMapper obj = new ObjectMapper();
        return obj.readValue(content.toString(), Item.class);
    }


    //GET BY NAME
    public List<Item> itemByName(String item) throws IOException {
        URL url = new URL("https://manage-bot-ufrn.herokuapp.com/items/byItem/" + item);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        ObjectMapper obj = new ObjectMapper();
        return obj.readValue(content.toString(), new TypeReference<List<Item>>() {});
    }

    //GET BY CATEGORY
    public List<Item> itemByCategory(String item) throws IOException {
        URL url = new URL("https://manage-bot-ufrn.herokuapp.com/items/byCategory/" + item);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        ObjectMapper obj = new ObjectMapper();
        return obj.readValue(content.toString(), new TypeReference<List<Item>>() {});
    }

    //GET BY LOCATION
    public List<Item> itemByLocation(String item) throws IOException {
        URL url = new URL("https://manage-bot-ufrn.herokuapp.com/items/byLocation/" + item);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        ObjectMapper obj = new ObjectMapper();
        return obj.readValue(content.toString(), new TypeReference<List<Item>>() {});
    }


    //GET BY DESCRIPTION
    public List<Item> itemByDescription(String item) throws IOException {
        URL url = new URL("https://manage-bot-ufrn.herokuapp.com/items/byDescription/" + item);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        ObjectMapper obj = new ObjectMapper();
        return obj.readValue(content.toString(), new TypeReference<List<Item>>() {});
    }












    //REQUISIÇÃO POST
    public String itemPost(String item) throws IOException {
        LocationCommandController locationController = new LocationCommandController();
        CategoryCommandController categoryController = new CategoryCommandController();
        Location localization = null;
        Category categoria = null;
        String[] split = item.split(",");
        List<Location> local = locationController.listallLocations();
        if(!local.isEmpty()) {
            for (Location i:
                    local) {
                localization = i;
            }
            List<Category> categories = categoryController.listallCategories();
            if(!categories.isEmpty()) {
                for (Category i:
                        categories) {
                    categoria = i;
                }
                Item post = new Item();
                post.setLocation(localization);
                post.setCategory(categoria);
                post.setItem(split[2]);
                post.setDescription(split[3]);
            } else {
                return "Categoria não cadastrada";
            }
        } else {
            return "Local não cadastrado";
        }
        return null;
    }
    //REQUISIÇÃO PUT
    //REQUISIÇÃO DELETE
}
