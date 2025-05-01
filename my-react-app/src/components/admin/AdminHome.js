import React from 'react';
import { useNavigate } from 'react-router-dom';

function AdminHome() {
    const navigate = useNavigate();

    return (
        <div style={{ padding: '20px' }}>
            <h1>관리자 홈</h1>
            <div style={{ marginTop: '20px' }}>
                <button onClick={() => navigate('/admin/users')} style={{ marginRight: '10px' }}>
                    회원 관리
                </button>
                <button onClick={() => navigate('/admin/products')} style={{ marginRight: '10px' }}>
                    상품/경매 관리
                </button>
                <button onClick={() => navigate('/admin/transactions')}>
                    거래내역 조회
                </button>
            </div>
        </div>
    );
}

export default AdminHome;
