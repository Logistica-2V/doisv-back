# 2V Logistica

![Logo 2V](docs/images/logo.png)

## 📌 Back-end

Este repositorio contem o codigo do back-end da plataforma **2V Logistica**, responsavel pelas APIs REST, autenticacao, regras de negocio e integracoes de suporte ao fluxo de solicitacoes, vendas, feedbacks e relatorios.

A aplicacao foi desenvolvida com **Java 17 + Spring Boot**, utilizando persistencia com **JPA/Hibernate**, autenticacao via **JWT**, documentacao com **OpenAPI/Swagger** e suporte a integracoes como **Google Drive** e envio de e-mails.

## 🎓 Universidade

USCS - Universidade Municipal de Sao Caetano do Sul

## 👥 Equipe

**Grupo 2V**

- Caio Mauriz
- Joao Pedro
- Kauan Serracine
- Marcelo William
- Thaiane Rosalem

## 🛠️ Tecnologias Utilizadas

### Back-end

- Java 17
- Spring Boot 3.4.5
- Spring Web
- Spring Data JPA
- Spring Security
- Lombok
- Spring Boot Actuator

### Banco de dados

- MySQL 8 (ambiente de desenvolvimento/producao)
- H2 Database (suporte para testes)

### Documentacao e autenticacao

- Springdoc OpenAPI (Swagger UI)
- JWT (jjwt-api, jjwt-impl, jjwt-jackson)

### Integracoes

- Google Drive API
- Spring Mail

## 📦 Dependencias principais (Maven)

- `org.springframework.boot:spring-boot-starter-web`
- `org.springframework.boot:spring-boot-starter-data-jpa`
- `org.springframework.boot:spring-boot-starter-security`
- `org.springframework.boot:spring-boot-starter-actuator`
- `org.springframework.boot:spring-boot-starter-mail`
- `org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6`
- `io.jsonwebtoken:jjwt-api:0.11.5`
- `io.jsonwebtoken:jjwt-impl:0.11.5`
- `io.jsonwebtoken:jjwt-jackson:0.11.5`
- `com.mysql:mysql-connector-j:8.3.0`
- `com.h2database:h2`
- `com.google.apis:google-api-services-drive:v3-rev20250216-2.0.0`
- `com.google.api-client:google-api-client:2.2.0`
- `com.google.auth:google-auth-library-oauth2-http:1.11.0`

## 🚀 Como executar o projeto

### Pre-requisitos

- Java 17
- Maven (ou Maven Wrapper)
- MySQL

### 1) Clone o repositorio

```bash
git clone <url-do-repositorio>
cd doisv-back
```

### 2) Configure o perfil da aplicacao

O projeto utiliza perfis Spring:

- `dev` (padrao): configuracoes em `src/main/resources/application-dev.properties`
- `prod`: configuracoes em `src/main/resources/application-prod.properties`

Definicao padrao atual:

- `spring.profiles.active=${APP_PROFILE:dev}`

### 3) Execute o projeto

No Windows:

```bash
.\mvnw.cmd spring-boot:run
```

Em Linux/macOS:

```bash
./mvnw spring-boot:run
```

### 4) Acesse a documentacao da API

Com a aplicacao em execucao:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## ⚙️ Variaveis de ambiente importantes

- `APP_PROFILE` (ex.: `dev` ou `prod`)
- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- `JWT_SECRET`
- `CORS_ORIGINS`
- `MAIL_USERNAME`, `MAIL_PASSWORD`
- `APP_DISTRIBUICAO`
- `DOMINIO_EMAIL`, `DOMINIO_CNPJ`

## 📁 Estrutura principal da API

Controladores presentes em `src/main/java/com/logistica/doisv/controllers`:

- `ConsumidorController`
- `FeedbackController`
- `LoginController`
- `LojaController`
- `LojistaController`
- `MetricaController`
- `ProdutoController`
- `RelatorioController`
- `SolicitacaoController`
- `VendaController`

## ✅ Testes

Para executar os testes:

```bash
.\mvnw.cmd test
```
