package com.example.demo;

import com.example.demo.model.Item;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@SpringBootApplication
public class TelegramManageBotApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(TelegramManageBotApplication.class, args);

        //Criação do objeto bot com as informações de acesso
        TelegramBot bot = new TelegramBot("1004965720:AAG14C7ARHBeYuV1VHyNwZ1XJgwp9UDwGiE");

        //objeto responsável por receber as mensagens
        GetUpdatesResponse updatesResponse;
        //objeto responsável por gerenciar o envio de respostas
        SendResponse sendResponse;
        //objeto responsável por gerenciar o envio de ações do chat
        BaseResponse baseResponse;

        //controle de off-set, isto é, a partir deste ID será lido as mensagens pendentes na fila
        int m = 0;

        String command = "";
        String mensagem = "";

        //loop infinito pode ser alterado por algum timer de intervalo curto
        while (true) {

            //executa comando no Telegram para obter as mensagens pendentes a partir de um off-set (limite inicial)
            updatesResponse = bot.execute(new GetUpdates().limit(100).offset(m));

            //lista de mensagens
            List<Update> updates = updatesResponse.updates();

//            análise de cada ação da mensagem
            for (Update update : updates) {

                //atualização do off-set
                m = update.updateId() + 1;
                mensagem = update.message().text();

                //envio de "Escrevendo" antes de enviar a resposta
                baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
                //verificação de ação de chat foi enviada com sucesso
                //TODO organizar o código para ficar mais bonito

                //Método para listar tudo
                if (update.message().text().equals("/list")) {
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
                    List<Item> itens = obj.readValue(content.toString(), new TypeReference<List<Item>>() {});
                    for (Item i:
                         itens) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                    }
                }
                //######################################################################################################

                //Método para buscar por ID
                if(update.message().text().equals("/findbyid")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Digite o ID:"));
                    mensagem = command;
                }
                if(command.equals("/findbyid")&&!command.equals(mensagem)) {
                    URL url = new URL("https://manage-bot-ufrn.herokuapp.com/items/" + update.message().text());
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
                    Item item = obj.readValue(content.toString(), Item.class);
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), item.toString()));
                    command = "";
                }
                //######################################################################################################

                //Método para buscar pelo nome do item
                if(update.message().text().equals("/findbyname")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Digite o nome do item:"));
                    mensagem = command;
                }
                if(command.equals("/findbyname")&&!command.equals(mensagem)) {
                    URL url = new URL("https://manage-bot-ufrn.herokuapp.com/items/byItem/" + update.message().text());
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
                    List<Item> itens = obj.readValue(content.toString(), new TypeReference<List<Item>>() {});
                    for (Item i:
                            itens) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                    }
                    command = "";
                }
                //######################################################################################################

                //Método para buscar pela categoria do item
                if(update.message().text().equals("/findbycategory")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Digite a categoria:"));
                    mensagem = command;
                }
                if(command.equals("/findbycategory")&&!command.equals(mensagem)) {
                    URL url = new URL("https://manage-bot-ufrn.herokuapp.com/items/byCategory/" + update.message().text());
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
                    List<Item> itens = obj.readValue(content.toString(), new TypeReference<List<Item>>() {});
                    for (Item i:
                            itens) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                    }
                    command = "";
                }
                //######################################################################################################

                //Método para buscar pela localização
                if(update.message().text().equals("/findbylocation")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Digite a localização:"));
                    mensagem = command;
                }
                if(command.equals("/findbylocation")&&!command.equals(mensagem)) {
                    URL url = new URL("https://manage-bot-ufrn.herokuapp.com/items/byLocation/" + update.message().text());
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
                    List<Item> itens = obj.readValue(content.toString(), new TypeReference<List<Item>>() {});
                    for (Item i:
                            itens) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                    }
                    command = "";
                }
                //######################################################################################################

                //Método para busca pela descrição do item
                if(update.message().text().equals("/findbydescription")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Digite a descrição:"));
                    mensagem = command;
                }
                if(command.equals("/findbydescription")&&!command.equals(mensagem)) {
                    URL url = new URL("https://manage-bot-ufrn.herokuapp.com/items/byDescription/" + update.message().text());
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
                    List<Item> itens = obj.readValue(content.toString(), new TypeReference<List<Item>>() {});
                    for (Item i:
                            itens) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                    }
                    command = "";
                }
                //######################################################################################################

                //Método para listar as localizações cadastradas
                if(update.message().text().equals("/locations")) {
                    URL url = new URL("https://manage-bot-ufrn.herokuapp.com/items/locations");
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
                    List<String> locations = obj.readValue(content.toString(), new TypeReference<List<String>>() {});
                    for (String i:
                            locations) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i));
                    }
                }
                //######################################################################################################

                //Método para listar as categorias cadastradas
                if(update.message().text().equals("/category")) {
                    URL url = new URL("https://manage-bot-ufrn.herokuapp.com/items/categories");
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
                    List<String> categories = obj.readValue(content.toString(), new TypeReference<List<String>>() {});
                    for (String i:
                            categories) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i));
                    }
                }
            }
        }
    }
}
