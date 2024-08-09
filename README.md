# Task Management System

## Описание
Это API для системы управления задачами, разработанное с использованием Java, Spring Boot и PostgreSQL. Сервис предоставляет возможности для создания, редактирования, удаления и просмотра задач, а также добавления комментариев к задачам. API также поддерживает аутентификацию и авторизацию с использованием JWT.

## Стек технологий
- Java 17+
- Spring Boot
- Spring Security
- JWT
- PostgreSQL
- Docker & Docker Compose
- Swagger/OpenAPI

## Требования
- Docker
- Docker Compose
- Java 17+

## Установка и запуск

### Локальная установка

1. Клонируйте репозиторий:
   git clone https://github.com/your-username/task-management-system.git
   cd task-management-system


2. Настройте файл application.properties в src/main/resources:
   spring.datasource.url=jdbc:postgresql://localhost:5432/taskmanagement
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true

   spring.security.jwt.secret=your_secret_key
   spring.security.jwt.expiration-ms=86400000


3. Соберите проект с помощью Maven:
   ./mvnw clean package


4. Запустите приложение:
   java -jar target/task-management-system.jar


### Запуск с использованием Docker

1. Создайте файл .env в корне проекта:
   POSTGRES_DB=taskmanagement
   POSTGRES_USER=your_username
   POSTGRES_PASSWORD=your_password


2. Запустите сервисы с использованием Docker Compose:
   docker-compose up --build


3. Приложение будет доступно по адресу http://localhost:8080.

## API Документация

API документация доступна по адресу:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Запуск тестов
Для запуска тестов выполните следующую команду:
```bash
./mvnw test