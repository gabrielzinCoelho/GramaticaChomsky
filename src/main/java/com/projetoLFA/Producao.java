package com.projetoLFA;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Producao {
    
    private List<Simbolo> simbolos;

    public Producao(List<Simbolo> simbolos){
        
        this.simbolos = new ArrayList<Simbolo>(simbolos);

    }

    public List<Simbolo> getSimbolos(){
        return new ArrayList<Simbolo>(simbolos);
    }

    public Simbolo getPrimeiroSimbolo(){
        return simbolos.get(0);
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder(); // evitar criacao de novo objeto a cada concatenacao

        for(Simbolo simbolo : simbolos)
            str.append(simbolo.toString());
        
        return str.toString();

    }

    @Override
    public boolean equals(Object objeto){

        if(this == objeto) //mesma referencia
            return true;
        
        if(objeto == null || !(objeto instanceof Producao))
            return false;
        
        Producao umaProducao = (Producao) objeto;
        return this.simbolos.equals(umaProducao.simbolos);
    }

    @Override
    public int hashCode(){
        return Objects.hash(simbolos);
    }

    public int tamanho(){
        return simbolos.size();
    }

}
