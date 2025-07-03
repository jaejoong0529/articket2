import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/Home.css';

function Home() {
    const navigate = useNavigate();

    return (
        <div className="home-container">
            {/* 헤더 */}
            <header className="home-header">
                <div className="logo" onClick={() => navigate('/')}>Articket</div>
            </header>

            {/* 메인 */}
            <main className="main-content">
                {/*  1. 메인 슬로건 */}
                <h1 className="main-title">🎨 Articket에서 중고 상품을 거래해보세요!</h1>
                <p className="subtitle">실시간 경매 시스템과 안전한 결제를 제공합니다.</p>

                {/*  4. 버튼 그룹 재배치 (회원 기능) */}
                <section className="user-access">
                    <h2>회원 기능</h2>
                    <div className="button-group">
                        <button onClick={() => navigate('/login')}>로그인</button>
                        <button onClick={() => navigate('/signup')}>회원가입</button>
                        <button onClick={() => navigate('/findUsername')}>아이디 찾기</button>
                        <button onClick={() => navigate('/findPassword')}>비밀번호 찾기</button>
                    </div>
                </section>
            </main>

            {/*  5. 푸터 */}
            <footer className="footer">
                <p>© 2025 Articket. All rights reserved.</p>
                <p>고객센터: help@articket.com</p>
            </footer>
        </div>
    );
}

export default Home;
