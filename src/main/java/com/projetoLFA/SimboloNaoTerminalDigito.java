package com.projetoLFA;

public class SimboloNaoTerminalDigito extends SimboloNaoTerminal{
    
    private Integer digito;

    public SimboloNaoTerminalDigito(Character valor, Integer digito){
        super(valor);
        this.digito = digito; 
    }

    public SimboloNaoTerminalDigito(SimboloNaoTerminalDigito umSimbolo){
        super(umSimbolo.valor);
        this.digito = umSimbolo.digito;
    }

    @Override
    public String getValor(){
        return Character.toString(valor) + Integer.toString(digito);
    }

}
