import React from 'react';
import { useNavigate } from 'react-router-dom';
import { getAccessToken } from './member/authService';
import '../css/Home.css';

function Home() {
    const navigate = useNavigate();
    const isLoggedIn = Boolean(getAccessToken());

    return (
        <div className="app-shell home-container">
            <header className="home-header app-header">
                <button className="logo brand-button" onClick={() => navigate('/')}>Articket</button>
                <nav className="header-actions" aria-label="주요 메뉴">
                    {isLoggedIn ? (
                        <>
                            <button className="ghost-button" onClick={() => navigate('/products/create')}>상품 등록</button>
                            <button className="ghost-button" onClick={() => navigate('/myinfo')}>내 거래</button>
                            <button className="primary-button" onClick={() => navigate('/products')}>경매 보기</button>
                        </>
                    ) : (
                        <>
                            <button className="ghost-button" onClick={() => navigate('/login')}>로그인</button>
                            <button className="primary-button" onClick={() => navigate('/signup')}>회원가입</button>
                        </>
                    )}
                </nav>
            </header>

            <main className="home-main">
                <section className="home-hero">
                    <div className="home-hero-copy">
                        <p className="eyebrow">SECONDHAND AUCTION</p>
                        <h1 className="main-title">Articket</h1>
                        <p className="subtitle">입찰, 즉시구매, 거래 내역까지 이어지는 중고 경매 마켓</p>
                        <div className="hero-actions">
                            <button className="primary-button" onClick={() => navigate(isLoggedIn ? '/products' : '/login')}>
                                {isLoggedIn ? '경매 둘러보기' : '로그인하고 시작'}
                            </button>
                            <button className="secondary-button" onClick={() => navigate(isLoggedIn ? '/products/create' : '/signup')}>
                                {isLoggedIn ? '상품 등록' : '계정 만들기'}
                            </button>
                        </div>
                    </div>

                    <div className="auction-preview" aria-label="경매 상태">
                        <div className="preview-row">
                            <span>현재 최고가</span>
                            <strong>248,000원</strong>
                        </div>
                        <div className="preview-row">
                            <span>즉시구매가</span>
                            <strong>310,000원</strong>
                        </div>
                        <div className="bid-ladder" aria-hidden="true">
                            <span style={{ height: '38%' }}></span>
                            <span style={{ height: '58%' }}></span>
                            <span style={{ height: '78%' }}></span>
                            <span style={{ height: '92%' }}></span>
                        </div>
                        <div className="preview-footer">
                            <span>마감 임박</span>
                            <strong>00:42:19</strong>
                        </div>
                    </div>
                </section>

                <section className="quick-panel" aria-label="빠른 이동">
                    <button onClick={() => navigate('/products')}>
                        <span>경매 목록</span>
                        <strong>진행 중인 상품</strong>
                    </button>
                    <button onClick={() => navigate('/recharge')}>
                        <span>잔액 충전</span>
                        <strong>입찰 준비</strong>
                    </button>
                    <button onClick={() => navigate('/myinfo')}>
                        <span>내 거래</span>
                        <strong>구매와 판매</strong>
                    </button>
                    <button onClick={() => navigate('/findPassword')}>
                        <span>계정 찾기</span>
                        <strong>비밀번호 재설정</strong>
                    </button>
                </section>
            </main>
        </div>
    );
}

export default Home;
