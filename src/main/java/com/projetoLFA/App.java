package com.projetoLFA;

public class App 
{
    public static void main(String[] args){

        String textoGramatica = "S->aS|bS|C|D\nC->c|.\nD->abc\nD->.";
        // String textoGramatica = "S->aA|bAB\nA->abC|.\nB->Ac|AA\nC->aBC|aB";
        Gramatica gramatica = new Gramatica(textoGramatica);
        GramaticaChomsky gramaticaChomsky = new GramaticaChomsky(gramatica);
        System.out.println(gramaticaChomsky);

    }
}
