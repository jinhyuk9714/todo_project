# 📝 Todo Application

이 프로젝트는 **PostgreSQL 데이터베이스**와 **Render**를 이용해 배포된 **Todo 관리 웹 애플리케이션**입니다.  
**Bootstrap**을 사용하여 직관적이고 반응형 UI를 구성하였습니다.

---

## 📌 주요 기능

### ✅ 기본 기능
- **Todo 목록 조회**: 등록된 Todo 항목들을 조회.
- **Todo 추가**: 새로운 Todo 항목을 추가.
- **Todo 삭제**: 
  - 특정 Todo 항목 삭제.
  - 완료된 Todo 항목을 일괄 삭제.
- **Todo 수정**: 기존 Todo 항목의 내용을 수정.
- **Todo 상태 토글**: Todo 항목의 완료 여부를 변경.
- **Todo 상태 필터링**: 완료 여부에 따라 Todo 항목을 필터링.

### 🔐 인증 및 보안
- **사용자 인증**: 로그인 및 회원가입 기능.
- **JWT 기반 인증**: 사용자 토큰 발행 및 유효성 검증.
- **자동 리다이렉트**: 인증되지 않은 사용자는 로그인 페이지로 리다이렉트.

---

## 🌟 스크린샷
### Todo 관리 화면
![Todo List](https://github.com/user-attachments/assets/5a87a138-3575-4a7a-87bc-1bbc99711f92)

---

## 🌐 배포된 사이트
[Todo Application](https://todo-project-j3jq.onrender.com/)  
Render를 통해 배포된 애플리케이션에서 직접 Todo를 관리하세요.

---

## ⚙️ 기술 스택

### Backend
- **Java**: Spring Boot 3.4.1
- **Database**: PostgreSQL
- **ORM**: Hibernate (Spring Data JPA)
- **Authentication**: Spring Security & JWT
- **Deployment**: Render

### Frontend
- **HTML5** / **CSS3**
- **JavaScript**: Axios를 이용한 REST API 호출
- **Bootstrap**: 직관적이고 반응형 UI 구성

### DevOps
- **Render**: 클라우드 기반 배포
- **UptimeRobot**: Render 애플리케이션의 슬립 상태 방지를 위해 사용

---

## 📂 프로젝트 구조

### Backend (`src/main/java/com/example/todo`)
- **controller**: REST API 엔드포인트 관리 (`AuthController`, `TodoController`)
- **service**: 비즈니스 로직 관리 (`UserService`, `TodoService`)
- **repository**: 데이터베이스 연동 (`UserRepository`, `TodoRepository`)
- **entity**: 데이터베이스 엔터티 정의 (`User`, `Todo`)
- **security**: JWT 기반 인증 및 Spring Security 설정

### Frontend (`frontend`)
- **pages**: 로그인, 회원가입, Todo 관리 페이지 (`login.html`, `register.html`, `todos.html`)
- **css**: 스타일시트 (Bootstrap 및 추가 커스텀 CSS)
- **js**: 클라이언트 로직 (`auth.js`, `todos.js`)

---

## 🐳 Docker로 실행하는 방법

로컬 환경에서 **Docker Compose**를 사용해 애플리케이션을 실행.

### 1. .env 파일 생성
`.env` 파일을 프로젝트 디렉토리에 생성하고 아래 내용을 작성:
```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://<DB_URL>:5432/<DB_NAME>
SPRING_DATASOURCE_USERNAME=<DB_USERNAME>
SPRING_DATASOURCE_PASSWORD=<DB_PASSWORD>
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
JWT_SECRET_KEY=your_key
```

### 2. Docker Compose 실행
```bash
docker-compose up --build
```

### 3. 애플리케이션 접속
로컬 환경에서 http://localhost:8080 에 접속.

---

## 🛠️ 사용된 도구 및 설정

### 1. 데이터베이스
- PostgreSQL을 사용하며 `docker-compose.yml`로 컨테이너화.

### 2. 인증
- Spring Security와 JWT(Json Web Token)를 이용해 사용자 인증을 구현.
- 사용자 인증에 따라 Todo 접근 제한.

### 3. 클라이언트
- Axios를 사용해 백엔드와 RESTful API 통신.
- Bootstrap을 활용해 반응형 UI 구성.

---

## 🚀 향후 개선 사항

- **프론트엔드 테스트**: Jest 또는 Cypress를 활용한 테스트 추가.
- **다국어 지원**: 한국어 및 영어 지원.
- **반응형 최적화**: 모바일 친화적인 UI 추가.
- **Todo 알림**: 완료 항목의 알림 기능 추가.

---

## 👨‍💻 개발자
성진혁
