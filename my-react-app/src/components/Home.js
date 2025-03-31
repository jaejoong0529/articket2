import React from 'react';
import { useNavigate } from 'react-router-dom';

function Home() {
    const navigate = useNavigate();

    const handleButtonClick = () => {
        navigate('/login');
    };

    const handleSignupClick = () => {
        navigate('/signup');
    };

    const handleFindUsernameClick = () => {
        navigate('/findUsername');
    };

    const handleFindPasswordClick = () => {
        navigate('/findPassword');
    };

    return (
        <div>
            <h1>Welcome to Articket2!</h1>
            <p>
                Articket2
            </p>
            <div>
                <button onClick={handleButtonClick}>로그인</button>
                <button onClick={handleSignupClick}>회원가입</button>
                <button onClick={handleFindUsernameClick}>아이디 찾기</button>
                <button onClick={handleFindPasswordClick}>비밀번호 찾기</button>
            </div>
            {/* 추가적인 기능들 (인기 공연 목록, 검색 등) */}
        </div>
    );
}

export default Home;