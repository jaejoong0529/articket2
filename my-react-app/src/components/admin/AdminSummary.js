import React, { useEffect, useState } from 'react';
import '../../css/Admin.css';
import { fetchSummary } from './adminService';
import {useNavigate} from "react-router-dom";

function AdminSummary() {
    const [summary, setSummary] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const loadSummary = async () => {
            try {
                const data = await fetchSummary();
                setSummary(data);
            } catch (error) {
                alert('요약 정보 조회 실패');
            }
        };
        loadSummary();
    }, []);

    if (!summary) return <div>로딩 중...</div>;

    return (
        <div className="admin-container">
            <h2>최근 7일 요약</h2>
            <p>신규 가입 회원 수: {summary.recentUsers}명</p>
            <p>신규 거래 수: {summary.recentTransactions}건</p>
            <button onClick={() => navigate(-1)}>뒤로 가기</button>
        </div>
    );
}

export default AdminSummary;
