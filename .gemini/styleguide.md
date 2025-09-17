# Team Java Backend Style Guide

이 문서는 우리 팀의 Java 백엔드 개발 코드 스타일 가이드입니다.  
Gemini AI 코드 리뷰어는 이 문서를 기반으로 코드 품질과 일관성을 평가합니다.  
코드는 단순히 작동만 하는 것이 아니라, **유지보수성**, **일관성**, **확장성** 측면에서도 우수해야 합니다.
리뷰는 이해를 돕기 위해 **한글**로 작성합니다.
아래 가이드를 준수하되, 가독성이나 코드 측면에서 더 나은 방향이 있다면 스스로 판단하고 리뷰합니다.

---

## 1. 패키지 구조 및 계층 분리

- 다음과 같은 계층형 구조를 사용합니다:
    - `controller`: HTTP 요청/응답 처리
    - `service`: 비즈니스 로직 처리
    - `repository`: 데이터베이스 접근
    - `dto`: API 입출력 객체 정의
    - `domain` 또는 `entity`: JPA Entity, 핵심 도메인 로직

- 각 계층은 자신의 역할에 집중하며, 상위 계층에 불필요하게 의존하지 않아야 합니다.
- Controller는 로직을 직접 구현하지 않고, Service 또는 Facade 계층에 위임합니다.

---

## 2. 네이밍 컨벤션 (Naming Conventions)

- 클래스: `UpperCamelCase` (예: `UserController`, `UserService`)
- 메서드 / 변수: `lowerCamelCase` (예: `getUserList`, `userId`)
- 상수: `UPPER_CASE_WITH_UNDERSCORES`
- 패키지/모듈: `lowercase.with.dots`
- DTO 클래스는 `Request`, `Response` 접미어를 명시합니다 (예: `LoginRequest`, `UserResponse`)

---

## 3. DTO와 Entity 분리

- Entity는 API 응답에 직접 노출하지 않습니다.
- Request / Response DTO는 명확히 분리하며, 필요한 경우에만 Mapper 또는 변환 로직을 작성합니다.
- Request DTO에는 검증 어노테이션을 명시적으로 작성합니다. (예: `@Valid`, `@NotNull`, `@Size`)

---

## 4. 예외 처리 및 로깅

- 예외는 전역 처리 방식(`@ControllerAdvice`)을 사용합니다.
- 가능한 한 **구체적인 예외 클래스**를 선언합니다.  
  (ex: `UserNotFoundException`, `InvalidInputException`)
- 로그 레벨은 상황에 맞게 `INFO`, `WARN`, `ERROR`를 사용합니다.
- 민감한 정보(비밀번호 등)는 로그에 출력하지 않도록 주의합니다.

---

## 5. 코드 스타일 및 문법

- 들여쓰기는 4 space를 사용합니다.
- 줄 길이는 120자 이내로 제한합니다.
- `@Override`, `@Transactional`, `@Slf4j` 등 어노테이션은 명확히 작성합니다.
- 유틸리티 메서드는 `static`으로 작성하고, 공통 모듈로 분리합니다.
- early return 패턴을 권장합니다.
  - 조건이 맞지 않는 경우 바로 return/throw 하여 코드 중첩을 최소화.
  - 불필요한 else 블록은 지양.
 
---

## 6. 테스트 원칙

- 단위 테스트는 JUnit 5 (`@Test`)를 사용합니다.
- Mocking은 Mockito 또는 Spring의 `@MockBean`을 사용합니다.
- 테스트 메서드는 `shouldDoSomething_whenCondition` 형식으로 작성합니다.
- given-when-then 패턴을 지향합니다.
- Service 단위에서의 단위 테스트를 우선시하고, 필요한 경우 통합 테스트를 작성합니다.

---

## 7. 비즈니스 로직 분리 및 재사용

- Controller에서는 비즈니스 로직을 구현하지 않고 Service에 위임합니다.
- 중복 로직은 Helper/Facade 레이어로 분리하여 재사용합니다.
- Service는 하나의 책임 단위로 기능을 분리하고, 복잡한 흐름은 별도 도메인 모델 또는 객체로 이동합니다.
- 자주 사용되는 유틸이나 기능들은 common 디렉토리 안에 위치할 수 있습니다.
- 단일 메서드에서만 사용되는 확장가능성이 없는 로직이라면 불필요하게 유틸/헬퍼로 분리하지 않습니다. (과도한 추상화 방지)

---

## 8. 확장성과 유지보수성 고려

- 추후 기능 추가 시, 다른 기능에 영향을 최소화할 수 있는 구조로 설계합니다.
- 필요한 경우 인터페이스, 추상 클래스 등으로 계층 확장성을 고려합니다.
- 강한 결합을 피하고, 의존성 주입을 통해 유연한 구조를 유지합니다.

---

## 9. 데이터베이스 관련 규칙

- JPA (RDB) 사용 시 dirty checking이 동작하므로, 변경 감지를 적극 활용.
- MongoDB는 dirty checking이 기본적으로 동작하지 않는다는 것을 인지.
  
---

## 10. Gemini 리뷰 기준 요약 (내재된 코드 리뷰 규칙)

Gemini는 아래 기준에 따라 코드를 리뷰합니다:

- 계층 구조가 올바르게 분리되어 있는가?
- DTO/Entity/Service/Controller 간 역할이 명확한가?
- 예외 처리와 로깅이 일관되고 구체적인가?
- 테스트가 가능하고, 유지보수성 높은 구조인가?
- 코드 스타일, 네이밍, 포맷팅이 일관되는가?
- 더 객체지향적인 방식 또는 리팩토링 포인트가 있는가?

> 단순히 “작동하는 코드”가 아닌, 팀의 스타일 가이드에 부합하며 실무적으로 적합한 품질 수준의 코드를 목표로 합니다.
