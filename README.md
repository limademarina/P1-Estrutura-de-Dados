# P1-Estrutura-de-Dados
### Disciplina: Algoritimos e Estruturas de Dados I

### Professor: Dimmy Magalhães

### Matrículas: 00030091 (Marina de Lima), 0030552 (Luna Mendes)

### Link Repositório: https://github.com/limademarina/P1-Estrutura-de-Dados

### Descrição do projeto: 
O projeto consiste em um simulador de escalonador de processos implementado em Java, onde o objetivo é representar de forma didática como sistemas operacionais organizam e controlam a execução de múltiplos processos. Para isso, foram criadas filas específicas de alta, média e baixa prioridade, além de uma fila de bloqueados para processos que necessitam de recursos externos, como o disco. A lógica do escalonador garante que processos de maior prioridade sejam atendidos primeiro, mas também implementa um mecanismo de anti-inanição para assegurar que processos de prioridade mais baixa não fiquem indefinidamente sem execução. Além disso, há tratamento de bloqueio e desbloqueio, simulando situações em que um processo precisa esperar por um recurso antes de continuar sua execução. O sistema permite medir desempenho e analisar a justiça do escalonamento, podendo ser usado tanto para visualização do comportamento ciclo a ciclo, quanto para testes de performance em larga escala, como no caso de simulações com milhares de processos.

### Instruções: 
Estar na pasta Main
ls ->
```
javac -d out src/*.java
```

voltar para a pasta raiz onde esta o arquivo com csv
cd ..
ls

aqui rodar o script

Caso não exista o arquivo CSV dentro da pasta main

rodar esse comando dentro da pasta raiz
```
java -cp Main/out GeradorDeCSV

java -cp Main/out Main processos_1000.csv --quiet
( Sem logs )

java -cp Main/out Main processos_1000.csv --anti=7
( Com os logs )
```