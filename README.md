# Gramática de Chomsky

## Desenvolvedores

* [Eduardo Coelho](https://github.com/eduardocoelho1)
* [Estevão Augusto](https://github.com/EstevaoAugusto)
* [Felipe Oliveira](https://github.com/FelipeOliveira30)
* [Gabriel Coelho Costa](https://github.com/gabrielzinCoelho)

## Objetivo

Projeto prático desenvolvido para a disciplina de Linguagens Formais e Autômatos lecionada na UFLA.

O objetivo é desenvolver um aplicativo que coloque uma GLC na FNC.

O algoritmo deve receber como entrada um arquivo texto com a GLC G’ e prover como saída um arquivo texto com a GLC G” na FNC equivalente à G’.

## Exemplo de Entrada e Saída

Entrada:

```
S -> aS | bS | C | D
C -> c | .
D -> abc
D -> .
```

Saída:

```
S' -> AS | BS | a | b | c | AT1 | .
S  -> AS | BS | a | b | c | AT1
T1 -> BC
A  -> a
B  -> b
C  -> c
```

### Terminologia adotada

* Variáveis: [A-Z] (as variáveis criadas pelo algoritmo são T1, T2, T3, etc.)
* Terminais: [a-z]
* Operador de definição: ->
* Separador de regras: | [as regras também podem ser escritas linha a linha (veja variável D)]
* Lambda: .