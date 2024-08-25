package com.projetoLFA;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.paukov.combinatorics3.Generator;

public class GramaticaChomsky extends Gramatica{
    
    Integer qtdRegraT;
    
    public GramaticaChomsky(Gramatica gramatica){
        super(gramatica); // invoca construtor de copia
        qtdRegraT = 0;
        aplicarFormaNormal();
    }

    private void aplicarFormaNormal(){

        if(!producoes.isEmpty()){
            removerRecursaoSimboloInicial();
            removerLambda();
            removerCadeia();
            removerSemTerminal();
            removerNaoReach();
            removerTerminalComposto();
            removerProducaoNaoBin();
        }  
            
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
            SimboloNaoTerminal novoSimboloInicial = (SimboloNaoTerminal)adicionarSimbolo("S'");
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

                List<List<Simbolo>> novasRegras = UtilitariosChomsky.criaCombinacaoRegra(simbolosRegra, indexAnulaveis);

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

            // remover produções lambda (exceto simbolo inicial)
            if (producao.getKey() != simboloInicial) {

                producoes.put(
                    variavelProducao, 
                    regrasProducao
                        .stream()
                        .filter(regra -> !regra.getSimbolos().contains(simboloLambda))
                        .collect(Collectors.toSet())
                );
            }

        }

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
    
                    if(UtilitariosChomsky.regraEhCombinacaoConjunto(regra.getSimbolos(), prevAnulaveis)){
                        anulaveis.add(variavelProducao);
                        break;
                    }
                    
                }
            }
            
        }while(!anulaveis.equals(prevAnulaveis));

        return anulaveis;

    }

    private void removerCadeia(){
        
        Map<SimboloNaoTerminal, Set<SimboloNaoTerminal>> cadeiasEncontradas = calculaCadeias();
        removerCadeias();
        copiaRegrasCadeia(cadeiasEncontradas);

    }


    private Map<SimboloNaoTerminal, Set<SimboloNaoTerminal>> calculaCadeias(){

        Map<SimboloNaoTerminal, Set<SimboloNaoTerminal>> cadeiasVariaveis = new HashMap<>();
        
        for(SimboloNaoTerminal variavelProducao : this.producoes.keySet()){
            
            Set<SimboloNaoTerminal> cadeiaVariavelProducao = calculaCadeiaVariavel(variavelProducao);

            cadeiasVariaveis.put(variavelProducao, cadeiaVariavelProducao);
        }

        return cadeiasVariaveis;
    }

    private Set<SimboloNaoTerminal> calculaCadeiaVariavel(SimboloNaoTerminal variavel){

        Set<SimboloNaoTerminal> cadeia = new HashSet<>(), prevCadeia, novoCadeia = new HashSet<>();
        cadeia.add(variavel);
        novoCadeia.add(variavel);

        do{  

            prevCadeia = new HashSet<>(cadeia);

            for(SimboloNaoTerminal variavelCadeia : novoCadeia){

                Set<Producao> producoesVariavelCadeia = this.producoes.get(variavelCadeia);

                for (Producao regra : producoesVariavelCadeia) {

                    //ehRegraCadeia
                    if(UtilitariosChomsky.ehRegraCadeia(regra)){

                        SimboloNaoTerminal cadeiaEncontrada = (SimboloNaoTerminal) regra.getPrimeiroSimbolo();
                        cadeia.add(cadeiaEncontrada);
                    }
                       
                }
            }

            // novo = cadeia - prev
            novoCadeia = new HashSet<>(cadeia);
            novoCadeia.removeAll(prevCadeia);

            
        }while(novoCadeia.size() > 0);

        return cadeia;

    }

    private void removerCadeias(){

        for(Map.Entry<SimboloNaoTerminal, Set<Producao>> producao : this.producoes.entrySet()){

            SimboloNaoTerminal variavelProducao = producao.getKey();
        
            Set<Producao> producoesSemCadeia = producao.getValue().stream()
                .filter(p -> !UtilitariosChomsky.ehRegraCadeia(p))
                .collect(Collectors.toSet());

            this.producoes.put(variavelProducao, producoesSemCadeia);
        }

    }

    private void copiaRegrasCadeia(Map<SimboloNaoTerminal, Set<SimboloNaoTerminal>> cadeiasEncontradas){

        Map<SimboloNaoTerminal, Set<Producao>> producoesOriginais = new HashMap<>(this.producoes);

        for(Map.Entry<SimboloNaoTerminal, Set<SimboloNaoTerminal>> cadeia : cadeiasEncontradas.entrySet()){
            SimboloNaoTerminal variavel = cadeia.getKey();
            Set<SimboloNaoTerminal> variaveisCadeia = cadeia.getValue();
            
            Set<Producao> conjuntoProducoes = this.producoes.get(variavel);


            for(SimboloNaoTerminal variavelCadeia : variaveisCadeia){
                if(variavelCadeia != variavel){
                    Set<Producao> conjuntoProducoesCadeia = producoesOriginais.get(variavelCadeia);
                    conjuntoProducoes.addAll(conjuntoProducoesCadeia);

                }
            }
        }

    }

    private void removerNaoReach(){

        Set<SimboloNaoTerminal> reach = new HashSet<>();

        reach.add(simboloInicial);

        Set<SimboloNaoTerminal> prevReach = new HashSet<>();

        while(!reach.equals(prevReach)){
            Set<SimboloNaoTerminal> newReach = new HashSet<>(reach);
            newReach.removeAll(prevReach);
            prevReach = reach;

            for(SimboloNaoTerminal variavelReach : newReach){
                Set<Producao> conjuntoProducoesReach = producoes.get(variavelReach);
                
                for(Producao cadaProducao : conjuntoProducoesReach){
                    List<Simbolo> simbolosProducao = cadaProducao.getSimbolos(); 

                    for(Simbolo simbolo : simbolosProducao){
                        if(simbolo instanceof SimboloNaoTerminal){
                            reach.add((SimboloNaoTerminal) simbolo);
                        }
                    }
                }
            }
        }

        atualizaGramaticaReach(reach);
    }

    private void atualizaGramaticaReach(Set<SimboloNaoTerminal> reach){
        Set<SimboloNaoTerminal> naoReach = new HashSet<>();
        for(Map.Entry<SimboloNaoTerminal, Set<Producao>> todosSimbolos : producoes.entrySet()){
            SimboloNaoTerminal simbolo = todosSimbolos.getKey();
            if(!(reach.contains(simbolo))){
                naoReach.add(simbolo);
            }
        }
        for(SimboloNaoTerminal simbolosInalcancaveis : naoReach){
            producoes.remove(simbolosInalcancaveis);
        }
    }

    private static class UtilitariosChomsky{

        // verifica se uma regra é formada exclusivamente por simbolos de um determinado conjunto  
        static boolean regraEhCombinacaoConjunto(List<? extends Simbolo> regra, Set<? extends Simbolo> conjunto){

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

        // dada uma regra e uma lista de indices anulaveis referentes à regra, retorna todas possiveis regras
        // criadas considerando a possibilidade de anular uma variavel anulavel ou nao
        static List<List<Simbolo>> criaCombinacaoRegra(List<Simbolo> regraOriginal, List<Integer> indexAnulaveis){

            int quantidadeAnulaveis = indexAnulaveis.size();
            List<List<Simbolo>> novasRegras = new ArrayList<>();
                
            for(int numAnulaveis = 1; numAnulaveis<=quantidadeAnulaveis; numAnulaveis++){

                novasRegras.addAll(
                    Generator.combination(indexAnulaveis)
                        .simple(numAnulaveis)
                        .stream()
                        .map(combinacaoAnulaveis -> {
                
                            List<Simbolo> novaRegra = new ArrayList<>();
                
                            for(int indexRegra = 0; indexRegra < regraOriginal.size(); indexRegra++){

                                //simbolo do index não faz parte da combinacao de anulaveis
                                if(!combinacaoAnulaveis.contains(indexRegra)){
                                    novaRegra.add(regraOriginal.get(indexRegra));
                                }
                            }
                
                            return novaRegra;
                        })
                        .collect(Collectors.toList())
                );

            }
    
            return novasRegras;
        }

        // verifica se uma regra eh Regra de Cadeia
        static boolean ehRegraCadeia(Producao regra){
            return regra.tamanho() == 1 && SimboloNaoTerminal.ehSimboloNaoTerminal(regra.getPrimeiroSimbolo());
        }

    }

    private void removerSemTerminal(){

        Set<SimboloNaoTerminal> term = new HashSet<>();
        Set<SimboloNaoTerminal> anterior = new HashSet<>();
        
        // encontra regras que contem alguma producao composta de terminais
        for (Map.Entry<SimboloNaoTerminal, Set<Producao>> regra : producoes.entrySet()) {
            Set<Producao> producoesDaRegra = regra.getValue();
            for (Producao producao : producoesDaRegra) {
                if (producao.geraTerminal(term)){
                    term.add(regra.getKey());
                    break;
                }
            }
        }
        
        // encontra regras que possuem alguma producao que gera terminais
        while (!anterior.equals(term)){
            for (Map.Entry<SimboloNaoTerminal, Set<Producao>> regra : producoes.entrySet()) {
                Set<Producao> producoesDaRegra = regra.getValue();
                anterior.addAll(term);
                for (Producao producao : producoesDaRegra) {
                    if (producao.geraTerminal(term)){
                        term.add(regra.getKey());
                        break;
                    }
                }
            }
        }

        // remove regras que nao geram terminais do map "producoes"
        Iterator<SimboloNaoTerminal> iterador = producoes.keySet().iterator();
        while (iterador.hasNext()) {
            SimboloNaoTerminal regra = iterador.next();
            if (!term.contains(regra)){
                iterador.remove();
            }
        }
        
        removerProducoesInuteis();
    }

    private void removerTerminalComposto(){
        Map<SimboloNaoTerminal, Set<Producao>> copiaProducoes = new HashMap<>(producoes);
        
        for (Map.Entry<SimboloNaoTerminal, Set<Producao>> regra : copiaProducoes.entrySet()){
            for (Producao producao : regra.getValue()){
                if (producao.containsTerminal() && producao.tamanho() > 1) {
                    List<Simbolo> arrSimbolos = new ArrayList<>(); // simbolos da producao atualizada

                    for (Simbolo simbolo : producao.getSimbolos()) {
                        if (simbolo instanceof SimboloTerminal) {
                            // adiciona "A'" como simbolo se encontra "a"
                            String strNovoSimbolo = simbolo.toString().toUpperCase() + "'";
                            SimboloNaoTerminalApostrofo novoSimbolo = (SimboloNaoTerminalApostrofo) adicionarSimbolo(strNovoSimbolo);
                            arrSimbolos.add(novoSimbolo);
                            List<Simbolo> terminalGerado = new ArrayList<>();
                            terminalGerado.add(simbolo);

                            adicionarProducao(novoSimbolo, terminalGerado); // adiciona a regra "A' -> a"
                        } else {
                            arrSimbolos.add(simbolo);
                        }
                    }
                    // troca a producao "wav" por "wA'v"
                    removerProducao(regra.getKey(), producao);
                    adicionarProducao(regra.getKey(), arrSimbolos);
                }
            }
        }
    }

    private void removerProducaoNaoBin(){
        Map<Producao, SimboloNaoTerminalDigito> regrasT = new HashMap<>();

        Map<SimboloNaoTerminal, Set<Producao>> copiaProducoes = new HashMap<>(producoes);
        
        for (Map.Entry<SimboloNaoTerminal, Set<Producao>> regra : copiaProducoes.entrySet()){
            for (Producao producao : regra.getValue()){
                if (producao.tamanho() > 2) {
                    removerProducao(regra.getKey(), producao);
                    arrumarProducao(regra.getKey(), producao, regrasT);
                }
            }
        }
    }

    private void arrumarProducao(SimboloNaoTerminal regra, Producao producao, Map<Producao, SimboloNaoTerminalDigito> regrasT) {
        List<Producao> array = new ArrayList<>();
        array.add(producao);
        while (array.get(0).tamanho() > 2) {
            List<Simbolo> arrSimbolos = new ArrayList<>(array.get(0).getSimbolos());
            arrSimbolos.remove(0);
            Producao producaoAtual = new Producao(arrSimbolos);
            array.add(0, producaoAtual);
        }
        while (array.size() > 1) {
            Producao producaoAtual = array.get(0);
            array.remove(0);
            SimboloNaoTerminalDigito simbolo;
            if (regrasT.containsKey(producaoAtual)) { // ja existe Tn para esta producao
                simbolo = regrasT.get(producaoAtual);
            } else { // novo Tn criado
                qtdRegraT++;
                simbolo = (SimboloNaoTerminalDigito) adicionarSimbolo("T" + qtdRegraT.toString());
                regrasT.put(producaoAtual, simbolo);
            }
            adicionarProducao(simbolo, producaoAtual.getSimbolos());

            for (Producao prod : array) {
                if (prod.tamanho() > 2) {
                    prod.substitui2(simbolo); // troca ABCD por ABTn
                }
            }
        }
        adicionarProducao(regra, array.get(0).getSimbolos());
    }

}
