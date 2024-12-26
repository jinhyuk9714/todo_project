# 📝 Todo Application

이 프로젝트는 PostgresQL 데이터베이스와 Render를 이용해 배포된 Todo 관리 웹 애플리케이션입니다. Docker Desktop을 사용하여 로컬 환경에서 테스트하고, Bootstrap을 사용해 간단한 프론트엔드를 구성했습니다.

## 📌 주요 기능
- **Todo 목록 조회**: 등록된 Todo 항목들을 조회합니다.
- **Todo 추가**: 새로운 Todo 항목을 추가합니다.
- **Todo 삭제**: 완료된 Todo 항목을 삭제합니다.

## 🌐 배포된 사이트
[Todo Application](https://todo-project-j3jq.onrender.com/)에 접속하여 Todo를 관리해보세요!

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

---

## 🐳 Docker로 실행하는 방법
로컬 환경에서 Docker Compose를 사용해 실행할 수 있습니다.

### 1. Docker Compose 실행
```bash
docker-compose up --build
