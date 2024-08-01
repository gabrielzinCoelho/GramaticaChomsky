package com.projetoLFA;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Gramatica{
    
    protected Map<String, Simbolo> simbolos;
    protected SimboloNaoTerminal simboloInicial;
    protected Map<SimboloNaoTerminal, Set<Producao>> producoes;
    protected SimboloTerminal simboloLambda;

    public Gramatica(){
        simbolos = new HashMap<>();
        producoes = new HashMap<>();
        simboloInicial = null;
        simboloLambda = (SimboloTerminal) adicionarSimbolo(".");
    }

    public Gramatica(Map<String, Simbolo> simbolos, SimboloNaoTerminal simboloInicial, Map<SimboloNaoTerminal, Set<Producao>> producoes){
        this.simbolos = simbolos;
        this.producoes = producoes;
        this.simboloInicial = simboloInicial;
        simboloLambda = (SimboloTerminal) adicionarSimbolo(".");
    }

    public Gramatica(String textoGramatica){
        simbolos = new HashMap<String, Simbolo>();
        producoes = new HashMap<SimboloNaoTerminal, Set<Producao>>();
        simboloInicial = null;
        simboloLambda = (SimboloTerminal) adicionarSimbolo(".");

        lerGramatica(textoGramatica);
    }

    public Simbolo adicionarSimbolo(String valorSimbolo){

        return simbolos.computeIfAbsent(valorSimbolo, k -> Simbolo.criarSimbolo(k));

    }

    public Producao adicionarProducao(SimboloNaoTerminal variavelProducao, List<Simbolo> arrSimbolos){
        Producao novaProducao = new Producao(arrSimbolos);
        // producoes.computeIfAbsent(variavelProducao, k -> new HashSet<>()).add(producao);
        // garantir retorno da referencia existente no set
        Set<Producao> conjuntoProducoes = producoes.computeIfAbsent(variavelProducao, k -> new HashSet<>());
        
        for(Producao producao : conjuntoProducoes){
            if(producao.equals(novaProducao))
                return producao;
        }
        conjuntoProducoes.add(novaProducao);
        return novaProducao;
    }

    public String toString(){

        StringBuilder str = new StringBuilder();

        for (Map.Entry<SimboloNaoTerminal, Set<Producao>> linhaGramatica : producoes.entrySet()){

            SimboloNaoTerminal variavel = linhaGramatica.getKey();
            Set<Producao> producao = linhaGramatica.getValue();

            str.append(variavel.toString()).append(" -> ");
            str.append(producao.toString()).append("\n");

        }

        return str.toString();
    }


    private void lerGramatica(String textoGramatica){

        String regexTerminal = "[a-z]";
        String regexNaoTerminal = "[A-Z][0-9]?";
        String regexRegra = String.format("(?:%s|%s)+|\\.", regexNaoTerminal, regexTerminal);
        String regexProducao = String.format("(%s)->((?:%s)(?:\\|(?:%s))*)", regexNaoTerminal, regexRegra, regexRegra);
        // String regexGramatica = String.format("((?:%s\n)*(?:%s))", regexProducao, regexProducao);
        
        Pattern pattern = Pattern.compile(regexProducao);
        Matcher matcher = pattern.matcher(textoGramatica);

        Pattern patternRegra = Pattern.compile(String.format("(%s|%s|\\.)", regexTerminal, regexNaoTerminal));

        boolean ehVariavelInicial = true;

        while(matcher.find()){
            
            String variavel = matcher.group(1);
            String[] regras = matcher.group(2).split("\\|");

            SimboloNaoTerminal simboloVariavel = (SimboloNaoTerminal) adicionarSimbolo(variavel);

            if(ehVariavelInicial){
                this.simboloInicial = simboloVariavel;
                ehVariavelInicial = false;
            }

            for(String regra : regras){

                Matcher matcherRegra = patternRegra.matcher(regra);
                List<Simbolo> arrSimbolos = new ArrayList<>();

                while(matcherRegra.find()){
                    arrSimbolos.add(
                        adicionarSimbolo(matcherRegra.group(0))
                    );
                }

                adicionarProducao(simboloVariavel, arrSimbolos);
            }

        }
    }   


    public Gramatica copiaGramatica(){

        Gramatica copiaGramatica = new Gramatica();

        for (Map.Entry<String, Simbolo> simbolos : this.simbolos.entrySet()){

            String chaveSimbolo = simbolos.getKey();

            Simbolo instanciaSimbolo = simbolos.getValue();

            Simbolo copiaSimbolo = instanciaSimbolo instanceof SimboloTerminal ?
                new SimboloTerminal((SimboloTerminal) instanciaSimbolo) : new SimboloNaoTerminal((SimboloNaoTerminal)instanciaSimbolo);

            copiaGramatica.simbolos.put(chaveSimbolo, copiaSimbolo);
        }

        String chaveSimboloInicial = this.simboloInicial.getValor();
        copiaGramatica.simboloInicial = (SimboloNaoTerminal)copiaGramatica.simbolos.get(chaveSimboloInicial);

        for (Map.Entry<SimboloNaoTerminal, Set<Producao>> producao : this.producoes.entrySet()){

            SimboloNaoTerminal variavelProducao = producao.getKey();
            SimboloNaoTerminal copiavariavelProducao = (SimboloNaoTerminal)copiaGramatica.simbolos.get(variavelProducao.getValor());
            Set<Producao> regrasProducao = producao.getValue();

            for (Producao regra : regrasProducao) {

                List<Simbolo> arrSimbolos = new ArrayList<>();

                for (Simbolo simbolo : regra.getSimbolos())
                    arrSimbolos.add(copiaGramatica.simbolos.get(simbolo.getValor()));

                copiaGramatica.adicionarProducao(copiavariavelProducao, arrSimbolos);
                
            }

        }

        return copiaGramatica;
    }

}
