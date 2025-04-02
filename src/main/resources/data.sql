INSERT INTO member (name, username, password, nickname, email, phone_Number, role, money, date_joined, last_login)
VALUES (
           '관리자', -- name
           'admin', -- username
           '$2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', -- password (해싱된 비밀번호)
           'adminNick', -- nickname
           'admin@example.com', -- email
           '010-1234-5678', -- phoneNumber
           'ROLE_ADMIN', -- role (Enum 타입이므로 문자열로 지정)
           0, -- money
           NOW(), -- date_joined (현재 시간)
           NOW()  -- last_login (현재 시간)
       );