package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.Item;
import com.example.demo.model.Location;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
        if(con.getResponseCode() != 200) {
            return null;
        }

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
    public String PostItem(Item item) throws IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonInputString = ow.writeValueAsString(item);

        URL url = new URL("http://manage-bot-ufrn.herokuapp.com/items");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            con.getResponseCode();
        }
        if(con.getResponseCode()==200) {
            return "Cadastro de item feito com sucesso";
        } else {
            return "Ops! Houve um erro no cadastro do item, verifique se o Local ou Categoria já estão cadastrados no sistema";
        }
    }
    //REQUISIÇÃO PUT
    public String PutItem(Item item) throws IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonInputString = ow.writeValueAsString(item);
        System.out.println(jsonInputString);

        URL url = new URL("http://manage-bot-ufrn.herokuapp.com/items");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            con.getResponseCode();
        }
        if(con.getResponseCode()==200) {
            return "Atualização do item feita com sucesso";
        } else {
            return "Ops! Houve um erro na atualização do item";
        }
    }
    //REQUISIÇÃO DELETE
    public String DeleteItem(String id) throws IOException {
        URL url = new URL("https://manage-bot-ufrn.herokuapp.com/items/" + id);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");
        if(con.getResponseCode()==200) {
            return "Remoção de item feita com sucesso";
        } else {
            return "Ops! Houve um erro na remoção";
        }
    }
}
