# API RAZZIES

API RESTful que permite acessar a lista de indicados e vencedores da categoria **Pior Filme** do **Golden Raspberry Awards** (Razzies) para atender a especificação do Teste de Recrutamento e Seleção da Outsera para a vaga de Desenvolvedor JAVA.


## Requisito do sistema:

  1. **Ler o arquivo CSV dos filmes e inserir os dados em uma base de dados ao iniciar a aplicação.**
      - Ao iniciar a aplicação é realizada a leitura do arquivo CSV dos filmes e faz a inserção dos registros na base de dados. 
      - A configuração de propriedades de acesso ao banco de dados como `url`, `username` e `password` estão especificadas no arquivo de configuração `application.properties`.
      - O **Web Console do H2 Database** é um mecanismo que facilita a verificação do conjunto de dados carregados na inicialização do web service, conforme informações contidas no tópico: [Web Console do H2 Database](#web-console-do-h2-database).
      - Foi disponiblizado também o **Swagger UI** que além de permitir a visualização dos endpoints da API e sua descrição, permite realizar chamadas para esses endpoints diretamente a partir da interface do navegador. As informações de acesso a essa ferramenta estão disponíveis no tópico: [Swagger UI](#swagger-ui).

## Requisitos da API:


  1. **Obter o produtor com maior intervalo entre dois prêmios consecutivos, e o que obteve dois prêmios mais rápido, seguindo a especificação de formato definida na página 2;**
      - A implementação do endpoint `/movie/producer-awards` atende ao requisito de identificar o produtor com o maior intervalo entre dois prêmios consecutivos e o que conquistou dois prêmios no menor intervalo. Esse endpoint foi desenvolvido para retornar os dados no formato especificado, conforme orientações definidas na página 2 das especificações.
      - Neste documento, está incluso um [Estudo de Caso](#estudo-de-caso) que demonstra com mais detalhes o resultado obtido a partir do consumo deste endpoint.



## Requisitos não funcionais do sistema:

  1. **O web service RESTful deve ser implementado com base no nível 2 de maturidade de Richardson;**
      - A API foi desenvolvida seguindo os princípios do nível 2 de maturidade de Richardson, utilizando corretamente os verbos HTTP (GET, POST, PUT e DELETE) para realizar operações específicas em cada recurso. Esses detalhes podem ser facilmente visualizados na interface do [Swagger UI](#swagger-ui), conforme descrito neste documento, permitindo uma visão clara do uso de recursos RESTful na API.
      - Na classe `MovieController`, o código-fonte e as anotações também ilustram, de forma detalhada, a implementação desses conceitos, evidenciando a aderência aos requisitos especificados para este item.

  2. **Devem ser implementados somente testes de integração. Eles devem garantir que os dados obtidos estão de acordo com os dados fornecidos na proposta;**
      - A garantia de conformidade dos dados obtidos com os dados fornecidos na proposta é assegurada pelo teste automatizado `testProcessCsv`, que valida a correta leitura e inserção dos dados do arquivo `movielist.csv`".
      - A precisão dos resultados, conforme definido nos "Requisitos da API", é garantida pelo teste `testProducerAwards`, que verifica a lógica aplicada na implementação da API.
      - Para esses testes, utiliza-se o arquivo `movielist.csv`, disponibilizado junto com as Especificações do Teste do Processo de Recrutamento, para importação da base de dados de validação.
      - No tópico [Testes Automatizados](#testes-automatizados) pode ser encontrado mais informações a respeito desse assunto.

  3. **O banco de dados deve estar em memória utilizando um SGBD embarcado (por exemplo, H2). Nenhuma instalação externa deve ser necessária;**
      - Está sendo utilizado o **H2 Database**, atendendo o requisito de estar em memória e não ser necessária nenhuma instalação externa para o seu funcionamento. 
      - Para mais informações a respeito do banco de dados, consultar o tópico [Web Console do H2 Database](#web-console-do-h2-database).

  4. **A aplicação deve conter um readme com instruções para rodar o projeto e os testes de integração.**
      - Recurso disponibilizado no arquivo `README.md` incluso no diretório raiz do projeto.
      - O arquivo arquivo `README.md` pode ser visualizado também acessando o repositório: [https://github.com/alonsodecarli/razzies.git](https://github.com/alonsodecarli/razzies.git).
      - As instruções de como rodar o projeto e os testes de integração estão disponíveis utilizando: 
        - [Maven](#maven) para construção, execução e testes do projeto;
        - [Docker](#docker);
        - [Kubernetes na Cloud](#kubernetes-na-cloud);
        - [Testes Automatizados](#testes-automatizados) de Integração.

#### O código-fonte deve ser disponibilizado em um repositório git (Github, Gitlab, Bitbucket,etc).
  - O código-fonte foi disponibilizado no GitHub conforme descrito no tópico [Repositório no GitHub](#repositorio-no-github).


#### Atenção: Na avaliação serão utilizados outros conjuntos de dados com cenários diferentes, portanto é importante garantir a precisão dos resultados independente dos dados de entrada.
  - A configuração da base de dados pode é configurada por padrão na propriedade `csv.filepath` disponibilizada no arquivo de configuração `application.properties`.
  - O arquivo `movielist.csv` está localizado no caminho `resources/csv` do projeto.
  - Pode ser informado para a aplicação um arquivo CSV disponibilizado na pasta `resources/csv` ou em qualquer lugar do computador da seguinte forma:
    1. **Configurando o `application.properties`**: alterar o valor da propriedade propriedade `csv.filepath`;
    2. **Definindo a variável de ambiente para o Maven**: 
      ```
          # exemplo de execução carregando um arquivo localizado na pasta resources do projeto (caminho relativo)
          mvn spring-boot:run -Dspring-boot.run.arguments="--csv.filepath=csv/movielist_menor.csv"
          # exemplo de execução carragando um arquivo localizado em um determinado local do computador 
          mvn spring-boot:run -Dspring-boot.run.arguments="--csv.filepath=/home/alonso.decarli/temp/movielist_menor.csv"
      ```
    3. **Definindo a variável de ambiente diretamento para o Java**: 
      ```
          # exemplo de execução carregando um arquivo localizado na pasta resources do projeto (caminho relativo)
          java -jar target/razzies-0.0.1-SNAPSHOT.jar --csv.filepath=csv/movielist_menor.csv
          # exemplo de execução carragando um arquivo localizado em um determinado local do computador 
          java -jar target/razzies-0.0.1-SNAPSHOT.jar --csv.filepath=/home/alonso.decarli/temp/movielist_menor.csv
      ```
    4. Consultar a documentação correspondente para **outras formas** de configuração ou uso de padrões de projetos específicos para realizarem o tratamento de variáveis de ambiente a exemplo do `Spring Cloud Config`.

#### Observação
  - A **versão 21 do Java** foi adotada em todas as fases do desenvolvimento do projeto. Recomenda-se o uso dessa mesma versão para executar e realizar os testes da aplicação, garantindo a compatibilidade e o desempenho esperado.

---

## Repositório no GitHub
Utilizamos nesse projeto um repositório do GitHub para permitir que equipes colaborem no desenvolvimento de software, realizando o versionamento de código, controle de alterações e gestão de projetos.
Segue o endereço do repositório:
    - [https://github.com/alonsodecarli/razzies.git](https://github.com/alonsodecarli/razzies.git)


### Como clonar o repositório
Para obter uma cópia local do repositório e começar a trabalhar com o projeto, utilize o comando git clone:
```
git clone https://github.com/alonsodecarli/razzies.git
```
[API RAZZIES](#api-razzies) : Topo da Página 

---


## Maven
Construção e execução de projetos de software.<br> 
Facilita tarefas como compilar o código, rodar testes e empacotar o projeto em um arquivo executável.

### Realizar o build do projeto
Para compilar e preparar o seu projeto, use o seguinte comando:
```
mvn clean install
```
Especificações do Comando:
- Limpa a construção anterior do projeto (exclui a pasta target).
- Compila o código-fonte, executa testes e empacota o código em um arquivo executável, no caso o `.jar`.

### Executar com o Maven 
Para rodar o projeto diretamente usando o Maven, use:
```
mvn spring-boot:run
```

### Executar diretamento pelo java  
Se preferir, você pode rodar o projeto diretamente com o Java, utilizando o arquivo .jar gerado pelo Maven:
```
java -jar target/razzies-0.0.1-SNAPSHOT.jar
```

### Comando específico para exeutar os testes
Podemos executar os testes automatizados com o seguinte comando:
```
 mvn test
```

### Observações:
- Certifique-se de executar os comandos no diretório raiz do projeto, onde o arquivo pom.xml está localizado. Caso contrário, o Maven pode não conseguir localizar as dependências e arquivos necessários para o build e execução.
- Se preferir, você pode usar ferramentas como IntelliJ IDEA, Eclipse ou VS Code, que oferecem integração com o Maven e permitem executar os comandos diretamente no ambiente da IDE, facilitando o processo de build e execução sem precisar acessar o terminal manualmente.

[API RAZZIES](#api-razzies) : Topo da Página 

---

## Web Console do H2 Database

Para acessar o Web Console do **H2 Database**, siga os passos abaixo:

1. Acesse o link do console:
   [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

2. Preencha os campos de acesso com as seguintes informações:

   - **JDBC URL**: `jdbc:h2:mem:moviesdb`
   - **Usuário**: `sa`
   - **Senha**: `kcGqwEEzRwXn1ln`


### Observações:

- O H2 Database está configurado para rodar em memória (`mem`), portanto os dados serão perdidos ao reiniciar o servidor.

[API RAZZIES](#api-razzies) : Topo da Página 

---

## Swagger UI
Para acessar a documentação interativa da API, use o seguinte link: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- A documentação do Swagger fornece uma interface gráfica para explorar e testar os endpoints da API de forma fácil e intuitiva.

![Documentação da API - Swagger](https://github.com/alonsodecarli/razzies/blob/main/src/main/resources/imagens/swagger-img1.png)

[API RAZZIES](#api-razzies) : Topo da Página 

---

## Actuator: Monitoramento e Observabilidade

O **Spring Boot Actuator** oferece uma série de endpoints úteis para monitoramento e observabilidade da aplicação. Esses endpoints são especialmente importantes em ambientes de produção e orquestração, como **Kubernetes**, pois fornecem informações detalhadas sobre o estado e o desempenho da aplicação.

### Indicadores para monitoramento:

- **Saúde da aplicação**: Fornece o status geral da aplicação, como se está funcionando corretamente ou se há falhas.  
  [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)

- **Informações da aplicação**: Exibe dados detalhados sobre a aplicação, como versão, ambiente e configurações gerais.  
  [http://localhost:8080/actuator/info](http://localhost:8080/actuator/info)

- **Métricas da aplicação**: Apresenta métricas detalhadas de desempenho e uso, como o tempo de resposta, número de requisições, entre outros.  
  [http://localhost:8080/actuator/metrics](http://localhost:8080/actuator/metrics)

Esses endpoints fornecem dados cruciais para o monitoramento contínuo da saúde da aplicação, ajudando na identificação de problemas e otimizando a performance em tempo real.

[API RAZZIES](#api-razzies) : Topo da Página 

---

## Docker
Comandos para gerar a imagem e rodar o container<br>
Neste tópico, você encontrará os comandos necessários para construir e executar a imagem Docker da aplicação.<br>
Segue o código utilizado no projeto no arquivo `Dockerfile` do projeto: 
```
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copiar o JAR gerado no build
COPY /target/razzies-0.0.1-SNAPSHOT.jar /app/razzies.jar

# Expõe a porta em que a aplicação Spring Boot irá rodar
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "/app/razzies.jar"]
```

### Gerar a Imagem Docker
Para construir a imagem Docker da aplicação, utilize o seguinte comando:
```
docker build -t razzies-app .
```
- Este comando cria a imagem com o nome `razzies-app` usando o Dockerfile localizado no diretório atual `.`.

### Listar as Imagens Docker
Para listar todas as imagens Docker disponíveis localmente, utilize:
```
docker images
```

### Rodar o Container
Para rodar o container com a aplicação, use o comando:
```
docker run -p 8080:8080 razzies-app
```
- Com esse comando, a aplicação será exposta na porta `8080` do seu host local, permitindo acessá-la via `http://localhost:8080`.

### Rodar o Container em Segundo Plano
Se você preferir rodar o container em segundo plano (modo "detached"), utilize o parâmetro `-d`:
```
docker run -d -p 8080:8080 razzies-app
```
- A aplicação será executada em segundo plano, liberando o terminal para outros comandos.

### Verificar Containers em Execução
Para listar os containers em execução, use:
```
docker ps
```
- Esse comando exibirá os containers ativos no momento, incluindo suas informações como ID, status e portas.

### Parar o Container
Para parar um container em execução, utilize o comando:
```
docker stop <container_id>
```
- Substitua `<container_id>` pelo ID ou nome do container que deseja parar.

### Remover o Container
Para remover um container parado, utilize:
```
docker rm <container_id>
```
- Novamente, substitua `<container_id>` pelo ID do container a ser removido.

### Remover a Imagem
Se desejar remover a imagem Docker criada, utilize os comandos abaixo:
```
docker rmi razzies-app
```
Ou, para forçar a remoção (se houver containers dependentes), use:
```
docker rmi -f razzies-app
```
- O comando `docker rmi -f` força a remoção da imagem, mesmo que ela esteja associada a containers existentes.

Esses comandos permitem que você construa, execute e gerencie a imagem Docker da aplicação de maneira eficiente.

[API RAZZIES](#api-razzies) : Topo da Página 

---


## Container Registry

As imagens do projeto **RAZZIES** encontram-se disponíveis no **GitHub Container Registry**, onde você pode acessar e gerenciar as imagens Docker geradas. 
Uma forma prática de armazenar, versionar e distribuir imagens Docker de maneira segura e eficiente.

Segue o endereço das imagens disponiblizadas:
  - [ghcr.io/alonsodecarli/razzies-app:latest](ghcr.io/alonsodecarli/razzies-app:latest)
  - [ghcr.io/alonsodecarli/razzies-app:v1](ghcr.io/alonsodecarli/razzies-app:v1)

Segue o endereço da imagem gerada para rodar na **arquitetura ARM**:
  - [ghcr.io/alonsodecarli/razzies-app-arm:latest](ghcr.io/alonsodecarli/razzies-app-arm:latest)
  - [ghcr.io/alonsodecarli/razzies-app-arm:v1](ghcr.io/alonsodecarli/razzies-app-arm:v1)



### Consumir as imagens do Container Registry

Para consumir a imagem Docker do projeto diretamente do GitHub Container Registry, execute o seguinte comando:
```
docker pull ghcr.io/alonsodecarli/razzies-app:latest
```
Este comando irá baixar a última versão da imagem do projeto, chamada **razzies-app**, para o seu ambiente local.

### Rodando o Container Docker

Após baixar a imagem, você pode rodar o container localmente com o seguinte comando:
```
docker run -p 8080:8080 ghcr.io/alonsodecarli/razzies-app:latest
```
Este comando irá iniciar a aplicação dentro de um container Docker, mapeando a porta 8080 do container para a porta 8080 da sua máquina local. Assim, a aplicação estará disponível em `http://localhost:8080`.

### Rodando em Segundo Plano

Se preferir rodar a aplicação em segundo plano, basta adicionar a opção `-d`:
```
docker run -d -p 8080:8080 ghcr.io/alonsodecarli/razzies-app:latest
```
Com isso, o container será executado em background, permitindo que você continue utilizando o terminal normalmente.

### Verificar Containers em Execução

Para listar os containers que estão sendo executados, utilize o comando:
```
docker ps
```
### Parar e Remover o Container

Se desejar parar o container em execução, use:
```
docker stop <container_id>
```
Para remover o container, após pará-lo:
```
docker rm <container_id>
```
E, caso queira remover a imagem da sua máquina local, execute:
```
docker rmi ghcr.io/alonsodecarli/razzies-app:latest
```
Esses passos permitem que você baixe, execute e gerencie a aplicação **RAZZIES** em um container Docker de forma simples e eficiente.


[API RAZZIES](#api-razzies) : Topo da Página 

---

### Kubernetes na Cloud
  - O arquivo `yaml` contém configurações para serem aplicadas no Kubernetes, utilizei a Oracle Cloud Infrastructure (OCI) disponibilizando três pods da `API RAZZIES` em um Cluster Kubernetes Gerenciado para realizar essa validação de execução em ambiente Cloud. 
  - A comunicação foi realizada por meio de um LoadBalancer para acesso e consumo dos recursos da API.
  - Pode ser utilizada também a imagem com tag versionada, exemplo: `ghcr.io/alonsodecarli/razzies-app:v1`

Segue o código utilizado no projeto para configuração do deployment e definições de infraestrutura no Kubernetes, disponibilizado no arquivo `razzies-deployment.yaml`:
```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: razzies-app
spec:
  replicas: 3
  selector:
    matchLabels:
      app: razzies-app
  template:
    metadata:
      labels:
        app: razzies-app
    spec:
      containers:
        - name: razzies-app
          image: ghcr.io/alonsodecarli/razzies-app:latest
          ports:
            - containerPort: 8080
      imagePullSecrets:
        - name: ocirsecret
---
apiVersion: v1
kind: Service
metadata:
  name: razzies-app-service
spec:
  selector:
    app: razzies-app
  ports:
    - protocol: TCP
      port: 80   # Porta exposta pelo serviço Kubernetes
      targetPort: 8080  # Porta do container do app
  type: LoadBalancer  # Tipo de serviço
```

Segue o comando para aplicar o `yaml` no Kubernetes:
```
kubectl apply -f razzies-deployment.yaml
```
Após aplicar o arquivo yaml podemos visualizar as informações dos pods e dos services, como essas:
```

kubectl get pods
razzies-app-f974cdddc-4x2nc      1/1     Running   0          9m46s
razzies-app-f974cdddc-6cnh6      1/1     Running   0          9m46s
razzies-app-f974cdddc-zhp9b      1/1     Running   0          9m46s

kubectl get pods
razzies-app-f974cdddc-4x2nc      1/1     Running   0          3h44m
razzies-app-f974cdddc-6cnh6      1/1     Running   0          3h44m
razzies-app-f974cdddc-zhp9b      1/1     Running   0          3h44m

kubectl get services
razzies-app-service   LoadBalancer   10.244.118.100   204.216.144.176   80:31264/TCP        9m51s
```


[API RAZZIES](#api-razzies) : Topo da Página 

---

## Estudo de Caso 
Esse estudo de caso demonstra o consumo do endpoint `producer-awards` que retorna o produtor com maior intervalo entre dois prêmios consecutivos, e o que obteve dois prêmios mais rápido, seguindo a especificação definida na proposta do Teste de Recrutamento.<br>


### Usando o Swagger UI 
Demonstração de uso pelo Swagger UI:
![Resultado do Estudo de Caso - Imagem 1](https://github.com/alonsodecarli/razzies/blob/main/src/main/resources/imagens/producer-awards-img1.png)


### Usando o Curl
Comando `Curl` para consumir o endpoint:
```
curl http://localhost:8080/movie/producer-awards
ou
curl -X GET http://localhost:8080/movie/producer-awards
```
Resultado obtido:
```json
{
  "min": [
    {
      "producer": "Joel Silver",
      "interval": 1,
      "previousWin": 1990,
      "followingWin": 1991
    }
  ],
  "max": [
    {
      "producer": "Matthew Vaughn",
      "interval": 13,
      "previousWin": 2002,
      "followingWin": 2015
    }
  ]
}
```

### Observação:

  - Nesse estudo de caso foi utilizada a base de dados fornecida no arquivo `(movielist.csv)` disponibilizado junto com a especificação do Teste de Recrutamento e Seleção da Outsera.


[API RAZZIES](#api-razzies) : Topo da Página 

---



## Testes Automatizados
Conforme solicitado nas especificações do Processo de Recrutamento é para ser implementado somente testes de integração, eles são essenciais para garantir que diferentes partes do sistema funcionem corretamente. Eles verificam a interação entre componentes, como APIs, bancos de dados e serviços externos, assegurando que o fluxo de dados e as funcionalidades estejam operando conforme esperado.<br>

A execução dos testes é normalmente utilizada de duas formas, a saber: 

### 1. Na realização do build do projeto
Os testes são executados no momento do build:
```
mvn clean install
```
Especificações do Comando:
- Limpa a construção anterior do projeto (exclui a pasta target).
- Compila o código-fonte, executa testes e empacota o código em um arquivo executável, no caso o `.jar`.

#### Parâmetro -DskipTests

Pode ser usado o comando -DskipTests, com ele os testes não serem realizados, como um exemplo:
```
mvn clean install -DskipTests
```

### 2. Comando específico para exeutar os testes
Podemos executar os testes automatizados com o seguinte comando específico do Maven:
```
 mvn test
```

Segue um exemplo da saída no console da aplicação dos testes de integração realizados na aplicação:

![Resultado dos Testes - Imagem 1](https://github.com/alonsodecarli/razzies/blob/main/src/main/resources/imagens/testes-img1.png)





[API RAZZIES](#api-razzies) : Topo da Página 
