import React, { useEffect, useState } from 'react';
import { getBoughtProducts, getSoldProducts } from './transactionService'; // 서비스 함수들 필요

function MyInfo() {
    const [bought, setBought] = useState([]);
    const [sold, setSold] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchTransactions();
    }, []);

    const fetchTransactions = async () => {
        try {
            const boughtResponse = await getBoughtProducts();
            const soldResponse = await getSoldProducts();
            setBought(boughtResponse.data);
            setSold(soldResponse.data);
        } catch (err) {
            console.error("Error fetching transactions:", err);
            setError("거래 내역을 불러오는 데 실패했습니다.");
        }
    };

    return (
        <div>
            <h2>내 정보</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}

            <h3>🛒 구매 내역</h3>
            <ul>
                {bought.map((item, index) => (
                    <li key={index}>
                        상품명: {item.productName} / 가격: {item.price} 원 / 날짜: {item.transactionDate}
                    </li>
                ))}
            </ul>

            <h3>📦 판매 내역</h3>
            <ul>
                {sold.map((item, index) => (
                    <li key={index}>
                        상품명: {item.productName} / 가격: {item.price} 원 / 날짜: {item.transactionDate}
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default MyInfo;