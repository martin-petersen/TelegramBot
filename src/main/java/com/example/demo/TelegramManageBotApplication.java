package com.example.demo;

import com.example.demo.controller.*;
import com.example.demo.model.Category;
import com.example.demo.model.Item;
import com.example.demo.model.Location;
import com.example.demo.user.User;
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
import java.util.TreeMap;

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

        LocationCommandController locationController = new LocationCommandController();
        CategoryCommandController categoryController = new CategoryCommandController();
        ItemCommandController itemCommandController = new ItemCommandController();

        //loop infinito pode ser alterado por algum timer de intervalo curto
        while (true) {

            //executa comando no Telegram para obter as mensagens pendentes a partir de um off-set (limite inicial)
            updatesResponse = bot.execute(new GetUpdates().limit(100).offset(m));

            TreeMap Users = new TreeMap();
            User usuario = null;

            //lista de mensagens
            List<Update> updates = updatesResponse.updates();

//            análise de cada ação da mensagem
            for (Update update : updates) {
                if(!Users.containsKey(update.message().chat().id())) {
                    Users.put(update.message().chat().id(), new User(update.message().chat().firstName()));
                }
                usuario = (User) Users.get(update.message().chat().id());
                //atualização do off-set
                m = update.updateId() + 1;
                usuario.setComandos(update.message().text());

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

                if(usuario.getComandos()[0].equals("/start")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Seja bem vindo ao Manage UFRN Bot " +
                            usuario.getNome() +
                            "Para utilizar os seviços utilize comandos de texto\n"));
                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                    continue;
                }

                else if(usuario.getComandos()[0].equals("/help")) {
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
                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                    continue;
                }

                //TODO REQUESTS RELACIONADOS A ITENS

                //Método para listar tudo
                else if (usuario.getComandos()[0].equals("/items")) {
                    List<Item> itens = itemCommandController.listallItems();
                    if(itens.isEmpty()) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Não há itens cadastrados"));
                    } else {
                        for (Item i:
                                itens) {
                            bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                }
                //######################################################################################################

                //Método para buscar por ID
                else if(usuario.getComandos()[0].equals("/byitemid")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Digite o ID:"));
                    usuario.setComandos(update.message().text());
                    Users.replace(update.message().chat().id(),usuario);
                }

                else if(usuario.getComandos()[0].equals("/byitemid") && usuario.getComandos()[1] != null) {
                    Item item = itemCommandController.itemByID(usuario.getComandos()[1]);
                    if(item == null) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Item não encontrado"));
                    } else {
                        bot.execute(new SendMessage(update.message().chat().id(), item.toString()));
                    }
                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                }

                //######################################################################################################

                //Método para buscar pelo tombo
                else if(usuario.getComandos()[0].equals("/byitemtombo")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Digite o Tombo:"));
                    usuario.setComandos(update.message().text());
                    Users.replace(update.message().chat().id(),usuario);
                }

                else if(usuario.getComandos()[0].equals("/byitemtombo") && usuario.getComandos()[1] != null) {
                    Item item = itemCommandController.itemByTombo(usuario.getComandos()[1]);
                    if(item == null) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Item não encontrado"));
                    } else {
                        bot.execute(new SendMessage(update.message().chat().id(), item.toString()));
                    }
                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                }

                //######################################################################################################

                //Método para buscar pelo nome do item
                else if(usuario.getComandos()[0].equals("/byitemname")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Digite o nome do item:"));
                    usuario.setComandos(update.message().text());
                    Users.replace(update.message().chat().id(),usuario);
                }
                else if(usuario.getComandos()[0].equals("/byitemname") && usuario.getComandos()[1] != null) {
                    List<Item> itens = itemCommandController.itemByName(usuario.getComandos()[1]);
                    if(itens.isEmpty()) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Não há itens cadastrados com esse nome"));
                    } else {
                        for (Item i:
                                itens) {
                            bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                }
                //######################################################################################################

                //Método para buscar pela categoria do item
                else if(usuario.getComandos()[0].equals("/byitemcategory")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Digite a categoria:"));
                    usuario.setComandos(update.message().text());
                    Users.replace(update.message().chat().id(),usuario);
                }
                else if(usuario.getComandos()[0].equals("/byitemcategory") && usuario.getComandos()[1] != null) {
                    List<Item> itens = itemCommandController.itemByCategory(usuario.getComandos()[1]);
                    if(itens.isEmpty()) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Não há itens cadastrados com essa categoria"));
                    } else {
                        for (Item i:
                                itens) {
                            bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                }
                //######################################################################################################

                //Método para buscar pela localização
                else if(usuario.getComandos()[0].equals("/byitemlocation")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Digite a localização:"));
                    usuario.setComandos(update.message().text());
                    Users.replace(update.message().chat().id(),usuario);
                }
                else if(usuario.getComandos()[0].equals("/byitemlocation") && usuario.getComandos()[1] != null) {
                    List<Item> itens = itemCommandController.itemByLocation(usuario.getComandos()[1]);
                    if(itens.isEmpty()) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Não há itens cadastrados para esse local"));
                    } else {
                        for (Item i:
                                itens) {
                            bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                }
                //######################################################################################################

                //Método para busca pela descrição do item
                else if(usuario.getComandos()[0].equals("/byitemdescription")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Digite a descrição:"));
                    usuario.setComandos(update.message().text());
                    Users.replace(update.message().chat().id(),usuario);
                }
                else if(usuario.getComandos()[0].equals("/byitemdescription") && usuario.getComandos()[1] != null) {
                    List<Item> itens = itemCommandController.itemByDescription(usuario.getComandos()[1]);
                    if(itens.isEmpty()) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Não há itens cadastrados com essa descrição"));
                    } else {
                        for (Item i:
                                itens) {
                            bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                }
                //######################################################################################################

                //Método para adicionar um item

                if(update.message().text().equals("/itempost")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Digite os atributos no seguinte formato:" + "\n" + "Local,Categoria,Item,Tombo,Descrição"));
                    usuario.setComandos(update.message().text());
                    Users.replace(update.message().chat().id(),usuario);
                }

                if(usuario.getComandos()[0].equals("/itempost") && usuario.getComandos()[1] != null) {
                    String[] itemAtributes = usuario.getComandos()[1].split(",");
                    Location local = new Location();
                    local.setLocation(itemAtributes[0]);
                    Category categoria = new Category();
                    categoria.setCategory(itemAtributes[1]);
                    Item item = new Item(local,categoria,itemAtributes[2],itemAtributes[4]);
                    item.setTombo(Integer.parseInt(itemAtributes[3]));
                    String response = itemCommandController.PostItem(item);
                    bot.execute(new SendMessage(update.message().chat().id(), response));

                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                }
                //Método para atualizar um item
                if(usuario.getComandos()[0].equals("/itemput")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Digite os atributos no seguinte formato:" + "\n" + "ID,Local,Categoria,Item,Tombo,Descrição"));
                    usuario.setComandos(update.message().text());
                    Users.replace(update.message().chat().id(),usuario);
                }

                else if(usuario.getComandos()[0].equals("/itemput") && usuario.getComandos()[1] != null) {
                    String[] itemAtributes = usuario.getComandos()[1].split(",");
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

                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                }
                //######################################################################################################

                else if(usuario.getComandos()[0].equals("/itemdelete")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Insira o ID do item:\n"));
                    usuario.setComandos(update.message().text());
                    Users.replace(update.message().chat().id(),usuario);
                }
                else if(usuario.getComandos()[0].equals("/itemdelete") && usuario.getComandos()[1] != null) {
                    String response = itemCommandController.DeleteItem(usuario.getComandos()[1]);
                    bot.execute(new SendMessage(update.message().chat().id(), response));
                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                }

                //Método para gerar um csv
                else if(usuario.getComandos()[0].equals("/exportcsv")) {
                    itemCommandController.CSV(itemCommandController.listallItems());
                    bot.execute(new SendDocument(update.message().chat().id(), new File("items.csv")));
                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                }

                //TODO REQUESTS RELACIONADOS A LOCALIZAÇÔES

                //######################################################################################################
                //Método para listar as localizações cadastradas
                else if(usuario.getComandos()[0].equals("/locations")) {
                    List<Location> locations = locationController.listallLocations();
                    if(locations.isEmpty()) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Não há locais cadastrados"));
                    } else {
                        for (Location i:
                                locations) {
                            bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                }


                //######################################################################################################
                else if(usuario.getComandos()[0].equals("/bylocalid")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Insira o ID do local:\n"));
                    usuario.setComandos(update.message().text());
                    Users.replace(update.message().chat().id(),usuario);
                }
                else if(usuario.getComandos()[0].equals("/bylocalid") && usuario.getComandos()[1] != null) {
                    Location local = locationController.localByID(usuario.getComandos()[1]);
                    if(local == null) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Local não encontrado"));
                    } else {
                        bot.execute(new SendMessage(update.message().chat().id(), local.toString()));
                    }
                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                }



                //######################################################################################################
                else if(usuario.getComandos()[0].equals("/bylocalname")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Insira o nome do local:\n"));
                    usuario.setComandos(update.message().text());
                    Users.replace(update.message().chat().id(),usuario);
                }
                else if(usuario.getComandos()[0].equals("/bylocalname") && usuario.getComandos()[1] != null) {
                    Location local = locationController.byLocationName(usuario.getComandos()[1]);
                    if(local == null) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Local não encontrado"));
                    } else {
                        bot.execute(new SendMessage(update.message().chat().id(), local.toString()));
                    }
                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                }



                //######################################################################################################
                else if(usuario.getComandos()[0].equals("/bylocaldescrip")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Insira a descrição do local:\n"));
                    usuario.setComandos(update.message().text());
                    Users.replace(update.message().chat().id(),usuario);
                }
                else if(usuario.getComandos()[0].equals("/bylocaldescrip") && usuario.getComandos()[1] != null) {
                    List<Location> locations = locationController.byLocationDescription(usuario.getComandos()[1]);
                    if(locations.isEmpty()) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Não há locais cadastrados com essa descrição"));
                    } else {
                        for (Location i:
                                locations) {
                            bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                }

                //######################################################################################################
                else if(usuario.getComandos()[0].equals("/localpost")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Digite os atributos no seguinte formato:" + "\n" + "Local,Descrição"));
                    usuario.setComandos(update.message().text());
                    Users.replace(update.message().chat().id(),usuario);
                }

                else if(usuario.getComandos()[0].equals("/localpost") && usuario.getComandos()[1] != null) {
                    String[] localAtributes = usuario.getComandos()[1].split(",");
                    Location local = new Location(localAtributes[0],localAtributes[1]);
                    String response = locationController.PostLocation(local);
                    bot.execute(new SendMessage(update.message().chat().id(), response));
                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                }

                //######################################################################################################
                else if(usuario.getComandos()[0].equals("/localdelete")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Insira o ID do local:\n"));
                    usuario.setComandos(update.message().text());
                    Users.replace(update.message().chat().id(),usuario);
                }
                else if(usuario.getComandos()[0].equals("/localpost") && usuario.getComandos()[1] != null) {
                    String response = locationController.DeleteLocal(usuario.getComandos()[1]);
                    bot.execute(new SendMessage(update.message().chat().id(), response));
                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                }

                //TODO REQUESTS RELACIONADOS A CATEGORIAS

                //######################################################################################################
                //Método para listar as categorias cadastradas
                else if(usuario.getComandos()[0].equals("/category")) {
                    List<Category> categories = categoryController.listallCategories();
                    if(categories.isEmpty()) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Não há categorias cadastradas"));
                    } else {
                        for (Category i:
                                categories) {
                            bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                }

                //######################################################################################################
                else if(usuario.getComandos()[0].equals("/bycategid")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Insira o ID da categoria:\n"));
                    usuario.setComandos(update.message().text());
                    Users.replace(update.message().chat().id(),usuario);
                }
                else if(usuario.getComandos()[0].equals("/bycategid") && usuario.getComandos()[1] != null) {
                    Category categorias = categoryController.categoryByID(usuario.getComandos()[1]);
                    if(categorias == null) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Categoria não encontrada"));
                    } else {
                        bot.execute(new SendMessage(update.message().chat().id(), categorias.toString()));
                    }
                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                }
                //######################################################################################################

                else if(usuario.getComandos()[0].equals("/bycategname")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Insira o nome da categoria:\n"));
                    usuario.setComandos(update.message().text());
                    Users.replace(update.message().chat().id(),usuario);
                }
                else if(usuario.getComandos()[0].equals("/bycategname") && usuario.getComandos()[1] != null) {
                    Category categorias = categoryController.byCategoryName(usuario.getComandos()[1]);
                    if(categorias == null) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Categoria não encontrada"));
                    } else {
                        bot.execute(new SendMessage(update.message().chat().id(), categorias.toString()));
                    }
                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                }

                //######################################################################################################
                else if(usuario.getComandos()[0].equals("/bycategdescrip")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Insira o nome da categoria:\n"));
                    usuario.setComandos(update.message().text());
                    Users.replace(update.message().chat().id(),usuario);
                }
                else if(usuario.getComandos()[0].equals("/bycategdescrip") && usuario.getComandos()[1] != null) {
                    List<Category> categories = categoryController.byCategoryDescription(usuario.getComandos()[1]);
                    if(categories.isEmpty()) {
                        bot.execute(new SendMessage(update.message().chat().id(), "Não há categorias cadastradas"));
                    } else {
                        for (Category i:
                                categories) {
                            bot.execute(new SendMessage(update.message().chat().id(), i.toString()));
                        }
                    }
                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                }

                //######################################################################################################
                else if(usuario.getComandos()[0].equals("/categorypost")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Digite os atributos no seguinte formato:" + "\n" + "Categoria,Descrição"));
                    usuario.setComandos(update.message().text());
                    Users.replace(update.message().chat().id(),usuario);
                }

                else if(usuario.getComandos()[0].equals("/categorypost") && usuario.getComandos()[1] != null) {
                    String[] categoryAtributes = usuario.getComandos()[1].split(",");
                    Category category = new Category(categoryAtributes[0],categoryAtributes[1]);
                    String response = categoryController.PostCategory(category);
                    bot.execute(new SendMessage(update.message().chat().id(), response));
                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                }

                //######################################################################################################
                else if(usuario.getComandos()[0].equals("/categorydelete")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Insira o ID da categoria:\n"));
                    usuario.setComandos(update.message().text());
                    Users.replace(update.message().chat().id(),usuario);
                }
                else if(usuario.getComandos()[0].equals("/categorydelete") && usuario.getComandos()[1] != null) {
                    String response = categoryController.DeleteCategoria(usuario.getComandos()[1]);
                    bot.execute(new SendMessage(update.message().chat().id(), response));
                    usuario.resetComandos();
                    Users.replace(update.message().chat().id(),usuario);
                }
            }
        }
    }
}