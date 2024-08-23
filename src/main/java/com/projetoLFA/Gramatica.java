package com.projetoLFA;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.regex.Matcher;

public class Gramatica{
    
    protected Map<String, Simbolo> simbolos;
    protected SimboloNaoTerminal simboloInicial;
    protected Map<SimboloNaoTerminal, Set<Producao>> producoes;
    protected SimboloTerminal simboloLambda;

    public Gramatica(String textoGramatica){
        simbolos = new HashMap<String, Simbolo>();
        producoes = new HashMap<SimboloNaoTerminal, Set<Producao>>();
        simboloInicial = null;
        simboloLambda = (SimboloTerminal) adicionarSimbolo(".");

        lerGramatica(textoGramatica);
    }

    public Gramatica(Gramatica umaGramatica){

        this.simbolos = new HashMap<String, Simbolo>();
        this.producoes = new HashMap<SimboloNaoTerminal, Set<Producao>>();
        this.simboloInicial = null;
        this.simboloLambda = (SimboloTerminal) adicionarSimbolo(".");

        // copia dos simbolos
        for (Map.Entry<String, Simbolo> simbolos : umaGramatica.simbolos.entrySet()){

            String chaveSimbolo = simbolos.getKey();

            Simbolo instanciaSimbolo = simbolos.getValue();

            Simbolo copiaSimbolo = instanciaSimbolo instanceof SimboloTerminal ?
                new SimboloTerminal((SimboloTerminal) instanciaSimbolo) : new SimboloNaoTerminal((SimboloNaoTerminal)instanciaSimbolo);

            this.simbolos.put(chaveSimbolo, copiaSimbolo);
        }

        // simboloInicial recebe a referencia da instancia do simbolo inicial da gramatica
        String chaveSimboloInicial = umaGramatica.simboloInicial.getValor();
        this.simboloInicial = (SimboloNaoTerminal) this.simbolos.get(chaveSimboloInicial);

        // copia das regras
        for (Map.Entry<SimboloNaoTerminal, Set<Producao>> producao : umaGramatica.producoes.entrySet()){

            // busca da referencia da variavel da producao na nova gramatica
            SimboloNaoTerminal variavelProducao = producao.getKey();
            SimboloNaoTerminal copiavariavelProducao = (SimboloNaoTerminal) this.simbolos.get(variavelProducao.getValor());
            Set<Producao> regrasProducao = producao.getValue();

            // copia das producoes de determinada regra
            for (Producao regra : regrasProducao) {

                // array com as referencias aos simbolos da gramatica (define uma regra)
                List<Simbolo> arrSimbolos = new ArrayList<>();

                for (Simbolo simbolo : regra.getSimbolos())
                    arrSimbolos.add(this.simbolos.get(simbolo.getValor()));

                this.adicionarProducao(copiavariavelProducao, arrSimbolos);
                
            }

        }
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

        producoes.keySet().stream()
            .sorted()
            .forEach(variavel -> {
                Set<Producao> producao = this.producoes.get(variavel);

                str.append(variavel.toString()).append(" -> ");

                List<String> listaProducoes = producao.stream()
                    .sorted()
                    .map(p -> p.toString())
                    .collect(Collectors.toList());
                

                str.append(String.join(", ", listaProducoes)).append("\n");
            });

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

}
