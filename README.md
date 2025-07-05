# articket-입찰 기반 중고거래 플랫폼

## 주요기능
### 사용자
- 회원가입/로그인(JWT인증)
- 이메일로 아이디/임시 비밀번호 찾기
- 비밀번호 변경
- 보유 금액 충전

### 상품
- 상품 등록/ 조회/ 삭제
- 상품 입찰/입찰 금액 검증
- 최고 입찰자 자동 낙찰/ 잔액 차감/실패자 환불

### 거래
-낙찰 완료 시 자동 거래 생성
-내 거래 내역 확인(구매/판매)

## 사용기술
### Backend
-Java 17/ Spring Boot 3
-Spring Security +JWT 인증
-JPA(Hibernate)+MariaDB
-JavaMailSender(Gmail SMTP)

### Frontend
-React 

## 시연 화면
![image](https://github.com/user-attachments/assets/59bec9eb-95cf-4c6c-ae63-addb0497abf6)
![image](https://github.com/user-attachments/assets/a19bf731-f729-4be1-8676-ce39f6cfcad3)
