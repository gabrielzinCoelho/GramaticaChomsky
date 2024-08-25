package com.projetoLFA;

public class SimboloNaoTerminal extends Simbolo{

    public static boolean ehSimboloNaoTerminal(Object objeto){
        return objeto instanceof SimboloNaoTerminal;
    }
    
    public SimboloNaoTerminal(Character valor){
        super(valor);
    }

    public SimboloNaoTerminal(SimboloNaoTerminal umSimbolo){
        super(umSimbolo.valor);
    }

}
