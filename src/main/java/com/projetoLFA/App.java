package com.projetoLFA;

public class App 
{
    public static void main(String[] args){

        String[] textosGramatica = new String[]{
            "S->aS|bS|C|D\nC->c|.\nD->abc\nD->.",
            "S->aA|bAB\nA->abC|.\nB->Ac|AA\nC->aBC|aB",
            "S->ACA|CA|AA|AC|A|C|.\nA->aAa|aa|B|C\nB->bB|b\nC->cC|c\n",
            "S->aS|b|A\nA->aA|a|C\nB->a|b\nC->c|B\nD->dD|B"
        };
        Gramatica gramatica = new Gramatica(textosGramatica[3]);
        GramaticaChomsky gramaticaChomsky = new GramaticaChomsky(gramatica);
        System.out.println(gramaticaChomsky);

    }
}
