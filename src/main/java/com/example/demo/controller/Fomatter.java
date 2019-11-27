package com.example.demo.controller;

public class Fomatter {

    public Fomatter() {
    }

    public String formatterSpaceEraser(String phrase) {
        String[] splited = phrase.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String i:
             splited) {
            sb.append(i);
            sb.append("-");
        }
        sb.deleteCharAt(sb.toString().lastIndexOf("-"));
        return sb.toString();
    }

    public String formatterHifenEraser(String phrase) {
        String[] splited = phrase.split("-");
        StringBuilder sb = new StringBuilder();
        for (String i:
                splited) {
            sb.append(i);
            sb.append(" ");
        }
        sb.deleteCharAt(sb.toString().lastIndexOf(" "));
        return sb.toString();
    }

    public String[] spliter(String mensagem) {
        String[] splited = mensagem.split(",");
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < splited.length; ++i) {
            sb.append(splited[i]);
            while(sb.toString().charAt(0) == ' ') {
                sb.deleteCharAt(0);
            }
            while(sb.toString().charAt(sb.toString().length()-1) == ' ') {
                sb.deleteCharAt(sb.toString().length()-1);
            }
            splited[i] = sb.toString();
            sb.setLength(0);
        }
        return splited;
    }
}
