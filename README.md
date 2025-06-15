![CI](https://github.com/irinakomarchenko/user-service-spring/actions/workflows/ci.yml/badge.svg)

[![Maintainability](https://qlty.sh/badges/a56c7491-b9be-4239-964a-541250c083e3/maintainability.svg)](https://qlty.sh/gh/irinakomarchenko/projects/user-service-spring)

# User Service Spring

**User Service Spring** — REST API-сервис на Spring Boot для управления пользователями (CRUD).

---

## Технологии

- Java 22
- Spring Boot
- Spring Web (REST API)
- Spring Data JPA (встроенный Hibernate)
- PostgreSQL (через Docker Compose)
- SLF4J + Logback (логирование)
- JUnit 5 + MockMvc (тестирование контроллеров и API)
- Maven (сборка и зависимости)
- Checkstyle (проверка стиля кода)
- Lombok — (автогенерации геттеров/сеттеров)
---

## Запуск проекта

### 1. Клонировать репозиторий

```sh
git clone https://github.com/ТВОЙ_ЛОГИН/user-service-hibernate.git
cd user-service-hibernate
```
### 2 Запустить базу данных PostgreSQL
```sh
docker compose up -d
```
### 3. Собрать проект и проверить стиль кода

```sh
mvn clean install
mvn checkstyle:check
```
### 4. Запустить тесты

```sh
mvn test
```
### 5. Формирование отчета о тестах

```sh
mvn verify
```

### 6. Запустить приложение


```sh
mvn clean package
java -jar target/user-service-spring.jar
```

### 7. Пример работы приложения
![img_1.png](readme-resources/img_1.png)
