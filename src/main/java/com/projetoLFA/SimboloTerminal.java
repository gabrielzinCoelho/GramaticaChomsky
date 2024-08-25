package com.projetoLFA;

public class SimboloTerminal extends Simbolo {
    
    public SimboloTerminal(char valor){
        super(valor);
    }

    // construtor de copia
    public SimboloTerminal(SimboloTerminal umSimbolo){
        super(umSimbolo.valor);
    }

}
