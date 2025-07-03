import React, { useState } from "react";
import axios from "axios";
import { getAccessToken } from "./authService";
import { useNavigate } from "react-router-dom"; // useNavigate 훅 import
import '../../css/Home.css';

function RechargeMoney() {
    const [amount, setAmount] = useState(""); // 문자열로 관리
    const navigate = useNavigate(); // useNavigate 훅 사용

    const formatNumber = (value) => {
        const numericValue = value.replace(/[^0-9]/g, ''); // 숫자만 추출
        return numericValue.replace(/\B(?=(\d{3})+(?!\d))/g, ','); // 3자리마다 콤마 추가
    };

    const handleAmountChange = (e) => {
        setAmount(formatNumber(e.target.value));
    };

    const handleRecharge = async () => {
        const token = getAccessToken();
        const amountNumber = parseInt(amount.replace(/,/g, ''), 10); // 콤마 제거 후 숫자로 변환
        try {
            await axios.post(
                "http://localhost:8080/api/members/recharge",
                { amount: amountNumber },
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                }
            );
            alert("돈 충전이 완료되었습니다.");
            navigate("/products");
        } catch (error) {
            const errorMessage = error.response?.data || '충전 실패했습니다.';
            alert(`충전 실패: ${errorMessage}`);
        }
    };


    return (
        <div className="home-container">
            <header className="home-header">
                <div className="logo" onClick={() => navigate('/products')}>Articket</div>
            </header>

            <main className="main-content">
                <h1 className="main-title">💰 돈 충전</h1>
                <input
                    type="text"
                    placeholder="충전할 금액 입력"
                    value={amount}
                    onChange={handleAmountChange}
                    style={{padding: '10px', fontSize: '16px', marginBottom: '20px', width: '200px'}}
                />
                <div className="button-group">
                    <button onClick={handleRecharge}>충전</button>
                    <button onClick={() => navigate(-1)}>← 뒤로가기</button>
                </div>
            </main>
        </div>
    );
}

export default RechargeMoney;
