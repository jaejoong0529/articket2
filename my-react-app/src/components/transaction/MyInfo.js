import React, { useEffect, useState } from 'react';
import { getBoughtProducts, getSoldProducts } from './transactionService'; // 서비스 함수들 필요
import '../../css/Home.css';
import {useNavigate} from "react-router-dom";

function MyInfo() {
    const navigate = useNavigate(); // useNavigate 훅 사용

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
        <div className="home-container">
            <header className="home-header">
                <div className="logo" onClick={() => navigate('/products')}>Articket</div>
            </header>

            <main className="main-content">
                <h1 className="main-title">👤 내 정보</h1>
                {error && <p style={{color: 'red'}}>{error}</p>}

                <h3 style={{marginTop: '30px'}}>🛒 구매 내역</h3>
                <ul style={{listStyle: 'none', padding: 0}}>
                    {bought.map((item, index) => (
                        <li key={index} style={{marginBottom: '10px'}}>
                            상품명: {item.productName} / 가격: {item.price.toLocaleString()} 원 / 날짜: {item.transactionDate}
                        </li>
                    ))}
                </ul>

                <h3 style={{marginTop: '30px'}}>📦 판매 내역</h3>
                <ul style={{listStyle: 'none', padding: 0}}>
                    {sold.map((item, index) => (
                        <li key={index} style={{marginBottom: '10px'}}>
                            상품명: {item.productName} / 가격: {item.price.toLocaleString()} 원 / 날짜: {item.transactionDate}
                        </li>
                    ))}
                </ul>
                <div className="button-group">
                    <button onClick={() => navigate(-1)}>← 뒤로가기</button>
                </div>
            </main>
        </div>
    );
}

export default MyInfo;