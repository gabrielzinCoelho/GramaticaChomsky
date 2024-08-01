package com.projetoLFA;

public class SimboloTerminal extends Simbolo {
    
    public SimboloTerminal(char valor){
        super(valor);
    }

    public String getValor(){
        return Character.toString(valor);
    }

    @Override
    public SimboloTerminal clone(){
        return (SimboloTerminal)super.clone();
    }

}
