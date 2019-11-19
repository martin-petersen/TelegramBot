package com.example.demo.user;

public class User {
    private String nome;
    private String[] comandos = new String[2];

    public User(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public String[] getComandos() {
        return comandos;
    }

    public void setComandos(String comando) {
        if(this.comandos[0] != null) {
            this.comandos[1] = comando;
        } else {
            this.comandos[0] = comando;
        }
    }

    public void resetComandos() {
        this.comandos = new String[2];
        this.comandos[0] = null;
        this.comandos[1] = null;
    }
}
