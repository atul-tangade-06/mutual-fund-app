# mutual-fund-app
mutual fund application


Mini Mutual Fund Transaction System

 
Design and build a financial transaction system with a focus on correctness, scalability, and production-grade implementation.

 

  Tech Stack
- Java 17
- Spring Boot
- PostgreSQL
- Spring Data JPA
- Hibernate

 

 API Base URL
http://localhost:8080/secure/mutualfund

 

APIs

1. Buy Mutual Fund

HTTP Method :POST 
API: http://localhost:8080/secure/mutualfund/buy


Request:
{
  "userId": 1,
  "fundId": 1,
  "amount": 1000,
  "idempotencyKey": "txn-5001"
}


Success Response: -

"Buy Successful"  

Status code : "200"

---------------------------------------------
Failure Scenarion: - 


Request: 
{
  "userId": 1,
  "fundId": 1,
  "amount": 1000,
  "idempotencyKey": "txn-6001"
} 


Response: 
"Duplicate Request"

Stauts Code : - 409 CONFLICT


--------------------------------------------------

Failure Scenario :-


Invalid Input (Missing amount)

{
  "userId": 1,
  "fundId": 1,
  "idempotencyKey": "txn-6002"
}


Response: 
"amount must not be null"

Status code:  400 BAD REQUEST



--------------------------------------------------------------------------


3. Fund Not Found

Request : -

{
  "userId": 1,
  "fundId": 99,
  "amount": 1000,
  "idempotencyKey": "txn-6003"
}


Response: -

"Fund not found"

Status Code: - 400 BAD REQUEST





==============================================================================================================

2. Sell Mutual Fund

HTTP Method :POST 
API: http://localhost:8080/secure/mutualfund/sell

Request:
{
  "userId": 1,
  "fundId": 1,
  "units": 2,
  "idempotencyKey": "txn-7001"
}


Success Response: -

{
    "message": "Sell Successful",
    "amount": 200.00
}

Staus code: "200"

-------------------------------------------------------------------------

Failure Scenarion: -

1. Insufficient Units


{
  "userId": 1,
  "fundId": 1,
  "units": 1000,
  "idempotencyKey": "txn-7002"
}


Failure Response: -


Response: - "Insufficient Units"
Status code : - 400 BAD REQUEST


-----------------------------------------------------------------------------------
	
2. Duplicate Sell Request

Request: -

{
  "userId": 1,
  "fundId": 1,
  "units": 2,
  "idempotencyKey": "txn-7001"
}

Failure Respones: -

"Duplicate Request"

Status Code: - 409 CONFLICT


-------------------------------------------------------------------------


3. No Holdings Found


Request: -

{
  "userId": 2,
  "fundId": 1,
  "units": 1,
  "idempotencyKey": "txn-7003"
}



Response: "No holdings"
Status Code: 400 BAD REQUEST





==========================================================================================================

3. Get Portfolio
HTTP Method : GET 


API: http://localhost:8080/secure/mutualfund/portfolio?userId=1


Response: -[
  {
    "userId": 1,
    "fundId": 1,
    "totalUnits": 8.0000,
    "investedAmount": 800.00
  }
]


Status code : 200 OK


-----------------------------------------------------------------------------------------------
1. Invalid User




Failure Scnarion: -

Request"- 
http://localhost:8080/secure/mutualfund/portfolio?userId=99

Response:-

Response []


Status Code- "200 OK"



====================================================================================================
 
Cases Covered

- Duplicate requests → 409 Conflict
- Insufficient balance → 400 Bad Request
- Invalid input → 400 Bad Request
- Concurrent updates handled safely

=============================================================================================

Database Tables

- userss
- funds
- Transactions
- Portfolio

===============================================================================================

Setup Instructions

1. Clone repository
https://github.com/atul-tangade-06/mutual-fund-app.git


=======================================================================================================================


2. Configure PostgreSQL

spring.datasource.url=jdbc:postgresql://localhost:5432/mutual_fund
spring.datasource.username=postgres
spring.datasource.password=Atul@123
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8080


====================================================================================================

3. Run application
mvn spring-boot:run

=============================================================================================

Sample Test Data

 

INSERT INTO users (id, name) VALUES
(1, 'Atul'),
(2, 'Rahul');

INSERT INTO fund (id, name, nav) VALUES
(1, 'HDFC Equity Fund', 100.00),
(2, 'ICICI Bluechip Fund', 50.00);

============================================================================================================================

Testing Steps

1. Call BUY API
2. Call PORTFOLIO API
3. Call SELL API
4. Test duplicate using same idempotencyKey

=================================================================================================================

Author
Atul Tangade
