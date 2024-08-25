package com.projetoLFA;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Producao implements Comparable<Producao>{
    
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


    @Override
    public int compareTo(Producao outraProducao){
        return this.simbolos.toString().compareTo(outraProducao.simbolos.toString());
    }

    public boolean geraTerminal(Set<SimboloNaoTerminal> geramTerminal){
        for (Simbolo simbolo : simbolos) {
            if ((simbolo instanceof SimboloNaoTerminal) && (geramTerminal == null || !geramTerminal.contains(simbolo))){
                return false;
            }
        }
        return true;
    }

    public boolean containsTerminal(){
        for (Simbolo simbolo : simbolos) {
            if (simbolo instanceof SimboloTerminal){
                return true;
            }
        }
        return false;
    }

}
