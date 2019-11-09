package com.example.demo;

import com.example.demo.controller.CategoryCommandController;
import com.example.demo.controller.CommandNotFound;
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
                autochat.setCommand(update.message().text());

                baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

                //TODO MENSAGEM DE BOASVINDAS DO COMANDO START

                if(update.message().text().equals("/start")) {
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Seja bem vindo ao Manage UFRN Bot"));
                }

                if(!autochat.commandNotFound() && command.equals("")) {
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Ops! Sua mensagem não corresponde a nenhum dos comandos" + "\n" + "Aguardo um comando para agir..."));
                }

                if(update.message().text().equals("/help")) {
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "/items - lista todos os itens\n" +
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
                            "/byitemcategory - busca pela categoria\n" +
                            "/byitemlocation - busca pelo localização\n" +
                            "/byitemdescription - busca pela descrição\n" +
                            "/bylocalid - busca pelo id do local\n" +
                            "/bylocalname - busca pelo nome do local\n" +
                            "/bylocaldescrip - busca pela descrição do local\n" +
                            "/bycategid - busca pelo id da categoria\n" +
                            "/bycategname - busca pelo nome da categoria\n" +
                            "/bycategdescrip - busca pela descrição da categoria\n" +
                            "/importcsv - adicionar itens, locais e categorias a partir de um CSV\n" +
                            "/exportcsv - gerar CSV com itens, locais e categorias"));
                }

                //TODO REQUESTS RELACIONADOS A ITENS

                //Método para listar tudo
                if (update.message().text().equals("/items")) {
                    List<Item> itens = itemCommandController.listallItems();
                    if(itens.isEmpty()) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Não há itens cadastrados"));
                    } else {
                        for (Item i:
                                itens) {
                            sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    command = "";
                }
                //######################################################################################################

                //Método para buscar por ID
                if(update.message().text().equals("/byitemid")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Digite o ID:"));
                    mensagem = command;
                }
                if(command.equals("/byitemid")&&!command.equals(mensagem)) {
                    Item item = itemCommandController.itemByID(mensagem);
                    if(item == null) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Item não encontrado"));
                    } else {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), item.toString()));
                    }
                    command = "";
                }
                //######################################################################################################

                //Método para buscar pelo nome do item
                if(update.message().text().equals("/byitemname")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Digite o nome do item:"));
                    mensagem = command;
                }
                if(command.equals("/byitemname")&&!command.equals(mensagem)) {
                    List<Item> itens = itemCommandController.itemByName(mensagem);
                    if(itens.isEmpty()) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Não há itens cadastrados com esse nome"));
                    } else {
                        for (Item i:
                                itens) {
                            sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    command = "";
                }
                //######################################################################################################

                //Método para buscar pela categoria do item
                if(update.message().text().equals("/byitemcategory")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Digite a categoria:"));
                    mensagem = command;
                }
                if(command.equals("/byitemcategory")&&!command.equals(mensagem)) {
                    List<Item> itens = itemCommandController.itemByCategory(mensagem);
                    if(itens.isEmpty()) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Não há itens cadastrados com essa categoria"));
                    } else {
                        for (Item i:
                                itens) {
                            sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    command = "";
                }
                //######################################################################################################

                //Método para buscar pela localização
                if(update.message().text().equals("/byitemlocation")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Digite a localização:"));
                    mensagem = command;
                }
                if(command.equals("/byitemlocation")&&!command.equals(mensagem)) {
                    List<Item> itens = itemCommandController.itemByLocation(mensagem);
                    if(itens.isEmpty()) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Não há itens cadastrados para esse local"));
                    } else {
                        for (Item i:
                                itens) {
                            sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    command = "";
                }
                //######################################################################################################

                //Método para busca pela descrição do item
                if(update.message().text().equals("/byitemdescription")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Digite a descrição:"));
                    mensagem = command;
                }
                if(command.equals("/byitemdescription")&&!command.equals(mensagem)) {
                    List<Item> itens = itemCommandController.itemByDescription(mensagem);
                    if(itens.isEmpty()) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Não há itens cadastrados com essa descrição"));
                    } else {
                        for (Item i:
                                itens) {
                            sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    command = "";
                }
                //######################################################################################################

                //Método para adicionar um item
                if(update.message().text().equals("/itempost")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Digite os atributos no seguinte formato:" + "\n" + "ID,Local,Categoria,Item,Descrição"));
                    mensagem = command;
                }

                if(command.equals("/itempost")&&!command.equals(mensagem)) {
                    String[] itemAtributes = mensagem.split(",");
                    Location local = new Location();
                    local.setLocation(itemAtributes[0]);
                    Category categoria = new Category();
                    categoria.setCategory(itemAtributes[1]);
                    Item item = new Item(local,categoria,itemAtributes[2],itemAtributes[3]);
                    String response = itemCommandController.PostItem(item);
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), response));

                    command = "";
                }
                //Método para atualizar um item
                if(update.message().text().equals("/itemput")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Digite os atributos no seguinte formato:" + "\n" + "ID,Local,Categoria,Item,Descrição"));
                    mensagem = command;
                }

                if(command.equals("/itemput")&&!command.equals(mensagem)) {
                    String[] itemAtributes = mensagem.split(",");
                    Location local = new Location();
                    local.setLocation(itemAtributes[1]);
                    Category categoria = new Category();
                    categoria.setCategory(itemAtributes[2]);
                    Item item = new Item(local,categoria,itemAtributes[3],itemAtributes[4]);
                    item.setId(Integer.parseInt(itemAtributes[0]));
                    String response = itemCommandController.PutItem(item);
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), response));

                    command = "";
                }
                //######################################################################################################

                //Método para gerar um csv
                if(update.message().text().equals("/exportcsv")) {
                    //TODO FUNÇÂO PARA GERAR CSV
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Em breve!"));
                }

                if(update.message().text().equals("/importcsv")) {
                    //TODO RESTO DA FUNÇÂO PARA IMPORTAR DADOS DE UM CSV
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Em breve!"));
                }

                //TODO REQUESTS RELACIONADOS A LOCALIZAÇÔES

                //Método para listar as localizações cadastradas
                if(update.message().text().equals("/locations")) {
                    List<Location> locations = locationController.listallLocations();
                    if(locations.isEmpty()) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Não há locais cadastrados"));
                    } else {
                        for (Location i:
                                locations) {
                            sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
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
                    if(local == null) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Local não encontrado"));
                    } else {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), local.toString()));
                    }
                    command = "";
                }



                //######################################################################################################
                if(update.message().text().equals("/bylocalname")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Insira o nome do local:\n"));
                    mensagem = command;
                }
                if(command.equals("/bylocalname")&&!command.equals(mensagem)) {
                    Location local = locationController.byLocationName(update.message().text());
                    if(local == null) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Local não encontrado"));
                    } else {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), local.toString()));
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
                    if(locations.isEmpty()) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Não há locais cadastrados com essa descrição"));
                    } else {
                        for (Location i:
                                locations) {
                            sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    command = "";
                }

                if(update.message().text().equals("/localpost")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Digite os atributos no seguinte formato:" + "\n" + "Local,Descrição"));
                    mensagem = command;
                }

                if(command.equals("/localpost")&&!command.equals(mensagem)) {
                    String[] localAtributes = mensagem.split(",");
                    Location local = new Location(localAtributes[0],localAtributes[1]);
                    String response = locationController.PostLocation(local);
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), response));
                    command = "";
                }

                if(update.message().text().equals("/localdelete")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Insira o ID do local:\n"));
                    mensagem = command;
                }
                if(command.equals("/localdelete")&&!command.equals(mensagem)) {
                    String response = locationController.DeleteLocal(mensagem);
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), response));
                    command = "";
                }

                //TODO REQUESTS RELACIONADOS A CATEGORIAS

                //Método para listar as categorias cadastradas
                if(update.message().text().equals("/category")) {
                    List<Category> categories = categoryController.listallCategories();
                    if(categories.isEmpty()) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Não há categorias cadastradas"));
                    } else {
                        for (Category i:
                                categories) {
                            sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                }

                if(update.message().text().equals("/bycategid")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Insira o ID da categoria:\n"));
                    mensagem = command;
                }
                if(command.equals("/bycategid")&&!command.equals(mensagem)) {
                    Category categorias = categoryController.categoryByID(mensagem);
                    if(categorias == null) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Categoria não encontrada"));
                    } else {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), categorias.toString()));
                    }
                    command = "";
                }
                //######################################################################################################

                if(update.message().text().equals("/bycategname")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Insira o nome da categoria:\n"));
                    mensagem = command;
                }
                if(command.equals("/bycategname")&&!command.equals(mensagem)) {
                    Category categorias = categoryController.byCategoryName(mensagem);
                    if(categorias == null) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Categoria não encontrada"));
                    } else {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), categorias.toString()));
                    }

                    command = "";
                }

                if(update.message().text().equals("/bycategdescrip")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Insira o nome da categoria:\n"));
                    mensagem = command;
                }
                if(command.equals("/bycategdescrip")&&!command.equals(mensagem)) {
                    List<Category> categories = categoryController.byCategoryDescription(mensagem);
                    if(categories.isEmpty()) {
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Não há categorias cadastradas"));
                    } else {
                        for (Category i:
                                categories) {
                            sendResponse = bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    command = "";
                }

                if(update.message().text().equals("/categorypost")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Digite os atributos no seguinte formato:" + "\n" + "Categoria,Descrição"));
                    mensagem = command;
                }

                if(command.equals("/categorypost")&&!command.equals(mensagem)) {
                    String[] categoryAtributes = mensagem.split(",");
                    Category category = new Category(categoryAtributes[0],categoryAtributes[1]);
                    String response = categoryController.PostCategory(category);
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), response));
                    command = "";
                }

                if(update.message().text().equals("/categorydelete")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Insira o ID da categoria:\n"));
                    mensagem = command;
                }
                if(command.equals("/categorydelete")&&!command.equals(mensagem)) {
                    String response = categoryController.DeleteCategoria(mensagem);
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), response));
                    command = "";
                }

                if(update.message().text().equals("/itemdelete")) {
                    command = update.message().text();
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Insira o ID do item:\n"));
                    mensagem = command;
                }
                if(command.equals("/itemdelete")&&!command.equals(mensagem)) {
                    String response = itemCommandController.DeleteItem(mensagem);
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), response));
                    command = "";
                }
            }
        }
    }
}