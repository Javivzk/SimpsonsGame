package com.svalero.juego;

public class Main {
    public static void main (String [] args ){
        System.out.println("The Simpsons Game");
        Tablero juego2 = new Tablero();
        juego2.configuraPartida();
        juego2.gameStart();
    }
}
