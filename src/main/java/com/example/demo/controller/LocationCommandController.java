package com.example.demo.controller;

import com.example.demo.model.Location;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    public List<Location> byLocationName(String location) throws IOException {
        URL url = new URL("https://manage-bot-ufrn.herokuapp.com/locations/byLocationName/" + location);
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
    //REQUISIÇÃO PUT
    //REQUISIÇÃO DELETE
}
