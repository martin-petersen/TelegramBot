package com.example.demo.controller;

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

public class LocationCommandController {
    public LocationCommandController() {
    }

    //REQUISIÇÔES GET:


    //GET ALL
    public List<Location> listallLocations() throws IOException {
        URL url = new URL("https://manage-bot-ufrn.herokuapp.com/locations");
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
        return obj.readValue(content.toString(), new TypeReference<List<Location>>() {});
    }


    //GET BY ID
    public Location localByID (String id) throws IOException {
        URL url = new URL("https://manage-bot-ufrn.herokuapp.com/locations/" + id);
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
        return obj.readValue(content.toString(), Location.class);
    }


    //GET BY NAME
    public Location byLocationName(String location) throws IOException {
        URL url = new URL("https://manage-bot-ufrn.herokuapp.com/locations/byLocationName/" + location);
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
        return obj.readValue(content.toString(), Location.class);
    }


    //GET BY DESCRIPTION
    public List<Location> byLocationDescription(String location) throws IOException {
        URL url = new URL("https://manage-bot-ufrn.herokuapp.com/locations/byLocationDescription/" + location);
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
        return obj.readValue(content.toString(), new TypeReference<List<Location>>() {});
    }


    //REQUISIÇÃO POST
    public String PostLocation(Location location) throws IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonInputString = ow.writeValueAsString(location);

        URL url = new URL("http://manage-bot-ufrn.herokuapp.com/locations");
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
            return "Cadastro de local feito com sucesso";
        } else {
            return "Ops! Houve um erro no cadastro da localização";
        }
    }

    //REQUISIÇÃO DELETE

    public String DeleteLocal(String id) throws IOException {
        URL url = new URL("https://manage-bot-ufrn.herokuapp.com/locations/" + id);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");
        if(con.getResponseCode()==200) {
            return "Remoção de local feita com sucesso";
        } else {
            return "Ops! Houve um erro na remoção";
        }
    }
}
