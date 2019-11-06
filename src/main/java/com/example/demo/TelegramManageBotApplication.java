package com.example.demo;

import com.example.demo.controller.CategoryCommandController;
import com.example.demo.controller.ItemCommandController;
import com.example.demo.controller.LocationCommandController;
import com.example.demo.model.Category;
import com.example.demo.model.Item;
import com.example.demo.model.Location;
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

import java.io.IOException;
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
        List<Item> FileStorage;
        LocationCommandController locationController = new LocationCommandController();
        CategoryCommandController categoryController = new CategoryCommandController();
        ItemCommandController itemCommandController = new ItemCommandController();

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

                baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));



                //TODO REQUESTS RELACIONADOS A ITENS

                //Método para listar tudo
                if (update.message().text().equals("/list")) {
                    List<Item> itens = itemCommandController.listallItems();
                    for (Item i:
                            itens) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                    }
                    command = "";
                }
                //######################################################################################################

                //Método para buscar por ID
                if(update.message().text().equals("/findbyid")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Digite o ID:"));
                    mensagem = command;
                }
                if(command.equals("/findbyid")&&!command.equals(mensagem)) {
                    Item item = itemCommandController.itemByID(mensagem);
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
                    List<Item> itens = itemCommandController.itemByName(mensagem);
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
                    List<Item> itens = itemCommandController.itemByCategory(mensagem);
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
                    List<Item> itens = itemCommandController.itemByLocation(mensagem);
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
                    List<Item> itens = itemCommandController.itemByDescription(mensagem);
                    for (Item i:
                            itens) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                    }
                    command = "";
                }
                //######################################################################################################

                //Método para adicionar um item
                if(update.message().text().equals("/post")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Digite as atributos no seguinte formato:"));
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Local,Categoria,Item,Descrição"));
                    mensagem = command;
                }

                if(command.equals("/post")&&!command.equals(mensagem)) {
                    itemCommandController.itemPost(mensagem);
                    command = "";
                }
                //######################################################################################################

                //Método para gerar um csv
                if(update.message().text().equals("/generatecsv")) {
                    List<Item> itens = itemCommandController.listallItems();
                    //TODO RESTO DA FUNÇÂO PARA GERAR CSV
                }

                //TODO REQUESTS RELACIONADOS A LOCALIZAÇÔES

                //Método para listar as localizações cadastradas
                if(update.message().text().equals("/locations")) {
                    List<Location> locations = locationController.listallLocations();
                    for (Location i:
                            locations) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                    }
                    command = "";
                }


                //######################################################################################################
                if(update.message().text().equals("/bylocalid")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Insira o ID do local:\n"));
                    mensagem = command;
                }
                if(command.equals("/bylocalid")&&!command.equals(mensagem)) {
                    Location local = locationController.localByID(update.message().text());
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), local.toString()));
                    command = "";
                }



                //######################################################################################################
                if(update.message().text().equals("/bylocalname")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Insira o nome do local:\n"));
                    mensagem = command;
                }
                if(command.equals("/bylocalname")&&!command.equals(mensagem)) {

                    List<Location> locations = locationController.byLocationName(update.message().text());
                    for (Location i:
                            locations) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                    }
                    command = "";
                }



                //######################################################################################################
                if(update.message().text().equals("/bylocaldescrip")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Insira a descrição do local:\n"));
                    mensagem = command;
                }
                if(command.equals("/bylocaldescrip")&&!command.equals(mensagem)) {

                    List<Location> locations = locationController.byLocationDescription(mensagem);
                    for (Location i:
                            locations) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                    }
                    command = "";
                }

                //TODO REQUESTS RELACIONADOS A CATEGORIAS

                //Método para listar as categorias cadastradas
                if(update.message().text().equals("/category")) {

                    List<Category> categories = categoryController.listallCategories();
                    for (Category i:
                            categories) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                    }
                }

                if(update.message().text().equals("/bycategid")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Insira o ID da categoria:\n"));
                    mensagem = command;
                }
                if(command.equals("/bycategid")&&!command.equals(mensagem)) {
                    Category categorias = categoryController.categoryByID(mensagem);
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), categorias.toString()));
                    command = "";
                }
                //######################################################################################################

                if(update.message().text().equals("/bycategname")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Insira o nome da categoria:\n"));
                    mensagem = command;
                }
                if(command.equals("/bycategname")&&!command.equals(mensagem)) {
                    List<Category> categorias = categoryController.byCategoryName(mensagem);
                    for (Category i:
                            categorias) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                    }
                    command = "";
                }

                if(update.message().text().equals("/bycategdescrip")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Insira o nome da categoria:\n"));
                    mensagem = command;
                }
                if(command.equals("/bycategdescrip")&&!command.equals(mensagem)) {
                    List<Category> categorias = categoryController.byCategoryDescription(mensagem);
                    for (Category i:
                            categorias) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                    }
                    command = "";
                }
            }
        }
    }
}