import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { getUserBids } from './bidService';

function UserBids() {
    const { memberId } = useParams();
    const [userBids, setUserBids] = useState([]);

    useEffect(() => {
        getUserBids(memberId)
            .then((response) => setUserBids(response.data))
            .catch((error) => console.error('사용자 입찰 내역 조회 실패:', error));
    }, [memberId]);

    return (
        <div>
            <h2>사용자 입찰 내역</h2>
            <ul>
                {userBids.map((bid) => (
                    <li key={bid.id}>
                        상품 ID: {bid.productId}, 입찰 금액: {bid.bidAmount}원
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default UserBids;