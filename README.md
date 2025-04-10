# 🌐 Spring API Common Library

Spring Boot 프로젝트에서 API 응답 및 예외 처리를 일관성 있게 관리할 수 있도록 도와주는 공통 라이브러리입니다.

> ✅ 개발자가 API 응답 포맷, 예외 처리 구조, null 검증 로직 등에서 발생할 수 있는 반복 작업을 최소화하고, 표준화된 개발 문화를 확산시키는 것을 목표로 합니다.

---
## 📦 Features

### ✅ 표준 API 응답 구조
- `ApiResponse<T>`: 모든 응답을 감싸는 공통 객체
- `ApiStatus`: SUCCESS / FAILURE / ERROR 응답 상태
- `EmptyResponse`: 응답 데이터가 없을 때 사용

### ✅ 편리한 응답 생성 유틸리티
- `ApiResponseUtil`: `ok()`, `created()`, `noContent()` 등 ResponseEntity 래핑 도우미

### ✅ 전역 예외 처리
- `GlobalExceptionHandler`: `BusinessException` 공통 처리
- `AbstractGlobalExceptionHandler`: 400, 404, 405, 500 기본 처리 핸들러 제공
- `@RestControllerAdvice`로 한 줄 설정

### ✅ 커스텀 예외 구조
- `ErrorCode` 인터페이스
- `CommonErrorCode` enum 구현
- `BusinessException` 예외

### ✅ 유틸리티
- `ObjectsUtil`: null 체크, ID 유효성 검사 지원

---

## 📁 패키지 구조

```
com.github.hyeonjaez.springcommon
│
├── exception         # 예외 코드, 비즈니스 예외 정의
│   ├── BusinessException.java
│   ├── ErrorCode.java
│   └── CommonErrorCode.java
│
├── handler           # 전역 예외 핸들러 및 에러 응답 구조
│   ├── GlobalExceptionHandler.java
│   ├── AbstractGlobalExceptionHandler.java
│   └── ErrorResponse.java
│
├── response          # API 응답 관련 클래스
│   ├── ApiResponse.java
│   ├── ApiResponseUtil.java
│   ├── ApiStatus.java
│   └── EmptyResponse.java
│
└── util              # 공통 유틸리티
    └── ObjectsUtil.java
```

---

## 🔧 설치 방법

### 1. Maven 설정

```xml
<dependency>
  <groupId>com.github.hyeonjaez</groupId>
  <artifactId>spring-api-common</artifactId>
  <version>1.0.0</version>
</dependency>
```

> ※ 실제 등록 전이라면 로컬 설치:
```bash
./gradlew publishToMavenLocal
```

---

## ⚙️ 사용법

### 1. 예외 처리 적용

```java
@RestControllerAdvice
public class MyExceptionHandler extends AbstractGlobalExceptionHandler {
    // 필요 시 오버라이딩하여 커스터마이징 가능
}
```

### 2. 응답 포맷 사용 예시

```java
@GetMapping("/users/{id}")
public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable Long id) {
    UserDto user = userService.findById(id);
    return ApiResponseUtil.ok("User fetched successfully", user);
}
```

### 3. 커스텀 예외 정의

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

---

## 🧪 테스트 코드

- 모든 유틸리티, 응답, 예외 처리 클래스에 대해 단위 테스트 작성 완료
- `TestController`를 통해 API 테스트 가능
- Java 17 기준으로 테스트 통과 (Java 11 일부 지원 고려)

---

## 📝 확장/커스터마이징 팁

- `AbstractGlobalExceptionHandler`를 상속한 후 예외 핸들러 메서드를 추가하면 커스터마이징 가능
- 도메인별 `ErrorCode` enum을 자유롭게 추가해 활용
- `ObjectsUtil`은 개발 상황에 따라 `String`, `List`, `Enum` 등의 유효성 검사로 확장 가능

---

## 🛠 Tech Stack

- Java 17+ (Java 11 partially supported)
- Spring Boot 3.2
- Jakarta Validation
- JUnit 5

---

## 📜 라이선스

MIT License  
Copyright (c) 2024  
[정재현 GitHub](https://github.com/hyeonjaez)

---

## 📮 문의 및 기여

이 프로젝트는 누구나 기여할 수 있습니다.  
PR이나 이슈 환영합니다!
