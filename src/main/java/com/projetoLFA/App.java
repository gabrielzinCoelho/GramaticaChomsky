package com.projetoLFA;

public class App 
{
    public static void main(String[] args){

        Gramatica gramatica = new Gramatica(GerenciarArquivo.lerEntrada(args[0]));
        GramaticaChomsky gramaticaChomsky = new GramaticaChomsky(gramatica);
        GerenciarArquivo.escreverSaida(args[1], gramaticaChomsky.toString());
    }
}
