# Compilador
Projeto apresentado ao curso de Ciência da Computação, da Universidade Federal de São João del Rei, como requisito parcial para obtenção da nota final da disciplina de Compiladores.

Este projeto consiste na implementação de um compilador para uma versão simplificada da linguagem "C".

## Gramática 
[PROGRAMA] ::= <null>

[PROGRAMA] ::= [DECLARACAO][PROGRAMA]

[PROGRAMA] ::= [REPETICAO][PROGRAMA]

[PROGRAMA] ::= [CONDICAO][PROGRAMA]

[PROGRAMA] ::= [ATRIBUICAO][PROGRAMA]

[EXPRESSAO] ::= [E]

[E] ::= [T][Elinha]

[Elinha] ::= <+>[T][Elinha] | <null>

[T] ::= [F][Tlinha]

[Tlinha] ::= <*>[F][Tlinha] | <null>

[ATRIBUICAO] ::= <id><=>[VALOR_ATRIBUIDO]<;>

[VALOR_ATRIBUIDO] ::= <id> | <numeral> | [EXPRESSAO]

[DECLARACAO] ::= [TIPO]<id>[DECLARACAO_MULTIPLA]<;>

[TIPO] ::= <int> | <float> | <char>

[DECLARACAO_MULTIPLA] ::= <,><id>[DECLARACAO_MULTIPLA] | <null>

[F] ::= <(>[E]<)> | <id> | <numeral>

[REPETICAO] ::= <while>[EXPRESSAO]<{>[BLOCO]<}>

[CONDICAO] ::= <if>[EXPRESSAO]<{>[BLOCO]<}>

[CONDICAO] ::= <if>[EXPRESSAO]<{>[BLOCO]<}><else><{>[BLOCO]<}>

## Execução
O compilador foi iplementado por meio da linguagem java. Para executar basta importar o projeto na IDE Eclipse ou compilar manualmente por meio do javac no terminal.

## Saída
O programa imprime como saida por meio do arquivo "arquivoSaida.txt":
- a lista de tokens 
- a listagem de erros identificando se o erro é léxico ou sintático.

### Desenvolvido por:
Ricardo de Souza Monteiro
Lucas Cruz ([github](https://github.com/ricardosm))

