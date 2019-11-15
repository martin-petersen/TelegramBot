package com.example.demo.controller;

public class CommandNotFound {
    private String command;

    /**
     * Constructor
     */
    public CommandNotFound() {
    }

    /**
     * Get command
     * @return command
     */
    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * Compare if command exist
     * @return boolean
     */
    public boolean commandNotFound() {
        if(!getCommand().equals("/help") && !getCommand().equals("/items")
        && !getCommand().equals("/locations") && !getCommand().equals("/category")
        && !getCommand().equals("/itempost") && !getCommand().equals("/itemdelete")
        && !getCommand().equals("/itemput") && !getCommand().equals("/localpost")
        && !getCommand().equals("/localdelete") && !getCommand().equals("/categorypost")
        && !getCommand().equals("/categorydelete") && !getCommand().equals("/byitemid")
        && !getCommand().equals("/byitemname") && !getCommand().equals("/byitemcategory")
        && !getCommand().equals("/byitemlocation") && !getCommand().equals("/byitemdescription")
        && !getCommand().equals("/bylocalid") && !getCommand().equals("/bylocalname")
        && !getCommand().equals("/bylocaldescrip") && !getCommand().equals("/bycategid")
        && !getCommand().equals("/bycategname") && !getCommand().equals("/bycategdescrip")
        && !getCommand().equals("/byitemtombo") && !getCommand().equals("/exportcsv")
        && !getCommand().equals("/start")) {
            return false;
        }
        return true;
    }
}
