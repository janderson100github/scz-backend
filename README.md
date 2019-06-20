# Social Credit API

Generate a pool of social credits to be shared with others.

# Running Locally

Use MariaDB or MySQL and create a database `credit`. Grant all privileges to `maria` identified by `maria`, or change `application.properties` accordingly.

`mvn clean package -DskipTests`

Run the application: `CreditApiApplication.java`

[http://localhost:8080]()

### API Documentation (Swagger)

[http://localhost:8080/swagger-ui.html]()


#### Pool

```
curl -v -X POST -H "Content-Type: application/json" -d '{"name": "PRFX", "id": 987, "amount": 1000000, "description": "a good one"}' "http://localhost:8080/pool"
```

```
curl -v -X GET -H "Content-Type: application/json" "http://localhost:8080/pool/PRFX/987"

```
