import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { getHighestBid } from './bidService';

function HighestBid() {
    const { productId } = useParams();
    const [highestBid, setHighestBid] = useState(0);

    useEffect(() => {
        getHighestBid(productId)
            .then((response) => setHighestBid(parseInt(response.data)))
            .catch((error) => console.error('최고 입찰가 조회 실패:', error));
    }, [productId]);

    return (
        <div>
            <h2>현재 최고 입찰가: {highestBid}원</h2>
        </div>
    );
}

export default HighestBid;