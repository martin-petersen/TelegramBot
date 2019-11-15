package com.example.demo;

import com.example.demo.controller.*;
import com.example.demo.model.Category;
import com.example.demo.model.Item;
import com.example.demo.model.Location;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
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

        //controle de off-set, isto é, a partir deste ID será lido as mensagens pendentes na fila
        int m = 0;

        String command = "";
        String mensagem;
        LocationCommandController locationController = new LocationCommandController();
        CategoryCommandController categoryController = new CategoryCommandController();
        ItemCommandController itemCommandController = new ItemCommandController();
        CommandNotFound autochat = new CommandNotFound();

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

                bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

                if(update.message().voice() != null) {
                    bot.execute(new SendAudio(update.message().chat().id(),"CQADAQADsQADX8hxRiywc8rRo0w6FgQ"));
                    update.poll();
                    continue;
                }

                if(update.message().photo() != null) {
                    bot.execute(new SendPhoto(update.message().chat().id(),new File("texto-motherfucker.png")));
                    update.poll();
                    continue;
                }

                if(update.message().animation() != null) {
                    bot.execute(new SendAnimation(update.message().chat().id(),new File("texto-motherfucker.gif")));
                    update.poll();
                    continue;
                }

                if(update.message().video() != null) {
                    bot.execute(new SendVideo(update.message().chat().id(),new File("texto-motherfucker.mp4")));
                    update.poll();
                    continue;
                }

                if(update.message().sticker() != null) {
                    bot.execute(new SendSticker(update.message().chat().id(),"CAADAQADAgADRWVwJ3EnrF7KD5K-FgQ"));
                    update.poll();
                    continue;
                }

                if(update.message().audio() != null) {
                    bot.execute(new SendAudio(update.message().chat().id(),"CQADAQADsQADX8hxRiywc8rRo0w6FgQ"));
                    update.poll();
                    continue;
                }

                if(update.message().text().equals("/start")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Seja bem vindo ao Manage UFRN Bot.\n" +
                            "Para utilizar os seviços utilize comandos de texto\n"));
                }

                else if(update.message().text().equals("/help")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "/items - lista todos os itens\n" +
                            "/locations - lista as localizações\n" +
                            "/category - lista as categorias\n" +
                            "/itempost - adiciona um item\n" +
                            "/itemdelete - remove um item\n" +
                            "/itemput - atualiza um item\n" +
                            "/localpost - adiciona um local\n" +
                            "/localdelete - remove um local\n" +
                            "/categorypost - adiciona uma categoria\n" +
                            "/categorydelete - remove uma categoria\n" +
                            "/byitemid - encontra o item pelo id\n" +
                            "/byitemname - busca pelo nome do item\n" +
                            "/byitemtombo - busca pelo tombo do item\n" +
                            "/byitemcategory - busca pela categoria\n" +
                            "/byitemlocation - busca pelo localização\n" +
                            "/byitemdescription - busca pela descrição\n" +
                            "/bylocalid - busca pelo id do local\n" +
                            "/bylocalname - busca pelo nome do local\n" +
                            "/bylocaldescrip - busca pela descrição do local\n" +
                            "/bycategid - busca pelo id da categoria\n" +
                            "/bycategname - busca pelo nome da categoria\n" +
                            "/bycategdescrip - busca pela descrição da categoria\n" +
                            "/exportcsv - gerar CSV com itens, locais e categorias"));
                }

                //TODO REQUESTS RELACIONADOS A ITENS

                //Método para listar tudo
                else if (update.message().text().equals("/items")) {
                    List<Item> itens = itemCommandController.listallItems();
                    if(itens.isEmpty()) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Não há itens cadastrados"));
                    } else {
                        for (Item i:
                                itens) {
                            bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    command = "";
                }
                //######################################################################################################

                //Método para buscar por ID
                else if(update.message().text().equals("/byitemid")) {
                    command = update.message().text();
                    bot.execute(new SendMessage(update.message().chat().id(), "Digite o ID:"));
                    mensagem = command;
                }

                else if(command.equals("/byitemid")&&!command.equals(mensagem)) {
                    Item item = itemCommandController.itemByID(mensagem);
                    if(item == null) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Item não encontrado"));
                    } else {
                        bot.execute(new SendMessage(update.message().chat().id(), item.toString()));
                    }
                    command = "";
                }

                //######################################################################################################

                //Método para buscar pelo tombo
                else if(update.message().text().equals("/byitemtombo")) {
                    command = update.message().text();
                    bot.execute(new SendMessage(update.message().chat().id(), "Digite o Tombo:"));
                    mensagem = command;
                }

                else if(command.equals("/byitemtombo")&&!command.equals(mensagem)) {
                    Item item = itemCommandController.itemByTombo(mensagem);
                    if(item == null) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Item não encontrado"));
                    } else {
                        bot.execute(new SendMessage(update.message().chat().id(), item.toString()));
                    }
                    command = "";
                }

                //######################################################################################################

                //Método para buscar pelo nome do item
                else if(update.message().text().equals("/byitemname")) {
                    command = update.message().text();
                    bot.execute(new SendMessage(update.message().chat().id(), "Digite o nome do item:"));
                    mensagem = command;
                }
                else if(command.equals("/byitemname")&&!command.equals(mensagem)) {
                    List<Item> itens = itemCommandController.itemByName(mensagem);
                    if(itens.isEmpty()) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Não há itens cadastrados com esse nome"));
                    } else {
                        for (Item i:
                                itens) {
                            bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    command = "";
                }
                //######################################################################################################

                //Método para buscar pela categoria do item
                else if(update.message().text().equals("/byitemcategory")) {
                    command = update.message().text();
                    bot.execute(new SendMessage(update.message().chat().id(), "Digite a categoria:"));
                    mensagem = command;
                }
                else if(command.equals("/byitemcategory")&&!command.equals(mensagem)) {
                    List<Item> itens = itemCommandController.itemByCategory(mensagem);
                    if(itens.isEmpty()) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Não há itens cadastrados com essa categoria"));
                    } else {
                        for (Item i:
                                itens) {
                            bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    command = "";
                }
                //######################################################################################################

                //Método para buscar pela localização
                else if(update.message().text().equals("/byitemlocation")) {
                    command = update.message().text();
                    bot.execute(new SendMessage(update.message().chat().id(), "Digite a localização:"));
                    mensagem = command;
                }
                else if(command.equals("/byitemlocation")&&!command.equals(mensagem)) {
                    List<Item> itens = itemCommandController.itemByLocation(mensagem);
                    if(itens.isEmpty()) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Não há itens cadastrados para esse local"));
                    } else {
                        for (Item i:
                                itens) {
                            bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    command = "";
                }
                //######################################################################################################

                //Método para busca pela descrição do item
                else if(update.message().text().equals("/byitemdescription")) {
                    command = update.message().text();
                    bot.execute(new SendMessage(update.message().chat().id(), "Digite a descrição:"));
                    mensagem = command;
                }
                else if(command.equals("/byitemdescription")&&!command.equals(mensagem)) {
                    List<Item> itens = itemCommandController.itemByDescription(mensagem);
                    if(itens.isEmpty()) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Não há itens cadastrados com essa descrição"));
                    } else {
                        for (Item i:
                                itens) {
                            bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    command = "";
                }
                //######################################################################################################

                //Método para adicionar um item

                if(update.message().text().equals("/itempost")) {
                    command = update.message().text();
                    bot.execute(new SendMessage(update.message().chat().id(), "Digite os atributos no seguinte formato:" + "\n" + "Local,Categoria,Item,Tombo,Descrição"));
                    mensagem = command;
                }

                if(command.equals("/itempost")&&!command.equals(mensagem)) {
                    String[] itemAtributes = mensagem.split(",");
                    Location local = new Location();
                    local.setLocation(itemAtributes[0]);
                    Category categoria = new Category();
                    categoria.setCategory(itemAtributes[1]);
                    Item item = new Item(local,categoria,itemAtributes[2],itemAtributes[4]);
                    item.setTombo(Integer.parseInt(itemAtributes[3]));
                    String response = itemCommandController.PostItem(item);
                    bot.execute(new SendMessage(update.message().chat().id(), response));

                    command = "";
                }
                //Método para atualizar um item
                if(update.message().text().equals("/itemput")) {
                    command = update.message().text();
                    bot.execute(new SendMessage(update.message().chat().id(), "Digite os atributos no seguinte formato:" + "\n" + "ID,Local,Categoria,Item,Tombo,Descrição"));
                    mensagem = command;
                }

                else if(command.equals("/itemput")&&!command.equals(mensagem)) {
                    String[] itemAtributes = mensagem.split(",");
                    if(itemAtributes.length != 6) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Não foi possível realizar a operação"));
                        continue;
                    }
                    Location local = new Location();
                    local.setLocation(itemAtributes[1]);
                    Category categoria = new Category();
                    categoria.setCategory(itemAtributes[2]);
                    Item item = new Item(local,categoria,itemAtributes[3],itemAtributes[5]);
                    item.setId(Integer.parseInt(itemAtributes[0]));
                    item.setTombo(Integer.parseInt(itemAtributes[4]));
                    String response = itemCommandController.PutItem(item);
                    bot.execute(new SendMessage(update.message().chat().id(), response));

                    command = "";
                }
                //######################################################################################################

                //Método para gerar um csv
                else if(update.message().text().equals("/exportcsv")) {
                    itemCommandController.CSV(itemCommandController.listallItems());
                    bot.execute(new SendDocument(update.message().chat().id(), new File("items.csv")));
                }

                //TODO REQUESTS RELACIONADOS A LOCALIZAÇÔES

                //######################################################################################################
                //Método para listar as localizações cadastradas
                else if(update.message().text().equals("/locations")) {
                    List<Location> locations = locationController.listallLocations();
                    if(locations.isEmpty()) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Não há locais cadastrados"));
                    } else {
                        for (Location i:
                                locations) {
                            bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    command = "";
                }


                //######################################################################################################
                else if(update.message().text().equals("/bylocalid")) {
                    command = update.message().text();
                    bot.execute(new SendMessage(update.message().chat().id(), "Insira o ID do local:\n"));
                    mensagem = command;
                }
                else if(command.equals("/bylocalid")&&!command.equals(mensagem)) {
                    Location local = locationController.localByID(update.message().text());
                    if(local == null) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Local não encontrado"));
                    } else {
                        bot.execute(new SendMessage(update.message().chat().id(), local.toString()));
                    }
                    command = "";
                }



                //######################################################################################################
                else if(update.message().text().equals("/bylocalname")) {
                    command = update.message().text();
                    bot.execute(new SendMessage(update.message().chat().id(), "Insira o nome do local:\n"));
                    mensagem = command;
                }
                else if(command.equals("/bylocalname")&&!command.equals(mensagem)) {
                    Location local = locationController.byLocationName(update.message().text());
                    if(local == null) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Local não encontrado"));
                    } else {
                        bot.execute(new SendMessage(update.message().chat().id(), local.toString()));
                    }

                    command = "";
                }



                //######################################################################################################
                else if(update.message().text().equals("/bylocaldescrip")) {
                    command = update.message().text();
                    bot.execute(new SendMessage(update.message().chat().id(), "Insira a descrição do local:\n"));
                    mensagem = command;
                }
                else if(command.equals("/bylocaldescrip")&&!command.equals(mensagem)) {
                    List<Location> locations = locationController.byLocationDescription(mensagem);
                    if(locations.isEmpty()) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Não há locais cadastrados com essa descrição"));
                    } else {
                        for (Location i:
                                locations) {
                            bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    command = "";
                }

                //######################################################################################################
                else if(update.message().text().equals("/localpost")) {
                    command = update.message().text();
                    bot.execute(new SendMessage(update.message().chat().id(), "Digite os atributos no seguinte formato:" + "\n" + "Local,Descrição"));
                    mensagem = command;
                }

                else if(command.equals("/localpost")&&!command.equals(mensagem)) {
                    String[] localAtributes = mensagem.split(",");
                    Location local = new Location(localAtributes[0],localAtributes[1]);
                    String response = locationController.PostLocation(local);
                    bot.execute(new SendMessage(update.message().chat().id(), response));
                    command = "";
                }

                //######################################################################################################
                else if(update.message().text().equals("/localdelete")) {
                    command = update.message().text();
                    bot.execute(new SendMessage(update.message().chat().id(), "Insira o ID do local:\n"));
                    mensagem = command;
                }
                else if(command.equals("/localdelete")&&!command.equals(mensagem)) {
                    String response = locationController.DeleteLocal(mensagem);
                    bot.execute(new SendMessage(update.message().chat().id(), response));
                    command = "";
                }

                //TODO REQUESTS RELACIONADOS A CATEGORIAS

                //######################################################################################################
                //Método para listar as categorias cadastradas
                else if(update.message().text().equals("/category")) {
                    List<Category> categories = categoryController.listallCategories();
                    if(categories.isEmpty()) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Não há categorias cadastradas"));
                    } else {
                        for (Category i:
                                categories) {
                            bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                }

                //######################################################################################################
                else if(update.message().text().equals("/bycategid")) {
                    command = update.message().text();
                    bot.execute(new SendMessage(update.message().chat().id(), "Insira o ID da categoria:\n"));
                    mensagem = command;
                }
                else if(command.equals("/bycategid")&&!command.equals(mensagem)) {
                    Category categorias = categoryController.categoryByID(mensagem);
                    if(categorias == null) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Categoria não encontrada"));
                    } else {
                        bot.execute(new SendMessage(update.message().chat().id(), categorias.toString()));
                    }
                    command = "";
                }
                //######################################################################################################

                else if(update.message().text().equals("/bycategname")) {
                    command = update.message().text();
                    bot.execute(new SendMessage(update.message().chat().id(), "Insira o nome da categoria:\n"));
                    mensagem = command;
                }
                else if(command.equals("/bycategname")&&!command.equals(mensagem)) {
                    Category categorias = categoryController.byCategoryName(mensagem);
                    if(categorias == null) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Categoria não encontrada"));
                    } else {
                        bot.execute(new SendMessage(update.message().chat().id(), categorias.toString()));
                    }

                    command = "";
                }

                //######################################################################################################
                else if(update.message().text().equals("/bycategdescrip")) {
                    command = update.message().text();
                    bot.execute(new SendMessage(update.message().chat().id(), "Insira o nome da categoria:\n"));
                    mensagem = command;
                }
                else if(command.equals("/bycategdescrip")&&!command.equals(mensagem)) {
                    List<Category> categories = categoryController.byCategoryDescription(mensagem);
                    if(categories.isEmpty()) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Não há categorias cadastradas"));
                    } else {
                        for (Category i:
                                categories) {
                            bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    command = "";
                }

                //######################################################################################################
                else if(update.message().text().equals("/categorypost")) {
                    command = update.message().text();
                    bot.execute(new SendMessage(update.message().chat().id(), "Digite os atributos no seguinte formato:" + "\n" + "Categoria,Descrição"));
                    mensagem = command;
                }

                else if(command.equals("/categorypost")&&!command.equals(mensagem)) {
                    String[] categoryAtributes = mensagem.split(",");
                    Category category = new Category(categoryAtributes[0],categoryAtributes[1]);
                    String response = categoryController.PostCategory(category);
                    bot.execute(new SendMessage(update.message().chat().id(), response));
                    command = "";
                }

                //######################################################################################################
                else if(update.message().text().equals("/categorydelete")) {
                    command = update.message().text();
                    bot.execute(new SendMessage(update.message().chat().id(), "Insira o ID da categoria:\n"));
                    mensagem = command;
                }
                else if(command.equals("/categorydelete")&&!command.equals(mensagem)) {
                    String response = categoryController.DeleteCategoria(mensagem);
                    bot.execute(new SendMessage(update.message().chat().id(), response));
                    command = "";
                }

                else if(update.message().text().equals("/itemdelete")) {
                    command = update.message().text();
                    bot.execute(new SendMessage(update.message().chat().id(), "Insira o ID do item:\n"));
                    mensagem = command;
                }
                else if(command.equals("/itemdelete")&&!command.equals(mensagem)) {
                    String response = itemCommandController.DeleteItem(mensagem);
                    bot.execute(new SendMessage(update.message().chat().id(), response));
                    command = "";
                }

                autochat.setCommand(command);
                 if(!autochat.commandNotFound()) {
                    System.out.println();
                    bot.execute(new SendMessage(update.message().chat().id(), "Ops! Sua mensagem não corresponde a nenhum dos comandos" + "\n" + "Aguardo um comando para agir..."));
                }
            }
        }
    }
}