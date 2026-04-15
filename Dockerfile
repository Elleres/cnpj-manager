# Estágio 1: Build (Compilação)
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /build

# Copia os arquivos do Maven Wrapper primeiro (para aproveitar o cache do Docker)
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Dá permissão de execução ao script do Maven e baixa as dependências
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline -B

# Copia o código fonte e compila o .jar (ignorando os testes que pausamos)
COPY src/ ./src/
RUN ./mvnw clean package -DskipTests

# Estágio 2: Run (Execução)
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia APENAS o .jar gerado no estágio anterior
COPY --from=builder /build/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]