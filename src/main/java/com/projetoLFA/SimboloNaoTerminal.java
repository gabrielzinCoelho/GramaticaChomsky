package com.projetoLFA;

public class SimboloNaoTerminal extends Simbolo{

    public static boolean ehSimboloNaoTerminal(Object objeto){
        return objeto instanceof SimboloNaoTerminal;
    }

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

    @Override
    public int compareTo(Simbolo outroSimbolo){

        if(!(outroSimbolo instanceof SimboloNaoTerminal))    
            return 1;
        

        SimboloNaoTerminal outroSimboloNaoTerminal = (SimboloNaoTerminal) outroSimbolo;
        if(this.valor.equals(outroSimbolo.valor)){

            if(this.digito == null){

                if(outroSimboloNaoTerminal.digito == null)
                    return 0;
                else
                    return 1;
                
    
            }else{
                if(outroSimboloNaoTerminal.digito == null)
                    return -1;
                else
                    return this.digito - outroSimboloNaoTerminal.digito;
                
            }

        }else{
            
            SimboloNaoTerminal simboloInicial = new SimboloNaoTerminal('S');
            if(this.valor.equals(simboloInicial.valor))
                return -1;
            else if(outroSimboloNaoTerminal.valor.equals(simboloInicial.valor))
                return 1;
            else
                return super.compareTo(outroSimboloNaoTerminal);
        }
        
    }

}
