<div align="center">
  <h1>2V Logística</h1>
  <img src="docs/images/logo.png" alt="Logo 2V Logística" width="180" />
  <p><strong>API REST para gestão logística, pós-venda, trocas, devoluções e relatórios.</strong></p>
</div>

---

## Sobre o projeto

O **2V Logística** é uma plataforma backend para gestão logística de lojas, desenvolvida ao longo de múltiplos semestres como parte do **Projeto Integrado Multidisciplinar (PIM)** da **USCS - Universidade Municipal de São Caetano do Sul**.

O sistema resolve um problema real do varejo: gerenciar o ciclo completo de pós-venda. Um consumidor compra, precisa trocar ou devolver, envia comprovantes, e toda essa cadeia precisa de rastreamento, validações, notificações e histórico. É exatamente isso que a plataforma cobre.

A API foi construída com **Java 17** e **Spring Boot**, aplicando conceitos reais de engenharia de software: arquitetura em módulos de domínio, autenticação stateless com JWT, validações de negócio, integração com serviços externos, documentação interativa via Swagger e deploy em nuvem.

**Equipe - Grupo 2V:**

- Caio Mauriz
- João Pedro
- Kauan Serracine
- Marcelo William
- Thaiane Rosalem

---

## ✅ Funcionalidades

**Autenticação e Acesso**

- Login de lojistas e consumidores com JWT
- Controle de permissões por perfil de usuário
- Recuperação de senha via e-mail

**Produtos e Vendas**

- Cadastro, atualização e inativação de produtos
- Importação em lote via CSV
- Registro e acompanhamento de vendas

**Solicitações de Troca e Devolução**

- Abertura de solicitações com validação de prazo e quantidade
- Aprovação, reprovação, atualização e cancelamento
- Upload e validação de anexos
- Histórico completo de mudanças de status

**Integrações e Relatórios**

- Armazenamento de arquivos via Google Drive API
- Envio de e-mails transacionais
- Exportação de relatórios em Excel
- Métricas públicas e privadas por loja

**Qualidade e Observabilidade**

- Documentação interativa com Swagger UI
- Monitoramento com Spring Boot Actuator

---

## 🛠 Stack

| Camada | Tecnologia |
| --- | --- |
| Linguagem | Java 17 |
| Framework | Spring Boot 3.4.5 |
| Banco de dados | MySQL 8 / H2 para testes |
| Segurança | Spring Security + JWT + BCrypt |
| Documentação | SpringDoc OpenAPI + Swagger UI |
| Armazenamento | Google Drive API |
| E-mail | Spring Mail |
| Arquivos | Apache Tika + Apache POI |
| Build | Maven |

---

## 📁 Estrutura do projeto

```text
src/main/java/com/logistica/doisv
├── core
│   ├── config          # Configurações globais: CORS, segurança e beans
│   ├── enums           # Enumerações de domínio
│   ├── exception       # Tratamento global de exceções
│   ├── file            # DTOs e recursos compartilhados de arquivos
│   ├── security        # JWT, filtros e autenticação
│   └── util            # Utilitários compartilhados
├── integrations
│   ├── email           # Envio de e-mails transacionais
│   └── google          # Integração com Google Drive
└── modules
    ├── autenticacao    # Login, token e recuperação de senha
    ├── consumidor      # Gestão de consumidores
    ├── feedback        # Registro e consulta de avaliações
    ├── loja            # Lojas, licenças e configurações
    ├── lojista         # Usuários lojistas e permissões
    ├── metrica         # Indicadores públicos e privados
    ├── produto         # Produtos, importação CSV e imagens
    ├── relatorio       # Exportação de relatórios em Excel
    ├── solicitacao     # Trocas, devoluções, anexos e histórico
    └── venda           # Registro e acompanhamento de vendas
```

---

## 🚀 Como executar

### Pré-requisitos

- Java 17
- Maven ou Maven Wrapper incluído no projeto
- MySQL 8

### 1. Clone o repositório

```bash
git clone <url-do-repositorio>
cd doisv-back
```

### 2. Configure as variáveis de ambiente

Crie um arquivo `.env` ou configure as variáveis abaixo no seu ambiente:

```env
APP_PROFILE=dev

DB_URL=jdbc:mysql://localhost:3306/doisv
DB_USERNAME=seu_usuario
DB_PASSWORD=sua_senha

JWT_SECRET=sua_chave_secreta

MAIL_USERNAME=seu_email@gmail.com
MAIL_PASSWORD=sua_senha_de_app

CORS_ORIGINS=http://localhost:3000
URL_FRONTEND=http://localhost:3000
```

O projeto utiliza perfis do Spring (`dev` e `prod`). Em `dev`, o banco pode ser configurado localmente. Em `prod`, as variáveis apontam para o ambiente de nuvem.

### 3. Execute a aplicação

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
.\mvnw.cmd spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

---

## 📖 Documentação da API

Com a aplicação em execução, acesse a documentação interativa:

```text
http://localhost:8080/swagger-ui/index.html
```

O Swagger UI permite visualizar todos os endpoints, contratos de entrada e saída, códigos de resposta e testar requisições autenticadas via Bearer Token.

**Exemplos de endpoints:**

```text
POST   /doisv/login
POST   /doisv/login/consumidores

GET    /doisv/produtos
POST   /doisv/produtos
POST   /doisv/produtos/importar

GET    /doisv/vendas
POST   /doisv/vendas

POST   /doisv/solicitacoes/criar
PUT    /doisv/solicitacoes/aprovar/{id}
PUT    /doisv/solicitacoes/reprovar/{id}
PUT    /doisv/solicitacoes/cancelar/{id}

GET    /doisv/metricas/publicas
GET    /doisv/metricas/privadas

GET    /doisv/relatorios/{relatorio}/excel
```

---

## 🧪 Testes

### Testes automatizados

```bash
# Linux / macOS
./mvnw test

# Windows
.\mvnw.cmd test
```
