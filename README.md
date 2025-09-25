<a id="readme-top"></a>

<p align="center">
    <a href="https://github.com/TP1202510030/backend">
        <img alt="Greenhouse Backend Logo" src="https://github.com/TP1202510030/mobile_app/blob/main/assets/images/logo.png?raw=true" width="100" />
    </a>
</p>

<h1 align="center">
Greenhouse Backend
</h1>

<p align="center">
A robust backend service for the Greenhouse IoT ecosystem, designed to manage companies, users, 
and automated grow rooms.
<br />
</p>

<details>
<summary>Table of Contents</summary>
<ol>
<li>
<a href="#about-the-project">About The Project</a>
<ul>
<li><a href="#architecture">Architecture</a></li>
<li><a href="#technologies-used">Technologies Used</a></li>
</ul>
</li>
<li>
<a href="#getting-started">Getting Started</a>
<ul>
<li><a href="#prerequisites">Prerequisites</a></li>
<li><a href="#installation--configuration">Installation & Configuration</a></li>
</ul>
</li>
<li><a href="#api-documentation">API Documentation</a></li>
<li><a href="#contact">Contact</a></li>
</ol>
</details>

## About The Project

This Spring Boot application serves as the core backend for the Greenhouse IoT System. It provides a secure, scalable,
and modular API to manage all aspects of the ecosystem.

The system is designed following a Domain-Driven Design (DDD) approach, separating business logic into distinct Bounded
Contexts:

- IAM (Identity and Access Management): Handles user authentication, authorization, roles, and security concerns.

- Companies: Manages client companies, their profiles, and relationships within the system.

- GrowRooms: Manages the lifecycle of mushroom grow rooms, including IoT device provisioning on AWS, crop management,
  and
  data collection for measurements and control actions.

## Architecture

The project follows a modular, hexagonal architecture, separating the application into four main layers: presentation,
application, domain, and infrastructure.

### Presentation/Interface Layer

This layer is the entry point for external requests. It contains the REST controllers, DTOs (Data Transfer Objects), and
request/response models. Its primary responsibility is to handle HTTP requests, delegate the work to the application
layer, and return appropriate HTTP responses.

### Application Layer

This layer orchestrates the application's use cases by interacting with the domain and infrastructure layers. It
contains services that handle application-specific logic and transactions.

### Domain Layer

This layer contains the core business logic and entities of the application, such as Company, User, GrowRoom, and Crop.
It is the heart of the application and has no dependencies on other layers.

### Infrastructure Layer

This layer is responsible for all technical concerns, such as database interactions, messaging, and communication with
external services.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Technologies Used

This project is built with a modern, enterprise-grade Java tech stack:

- [Spring Boot](https://spring.io/projects/spring-boot): Framework for creating stand-alone, production-grade
  Spring-based Applications.
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa): Persists data in SQL stores with Java Persistence API
  using Spring Data and Hibernate.
- [Spring Security](https://spring.io/projects/spring-security): Provides authentication and authorization for the
  application.
- [PostgreSQL](https://www.postgresql.org/): A powerful, open-source object-relational database system.
- [Maven](https://maven.apache.org/): A build automation tool used primarily for Java projects.
- [Docker](https://www.docker.com/): A set of platform-as-a-service products that use OS-level virtualization to deliver
  software in packages called containers.
- [AWS IoT Core](https://aws.amazon.com/iot-core/): A managed cloud service that lets connected devices easily and
  securely interact with cloud applications and other devices.
- [MQTT](https://mqtt.org/): A lightweight, publish-subscribe, machine-to-machine network protocol for message
  queuing/message brokering.
- [OpenAPI](https://www.openapis.org/): A specification for machine-readable interface files for describing, producing,
  consuming, and visualizing RESTful web services.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Getting Started

### Prerequisites

- [JDK 22+](https://www.oracle.com/java/technologies/downloads/)
- [Maven](https://maven.apache.org/download.cgi)
- [Docker](https://docs.docker.com/get-docker/)
- [Git](https://git-scm.com/downloads)
- AWS Account & Credentials: Properly configured with programmatic access and permissions for AWS IoT Core.

### Installation & Configuration

1. Clone the repository:

```sh
git clone https://github.com/TP1202510030/backend.git
```

2. Navigate to the project directory:

```sh
cd backend
```

3. Set up AWS IoT Certificates:
   This application requires a set of certificates to securely connect to the AWS IoT MQTT broker. Follow these steps to
   create and configure them:
    1. Create a "Thing" in AWS IoT Core:
        1. Navigate to the AWS IoT Console.
        2. In the left navigation pane, choose Manage, and then choose Things.
        3. Click Create things, select Create single thing, and give your thing a name (e.g., GreenhouseBackend).
           Click Next.
        4. On the Add a certificate for your thing page, choose Auto-generate a new certificate (recommended)
           and click Next.
    2. Create and Attach a Policy:
        1. You'll need a policy that grants your backend service the necessary permissions. Click Create policy.
        2. Give the policy a name (e.g., GreenhouseBackendPolicy).
        3. In the Policy document section, replace the existing content with the following JSON, which grants broad
           permissions for
           development purposes. Note: For production environments, you should follow the principle of least privilege
           and restrict these permissions as much as possible.
       ```json
       {
         "Version": "2012-10-17",
         "Statement": [
           {
             "Effect": "Allow",
             "Action": "iot:*",
             "Resource": "*"
           }
         ]
       }
       ```
       Click Create.

        4. Go back to the "Create thing" workflow, find the policy you just created, select it, and click Create thing.
    3. Download Certificates:
        1. On the final screen, you will see links to download your new certificates. This is the only time you can
           download the private key.
        2. Download the Device certificate, Private key, and Public key.
        3. You also need the Amazon Root CA certificate. Download the Amazon Root CA 1 certificate
           from [this page.](https://docs.aws.amazon.com/iot/latest/developerguide/server-authentication.html#server-authentication-certs)
    4. Organize and Rename Certificate Files:
        1. Create a certs directory in the root of the backend project.
        2. Move the downloaded files into this certs directory.
        3. Rename the files exactly as follows:
            - The Amazon Root CA 1 file to root-CA.crt.
            - The device certificate file to Backend.cert.pem.
            - The private key file to Backend.private.key.

4. Create a .env file in the root of the backend project. This file will hold your local environment variables.
   Copy the contents of .env.example and modify it with your actual credentials.

5. Build the project using Maven:

```sh
mvn clean install
```

6. Run the application using Docker Compose:

```sh
docker-compose up
```

7. The application will be available at http://localhost:3000.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## API Documentation

The API is self-documented using OpenAPI 3 (Swagger). Once the application is running, you can access the interactive
API documentation to view and test all available endpoints.

- Swagger UI: http://localhost:3000/swagger-ui/index.html
- OpenAPI Spec (JSON): http://localhost:3000/v3/api-docs

This documentation provides detailed information about each endpoint, including request parameters, response bodies, and
security requirements.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Contact

### Alan Galavis

- GitHub: https://github.com/alanegd
- LinkedIn: https://www.linkedin.com/in/alan-galavis/

### Carlo Luca Seminario

- GitHub: https://github.com/carlolsg
- LinkedIn: https://www.linkedin.com/in/carlo-luca-seminario/

<p align="right">(<a href="#readme-top">back to top</a>)</p>