package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.Item;
import com.example.demo.model.Location;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ItemCommandController {
    public ItemCommandController() {
    }

    //REQUISIÇÔES GET:

    //GET ALL

    /**
     * Method get for all itens on data center
     * @return List of items
     * @throws IOException
     */
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

    /**
     * Method get item by ID
     * @param id
     * @return A item object with specific ID
     * @throws IOException
     */
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


    //GET BY TOMBO

    /**
     * Method get item by tombo
     * @param itemTombo
     * @return A item object with specific tombo
     * @throws IOException
     */
    public Item itemByTombo(String itemTombo) throws IOException {
        URL url = new URL("https://manage-bot-ufrn.herokuapp.com/items/byTombo/" + itemTombo);
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

    /**
     * Method get item by name
     * @param itemName
     * @return A List of items objects with close names
     * @throws IOException
     */
    public List<Item> itemByName(String itemName) throws IOException {
        URL url = new URL("https://manage-bot-ufrn.herokuapp.com/items/byItem/" + itemName);
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

    /**
     * Method get item by category
     * @param categoryName
     * @return A item object with specific category
     * @throws IOException
     */
    public List<Item> itemByCategory(String categoryName) throws IOException {
        URL url = new URL("https://manage-bot-ufrn.herokuapp.com/items/byCategory/" + categoryName);
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

    /**
     * Method get item by local
     * @param localName
     * @return A item object with specific name
     * @throws IOException
     */
    public List<Item> itemByLocation(String localName) throws IOException {
        URL url = new URL("https://manage-bot-ufrn.herokuapp.com/items/byLocation/" + localName);
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

    /**
     * Method get item by description
     * @param itemDescription
     * @return A item object with specific name
     * @throws IOException
     */
    public List<Item> itemByDescription(String itemDescription) throws IOException {
        URL url = new URL("https://manage-bot-ufrn.herokuapp.com/items/byDescription/" + itemDescription);
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

    /**
     * Method for create new Item
     * @param item
     * @return passed or failed
     * @throws IOException
     */
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

    /**
     * Method for update any info from item
     * @param item
     * @return passed or failed
     * @throws IOException
     */
    public String PutItem(Item item) throws IOException {
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
            return "Atualização do item feita com sucesso";
        } else {
            return "Ops! Houve um erro na atualização do item";
        }
    }

    //REQUISIÇÃO DELETE

    /**
     * Method for delete a item
     * @param id
     * @return passed or failed
     * @throws IOException
     */
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

    public void CSV(List<Item> li) throws IOException {
        StringBuilder CSV = new StringBuilder();
        for (Item i:
                li) {
            CSV.append(i.toCSV());
        }
        File file = new File("items.csv");
        FileWriter fw = new FileWriter(file);
        fw.write(CSV.toString());
        fw.close();
    }
}
