# PragmaBrewery

## About
Current solution was designed and implemented with Microservice Architecture based on Spring Cloud Netflix and Spring Boot.
Platform contains 4 microservices:
* **Dashboard** which displays temperature in real-time and notify user when container temperature outside the correct range.
![Dashboard with alert](/images/dashboard-alert.png)
* **Container-controller** which is started 6 times (per beer type) with different spring profiles and emulate work of refrigerator. Can be controlled via JMX which is exposed to web to cloud-dashboard. When door is opened temperature in current container grows by one degree and vise versa when door is closed until it's normalized.
![JMX](/images/spring-boot-admin-jmx.png)
* **Neflix Eureka service discovery**
![Service Discovery](/images/service-discovery.png)
* **Spring Boot Admin**
![Dashboard with alert](/images/spring-boot-admin.png)
  
Services comunicate via asynchronous JMS messages. UI is based on JQuery and WebSockets to retrieve data in real-time.


## Quickstart
### Prerequisites
 * Pre-installed [Docker](https://www.docker.com/)
 * Adjusted RAM memory dedicated to Docker to 11 GB

>Since the size of one of the smallest java8 Docker image is about 150 Mb. and Spring Boot in conjuction with Actuator and other libraries consumes about 800Mb. based on this we get total amount of memory consumed by one microservice which is around 1GB. And we need to start 6 beer container controllers, dashboard, cloud dashboar(Spring Boot Admin) and Eureka plus ActiveMQ broker.

### Clone git repositories
```
$ git clone https://github.com/Cheste/cloud.git
```
### Start the platform
All commands should be executed from project root directory.

To start infrastructure services (ActiveMQ, Cloud Admin, Eureka) run:
```
# docker-compose -f docker-compose-env.yml up --build
```
To start other services (Dashboard and 6 container controllers) run:
```
# docker-compose up --build
```

### Useful links
 * Dashboard: http://localhost:8080/
 * Cloud-dashboard: http://localhost:8090/
 * Eureka: http://localhost:8671/
 * ActiveMQ: http://localhost:8161/admin/

## Questions and Plan for Release 2

- [ ] E2E tests to be implemeted.
- [ ] Introduce curcuit-braker for all network calls.
- [ ] Add authentication and security.
- [ ] Implement intermediate gateway service to be able to spread measurements to different endpoints(web-app, mobile-app).
- [ ] Migrate from JMS to Kafka to be able to stream messages or switch to asynchronous REST api?
- [ ] Externalize configuration via Archaius or Spring Cloud Config
- [ ] And other...
