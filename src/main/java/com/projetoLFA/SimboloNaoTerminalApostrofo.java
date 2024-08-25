package com.projetoLFA;

public class SimboloNaoTerminalApostrofo extends SimboloNaoTerminal{
    
    public SimboloNaoTerminalApostrofo(Character valor){
        super(valor);
    }

    public SimboloNaoTerminalApostrofo(SimboloNaoTerminalApostrofo umSimbolo){
        super(umSimbolo.valor);
    }

    @Override
    public String getValor(){
        return Character.toString(valor) + "'";
    }

}
