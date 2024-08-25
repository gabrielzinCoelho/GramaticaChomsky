package com.projetoLFA;

import java.util.HashMap;

public abstract class Simbolo implements Comparable<Simbolo>{

    protected Character valor;

    public static Simbolo criarSimbolo(String strSimbolo){

        if(strSimbolo.length() == 1){

            Character valor = strSimbolo.charAt(0);

            if(Character.isUpperCase(valor))
                return new SimboloNaoTerminal(valor);
            else
                return new SimboloTerminal(valor);

        }else if(strSimbolo.charAt(1) == '\'')
            return new SimboloNaoTerminalApostrofo(strSimbolo.charAt(0));
        else
            return new SimboloNaoTerminalDigito(strSimbolo.charAt(0), Integer.valueOf(strSimbolo.substring(1)));
        

    }

    protected Simbolo(Character valor){
        this.valor = valor;
    }

    public String getValor(){
        return Character.toString(valor);
    }

    @Override
    public boolean equals(Object objeto){

        if(this == objeto) // ambos possuem a mesma referência
            return true;
        
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        
        Simbolo umSimbolo = (Simbolo) objeto;

        return getValor().equals(umSimbolo.getValor());
    }

    @Override
    public int hashCode(){
        return getValor().hashCode();
    }

    @Override
    public String toString(){
        return getValor();
    }

    static final  HashMap<String, Integer> pesoComparacaoSimbolos = new HashMap<>();
    static final String[] pesosEspeciais = {"S'", "S"};

    static {

        pesoComparacaoSimbolos.put("SimboloTerminal", 10);
        pesoComparacaoSimbolos.put("SimboloNaoTerminal", 11);
        pesoComparacaoSimbolos.put("SimboloNaoTerminalDigito", 12);
        pesoComparacaoSimbolos.put("SimboloNaoTerminalApostrofo", 13);
        
    }


    @Override
    public int compareTo(Simbolo outroSimbolo){

        int pesoSimbolo_1 = -1, pesoSimbolo_2 = -1;

        for (int i = 0; i < pesosEspeciais.length; i++) {
            if(this.getValor().equals(pesosEspeciais[i]))
                pesoSimbolo_1 = i;
            if(outroSimbolo.getValor().equals(pesosEspeciais[i]))
                pesoSimbolo_2 = i;
        }

        if(pesoSimbolo_1 == -1){
            pesoSimbolo_1 = pesoComparacaoSimbolos.get(this.getClass().getSimpleName());
        }
        
        if(pesoSimbolo_2 == -1){
            pesoSimbolo_2 = pesoComparacaoSimbolos.get(outroSimbolo.getClass().getSimpleName());
        }
            
        int valorComparacao = pesoSimbolo_1 - pesoSimbolo_2;
        
        return (valorComparacao != 0 ?
            valorComparacao : 
            toString().compareTo(outroSimbolo.toString()));

    }

}
