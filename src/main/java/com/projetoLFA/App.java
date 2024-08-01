package com.projetoLFA;

public class App 
{
    public static void main(String[] args){

        // String textoGramatica = "S->aS|aS|bS|C|D\nC->c|.\nD->abc\nD->.";
        // String textoGramatica = "S->aA|bAB\nA->abC|.\nB->Ac|AA\nC->aBC|aB";
        // Gramatica gramatica = new Gramatica(textoGramatica);
        // GramaticaChomsky gramaticaChomsky = new GramaticaChomsky(gramatica.copiaGramatica());
        // System.out.println(gramaticaChomsky);

        SimboloNaoTerminal test = (SimboloNaoTerminal) Simbolo.criarSimbolo("A1");
        SimboloNaoTerminal test2 = (SimboloNaoTerminal) Simbolo.criarSimbolo("A2");

        System.out.println(test.equals(test2));

    }
}
