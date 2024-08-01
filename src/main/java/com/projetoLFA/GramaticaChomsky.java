package com.projetoLFA;

import java.util.*;
import java.util.stream.Collectors;

import org.paukov.combinatorics3.Generator;

public class GramaticaChomsky extends Gramatica{
    
    public GramaticaChomsky(Gramatica gramatica){
        super(gramatica); // invoca construtor de copia
        aplicarFormaNormal();
    }

    private void aplicarFormaNormal(){
        removerRecursaoSimboloInicial();
        removerLambda();
    }

    private void removerRecursaoSimboloInicial(){

        boolean possuiRecursao = false;

        for(Map.Entry<SimboloNaoTerminal, Set<Producao>> producao : producoes.entrySet()){

            Set<Producao> regrasProducao = producao.getValue(); 

            for(Producao regra : regrasProducao){

                if(regra.getSimbolos().contains(simboloInicial)){
                    possuiRecursao = true;
                    break;
                }
            }

            if(possuiRecursao)
                break;
        }

        //S1 -> S
        if(possuiRecursao){
            SimboloNaoTerminal novoSimboloInicial = (SimboloNaoTerminal)adicionarSimbolo("S1");
            adicionarProducao(novoSimboloInicial, new ArrayList<Simbolo>(Arrays.asList(simboloInicial)));
            this.simboloInicial = novoSimboloInicial;
        }

    }

    private void removerLambda(){

        Set<SimboloNaoTerminal> anulaveis = variaveisAnulaveis();

        for(Map.Entry<SimboloNaoTerminal, Set<Producao>> producao : producoes.entrySet()){

            SimboloNaoTerminal variavelProducao = producao.getKey();
            Set<Producao> regrasProducao = producao.getValue(); 
            Set<Producao> copiaRegrasProducao = new HashSet<>(regrasProducao);    

            for(Producao regra : copiaRegrasProducao){ // iterar sobre as regras antigas

                List<Simbolo> simbolosRegra = regra.getSimbolos();
                List<Integer> indexAnulaveis = new ArrayList<>();

                int contadorIndex = 0;

                // mapear indices que podem ser omitidos para gerar novas regras
                for(Simbolo simbolo : simbolosRegra){
                    if(anulaveis.contains(simbolo))
                        indexAnulaveis.add(contadorIndex);
                    contadorIndex++;
                }

                int quantidadeAnulaveis = indexAnulaveis.size();
                
                for(int i = 1; i<=quantidadeAnulaveis; i++){

                    List<List<Simbolo>> novasRegras = criaCombinacaoRegra(simbolosRegra, indexAnulaveis, i);

                    for(List<Simbolo> novaRegra : novasRegras){
                        
                        // regra lambda apenas no simbolo inicial
                        if(novaRegra.size() == 0){
                            if(variavelProducao == simboloInicial){
                                adicionarProducao(simboloInicial, Arrays.asList(simboloLambda));
                            }
                        }else{
                            adicionarProducao(variavelProducao, novaRegra);
                        }

                    }

                }
                
            }

            // remover produções lambda (exceto simbolo inicial)
            if (producao.getKey() != simboloInicial) {

                // evitar ConcurrentModificationException
                Iterator<Producao> iterator = regrasProducao.iterator();
                while (iterator.hasNext()) {
                    Producao regra = iterator.next();
                    if (regra.getSimbolos().contains(simboloLambda)) {
                        iterator.remove();
                    }
                }

            }

        }

    }

    private List<List<Simbolo>> criaCombinacaoRegra(List<Simbolo> regraOriginal, List<Integer> indexAnulaveis, int quantidadeAnulacoes){

        List<List<Simbolo>> novasRegras = Generator.combination(indexAnulaveis)
        .simple(quantidadeAnulacoes)
        .stream()
        .map(combinacaoAnulaveis -> {

            List<Simbolo> novaRegra = new ArrayList<>();

            for(int i = 0; i < regraOriginal.size(); i++){
                if(!combinacaoAnulaveis.contains(i)){
                    novaRegra.add(regraOriginal.get(i));
                }
            }

            return novaRegra;
        })
        .collect(Collectors.toList());

        return novasRegras;
    
    }

    private Set<SimboloNaoTerminal> variaveisAnulaveis(){

        Set<SimboloNaoTerminal> anulaveis = new HashSet<>(), prevAnulaveis;

        for(Map.Entry<SimboloNaoTerminal, Set<Producao>> producao : producoes.entrySet()){

            SimboloNaoTerminal variavelProducao = producao.getKey();
            Set<Producao> regrasProducao = producao.getValue();

            for(Producao regra : regrasProducao){

                if(regra.getSimbolos().contains(simboloLambda)){
                    anulaveis.add(variavelProducao);
                    break;
                }
            }
        }

        do{
            prevAnulaveis = new HashSet<>(anulaveis);

            for(Map.Entry<SimboloNaoTerminal, Set<Producao>> producao : producoes.entrySet()){

                SimboloNaoTerminal variavelProducao = producao.getKey();
                Set<Producao> regrasProducao = producao.getValue();
    
                for(Producao regra : regrasProducao){
    
                    if(regraEhCombinacaoConjunto(regra.getSimbolos(), prevAnulaveis)){
                        anulaveis.add(variavelProducao);
                        break;
                    }
                    
                }
            }
            
        }while(!anulaveis.equals(prevAnulaveis));

        return anulaveis;

    }

    private boolean regraEhCombinacaoConjunto(List<? extends Simbolo> regra, Set<? extends Simbolo> conjunto){

        for(Simbolo simboloRegra : regra){

            boolean simboloPertence = false;

            for(Simbolo simboloConjunto : conjunto)
                if(simboloRegra.equals(simboloConjunto)){
                    simboloPertence = true;
                    break;
                }
            
            if(!simboloPertence)
                return false;
            
        }

        return true;

    }

}
