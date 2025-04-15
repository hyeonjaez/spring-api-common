# 🌱 spring-api-common

**A lightweight, opinionated Spring-based library for standardized API responses and exception handling.**  
This library is designed with a focus on **minimal dependencies**, **clean error handling**, and **null-free responses** through the use of an `EmptyResponse` object.

---

## 🎯 Philosophy

This library was built based on the following principles:

- **🚫 Null-Free Design**  
  API responses should never return `null`. Instead, a singleton `EmptyResponse` object ensures empty payloads are always predictable.

- **🔗 Minimal Dependencies**  
  No third-party dependencies beyond Spring Web and Jakarta Validation — lightweight and integration-friendly.

- **📐 Consistent Structure**  
  All success and error responses follow a unified format (`ApiResponse<T>`, `ErrorResponse`), making it easier to parse and document.

- **🧰 Developer-Centric Utilities**  
  Includes utilities like `ApiResponseUtil`, `ObjectsUtil`, and fluent builders to reduce boilerplate code.

- **🧩 Easy to Customize**  
  Extend `GlobalExceptionHandler` or `AbstractGlobalExceptionHandler` and plug it into your project via `@RestControllerAdvice`.

---

## 📦 Installation

<details>
<summary>Gradle</summary>

```groovy
dependencies {
    implementation 'io.github.hyeonjaez:spring-api-common:0.0.1'
}
```
</details>

<details>
<summary>Maven</summary>

```xml
<dependency>
  <groupId>com.github.hyeonjaez</groupId>
  <artifactId>spring-api-common</artifactId>
  <version>0.0.1</version>
</dependency>
```
</details>

> ※ If not published yet, use local install:
```bash
./gradlew publishToMavenLocal
```

---

## ✅ Features

- Unified success response wrapper (`ApiResponse<T>`)
- Standardized error handling with `ErrorResponse`
- Predefined error codes in `CommonErrorCode`
- Domain-specific `BusinessException` and `ErrorCode` abstraction
- Extendable global exception handlers
- Singleton `EmptyResponse` for null-free design
- `ObjectsUtil` for null/ID validation

---

## 📁 Package Structure

```
com.github.hyeonjaez.springcommon
│
├── exception         # Error codes & business exceptions
│   ├── BusinessException.java
│   ├── ErrorCode.java
│   └── CommonErrorCode.java
│
├── handler           # Global exception handlers
│   ├── GlobalExceptionHandler.java
│   ├── AbstractGlobalExceptionHandler.java
│   └── ErrorResponse.java
│
├── response          # API response wrappers
│   ├── ApiResponse.java
│   ├── ApiResponseUtil.java
│   ├── ApiStatus.java
│   └── EmptyResponse.java
│
└── util              # Validation utility
    └── ObjectsUtil.java
```

---

## ⚙️ Usage

### 1. Global Exception Handler

```java
@RestControllerAdvice
public class MyExceptionHandler extends AbstractGlobalExceptionHandler {
    // Override default handlers as needed
}
```

### 2. Success Response Example

```java
@GetMapping("/users/{id}")
public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable Long id) {
    UserDto user = userService.findById(id);
    return ApiResponseUtil.ok("User fetched successfully", user);
}
```

### ✅ Sample Success Response

```json
{
  "status": "SUCCESS",
  "message": "User fetched successfully",
  "data": {
    "id": 1,
    "name": "Jaehyun",
    "email": "jaehyun@example.com"
  }
}
```

### 3. Custom ErrorCode

```java
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-001", "User not found");
}
```

```java
if (user == null) {
    throw new BusinessException(UserErrorCode.USER_NOT_FOUND);
}
```
### ❌ Sample Error Response
```json
{
  "status": "FAILURE",
  "statusCode": 404,
  "errorCode": "USER-001",
  "message": "User not found"
}
```

---

## 🧪 Test & Compatibility

- All classes are unit-tested (e.g., `ApiResponseTest`, `ErrorResponseTest`, etc.)
- Compatible with Java 17 (partial support for Java 11)
- Spring Boot 3.2 tested

---

## 📝 Extension Tips

- Implement `ErrorCode` per domain (e.g., `UserErrorCode`, `AuthErrorCode`)
- Override methods in `AbstractGlobalExceptionHandler` to handle custom scenarios
- Extend `ObjectsUtil` for validating strings, collections, enums, etc.

---

## 🛠 Tech Stack

- Java 17+
- Spring Boot 3.2+
- Jakarta Validation
- JUnit 5

---

## 📜 License

MIT License  
Copyright (c) 2024  
[정재현 GitHub](https://github.com/hyeonjaez)

---

## 🙌 Contributing

Contributions are welcome!

- 🐛 Found a bug?
- 💡 Have a new idea or improvement?
- 📄 Want to improve documentation?

Feel free to open an issue or submit a pull request.  
Let’s make Spring API development clearer and more robust — together.

**Made with care by [@hyeonjaez](https://github.com/hyeonjaez)**
