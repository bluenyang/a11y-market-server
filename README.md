# 🛒 A11yMarket Server

[![Java](https://img.shields.io/badge/Java-21-orange?logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-green?logo=springboot)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue?logo=docker)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)](LICENSE)

**A11yMarket**은 누구나 제약 없이 쇼핑을 즐길 수 있는 **접근성(Accessibility, A11y) 중심의 중개 이커머스 플랫폼**입니다.
이 저장소는 A11yMarket의 백엔드 서버 프로젝트로, **Layered Architecture**를 기반으로 설계되었으며 안정적인 서비스 제공을 위해 신뢰성 높은 기술 스택을 채택했습니다.

---

## 📖 목차 (Table of Contents)

- [프로젝트 소개 (Introduction)](#-프로젝트-소개-introduction)
- [시스템 아키텍처 (System Architecture)](#-시스템-아키텍처-system-architecture)
- [주요 기능 (Key Features)](#-주요-기능-key-features)
- [기술 스택 (Tech Stack)](#-기술-스택-tech-stack)
- [시작하기 (Getting Started)](#-시작하기-getting-started)
- [배포 가이드 (Deployment)](#-배포-가이드-deployment)
- [설정 가이드 (Configuration)](#-설정-가이드-configuration)
- [API 문서 (API Documentation)](#-api-문서-api-documentation)

---

## 📢 프로젝트 소개 (Introduction)

A11yMarket은 시각 장애인, 고령자 등 디지털 소외 계층을 포함한 모든 사용자가 편리하게 이용할 수 있도록 **맞춤형 접근성 UI**를 제공하는 것을 최우선 목표로 합니다.
판매자와 구매자를 연결하는 중개몰(Open Market) 형태이며, AI 기술을 활용하여 상품 정보의 접근성을 획기적으로 개선했습니다.

![MainPageAndA11ySetting](./.github/resources/1.png)

---

## 🏗 시스템 아키텍처 (System Architecture)

본 프로젝트는 유지보수와 확장이 용이한 **Layered Architecture**를 따르며, Docker 환경에서 Traefik을 로드밸런서로 사용하여 안정적인 서비스를 제공합니다.

![Architecture](./.github/resources/Architecture.png)

### Spring Boot 서버 아키텍처 (Architecture for Spring Boot Server)

![SpringBootArchitecture](./.github/resources/SpringArchitecture.png)

- **Client (React):** 사용자 인터페이스를 담당하며 REST API를 통해 서버와 통신합니다.
- **Controller (Presentation Layer):** 클라이언트의 요청(Request)을 받아 유효성을 검증하고, 적절한 응답(Response)을 반환합니다.
- **Service (Business Layer):** 핵심 비즈니스 로직(주문 처리, 결제 검증 등)을 수행하며 트랜잭션을 관리합니다.
- **Repository (Data Access Layer):** JPA를 사용하여 Oracle DB와의 데이터 CRUD 작업을 전담합니다.
- **Infrastructure & External Services:**
  - **Google Gemini AI:** 상품 이미지 분석 및 요약 텍스트 생성
  - **Toss Payments:** 안전한 결제 및 정산 처리
  - **S3 (Compatible):** 상품 이미지 및 정적 리소스 저장
  - **Kakao OAuth:** 소셜 로그인 인증 처리

---

## ✨ 주요 기능 (Key Features)

| 기능                         | 설명                                                                                                     |
| :--------------------------- | :------------------------------------------------------------------------------------------------------- |
| **♿ 맞춤형 접근성 프로필**  | 사용자의 시력, 색각 등 신체적 특성에 맞춘 접근성 설정(글자 크기, 대비 등)을 프로필로 관리                |
| **🤖 AI 상품 분석 (Gemini)** | 상품 등록 시 **Google Gemini 2.0 Flash** 모델이 이미지를 분석하여 시각 장애인을 위한 상세 요약 정보 생성 |
| **💳 신뢰할 수 있는 결제**   | **Toss Payments** 연동을 통해 안전하고 간편한 결제 및 정산 시스템 구축                                   |
| **⭐ 투명한 판매자 등급**    | 판매 활동 데이터를 분석하여 판매자 등급을 산정 및 공개, 구매자 신뢰도 확보                               |
| **🔐 간편 인증**             | JWT 기반 인증 및 **Kakao OAuth2**를 이용한 소셜 로그인 지원                                              |

---

## 🛠 기술 스택 (Tech Stack)

### Backend

| 분류          | 기술              | 버전 / 상세                           |
| :------------ | :---------------- | :------------------------------------ |
| **Language**  | Java              | 21 (LTS)                              |
| **Framework** | Spring Boot       | 3.5.7                                 |
| **Security**  | Spring Security   | OAuth2 Client (Kakao), JWT            |
| **Database**  | Oracle / H2       | `ojdbc17`, JPA (Hibernate)            |
| **AI**        | Spring AI         | `spring-ai-openai` (Gemini)           |
| **Storage**   | S3 (MinIO)        | `spring-cloud-aws-starter-s3`         |
| **Docs**      | Swagger           | `springdoc-openapi-starter-webmvc-ui` |
| **Utils**     | Lombok, MapStruct | 코드 간소화 및 매핑                   |

### DevOps & Infrastructure

| 분류          | 기술    | 상세                         |
| :------------ | :------ | :--------------------------- |
| **Container** | Docker  | Dockerfile, Docker Compose   |
| **Registry**  | GHCR    | GitHub Container Registry    |
| **Proxy**     | Traefik | Reverse Proxy, Load Balancer |
| **Build**     | Gradle  | 빌드 및 의존성 관리          |

---

## 🚀 시작하기 (Getting Started)

로컬 개발 환경에서 프로젝트를 실행하는 방법입니다.

### 사전 요구사항 (Prerequisites)

- Java 21 Development Kit
- Docker & Docker Compose
- Oracle Database (또는 로컬 테스트용 H2)

### 설치 및 실행 (Installation)

1. **레포지토리 클론 (Clone)**

   ```bash
   git clone https://github.com/gamesung-coding/a11y-market-server.git
   cd a11y-market-server
   ```

2. **환경 변수 설정 (Environment Setup)**

- `src/main/resources/application.yaml` 파일을 수정하거나, `application-develop.yaml`로 생성합니다.
- 필요한 API Key(DB, Kakao, Toss, Gemini, AWS)를 입력합니다.
- `application-develop.yaml`의 경우, Spring Boot 실행 시 profile을 `develop`으로 지정해야 합니다.

3. **애플리케이션 실행 (Run)**
   ```bash
   ./gradlew bootRun
   ```

---

## 🐳 배포 가이드 (Deployment)

운영(Production) 환경에서는 `.docker/` 디렉토리에 포함된 설정 파일들을 사용하여 간편하게 배포할 수 있습니다.

### 1. 배포 파일 준비

프로젝트의 `.docker` 폴더 내에 있는 다음 두 파일을 배포 서버의 동일한 경로에 위치시킵니다.

- `docker-compose.production.yaml`
- `.env`

### 2. 환경 변수 설정 (.env)

`.env` 파일을 열어 운영 환경에 맞는 값으로 수정합니다.

| 변수명                            | 설명                                                               | 예시                                                   |
| --------------------------------- | ------------------------------------------------------------------ | ------------------------------------------------------ |
| **HOST_NAME**                     | 서비스 도메인 (Traefik 라우팅용)                                   | `api.a11ymarket.com`                                   |
| **DB_URL**                        | Oracle DB 접속 URL                                                 | `jdbc:oracle:thin:@db-host:1521:xe`                    |
| **DB_USERNAME**                   | DB 사용자명                                                        | `a11y_admin`                                           |
| **DB_PASSWORD**                   | DB 비밀번호                                                        | `secure_password`                                      |
| **JPA_SHOW_SQL**                  | JPA SQL 출력 여부 (`true` 또는 `false`)                            | `false`                                                |
| **JPA_HIBERNATE_DDL_AUTO**        | JPA DDL 자동 생성 옵션 (`none`, `update`, `create`, `create-drop`) | `update`                                               |
| **GEMINI_API_KEY**                | Google Gemini AI API 키                                            | `AIzaSy...`                                            |
| **S3_REGION**                     | S3 리전 (MinIO 사용 시 임의 값 가능)                               | `us-east-1`                                            |
| **S3_ACCESS_KEY**                 | S3 액세스 키                                                       | `minio_access_key`                                     |
| **S3_SECRET_KEY**                 | S3 시크릿 키                                                       | `minio_secret_key`                                     |
| **S3_ENDPOINT_URL**               | S3 엔드포인트 URL                                                  | `http://minio-server:9000`                             |
| **S3_BUCKET_NAME**                | S3 버킷 이름                                                       | `a11y-market-bucket`                                   |
| **KAKAO_CLIENT_ID**               | 카카오 로그인 REST API 키                                          | `kakao_key...`                                         |
| **KAKAO_CLIENT_SECRET**           | 카카오 로그인 REST API 시크릿 키                                   | `kakao_secret...`                                      |
| **KAKAO_REDIRECT_URI**            | 카카오 로그인 리다이렉트 URI                                       | `https://api.a11ymarket.com/auth/kakao/callback`       |
| **OAUTH_REDIRECT_URI**            | OAuth 공통 리다이렉트 URI                                          | `https://a11ymarket.croffledev.kr/auth/oauth-redirect` |
| **OAUTH_SIGNUP_REDIRECT_URI**     | OAuth 회원가입 리다이렉트 URI                                      | `https://a11ymarket.croffledev.kr/auth/oauth-signup`   |
| **LOGGING_LEVEL_ROOT**            | 전체 로깅 레벨 (`TRACE`, `DEBUG`, `INFO`, `WARN`, `ERROR`)         | `INFO`                                                 |
| **CORS_ALLOWED_ORIGINS**          | CORS 허용 출처 (콤마(,)로 다중 출처 구분)                          | `https://a11ymarket.croffledev.kr`                     |
| **JWT_SECRET_KEY**                | JWT 서명용 비밀키 (32자 이상)                                      | `very_secret_key...`                                   |
| **JWT_ACCESS_TOKEN_VALIDITY_MS**  | JWT 액세스 토큰 유효 기간 (밀리초 단위)                            | `900000` (15분)                                        |
| **JWT_REFRESH_TOKEN_VALIDITY_MS** | JWT 리프레시 토큰 유효 기간 (밀리초 단위)                          | `604800000` (7일)                                      |

|
| **TOSS_PAYMENT_SECRET_KEY** | 토스 페이먼츠 시크릿 키 | `sk_test...` |
| **SWAGGER_DOCS_ENABLED** | Swagger API 문서 활성화 여부 (`true` 또는 `false`) | `true` |
| **SWAGGER_UI_ENABLED** | Swagger UI 활성화 여부 (`true` 또는 `false`) | `true` |

> **Note:** 보안을 위해 민감한 정보(API 키, DB 비밀번호 등)는 절대 공개 저장소에 커밋하지 마세요.

### 3. 컨테이너 실행

최신 이미지를 받아오고 서비스를 실행합니다.

```bash
# 1. 최신 이미지 Pull (GHCR)
docker-compose -f docker-compose.production.yaml pull

# 2. 컨테이너 실행 (Background)
docker-compose -f docker-compose.production.yaml up -d
```

> **Note:** 최초 배포 이후 업데이트 시에도 위 pull 및 up -d 명령어를 순서대로 실행하면 무중단(또는 최소 중단)으로 업데이트가 반영됩니다.

---

## 📚 API 문서 (API Documentation)

서버 실행 시, `.env`에서 `SWAGGER_DOCS_ENABLED`를 `true`로 설정했다면, 서버를 실행한 후 아래 주소에서 Swagger UI를 통해 API 명세를 확인하고 테스트할 수 있습니다.

- **URL:** `http://localhost:8080/swagger-ui.html`

[이미지4:Swagger UI 실행 화면 스크린샷]

> **Note:** 보안이 적용된 API를 호출하려면 우측 상단의 `Authorize` 버튼을 클릭하고 `Bearer {AccessToken}` 형태로 인증 토큰을 입력해야 합니다.

---

## 🤝 Contributing

기여는 언제나 환영합니다! 버그 신고, 기능 제안, PR 등은 Issue 탭을 이용해 주세요.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request
