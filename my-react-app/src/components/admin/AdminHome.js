import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../../css/Home.css';
import { logout } from "../member/authService";

function AdminHome() {
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate("/");
    };

    return (
        <div className="home-container">
            <header className="home-header">
                <div className="logo" onClick={() => window.location.reload()}>Articket</div>
            </header>

            <main className="main-content">
                <h1 className="main-title">관리자 전용 페이지</h1>

                <div className="button-group">
                    <button onClick={() => navigate('/admin/users')}>회원 관리</button>
                    <button onClick={() => navigate('/admin/products')}>상품/경매 관리</button>
                    <button onClick={() => navigate('/admin/transactions')}>거래내역 조회</button>
                </div>

                <div className="button-group" style={{ marginTop: '20px' }}>
                    <button onClick={() => navigate('/admin/members/search')}>🔍 회원 검색</button>
                    <button onClick={() => navigate('/admin/products/filter')}>📦 상품 필터링</button>
                    <button onClick={() => navigate('/admin/summary')}>📊 주간 요약 통계</button>
                </div>

                <button onClick={handleLogout} style={{ marginTop: '30px' }}>로그아웃</button>
            </main>
        </div>
    );
}

export default AdminHome;
