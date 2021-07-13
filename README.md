# Mediscreen
Mediscreen is an application about managing patient.
It offer services to doctors, clinics or other medical compagnies to handle patients file.
It can releases Mediscreen will be able to warn medical assistant of potencial deseases risks.


This application use different technologies to achieve his purpose like:
- Java 8
- Spring Boot
- Docker
- Angular

Mediscreen is built on a multi service architecture.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install the software and how to install them

- Java >= 1.8
- Docker >= 20

### Installing environment

1.Install Java:

https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html

2.Install Docker:

https://docs.docker.com/engine/install/

3. For Angular front end you need:

- Node.js : https://nodejs.org/en/download/
- Npm : `npm install -g npm@lasted`
- Angular/cli: `npm install -g @angular/cli`

### Installing App

1.Clone this project using git:

`git clone <url>`

2.Build and run the app in your docker environment:

`gradlew runDocker`

This will build all micro services needed and add them to your docker environment.

This also build the Angular UI and add an image to docker environment.

If you want build and run only the back end (without Angular UI), you can use this task:

`gradlew runBackDocker`

### Clean Docker environment

1. If you want to uninstall all microservices and clean your Docker environment, use :

`gradlew stopDocker`

OR

`gradlew stopBackDocker`

### Running Tests

Tests cover all Java micro services.

Run tests:

`gradlew test`

### URLs

Docker will open sockets for each micro services:

- Mediscreen UI: http://localhost:4200
- Mediscreen Patient restful API: http://localhost:8081
- Mediscreen Patient Note restful API: http://localhost:8082
- Mediscreen Patient Diabetes IA API: http://localhost:8083
- Mediscreen MongoDB http://localhost:27017
- Mediscreen MySQL http://localhost:3306

### Docs

A swagger doc is available for all Java micro services at http://localhost:<microservice port>/swagger-ui.html#/
