# Task Management API

REST API для управления задачами.

## 🚀 Технологии

- Java 21
- Spring Boot 4
- Spring Data JPA
- PostgreSQL
- Flyway
- Docker / Docker Compose
- Swagger (OpenAPI 3)
- JUnit 5 + Mockito

## 📋 Функциональность

- ✅ CRUD операции для задач (Create, Read, Update, Delete)
- ✅ Фильтрация задач по статусу (`PENDING`, `IN_PROGRESS`, `COMPLETED`)
- ✅ Валидация входных данных
- ✅ Глобальная обработка ошибок
- ✅ Автоматические миграции БД (`Flyway`)
- ✅ Документация API (`Swagger`)

## 🏃‍♂️ Запуск проекта

### Требования

Установленный [Docker](https://www.docker.com/get-started/) и Docker Compose

### Запуск

```bash
# Склонировать репозиторий
git clone https://github.com/trifinyuk/task-management-api.git
cd task-management-api

# Запустить приложение
docker compose up --build
```

## 📚 API Документация

После запуска проекта Swagger UI доступен по адресу:
http://localhost:8080/swagger-ui.html

## 📬 Postman Collection

Готовая коллекция для тестирования API:  
👉 [postman_collection.json](postman_collection.json)

Импортируйте файл в Postman (File → Import).

### Примеры запросов

**Создание задачи:**
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Learn Spring Boot","description":"Complete pet project","status":"IN_PROGRESS"}'
```

**Получение всех задач:**
```bash
curl http://localhost:8080/api/tasks
```

**Получение задачи по ID:**
```bash
curl http://localhost:8080/api/tasks/1
```

**Фильтрация по статусу:**
```bash
curl http://localhost:8080/api/tasks?status=PENDING
```

**Обновление задачи:**
```bash
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Updated Title","description":"Updated Desc","status":"COMPLETED"}'
```

**Удаление задачи:**
```bash
curl -X DELETE http://localhost:8080/api/tasks/1
```

## 🧪 Тестирование

Для запуска unit-тестов локально (требуется Java 21 и Maven):

```bash
# Запустить все тесты
./mvnw test
```

## 📁 Структура проекта

```
src/main/java/.../
├── controller/     # REST контроллеры
├── service/        # Бизнес-логика
├── repository/     # JPA репозитории
├── model/          # Entity и Enums
├── dto/            # Data Transfer Objects
├── exception/      # Глобальный обработчик ошибок
└── config/         # Конфигурации (Swagger)
```

## 👨‍💻 Автор

GitHub: [trifinyuk](https://github.com/trifinyuk)

## 📄 Лицензия

MIT