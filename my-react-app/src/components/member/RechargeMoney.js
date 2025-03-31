import React, { useState } from "react";
import axios from "axios";
import { getAccessToken } from "./authService";
import { useNavigate } from "react-router-dom"; // useNavigate 훅 import

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
            const response = await axios.post(
                "http://localhost:8080/recharge",
                { amount: amountNumber },
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                }
            );
            alert("돈 충전이 완료되었습니다.");
            navigate("/products"); // 상품 목록 페이지로 이동
        } catch (error) {
            console.error("Error recharging money:", error);
            alert("돈 충전에 실패했습니다.");
        }
    };

    return (
        <div>
            <h2>돈 충전</h2>
            <input
                type="text" // 숫자 포맷팅을 위해 type을 text로 변경
                placeholder="충전할 금액"
                value={amount}
                onChange={handleAmountChange} // 입력 값 변경 시 포맷팅 적용
            />
            <button onClick={handleRecharge}>충전</button>
        </div>
    );
}

export default RechargeMoney;