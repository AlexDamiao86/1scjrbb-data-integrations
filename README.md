# Development and Integrations Tools

## Executando o projeto 

1. Iniciar o Kafka Broker: 

```bash
docker-compose up -d
```

2. Iniciar o drone-consumer:

```bash
cd drone-producer
gradle bootRun
```

3. Iniciar o drone-producer: 

```bash
cd drone-producer
gradle bootRun
```

4. Testar aplicação enviando requisições via Postman ou CURL:

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
