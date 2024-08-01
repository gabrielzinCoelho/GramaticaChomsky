package com.projetoLFA;

public class SimboloNaoTerminal extends Simbolo{

    private Character digito;
    
    public SimboloNaoTerminal(Character valor){
        super(valor);
        digito = null;
    }

    public SimboloNaoTerminal(Character valor, Character digito){
        super(valor);
        this.digito = digito; 
    }

    public SimboloNaoTerminal(SimboloNaoTerminal umSimbolo){
        super(umSimbolo.valor);
        this.digito = umSimbolo.digito;
    }

    @Override
    public String getValor(){
        if(digito == null)
            return Character.toString(valor);
        else
            return Character.toString(valor) + Character.toString(digito);
    }
}
