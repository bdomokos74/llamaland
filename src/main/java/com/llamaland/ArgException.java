package com.llamaland;

public class ArgException extends Exception{
    public ArgException(String message) {
        super(message);
    }

    public void printMessage() {
        System.out.println(getMessage());
    }
}
