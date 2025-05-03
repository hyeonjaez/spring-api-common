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
    implementation group: 'io.github.hyeonjaez', name: 'spring-api-common', version: '0.0.1'
}
```
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
    <groupId>io.github.hyeonjaez</groupId>
    <artifactId>spring-api-common</artifactId>
    <version>0.0.1</version>
</dependency>
```
</details>

---

## ✅ Features

- Unified success response wrapper (`ApiResponse<T>`)
- Standardized error handling with `ErrorResponse`
- Predefined error codes in `CommonErrorCode`
- Domain-specific `BusinessException` and `ErrorCode` abstraction
- Extendable global exception handlers
- Singleton `EmptyResponse` for null-free design
- `ObjectsUtil` for null/ID validation
- `GraphQL Global Exception Handling` via a DataFetcherExceptionResolver implementation that produces consistent error payloads in errors.extensions

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
├── graphql           # GraphQL integration
│   ├── AbstractGraphQLExceptionResolver.java
│   └── GraphQLExceptionResolver.java
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
### 4. GraphQL Global Exception Handling
Any BusinessException or common framework exception thrown in a `@QueryMapping` or `@MutationMapping` will be converted into a GraphQL error with standardized extensions.

### Example Resolver:
```java
@QueryMapping
public UserDto getUserById(@Argument Long id) {
    UserDto user = userService.findById(id);
    if (user == null) {
        throw new BusinessException(UserErrorCode.USER_NOT_FOUND);
    }
    return user;
}
```

### ❌ Sample Error Response
```json
{
  "errors": [
    {
      "message": "User not found",
      "extensions": {
        "status": "FAILURE",
        "statusCode": 404,
        "errorCode": "USER-001"
      }
    }
  ],
  "data": {
    "getUserById": null
  }
}
```
Errors are always returned with HTTP 200, and clients should inspect errors[*].extensions.errorCode or statusCode to handle failures.

---

## 🧪 Test & Compatibility

- All classes are unit-tested (e.g., `ApiResponseTest`, `ErrorResponseTest`, etc.)
- Compatible with Java 17 (partial support for Java 11)
- Spring Boot 3.2 tested
- Tested with Spring for GraphQL 1.1+

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
- Spring for GraphQL 1.1+

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

---

## 👥 Contributors

Thanks to the following people who have contributed to this project 💖

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/hyeonjaez">
        <img src="https://avatars.githubusercontent.com/hyeonjaez" width="80px;" alt="hyeonjaez"/>
        <br />
        <sub><b>Fiat_lux</b></sub>
      </a>
      <br />
      <sup>Creator & Maintainer</sup>
    </td>
    <td align="center">
      <a href="https://github.com/hansnam1105">
        <img src="https://avatars.githubusercontent.com/hansnam1105" width="80px;" alt="username1"/>
        <br />
        <sub><b>Seungnam Han</b></sub>
      </a>
      <br />
      <sup>Contributor</sup>
    </td>
    <!-- Add more contributors below in the same format -->
  </tr>
</table>

Want to contribute? Feel free to submit a pull request or open an issue!  
Let’s build something great together. 🚀

**Made with care by [@hyeonjaez](https://github.com/hyeonjaez)**
