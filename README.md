# Como executar

Entrar na raiz do projeto e executar os comandos:

`docker-compose up`

`./gradlew bootRun`

## Endpoints

### Criar partner
POST http://localhost:8080/partner
headers:
```
Content-Type: application/json
```
body:
```
{
  "tradingName": "asdf",
  "ownerName": "Zé da Silva",
  "document": "1432132123891/0001",
  "coverageArea": [
        [
            [
                { 
                 "long" : 30,
                 "lat": 20
                },
                { 
                 "long" : 45,
                 "lat": 40
                },
                { 
                 "long" : 10,
                 "lat": 40
                },
                { 
                 "long" : 30,
                 "lat": 20
                }
            ]
        ]
  ]
  ,
  "address": {"long" :-46.57421, "lat": -21.785741}
}
```
### Procurar partner por id
GET http://localhost:8080/partner/{partnerId}

### Procurar partner mais próximo
GET http://localhost:8080/partner/search
headers:
```
Content-Type: application/json
```
body:
```
{
  "long": 30,
  "lat": 40
}
```