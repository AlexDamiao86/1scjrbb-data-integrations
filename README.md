# Drone Monitor

![Badge em Desenvolvimento](http://img.shields.io/static/v1?label=STATUS&message=EM%20DESENVOLVIMENTO&color=GREEN&style=for-the-badge)

## 🎯 Objetivo

Trata-se de um sistema para receber informações de drones (id, latitude, longitude, temperatura, umidade) que efetuam monitoramento de uma determinada área rural. 

Os drones deverão enviar dados ao sistema a cada 10 segundos. Os dados obtidos são analisados e quando atingidas determinadas condições climáticas (temperatura >= 35ºC ou <=0) ou (umidade <= 15%) por mais de 1 minuto será enviado um e-mail de alerta.

## 📐 Projeto da aplicação

A aplicação está dividida em 3 projetos Java Spring.

1) <i>drone-producer</i> - Disponibiliza um endpoint POST /drone/publish para que os drones enviem os dados para o sistema. 
Valida as informações recebidas dos drones que deverão atender algumas condições:
- Id (valor numérico maior ou igual a 1)
- Latitude (valor numérico entre -90 e 90)
- Longitude (valor numérico entre -180 e 180)
- Temperatura (valor numérico entre -25 e 40)
- Umidade (valor numérico entre 0 e 100)
- Rastrear (true ou false)

Caso os dados recebidos estejam válidos, gera mensagem no tópico <i>drone-data</i> e devolve o HTTP Status Code 202 (Accepted) para o drone.
Caso contrário, devolve HTTP Status Code 400 (Bad Request) e lista com campo e respectiva mensagem de erro.

2) <i>drone-consumer</i> - Consome mensagens do tópico <i>drone-data</i>, analisa os dados e verifica se a área monitorada por um determinado drone encontra-se em condições climáticas alarmantes. 
Se este mesmo drone continuar informando condições climáticas alarmantes por mais de 1 minuto será gerada uma mensagem no tópico <i>send-email</i>.

<i>Observação:</i> Se o drone retornar a temperatura ou umidade consideradas como normais, ele será considerado como fora de alarme. 
O mesmo acontece se o drone deixar de ser rastreado, ou seja, apresentar o valor "false" para o campo "Rastrear". 

3) <i>email-consumer</i> - Consome mensagens do tópico <i>send-email</i>, para cada uma das mensagens recebidas no tópico, envia mensagem de e-mail de alerta com os dados obtidos na última leitura do respectivo drone. 
Observação: O e-mail será enviado para o endereço informado em campo no application.yml da aplicação.

### Visão Geral do Sistema
![Visão Geral do Sistema](images/Arquitetura%20solução.jpg)

## 🛠️ Tecnologias utilizadas

- Linguagem Java (versão 11)
- [Spring Framework](https://spring.io)
    - [Spring Initializr](https://start.spring.io)
    - [Spring Boot Web](https://spring.io/projects/spring-boot)
    - [Spring for Apache Kafka](https://spring.io/projects/spring-kafka)
    - [Spring Mail](https://www.baeldung.com/spring-email)
    - [Spring REST Doc](https://spring.io/projects/spring-restdocs)
- [Gradle (Gerenciador de dependências)](https://gradle.org)
- [Github (Controle de versão)](https://github.com)
- [Docker](https://www.docker.com)
- [Apache Kafka](https://kafka.apache.org)
- [Swagger](http://swagger.io)


## ⚙️ Executando o projeto 

1. Clonar projeto do GitHub e ir até a pasta do projeto:

```bash
git clone https://github.com/AlexDamiao86/1scjrbb-data-integrations.git
cd 1scjrbb-data-integrations
```

2. Iniciar o Kafka Broker: 

```bash
docker-compose up -d
```

3. Iniciar o drone-producer:

```bash
cd drone-producer
gradle bootRun
```

4. Iniciar o drone-consumer:

```bash
cd drone-consumer
gradle bootRun
```

5. Iniciar o email-consumer:

<i>Observação</i>: Para o correto funcionamento dessa aplicação, é necessário possuir uma conta de e-mail GMAIL (remetente da mensagem). 
Além disso, deverá ser criada uma senha de App para esse GMAIL conforme instruções no seguinte endereço: https://support.google.com/accounts/answer/185833?hl=pt-BR

Ao executar a aplicação deverão ser informadas as seguintes variáveis de ambiente (ver application.yml):
~~~yaml
GMAIL_SERVER_USERNAME=<seu-email-gmail>
GMAIL_SERVER_PASSWORD=<sua-chave-app-gmail>
EMAIL_TO=<email-destinatario>
~~~

```bash
cd email-consumer
GMAIL_SERVER_USERNAME=<seu-email-gmail> GMAIL_SERVER_PASSWORD=<sua-chave-app-gmail> EMAIL_TO=<email-destinatario> gradle bootRun
```

6. Testar aplicação enviando requisições via Swagger ou CURL:

#### SWAGGER

[Swagger UI](http://localhost:8080/swagger-ui/index.html)

#### CURL

```bash
curl --location --request POST 'localhost:8080/drone/publish' \
--header 'Content-Type: application/json' \
--data-raw '{
"id": 1,
"latitude": -39.1,
"longitude": 112.43,
"temperatura": 27.55,
"umidade": 52.3,
"rastrear": true  
}
```

## 👨🏽‍💻 Desenvolvedores

| [<img src="https://avatars.githubusercontent.com/AlexDamiao86" width=115><br><sub>Alexandre Damião Mendonça Maia</sub>](https://github.com/AlexDamiao86) |  [<img src="https://avatars.githubusercontent.com/FabioQuimico" width=115><br><sub>Fabio Ferreira dos Santos</sub>](https://github.com/FabioQuimico) |  [<img src="https://avatars.githubusercontent.com/Gabriel2503" width=115><br><sub>Gabriel Oliveira Barbosa</sub>](https://github.com/Gabriel2503) | [<img src="https://avatars.githubusercontent.com/ferreirabraga" width=115><br><sub>Rafael Braga da Silva Ferreira</sub>](https://github.com/ferreirabraga) | 
| :---: | :---: | :---: | :---: |

>
>Projeto realizado como requisito para conclusão da disciplina <i>Development and Integrations Tools</i> do MBA Full Stack Development - FIAP 2022
>
>[Prof. Rafael Thomazelli](https://github.com/rafamazzucato)
