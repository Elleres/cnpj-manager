Baseado em toda a nossa conversa e no código fantástico que construímos juntos, eu não preciso que você me envie o código\! Já tenho todo o contexto arquitetural, as regras de negócio e a stack do seu projeto.

Como você está montando um portfólio forte para vagas de Backend/DevOps, escrevi este README com uma linguagem profissional, destacando as práticas corporativas (como JTS Spatial, validações de domínio e arquitetura DTO) que implementamos.

Aqui está o seu **README.md** pronto para ir para o GitHub:

-----

# CNPJ Manager

> Sistema corporativo para orquestração e validação de entidades empresariais (Matrizes e Filiais) com inteligência geográfica.

## Visão Geral

O **CNPJ Manager** é uma API RESTful robusta desenvolvida para resolver a complexidade do gerenciamento de hierarquias empresariais. O sistema garante a integridade dos dados validando raízes de CNPJ entre matrizes e filiais, além de processar e persistir automaticamente dados geoespaciais (Latitude/Longitude) nativamente no banco de dados. Focado em alta coesão e baixo acoplamento, o projeto utiliza as melhores práticas do mercado, como atualizações parciais (PATCH), arquitetura em camadas e DTOs segregados.

## Stack de Tecnologias

  * **Linguagem:** Java 21
  * **Framework principal:** Spring Boot 4.x (Web, Data JPA, Validation)
  * **Bibliotecas principais:** \* `locationtech.jts` (Java Topology Suite para cálculos de SRID 4326 / Geometria)
      * `hibernate-validator` (Validação de beans e CNPJ)
      * `JUnit 5` & `Mockito` (Testes Unitários)
  * **Banco de dados:** Oracle Database (com suporte a Spatial Data)
  * **Infraestrutura:** Maven Wrapper (mvnw), Docker & Docker Compose (Roadmap)

## Principais Funcionalidades

  * **Gerenciamento de Matrizes e Filiais:** CRUD completo com validação de família de CNPJ (a raiz da filial deve obrigatoriamente pertencer à matriz vinculada).
  * **Atualizações Parciais Inteligentes (PATCH):** Endpoints otimizados que permitem a atualização de campos específicos sem a necessidade de enviar payloads completos, protegendo a integridade de dados não alterados.
  * **Inteligência Geográfica:** Conversão automática e transparente de coordenadas (Lat/Lng) vindas do JSON para objetos geométricos `Point` usando o padrão mundial WGS84, prontos para consultas espaciais no banco.
  * **Arquitetura:** Monólito em Camadas (Layered Architecture / N-Tier) baseado no padrão MVC.

-----

## Guia de Onboarding

### Pré-requisitos

Certifique-se de ter os seguintes componentes instalados no seu ambiente antes de começar:

  * **Java SDK:** = 21
  * **Banco de Dados:** Oracle Database (Local ou via Docker)
  * **IDE Recomendada:** VS Code, IntelliJ IDEA ou Eclipse

### Instalação e Configuração

**1. Clone o repositório**

```bash
git clone https://github.com/seu-usuario/cnpj-manager.git
cd cnpj-manager
```

**2. Configure as Variáveis de Ambiente**
O projeto utiliza o conceito de 12-Factor App. Nunca comite credenciais reais.
Crie o seu arquivo de ambiente local baseado no exemplo:

```bash
cp .env.example .env
```

*Edite o arquivo `.env` com as credenciais do seu banco de dados Oracle local.*

**3. Baixe as dependências**
Graças ao Maven Wrapper, você não precisa ter o Maven instalado globalmente.

```bash
# No Linux/macOS
./mvnw clean install -DskipTests

# No Windows
mvnw.cmd clean install -DskipTests
```

### Como Executar

**Desenvolvimento (Local)**
A aplicação pode ser iniciada diretamente via wrapper. Certifique-se de que sua IDE esteja configurada para injetar o `.env` ou exporte as variáveis no terminal antes de rodar:

```bash
./mvnw spring-boot:run
```

**Com Docker (Em Breve)**

```bash
docker compose up -d
```

### Como Executar os Testes

O projeto conta com uma suíte de testes unitários isolados focados nas regras de negócio (Services), utilizando Mockito para simular a camada de persistência.

```bash
./mvnw test
```

-----

## Ondas de Desenvolvimento

| Onda | Nome | Status | Período | Pasta |
| :--- | :--- | :--- | :--- | :--- |
| 1 | MVP & Core Domain | Entregue | 2026-04 | `_docs/1-mvp/` |
| 2 | Conteinerização (Docker) | Planejado | - | `_docs/2-docker/` |
| 3 | CI/CD Pipeline | Planejado | - | `_docs/3-cicd/` |

## Documentação Adicional

