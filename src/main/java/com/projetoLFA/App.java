package com.projetoLFA;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class App 
{
    public static void main(String[] args){

        String textoGramatica = "";
        try {
            File arquivo = new File(args[0]);
            Scanner texto = new Scanner(arquivo);
            while (texto.hasNextLine()) {
                String linha = texto.nextLine();
                linha = linha.replace(" ", "");
                textoGramatica += (linha + "\n");
            }
            texto.close();
        } catch (FileNotFoundException e) {
            System.out.println("Ocorreu um erro.");
            e.printStackTrace();
        }

        Gramatica gramatica = new Gramatica(textoGramatica);
        GramaticaChomsky gramaticaChomsky = new GramaticaChomsky(gramatica);
        System.out.println(gramaticaChomsky);
    }
}
