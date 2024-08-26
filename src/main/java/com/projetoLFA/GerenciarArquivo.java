package com.projetoLFA;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

public class GerenciarArquivo {

    public static String lerEntrada(String nomeArquivo){
        
        try {
            String textoGramatica = "";
            File arquivo = new File(nomeArquivo);
            Scanner texto = new Scanner(arquivo);
            while (texto.hasNextLine()) {
                String linha = texto.nextLine();
                linha = linha.replace(" ", "");
                textoGramatica += (linha + "\n");
            }
            texto.close();
            return textoGramatica;

        } catch (FileNotFoundException e) {
            System.out.println("Ocorreu um erro.");
            e.printStackTrace();
        }

        return "";
        
    }

    public static void escreverSaida(String nomeArquivo, String textoSaida){
        
        
        try{
            File arquivo = new File(nomeArquivo);
            arquivo.createNewFile();
            FileWriter fileWriter = new FileWriter(arquivo);
            fileWriter.write(textoSaida);
            fileWriter.close();
        }catch(Exception e){

        }

    }

    
}
