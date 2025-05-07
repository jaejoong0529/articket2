import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/Home.css';

function Home() {
    const navigate = useNavigate();

    return (
        <div className="home-container">
            <header className="home-header">
                <div className="logo" onClick={() => window.location.reload()}>Articket</div>
            </header>

            <main className="main-content">
                <h1 className="main-title">Articket에서 중고 거래 경매 하세요!</h1>
                <div className="search-box">
                    <input type="text" placeholder="검색어를 입력해주세요" />
                    <button className="search-btn">🔍</button>
                </div>
                <div className="button-group">
                    <button onClick={() => navigate('/login')}>로그인</button>
                    <button onClick={() => navigate('/signup')}>회원가입</button>
                    <button onClick={() => navigate('/findUsername')}>아이디 찾기</button>
                    <button onClick={() => navigate('/findPassword')}>비밀번호 찾기</button>
                </div>
            </main>
        </div>
    );
}

export default Home;
