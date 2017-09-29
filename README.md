trafegoEspacial
=====================

**Um sistema de controle de tráfego espacial para controlar os hangares e as viagens espaciais do Império Galáctico do Universo Star Wars utilizando api SWAPI.**

Executando o projeto
--------------------

### Pré-requisitos

- Java 7 ou superior
- Maven 3 ou superior

### Passos

1. Rodar a aplicação
2. Acessar através de um navegador

### Opções para rodar o projeto

1. [Rodar com maven e tomcat embarcado](#rodar-com-maven-e-tomcat-embarcado-por-linha-de-comando)
2. [Implantar em um servidor existente](#implantar-em-um-servidor-existente)

#### Rodar com maven e tomcat embarcado por linha de comando

##### Comando

na pasta base (local onde o projeto foi baixado) executar o seguinte comando

```shell
mvn clean tomcat7:run
```

#### Implantar em um servidor existente

##### Pré-requisitos adicionais

- servidor de aplicação java compativel com a especificação Java Servlet 3.0 (JSR-000315)

##### Passos

1. Contruir o projeto e gerar os artefatos
2. Implantar o artefato
3. Iniciar a aplicação

###### Contruindo o projeto e gerando os artefatos

####### Comando

na pasta base (local onde o projeto foi baixado) executar o seguinte comando

```shell
mvn clean install
```
isso vai gerar o artefato trafegoEspacial.war