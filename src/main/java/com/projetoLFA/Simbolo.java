package com.projetoLFA;

public abstract class Simbolo implements Cloneable{

    protected Character valor;

    public static Simbolo criarSimbolo(String strSimbolo){

        if(strSimbolo.length() == 1){

            Character valor = strSimbolo.charAt(0);

            if(Character.isUpperCase(valor))
                return new SimboloNaoTerminal(valor);
            else
                return new SimboloTerminal(valor);

        }else if(strSimbolo.length() == 2){

            return new SimboloNaoTerminal(strSimbolo.charAt(0), strSimbolo.charAt(1));
        }
        else
            throw new IllegalArgumentException();

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
        
        if (objeto == null || getClass() != objeto.getClass()) //!(objeto instanceof Simbolo)
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
        return String.valueOf(valor);
    }

    @Override
    public Simbolo clone(){
        try{
            return (Simbolo) super.clone();
        }catch (CloneNotSupportedException err){
            throw new AssertionError();
        }
    }

}
