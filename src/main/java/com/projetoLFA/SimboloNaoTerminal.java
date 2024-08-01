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

    @Override
    public String getValor(){
        if(digito == null)
            return Character.toString(valor);
        else
            return Character.toString(valor) + Character.toString(digito);
    }

    @Override
    public SimboloNaoTerminal clone(){
        SimboloNaoTerminal copia = (SimboloNaoTerminal)super.clone();
        
        copia.digito = this.digito == null ? null : Character.valueOf(this.digito);
        
        return copia;
    }

}
