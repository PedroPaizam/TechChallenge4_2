📦 Sistema de Gerenciamento de Encomendas para Condomínios

🏢 Plataforma para organização, registro e notificação de entregas em prédios residenciais.

[Java] [Spring Boot] [Docker] [RabbitMQ] [PostgreSQL]

---

📌 Visão Geral
O Gerenciamento de Encomendas é um sistema que auxilia porteiros e administradores de condomínios na gestão de entregas para moradores, com notificações automáticas via mensageria (RabbitMQ) e persistência de dados no PostgreSQL.
O projeto segue os princípios da Clean Architecture para garantir alta manutenibilidade e escalabilidade.

---

🚀 Funcionalidades
- Cadastro de moradores
- Registro de novas encomendas
- Notificação automática ao morador via RabbitMQ
- Confirmação de retirada de encomendas
- Persistência dos dados com PostgreSQL
- Documentação automática da API via Swagger
- Execução facilitada com Docker e Docker Compose

---

🛠 Tecnologias
- Java 17
- Spring Boot 3.0.0
- Spring Data JPA
- Spring AMQP (RabbitMQ)
- PostgreSQL 15+
- Flyway
- Docker / Docker Compose
- JUnit 5 & Mockito
- Lombok
- SpringDoc OpenAPI (Swagger)

---

⚙️ Configuração do Ambiente

1. Pré-requisitos
- JDK 17
- Docker e Docker Compose
- PostgreSQL 15+
- RabbitMQ

2. Subindo serviços com Docker
docker-compose up -d

3. Configuração manual (alternativo)
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/encomendas
    username: seu-usuario
    password: sua-senha
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
  flyway:
    enabled: true

4. Rodando o projeto
git clone https://github.com/PedroPaizam/Tech-42.git
cd api-encomendas

mvn clean install
mvn spring-boot:run

---

📖 Acesso à API
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/api-docs

---

📚 Arquitetura (Clean Architecture)

- Domain: Entidades de negócio (ex: Morador, Encomenda)
- Application: Casos de uso, serviços, mapeamentos e DTOs
- Adapters: Controllers, Repositórios, Mensageria
- Infrastructure: Banco de dados e integrações externas

---

🗂 Estrutura de Pastas

tech-challenge-encomendas
├── src/main/java/com/techchallenge/encomendas
│   ├── adapters
│   ├── application
│   ├── domain
│   ├── infrastructure
│   └── tests
├── Dockerfile
├── docker-compose.yml
├── README.md

---

📬 Endpoints Principais

Moradores
- POST /moradores : Cadastrar morador
- GET /moradores : Listar moradores
- GET /moradores/{id} : Buscar morador por ID
- GET /moradores?cpf={cpf} : Buscar morador por CPF
- GET /moradores?apartamento={numero} : Buscar morador por apartamento

Encomendas
- POST /encomendas : Registrar nova encomenda
- GET /encomendas/{id} : Buscar encomenda por ID
- PUT /encomendas/{id}/retirada : Confirmar retirada
- PUT /encomendas/{id}/confirmar-notificacao : Confirmar notificação

---

🧪 Testes
mvn test

---

🐳 Executando com Docker

docker build -t tech-challenge-encomendas .
docker run -p 8080:8080 tech-challenge-encomendas

Ou utilizando Docker Compose:

docker-compose up -d

---

🤝 Contribuindo
1. Faça um fork do repositório
2. Crie uma nova branch (feature/minha-feature)
3. Commit suas alterações
4. Envie um Pull Request 🚀

---

📣 Contato
Projeto desenvolvido por Pedro Paizam
GitHub: https://github.com/PedroPaizam
"""

# Criando o arquivo
with open("/mnt/data/README.txt", "w", encoding="utf-8") as f:
    f.write(conteudo_readme)

"/mnt/data/README.txt"

