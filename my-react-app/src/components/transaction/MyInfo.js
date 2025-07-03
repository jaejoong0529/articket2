import React, { useEffect, useState } from 'react';
import { getBoughtProducts, getSoldProducts } from './transactionService';
import { getMyInfo } from '../member/memberService';
import '../../css/Home.css';
import { useNavigate } from 'react-router-dom';

function MyInfo() {
    const navigate = useNavigate();

    const [bought, setBought] = useState([]);
    const [sold, setSold] = useState([]);
    const [myInfo, setMyInfo] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchTransactions();
        fetchUserInfo();
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

    const fetchUserInfo = async () => {
        try {
            const response = await getMyInfo();
            setMyInfo(response);
        } catch (err) {
            console.error("Error fetching user info:", err);
            setError("내 정보를 불러오는 데 실패했습니다.");
        }
    };

    return (
        <div className="home-container">
            <header className="home-header">
                <div className="logo" onClick={() => navigate('/products')}>Articket</div>
            </header>

            <main className="main-content">
                <h1 className="main-title">👤 내 정보</h1>
                {error && <div className="error-message">{error}</div>}

                {myInfo && (
                    <div className="info-card">
                        <h3>🙋‍♂️ 사용자 정보</h3>
                        <p><strong>아이디:</strong> {myInfo.username}</p>
                        <p><strong>이름:</strong> {myInfo.name}</p>
                        <p><strong>닉네임:</strong> {myInfo.nickname}</p>
                        <p><strong>이메일:</strong> {myInfo.email}</p>
                        <p><strong>보유 금액:</strong> {myInfo.money.toLocaleString()} 원</p>
                    </div>
                )}

                <div className="info-card">
                    <h3>🛒 구매 내역</h3>
                    <ul className="transaction-list">
                        {bought.map((item, index) => (
                            <li key={index}>
                                <strong>상품명:</strong> {item.productName} /
                                <strong> 가격:</strong> {item.price.toLocaleString()} 원 /
                                <strong> 날짜:</strong> {new Date(item.transactionDate).toLocaleString()}
                            </li>
                        ))}
                    </ul>
                </div>

                <div className="info-card">
                    <h3>📦 판매 내역</h3>
                    <ul className="transaction-list">
                        {sold.map((item, index) => (
                            <li key={index}>
                                <strong>상품명:</strong> {item.productName} /
                                <strong> 가격:</strong> {item.price.toLocaleString()} 원 /
                                <strong> 날짜:</strong> {new Date(item.transactionDate).toLocaleString()}
                            </li>
                        ))}
                    </ul>
                </div>

                <div className="button-group" style={{ marginTop: '40px' }}>
                    <button onClick={() => navigate(-1)}>← 뒤로가기</button>
                </div>
            </main>
        </div>
    );
}

export default MyInfo;
