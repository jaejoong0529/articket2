# Articket - 입찰 기반 중고 경매 플랫폼

Articket은 중고거래에 경매 방식을 결합한 백엔드 중심 프로젝트입니다. 단순 상품 CRUD보다, 동시에 들어오는 입찰 요청에서 최고가, 잔액, 낙찰, 거래 데이터가 어긋나지 않도록 만드는 것을 핵심 과제로 잡았습니다.

## 핵심 포인트

- JPA `PESSIMISTIC_WRITE` 락과 `@Transactional` 경계로 동일 상품 입찰을 DB 기준으로 직렬화
- 이전 최고 입찰자 자동 환불, 신규 입찰자 잔액 선차감, 낙찰 거래 생성 흐름 구현
- 즉시구매 시 기존 최고 입찰자 환불 누락을 보완해 거래 정합성 개선
- Redis 기반 Refresh Token 저장과 Access Token blacklist 로그아웃 처리
- 관리자 API 인가 경로 정리 및 method security 활성화
- H2 기반 테스트 프로필과 핵심 도메인 단위 테스트 추가
- 상품 목록 응답에 최고 입찰가를 포함해 프론트의 상품별 반복 API 호출 제거

## 기술 스택

| 영역 | 기술                                                              |
| --- |-----------------------------------------------------------------|
| Backend | Java 17, Spring Boot 3.4, Spring Security, JWT, Spring Data JPA |
| Database / Cache | MySQL, Redis, H2(test)                                          |
| Frontend | React, Axios, React Router                                      |
| Test | JUnit 5, Mockito, AssertJ                                       |

## 주요 기능

### 인증 / 회원

- 회원가입, 로그인, 로그아웃
- JWT Access Token / Refresh Token 재발급
- Redis Refresh Token 저장
- 로그아웃된 Access Token blacklist 처리
- 보유 금액 충전
- 아이디 찾기, 임시 비밀번호 메일 발송

### 상품 / 입찰 / 거래

- 상품 등록, 수정, 삭제, 이미지 업로드
- 상품 목록, 카테고리 검색, 상세 조회
- 시작가 이상 입찰 검증
- 최고 입찰자 자동 환불
- 입찰자 잔액 선차감
- 즉시구매 및 기존 최고 입찰자 환불
- 경매 종료 상품 자동 낙찰 처리
- 구매 / 판매 거래 내역 조회

### 관리자

- 회원, 상품, 거래 목록 조회
- 관리자 전용 API 인가
- 회원, 상품, 거래 삭제
- 최근 가입 / 거래 통계 조회

## 정합성 설계

입찰은 다음 작업이 하나의 트랜잭션으로 처리되어야 합니다.

1. 상품 행 비관적 락 획득
2. 판매 완료 여부와 마감 시간 검증
3. 현재 최고 입찰 행 비관적 락 조회
4. 입찰 금액과 잔액 검증
5. 기존 최고 입찰자 환불
6. 신규 입찰자 잔액 차감
7. 신규 입찰 저장

이 흐름은 `BidService.bidProduct()`에 구현되어 있으며, 테스트로 이전 최고 입찰자 환불과 신규 입찰자 차감을 검증합니다.

## 개선한 부분

- `gradlew` 실행 권한 복구
- 테스트 전용 H2 설정 추가로 외부 MySQL 없이 `./gradlew test` 가능
- Refresh Token 재발급 시 role 누락 문제 수정
- `/api/admin/**` 인가 경로 수정
- `@EnableMethodSecurity` 추가로 `@PreAuthorize` 활성화
- 로그아웃 Access Token blacklist 구현
- 즉시구매 시 현재 최고 입찰자 환불 처리
- 판매자 본인 상품 입찰 / 구매 차단
- 타인 입찰 내역 조회 차단
- 상품 목록 최고 입찰가 batch 조회 추가
- React 상품 목록의 최고가 반복 호출 제거

## 실행 방법

### Backend

```bash
cp src/main/resources/application-example.yml src/main/resources/application.yml
./gradlew bootRun
```

`application.yml`에는 MySQL, Redis, JWT secret, mail 정보를 로컬 환경에 맞게 입력합니다.

기본 DB 이름은 `articket`입니다.

### Backend Test

```bash
./gradlew test
```

테스트는 H2 기반 `test` 프로필을 사용하므로 MySQL과 Redis를 실행하지 않아도 동작합니다.

### Frontend

```bash
cd my-react-app
npm ci
npm start
```

### Frontend Build

```bash
cd my-react-app
npm run build
```

## 포트폴리오 설명 문장

> 중고 경매 도메인에서 동시 입찰로 발생할 수 있는 최고가 오류, 중복 낙찰, 잔액 불일치 문제를 JPA 비관적 락과 트랜잭션 경계로 해결했습니다. 또한 기존 구현의 즉시구매 환불 누락, 토큰 재발급 권한 누락, 관리자 API 인가 경로 불일치 문제를 테스트와 함께 보완해 정합성과 보안 완성도를 높였습니다.

## 검증

- `./gradlew test`
- `npm run build`
