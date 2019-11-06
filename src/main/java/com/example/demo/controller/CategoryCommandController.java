package com.example.demo.controller;

import com.example.demo.model.Category;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CategoryCommandController {
    public CategoryCommandController() {
    }

    //REQUISIÇÔES GET:


    //GET ALL
    public List<Category> listallCategories() throws IOException {
        URL url = new URL("https://manage-bot-ufrn.herokuapp.com/category");
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
        List<Category> categories = obj.readValue(content.toString(), new TypeReference<List<Category>>() {});
        return categories;
    }


    //GET BY ID
    public Category categoryByID (String id) throws IOException {
        URL url = new URL("https://manage-bot-ufrn.herokuapp.com/category/" + id);
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
        return obj.readValue(content.toString(), Category.class);
    }


    //GET BY NAME
    public List<Category> byCategoryName(String categoria) throws IOException {
        URL url = new URL("https://manage-bot-ufrn.herokuapp.com/category/byCategoryName/" + categoria);
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
        return obj.readValue(content.toString(), new TypeReference<List<Category>>() {});
    }


    //GET BY DESCRIPTION
    public List<Category> byCategoryDescription(String categoria) throws IOException {
        URL url = new URL("https://manage-bot-ufrn.herokuapp.com/category/byCategoryDescription/" + categoria);
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
        return obj.readValue(content.toString(), new TypeReference<List<Category>>() {});
    }












    //REQUISIÇÃO POST
    //REQUISIÇÃO PUT
    //REQUISIÇÃO DELETE
}
