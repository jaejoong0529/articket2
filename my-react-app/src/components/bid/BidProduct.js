import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { bidProduct, getHighestBid } from './bidService';

function BidProduct() {
    const { productId } = useParams();
    const [bidAmount, setBidAmount] = useState('');
    const [highestBid, setHighestBid] = useState(0);
    const navigate = useNavigate();

    useEffect(() => {
        getHighestBid(productId)
            .then((highestBidValue) => {
                console.log("Parsed highest bid:", highestBidValue);
                if (typeof highestBidValue === 'number' && !isNaN(highestBidValue)) {
                    setHighestBid(highestBidValue);
                } else {
                    console.error("최고 입찰가 데이터가 유효한 숫자가 아닙니다.");
                    alert("최고 입찰가 데이터를 불러오는데 실패했습니다.");
                }
            })
            .catch((error) => {
                console.error('최고 입찰가 조회 실패:', error);
                alert('최고 입찰가 조회에 실패했습니다.');
            });
    }, [productId]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await bidProduct(productId, parseInt(bidAmount, 10));
            alert('입찰이 완료되었습니다.');
            navigate(`/products/${productId}`);
        } catch (error) {
            console.error('입찰 실패:', error);
            alert('입찰에 실패했습니다.');
        }
    };

    return (
        <div>
            <h2>상품 입찰</h2>
            <p>
                {highestBid === 0 ? '현재 입찰가가 없습니다.' : `현재 최고 입찰가: ${highestBid}원`}
            </p>
            <form onSubmit={handleSubmit}>
                <input
                    type="number"
                    placeholder="입찰 금액"
                    value={bidAmount}
                    onChange={(e) => setBidAmount(e.target.value)}
                />
                <button type="submit">입찰</button>
            </form>
        </div>
    );
}

export default BidProduct;