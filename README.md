# 📝 Todo Application

이 프로젝트는 **PostgreSQL 데이터베이스**와 **Render**를 이용해 배포된 Todo 관리 웹 애플리케이션. **Docker Desktop**을 사용하여 로컬 환경에서 테스트하고, **Bootstrap**을 이용해 간단한 프론트엔드를 구성.

---

## 📌 주요 기능
- **Todo 목록 조회**: 등록된 Todo 항목들을 조회.
- **Todo 추가**: 새로운 Todo 항목을 추가.
- **Todo 삭제**: 특정 Todo 항목을 삭제하거나 완료된 항목을 일괄 삭제.
- **Todo 수정**: 기존 Todo 항목의 내용을 수정.
- **Todo 상태 토글**: Todo 항목의 완료 여부를 변경.
- **Todo 상태 필터링**: 완료 여부에 따라 Todo 항목을 필터링.

---

## 🌟 스크린샷
![Screenshot 2024-12-26 160455](https://github.com/user-attachments/assets/5a87a138-3575-4a7a-87bc-1bbc99711f92)
---

## 🌐 배포된 사이트
[Todo Application](https://todo-project-j3jq.onrender.com/)에 접속하여 Todo를 관리

---

## ⚙️ 기술 스택

### Backend
- **Java**: Spring Boot 3.4.1
- **Database**: PostgreSQL
- **ORM**: Hibernate (Spring Data JPA)
- **Deployment**: Render

### Frontend
- **HTML5** / **CSS3**
- **JavaScript**: Axios를 이용한 API 호출
- **Bootstrap**: 간단한 UI 구성

### DevOps
- **Docker**: 로컬 개발 및 테스트 환경 구축
- **Docker Compose**: 서비스 컨테이너화
- **Render**: 클라우드 기반 배포
- **UptimeRobot**: Render 애플리케이션의 슬립 상태 방지를 위해 사용
  
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
```

### 2. Docker Compose 실행
```bash
docker-compose up --build
```

### 3. 애플리케이션 접속
로컬 환경에서 http://localhost:8080 에 접속.


