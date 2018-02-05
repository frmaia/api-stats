# How to use:

## 1 - Build and run service:

#### Configure application parameters
Please, refer to the [application.properties](/src/main/resources/application.properties) file if you eventually want to change default values for this application. 

#### Build:
```
mvn clean package
```
#### Just run automated tests
```
mvn test
```

#### Run service using java
```
java -jar {{path-to-the-builded-jar}}
```

## 2 - Consume the API's
#### POST /transactions (example)
```
timestamp=$(date +"%s"000);echo $timestamp; curl -X POST -H "Content-Type:application/json" -d "{\"amount\":10, \"timestamp\":${timestamp}}" localhost:8080/transactions
```

#### GET /statistics (example)
```
curl localhost:8080/statistics
```
